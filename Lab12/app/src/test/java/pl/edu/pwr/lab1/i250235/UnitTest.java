package pl.edu.pwr.lab1.i250235;

import org.junit.Test;

import java.util.Random;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void BMI_classTest() {
        Task bmi = new Task();

        //check if incorrect values work
        try {
            bmi.equationBMI(0,0, MainActivity.getMetricSys());
        }catch (IllegalArgumentException iae)
        {}

        try {
            bmi.equationBMI(0,0, !MainActivity.getMetricSys());
        }catch (IllegalArgumentException iae)
        {}

        try {
            bmi.equationBMI(300.1,251, MainActivity.getMetricSys());
        }catch (IllegalArgumentException iae)
        {}

        try {
            bmi.equationBMI(250.1,0.9, !MainActivity.getMetricSys());
        }catch (IllegalArgumentException iae)
        {}

        Random m = new Random();

        try {

            for(int i = 0; i < 100; i++)
            {
                double value1 = m.nextDouble();
                double value2 = m.nextDouble();

                double d = bmi.equationBMI(value1,value2, MainActivity.getMetricSys());
                double realSol = value1 / ((value2/100) * (value2/100));
                if(d != realSol)
                    throw new IllegalStateException();
            }

        }catch (Exception e){}
    }


}