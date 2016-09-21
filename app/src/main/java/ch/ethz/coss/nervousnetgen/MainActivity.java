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

import ch.ethz.coss.nervousnetgen.configurations.ConfigurationClass;
import ch.ethz.coss.nervousnetgen.configurations.ConfigurationLoader;
import ch.ethz.coss.nervousnetgen.sensor_wrappers.Wrapper1;
import ch.ethz.coss.nervousnetgen.storage.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ConfigurationClass> confClassList;
    private DatabaseHelper databaseHelper;
    private ArrayList<Wrapper1> wrapers;
    Button stopButton;
    Button allButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// Initialize button for get All sensor data
        allButton = (Button) findViewById(R.id.getAllButton);
        allButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "Button clicked");
                for ( ConfigurationClass cc : confClassList) {
                    databaseHelper.getAll(cc.getSensorName());
                }
            }
        });

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.d("MAIN", "Button clicked");
                for ( Wrapper1 w : wrapers ){
                    w.stop();
                }
            }
        });

        Log.d("MAIN", "Buttons initialized");

        ConfigurationLoader confLoader = new ConfigurationLoader(this);
        confClassList = confLoader.load();
        databaseHelper = new DatabaseHelper(this);
        wrapers = new ArrayList<>();


        //databaseHelper.deleteTable(sensorName);

        for ( ConfigurationClass cc : confClassList) {
            Wrapper1 readSensor1 = new Wrapper1(this, databaseHelper, cc.getSensorName(),
                    cc.getParametersNames(), cc.getParametersTypes(), cc.getMetadata(),
                    cc.getAndroidSensorType(), cc.getSamplingPeriod(),
                    cc.getAndroidParametersPositions());
            readSensor1.start();
            wrapers.add(readSensor1);
            Log.d("MAIN", cc.getSensorName() + " DONE");
        }

        Log.d("MAIN", "Sensors all up");


    }
}
