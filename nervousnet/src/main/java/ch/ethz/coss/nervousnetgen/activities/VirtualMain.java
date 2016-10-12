package ch.ethz.coss.nervousnetgen.activities;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.GraphView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
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
import ch.ethz.coss.nervousnetgen.virtual.plot.GraphPlot;
import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor.PossibleStatesHandler;
import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor_generator.CombineReadings;
import ch.ethz.coss.nervousnetgen.virtual.exceptions.NoData;
import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor.VirtualSensor;
import weka.clusterers.EM;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by ales on 01/10/16.
 */
public class VirtualMain {

    private ArrayList<VirtualSensorConfiguration> virtualSensorConfigurations;
    private iSensorQuery sensorDatabase;
    private Context context;
    private GraphView graphView;




    public VirtualMain(Context context, iSensorQuery sensorDatabase) {
        this.virtualSensorConfigurations = VirtualConfigurationLoader.load(context);
        this.sensorDatabase = sensorDatabase;
        this.context = context;
    }

    public VirtualMain(Context context, iSensorQuery sensorDatabase, GraphView graph) {
        this.virtualSensorConfigurations = VirtualConfigurationLoader.load(context);
        this.sensorDatabase = sensorDatabase;
        this.context = context;
        this.graphView = graph;
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





    public ArrayList<String> performanceListString = new ArrayList<>();

    public void testClustering() throws NoData {


        ArrayList<String> results = new ArrayList<>();
        performanceListString = results;
        results.add("First string test");

        for (VirtualSensorConfiguration virtualSensorConf : virtualSensorConfigurations) {

            String sensorName = virtualSensorConf.getVirtualSensorName();

            // 1. get initial sensor data
            // TODO
            long stop = 1476201600000l; //System.currentTimeMillis();
            long start = stop - 86400000;// capture 1 day

            // 2. get readings for all sensors

            ArrayList<String> virtualParamNames = new ArrayList<>();
            for (SensorConfiguration sensor : virtualSensorConf.getSensors()) {
                virtualParamNames.addAll(sensor.getParamNames());
            }

            ArrayList<String> virtualParamType = new ArrayList<>();
            for (String s : virtualParamNames) {
                virtualParamType.add("DOUBLE");
            }


            //StoreStrings storeALL = new StoreStrings(context, "MeasurementsALL"+sensorName);
            StoreStrings storeAVG = new StoreStrings(context, "MeasurementsAVG"+sensorName);

            int[] rateArr = new int[]{60000, 30000, 10000, 1000};
            //int[] rateArr = new int[]{6000000};

            int dim = virtualParamNames.size();

            for (int samplingRate : rateArr) {
                int step = samplingRate;

                long threshold = start;
                for (SensorConfiguration sensor : virtualSensorConf.getSensors()) {
                    // FIND FIRST
                    boolean firstFound = false;
                    ArrayList<SensorReading> sensorReadings = new ArrayList<>();
                    do {
                         sensorReadings = sensorDatabase.getLatestReadingUnderRange(
                                sensor.getNameID(), sensor.getParamNames(), 0, threshold,//TODO
                                sensor.getParamNames());
                        if (!sensorReadings.isEmpty()){
                            // We found something
                            break;
                        }
                        else {
                            // Increse threshold
                            threshold += step;
                        }
                    } while (threshold < stop);

                    if (sensorReadings.isEmpty()){
                        // if there is no data for some sensor, then we can not do anything,
                        // there is no data
                        return;
                    }

                    // here, we found something and we go to the next sensor
                }
                // We have a threshold, that we find something
                ArrayList<VirtualSensor> virtualSensors = new ArrayList<>();

                do {

                    VirtualSensor virtualSensor = new VirtualSensor();

                    // Set timestamp of the combined virtual point

                    virtualSensor.setTimestampEpoch(threshold);
                    virtualSensor.setParametersNames(virtualParamNames);
                    Object[] originalValues = virtualSensor.getValues(); // get array to fill it

                    // Fill the VirtualSensor
                    int pos = 0;

                    for (SensorConfiguration sensor : virtualSensorConf.getSensors()) {
                        SensorReading reading = sensorDatabase.getLatestReadingUnderRange(
                                sensor.getNameID(), sensor.getParamNames(), 0, threshold, // TODO, change 0 to start
                                sensor.getParamNames()).get(0);
                        Object[] vals = reading.getValues();
                        for (int i = 0; i < vals.length; i++)
                            originalValues[pos++] = vals[i];
                    }
                    // TODO PATCH
                    virtualSensor.setCoordinates();
                    virtualSensors.add(virtualSensor);

                    threshold += step;

                } while (threshold < stop );

                int VPSIZE = virtualSensors.size();

                int wekaNumOfClusters = numOfClusters(virtualSensors);
                Log.d("WEKA", "Number of clusters by WEKA = " + wekaNumOfClusters);

                int numOfClusters = wekaNumOfClusters;

                    iClustering clustering = new KMeans(virtualParamNames.size(), numOfClusters);

                    int nRepetitions = 30;
                    double[] times = new double[nRepetitions];
                    for (int i = 0; i < nRepetitions; i++) {
                        long start_ = 0;
                        long stop_ = 0;

                        boolean repeat = true;
                        do {
                            try {
                                start_ = System.currentTimeMillis();
                                clustering.compute(virtualSensors);
                                stop_ = System.currentTimeMillis();
                                double diff = (double)(stop_ - start_);
                                times[i] = diff;
                                //storeALL.store(samplingRate, VPSIZE, dim, numOfClusters, diff, 0);
                                repeat = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                                repeat = true;
                            }
                        } while (repeat);
                    }

                    // Sum
                    long sum = 0;
                    for (int i = 0; i < nRepetitions; i++){
                        sum += times[i];
                    }

                    // Average
                    double average = (double)sum / (double)nRepetitions;

                    // Standard Deviation
                    // http://www.miniwebtool.com/sample-standard-deviation-calculator/
                    double sqSum = 0;
                    for (int i = 0; i < nRepetitions; i++){
                        double diff = times[i] - average;
                        sqSum += diff * diff;
                    }
                    double sd = Math.sqrt( sqSum / (nRepetitions - 1) );

                    String str = sensorName + ": rate=" + samplingRate + " n=" + VPSIZE + " dim=" + dim + " cl=" + numOfClusters + " t=" + average + " sd=" + sd;
                    storeAVG.store(samplingRate, VPSIZE, dim, numOfClusters, average, sd);
                    results.add(str);
                    printString(results);
            }

            //HashMap<Long, String> strings = storeStrings.getAll();
            //ArrayList<Long> array = new ArrayList(new TreeSet(strings.keySet()));
            //for (Long l : array){
            //    Log.d("TIME_PRINT_DB", strings.get(l));
            //}


        }
    }


    public void printString(ArrayList<String> list){
        for (String str : list){
            Log.d("TIME_PRINT", str);
        }
    }


    private int numOfClusters(ArrayList<VirtualSensor> virtualPoints){

        FastVector atts = new FastVector();
        for (int i = 0; i < virtualPoints.get(0).getDimensions(); i++)
            atts.addElement(new Attribute("Attr"+i));

        Instances test = new Instances("kmeans", atts, 0);

        for (VirtualSensor vs : virtualPoints){
            double[] vals = vs.getCoordinates();
            test.add(new Instance(1.0, vals));
        }

        EM em = new EM();
        int numOfIterations = 300;
        double minStdDeviation = 0.0001;
        try {
            em.setMaxIterations(numOfIterations);
            em.setMinStdDev(minStdDeviation);
            em.setNumClusters(-1);
            em.buildClusterer(test);
            int automaticNumOfClusters = em.numberOfClusters();
            return automaticNumOfClusters;
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }



    public void printReadings(){

        VirtualSensorConfiguration virtualSensorConf = virtualSensorConfigurations.get(1);

        String sensorName = virtualSensorConf.getVirtualSensorName();

        // 1. get initial sensor data
        // TODO
        long stop = 1476201600000l; //System.currentTimeMillis(); //1476201600000l
        long start = stop - 86400000;// capture 1 day

        // 2. get readings for all sensors
        ArrayList<ArrayList<SensorReading>> readings = new ArrayList<>();   // data
        ArrayList<String> virtualParamNames = new ArrayList<>();
        for (SensorConfiguration sensor : virtualSensorConf.getSensors()) {
            String sensorID = sensor.getNameID();
            ArrayList<SensorReading> sensorReadings = sensorDatabase.getReadings(sensorID,
                    sensor.getParamNames(), start, stop, sensor.getParamNames());
            readings.add(sensorReadings);
            virtualParamNames.addAll(sensor.getParamNames());
        }

        ArrayList<SensorReading> reads = readings.get(0);

        for (SensorReading r : reads){
            Log.d("PRINT-READING", r.getTimestampEpoch() + " " + r.getValues());
        }
    }


    public void plotVS(){


        // TODO: GRAPH VIEW CAN BE CALLED ONLY FROM THE MAIN THREAD

        int SAMPLING_RATE = 60000;

        GraphPlot graph = new GraphPlot(graphView);




        VirtualSensorConfiguration virtualSensorConf = virtualSensorConfigurations.get(0);

        String sensorName = virtualSensorConf.getVirtualSensorName();

        // 1. get initial sensor data
        // TODO
        long stop = 1476201600000l; //System.currentTimeMillis();
        long start = stop - 86400000;// capture 1 day

        // 2. get readings for all sensors
        ArrayList<ArrayList<SensorReading>> readings = new ArrayList<>();   // data
        ArrayList<String> virtualParamNames = new ArrayList<>();
        for (SensorConfiguration sensor : virtualSensorConf.getSensors()) {
            String sensorID = sensor.getNameID();
            ArrayList<SensorReading> sensorReadings = sensorDatabase.getReadings(sensorID,
                    sensor.getParamNames(), start, stop, sensor.getParamNames());
            readings.add(sensorReadings);
            virtualParamNames.addAll(sensor.getParamNames());
        }

        ArrayList<VirtualSensor> virtualPoints = null;
        try {
            virtualPoints =
                    CombineReadings.combine(readings, virtualParamNames, SAMPLING_RATE);
            for (VirtualSensor vs : virtualPoints) {
                vs.setCoordinates();
            }
        } catch (NoData noData) {
            noData.printStackTrace();
        }

        graph.plot(virtualPoints);
    }

    public void numClusters(){
        int SAMPLING_RATE = 60000;

        VirtualSensorConfiguration virtualSensorConf = virtualSensorConfigurations.get(1);

        String sensorName = virtualSensorConf.getVirtualSensorName();

        // 1. get initial sensor data
        // TODO
        long stop = 1476201600000l; //System.currentTimeMillis(); //1476201600000l
        long start = stop - 86400000;// capture 1 day

        // 2. get readings for all sensors
        ArrayList<ArrayList<SensorReading>> readings = new ArrayList<>();   // data
        ArrayList<String> virtualParamNames = new ArrayList<>();
        for (SensorConfiguration sensor : virtualSensorConf.getSensors()) {
            String sensorID = sensor.getNameID();
            ArrayList<SensorReading> sensorReadings = sensorDatabase.getReadings(sensorID,
                    sensor.getParamNames(), start, stop, sensor.getParamNames());
            readings.add(sensorReadings);
            virtualParamNames.addAll(sensor.getParamNames());
        }

        ArrayList<VirtualSensor> virtualPoints = null;
        try {
            virtualPoints =
                    CombineReadings.combine(readings, virtualParamNames, SAMPLING_RATE);
            for (VirtualSensor vs : virtualPoints) {
                vs.setCoordinates();
            }
        } catch (NoData noData) {
            noData.printStackTrace();
        }

        int wekaNumOfClusters = numOfClusters(virtualPoints);
        Log.d("WEKA", "Number of clusters by WEKA = " + wekaNumOfClusters);

    }
}
