package ch.ethz.coss.nervousnetgen.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.ethz.coss.nervousnetgen.SensorReading;

/**
 * Created by ales on 21/09/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements iDatabaseHelper{

    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VirtualSensorDB";

    private static final String ID = "ID";
    private static final String sensorName = "SensorName";
    private static final String timestampEpoch = "TimestampEpoch";
    private static final String paramNames = "ParametersNames";
    private static final String paramTypes = "ParametersTypes";
    private static final String values = "Values_";
    private static final String metadata = "Metadata";
    private static final String[] columnNames = new String[]{ID, sensorName, timestampEpoch, paramNames, paramTypes, metadata, values};
    private static final String[] columnTypes = new String[]{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public boolean store(SensorReading reading){
        // Check if the table exists. If not, create one.
        // We need to get UNIQE NAME (maybe should be specified)
        // in config file as a name of the sensor

        String tableName = reading.getSensorName();
        SQLiteDatabase database = this.getWritableDatabase();

        createTableIfNotExists(database, tableName, this.columnNames, this.columnTypes);

        ContentValues insertList = new ContentValues();
        insertList.put(this.sensorName, tableName);
        insertList.put(this.timestampEpoch, reading.getTimestampEpoch());
        insertList.put(this.paramNames, joinStringsWithComma(reading.getParametersNames()));
        insertList.put(this.paramTypes, joinStringsWithComma(reading.getTypeNames()));
        insertList.put(this.values,     joinStringsWithComma(reading.getValues()));
        insertList.put(this.metadata,   joinStringsWithComma(reading.getMetadata()));

        database.insert(tableName, null, insertList);


        // ADD TO COMMON TABLE
        String commonTable = "CommonTable";
        createTableIfNotExists(database, commonTable, this.columnNames, this.columnTypes);
        database.insert(commonTable, null, insertList);

        database.close();

        return true;
    }



    public ArrayList<SensorReading> printTable(String tableName) {
        ArrayList<SensorReading> readings = new ArrayList();

        // 1. build the query
        String query = "SELECT  * FROM " + tableName;

        return getQuery(query);
    }


    public ArrayList<SensorReading> getQuery(String query) {
        ArrayList<SensorReading> readings = new ArrayList();

        // 1. build the query
        // done

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build sensor value and add it to list
        List<String> list = Arrays.asList(this.columnNames);
        if (cursor.moveToFirst()) {
            do {
                String sensorName = cursor.getString(list.indexOf(this.sensorName));
                String timestampEpoch = cursor.getString(list.indexOf(this.sensorName));
                String[] paramNames = cursor.getString(list.indexOf(this.paramNames)).split(",");
                String[] paramTypes = cursor.getString(list.indexOf(this.paramTypes)).split(",");
                String[] values = cursor.getString(list.indexOf(this.values)).split(",");
                String[] metadata = cursor.getString(list.indexOf(this.metadata)).split(",");

                SensorReading reading = new SensorReading(sensorName, timestampEpoch, paramNames, paramTypes, values, metadata);
                readings.add(reading);
                Log.d(LOG_TAG, reading.toString());
            } while (cursor.moveToNext());
        }
        db.close();
        return readings;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void deleteTable(String sensorName) {
        Log.d("DATABASE", "Delete table ");
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "DROP TABLE IF EXISTS "+sensorName;
        db.execSQL(sql);
    }








    // HELPER FUNCTIONS
    private static boolean createTableIfNotExists(SQLiteDatabase database, String tableName,
                                String[] columnNames, String[] columnTypes){
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ( ";

        for (int i = 0; i < columnNames.length; i++){
            if (i>0) sql += ", ";
            sql += columnNames[i] + " " + columnTypes[i];
        }
        sql += " );";
        //Log.d(LOG_TAG, "Execute "+ sql + " ... database="+database);
        database.execSQL(sql);
        return true;
    }

    private static String joinStringsWithComma(String[] list){
        StringBuilder listString = new StringBuilder();

        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            if (i > 0) listString.append(",");
            listString.append(s);
        }
        return listString.toString();
    }



    public static String getSensorName() {
        return sensorName;
    }
}