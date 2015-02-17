package at.monol1th.pic1.core.grid.solver;

import at.monol1th.pic1.core.grid.Grid;
import at.monol1th.pic1.core.particles.ParticleManager;

/**
 * Created by David on 14.02.2015.
 */
public interface IFieldSolver {
	public void solveFields(Grid g, ParticleManager pL);
}
