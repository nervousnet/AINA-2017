package ch.ethz.coss.nervousnetgen.virtual;

import android.content.Context;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.virtual.configuration.SensorConfiguration;
import ch.ethz.coss.nervousnetgen.virtual.configuration.VirtualConfigurationLoader;
import ch.ethz.coss.nervousnetgen.virtual.configuration.VirtualSensorConfiguration;
import ch.ethz.coss.nervousnetgen.sensor.iDataManager;

/**
 * Created by ales on 01/10/16.
 */
public class VirtualMain {

    private ArrayList<VirtualSensorConfiguration> virtualSensorConfigurations;
    private iDataManager dataManager;

    public VirtualMain(Context context, iDataManager dataManager) {
        this.virtualSensorConfigurations = VirtualConfigurationLoader.load(context);
        this.dataManager = dataManager;
    }

    public void periodic(int index){

        VirtualSensorConfiguration virtualSensorConf = virtualSensorConfigurations.get(index);

        // 1. get initial sensor data
        long stop = System.currentTimeMillis();
        long start = stop - virtualSensorConf.getPeriodicWindowSize();

        // 2. get readings for all sensors
        ArrayList<ArrayList<SensorReading>> readings = new ArrayList<>();   // data
        ArrayList<String> virtualParamNames = new ArrayList<>();            // virtual param names

        for (SensorConfiguration sensor : virtualSensorConf.getSensors()){
            String sensorID = sensor.getNameID();
            ArrayList<SensorReading> sensorReadings = dataManager.getReadings(sensorID, start, stop,
                    sensor.getParamNames());
            readings.add(sensorReadings);
            virtualParamNames.addAll(sensor.getParamNames());
        }



        // 2. compute initial clusters

        // 3. add points to database

        // 4. periodic computation of new ones

    }



}
