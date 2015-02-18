import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.NGPInterpolator;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;
import at.monol1th.pic1.core.settings.Settings;
import at.monol1th.pic1.util.display.AsciiDisplay;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by David on 15.02.2015.
 */
public class Main
{
	public static void main(String[] args)
    {

		/*
				Simulation settings
		 */

		/*
			Two-stream instability
		 */

		Settings twoStream = new Settings();

		twoStream.gridSize          = (int) Math.pow(2, 10);
		twoStream.gridSpacing       = 1.0/twoStream.gridSize;
		twoStream.timeStep          = twoStream.gridSpacing * 0.1;
		twoStream.speedOfLight      = twoStream.gridSpacing / twoStream.timeStep * 0.5;

		twoStream.particleMover                 = new RelativisticLeapFrogMover();
		twoStream.particleBoundaryConditions    = new PeriodicBoundaryConditions();
		twoStream.interpolationMethod           = new CICInterpolator();
		twoStream.fieldSolver                   = new Poisson1DFieldSolver();
		twoStream.fieldUpdater                  = new LeapFrogFieldUpdater();

		int particleCount                      = (int) Math.pow(2, 15);
		double initialMomentumParameter         = 0.5;
		int perturbationNodes                   = 2;
		double perturbationAmplitude            = 0.0;
        double totalCharge                      = Math.pow(2, 16);

		twoStream.listOfParticles = new ArrayList<Particle>();

        Random randomGenerator = new Random(2323);

        for (int i = 0; i < particleCount; i++) {
			double d = 2.0 * ((i % 2) - 0.5);

			Particle p = new Particle();
			double w = i / (double) particleCount;

			//p.x = w * twoStream.gridSize * twoStream.gridSpacing;
			//p.x *= 1.0 + d *perturbationAmplitude * Math.sin(perturbationNodes*w*2.0*Math.PI);
            double r = randomGenerator.nextDouble();
            p.x = r * twoStream.gridSize * twoStream.gridSpacing;

			p.px = initialMomentumParameter * d * twoStream.speedOfLight;
			p.px *= 1.0 + perturbationAmplitude * Math.sin(perturbationNodes * w * 2.0 * Math.PI);
			p.q = totalCharge / particleCount;
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

        /*
                Simulation initialization
         */

		Simulation sim = new Simulation(twoStream);
		sim.initialize();



        /*
                Create ascii display
         */

        int targetFPS = 60;
        int computationalStepsPerFrame = 1;
        long optimalTime = 1000000000 / targetFPS;

        AsciiDisplay display = new AsciiDisplay(128, 32, sim);

		while (true) {
			long startTime = System.nanoTime();
            for (int t = 0; t < computationalStepsPerFrame; t++) sim.update();
            display.update(true);
			long diff = optimalTime - System.nanoTime() + startTime;

			try {

                display.repaint();
                TimeUnit.NANOSECONDS.sleep(diff);
			} catch (InterruptedException e) {
			}
		}
	}


}
