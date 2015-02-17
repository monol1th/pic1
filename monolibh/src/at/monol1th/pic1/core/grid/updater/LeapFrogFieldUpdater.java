package at.monol1th.pic1.core.grid.updater;

import at.monol1th.pic1.core.grid.Cell;
import at.monol1th.pic1.core.grid.Grid;

/**
 * Created by David on 14.02.2015.
 */
public class LeapFrogFieldUpdater implements IFieldUpdater {
	public void updateFields(Grid g, double dt) {
		for(Cell cell : g.cells)
		{
			cell.Ex -= dt * cell.jx;
		}
	}
}
