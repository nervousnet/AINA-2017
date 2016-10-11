package ch.ethz.coss.nervousnetgen.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;

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
    Button buttonPrintPerformance;
    GraphView graphView;

    Context context;

    VirtualMain vm;

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
                //numOfClust(context);
                printReadings(context);
            }
        });

        buttonPrintPerformance = (Button) findViewById(R.id.button_print);
        buttonPrintPerformance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vm.printString(vm.performanceListString);
            }
        });

        graphView = (GraphView) findViewById(R.id.graph);

        wrapperConfiguration();
        vm = new VirtualMain(context, new SensorQuery(context));


        /*Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startAllSensors();
            }
        };

        thread.start();*/
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
        //TODO for now, run only first virtual sensor

        // TODO test
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //vm.plotVS();
                    vm.testClustering();
                    //vm.periodic(0);
                    Log.d("MAIN", "Finish virtual");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }


    public void numOfClust(Context context){
        //TODO for now, run only first virtual sensor

        // TODO test
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    vm.numClusters();
                    //vm.periodic(0);
                    Log.d("MAIN", "Finish virtual");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }


    private void printReadings(Context context){
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    vm.printReadings();
                    Log.d("MAIN", "Finish virtual");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
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
