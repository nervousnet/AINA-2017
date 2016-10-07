package ch.ethz.coss.nervousnetgen.virtual.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ales on 05/10/16.
 */
public class VirtualSensorDBManager extends SQLiteOpenHelper {

    private static final String LOG_TAG = VirtualSensorDBManager.class.getSimpleName();

    private String tableName;
    private int dimension;
    private ArrayList<String> paramNames;
    private ArrayList<String> paramTypes;

    public VirtualSensorDBManager(Context context, String tableName, ArrayList<String> paramNames,
                                  ArrayList<String> paramTypes) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.tableName = tableName;
        this.paramNames = paramNames;
        this.paramTypes = paramTypes;
        this.dimension = paramNames.size();
        createTableIfNotExists();
    }


    private void createTableIfNotExists(){
        Log.d(LOG_TAG, "Create table " + tableName);
        // Create table
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " +
                // Define ID
                Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.TIMESTAMP + " INTEGER";
        // Add value columns
        for (int i = 0; i < this.dimension; i++){
            sql += ", " + paramNames.get(i) + " " + paramTypes.get(i);
        }
        // Add foreign key to the possible states table
        sql += ", " + Constants.POSSIBLE_STATES_ID + " INTEGER, " +
                "FOREIGN KEY(" +Constants.POSSIBLE_STATES_ID+ ") REFERENCES " +
                "" + Constants.POSSIBLE_STATES_TABLE + "("+ Constants.POSSIBLE_STATES_ID +") );";
        Log.d(LOG_TAG, "Create table " + sql);
        database.execSQL(sql);
    }

    public long store(long timestamp, int possibleStateRefId, double[] values){

        ContentValues insertList = new ContentValues();
        insertList.put(Constants.POSSIBLE_STATES_ID, possibleStateRefId);
        insertList.put(Constants.TIMESTAMP, timestamp);

        for (int i = 0; i < this.dimension; i++){
            String name = this.paramNames.get(i);
            Object val = values[i];

            if (val.getClass().equals(String.class))
                insertList.put(name, (String)val);


            else if (val instanceof Integer)
                insertList.put(name, (Integer)val);


            else if (val instanceof Double)
                insertList.put(name, (Double)val);


            else if (val instanceof Float)
                insertList.put(name, (Float)val);


            else if (val instanceof Long)
                insertList.put(name, (Long)val);

        }
        SQLiteDatabase db = this.getWritableDatabase();
        long idReturn = db.insert(this.tableName, null, insertList);
        db.close();
        Log.d(LOG_TAG, "Store new value id="+values);
        return idReturn;
    }

    public void removeOlder(long timestamp){
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "DELETE FROM " + tableName + " WHERE " + Constants.TIMESTAMP + " <= " + timestamp + ";";
        database.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
