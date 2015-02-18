package at.monol1th.pic1.core.interpolation;

import at.monol1th.pic1.core.grid.Cell;
import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.ParticleManager;

/**
 * Created by David on 14.02.2015.
 */
public class CICInterpolator implements IInterpolator
{
    public void interpolateParticlesToChargeDensity(ParticleManager particleManager, Grid g)
    {
        g.clearChargeDensity();
        for (Particle p : particleManager.listOfParticles)
        {
            int ix = (int) (p.x / g.dx);
            double wx = p.x / g.dx - ix;

            g.getCell(ix).d += (1.0 - wx) * p.q / g.dx;
            g.getCell(ix + 1).d += wx * p.q / g.dx;
        }
    }

    public void interpolateParticlesToCurrentDensities(ParticleManager particleManager, Grid g, double dt)
    {
        g.clearCurrentDensity();
        for (Particle p : particleManager.listOfParticles)
        {
            double x1 = p.x;
            double x0 = p.x - dt * p.vx;
            int ix1 = (int) (x1 / g.dx);
            int ix0 = (int) (x0 / g.dx);

            if (ix0 == ix1)
            {
                /*  One-cell move   */
                applyOneCellMoveInterpolation(g.getCell(ix1), x1, x0, p.q, dt, g.dx);
            } else
            {
				/*  Two-cell move   */
                int middleCellPosition = Math.max(ix1, ix0);
                applyOneCellMoveInterpolation(g.getCell(ix0), middleCellPosition * g.dx, x0, p.q, dt, g.dx);
                applyOneCellMoveInterpolation(g.getCell(ix1), x1, middleCellPosition * g.dx, p.q, dt, g.dx);
            }

        }
    }

    private void applyOneCellMoveInterpolation(Cell cell, double x1, double x0, double q, double dt, double dx)
    {
        cell.jx += (x1 - x0) * q / (dx * dt);
    }

    public void interpolateFieldsToParticles(ParticleManager particleManager, Grid g)
    {
        for (Particle p : particleManager.listOfParticles)
        {
            int ix = (int) (p.x / g.dx + 0.5);
            double wx = p.x / g.dx - ix;
            double ExL = g.getCell(ix - 1).Ex;
            double ExR = g.getCell(ix).Ex;
            p.Ex = ExL * (0.5 - wx) + ExR * (0.5 + wx);
        }
    }
}
