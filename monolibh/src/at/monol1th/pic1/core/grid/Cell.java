package at.monol1th.pic1.core.grid;

/**
 * Created by David on 14.02.2015.
 */
public class Cell {

	public int ix;              /*  Cell position in lattice coordinates            */
	public double x;            /*  Cell position in spatial coordinates            */
	public double dx;           /*  Cell width                                      */

	public double jx;           /*  Cell current (sits at ix+0.5, it+0.5)           */
	public double Ex;           /*  Cell electrical field (sits at ix+0.5, it)      */
	public double rx;           /*  Cell potential (sits at ix, it)                 */
	public double d;            /*  Cell density (sits at ix, it)                   */

	public Cell(int ix, double x, double dx) {
		this.ix = ix;
		this.x = x;
		this.dx = dx;
	}

}
