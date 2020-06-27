package main.parameters;

import main.collision.CollisionObject;

public class SimulationOptions {

	public SimulationParameters simulationParameters;
	public FluidParameters fluidParameters;
	public InitialCondition initialCondition;
	public CollisionObject collisionObject;

	public SimulationOptions(SimulationParameters simulationParameters, FluidParameters fluidParameters,
			InitialCondition initialCondition, CollisionObject collisionObject) {
		this.simulationParameters = simulationParameters;
		this.fluidParameters = fluidParameters;
		this.initialCondition = initialCondition;
		this.collisionObject = collisionObject;
	}

}
