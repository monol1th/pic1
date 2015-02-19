package at.monol1th.pic1.util.output.ascii;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.particles.Particle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by David on 15.02.2015.
 */
public class AsciiPlotter
{
    private Simulation s;
    private int nx;
    private int ny;

    private char[] characterSet =  {0, '.', ',', ':', ';', '%', '#',
                                    176, 176, 176, 176, 177, 177, 177, 177,
                                    178, 178, 178, 178, 219, 219, 219, 219};


    public AsciiPlotter(Simulation s, int nx, int ny)
    {
        this.s = s;
        this.nx = nx;
        this.ny = ny;
    }

    public char[][] getPhaseSpaceOutput()
    {
        char[][]    output      = new char[ny][nx];
        double[][]  psDensity   = new double[ny][nx];

        double simulationBoxLength  = s.settings.gridSize * s.settings.gridSpacing;
        double c                    = s.speedOfLight;

        /*
            Initialize phase space density array and output character array.
         */
        for (int x = 0; x < nx; x++)
        {
            for (int y = 0; y < ny; y++)
            {
                psDensity[y][x] = 0.0;
                output[y][x] = 0;
            }
        }
        /*
            Compute phase space density.
         */

        for (Particle p : s.particleManager.listOfParticles)
        {
            double px = p.x / simulationBoxLength * nx;
            double pv = (p.vx / c + 1.0) * ny / 2.0;

            psDensity[(int) pv][(int) px] += Math.abs(p.q);
        }

        double maxDensity = 0.0;
        for (int x = 0; x < nx; x++)
            for (int y = 0; y < ny; y++)
                if (maxDensity < psDensity[y][x]) maxDensity = psDensity[y][x];

        /*
            Draw phase space density.
         */

        for (int x = 0; x < nx; x++)
        {
            for (int y = 0; y < ny; y++)
            {
                int ci = (int) Math.min((psDensity[y][x] / maxDensity) * (characterSet.length - 1) + 0.5, characterSet.length - 1);
                if (ci > 0) output[y][x] = characterSet[ci];
            }
        }

        return output;
    }

    public char[][] getElectricFieldOutput()
    {
        char[][] output = new char[ny][nx];

        /*
            Initialize output character array.
         */
        for (int x = 0; x < nx; x++)
            for (int y = 0; y < ny; y++)
                output[y][x] = 0;

        /*
            Find maximal electric field.
         */


        double maxElectricField =  Math.pow(10, -17);
        for (int ix = 0; ix < s.settings.gridSize; ix++)
            if (maxElectricField < Math.abs(s.grid.getCell(ix).Ex))
                maxElectricField = Math.abs(s.grid.getCell(ix).Ex);

        /*
            Draw electric field to output character array.
         */

        for (int x = 0; x < nx; x++)
        {
            int    ix = (int) (x / (double) nx * s.settings.gridSize);
            double scaledEx = s.grid.getCell(ix).Ex / maxElectricField;
            double y = 0.5 * (scaledEx * 0.5 + 1.0) * (ny - 1);

            y = Math.max(0.0, Math.min(ny-1, y));
            output[(int) y][x] = 219;
        }

        return output;
    }

    public char[][] getCurrentDensityOutput()
    {
        char[][] output = new char[ny][nx];

        /*
            Initialize output character array.
         */
        for (int x = 0; x < nx; x++)
            for (int y = 0; y < ny; y++)
                output[y][x] = 0;

        /*
            Find maximal current density.
         */


        double maxCurrent =  Math.pow(10, -17);
        for (int ix = 0; ix < s.settings.gridSize; ix++)
            if (maxCurrent < Math.abs(s.grid.getCell(ix).jx))
                maxCurrent = Math.abs(s.grid.getCell(ix).jx);

        /*
            Draw current density to output character array.
         */

        for (int x = 0; x < nx; x++)
        {
            int    ix = (int) (x / (double) nx * s.settings.gridSize);
            double scaledJx = s.grid.getCell(ix).jx / maxCurrent;
            double y = 0.5 * (scaledJx * 0.5 + 1.0) * (ny - 1);

            y = Math.max(0.0, Math.min(ny-1, y));
            output[(int) y][x] = 219;
        }

        return output;
    }

    public char[][] getChargeDensityOutput()
    {

        char[][] output = new char[ny][nx];

        /*
            Initialize output character array.
         */
        for (int x = 0; x < nx; x++)
            for (int y = 0; y < ny; y++)
                output[y][x] = 0;

        /*
            Find maximal charge density.
         */


        double maxDensity =  Math.pow(10, -17);
        for (int ix = 0; ix < s.settings.gridSize; ix++)
            if (maxDensity < Math.abs(s.grid.getCell(ix).d))
                maxDensity = Math.abs(s.grid.getCell(ix).d);

        /*
            Draw electric field to output character array.
         */


        for (int x = 0; x < nx; x++)
        {
            int    ix = (int) (x / (double) nx * s.settings.gridSize);
            double scaledDensity = s.grid.getCell(ix).d / maxDensity;
            double y = 0.5 * (scaledDensity * 0.25 + 1.0) * (ny - 1);

            y = Math.max(0.0, Math.min(ny-1, y));
            output[(int) y][x] = 219;
        }


        return output;

    }
}
