package main.kernels;

import static main.Vec.*;

import main.Vec;

import static main.Utils.*;

public class PressureSmoothingKernel implements SmoothingKernel {

	private final double h;
	private final double C1, C2, C3;

	public PressureSmoothingKernel(double h) {
		this.h = h;

		C1 = 15.0 / (Math.PI * Math.pow(h, 6));
		C2 = -45.0 / (Math.PI * Math.pow(h, 6));
		C3 = -90.0 / (Math.PI * Math.pow(h, 6));
	}

	@Override
	public double value(Vec r) {
		double l = length(r);

		if (l >= h) {
			return 0;
		}

		double a = h - l;
		return C1 * a * a * a;
	}

	@Override
	public Vec gradient(Vec r) {
		double l = length(r);

		if (l >= h) {
			return vec();
		}

		double a = h - l;

		if (l < EPS) {
			return mul(C2, signNoZero(r));
		}

		return mul(C2 * a * a / l, r);
	}

	@Override
	public double laplacian(Vec r) {
		double l = length(r);

		if (l >= h) {
			return 0;
		} else if (l == 0) {
			return -INF;
		}

		return clamp(C3 * (h - l) * (h - 2 * l) / l, INF);
	}

}
