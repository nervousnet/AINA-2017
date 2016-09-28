package ch.ethz.coss.nervousnetgen.sensor;

import java.util.Arrays;

/**
 * Created by ales on 20/09/16.
 */
public class SensorReading {

    private String sensorName;
    private String timestampEpoch;
    private String[] parametersNames;
    private String[] typeNames;
    private String[] values;
    private String[] metadata;

    public SensorReading(String sensorName, String timestampEpoch, String[] parametersNames, String[] typeNames, String[] values, String[] metadata){
        this.sensorName = sensorName;
        this.timestampEpoch = timestampEpoch;
        this.parametersNames = parametersNames;
        this.typeNames = typeNames;
        this.values = values;
        this.metadata = metadata;
    }

    public String[] getParametersNames() {
        return parametersNames;
    }

    public void setParametersNames(String[] parametersNames) {
        this.parametersNames = parametersNames;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String[] getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(String[] typeNames) {
        this.typeNames = typeNames;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getMetadata() {
        return metadata;
    }

    public void setMetadata(String[] metadata) {
        this.metadata = metadata;
    }

    public String getTimestampEpoch() {
        return timestampEpoch;
    }

    public void setTimestampEpoch(String timestampEpoch) {
        this.timestampEpoch = timestampEpoch;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "sensorName='" + sensorName + '\'' +
                ", timestampEpoch='" + timestampEpoch + '\'' +
                ", parametersNames=" + Arrays.toString(parametersNames) +
                ", typeNames=" + Arrays.toString(typeNames) +
                ", values=" + Arrays.toString(values) +
                ", metadata=" + Arrays.toString(metadata) +
                '}';
    }
}
