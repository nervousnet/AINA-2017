package ch.ethz.coss.nervousnetgen.sensor;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.sensor.SensorReading;

/**
 * Created by ales on 01/10/16.
 */
public interface iDataManager {

    public SensorReading getLatestReading(String sensorID);
    public ArrayList<SensorReading> getReadings(String sensorID, long startTimestamp, long stopTimestamp);
    public ArrayList<SensorReading> getReadings(String sensorID, long startTimestamp, long stopTimestamp,
                                                ArrayList<String> selectColumns);
    public void storeReading(SensorReading reading);
    public void storeReadings(ArrayList<SensorReading> list);

}
