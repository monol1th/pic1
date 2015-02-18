package at.monol1th.pic1.core.grid;

/**
 * Created by David on 14.02.2015.
 */
public class Grid
{

    public int isizex;          /*  Grid size in lattice coordinates        */
    public double size;         /*  Grid size in spatial coordinates        */
    public double dx;           /*  Grid spacing in spatial coordinates     */

    public Cell[] cells;      /*  Cell array containing all cells         */

    public Grid(int isizex, double dx)
    {
        this.isizex = isizex;
        this.size = isizex * dx;
        this.dx = dx;

        cells = new Cell[isizex];

        for (int i = 0; i < isizex; i++)
        {
            double x = i * this.dx;
            cells[i] = new Cell(i, x, this.dx);
        }
    }

    public Cell getCell(int ix)
    {
        return cells[getCellIndex(ix)];
    }

    public Cell getCell(double x)
    {
        int ix = (int) (x / this.dx);
        return getCell(ix);
    }

    public void clearChargeDensity()
    {
        for (Cell cell : cells) cell.d = 0;
    }

    public void clearCurrentDensity()
    {
        for (Cell cell : cells) cell.jx = 0;
    }

    public void clearElectricField()
    {
        for (Cell cell : cells) cell.Ex = 0;
    }


    public int getCellIndex(int ix)
    {
        /*
			Periodic boundary conditions for the grid.
			Thanks, Gerald!
		 */
        return (ix % this.isizex + this.isizex) % this.isizex;
    }
}
