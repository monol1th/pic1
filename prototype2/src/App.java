import asciiPanel.AsciiPanel;
import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.util.output.ascii.AsciiPhaseSpaceOutput;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by David on 15.02.2015.
 */
public class App extends JFrame {
	public static final int NX = 128;
	public static final int NY = 32;
	public AsciiPanel terminal;

	public App() {
		super();
		terminal = new AsciiPanel(NX, NY + 3);
		terminal.setDefaultBackgroundColor(new Color(16, 26, 41));
		terminal.clear();
		add(terminal);
		pack();

	}

	public static void main(String[] args) {
		App app = new App();
		app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		app.setVisible(true);
		app.setResizable(true);

		int targetFPS = 60;
		int computationalStepsPerFrame = 1;
		long optimalTime = 1000000000 / targetFPS;
		int n = (int) Math.pow(2, 8);
		double dx = 0.08;   /*  was 0.04    */
		double dt = 0.001;
		double c = dx / dt * 0.5;
		double size = dx * n;

		Simulation sim = new Simulation(n, dx, dt, c);
		/*
			Produce a few particles for two-stream instability.
		 */


		int particleNumber = (int) Math.pow(2, 13);

		double initialMomentumParameter = 0.5;

		int perturbationNodes = 4;  //  was 3
		double perturbationAmplitude = 0.1;

		for (int i = 0; i < particleNumber; i++) {
			double d = 2.0 * ((i % 2) - 0.5);

			Particle p = new Particle();
			double w = i / (double) particleNumber;

			p.x = w * n * dx;
			//p.x *= 1.0 + d *perturbationAmplitude * Math.sin(perturbationNodes*w*2.0*Math.PI);

			p.px = initialMomentumParameter * d * c;
			p.px *= 1.0 + perturbationAmplitude * Math.sin(perturbationNodes * w * 2.0 * Math.PI);

			p.q = 1.0;
			p.m = 1.0;

			sim.particleList.addParticle(p);
		}


		/*
			Two-particle test
		 */
        /*
		Particle p1 = new Particle();
		p1.x = n * dx * 0.3123;
		p1.px = 0.5;
		p1.m = 1.0;
		p1.q = 1.0;

		Particle p2 = new Particle();
		p2.x = n*dx * 0.69;
		p2.px = 0.5;
		p2.m = 1.0;
		p2.q = -1.0;

		sim.particleList.addParticle(p1);
		sim.particleList.addParticle(p2);
        */

		sim.initialize();

		AsciiPhaseSpaceOutput asciiPSOutput = new AsciiPhaseSpaceOutput(sim, NX, NY);
		asciiPSOutput.drawElectricField = true;

		while (true) {
			long startTime = System.nanoTime();

			//  Create output.
			app.terminal.repaint();
			//app.terminal.clear();

			app.terminal.write("One dimensional electromagnetic particle-in-cell simulation by monol1th (c) 2015.", 1, 1);
			String line = String.format("Setup: Two-stream instability with  grid size N = %d, and particle number pN = %d.", n, particleNumber);

			app.terminal.write(line, 1, 2);

			asciiPSOutput.drawOutput();
			char[][] tmp = asciiPSOutput.getOutputCharArray();
			for (int y = 0; y < NY; y++) {
				for (int x = 0; x < NX; x++) {
					app.terminal.write(tmp[y][x], x, y + 3);
				}
			}
			//app.repaint();

			//  Draw using asciiPanel.
			//app.terminal.write(asciiPSOutput.getOutputString());


            for (int t = 0; t < computationalStepsPerFrame; t++) sim.update();

			//  Sleep until next update.
			long diff = optimalTime - System.nanoTime() + startTime;
			if(diff < 0)
				app.terminal.write((char) 219, NX-1, 0);
			else
				app.terminal.write((char) 0, NX-1, 0);

			try {
				TimeUnit.NANOSECONDS.sleep(diff);
			} catch (InterruptedException e) {
			}
		}
	}

	public void repaint() {
		terminal.clear();
		super.repaint();
	}
}
