package at.monol1th.pic1.util.output.ascii;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 15.02.2015.
 */
public class AsciiGridOutput
{
    private static double E_ACCURACY = 0.001;
    private Simulation s;
    private int isizex;
    private double dx;
    private char[] output1;
    private char[] output2;

    public AsciiGridOutput(Simulation s)
    {
        this.s = s;
        this.isizex = s.isizex;
        this.dx = s.dx;
        this.output1 = new char[this.isizex];
        this.output2 = new char[this.isizex];
        for (int i = 0; i < this.isizex; i++)
        {
            output1[i] = '_';
            output2[i] = '_';
        }
    }

    public void drawCurrentState()
    {
        /*
			Draw electric field.
		 */
        for (int i = 0; i < this.isizex; i++)
        {
            output1[i] = ' ';
            if (s.grid.getCell(i).Ex > E_ACCURACY) output1[i] = '+';
            if (s.grid.getCell(i).Ex < -E_ACCURACY) output1[i] = '-';
        }

		/*
			Draw particles.
		 */
        for (Particle p : s.particleManager.listOfParticles)
        {
            int ix = (int) (p.x / this.dx);
            if (p.q > 0) output1[ix] = 'X';
            if (p.q < 0) output1[ix] = 'O';
        }
    }

    public char[] getOutput()
    {
        return output1;
    }
}
