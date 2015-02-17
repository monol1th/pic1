package at.monol1th.pic1.core.interpolation;

import at.monol1th.pic1.core.grid.Cell;
import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.ParticleManager;

import java.util.List;

/**
 * Created by David on 14.02.2015.
 */
public class NGPInterpolator implements IInterpolator {
	public void interpolateParticlesToChargeDensity(ParticleManager particleManager, Grid grid) {
		List<Particle> list = particleManager.listOfParticles;
		grid.clearChargeDensity();

		for(Particle p : list)
		{
			int ngp = (int) (p.x / grid.dx + 0.5);
			Cell cell = grid.getCell(ngp);
			cell.d += p.q / grid.dx;
		}
	}

	public void interpolateParticlesToCurrentDensities(ParticleManager particleManager, Grid grid, double dt) {
		List<Particle> list = particleManager.listOfParticles;
		grid.clearCurrentDensity();

		for(Particle p : list)
		{
			int ngp     = (int) (p.x / grid.dx + 0.5);
			int ngp0    = (int) ((p.x - p.vx * dt) / grid.dx + 0.5);

			if( ngp != ngp0)
			{
				Cell cell = grid.getCell(p.x);
                cell.jx += (ngp - ngp0) * p.q / dt;
            }
        }
    }

	public void interpolateFieldsToParticles(ParticleManager particleManager, Grid grid) {
		List<Particle> list = particleManager.listOfParticles;

		for(Particle p : list)
		{
			p.Ex = grid.getCell(p.x).Ex;
		}
	}
}
