package ch.ethz.coss.nervousnetgen.sensor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ales on 20/09/16.
 */
public class SensorReading {

    protected String sensorName;
    private long timestampEpoch;

    protected List<String> parametersNames;
    private Object[] values;

    protected String[] metadata;
    protected int samplingPeriod;

    public SensorReading(iSensorReadingConfiguration config){
        this.sensorName = config.getSensorName();
        this.parametersNames = Arrays.asList( config.getParametersNames() );
        this.metadata = config.getMetadata();
        this.samplingPeriod = config.getSamplingPeriod();
        this.values = new Object[this.parametersNames.size()];
    }

    public String getSensorName() {
        return sensorName;
    }

    public long getTimestampEpoch() {
        return timestampEpoch;
    }

    public List<String> getParametersNames() {
        return parametersNames;
    }

    public Object[] getValues() {
        return values;
    }

    public String[] getMetadata() {
        return metadata;
    }

    public int getSamplingPeriod() {
        return samplingPeriod;
    }

    public void setValue(String paramName, Object value){
        int index = this.parametersNames.indexOf(paramName);
        this.values[index] = value;
    }

    public void setTimestampEpoch(long timestamp){
        this.timestampEpoch = timestamp;
    }

    @Override
    public String toString() {
        return "SensorReading_v3{" +
                "sensorName='" + sensorName + '\'' +
                ", parametersNames size=" + parametersNames.size() +
                ", metadata=" + Arrays.toString(metadata) +
                ", samplingPeriod=" + samplingPeriod +
                ", timestampEpoch=" + timestampEpoch +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
