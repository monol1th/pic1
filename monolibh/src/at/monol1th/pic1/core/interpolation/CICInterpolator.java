package at.monol1th.pic1.core.interpolation;

import at.monol1th.pic1.core.grid.Cell;
import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.ParticleList;

/**
 * Created by David on 14.02.2015.
 */
public class CICInterpolator implements IInterpolator {
	public void interpolateParticlesToChargeDensity(ParticleList particleList, Grid g)
	{
		g.clearChargeDensity();
		for(Particle p : particleList.list)
		{
			int ix = (int) (p.x / g.dx);
			double wx = p.x / g.dx - ix;

			g.getCell(ix).d     += (1.0 - wx) * p.q / g.dx;
			g.getCell(ix+1).d   += wx * p.q / g.dx;
		}
	}

	public void interpolateParticlesToCurrentDensities(ParticleList particleList, Grid g, double dt)
	{
		g.clearCurrentDensity();
		for(Particle p : particleList.list)
		{
            double x1 = p.x;
            double x0 = p.x - dt * p.vx;
			int ix1   = (int) (x1 / g.dx);
			int ix0   = (int) (x0 / g.dx);

			if(ix0 == ix1)
			{
				/*  One-cell move   */
                oneCellMove(g.getCell(ix1), x1, x0, p.q, dt, g.dx);
			}
 			else
			{
				/*  Two-cell move   */
                int middleCellPosition = Math.max(ix1, ix0);
                oneCellMove(g.getCell(ix0), middleCellPosition * g.dx, x0, p.q, dt, g.dx);
                oneCellMove(g.getCell(ix1), x1, middleCellPosition * g.dx , p.q, dt, g.dx);
			}

		}
	}

    private void oneCellMove(Cell cell, double x1, double x0, double q, double dt, double dx)
    {
        cell.jx += (x1 - x0) * q / (dx * dt);
    }

	public void interpolateFieldsToParticles(ParticleList particleList, Grid g)
	{
		for(Particle p : particleList.list)
		{
			int ix = (int) (p.x / g.dx + 0.5);
			double wx = p.x/g.dx - ix;


            double ExL = g.getCell(ix-1).Ex;
            double ExR = g.getCell(ix).Ex;
            p.Ex = ExL * (0.5 - wx) + ExR * (0.5 + wx);
		}
	}
}
