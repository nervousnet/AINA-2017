package ch.ethz.coss.nervousnetgen.virtual.configuration;

import java.util.ArrayList;

/**
 * Created by ales on 01/10/16.
 */
public class VirtualSensorConfiguration {

    private ArrayList<SensorConfiguration> sensors;
    private long periodicWindowSize; // miliseconds
    private String virtualSensorName;

    private int numClusters = 5;

    public VirtualSensorConfiguration(ArrayList<SensorConfiguration> sensors, long periodicWindowSize) {
        this.sensors = sensors;
        this.periodicWindowSize = periodicWindowSize;
    }

    public VirtualSensorConfiguration(ArrayList<SensorConfiguration> sensors, long periodicWindowSize, String virtualSensorName) {
        this.sensors = sensors;
        this.periodicWindowSize = periodicWindowSize;
        this.virtualSensorName = virtualSensorName;
    }

    public ArrayList<SensorConfiguration> getSensors() {
        return sensors;
    }

    public long getPeriodicWindowSize() {
        return periodicWindowSize;
    }

    public String getVirtualSensorName() {
        return virtualSensorName;
    }

    public int getNumClusters() {
        return numClusters;
    }

    @Override
    public String toString() {
        return "VirtualSensorConfiguration{" +
                "sensors=" + sensors +
                ", periodicWindowSize=" + periodicWindowSize +
                ", virtualSensorName='" + virtualSensorName + '\'' +
                ", numClusters=" + numClusters +
                '}';
    }
}
