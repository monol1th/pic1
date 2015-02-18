package at.monol1th.pic1.core.particles.movement;

import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 15.02.2015.
 */
public interface IParticleBoundaryConditions
{
    public void applyBoundaryConditions(Particle p, double size);
}
