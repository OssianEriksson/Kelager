package main.parameters;

import java.awt.Color;

public class FluidParameters {

	public static FluidParameters MUCUS = new FluidParameters(1000, 0, 36, 6, 5, 0.5, new Color(50, 220, 100));
	public static FluidParameters WATER = new FluidParameters(998.29, 0, 3.5, 0.0728, 3, 0, new Color(50, 100, 220));
	public static FluidParameters STEAM = new FluidParameters(0.59, 5, 0.01, 0, 4, 0, new Color(150, 150, 150));

	public double density;
	public double buoyancyDiffusion;
	public double dynamicViscosity;
	public double surfaceTension;
	public double gasStiffness;
	public double restitution;
	public Color color;

	public FluidParameters(double density, double buoyancyDiffusion, double dynamicViscosity, double surfaceTension,
			double gasStiffness, double restitution, Color color) {
		this.density = density;
		this.buoyancyDiffusion = buoyancyDiffusion;
		this.dynamicViscosity = dynamicViscosity;
		this.surfaceTension = surfaceTension;
		this.gasStiffness = gasStiffness;
		this.restitution = restitution;
		this.color = color;
	}

}
