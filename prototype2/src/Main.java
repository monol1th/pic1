import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.NGPInterpolator;
import at.monol1th.pic1.core.observables.CenterOfMass;
import at.monol1th.pic1.core.observables.TotalCurrent;
import at.monol1th.pic1.core.settings.Settings;
import at.monol1th.pic1.core.settings.examples.*;
import at.monol1th.pic1.util.display.AsciiDisplay;

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

        Settings settings = new TwoStreamSettings();
        settings.interpolationMethod = new CICInterpolator();
        Simulation sim = new Simulation(settings);
        sim.initialize();


        /*
                Create ascii display
         */

        int targetFPS = 60;
        int computationalStepsPerFrame = 2;
        long optimalTime = 1000000000 / targetFPS;

        AsciiDisplay display = new AsciiDisplay(128, 32, sim);
        display.drawPhaseSpace = true;
        display.drawElectricField = false;
        display.drawChargeDensity = false;
        display.drawCurrentDensity = false;
        display.drawHighlightedParticles = true;

        //  Add 10 random particles to highlight list.
        for(int i = 0; i < 10; i++)
        {
            int j = (int) (Math.random() * sim.particleManager.numberOfParticles);
            display.listOfHighlightedParticles.add(sim.particleManager.listOfParticles.get(j));
        }

        boolean running = true;

		while (running) {
			long startTime = System.nanoTime();
            for (int t = 0; t < computationalStepsPerFrame; t++)
            {
                sim.update();

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
			}
            catch (InterruptedException e)
            {

			}
		}
	}


}
