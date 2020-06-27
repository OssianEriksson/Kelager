package main.integrators;

import static main.Vec.*;

import main.Particle;
import main.Vec;

public class LeapfrogIntegrator implements Integrator {

	private Vec[] uPrevious = null, uNext = null;

	@Override
	public void integrate(Particle[] particles, double dt) {
		if (uPrevious == null || uNext == null) {
			uPrevious = new Vec[particles.length];
			uNext = new Vec[particles.length];

			for (int i = 0; i < particles.length; i++) {
				uPrevious[i] = vec();
				uNext[i] = add(particles[i].u, mul(dt / 2, particles[i].a));
			}
		}

		for (int i = 0; i < particles.length; i++) {
			uPrevious[i].set(uNext[i]);

			double uNextX = uPrevious[i].x + dt * particles[i].a.x;
			double uNextY = uPrevious[i].y + dt * particles[i].a.y;
			double uNextZ = uPrevious[i].z + dt * particles[i].a.z;

			uNext[i].set(uNextX, uNextY, uNextZ);

			particles[i].r.add(dt * uNextX, dt * uNextY, dt * uNextZ);
			particles[i].u.set(getVelocity(i));
		}
	}

	@Override
	public void setVelocity(int index, Vec u) {
		uNext[index].set(u);
	}

	@Override
	public Vec getVelocity(int index) {
		Vec uPrev = this.uPrevious[index];
		Vec uNext = this.uNext[index];

		return vec((uPrev.x + uNext.x) / 2, (uPrev.y + uNext.y) / 2, (uPrev.z + uNext.z) / 2);
	}

}
