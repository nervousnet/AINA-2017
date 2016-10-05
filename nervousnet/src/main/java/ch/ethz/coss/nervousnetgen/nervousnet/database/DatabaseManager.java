package ch.ethz.coss.nervousnetgen.nervousnet.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.nervousnet.sensor.SensorReading;

/**
 * Created by ales on 03/10/16.
 */
public class DatabaseManager extends SQLiteOpenHelper implements iDatabaseManager {

    public DatabaseManager(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public ArrayList<SensorReading> getReadings(String sensorName, ArrayList<String> sensorParamNames){
        String query = "SELECT * FROM " + sensorName;
        return getReadings(sensorParamNames, query);
    }

    public ArrayList<SensorReading> getReadings(String sensorName, ArrayList<String> sensorParamNames,
                                               long start, long stop){
        String query = "SELECT * FROM " + sensorName + " WHERE " + Constants.TIMESTAMP + " >= " + start + " AND " + Constants.TIMESTAMP + " <= " + stop + ";";
        return getReadings(sensorParamNames, query);
    }

    public ArrayList<SensorReading> getReadings(String sensorName, ArrayList<String> sensorParamNames,
                                               long start, long stop, ArrayList<String> selectColumns){
        String cols = TextUtils.join(", ", selectColumns);
        String query = "SELECT " + Constants.ID + ", " + Constants.TIMESTAMP + ", " + cols + " " +
                "FROM " + sensorName + " WHERE " + Constants.TIMESTAMP + " >= " + start + " AND " +
                "" + Constants.TIMESTAMP + " <= " + stop + ";";
        return getReadings(sensorParamNames, query);
    }

    public ArrayList<SensorReading> getReadings(ArrayList<String> sensorParamNames,
                                           String query) {

        // 1. create the query

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int nColumns = cursor.getColumnCount();
        String[] columnNames = cursor.getColumnNames();

        ArrayList<SensorReading> returnList = new ArrayList<>();

        // 3. go over each row, build sensor value and add it to list
        if (cursor.moveToFirst()) {
            do {
                SensorReading reading = new SensorReading();

                // Update timestamp and values
                int indexTimestamp = cursor.getColumnIndex(Constants.TIMESTAMP);
                reading.setTimestampEpoch(cursor.getLong(indexTimestamp));
                reading.setParametersNames(sensorParamNames);

                for (String columnName : sensorParamNames){
                    int indexParam = cursor.getColumnIndex(columnName);
                    Object value = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        switch (cursor.getType(indexParam)) {
                            case Constants.FIELD_TYPE_INTEGER:
                                value = cursor.getInt(indexParam);
                                break;

                            case Constants.FIELD_TYPE_FLOAT:
                                value = cursor.getFloat(indexParam);
                                break;

                            case Constants.FIELD_TYPE_STRING:
                                value = cursor.getString(indexParam);
                                break;

                            default:
                                value = null;
                        }
                    }

                    reading.setValue(columnName, value);
                }

                returnList.add(reading);
                //Log.d(LOG_TAG, Arrays.toString( values ));
            } while (cursor.moveToNext());
        }
        db.close();
        return returnList;
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
