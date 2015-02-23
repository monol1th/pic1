package at.monol1th.pic1.core.observables;

import at.monol1th.pic1.core.Simulation;
import at.monol1th.pic1.core.grid.Cell;
import at.monol1th.pic1.core.particles.Particle;

/**
 * Created by dmueller on 2/19/15.
 */
public class Energy {
    private Simulation s;

    public Energy(Simulation s) {
        this.s = s;
    }

    public double computeParticleEnergy() {
        double energy = 0.0;
        double c = s.settings.speedOfLight;
        for (Particle p : s.particleManager.listOfParticles) {
            energy += Math.sqrt(Math.pow(p.m * c * c, 2) + Math.pow(p.px * c, 2)) - p.m * c * c;
        }
        return energy;
    }

    public double computeFieldEnergy() {
        double energy = 0.0;
        for (Cell cell : s.grid.cells) {
            energy += Math.pow(cell.Ex, 2) * s.grid.dx;
        }
        return energy / 2.0;
    }

	public double computeInteractionEnergy() {
		double energy = 0.0;
		for (Cell cell : s.grid.cells) {
			energy += cell.jx * cell.Ex * s.grid.dx;
		}
		return energy;
	}
}
