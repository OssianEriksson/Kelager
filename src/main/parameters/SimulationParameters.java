package main.parameters;

import main.Vec;

public class SimulationParameters {

	public double particleMass;
	public double kernelParticles;
	public double dt;
	public Vec gravity;

	public SimulationParameters(double particleMass, double kernelParticles, double dt, Vec gravity) {
		this.particleMass = particleMass;
		this.kernelParticles = kernelParticles;
		this.dt = dt;
		this.gravity = gravity;
	}

}
