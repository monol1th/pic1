package at.monol1th.pic1.core.observables;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 17.02.2015.
 */
public class TotalCharge {

	private Simulation s;

	public TotalCharge(Simulation s)
	{
		this.s = s;
	}

	public double computeTotalChargeFromGrid()
	{
		double Q = 0.0;
		for(int ix = 0; ix < s.isizex; ix++)
		{
			Q += s.grid.getCell(ix).d * s.dx;
		}
		return Q;
	}

	public double computeTotalChargeFromParticles()
	{
		double Q = 0.0;
		for(Particle p : s.particleManager.listOfParticles)
		{
			Q += p.q;
		}
		return Q;
	}
}
