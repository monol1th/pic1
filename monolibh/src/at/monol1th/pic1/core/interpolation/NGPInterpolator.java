package at.monol1th.pic1.core.interpolation;

import at.monol1th.pic1.core.grid.Cell;
import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.ParticleList;

import java.util.List;

/**
 * Created by David on 14.02.2015.
 */
public class NGPInterpolator implements IInterpolator {
	public void interpolateParticlesToChargeDensity(ParticleList particleList, Grid grid) {
		List<Particle> list = particleList.list;
		grid.clearChargeDensity();

		for(Particle p : list)
		{
			int ngp = (int) (p.x / grid.dx + 0.5);
			Cell cell = grid.getCell(ngp);
			cell.d += p.q / grid.dx;
		}
	}

	public void interpolateParticlesToCurrentDensities(ParticleList particleList, Grid grid, double dt) {
		List<Particle> list = particleList.list;
		grid.clearCurrentDensity();

		for(Particle p : list)
		{
			int ngp     = (int) (p.x / grid.dx + 0.5);
			int ngp0    = (int) ((p.x - p.vx * dt) / grid.dx + 0.5);

			if( ngp != ngp0)
			{
				Cell cell = grid.getCell(p.x);
                //cell.jx += (ngp - ngp0) * p.q / grid.dx;
                cell.jx += (ngp - ngp0) * p.q / dt;
            }
        }
    }

	public void interpolateFieldsToParticles(ParticleList particleList, Grid grid) {
		List<Particle> list = particleList.list;

		for(Particle p : list)
		{
			p.Ex = grid.getCell(p.x).Ex;
		}
	}
}
