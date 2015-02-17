package at.monol1th.pic1.core.particles.movement;

import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 14.02.2015.
 */
public class RelativisticLeapFrogMover implements IParticleMover {
	public void updateParticle(Particle p, double dt, double c) {
		p.px += dt * p.q * p.Ex;

		double bu = p.px / (p.m * c);
		double gamma = Math.sqrt(1.0 + bu * bu);

		p.vx = bu * c / gamma;

		p.x += dt * p.vx;
	}

	public void initializeParticle(Particle p, double dt, double c) {
		p.px += 0.5 * dt * p.q * p.Ex;

		double bu = p.px / (p.m * c);
		double gamma = Math.sqrt(1.0 + bu * bu);

		p.vx = bu * c / gamma;
	}
}
