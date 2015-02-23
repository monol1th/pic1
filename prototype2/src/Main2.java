import at.monol1th.pic1.benchmarks.TwoParticleCenterOfMassTest;

import java.io.FileNotFoundException;

/**
 * Created by David on 21.02.2015.
 */
public class Main2
{
    public static void main(String[] args)
    {
        TwoParticleCenterOfMassTest test = new TwoParticleCenterOfMassTest();

        double[] dts = {0.00001};

	    try {
		    test.runTests(dts, 10000, "NGP", 0, "ngptests.dat");
		    test.runTests(dts, 10000, "CIC", 0, "cictests.dat");
	    }
	    catch(FileNotFoundException ex) {
	    }
	}
}
