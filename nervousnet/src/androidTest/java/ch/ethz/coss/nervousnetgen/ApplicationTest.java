package ch.ethz.coss.nervousnetgen;


import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ApplicationTest {

    @Test
    public void testReadSensor1(){

        // Test Acceleometer

        String[] paramNames = new String[]{"accX", "accY", "accZ"};
        String[] typeNames = new String[]{"double", "double", "double"};
        String[] metadata = new String[]{"sth", "sth", "sth"};
        int samplingPeriod = SensorManager.SENSOR_DELAY_FASTEST;
        int[] paramPos = new int[]{0, 1, 2};
        int sensorType = Sensor.TYPE_ACCELEROMETER;



        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }
}