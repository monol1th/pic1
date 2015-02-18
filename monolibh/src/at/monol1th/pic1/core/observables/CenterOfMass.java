package at.monol1th.pic1.core.observables;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by David on 19.02.2015.
 */
public class CenterOfMass
{
    private Simulation s;

    public CenterOfMass(Simulation s)
    {
        this.s = s;
    }

    public double computeCenterOfMass()
    {
        double centerOfMass = 0.0;
        for(Particle p : s.particleManager.listOfParticles)
        {
            centerOfMass += p.x * p.m;
        }
        return centerOfMass;
    }

}
