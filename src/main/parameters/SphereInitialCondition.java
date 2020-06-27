package main.parameters;

import static main.Vec.*;

import java.util.ArrayList;
import java.util.Collection;

import main.Particle;
import main.Vec;

public class SphereInitialCondition implements InitialCondition {

	private final Vec center;
	private final Vec initialVelocity;
	private final double V;
	private final int approxParticleCount;

	public SphereInitialCondition(Vec center, SimulationParameters simulationParameters, FluidParameters fluidParameters, int approxParticleCount, Vec initialVelocity) {
		this.center = center;
		this.initialVelocity = initialVelocity;
		this.approxParticleCount = approxParticleCount;
		
		V = simulationParameters.particleMass * approxParticleCount / fluidParameters.density;
	}

	@Override
	public Collection<Particle> generateParticles(SimulationOptions opts) {
		double radius = Math.pow(3 * V / (4 * Math.PI), 1.0 / 3.0);
		double r = Math.pow(V / approxParticleCount, 1.0 / 3.0) / 2;
		
		Collection<Particle> particles = new ArrayList<Particle>();

		for (double x = center.x - radius + r; x < center.x + radius; x += 2 * r) {
			for (double y = center.y - radius + r; y < center.y + radius; y += 2 * r) {
				for (double z = center.z - radius + r; z < center.z + radius; z += 2 * r) {
					Vec position = vec(x, y, z);

					if (dist(position, center) > radius) {
						continue;
					}

					particles.add(new Particle(position, initialVelocity, opts));
				}
			}
		}

		return particles;
	}

}
