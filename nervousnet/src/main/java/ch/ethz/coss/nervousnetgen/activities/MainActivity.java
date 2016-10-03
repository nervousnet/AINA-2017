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
import ch.ethz.coss.nervousnetgen.database.DatabaseManager;
import ch.ethz.coss.nervousnetgen.nervousnet.configuration.ConfigurationClass;
import ch.ethz.coss.nervousnetgen.nervousnet.configuration.ConfigurationLoader;
import ch.ethz.coss.nervousnetgen.nervousnet.database.QueryRich;
import ch.ethz.coss.nervousnetgen.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.nervousnet.sensor_wrappers.Wrapper_v2;
import ch.ethz.coss.nervousnetgen.nervousnet.sensor_wrappers.Wrapper_v3;
import ch.ethz.coss.nervousnetgen.nervousnet.sensor_wrappers.iWrapper;
import ch.ethz.coss.nervousnetgen.virtual.VirtualMain;

public class MainActivity extends Activity {

    //private ArrayList<ConfigurationClass> confClassList;
    //private DatabaseHelper databaseHelper;
    private ArrayList<iWrapper> wrappers = new ArrayList<>();
    private HashMap<String, ConfigurationClass> sensors = new HashMap<>();
    Button startButton;
    Button stopButton;
    Button allButton;
    Button commonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// Initialize button for get All sensor data

        startButton = (Button) findViewById(R.id.buttonStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "Start button clicked");
                for ( iWrapper wrapper : wrappers) {
                    wrapper.start();
                }
            }
        });

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "Stop button clicked");
                for ( iWrapper wrapper : wrappers ){
                    wrapper.stop();
                }
            }
        });

        commonButton = (Button) findViewById(R.id.buttonCommonTable);
        commonButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "Common button clicked");
                //databaseHelper.printTable("CommonTable");
            }
        });

        allButton = (Button) findViewById(R.id.getAllButton);
        initGetAll(this);


        Log.d("MAIN", "Buttons initialized");

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
                default:
                    // do nothing, ignore
                    Log.d("MAIN", "ERROR - wrapper not supported in main activity class");
            }
            wrappers.add(wrapper);
            sensors.put(cc.getSensorName(), cc);
            Log.d("MAIN", cc.getSensorName() + " DONE");
        }

        // TESTING
        //Test.queryCommonTable(this);





        /////////////////////////////////////
        // VIRTUAL
        /////////////////////////////////////

        VirtualMain vm = new VirtualMain(this.getApplicationContext(), new DatabaseManager(this.getApplicationContext()));
        //TODO for now, run only first virtual sensor
        vm.periodic(0);
    }


    private void initGetAll(final Context context){
        allButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "All button clicked");
                /*for ( ConfigurationClass cc : confClassList) {
                    databaseHelper.printTable(cc.getSensorName());
                }*/
                QueryRich query = new QueryRich(context);
                for ( String sensorName : sensors.keySet() ) {
                    ArrayList<SensorReading> result = query.getAll(sensors.get(sensorName));
                    Log.d("GET ALL", result.toString());
                }
            }
        });
    }

}
