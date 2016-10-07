package ch.ethz.coss.nervousnetgen.activities;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.nervousnet.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.virtual.clustering.Cluster;
import ch.ethz.coss.nervousnetgen.virtual.clustering.KMeans;
import ch.ethz.coss.nervousnetgen.virtual.clustering.iClustering;
import ch.ethz.coss.nervousnetgen.virtual.configuration.SensorConfiguration;
import ch.ethz.coss.nervousnetgen.virtual.configuration.VirtualConfigurationLoader;
import ch.ethz.coss.nervousnetgen.virtual.configuration.VirtualSensorConfiguration;
import ch.ethz.coss.nervousnetgen.nervousnet.database.iSensorQuery;
import ch.ethz.coss.nervousnetgen.virtual.database.VirtualSensorDBManager;
import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor.PossibleStatesHandler;
import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor_generator.CombineReadings;
import ch.ethz.coss.nervousnetgen.virtual.exceptions.NoData;
import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor.VirtualSensor;

/**
 * Created by ales on 01/10/16.
 */
public class VirtualMain {

    private ArrayList<VirtualSensorConfiguration> virtualSensorConfigurations;
    private iSensorQuery sensorDatabase;
    private Context context;

    public VirtualMain(Context context, iSensorQuery sensorDatabase) {
        this.virtualSensorConfigurations = VirtualConfigurationLoader.load(context);
        this.sensorDatabase = sensorDatabase;
        this.context = context;
    }

    public void periodic(int index) throws NoData {

        VirtualSensorConfiguration virtualSensorConf = virtualSensorConfigurations.get(index);

        // 1. get initial sensor data
        long stop = System.currentTimeMillis();
        long start = stop - virtualSensorConf.getSlidingWindow();


        // 2. get readings for all sensors
        ArrayList<ArrayList<SensorReading>> readings = new ArrayList<>();   // data
        ArrayList<String> virtualParamNames = new ArrayList<>();
        for (SensorConfiguration sensor : virtualSensorConf.getSensors()){
            String sensorID = sensor.getNameID();
            ArrayList<SensorReading> sensorReadings = sensorDatabase.getReadings(sensorID,
                    sensor.getParamNames(), start, stop, sensor.getParamNames());
            // TODO change from all to RANGE
            //ArrayList<SensorReading> sensorReadings = dataManager.getReadings(sensorID, sensor.getParamNames());
            readings.add(sensorReadings);
            virtualParamNames.addAll(sensor.getParamNames());
        }


        // 3. combine sensor readings
        ArrayList<VirtualSensor> virtualPoints =
                CombineReadings.combine(readings, virtualParamNames);
        Log.d("MAIN_VIRTUAL", "" + virtualPoints.size());
        for (VirtualSensor vs : virtualPoints) {
            vs.setCoordinates();
        }


        // CLUSTERING
        // TODO, num of clusters
        int numOfClusters = 5;
        iClustering clustering = new KMeans(virtualParamNames.size(), numOfClusters);
        ArrayList<Cluster> clusters = clustering.compute(virtualPoints);

        // STORE CLUSTERS/POSSIBLE STATES INTO DATABASE
        ArrayList<String> virtualParamType = new ArrayList<>();
        for (String s : virtualParamNames){
            virtualParamType.add("DOUBLE");
        }
        // TODO: specify correct unique name
        String uniqueName = "TESTname";
        PossibleStatesHandler psh = new PossibleStatesHandler(context, uniqueName,
                virtualParamNames, virtualParamType);

        for (int i = 0; i < clusters.size(); i ++) {
            Cluster c = clusters.get(i);
            psh.addPossibleState(i, c.getCoordinates());
        }

        Log.d("MAIN_VIRTUAL", "End of clustering");
        // STORE VIRTUAL SENSORS
        // TODO: unique name ... check notes, create tree, alphabetical order
        String vsUniqueName = "VStest";
        VirtualSensorDBManager vsdb = new VirtualSensorDBManager(context, vsUniqueName,
                virtualParamNames, virtualParamType);
        for (VirtualSensor vs : virtualPoints){
            // TODO timestamp has to be given by sensor merger
            long timestamp = System.currentTimeMillis();
            vsdb.store(timestamp, vs.getCluster(), vs.getCoordinates());
            Log.d("MAIN_VIRTUAL", "Store vs " + vs.getCluster());
        }

        Log.d("MAIN_VIRTUAL", "Virtual Sensors stored");
        // 4. periodic computation of new ones

    }



}
