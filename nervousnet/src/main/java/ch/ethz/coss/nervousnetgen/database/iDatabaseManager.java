package ch.ethz.coss.nervousnetgen.database;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.sensor.SensorReading;

/**
 * Created by ales on 01/10/16.
 */
public interface iDatabaseManager {

    public ArrayList<SensorReading> getReadings(String sensorID, ArrayList<String> sensorParamNames);
    public ArrayList<SensorReading> getReadings(String sensorID, ArrayList<String> sensorParamNames,
                                                long startTimestamp, long stopTimestamp);
    public ArrayList<SensorReading> getReadings(String sensorID, ArrayList<String> sensorParamNames,
                                                long startTimestamp, long stopTimestamp,
                                                ArrayList<String> selectColumns);
    //public void storeReading(SensorReading reading);
    //public void storeReadings(ArrayList<SensorReading> list);

}
