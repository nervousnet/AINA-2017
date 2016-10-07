package ch.ethz.coss.nervousnetgen.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import ch.ethz.coss.nervousnetgen.R;
import ch.ethz.coss.nervousnetgen.nervousnet.database.SensorQuery;
import ch.ethz.coss.nervousnetgen.nervousnet.configuration.ConfigurationClass;
import ch.ethz.coss.nervousnetgen.nervousnet.configuration.ConfigurationLoader;
import ch.ethz.coss.nervousnetgen.nervousnet.sensor_wrappers.Wrapper_v2;
import ch.ethz.coss.nervousnetgen.nervousnet.sensor_wrappers.Wrapper_v3;
import ch.ethz.coss.nervousnetgen.nervousnet.sensor_wrappers.iWrapper;
import ch.ethz.coss.nervousnetgen.virtual.exceptions.NoData;

public class MainActivity extends Activity {

    private ArrayList<iWrapper> wrappers = new ArrayList<>();
    private HashMap<String, ConfigurationClass> sensors = new HashMap<>();
    Button buttonStartNervousnet;
    Button buttonStopNervousnet;
    Button buttonStartVirtual;
    Button buttonStopVirtual;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// Initialize button for get All sensor data

        context = this.getApplicationContext();

        buttonStartNervousnet = (Button) findViewById(R.id.button_start_nervousnet);
        buttonStartNervousnet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "Start button clicked");
                startAllSensors();
            }
        });

        buttonStopNervousnet = (Button) findViewById(R.id.button_stop_nervousnet);
        buttonStopNervousnet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "Stop button clicked");
                stopAllSensors();
            }
        });

        buttonStartVirtual = (Button) findViewById(R.id.button_start_virtual);
        buttonStartVirtual.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startVirtual(context);
            }
        });

        buttonStopVirtual = (Button) findViewById(R.id.button_stop_virtual);
        buttonStopVirtual.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        wrapperConfiguration();
    }


    public void wrapperConfiguration(){
        ConfigurationLoader confLoader = new ConfigurationLoader(this);
        ArrayList<ConfigurationClass> confClassList = confLoader.load();
        //databaseHelper = new DatabaseHelper(this);

        //databaseHelper.deleteTable(sensorName);

        for ( ConfigurationClass cc : confClassList) {

            // TODO: select right Wrapper
            String chooseWrapper = "Wrapper_v3";
            iWrapper wrapper = null;
            switch (chooseWrapper){
                case "Wrapper1":
                   /* wrapper = new Wrapper1(this, databaseHelper, cc.getSensorName(),
                            cc.getParametersNames(), cc.getParametersTypes(), cc.getMetadata(),
                            cc.getAndroidSensorType(), cc.getSamplingPeriod(),
                            cc.getAndroidParametersPositions());
                    wrappers.add(wrapper);*/
                    break;
                case "Wrapper_v2":
                    wrapper = new Wrapper_v2(this, cc.getSensorName(),
                            cc.getParametersNames(), cc.getParametersTypes(), cc.getMetadata(),
                            cc.getAndroidSensorType(), cc.getSamplingPeriod(),
                            cc.getAndroidParametersPositions());
                    break;

                case "Wrapper_v3":
                    wrapper = new Wrapper_v3(this, cc);
                    break;
                default:
                    // do nothing, ignore
                    Log.d("MAIN", "ERROR - wrapper not supported in main activity class");
            }
            wrappers.add(wrapper);
            sensors.put(cc.getSensorName(), cc);
            Log.d("MAIN", cc.getSensorName() + " DONE");
        }
    }

    public void startAllSensors(){
        for ( iWrapper wrapper : wrappers) {
            wrapper.start();
        }
    }

    public void stopAllSensors(){
        for ( iWrapper wrapper : wrappers ){
            wrapper.stop();
        }
    }

    public void startVirtual(Context context){
        VirtualMain vm = new VirtualMain(context, new SensorQuery(context));
        //TODO for now, run only first virtual sensor
        try {
            vm.periodic(0);
            Log.d("MAIN", "Finish virtual");
        } catch (NoData noData) {
            Log.d("MAIN-ERROR", noData.getMessage());
        }
    }


    /*private void initGetAll(final Context context){
        allButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "All button clicked");
                *//*for ( ConfigurationClass cc : confClassList) {
                    databaseHelper.printTable(cc.getSensorName());
                }*//*
                QueryRich query = new QueryRich(context);
                for ( String sensorName : sensors.keySet() ) {
                    ArrayList<SensorReading> result = query.getAll(sensors.get(sensorName));
                    Log.d("GET ALL", result.toString());
                }
            }
        });
    }*/

}
