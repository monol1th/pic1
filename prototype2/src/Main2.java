import at.monol1th.pic1.benchmarks.TwoParticleCenterOfMassTest;

/**
 * Created by David on 21.02.2015.
 */
public class Main2
{
    public static void main(String[] args)
    {
        TwoParticleCenterOfMassTest test = new TwoParticleCenterOfMassTest();

        double[] dts = {0.05,0.01,0.005,0.001,0.0005,0.0001,0.00005,0.00001};

        test.runTests(dts, 1, "NGP", 234);
    }
}
