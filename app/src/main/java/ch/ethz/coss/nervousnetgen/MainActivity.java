package ch.ethz.coss.nervousnetgen;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import ch.ethz.coss.nervousnetgen.configurations.ConfigurationClass;
import ch.ethz.coss.nervousnetgen.configurations.ConfigurationLoader;
import ch.ethz.coss.nervousnetgen.sensor_wrappers.Wrapper_v2;
import ch.ethz.coss.nervousnetgen.sensor_wrappers.iWrapper;
import ch.ethz.coss.nervousnetgen.storage.Query;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ConfigurationClass> confClassList;
    //private DatabaseHelper databaseHelper;
    private ArrayList<iWrapper> wrappers = new ArrayList<>();
    private ArrayList<String> sensorNames = new ArrayList<>();
    Button startButton;
    Button stopButton;
    Button allButton;
    Button commonButton;

    Query query;


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
        allButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "All button clicked");
                /*for ( ConfigurationClass cc : confClassList) {
                    databaseHelper.printTable(cc.getSensorName());
                }*/

                for ( String name : sensorNames ) {
                    Object[] result = query.getQuery("SELECT * FROM " + name + ";");
                    Log.d("MAIN", Arrays.toString((String[])result[0]));
                    Object[] values = ((ArrayList<Object[]>)result[1]).get(0);
                    for (int i = 0; i < values.length; i++)
                        Log.d("MAIN", "" + values[i]);
                }
            }
        });


        Log.d("MAIN", "Buttons initialized");

        ConfigurationLoader confLoader = new ConfigurationLoader(this);
        confClassList = confLoader.load();
        //databaseHelper = new DatabaseHelper(this);
        this.query = new Query(this);

        //databaseHelper.deleteTable(sensorName);

        for ( ConfigurationClass cc : confClassList) {

            // TODO: select right Wrapper
            String chooseWrapper = "Wrapper_v2";
            iWrapper wrapper;
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
                    wrappers.add(wrapper);
                    sensorNames.add(cc.getSensorName());
                    break;
                default:
                    // do nothing, ignore
                    Log.d("MAIN", "ERROR - wrapper not supported in main activity class");
            }
            Log.d("MAIN", cc.getSensorName() + " DONE");
        }

        // TESTING
        //Test.queryCommonTable(this);
    }


    public ArrayList<ConfigurationClass> getConfClassList() {
        return confClassList;
    }


    public ArrayList<iWrapper> getWrapers() {
        return wrappers;
    }
}
