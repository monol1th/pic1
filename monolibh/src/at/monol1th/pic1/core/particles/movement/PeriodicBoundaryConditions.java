package at.monol1th.pic1.core.particles.movement;

import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 15.02.2015.
 */
public class PeriodicBoundaryConditions implements IParticleBoundaryConditions
{
	public void applyBoundaryConditions(Particle p, double size)
	{
		int i = (int) (p.x/size + 1);
		p.x -= (i-1) *size;
		/*
		if(p.x < 0) p.x += size;
		if(p.x > size) p.x -= size;
		*/
	}
}
