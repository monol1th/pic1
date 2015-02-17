import at.monol1th.pic.core.Simulation;
import at.monol1th.pic.core.particles.Particle;
import at.monol1th.pic.util.output.ascii.AsciiGridOutput;
import at.monol1th.pic.util.output.ascii.AsciiPhaseSpaceOutput;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by David on 14.02.2015.
 */
public class Main
{
	public static void main(String [ ] args) throws IOException {
		int targetFPS = 10;
		long optimalTime = 1000000000 / targetFPS;
		int n = 128;
		double dx = 0.1;
		double dt = 0.0005;
		double c = dx / dt * 0.5;
		double size = dx * n;

		Simulation sim = new Simulation(n, dx, dt, c);

		/*
			Produce a few partilces.
		 */
		int particleNumber = 4000;
		for(int i = 0; i < particleNumber; i++)
		{
			double d = 2.0*((i % 2) - 0.5);

			Particle p = new Particle();
			//p.x = i / (double) particleNumber * n * dx;
			p.x = Math.random() * n * dx;
			p.px = 0.5 * d * c;
			p.q = 1.0;
			p.m = 1.0;

			sim.particleList.addParticle(p);
		}


		/*
		Particle p1 = new Particle();
		p1.x = n * dx * 0.41;
		p1.vx = 0;
		p1.px = 0.00;
		p1.q = -1.0;
		p1.m = 1;

		Particle p2 = new Particle();
		p2.x = n * dx * 0.59;
		p2.vx = 0.0;
		p2.px = 0.00;
		p2.q = 1.0;
		p2.m = 1;

		sim.particleList.addParticle(p1);
		sim.particleList.addParticle(p2);
		*/

		sim.initialize();
		AsciiGridOutput asciiOutput = new AsciiGridOutput(sim);
		AsciiPhaseSpaceOutput asciiPSOutput = new AsciiPhaseSpaceOutput(sim, 80, 50);

		//System.out.printf("Setup: One dimensional 'two particle test' on a grid with N=%d.\n", n);
		while(true)
		{
			long startTime = System.nanoTime();

			//  Update simulation.
			for(int t  = 0; t < 20; t++)    sim.update();

			/*
				Single line output.
			 */
			/*
			asciiOutput.drawCurrentState();
			System.out.print(asciiOutput.getOutput());
			System.out.print("\r");
			*/

			/*
				Multiline phase space output.
			 */

			//  Clear the command line for new ouput.
			//  Taken from:     http://stackoverflow.com/questions/4888362/commands-in-java-to-clear-the-screen

			/*
			String ANSI_CLS = "\u001b[2J";
			String ANSI_HOME = "\u001b[H";
			System.out.print(ANSI_CLS + ANSI_HOME);
			System.out.flush();
			*/
			//  Draw output and print to screen.
			//Runtime.getRuntime().exec("cmd /c cls");
			//System.out.println("Partilce-in-cell simulation by monol1th (c) 2015");
			//System.out.println();

			for(int i = 0; i < 50; i++)  System.out.print("\n");

			asciiPSOutput.drawOutput();
			String out = asciiPSOutput.getOutputString();
			System.out.print(out);

			//  Sleep until next update.
			long endTime = System.nanoTime();
			try     {   TimeUnit.NANOSECONDS.sleep(optimalTime - (endTime - startTime)); }
			catch (InterruptedException e) {}

		}
	}
}


