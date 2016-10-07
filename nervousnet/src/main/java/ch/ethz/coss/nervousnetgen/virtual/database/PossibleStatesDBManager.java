package ch.ethz.coss.nervousnetgen.virtual.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ch.ethz.coss.nervousnetgen.virtual.clustering.Point;

/**
 * Created by ales on 07/10/16.
 */
public class PossibleStatesDBManager extends SQLiteOpenHelper {

    private static final String LOG_TAG = PossibleStatesDBManager.class.getSimpleName();

    private String tableName;
    private ArrayList<String> columnNames;
    private ArrayList<String> columnTypes;
    private int dimension;

    public PossibleStatesDBManager(Context context, String tableName, ArrayList<String> columnNames, ArrayList<String> columnTypes) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);

        // TODO: try to provide unique naming of possible states tabel, that it doesn't override
        // virtual sensor
        this.tableName = Constants.POSSIBLE_STATES_TABLE + tableName;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.dimension = columnNames.size();
        createNewTable();
    }


    private void createNewTable(){
        Log.d(LOG_TAG, "Create table " + tableName);
        // Drop table first, then we create new one
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + tableName + ";");
        String sql = "CREATE TABLE " + tableName + " ( " +
                Constants.POSSIBLE_STATES_ID + " INTEGER PRIMARY KEY";

        int len = columnNames.size();
        for (int i = 0; i < len; i++){
            sql += ", " + columnNames.get(i) + " " + columnTypes.get(i);
        }
        sql += " );";
        database.execSQL(sql);
    }

    public void deleteAll(){
        Log.d(LOG_TAG, "Delete all from table " + tableName);
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + tableName + ";");
    }


    public long store(int id, double[] values){

        ContentValues insertList = new ContentValues();
        insertList.put(Constants.ID, id);
        for (int i = 0; i < this.dimension; i++){
            String name = this.columnNames.get(i);
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
        Log.d(LOG_TAG, "Store new value id="+id);
        return idReturn;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
