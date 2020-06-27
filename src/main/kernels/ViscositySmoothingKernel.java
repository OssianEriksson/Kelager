package main.kernels;

import static main.Vec.*;

import main.Vec;

import static main.Utils.*;

public class ViscositySmoothingKernel implements SmoothingKernel {

	private final double h, h2, h3;
	private final double C1, C2, C3;

	public ViscositySmoothingKernel(double h) {
		this.h = h;
		this.h2 = h * h;
		this.h3 = h * h * h;

		C1 = 15.0 / (2 * Math.PI * h3);
		C2 = C1;
		C3 = 45.0 / (Math.PI * Math.pow(h, 6));
	}

	@Override
	public double value(Vec r) {
		double l = length(r);

		if (l >= h) {
			return 0;
		} else if (l == 0) {
			return INF;
		}

		double l2 = l * l;
		double l3 = l2 * l;

		return clamp(C1 * (-l3 / (2 * h3) + l2 / h2 + h / (2 * l) - 1), INF);
	}

	@Override
	public Vec gradient(Vec r) {
		double l = length(r);

		if (l >= h) {
			return vec();
		} else if (l == 0) {
			return mul(-INF, signNoZero(r));
		}

		double l3 = l * l * l;

		return clamp(mul(C2 * (-3 * l / (2 * h3) + 2 / h2 - h / (2 * l3)), r), INF);
	}

	@Override
	public double laplacian(Vec r) {
		double l = length(r);

		if (l >= h) {
			return 0;
		}
		
		return C3 * (h - l);
	}

}
