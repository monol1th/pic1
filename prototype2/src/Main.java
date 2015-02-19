import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.NGPInterpolator;
import at.monol1th.pic1.core.observables.CenterOfMass;
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
                Simulation initialization
         */

		Simulation sim = new Simulation(new TwoParticleSettings());
		sim.initialize();


        /*
                Create ascii display
         */

        int targetFPS = 60;
        int computationalStepsPerFrame = 2000;
        long optimalTime = 1000000000 / targetFPS;

        AsciiDisplay display = new AsciiDisplay(128, 40, sim);
        display.drawPhaseSpace = true;
        display.drawElectricField = true;
        display.drawChargeDensity = false;
        display.drawCurrentDensity = false;

        TotalCurrent current = new TotalCurrent(sim);
        CenterOfMass centerOfMass = new CenterOfMass(sim);

        double com0 = centerOfMass.computeCenterOfMass();
        double relativeAccuracy = Math.pow(10, -5);

        boolean running = true;

		while (running) {
			long startTime = System.nanoTime();
            for (int t = 0; t < computationalStepsPerFrame; t++)
            {
                sim.update();
                if(Math.abs((centerOfMass.computeCenterOfMass() - com0)/com0) > relativeAccuracy)
                {
                    System.out.println(String.format("Computational steps until failure: ti = %d", sim.computationalSteps));
                    System.out.println(String.format("Simulation time until failure: t = %f", sim.elapsedTime));
                    running = false;
                    break;
                }
               // System.out.println(current.computeTotalCurrent());
            }
            display.update(true);
            display.writeStatusLine(String.format("N = %d; pN = %d; L = %.2f;",
                            sim.settings.gridSize,
                            sim.particleManager.numberOfParticles,
                            sim.settings.gridSize * sim.settings.gridSpacing),
                    2, 0);
            display.writeStatusLine(String.format("dx = %.5f; dt = %.5f;", sim.settings.gridSpacing, sim.settings.timeStep), 2, 1);
            display.writeStatusLine(String.format("t = %.1f; ti = %dl", sim.elapsedTime, sim.computationalSteps), 50, 0);
            display.writeStatusLine(String.format("c = %.5fl", sim.speedOfLight), 50, 1);
			long diff = optimalTime - System.nanoTime() + startTime;

			try {

                display.repaint();
                TimeUnit.NANOSECONDS.sleep(diff);
			} catch (InterruptedException e) {
			}
		}
	}


}
