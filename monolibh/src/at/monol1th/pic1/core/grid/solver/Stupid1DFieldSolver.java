package at.monol1th.pic1.core.grid.solver;

import at.monol1th.pic1.core.grid.Cell;
import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.ParticleManager;

/**
 * Created by David on 14.02.2015.
 */
public class Stupid1DFieldSolver implements IFieldSolver {
	public void solveFields(Grid g, ParticleManager pL)
	{

		double averageField = 0.0;
		g.getCell(0).Ex = 0.0;

		for(int i = 0; i < g.isizex-1; i++)
		{
			Cell cell0 = g.getCell(i);
			Cell cell1 = g.getCell(i+1);
			cell1.Ex = g.dx * cell0.d + cell0.Ex;
			averageField += cell1.Ex;
		}

		averageField /= g.isizex;

		for(int i = 0; i < g.isizex; i++)
		{
			//g.getCell(i).Ex -= averageField;
		}
	}
}
