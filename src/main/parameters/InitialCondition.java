package main.parameters;

import java.util.Collection;

import main.Particle;

public interface InitialCondition {
	
	Collection<Particle> generateParticles(SimulationOptions opts);

}
