import asciiPanel.AsciiPanel;
import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.NGPInterpolator;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;
import at.monol1th.pic1.core.settings.Settings;
import at.monol1th.pic1.util.output.ascii.AsciiPhaseSpaceOutput;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
        int computationalStepsPerFrame = 5;
        long optimalTime = 1000000000 / targetFPS;

		/*
				Simulation settings
		 */

		/*
			Two-stream instability
		 */

		Settings twoStream = new Settings();

		twoStream.gridSize          = (int) Math.pow(2, 11);
		twoStream.gridSpacing       = 400.0/twoStream.gridSize;
		twoStream.timeStep          = 0.004;
		twoStream.speedOfLight      = twoStream.gridSpacing / twoStream.timeStep * 0.5;

		twoStream.particleMover                 = new RelativisticLeapFrogMover();
		twoStream.particleBoundaryConditions    = new PeriodicBoundaryConditions();
		twoStream.interpolationMethod           = new NGPInterpolator();
		twoStream.fieldSolver                   = new Poisson1DFieldSolver();
		twoStream.fieldUpdater                  = new LeapFrogFieldUpdater();

		int particleNumber                      = (int) Math.pow(2, 15);
		double initialMomentumParameter         = 0.6;
		int perturbationNodes                   = 2;
		double perturbationAmplitude            = 0.05;

		twoStream.listOfParticles = new ArrayList<Particle>();

        for (int i = 0; i < particleNumber; i++) {
			double d = 2.0 * ((i % 2) - 0.5);

			Particle p = new Particle();
			double w = i / (double) particleNumber;

			p.x = w * twoStream.gridSize * twoStream.gridSpacing;
			//p.x *= 1.0 + d *perturbationAmplitude * Math.sin(perturbationNodes*w*2.0*Math.PI);
            //p.x = Math.random() * twoStream.gridSize * twoStream.gridSpacing;

			p.px = initialMomentumParameter * d * twoStream.speedOfLight;
			p.px *= 1.0 + perturbationAmplitude * Math.sin(perturbationNodes * w * 2.0 * Math.PI);
			p.q = 1.0;
			p.m = 1.0;
	        twoStream.listOfParticles.add(p);
		}


		/*
			Two-particle test
		 */

		Settings twoParticle = new Settings();

		twoParticle.gridSize          = (int) Math.pow(2, 8);
		twoParticle.gridSpacing       = 0.08;
		twoParticle.timeStep          = 0.02;
		twoParticle.speedOfLight      = twoStream.gridSpacing / twoStream.timeStep * 0.5;

		twoParticle.particleMover                 = new RelativisticLeapFrogMover();
		twoParticle.particleBoundaryConditions    = new PeriodicBoundaryConditions();
		twoParticle.interpolationMethod           = new NGPInterpolator();
		twoParticle.fieldSolver                   = new Poisson1DFieldSolver();
		twoParticle.fieldUpdater                  = new LeapFrogFieldUpdater();

		Particle p1 = new Particle();
        p1.x = twoParticle.gridSize * twoParticle.gridSpacing * 0.4;
        p1.px = 0.2;
        p1.m = 1.0;
		p1.q = 1.0;

		Particle p2 = new Particle();
        p2.x = twoParticle.gridSize * twoParticle.gridSpacing * 0.6;
        p2.px = -0.2;
        p2.m = 1.0;
		p2.q = -1.0;

		twoParticle.listOfParticles = new ArrayList<Particle>();

		twoParticle.listOfParticles.add(p1);
		twoParticle.listOfParticles.add(p2);


		Simulation sim = new Simulation(twoStream);
		sim.initialize();

		AsciiPhaseSpaceOutput asciiPSOutput = new AsciiPhaseSpaceOutput(sim, NX, NY);
        asciiPSOutput.drawPhaseSpace = true;
        asciiPSOutput.drawElectricField = true;
        asciiPSOutput.drawChargeDensity = false;

		while (true) {
			long startTime = System.nanoTime();

			//  Create output.
			//app.terminal.clear();

            app.terminal.write("One dimensional electrostatic particle-in-cell simulation by monol1th (c) 2015.", 1, 1);
            String line = String.format("Setup: Grid size N = %d, and particle number pN = %d.", sim.settings.gridSize, sim.particleManager.numberOfParticles);

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

			//  Show computational steps.
			line = "it = " + sim.computationalSteps;
			app.terminal.write(line, 0, NY+2);

			//  Show computational time.
			line = "t = " + String.format( "%.2f", sim.computationalSteps*sim.dt )  ;
			app.terminal.write(line, 0, NY+1);

			//  Sleep until next update.
			long diff = optimalTime - System.nanoTime() + startTime;
			if(diff < 0)
				app.terminal.write((char) 219, NX-1, 0);
			else
				app.terminal.write((char) 0, NX-1, 0);
			try {

                app.terminal.repaint();
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
