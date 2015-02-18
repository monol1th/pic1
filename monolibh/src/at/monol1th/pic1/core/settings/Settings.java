package at.monol1th.pic1.core.settings;

import at.monol1th.pic1.core.grid.solver.IFieldSolver;
import at.monol1th.pic1.core.grid.updater.IFieldUpdater;
import at.monol1th.pic1.core.interpolation.IInterpolator;
import at.monol1th.pic1.core.particles.Particle;
import at.monol1th.pic1.core.particles.movement.IParticleBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.IParticleMover;

import java.util.List;

/**
 * Created by David on 17.02.2015.
 */
public class Settings
{
    /*
			Simulation parameters
	 */

    public int gridSize;
    public double gridSpacing;
    public double timeStep;
    public double speedOfLight;

	/*
			Interpolation method
	 */

    public IInterpolator interpolationMethod;

	/*
			Particle mover & particle boundary conditions
	 */

    public IParticleMover particleMover;
    public IParticleBoundaryConditions particleBoundaryConditions;

	/*
			Field solver & updater
	 */

    public IFieldSolver fieldSolver;
    public IFieldUpdater fieldUpdater;

	/*
			List of particles
	 */

    public List<Particle> listOfParticles;


}
