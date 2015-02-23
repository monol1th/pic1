package at.monol1th.pic1.core.observables;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 22.02.2015.
 */
public class Momentum
{
	private Simulation s;
	public Momentum(Simulation s)
	{
		this.s = s;
	}

	public double computeTotalParticleMomentum()
	{
		double momentum = 0.0;
		for(Particle p : s.particleManager.listOfParticles)
		{
			momentum += p.px;
		}
		return momentum;
	}
}
