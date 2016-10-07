package ch.ethz.coss.nervousnetgen.virtual.virtual_sensor_generator;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.nervousnet.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.virtual.exceptions.NoData;
import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor.VirtualSensor;

/**
 * Created by ales on 02/10/16.
 */
public class CombineReadings {

    private static final long initWindowSizeMiliseconds = 1000;

    public static ArrayList<VirtualSensor> combine(
            ArrayList<ArrayList<SensorReading>> listOfSensorsReadings, ArrayList<String> paramNames) throws NoData {

        long startTimestamp = Long.MIN_VALUE;
        long stopTimestamp = Long.MIN_VALUE;

        //1. get the beginning and end point of the frame, used for cuting into intervals
        for( ArrayList<SensorReading> arr : listOfSensorsReadings ){

            // TODO Check if array is empyt, if so, we need to do something
            if (arr.isEmpty()){
                throw new NoData("Sensor array for one of the specified sensors is empty.");
            }

            long tmpStart = arr.get(0).getTimestampEpoch();
            if (tmpStart > startTimestamp)
                startTimestamp = tmpStart;
            long tmpStop = arr.get(arr.size() - 1).getTimestampEpoch();
            if (tmpStop > stopTimestamp)
                stopTimestamp = tmpStop;
        }

        // Let's take interval as 1s:
        long start = startTimestamp;
        long step = initWindowSizeMiliseconds;

        // Initialize pointes which will run through all arrays
        int[] pointers = new int[listOfSensorsReadings.size()];
        for( int i = 0; i < listOfSensorsReadings.size(); i++ ){
            pointers[i++] = -1;
        }


        ArrayList<VirtualSensor> vsparr = new ArrayList<>();

        while ( start <= stopTimestamp ) {

            for (int i = 0; i < pointers.length; i++) {
                ArrayList<SensorReading> readings = listOfSensorsReadings.get(i);
                int sizeI = readings.size();
                while (pointers[i]+1 < sizeI && readings.get(pointers[i]+1).getTimestampEpoch() <= start) {
                    pointers[i]++;
                }
            }

            VirtualSensor original = new VirtualSensor();

            // Set timestamp of the combined virtual point

            original.setTimestampEpoch(start);
            original.setParametersNames(paramNames);
            Object[] originalValues = original.getValues();
            // Fill the VirtualSensor
            int pos = 0;
            for (int i = 0; i < pointers.length; i++) {

                SensorReading reading = listOfSensorsReadings.get(i).get(pointers[i]);
                Object[] readingValues = reading.getValues();
                for( int p = 0; p < readingValues.length; p++){
                    originalValues[pos++] = readingValues[p];
                }
            }
            vsparr.add(original);
            start += step;
        }
        return vsparr;
    }

}
