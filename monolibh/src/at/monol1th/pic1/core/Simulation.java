package at.monol1th.pic1.core;

import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.grid.solver.IFieldSolver;
import at.monol1th.pic1.core.grid.solver.Poisson1DFieldSolver;
import at.monol1th.pic1.core.grid.updater.IFieldUpdater;
import at.monol1th.pic1.core.grid.updater.LeapFrogFieldUpdater;
import at.monol1th.pic1.core.interpolation.CICInterpolator;
import at.monol1th.pic1.core.interpolation.IInterpolator;
import at.monol1th.pic1.core.particles.ParticleList;
import at.monol1th.pic1.core.particles.movement.PeriodicBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.RelativisticLeapFrogMover;

/**
 * Created by David on 14.02.2015.
 */
public class Simulation {

	public Grid grid;
	public ParticleList particleList;
	public IFieldUpdater fieldUpdater;
	public IFieldSolver fieldSolver;
	public IInterpolator interpolator;

	public int isizex;
	public double dx;
	public double dt;
	public double speedOfLight;

	public Simulation(int isizex, double dx, double dt, double c)
	{
		this.isizex = isizex;
		this.dx = dx;
		this.dt = dt;
		this.speedOfLight = c;

		grid = new Grid(isizex, dx);
		particleList = new ParticleList(new RelativisticLeapFrogMover(), new PeriodicBoundaryConditions(), dt, c, isizex * dx);
		interpolator = new CICInterpolator();
		fieldUpdater = new LeapFrogFieldUpdater();
		fieldSolver  = new Poisson1DFieldSolver();
	}

	public void initialize()
	{
		interpolator.interpolateParticlesToChargeDensity(particleList, grid);
		fieldSolver.solveFields(grid, particleList);
		interpolator.interpolateFieldsToParticles(particleList, grid);
		particleList.initializeParticles();
	}

	public void update()
	{
		interpolator.interpolateFieldsToParticles(particleList, grid);
		particleList.updateParticles();
		interpolator.interpolateParticlesToCurrentDensities(particleList, grid, dt);
		fieldUpdater.updateFields(grid, dt);
	}
}
