package ch.ethz.coss.nervousnetgen.virtual.configuration;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ales on 21/09/16.
 */
public class VirtualConfigurationLoader {

    private static String CONF_FILE_NAME = "virtual_sensors_configuration.json";
    private static final String LOG_TAG = VirtualConfigurationLoader.class.getSimpleName();

    public static ArrayList<VirtualSensorConfiguration> load(Context context) {

        String line,line1 = "";
        try
        {
            //TODO
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(CONF_FILE_NAME)));

            try
            {
                while ((line = reader.readLine()) != null)
                    line1+=line;
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        catch (Exception e)
        {
            Log.d(LOG_TAG, "ERROR " + e.getMessage());
        }

        String strJson = line1;

        try {


            ArrayList<VirtualSensorConfiguration> virtualSensors = new ArrayList<>();
            JSONArray sensorConfList = (new JSONObject(strJson)).getJSONArray("virtual_sensors");

            for (int i = 0; i < sensorConfList.length(); i++) {

                ArrayList<SensorConfiguration> sensorList = new ArrayList<>();


                JSONObject virtSensorConf = sensorConfList.getJSONObject(i);
                JSONObject sensors = virtSensorConf.getJSONObject("sensors");
                Iterator<String> keys = sensors.keys();

                // Sensors of one virtual sensor
                while (keys.hasNext()){
                    String sensorName = keys.next();

                    ArrayList<String> paramNames = new ArrayList<>();

                    JSONArray params = sensors.getJSONArray(sensorName);
                    int len = params.length();
                    for (int p = 0; p < len; p++){
                        paramNames.add(params.getString(p));
                    }
                    SensorConfiguration sensor = new SensorConfiguration(sensorName, paramNames);
                    sensorList.add(sensor);
                }

                long periodicWindowSize = virtSensorConf.getLong("periodicWindowSize");

                VirtualSensorConfiguration vs;
                if (virtSensorConf.has("virtualSensorName")) {
                    String virtualSensorName = virtSensorConf.getString("virtualSensorName");
                    vs = new VirtualSensorConfiguration(sensorList,
                            periodicWindowSize, virtualSensorName);
                }
                else {
                    vs = new VirtualSensorConfiguration(sensorList,
                            periodicWindowSize);
                }


                virtualSensors.add(vs);
            }
            return virtualSensors;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}


