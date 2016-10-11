package ch.ethz.coss.nervousnetgen.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.HashMap;

import ch.ethz.coss.nervousnetgen.virtual.database.Constants;

/**
 * Created by ales on 07/10/16.
 */
public class StoreStrings extends SQLiteOpenHelper {

    private static final String LOG_TAG = StoreStrings.class.getSimpleName();

    public static String DATABASE_NAME = "TestMeasurementsDBForPublication";
    private String tableName;


    public StoreStrings(Context context, String tableName) {
        super(context, DATABASE_NAME, null, 1);
        this.tableName = tableName;
        createNewTable();
    }


    private void createNewTable(){
        Log.d(LOG_TAG, "Create table " + tableName);
        // Drop table first, then we create new one
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + tableName + ";");
        String sql = "CREATE TABLE " + tableName + " ( " +
                Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "samplingRate INTEGER, " +
                "dataSize INTEGER, " +
                "dimension INTEGER, " +
                "numClusters INTEGER, " +
                "timeMilis REAL, " +
                "stdDeviation REAL";
        sql += " );";
        database.execSQL(sql);
    }

    public void deleteAll(){
        Log.d(LOG_TAG, "Delete all from table " + tableName);
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + tableName + ";");
    }


    public long store(int samplingRate, int dataSize, int dim, int numClusters, double time, double sd){

        ContentValues insertList = new ContentValues();
        insertList.put("samplingRate", samplingRate);
        insertList.put("dataSize", dataSize);
        insertList.put("dimension", dim);
        insertList.put("numClusters", numClusters);
        insertList.put("timeMilis", time);
        insertList.put("stdDeviation", sd);

        SQLiteDatabase db = this.getWritableDatabase();
        long idReturn = db.insert(this.tableName, null, insertList);
        db.close();
        Log.d(LOG_TAG, "Store new item in " + tableName);
        return idReturn;
    }

    public HashMap<Long, String> getAll() {

        // 1. create the query
        String query = "SELECT * FROM " + tableName;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        HashMap<Long, String> returnList = new HashMap<>();

        // 3. go over each row, build sensor value and add it to list
        if (cursor.moveToFirst()) {
            do {
                int id_index = cursor.getColumnIndex(Constants.ID);
                long id = cursor.getLong(id_index);

                int str_index = cursor.getColumnIndex("String");
                String text = cursor.getString(str_index);

                returnList.put(new Long(id), text);
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
