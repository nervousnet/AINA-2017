package ch.ethz.coss.nervousnetgen.nervousnet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by ales on 26/09/16.
 */
public class Store extends SQLiteOpenHelper implements iStore {

    private static final String LOG_TAG = Store.class.getSimpleName();

    private String tableName;
    private String[] columnNames;

    public Store(Context context, String tableName, String[] columnNames, String[] columnTypes) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.columnNames = columnNames;
        this.tableName = tableName;

        SQLiteDatabase db = this.getWritableDatabase();
        createTableIfNotExists(db, tableName, columnNames, columnTypes);
    }



    private static void createTableIfNotExists(SQLiteDatabase database, String tableName,
                                                  String[] columnNames, String[] columnTypes){
        Log.d(LOG_TAG, "Create table " + tableName);
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " +
                Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT";

        for (int i = 0; i < columnNames.length; i++){
            sql += ", " + columnNames[i] + " " + columnTypes[i];
        }
        sql += " );";
        database.execSQL(sql);
    }

    public void store(HashMap<String, Object> hash){

        ContentValues insertList = new ContentValues();
        for (String name : this.columnNames){
            if (hash.containsKey(name)) {
                Object val = hash.get(name);

                if (val.getClass().equals(String.class))
                    insertList.put(name, (String)hash.get(name));


                else if (val instanceof Integer)
                    insertList.put(name, (Integer)hash.get(name));


                else if (val instanceof Double)
                    insertList.put(name, (Double)hash.get(name));


                else if (val instanceof Float)
                    insertList.put(name, (Float)hash.get(name));


                else if (val instanceof Long)
                    insertList.put(name, (Long)hash.get(name));

            }
            else {
                insertList.putNull(name);
            }
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(this.tableName, null, insertList);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
