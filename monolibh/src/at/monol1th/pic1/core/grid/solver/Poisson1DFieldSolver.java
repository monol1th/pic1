package at.monol1th.pic1.core.grid.solver;

import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.ParticleManager;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

/**
 * Created by David on 15.02.2015.
 */
public class Poisson1DFieldSolver implements IFieldSolver
{
    public void solveFields(Grid g, ParticleManager pL)
    {
        DoubleFFT_1D fft = new DoubleFFT_1D(g.isizex);
        /*
			Initialize new charge density array (rho) for use with FFT.
		 */

        double[] rho = new double[2 * g.isizex];
        double[] phi = new double[2 * g.isizex];

        for (int i = 0; i < g.isizex; i++)
        {
            rho[2 * i] = g.getCell(i).d;
            rho[2 * i + 1] = 0;
        }

		/*
			Compute FFT of charge density array (rho).
		 */

        fft.complexForward(rho);

		/*
			Solve Poisson equation in Fourier space.
		 */

        phi[0] = 0;
        phi[1] = 0;
        double dx2 = g.dx * g.dx;
        for (int k = 1; k < g.isizex; k++)
        {
            double k2 = dx2 / (2.0 * (Math.cos(2.0 * Math.PI * k / g.isizex) - 1.0));
            phi[2 * k] = -rho[2 * k] * k2;
            phi[2 * k + 1] = -rho[2 * k + 1] * k2;
        }

		/*
			Compute inverse FFT of electrostatic potential (phi).
		 */

        fft.complexInverse(phi, true);

		/*
			Apply electric field to grid cells.
		 */
        for (int i = 0; i < g.isizex; i++)
        {
            int iL = 2 * g.getCellIndex(i);
            int iR = 2 * g.getCellIndex(i + 1);
            g.getCell(i).Ex = -(phi[iR] - phi[iL]) / g.dx;
        }

		/*
            Subtract unphysical constant field due to periodic boundary conditions.
		 */


        //  Make sure total charge is zero.
        double dc = 0.0;
        for (int i = 0; i < g.isizex; i++)
        {
            dc += g.getCell(i).d;
        }
        dc /= (g.isizex);
        for (int i = 0; i < g.isizex; i++) {
            g.getCell(i).d -= dc;
        }

        //  Compute constant field from density.
        double Ec = 0.0;
        for (int i = 0; i < g.isizex; i++)
        {
            Ec += g.getCell(i).d * i;
        }
        Ec *= g.dx / g.isizex;

        for (int i = 0; i < g.isizex; i++) {
            g.getCell(i).Ex -= Ec;
        }
        for (int i = 0; i < g.isizex; i++) {
            g.getCell(i).d += dc;
        }



    }
}
