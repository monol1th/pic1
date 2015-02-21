package at.monol1th.pic1.benchmarks;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.NGPInterpolator;
import at.monol1th.pic1.core.observables.CenterOfMass;
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

    public void runTests(double[] timeSteps,
                         int numberOfTestsPerTimeStep,
                         String interpolationType,
                         int randomSeed)
    {
        System.out.println("Type\tdt\tsteps\ttime");
        for(int i = 0; i < timeSteps.length; i++)
        {
            double relativeAccuracy = Math.pow(10, -5);

            double dt = timeSteps[i];
            for(int j = 0; j < numberOfTestsPerTimeStep; j++)
            {
                try
                {
                    Settings currentSetting = new InitialSettings(interpolationType, dt, randomSeed);
                    Simulation currentSimulation = new Simulation(currentSetting);
                    currentSimulation.initialize();
                    CenterOfMass centerOfMass = new CenterOfMass(currentSimulation);
                    double com0 = centerOfMass.computeCenterOfMass();
                    boolean running = true;
                    while (running)
                    {
                        currentSimulation.update();
                        if(Math.abs((centerOfMass.computeCenterOfMass() - com0)/com0) > relativeAccuracy)
                        {
                            System.out.println(interpolationType + "\t"
                                    + dt + "\t"
                                    + currentSimulation.computationalSteps + "\t"
                                    + currentSimulation.elapsedTime);
                            running = false;
                            break;
                        }
                    }
                }
                catch(SimulationParameterException ex)
                {
                    System.out.println(ex.toString());
                }
            }
        }
    }


    private class InitialSettings extends Settings {
        public InitialSettings(String interpolationType, double timeStep, int randomSeed) throws SimulationParameterException {
            this.gridSize = (int) Math.pow(2, 10);
            this.gridSpacing = 0.08;
            this.timeStep = timeStep;
            this.speedOfLight = 1.0;

            if (timeStep * speedOfLight > this.gridSpacing)
                throw new SimulationParameterException("Error: time step too high.");


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
