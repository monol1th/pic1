package at.monol1th.pic1.core;

import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.grid.solver.IFieldSolver;
import at.monol1th.pic1.core.grid.updater.IFieldUpdater;
import at.monol1th.pic1.core.interpolation.IInterpolator;
import at.monol1th.pic1.core.particles.ParticleManager;
import at.monol1th.pic1.core.settings.Settings;

/**
 * Created by David on 14.02.2015.
 */
public class Simulation
{

    public Settings settings;

    public Grid grid;
    public ParticleManager particleManager;
    public IFieldUpdater fieldUpdater;
    public IFieldSolver fieldSolver;
    public IInterpolator interpolator;

    public int isizex;
    public double dx;
    public double dt;
    public double speedOfLight;

    public int computationalSteps;
    public double elapsedTime;

    public Simulation(Settings settings)
    {
        this.settings = settings;

        this.isizex = settings.gridSize;
        this.dx = settings.gridSpacing;
        this.dt = settings.timeStep;
        this.speedOfLight = settings.speedOfLight;

        grid = new Grid(isizex, dx);
        particleManager = new ParticleManager(settings);
        particleManager.addParticles(settings.listOfParticles);
        interpolator = settings.interpolationMethod;
        fieldUpdater = settings.fieldUpdater;
        fieldSolver = settings.fieldSolver;
    }

    public void initialize()
    {
        computationalSteps = 0;

        interpolator.interpolateParticlesToChargeDensity(particleManager, grid);
        fieldSolver.solveFields(grid, particleManager);
        interpolator.interpolateFieldsToParticles(particleManager, grid);
        particleManager.initializeParticles();
    }

    public void update()
    {
        computationalSteps++;
        elapsedTime = computationalSteps * dt;

        interpolator.interpolateFieldsToParticles(particleManager, grid);
        particleManager.updateParticles();
        interpolator.interpolateParticlesToChargeDensity(particleManager, grid);
        interpolator.interpolateParticlesToCurrentDensities(particleManager, grid, dt);
        fieldUpdater.updateFields(grid, dt);
    }
}
