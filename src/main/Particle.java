package main;

import static main.Vec.*;

import java.util.Collection;

import main.parameters.SimulationOptions;

public class Particle {

	private static final int P1 = 73856093, P2 = 19349633, P3 = 83492781;

	public final Vec r, u, a;
	public final Vec F = vec();
	
	public Collection<Particle> neighbors;
	
	private final double l;
	public final double m;
	
	public double rho;
	public double p;

	public Particle(Vec position, Vec velocity, SimulationOptions opts) {
		this.r = vec(position);
		this.u = vec(velocity);
		
		a = vec();

		m = opts.simulationParameters.particleMass;
		l = Math.sqrt(opts.fluidParameters.density / opts.simulationParameters.kernelParticles);
	}
	
	@Override
	public int hashCode() {
		return hash(r, l);
	}
	
	public static int hash(Vec r, double l) {
		int rHatX = (int) Math.floor(r.x / l);
		int rHatY = (int) Math.floor(r.y / l);
		int rHatZ = (int) Math.floor(r.z / l);
		
		return hash(rHatX, rHatY, rHatZ);
	}
	
	public static int hash(int rHatX, int rHatY, int rHatZ) {
		return (rHatX * P1) ^ (rHatY * P2) ^ (rHatZ * P3);
	}

}
