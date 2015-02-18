package at.monol1th.pic1.core.particles;

import at.monol1th.pic1.core.particles.movement.IParticleBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.IParticleMover;
import at.monol1th.pic1.core.settings.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 14.02.2015.
 */
public class ParticleManager
{

    public int numberOfParticles;
    public List<Particle> listOfParticles;
    public IParticleMover particleMover;
    public IParticleBoundaryConditions particleBoundaryConditions;
    public double dt;
    public double size;
    public double c;

    public ParticleManager(Settings settings)
    {
        listOfParticles = new ArrayList<Particle>();
        this.particleMover = settings.particleMover;
        this.particleBoundaryConditions = settings.particleBoundaryConditions;
        this.dt = settings.timeStep;
        this.c = settings.speedOfLight;
        this.size = settings.gridSize * settings.gridSpacing;
    }

    public void addParticle(Particle p)
    {
        listOfParticles.add(p);
        numberOfParticles = listOfParticles.size();
    }

    public void addParticles(List<Particle> list)
    {
        for (Particle p : list)
        {
            addParticle(p);
        }
    }

    public void updateParticles()
    {
        for (Particle p : listOfParticles)
        {
            this.particleMover.updateParticle(p, this.dt, this.c);
            this.particleBoundaryConditions.applyBoundaryConditions(p, size);
        }
    }

    public void initializeParticles()
    {
        for (Particle p : listOfParticles)
        {
            this.particleMover.initializeParticle(p, this.dt, this.c);
            this.particleBoundaryConditions.applyBoundaryConditions(p, size);
        }
    }
}
