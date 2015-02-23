package at.monol1th.pic1.benchmarks;

import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;
import at.monol1th.pic1.core.settings.Settings;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dmueller on 2/23/15.
 */
public class PlasmaFrequencyTest
{

    public PlasmaFrequencyTest() {

    }

    public void runTests(double initialMomentumParameter, double numberDensity, double charge) {


    }


    private class initialSettings extends Settings {
        public initialSettings(double initialMomentumParameter, double numberDensity, double charge)
        {
            super();

            this.gridSize          = (int) Math.pow(2, 10);
            this.gridSpacing       = 2.0/this.gridSize;
            this.timeStep          = this.gridSpacing * 0.1;
            this.speedOfLight      = this.gridSpacing / this.timeStep * 0.5;

            this.particleMover                 = new RelativisticLeapFrogMover();
            this.particleBoundaryConditions    = new PeriodicBoundaryConditions();
            this.interpolationMethod           = new CICInterpolator();
            this.fieldSolver                   = new Poisson1DFieldSolver();
            this.fieldUpdater                  = new LeapFrogFieldUpdater();

            int particleCount                      = (int) (numberDensity * this.gridSize * this.gridSpacing);
            double totalCharge                      = charge * particleCount;

            this.listOfParticles = new ArrayList<Particle>();

            Random randomGenerator = new Random(2323);

            for (int i = 0; i < particleCount; i++)
            {

                double w = i / (double) particleCount;

                Particle p = new Particle();
                p.x = w * this.gridSize * this.gridSpacing;
                p.px = initialMomentumParameter * this.speedOfLight;
                p.q = totalCharge / particleCount;
                p.m = 1.0;

                this.listOfParticles.add(p);
            }
    }
}
