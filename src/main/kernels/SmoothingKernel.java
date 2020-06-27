package main.kernels;

import main.Vec;

public interface SmoothingKernel {
	
	public static final double EPS = 1e-8;
	public static final double INF = 1e3;
	
	public double value(Vec r);
	
	public Vec gradient(Vec r);
	
	public double laplacian(Vec r);

}
