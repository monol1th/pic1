package at.monol1th.pic1.benchmarks;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.observables.Momentum;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;
import at.monol1th.pic1.core.settings.Settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

/**
 * Created by dmueller on 2/23/15.
 */
public class PlasmaFrequencyTest
{

    public PlasmaFrequencyTest() {

    }

    public void runTests(double initialMomentumParameter, double numberDensity, double charge, double mass, double maxTime, int measurements, String fileName) throws FileNotFoundException {
        Simulation s = new Simulation(new InitialSettings(initialMomentumParameter, numberDensity, charge, mass));
        Momentum momentum = new Momentum(s);

        File file = new File(fileName);
        PrintWriter writer = new PrintWriter(file);


        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat formater = new DecimalFormat("############.############");
        formater.setDecimalFormatSymbols(otherSymbols);


        double theoreticalPlasmaFrequency = Math.sqrt(numberDensity * charge * charge / mass);

        writer.println("{");
        writer.println("{" + formater.format(theoreticalPlasmaFrequency) + "," + formater.format(numberDensity) + "},");
        int maxit = (int) (maxTime / s.settings.timeStep);
        int stepsPerMeasurement = maxit / measurements;
        for (int i = 0; i < measurements; i++) {
            double totalMomentum = momentum.computeTotalParticleMomentum();
            writer.print("{" + formater.format(s.elapsedTime) + ", " + formater.format(totalMomentum) + "}");
            if (i < measurements - 1) {
                writer.println(",");
            }

            for (int j = 0; j < stepsPerMeasurement; j++) {
                s.update();
            }

            // System.out.println(i + "/" + measurements);
        }
        writer.println("}");
        writer.close();

        System.out.println("Finished.");
    }


    private class InitialSettings extends Settings {
        public InitialSettings(double initialMomentumParameter, double numberDensity, double charge, double mass) {
            super();

            this.gridSize = (int) Math.pow(2, 8);
            this.gridSpacing = 1.0 / this.gridSize;
            this.timeStep = this.gridSpacing * 0.1;
            //this.speedOfLight = this.gridSpacing / this.timeStep * 0.5;
            this.speedOfLight = 1.0;

            this.particleMover = new RelativisticLeapFrogMover();
            this.particleBoundaryConditions = new PeriodicBoundaryConditions();
            this.interpolationMethod = new CICInterpolator();
            this.fieldSolver = new Poisson1DFieldSolver();
            this.fieldUpdater = new LeapFrogFieldUpdater();

            int particleCount = (int) (numberDensity * this.gridSize * this.gridSpacing);
            double totalCharge = charge * particleCount;

            this.listOfParticles = new ArrayList<Particle>();

            for (int i = 0; i < particleCount; i++) {

                double w = i / (double) particleCount;

                Particle p = new Particle();
                p.x = w * this.gridSize * this.gridSpacing;
                p.px = initialMomentumParameter * this.speedOfLight;
                p.q = totalCharge / particleCount;
                p.m = mass;

                this.listOfParticles.add(p);
            }
        }
    }
}
