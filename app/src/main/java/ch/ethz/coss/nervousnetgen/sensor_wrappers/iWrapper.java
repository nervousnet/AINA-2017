package ch.ethz.coss.nervousnetgen.sensor_wrappers;

import android.hardware.SensorEventListener;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.SensorReading;

/**
 * Created by ales on 26/09/16.
 */
public interface iWrapper {
    public boolean start();
    public boolean stop();
}
