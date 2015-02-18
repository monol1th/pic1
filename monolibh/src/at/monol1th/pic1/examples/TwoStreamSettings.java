package at.monol1th.pic1.examples;

import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
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

        this.gridSize          = (int) Math.pow(2, 10);
        this.gridSpacing       = 2.0/this.gridSize;
        this.timeStep          = this.gridSpacing * 0.1;
        this.speedOfLight      = this.gridSpacing / this.timeStep * 0.5;

        this.particleMover                 = new RelativisticLeapFrogMover();
        this.particleBoundaryConditions    = new PeriodicBoundaryConditions();
        this.interpolationMethod           = new NGPInterpolator();
        this.fieldSolver                   = new Poisson1DFieldSolver();
        this.fieldUpdater                  = new LeapFrogFieldUpdater();

        int particleCount                      = (int) Math.pow(2, 15);
        double initialMomentumParameter         = 0.5;
        int perturbationNodes                   = 4;
        double perturbationAmplitude            = 0.01;
        double totalCharge                      = Math.pow(2, 14);

        this.listOfParticles = new ArrayList<Particle>();

        Random randomGenerator = new Random(2323);

        for (int i = 0; i < particleCount; i++) {
            double d = 2.0 * ((i % 2) - 0.5);

            Particle p = new Particle();
            double w = i / (double) particleCount;

            p.x = w * this.gridSize * this.gridSpacing;
            //p.x *= 1.0 + d *perturbationAmplitude * Math.sin(perturbationNodes*w*2.0*Math.PI);
            double r = randomGenerator.nextDouble();
            //p.x = r * twoStream.gridSize * twoStream.gridSpacing;

            p.px = initialMomentumParameter * d * this.speedOfLight;
            p.px *= 1.0 + perturbationAmplitude * Math.sin(perturbationNodes * w * 2.0 * Math.PI);
            p.q = totalCharge / particleCount;
            p.m = 1.0;
            this.listOfParticles.add(p);
        }
    }
}
