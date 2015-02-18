import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.NGPInterpolator;
import at.monol1th.pic1.core.observables.TotalCurrent;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;
import at.monol1th.pic1.core.settings.Settings;
import at.monol1th.pic1.examples.*;
import at.monol1th.pic1.util.display.AsciiDisplay;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
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

        Settings singleStream = new SingleStreamSettings();
		Settings twoStream = new TwoStreamSettings();
        Settings singleParticle = new SingleParticleSettings(-100.0);
		Settings twoParticle = new TwoParticleSettings();
		Settings fourParticle = new FourParticleSettings();

        /*
                Simulation initialization
         */

		Simulation sim = new Simulation(singleParticle);
		sim.initialize();


        /*
                Create ascii display
         */

        int targetFPS = 3;
        int computationalStepsPerFrame = 1;
        long optimalTime = 1000000000 / targetFPS;

        AsciiDisplay display = new AsciiDisplay(128, 40, sim);
        display.drawPhaseSpace = true;
        display.drawElectricField = false;
        display.drawChargeDensity = false;
        display.drawCurrentDensity = true;

        TotalCurrent current = new TotalCurrent(sim);

		while (true) {
			long startTime = System.nanoTime();
            for (int t = 0; t < computationalStepsPerFrame; t++)
            {
                sim.update();
               // System.out.println(current.computeTotalCurrent());
            }
            display.update(true);
            display.writeStatusLine(String.format("N = %d, pN = %d", sim.settings.gridSize, sim.particleManager.numberOfParticles), 2, 0);
            display.writeStatusLine(String.format("dx = %.5f, dt = %.5f", sim.settings.gridSpacing, sim.settings.timeStep), 2, 1);
            display.writeStatusLine(String.format("t = %.1f, ti = %d", sim.elapsedTime, sim.computationalSteps), 50, 0);
			long diff = optimalTime - System.nanoTime() + startTime;

			try {

                display.repaint();
                TimeUnit.NANOSECONDS.sleep(diff);
			} catch (InterruptedException e) {
			}
		}
	}


}
