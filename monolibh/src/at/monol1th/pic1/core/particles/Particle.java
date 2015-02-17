package at.monol1th.pic1.core.particles;

/**
 * Created by David on 14.02.2015.
 */
public class Particle {
	public double x;            /*  Particle position in spatial coordinates (evaluated at it)      */
	public double px;           /*  Particle momentum in spatial coordiantes (evaluated at it+0.5)  */
	public double vx;           /*  Particle velocity in spatial coordiantes (evaluated at it+0.5)  */
	public double q;            /*  Particle charge     */
	public double m;            /*  Particle mass       */

	public double Ex;           /*  Electrical field at particle position   */
}
