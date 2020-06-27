package main;

public class Vec {

	public double x, y, z;

	public Vec(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vec set(Vec a) {
		x = a.x;
		y = a.y;
		z = a.z;
		return this;
	}

	public Vec add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vec add(Vec a) {
		x += a.x;
		y += a.y;
		z += a.z;
		return this;
	}

	public Vec mul(double a) {
		x *= a;
		y *= a;
		z *= a;
		return this;
	}

	@Override
	public String toString() {
		return "[" + x + "\t" + y + "\t" + z + "]";
	}

	public static Vec vec() {
		return new Vec(0, 0, 0);
	}

	public static Vec vec(double x, double y, double z) {
		return new Vec(x, y, z);
	}

	public static Vec vec(Vec a) {
		return new Vec(a.x, a.y, a.z);
	}

	public static double length2(Vec a) {
		return a.x * a.x + a.y * a.y + a.z * a.z;
	}

	public static double length(Vec a) {
		return Math.sqrt(length2(a));
	}

	public static double dist2(Vec a, Vec b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		double dz = a.z - b.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public static double dist(Vec a, Vec b) {
		return Math.sqrt(dist2(a, b));
	}

	public static Vec mul(double a, Vec b) {
		return new Vec(a * b.x, a * b.y, a * b.z);
	}

	public static Vec add(Vec a, Vec b) {
		return new Vec(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	public static Vec sub(Vec a, Vec b) {
		return new Vec(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	public static double dot(Vec a, Vec b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public static Vec signNoZero(Vec a) {
		return new Vec(Utils.signNoZero(a.x), Utils.signNoZero(a.y), Utils.signNoZero(a.z));
	}

	public static Vec clamp(Vec a, double m) {
		return new Vec(Utils.clamp(a.x, m), Utils.clamp(a.y, m), Utils.clamp(a.z, m));
	}

}
