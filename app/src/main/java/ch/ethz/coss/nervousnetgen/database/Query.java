package ch.ethz.coss.nervousnetgen.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.sensor.SensorReading_v2;

/**
 * Created by ales on 26/09/16.
 */
public class Query extends SQLiteOpenHelper {

    private static final String LOG_TAG = Query.class.getSimpleName();

    public Query(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }
    /*
Returns [0] a string array of column names and [1] an array of arrays of values
 */
    public Object[] getQuery(String query) {

        // 1. build the query
        // done

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int nColumns = cursor.getColumnCount();
        String[] columnNames = cursor.getColumnNames();
        ArrayList<Object[]> returnList = new ArrayList<>();

        // 3. go over each row, build sensor value and add it to list
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
                //Log.d(LOG_TAG, Arrays.toString( values ));
            } while (cursor.moveToNext());
        }
        db.close();
        return new Object[]{ columnNames, returnList };
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
