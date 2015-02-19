package at.monol1th.pic1.examples;

import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.NGPInterpolator;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;
import at.monol1th.pic1.core.settings.Settings;

import java.util.ArrayList;

/**
 * Created by David on 18.02.2015.
 */
public class FourParticleSettings extends Settings
{
    public FourParticleSettings()
    {

        this.gridSize          = (int) Math.pow(2, 10);
        this.gridSpacing       = 0.08;
        this.timeStep          = 0.002;
        this.speedOfLight      = this.gridSpacing / this.timeStep * 0.5;

        this.particleMover                 = new RelativisticLeapFrogMover();
        this.particleBoundaryConditions    = new PeriodicBoundaryConditions();
        this.interpolationMethod           = new CICInterpolator();
        this.fieldSolver                   = new Poisson1DFieldSolver();
        this.fieldUpdater                  = new LeapFrogFieldUpdater();

        Particle p1 = new Particle();
        p1.x = this.gridSize * this.gridSpacing * 0.3;
        p1.px = 0.01;
        p1.m = 1.0;
        p1.q = 1.0;

        Particle p2 = new Particle();
        p2.x = this.gridSize * this.gridSpacing * 0.4;
        p2.px = -0.01;
        p2.m = 1.0;
        p2.q = -1.0;

        Particle p3 = new Particle();
        p3.x = this.gridSize * this.gridSpacing * 0.7;
        p3.px = 0.01;
        p3.m = 1.0;
        p3.q = -1.0;

        Particle p4 = new Particle();
        p4.x = this.gridSize * this.gridSpacing * 0.8;
        p4.px = -0.01;
        p4.m = 1.0;
        p4.q = 1.0;


        this.listOfParticles = new ArrayList<Particle>();

        this.listOfParticles.add(p1);
        this.listOfParticles.add(p2);
        this.listOfParticles.add(p3);
        this.listOfParticles.add(p4);

    }
}
