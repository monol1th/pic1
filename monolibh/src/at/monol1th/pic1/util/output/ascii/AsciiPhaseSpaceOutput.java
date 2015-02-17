package at.monol1th.pic1.util.output.ascii;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 15.02.2015.
 */
public class AsciiPhaseSpaceOutput
{
    public boolean drawPhaseSpace = true;
    public boolean drawElectricField = false;
    public boolean drawChargeDensity = false;
    private Simulation s;
    private int nx;
	private int ny;
	private char[][] output;
	private double[][] psDensity;
    private double max;

	public AsciiPhaseSpaceOutput(Simulation s, int nx, int ny)
	{
		this.s = s;
		this.nx = nx;
		this.ny = ny;

		output = new char[ny][nx];
		psDensity = new double[ny][nx];
		for(int x = 0; x < nx; x++)
			for(int y = 0; y < ny; y++)
				psDensity[y][x] = 0.0;

        this.max = 0.0;

		clearOutput();
	}

	public void drawOutput()
	{
		clearOutput();

		double c = s.speedOfLight;
		double size = s.isizex * s.dx;



		/*
			Draw phase space distribution.
		 */
        if (drawPhaseSpace) {
            for (Particle p : s.particleList.list) {
                double px = p.x / size * nx;
                double pv = (p.vx / c + 1.0) * ny / 2.0;

                psDensity[(int) pv][(int) px] += 1.0;


            }

            if (max < 1.0) {
                for (int x = 0; x < nx; x++) {
                    for (int y = 0; y < ny; y++) {
                        if (max < psDensity[y][x]) max = psDensity[y][x];
                    }
                }
            }

            //char[] charSet = {0,'.',176,177,178,219};
            char[] charSet = {0, '.', ',', ':', ';', '%', '#', 176, 176, 176, 176, 177, 177, 177, 177, 178, 178, 178, 178, 219, 219, 219, 219};
            //char[] charSet = {0,176,176,176,176,177,177,177,177,178,178,178,178,219,219,219,219};
            //char[] charSet = {0,176,177,178,219};
            for (int x = 0; x < nx; x++) {
                for (int y = 0; y < ny; y++) {
                    int ci = (int) Math.min((psDensity[y][x] / max) * (charSet.length - 1) + 0.5, charSet.length - 1);
                    if (ci > 0) output[y][x] = charSet[ci];
                }
            }
        }
		/*
			Draw electrical field, if activated.
		 */
		if(drawElectricField)
		{
			double maxEx = 0.0;
			for(int x = 0; x < nx; x++)
			{
				int ix = (int) (x / ((double) nx) * s.isizex);
				double Ex = s.grid.getCell(ix).Ex;
				if(Math.abs(Ex) > maxEx)    maxEx = Math.abs(Ex);
			}

			//maxEx = 0.1;

			for(int x = 0; x < nx; x++)
			{
				int ix = (int) (x / ((double) nx) * s.isizex);
				double Ex = Math.round(0.5 * ((s.grid.getCell(ix).Ex / maxEx ) * 0.5 +  1.0) * ny);


				output[(int) Ex][x] = 'X';
			}
		}

        /*
            Draw charge density, if activated.
		 */
        if (drawChargeDensity) {
            double maxd = 0.0;
            for (int x = 0; x < nx; x++) {
                int ix = (int) (x / ((double) nx) * s.isizex);
                double d = s.grid.getCell(ix).d;
                if (Math.abs(d) > maxd) maxd = Math.abs(d);
            }

            //maxEx = 0.1;

            for (int x = 0; x < nx; x++) {
                int ix = (int) (x / ((double) nx) * s.isizex);
                double d = Math.round(0.5 * ((s.grid.getCell(ix).d / maxd) * 0.25 + 1.0) * ny);


                output[(int) d][x] = 'O';
            }
        }
    }

	public String getOutputString()
	{
		String outputString  = "";
		for(int y = 0; y < ny; y++)
		{
			String line =  String.copyValueOf(output[y]);
			outputString += line;
			outputString += "\n";
		}
		return outputString;
	}

	public char[][] getOutputCharArray()
	{
		return output;
	}

	private void clearOutput()
	{
		for(int x = 0; x < nx; x++)
		{
			for(int y = 0; y < ny; y++)
			{
				output[y][x] = 0;
				psDensity[y][x] = 0.0;
			}
		}
	}
}
