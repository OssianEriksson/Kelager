package main.rendering;

import main.Particle;
import main.parameters.SimulationOptions;

public interface Renderer {
	
	public void init(Particle[] particles, SimulationOptions opts);
	
	public void render();
	
	public void delete();

}
