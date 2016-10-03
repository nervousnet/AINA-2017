package ch.ethz.coss.nervousnetgen.nervousnet.sensor_wrappers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

import ch.ethz.coss.nervousnetgen.database.Constants;
import ch.ethz.coss.nervousnetgen.nervousnet.database.Store;
import ch.ethz.coss.nervousnetgen.nervousnet.database.iStore;

/**
 * Created by ales on 20/09/16.
 */
public class Wrapper_v2 implements iWrapper, SensorEventListener {

    private static final String LOG_TAG = Wrapper_v2.class.getSimpleName();

    private final SensorManager mSensorManager;
    private final Sensor sensor;
    private iStore databaseHandler;

    private final String sensorName;
    private final String[] parametersNames;
    private final String[] typeNames;
    private final String[] metadata;
    private int samplingPeriod;
    private final int[] parametersPositions;

    // Extra for testing
    private iStore databaseHandlerCommonTable;


    // This is actually extraction from config file
    public Wrapper_v2(Context context, String sensorName, String[] paramNames, String[] typeNames, String[] metadata,
                      int sensorType, int samplingPeriod, int[] parametersPositions){


        String[] columnNames = new String[3+paramNames.length];
        String[] columnTypes = new String[3+paramNames.length];

        columnNames[0] = "timestamp";
        columnTypes[0] = "BIGINT";

        columnNames[1] = "metadata";
        columnTypes[1] = "TEXT";

        columnNames[2] = "samplingPeriod";
        columnTypes[2] = "INT";
        for (int i = 0; i < paramNames.length; i++){
            columnNames[i+3] = paramNames[i];
            switch (typeNames[i]){
                case "int":
                    columnTypes[i+3] = "INT";
                    break;
                case "double":
                    columnTypes[i+3] = "REAL";
                    break;
                case "String":
                    columnTypes[i+3] = "TEXT";
            }
        }


        this.databaseHandler = new Store(context, sensorName, columnNames, columnTypes);

        this.sensorName = sensorName;
        this.parametersNames = paramNames;
        this.typeNames = typeNames;
        this.metadata = metadata;
        this.samplingPeriod = samplingPeriod;
        this.parametersPositions = parametersPositions;

        this.mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        this.sensor = mSensorManager.getDefaultSensor(sensorType);
    }

    @Override
    public boolean start() {
        Log.d(LOG_TAG, "Register sensor ...");
        mSensorManager.registerListener(this, sensor, samplingPeriod);
        return true;
    }

    @Override
    public boolean stop() {
        Log.d(LOG_TAG, "Unregister sensor ...");
        mSensorManager.unregisterListener(this, this.sensor);
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long timestamp = System.currentTimeMillis();
        //Log.d(LOG_TAG, "onSenesorChanged activated ...");
        Log.d(LOG_TAG, "Reading " + sensorName + "...");

        // Create element, to store all data to
        HashMap<String, Object> sample = new HashMap<>();

        // 1. Insert timestamp first
        sample.put(Constants.TIMESTAMP, timestamp);
        sample.put("samplingPeriod" , samplingPeriod);
        sample.put("metadata" , Arrays.toString(metadata));
        // 2. Insert values
        Log.d(LOG_TAG, "timestamp " + sample.get("timestamp") + " sampling " + sample.get("samplingPeriod") + " metadata " + sample.get("metadata") );
        for (int i = 0; i < parametersPositions.length; i++) {
            Log.d(LOG_TAG, "Put (key, value) = (" + parametersNames[i] + ", " + sensorEvent.values[parametersPositions[i]] + ")");
            sample.put(parametersNames[i], sensorEvent.values[parametersPositions[i]]);
        }
                databaseHandler.store(sample);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
