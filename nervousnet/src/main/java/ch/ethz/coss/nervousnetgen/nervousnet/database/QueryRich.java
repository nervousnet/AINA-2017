package ch.ethz.coss.nervousnetgen.nervousnet.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.sensor.iSensorReadingConfiguration;


/**
 * Created by ales on 27/09/16.
 */
public class QueryRich extends SQLiteOpenHelper {

    private static final String LOG_TAG = QueryRich.class.getSimpleName();

    public QueryRich(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public ArrayList<SensorReading> getAll(iSensorReadingConfiguration config) {

        // 1. create the query
        String query = "SELECT * FROM " + config.getSensorName();

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int nColumns = cursor.getColumnCount();
        String[] columnNames = cursor.getColumnNames();

        ArrayList<SensorReading> returnList = new ArrayList<>();

        // 3. go over each row, build sensor value and add it to list
        if (cursor.moveToFirst()) {
            do {
                SensorReading reading = new SensorReading(config);

                // Update timestamp and values
                int indexTimestamp = cursor.getColumnIndex(Constants.TIMESTAMP);
                reading.setTimestampEpoch(cursor.getLong(indexTimestamp));

                for (String columnName : config.getParametersNames()){
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
