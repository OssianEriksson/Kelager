package main.collision;

import main.Vec;

public class CollisionData {

	public final double F;
	public final Vec cp;
	public final double d;
	public final Vec n;

	public CollisionData(double F, Vec cp, double d, Vec n) {
		this.F = F;
		this.cp = cp;
		this.d = d;
		this.n = n;
	}
	
	public CollisionData(double F) {
		this(F, null, Double.NaN, null);
	}

}
