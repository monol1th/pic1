package at.monol1th.pic1.core.observables;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.Cell;
/**
 * Created by David on 18.02.2015.
 */
public class TotalCurrent
{

    private Simulation s;

    public TotalCurrent(Simulation s)
    {
        this.s = s;
    }

    public double computeTotalCurrent()
    {
        double jx = 0.0;
        for(Cell cell : s.grid.cells)
        {
            jx += cell.jx;
        }
        return jx;
    }
}
