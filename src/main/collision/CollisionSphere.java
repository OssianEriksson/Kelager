package main.collision;

import static main.Vec.*;

import main.Vec;

import static main.Utils.*;

public class CollisionSphere implements CollisionObject {
	
	private final Vec c;
	private final double r, r2;
	private final boolean insideOut;
	
	public CollisionSphere(Vec center, double radius, boolean insideOut) {
		c = center;
		r = radius;
		r2 = r * r;
		this.insideOut = insideOut;
	}

	@Override
	public CollisionData test(Vec x) {
		double dist2 = dist2(x, c);
		
		double F = insideOut ? r2 - dist2 : dist2 - r2;
		if (F > 0) {
			return new CollisionData(F);
		}
		
		double dist = Math.sqrt(dist2);
		
		double d = Math.abs(dist - r);
		
		Vec cp, n;
		if (dist == 0) {
			cp = vec(r, 0, 0);
			n = vec(1, 0, 0);
		} else {
			double x0 = (x.x - c.x) / dist;
			double y0 = (x.y - c.y) / dist;
			double z0 = (x.z - c.z) / dist;

			cp = vec(c.x + r * x0, c.y + r * y0, c.z + r * z0);
			
			double _F = -signNoZero(F);
			n =  vec(_F * x0, _F * y0, _F * z0);
		}
		
		return new CollisionData(F, cp, d, n);
	}
	
	

}
