import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.observables.Energy;
import at.monol1th.pic1.core.observables.Momentum;
import at.monol1th.pic1.core.settings.Settings;
import at.monol1th.pic1.core.settings.examples.SingleParticleSettings;
import at.monol1th.pic1.util.display.AsciiDisplay;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

        Settings settings = new SingleParticleSettings(5.0);
        Simulation sim = new Simulation(settings);
        sim.initialize();

	    Energy energy = new Energy(sim);


        /*
                Create ascii display
         */

        int targetFPS = 60;
        int computationalStepsPerFrame = 1;
        long optimalTime = 1000000000 / targetFPS;

        AsciiDisplay display = new AsciiDisplay(128, 32, sim);
        display.drawPhaseSpace = true;
        display.drawElectricField = true;
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

	    Momentum momentum = new Momentum(sim);

	    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
	    otherSymbols.setDecimalSeparator('.');
	    DecimalFormat formater = new DecimalFormat("############.############");
	    formater.setDecimalFormatSymbols(otherSymbols);

		while (running) {
			long startTime = System.nanoTime();
            for (int t = 0; t < computationalStepsPerFrame; t++)
            {
	            System.out.println(formater.format(sim.elapsedTime) + ", " + formater.format(momentum.computeTotalParticleMomentum()) +", ");
                sim.update();
                // System.out.println(current.computeTotalCurrent());
            }
			double energyField = energy.computeFieldEnergy();
			double energyParticles = energy.computeParticleEnergy();
            display.update(true);
            display.writeStatusLine(String.format("N = %d; pN = %d; L = %.2f;",
				            sim.settings.gridSize,
				            sim.particleManager.numberOfParticles,
				            sim.settings.gridSize * sim.settings.gridSpacing),
		            2, 0);
            display.writeStatusLine(String.format("dx = %.5f; dt = %.5f;", sim.settings.gridSpacing, sim.settings.timeStep), 2, 1);
            display.writeStatusLine(String.format("t = %.1f; ti = %d;", sim.elapsedTime, sim.computationalSteps), 50, 0);
            display.writeStatusLine(String.format("c = %.5f;", sim.speedOfLight), 50, 1);
            display.writeStatusLine(String.format("Ef = %.5f;", energyField), 70, 0);
            display.writeStatusLine(String.format("Ep = %.5f;", energyParticles), 70, 1);
            display.writeStatusLine(String.format("E = %.5f;", energyParticles + energyField), 100, 1);
           // display.writeStatusLine(String.format("Ei = %.5f;", energy.computeInteractionEnergy()), 100, 0);
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
