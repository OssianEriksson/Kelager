package main.integrators;

import main.Particle;
import main.Vec;

public interface Integrator {
	
	public void integrate(Particle[] particles, double dt);

	public void setVelocity(int index, Vec u);
	
	public Vec getVelocity(int index);
	
}
