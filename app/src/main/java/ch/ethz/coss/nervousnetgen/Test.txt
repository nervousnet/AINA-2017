package ch.ethz.coss.nervousnetgen;

import android.util.Log;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.storage.DatabaseHelper;

/**
 * Created by ales on 25/09/16.
 */
public class Test {


    public static void queryCommonTable(final MainActivity activity){

        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DatabaseHelper databaseHelper = activity.getDatabaseHelper();

                String query = "SELECT * FROM CommonTable WHERE " + databaseHelper.getSensorName() + " = 'Light';";

                long startTime = System.currentTimeMillis();
                ArrayList<SensorReading> list = databaseHelper.getQuery(query);
                long stopTime = System.currentTimeMillis();
                long difference = stopTime - startTime;

                String query2 = "SELECT * FROM Light WHERE " + databaseHelper.getSensorName() + " = 'Light';";
                long startTime2 = System.currentTimeMillis();
                ArrayList<SensorReading> list2 = databaseHelper.getQuery(query2);
                long stopTime2 = System.currentTimeMillis();
                long difference2 = stopTime2 - startTime2;

                Log.d("TEST", "Time consumed for query light in common table is " + difference);

                Log.d("TEST", "Time consumed for query light in common table is " + difference2);


            }
        };
        thread.start();
    }
}
