package ch.ethz.coss.nervousnetgen.activities;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

import ch.ethz.coss.nervousnetgen.nervousnet.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.test.StoreStrings;
import ch.ethz.coss.nervousnetgen.virtual.clustering.Cluster;
import ch.ethz.coss.nervousnetgen.virtual.clustering.ClusteringException;
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

        ArrayList<String> virtualParamType = new ArrayList<>();
        for (String s : virtualParamNames){
            virtualParamType.add("DOUBLE");
        }

        // 3. combine sensor readings
        ArrayList<VirtualSensor> virtualPoints =
                CombineReadings.combine(readings, virtualParamNames, virtualSensorConf.getSamplingRate());

        Log.d("MAIN_VIRTUAL", "" + virtualPoints.size());

        for (VirtualSensor vs : virtualPoints) {
            vs.setCoordinates();
        }


        // CLUSTERING
        // TODO, num of clusters
        int numOfClusters = 5;
        int dimensions = virtualParamNames.size();
        iClustering clustering = new KMeans(dimensions, numOfClusters);
        ArrayList<Cluster> clusters = null;
        try {
            clusters = clustering.compute(virtualPoints);
        } catch (ClusteringException e) {
            e.printStackTrace();
        }

        storePossibleStates(virtualParamNames, virtualParamType, clusters);
        storeVirtualSensors(virtualParamNames, virtualParamType, virtualPoints);

    }

    private void storePossibleStates(ArrayList<String> virtualParamNames,
                                     ArrayList<String> virtualParamType,
                                     ArrayList<Cluster> clusters){
        // STORE CLUSTERS/POSSIBLE STATES INTO DATABASE
        // TODO: specify correct unique name
        String uniqueName = "TESTname";
        PossibleStatesHandler psh = new PossibleStatesHandler(context, uniqueName,
                virtualParamNames, virtualParamType);

        for (int i = 0; i < clusters.size(); i ++) {
            Cluster c = clusters.get(i);
            psh.addPossibleState(i, c.getCoordinates());
        }

    }

    private void storeVirtualSensors(ArrayList<String> virtualParamNames,
                                     ArrayList<String> virtualParamType,
                                     ArrayList<VirtualSensor> virtualPoints){
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
    }

    public void testClustering() throws NoData {

        VirtualSensorConfiguration virtualSensorConf = virtualSensorConfigurations.get(0);

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

        ArrayList<String> virtualParamType = new ArrayList<>();
        for (String s : virtualParamNames){
            virtualParamType.add("DOUBLE");
        }


        StoreStrings storeStrings = new StoreStrings(context);


        ArrayList<String> results = new ArrayList<>();

        int[] rateArr = new int[]{60000, 30000, 10000, 1000};
        int[] dimArr = new int[]{1, 2, 3, 4};
        int[] clusArr = new int[]{3, 5, 7};

        int nRepetitions = 20;

        for (int samplingRate : rateArr){
            for (int dimensions : dimArr){
                for (int numOfClusters : clusArr){
                    // 3. combine sensor readings
                    ArrayList<VirtualSensor> virtualPoints =
                            CombineReadings.combine(readings, virtualParamNames, samplingRate);

                    int VPSIZE = virtualPoints.size();

                    Log.d("MAIN_VIRTUAL", "" + virtualPoints.size());

                    for (VirtualSensor vs : virtualPoints) {
                        vs.setCoordinates();
                    }


                    iClustering clustering = new KMeans(dimensions, numOfClusters);
                    long sum = 0;

                    long diff = 0;
                    for (int i = 0; i < nRepetitions; i++) {
                        long start_ = 0;
                        long stop_ = 0;

                        boolean repeat = true;
                        do {
                            try {
                                start_ = System.currentTimeMillis();
                                clustering.compute(virtualPoints);
                                stop_ = System.currentTimeMillis();
                                diff = stop_ - start_;
                                sum += diff;
                                repeat = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                                repeat = true;
                            }
                        } while (repeat);
                    }


                    double measuredTime = ((double)sum) / nRepetitions;
                    String str = "rate=" + samplingRate + " n_virtSens=" + VPSIZE + " dim=" + dimensions + " n_clus=" + numOfClusters + " time=" + measuredTime;
                    storeStrings.store(str);
                    results.add(str);
                    printString(results);
                }
            }
        }

        HashMap<Long, String> strings = storeStrings.getAll();
        ArrayList<Long> array = new ArrayList(new TreeSet(strings.keySet()));
        for (Long l : array){
            Log.d("TIME_PRINT_DB", strings.get(l));
        }


    }

    public void printString(ArrayList<String> list){
        for (String str : list){
            Log.d("TIME_PRINT", str);
        }
    }
}
