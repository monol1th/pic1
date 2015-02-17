package at.monol1th.pic1.core.particles;

import at.monol1th.pic1.core.particles.movement.IParticleBoundaryConditions;
import at.monol1th.pic1.core.particles.movement.IParticleMover;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 14.02.2015.
 */
public class ParticleList {

	public int numberOfParticles;
	public List<Particle> list;
	public IParticleMover particleMover;
	public IParticleBoundaryConditions particleBoundaryConditions;
	public double dt;
	public double size;
	public double c;

	public ParticleList(IParticleMover particleMover, IParticleBoundaryConditions particleBoundaryConditions, double dt, double c, double size) {
		list = new ArrayList<Particle>();
		this.particleMover = particleMover;
		this.particleBoundaryConditions = particleBoundaryConditions;
		this.dt = dt;
		this.c = c;
		this.size = size;
	}

	public void addParticle(Particle p) {
		list.add(p);
		numberOfParticles = list.size();
	}

	public void updateParticles()
	{
		for (Particle p : list) {
			this.particleMover.updateParticle(p, this.dt, this.c);
			this.particleBoundaryConditions.applyBoundaryConditions(p, size);
		}
	}

	public void initializeParticles()
	{
		for (Particle p : list) {
			this.particleMover.initializeParticle(p, this.dt, this.c);
			this.particleBoundaryConditions.applyBoundaryConditions(p, size);
		}
	}
}
