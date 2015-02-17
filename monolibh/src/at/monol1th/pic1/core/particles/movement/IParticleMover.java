package at.monol1th.pic1.core.particles.movement;

import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 14.02.2015.
 */
public interface IParticleMover {
	public void updateParticle(Particle p, double dt, double c);
	public void initializeParticle(Particle p, double dt, double c);
}
