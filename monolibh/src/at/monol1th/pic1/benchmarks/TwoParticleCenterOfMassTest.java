package at.monol1th.pic1.benchmarks;

import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.NGPInterpolator;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;
import at.monol1th.pic1.core.settings.Settings;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dmueller on 2/19/15.
 */
public class TwoParticleCenterOfMassTest {
    public TwoParticleCenterOfMassTest() {

    }

    public void runTests(ArrayList<Double> timeSteps,
                         int numberOfTestsPerTimeStep,
                         String interpolationType,
                         int randomSeed) {


    }


    private class initialSettings extends Settings {
        public initialSettings(String interpolationType, double timeStep, int randomSeed) throws SimulationParameterException {
            this.gridSize = (int) Math.pow(2, 8);
            this.gridSpacing = 0.08;
            this.timeStep = timeStep;

            if (timeStep * speedOfLight > 0.08)
                throw new SimulationParameterException("Error: time step too high.");

            this.speedOfLight = 1.0;

            this.particleMover = new RelativisticLeapFrogMover();
            this.particleBoundaryConditions = new PeriodicBoundaryConditions();
            this.fieldSolver = new Poisson1DFieldSolver();
            this.fieldUpdater = new LeapFrogFieldUpdater();

            switch (interpolationType) {
                case "NGP":
                    this.interpolationMethod = new NGPInterpolator();

                case "CIC":
                    this.interpolationMethod = new CICInterpolator();
                /*
                default:
                    throw new SimulationParameterException("Error: Interpolation method '" + interpolationType + "' unknown.");
                    */
            }


            double r1, r2;
            if (randomSeed == 0) {
                r1 = 0.5 * Math.random() + 0.25;
                r2 = 0.5 * Math.random() + 0.25;
            } else {
                Random generator = new Random(randomSeed);
                r1 = 0.5 * generator.nextDouble() + 0.25;
                r2 = 0.5 * generator.nextDouble() + 0.25;
            }

            Particle p1 = new Particle();
            p1.x = this.gridSize * this.gridSpacing * Math.min(r1, r2);
            p1.px = 0.01;
            p1.m = 1.0;
            p1.q = 1.0;

            Particle p2 = new Particle();
            p2.x = this.gridSize * this.gridSpacing * Math.max(r1, r2);
            p2.px = -0.01;
            p2.m = 1.0;
            p2.q = -1.0;

            this.listOfParticles = new ArrayList<Particle>();

            this.listOfParticles.add(p1);
            this.listOfParticles.add(p2);
        }
    }

    private class SimulationParameterException extends Exception {
        public SimulationParameterException(String s) {
            super(s);
        }
    }
}
