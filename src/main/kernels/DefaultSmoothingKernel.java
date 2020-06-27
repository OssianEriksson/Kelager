package main.kernels;

import static main.Vec.*;

import main.Vec;

public class DefaultSmoothingKernel implements SmoothingKernel {

	private final double C1, C2, C3;
	private final double h2;

	public DefaultSmoothingKernel(double h) {
		this.h2 = h * h;

		C1 = 315.0 / (64 * Math.PI * Math.pow(h, 9));
		C2 = -945.0 / (32 * Math.PI * Math.pow(h, 9));
		C3 = C2;
	}

	@Override
	public double value(Vec r) {
		double r2 = length2(r);

		if (r2 >= h2) {
			return 0;
		}

		double a = h2 - r2;
		return C1 * a * a * a;
	}

	@Override
	public Vec gradient(Vec r) {
		double r2 = length2(r);

		if (r2 >= h2) {
			return vec();
		}

		double a = h2 - r2;
		return mul(C2 * a * a, r);
	}

	@Override
	public double laplacian(Vec r) {
		double r2 = length2(r);

		if (r2 >= h2) {
			return 0;
		}

		return C3 * (h2 - r2) * (3 * h2 - 7 * r2);
	}

}
