package ch.ethz.coss.nervousnetgen.configurations;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by ales on 21/09/16.
 */
public class ConfigurationLoader implements iConfigurationLoader {

    private static final String LOG_TAG = ConfigurationLoader.class.getSimpleName();
    private Context context;

    public ConfigurationLoader(Context context) {
        this.context = context;
    }

    @Override
    public ArrayList<ConfigurationClass> load() {
        ArrayList<ConfigurationClass> list = new ArrayList<>();

        String line,line1 = "";
        try
        {
            //TODO
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("sensors_configuration.json")));

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
            JSONArray sensorConfList = (new JSONObject(strJson)).getJSONArray("sensors_configurations");

            for (int i = 0; i < sensorConfList.length(); i++) {
                JSONObject sensorConf = sensorConfList.getJSONObject(i);

                String sensorName = sensorConf.getString("sensorName");
                int androidSensorType = sensorConf.getInt("androidSensorType");
                String[] paramNames = convertToStringArr(sensorConf.getJSONArray("parametersNames"));
                String[] paramTypes = convertToStringArr(sensorConf.getJSONArray("parametersTypes"));
                String[] metadata = convertToStringArr(sensorConf.getJSONArray("metadata"));
                int[] positions = convertToIntArr(sensorConf.getJSONArray("androidParametersPositions"));
                int samplingPeriod = sensorConf.getInt("samplingPeriod");

                ConfigurationClass confClass = new ConfigurationClass(sensorName, androidSensorType,
                        paramNames, paramTypes, metadata, positions, samplingPeriod);
                list.add(confClass);
                Log.d(LOG_TAG, confClass.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    private static String[] convertToStringArr(JSONArray jList){
        int len = jList.length();
        String[] list = new String[len];
        for (int i = 0; i < len; i++){
            try {
                list[i] = jList.getString(i);
            } catch (JSONException e) {
                list[i] = "null";
                e.printStackTrace();
            }
        }
        return list;
    }

    private static int[] convertToIntArr(JSONArray jList){
        int len = jList.length();
        int[] list = new int[len];
        for (int i = 0; i < len; i++){
            try {
                list[i] = jList.getInt(i);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return list;
    }

}


