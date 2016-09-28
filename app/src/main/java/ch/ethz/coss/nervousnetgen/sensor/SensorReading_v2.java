package ch.ethz.coss.nervousnetgen.sensor;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ales on 20/09/16.
 */
public class SensorReading_v2 {

    private String sensorName;
    private String timestampEpoch;
    private String[] parametersNames;
    private Object[] values;
    private String metadata;  // TODO, needs to be upgraded
    private long samplingPeriod;

    public SensorReading_v2(String sensorName, String timestampEpoch, String[] parametersNames, String metadata, long samplingPeriod){
        this.sensorName = sensorName;
        this.timestampEpoch = timestampEpoch;
        this.parametersNames = parametersNames;
        this.values = new Object[parametersNames.length];
        this.metadata = metadata;
        this.samplingPeriod = samplingPeriod;
    }


    public SensorReading_v2(String sensorName, String timestampEpoch, String[] parametersNames, Object[] values, String metadata, long samplingPeriod){
        this.sensorName = sensorName;
        this.timestampEpoch = timestampEpoch;
        this.parametersNames = parametersNames;
        this.values = values;
        this.metadata = metadata;
        this.samplingPeriod = samplingPeriod;
    }

    public String getSensorName() {
        return sensorName;
    }

    public String getTimestampEpoch() {
        return timestampEpoch;
    }

    public String[] getParametersNames() {
        return parametersNames;
    }

    public Object[] getValues() {
        return values;
    }

    public String getMetadata() {
        return metadata;
    }

    public long getSamplingPeriod() {
        return samplingPeriod;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "SensorReading_v2{" +
                "sensorName='" + sensorName + '\'' +
                ", timestampEpoch='" + timestampEpoch + '\'' +
                ", values=" + values +
                ", metadata='" + metadata + '\'' +
                '}';
    }
}
