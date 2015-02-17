package at.monol1th.pic1.core.interpolation;

import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.ParticleManager;

/**
 * Created by David on 14.02.2015.
 */
public interface IInterpolator {
	public void interpolateParticlesToChargeDensity(ParticleManager particleManager, Grid grid);

	public void interpolateParticlesToCurrentDensities(ParticleManager particleManager, Grid grid, double dt);

	public void interpolateFieldsToParticles(ParticleManager particleManager, Grid grid);
}
