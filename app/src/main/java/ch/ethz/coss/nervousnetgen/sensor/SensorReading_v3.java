package ch.ethz.coss.nervousnetgen.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ch.ethz.coss.nervousnetgen.configurations.ConfigurationClass;

/**
 * Created by ales on 20/09/16.
 */
public class SensorReading_v3 {

    protected String sensorName;
    protected ArrayList<String> parametersNames = new ArrayList<>();
    protected String[] metadata;
    protected int samplingPeriod;
    private long timestampEpoch;
    private ArrayList<Object> values = new ArrayList<>();

    public SensorReading_v3(){}

    public SensorReading_v3(String sensorName, String[] parametersNames, String[] metadata,
                            int samplingPeriod, long timestampEpoch, Object[] values) {
        this.sensorName = sensorName;
        this.parametersNames = (ArrayList<String>) Arrays.asList(parametersNames);
        this.metadata = metadata;
        this.samplingPeriod = samplingPeriod;
        this.timestampEpoch = timestampEpoch;
        this.values = (ArrayList<Object>) Arrays.asList(values);
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public ArrayList<String> getParametersNames() {
        return parametersNames;
    }

    public void setParametersNames(ArrayList<String> parametersNames) {
        this.parametersNames = parametersNames;
    }

    public String[] getMetadata() {
        return metadata;
    }

    public void setMetadata(String[] metadata) {
        this.metadata = metadata;
    }

    public int getSamplingPeriod() {
        return samplingPeriod;
    }

    public void setSamplingPeriod(int samplingPeriod) {
        this.samplingPeriod = samplingPeriod;
    }

    public long getTimestampEpoch() {
        return timestampEpoch;
    }

    public void setTimestampEpoch(long timestampEpoch) {
        this.timestampEpoch = timestampEpoch;
    }

    public ArrayList<Object> getValues() {
        return values;
    }

    public void setValues(ArrayList<Object> values) {
        this.values = values;
    }

    public void setValue(String paramName, Object value){
        if (!this.parametersNames.contains(paramName)) {
            this.parametersNames.add(paramName);
            this.values.add(value);
        } else {
            int index = this.parametersNames.indexOf(paramName);
            this.values.set(index, value);
        }
    }

    public void loadFromConfig(ConfigurationClass config){
        this.sensorName = config.getSensorName();
        ArrayList<String> names = new ArrayList<>();
        names.addAll(Arrays.asList(config.getParametersNames()));
        this.parametersNames = names;
        this.metadata = config.getMetadata();
        this.samplingPeriod = config.getSamplingPeriod();
        for (int i = 0; i < parametersNames.size(); i ++){
            values.add(null);
        }
    }

    @Override
    public String toString() {
        return "SensorReading_v3{" +
                "sensorName='" + sensorName + '\'' +
                ", parametersNames size=" + parametersNames.size() +
                ", metadata=" + Arrays.toString(metadata) +
                ", samplingPeriod=" + samplingPeriod +
                ", timestampEpoch=" + timestampEpoch +
                ", values size=" + values.size() +
                '}';
    }
}
