import at.monol1th.pic1.benchmarks.PlasmaFrequencyTest;

import java.io.FileNotFoundException;

/**
 * Created by dmueller on 2/23/15.
 */
public class Main3 {
    public static void main(String[] args) {
        PlasmaFrequencyTest test = new PlasmaFrequencyTest();

        try {
            int points = 40;
            for (int t = 1; t <= points; t++) {
                System.out.println(t + "/" + points);
                test.runTests(2.0, t * Math.pow(10, 2), 1.0, 1.0, 10.0, 20000, "./data/plasma" + t + ".dat");
            }
        } catch (FileNotFoundException ex) {

        }

    }
}
