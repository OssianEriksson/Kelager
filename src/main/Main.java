package main;

import static main.Vec.*;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

import main.collision.CollisionCapsule;
import main.collision.CollisionObject;
import main.parameters.FluidParameters;
import main.parameters.InitialCondition;
import main.parameters.SimulationOptions;
import main.parameters.SimulationParameters;
import main.parameters.SphereInitialCondition;
import main.rendering.Renderer;
import main.rendering.StaticSwingRenderer;

public class Main {

	public static void main(String[] args) {
		// See http://www.glowinggoo.com/sph/bin/kelager.06.pdf for definition of fluid and simulation parameters
		
		SimulationParameters simulationParameters = new SimulationParameters(
				0.5, // Controls particle mass (and hence also size)
				40,
				0.01,
				vec(0, 1, 0) // Gravity
		);
		
		FluidParameters fluidParameters = FluidParameters.WATER;
		
		InitialCondition initialCondition = new SphereInitialCondition(
				vec(), // Sphere center
				simulationParameters,
				fluidParameters,
				500, // Approximate number of particles
				vec(0.5, -1, -0.5) // Initial velocity of particles
		);
		
		CollisionObject collisionObject = new CollisionCapsule(
				vec(0, 0.5, 0), // 1:st end of capsule
				vec(0, -0.5, 0), // 2:nd end of capsule
				0.3, // Radius of capsule
				true // True if particles collides with inside, false if particles collide with outside
		);

		SimulationOptions opts = new SimulationOptions(simulationParameters, fluidParameters, initialCondition,
				collisionObject);

		FluidEngine fluidEngine = new FluidEngine(opts);

		fluidEngine.init();

		Quaternionf rotation = new Quaternionf().rotateXYZ((float) Math.toRadians(-15), 0, 0); // Controls camera rotation
		Matrix4f viewMatrix = new Matrix4f().rotate(rotation).translate(0, 0.5f, -1.2f); // Controls camera position
		Matrix4f projectionMatrix = new Matrix4f().setPerspective((float) Math.toRadians(90), 1, 0.1f, 3f);
		Matrix4f viewProjectionMatrix = new Matrix4f();
		projectionMatrix.mul(viewMatrix, viewProjectionMatrix);

		Renderer renderer = new StaticSwingRenderer(viewProjectionMatrix, fluidEngine.getV(), 0.7);

		renderer.init(fluidEngine.getParticles(), opts);

		Timer timer = new Timer(opts.simulationParameters.dt);

		int i = 0;
		while (true) {
			// Render every 4:th frame for better(?) performance
			if (i == 0) {
				renderer.render();
			}
			i = (i + 1) % 4;
			
			fluidEngine.tick();
			
			timer.sleep();
		}
	}

}
