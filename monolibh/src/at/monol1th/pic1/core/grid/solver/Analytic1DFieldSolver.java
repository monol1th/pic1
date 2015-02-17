package at.monol1th.pic1.core.grid.solver;

import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.ParticleList;

/**
 * Created by David on 15.02.2015.
 */
public class Analytic1DFieldSolver implements IFieldSolver
{
	public void solveFields(Grid g, ParticleList pL)
	{
		g.clearElectricField();
		for(Particle p : pL.list)
		{
			/*
				Cycle through each particle, apply (interpolated) analytic solution.
			 */

			int ixL = (int) (p.x / g.dx);
			int ixR = (int) (p.x / g.dx);

			for(int i = 0; i < g.isizex; i++)
			{
				if(i < ixL) g.getCell(i).Ex -= p.q / 2.0;
				if(i > ixR) g.getCell(i).Ex += p.q / 2.0;
				if(ixL <= i || i <= ixR)
				{
					double wx = p.x / g.dx - ixL;
					g.getCell(ixL).Ex -= p.q/2.0 * (1.0 - wx);
					g.getCell(ixR).Ex += p.q/2.0 * wx;
				}
			}
		}
	}
}
