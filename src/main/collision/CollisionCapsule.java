package main.collision;

import static main.Utils.clamp;
import static main.Utils.signNoZero;
import static main.Vec.*;

import main.Vec;

public class CollisionCapsule implements CollisionObject {

	private final Vec p0;
	private final Vec u, v;
	private final double u2;
	private final double r, r2;
	private final boolean insideOut;

	public CollisionCapsule(Vec p0, Vec p1, double radius, boolean insideOut) {
		this.p0 = p0;
		r = radius;
		r2 = r * r;
		this.insideOut = insideOut;

		u = sub(p1, p0);
		u2 = length2(u);
		v = mul(-1 / u2, u);
	}

	@Override
	public CollisionData test(Vec x) {
		double t = clamp((p0.x - x.x) * v.x + (p0.y - x.y) * v.y + (p0.z - x.z) * v.z, 0, 1);
		Vec q = vec(u).mul(t).add(p0);

		double dist2 = dist2(x, q);

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
			double x0 = (x.x - q.x) / dist;
			double y0 = (x.y - q.y) / dist;
			double z0 = (x.z - q.z) / dist;

			cp = vec(q.x + r * x0, q.y + r * y0, q.z + r * z0);
			
			double _F = -signNoZero(F);
			n =  vec(_F * x0, _F * y0, _F * z0);
		}
		
		return new CollisionData(F, cp, d, n);
	}

}
