package ch.ethz.coss.nervousnetgen.virtual.configuration;

import java.util.ArrayList;

/**
 * Created by ales on 01/10/16.
 */
public class VirtualSensorConfiguration {

    private ArrayList<SensorConfiguration> sensors;
    private long samplingRate; // miliseconds
    private long slidingWindow; //miliseconds
    private String virtualSensorName;

    private int numClusters = 5;

    public VirtualSensorConfiguration(ArrayList<SensorConfiguration> sensors, long samplingPeriod,
                                      long slidingWindow) {
        this.sensors = sensors;
        this.samplingRate = samplingPeriod;
        this.slidingWindow = slidingWindow;
    }

    public VirtualSensorConfiguration(ArrayList<SensorConfiguration> sensors, long samplingPeriod,
                                      long slidingWindow, String virtualSensorName) {
        this.sensors = sensors;
        this.samplingRate = samplingPeriod;
        this.slidingWindow = slidingWindow;
        this.virtualSensorName = virtualSensorName;
    }


    public ArrayList<SensorConfiguration> getSensors() {
        return sensors;
    }

    public long getSamplingRate() {
        return samplingRate;
    }

    public long getSlidingWindow() {
        return slidingWindow;
    }

    public String getVirtualSensorName() {
        return virtualSensorName;
    }

    public int getNumClusters() {
        return numClusters;
    }
}
