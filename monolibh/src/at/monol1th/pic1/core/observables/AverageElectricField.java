package at.monol1th.pic1.core.observables;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.Cell;
import at.monol1th.pic1.core.grid.Grid;

/**
 * Created by David on 17.02.2015.
 */
public class AverageElectricField
{
	private Grid g;
	public AverageElectricField(Grid g)
	{
		this.g = g;
	}
	public double compute()
	{
		double Eavg = 0.0;
		for(Cell cell : g.cells)
		{
			Eavg += cell.Ex;
		}
		return Eavg;
	}
}
