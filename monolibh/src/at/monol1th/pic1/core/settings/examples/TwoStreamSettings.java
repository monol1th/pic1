package at.monol1th.pic1.core.settings.examples;

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
 * Created by David on 18.02.2015.
 */
public class TwoStreamSettings extends Settings
{
    public TwoStreamSettings()
    {
        super();

        this.gridSize = (int) Math.pow(2, 9);
        this.gridSpacing       = 1.0/this.gridSize;
        this.timeStep          = this.gridSpacing * 0.1;
        this.speedOfLight      = this.gridSpacing / this.timeStep * 0.5;

        this.particleMover                 = new RelativisticLeapFrogMover();
        this.particleBoundaryConditions    = new PeriodicBoundaryConditions();
        this.interpolationMethod           = new CICInterpolator();
        this.fieldSolver                   = new Poisson1DFieldSolver();
        this.fieldUpdater                  = new LeapFrogFieldUpdater();

        int particleCount                      = (int) Math.pow(2, 8);
        double initialMomentumParameter         = 0.6;
        double initialMomentumDistributionParameter         = 0.0;
        int perturbationNodes = 3;
        double perturbationAmplitude = 0.00;
        double totalCharge                      = Math.pow(2, 13);

        this.listOfParticles = new ArrayList<Particle>();

        Random randomGenerator = new Random(878878);

        for (int i = 0; i < particleCount; i++) {
            double d = 2.0 * ((i % 2) - 0.5);

            Particle p = new Particle();
            double w = i / (double) particleCount;

            p.x = w * this.gridSize * this.gridSpacing;
            //p.x *= 1.0 + d *perturbationAmplitude * Math.sin(perturbationNodes*w*2.0*Math.PI);
            //double r = randomGenerator.nextDouble();
            //p.x = r * this.gridSize * this.gridSpacing;

            p.px = initialMomentumParameter  * this.speedOfLight * d;
            p.px *= 1.0 + perturbationAmplitude * Math.sin(perturbationNodes * w * 2.0 * Math.PI);
            p.px *= 1.0 + initialMomentumDistributionParameter * (Math.random()-0.5);
            p.q = totalCharge / particleCount;
            p.m = 1.0;
            this.listOfParticles.add(p);
        }
    }
}
