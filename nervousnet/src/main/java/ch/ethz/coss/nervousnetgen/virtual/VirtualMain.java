package ch.ethz.coss.nervousnetgen.virtual;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.virtual.configuration.SensorConfiguration;
import ch.ethz.coss.nervousnetgen.virtual.configuration.VirtualConfigurationLoader;
import ch.ethz.coss.nervousnetgen.virtual.configuration.VirtualSensorConfiguration;
import ch.ethz.coss.nervousnetgen.database.iDatabaseManager;
import ch.ethz.coss.nervousnetgen.virtual.data.CombineReadings;

/**
 * Created by ales on 01/10/16.
 */
public class VirtualMain {

    private ArrayList<VirtualSensorConfiguration> virtualSensorConfigurations;
    private iDatabaseManager dataManager;

    public VirtualMain(Context context, iDatabaseManager dataManager) {
        this.virtualSensorConfigurations = VirtualConfigurationLoader.load(context);
        this.dataManager = dataManager;
    }

    public void periodic(int index){

        VirtualSensorConfiguration virtualSensorConf = virtualSensorConfigurations.get(index);

        // 1. get initial sensor data
        long stop = System.currentTimeMillis();
        long start = stop - virtualSensorConf.getSlidingWindow();

        // 2. get readings for all sensors
        ArrayList<ArrayList<SensorReading>> readings = new ArrayList<>();   // data
        ArrayList<String> virtualParamNames = new ArrayList<>();            // virtual param names

        for (SensorConfiguration sensor : virtualSensorConf.getSensors()){
            String sensorID = sensor.getNameID();
            //ArrayList<SensorReading> sensorReadings = dataManager.getReadings(sensorID, sensor.getParamNames(), start, stop,
            //        sensor.getParamNames());
            // TODO change from all to RANGE
            ArrayList<SensorReading> sensorReadings = dataManager.getReadings(sensorID, sensor.getParamNames());
            readings.add(sensorReadings);
            virtualParamNames.addAll(sensor.getParamNames());
        }

        // 3. combine sensor readings
        ArrayList<SensorReading> virtualPoints = CombineReadings.combine(readings, virtualParamNames);

        Log.d("MAIN_VIRTUAL", "" + virtualPoints.size());
        // 2. compute initial clusters

        // 3. add points to database

        // 4. periodic computation of new ones

    }



}
