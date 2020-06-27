package main;

import static main.Vec.*;

import java.util.Collection;
import java.util.LinkedList;

import main.collision.CollisionData;
import main.integrators.Integrator;
import main.integrators.LeapfrogIntegrator;
import main.kernels.DefaultSmoothingKernel;
import main.kernels.PressureSmoothingKernel;
import main.kernels.SmoothingKernel;
import main.kernels.ViscositySmoothingKernel;
import main.parameters.SimulationOptions;

public class FluidEngine {

	private final SimulationOptions opts;

	private Particle[] particles;
	private HashTable<Particle> hashTable;
	private SmoothingKernel WDefault, WPressure, WViscosity;
	private Integrator integrator;

	private Vec g;
	private double h, h2;
	private double V;
	private double x;
	private double l, l2;
	private double k;
	private double mu;
	private double b;
	private double sigma;
	private double rho0;
	private double dt;
	private double cR;
	private int n;

	public FluidEngine(SimulationOptions opts) {
		this.opts = opts;
	}

	public void init() {
		initializeSPHSystem();
	}

	public void tick() {
		resetSimulationStepValues();
		computeDensityAndPressure();
		computeInternalForces();
		computeExternalForces();
		timeIntegrationAndCollisionHandling();
	}

	private void initializeSPHSystem() {
		Collection<Particle> particleCollection = opts.initialCondition.generateParticles(opts);
		particles = particleCollection.toArray(new Particle[particleCollection.size()]);

		n = particles.length;
		x = opts.simulationParameters.kernelParticles;
		k = opts.fluidParameters.gasStiffness;
		rho0 = opts.fluidParameters.density;
		mu = opts.fluidParameters.dynamicViscosity;
		sigma = opts.fluidParameters.surfaceTension;
		b = opts.fluidParameters.buoyancyDiffusion;
		dt = opts.simulationParameters.dt;
		cR = opts.fluidParameters.restitution;
		g = opts.simulationParameters.gravity;
		V = getVolume(particles, opts);
		h = Math.pow(3 * V * x / (4 * Math.PI * n), 1.0 / 3.0);
		h2 = h * h;
		l2 = opts.fluidParameters.density / opts.simulationParameters.kernelParticles;
		l = Math.sqrt(l2);

		WDefault = new DefaultSmoothingKernel(h);
		WPressure = new PressureSmoothingKernel(h);
		WViscosity = new ViscositySmoothingKernel(h);

		hashTable = new HashTable<Particle>(Utils.nextPrime(2 * n - 1));

		integrator = new LeapfrogIntegrator();
	}

	private void resetSimulationStepValues() {
		hashTable.clear();

		for (Particle p : particles) {
			hashTable.add(p);
		}

		for (Particle i : particles) {
			i.neighbors = findNeighbors(i);

			i.rho = 0;
			i.F.set(0, 0, 0);
		}
	}

	private void computeDensityAndPressure() {
		for (Particle i : particles) {
			for (Particle j : i.neighbors) {
				i.rho += j.m * WDefault.value(sub(i.r, j.r));
			}

			i.p = k * (i.rho - rho0);
		}
	}

	private Collection<Particle> findNeighbors(Particle p) {
		Collection<Particle> neighbors = new LinkedList<Particle>();

		int BBMinX = (int) Math.floor((p.r.x - h) / l);
		int BBMinY = (int) Math.floor((p.r.y - h) / l);
		int BBMinZ = (int) Math.floor((p.r.z - h) / l);

		int BBMaxX = (int) Math.floor((p.r.x + h) / l);
		int BBMaxY = (int) Math.floor((p.r.y + h) / l);
		int BBMaxZ = (int) Math.floor((p.r.z + h) / l);
		
		for (int x = BBMinX; x <= BBMaxX; x++) {
			for (int y = BBMinY; y <= BBMaxY; y++) {
				for (int z = BBMinZ; z <= BBMaxZ; z++) {
					Collection<Particle> c = hashTable.get(Particle.hash(x, y, z));
					
					for (Particle n : c) {
						if (dist2(p.r, n.r) <= h2) {
							neighbors.add(n);
						}
					}
				}
			}
		}
		
		return neighbors;
	}

	private void computeInternalForces() {
		for (Particle i : particles) {
			Vec fPressure = vec(), fViscosity = vec();

			for (Particle j : i.neighbors) {
				if (j.equals(i)) {
					continue;
				}

				double a = (i.p / (i.rho * i.rho) + j.p / (j.rho * j.rho)) * j.m;
				fPressure.add(mul(a, WPressure.gradient(sub(i.r, j.r))));
				
				double b = j.m * WViscosity.laplacian(sub(i.r, j.r)) / j.rho;
				fViscosity.add(mul(b, sub(j.u, i.u)));
			}

			fPressure.mul(-i.rho);
			fViscosity.mul(mu);

			i.F.add(fPressure);
			i.F.add(fViscosity);
		}
	}

	private void computeExternalForces() {
		for (Particle i : particles) {
			Vec fGravity = mul(i.rho, g);

			Vec n = vec();
			double laplaceC = 0;
			for (Particle j : i.neighbors) {
				double a = j.m / j.rho;
				Vec d = sub(i.r, j.r);
				n.add(mul(a, WDefault.gradient(d)));
				laplaceC += a * WDefault.laplacian(d);
			}

			double n2 = length2(n);
			Vec fSurface = n2 >= l2 ? mul(-sigma * laplaceC / Math.sqrt(n2), n) : vec();

			Vec fBuoyancy = mul(b * (i.rho - rho0), g);

			i.F.add(fGravity);
			i.F.add(fSurface);
			i.F.add(fBuoyancy);
		}
	}

	private void timeIntegrationAndCollisionHandling() {
		for (Particle i : particles) {
			if (i.rho != 0) {
				i.a.set(mul(1 / i.rho, i.F));
			}
		}
		
		integrator.integrate(particles, dt);
		
		for (int i = 0; i < particles.length; i++) {
			CollisionData collision = opts.collisionObject.test(particles[i].r);
			
			if (collision.F >= 0) {
				continue;
			}
			
			double lu = length(particles[i].u);
			
			if (lu == 0) {
				continue;
			}
			
			particles[i].r.set(collision.cp);
			
			double a = (1 + cR * collision.d / (dt * lu)) * dot(particles[i].u, collision.n);
			double uX = particles[i].u.x - a * collision.n.x;
			double uY = particles[i].u.y - a * collision.n.y;
			double uZ = particles[i].u.z - a * collision.n.z;
			
			integrator.setVelocity(i, vec(uX, uY, uZ));
			particles[i].u.set(integrator.getVelocity(i));
		}
	}

	private double getVolume(Particle[] particles, SimulationOptions opts) {
		double V = 0;

		for (Particle particle : particles) {
			V += particle.m / rho0;
		}

		return V;
	}

	public Particle[] getParticles() {
		if (particles == null) {
			throw new RuntimeException("Call to init() must precede call to getParticles()");
		}
		return particles;
	}

	public double getV() {
		if (particles == null) {
			throw new RuntimeException("Call to init() must precede call to getV()");
		}
		return V;
	}
	
}
