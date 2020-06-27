package main;

public class Utils {
	
	public static int mod(int a, int b) {
		a %= b;
		return a < 0 ? a + b : a;
	}
	
	public static double clamp(double a, double m) {
		if (a <= -m) {
			return -m;
		} else if (a >= m) {
			return m;
		}
		return m;
	}
	
	public static double clamp(double a, double min, double max) {
		if (a <= min) {
			return min;
		} else if (a >= max) {
			return max;
		}
		return a;
	}
	
	public static int sign(double a) {
		return a == 0 ? 0 : (a > 0 ? 1 : -1);
	}

	public static int signNoZero(double a) {
		return a > 0 ? 1 : -1;
	}
	
	public static int nextPrime(int x) {
		loop:
		for (int p = x + 1; ; p++) {
			if (p == 1 || p % 2 == 0) {
				continue;
			}
			int D = (int) Math.ceil(Math.sqrt(p));
			for (int d = 3; d <= D; d += 2) {
				if (p % d == 0) {
					continue loop;
				}
			}
			return p;
		}
	}

}
