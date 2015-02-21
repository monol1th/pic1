package at.monol1th.pic1.core.settings.examples;

import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;
import at.monol1th.pic1.core.settings.Settings;

import java.util.ArrayList;

/**
 * Created by David on 18.02.2015.
 */
public class SingleParticleSettings extends Settings
{
    public SingleParticleSettings(double initialMomentumParameter)
    {

        this.gridSize          = (int) Math.pow(2, 3);
        this.gridSpacing       = 0.08;
        this.timeStep          = 0.0001;
        this.speedOfLight      = this.gridSpacing / this.timeStep * 0.5;

        this.particleMover                 = new RelativisticLeapFrogMover();
        this.particleBoundaryConditions    = new PeriodicBoundaryConditions();
        this.interpolationMethod           = new CICInterpolator();
        this.fieldSolver                   = new Poisson1DFieldSolver();
        this.fieldUpdater                  = new LeapFrogFieldUpdater();

        Particle p1 = new Particle();
        p1.x = this.gridSize * this.gridSpacing * 0.4;
        p1.px = initialMomentumParameter;
        p1.m = 1.0;
        p1.q = 1.0;

        this.listOfParticles = new ArrayList<Particle>();

        this.listOfParticles.add(p1);

    }

    public SingleParticleSettings()
    {
        this(0.5);
    }
}
