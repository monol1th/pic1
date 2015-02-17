package at.monol1th.pic1.core.interpolation;

import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.ParticleList;

/**
 * Created by David on 14.02.2015.
 */
public interface IInterpolator {
	public void interpolateParticlesToChargeDensity(ParticleList particleList, Grid grid);

	public void interpolateParticlesToCurrentDensities(ParticleList particleList, Grid grid, double dt);

	public void interpolateFieldsToParticles(ParticleList particleList, Grid grid);
}
