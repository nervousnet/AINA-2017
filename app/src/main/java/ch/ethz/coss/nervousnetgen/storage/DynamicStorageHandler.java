package ch.ethz.coss.nervousnetgen.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ch.ethz.coss.nervousnetgen.SensorReading;

/**
 * Created by ales on 26/09/16.
 */
public class DynamicStorageHandler extends SQLiteOpenHelper implements iDatabaseHelper {

    private static final String LOG_TAG = DynamicStorageHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VirtualSensorDB";

    private String tableName;
    private static final String ID = "ID";
    private String[] columnNames;

    public DynamicStorageHandler(Context context, String tableName, String[] columnNames, String[] columnTypes) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.columnNames = columnNames;
        this.tableName = tableName;

        SQLiteDatabase db = this.getWritableDatabase();
        createTableIfNotExists(db, tableName, columnNames, columnTypes);
    }



    private static void createTableIfNotExists(SQLiteDatabase database, String tableName,
                                                  String[] columnNames, String[] columnTypes){
        Log.d(LOG_TAG, "Create table " + tableName);
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT";

        for (int i = 0; i < columnNames.length; i++){
            sql += ", " + columnNames[i] + " " + columnTypes[i];
        }
        sql += " );";
        database.execSQL(sql);
    }

    @Override
    public boolean store(SensorReading reading) {
        // TODO remove
        return false;
    }

    public void store(HashMap<String, Object> hash){

        ContentValues insertList = new ContentValues();
        for (String name : this.columnNames){
            if (hash.containsKey(name)) {
                Object val = hash.get(name);

                if (val.getClass().equals(String.class))
                    insertList.put(name, (String)hash.get(name));


                else if (val.getClass().equals(Integer.class))
                    insertList.put(name, (Integer)hash.get(name));


                else if (val.getClass().equals(Double.class))
                    insertList.put(name, (Double)hash.get(name));


                else if (val.getClass().equals(Float.class))
                    insertList.put(name, (Float)hash.get(name));

            }
            else {
                insertList.putNull(name);
            }
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(this.tableName, null, insertList);
    }

    @Override
    public ArrayList<Object[]> getAll(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + this.tableName;
        return getQuery(query);
    }

    public ArrayList<Object[]> getQuery(String query) {

        // 1. build the query
        // done

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int nColumns = cursor.getColumnCount();
        String[] columnNames = cursor.getColumnNames();
        ArrayList<Object[]> returnList = new ArrayList<>();

        // 3. go over each row, build sensor value and add it to list
        List<String> list = Arrays.asList(this.columnNames);
        if (cursor.moveToFirst()) {
            do {
                Object[] values = new Object[nColumns];
                for ( int i = 0; i < nColumns; i++ ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        switch (cursor.getType(i)) {
                            case Constants.FIELD_TYPE_INTEGER:
                                values[i] = cursor.getInt(i);
                                break;

                            case Constants.FIELD_TYPE_FLOAT:
                                values[i] = cursor.getFloat(i);
                                break;

                            case Constants.FIELD_TYPE_STRING:
                                values[i] = cursor.getString(i);
                                break;

                            default:
                                values[i] = null;
                        }
                    }
                }

                returnList.add(values);
                Log.d(LOG_TAG, Arrays.toString( values ));
            } while (cursor.moveToNext());
        }
        db.close();
        return returnList;
    }

    /*public HashMap<String, Object> getAll(){

        // 1. query
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + this.tableName;
        Cursor cursor = db.rawQuery(query, null);

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

    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
