package ch.ethz.coss.nervousnetgen.nervousnet.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ales on 20/09/16.
 */
public class SensorReading {

    protected int id;
    protected String sensorName;
    private long timestampEpoch;
    protected List<String> parametersNames;
    private Object[] values;

    protected String[] metadata;

    public SensorReading(){};

    public SensorReading(iSensorReadingConfiguration config){
        this.sensorName = config.getSensorName();
        this.parametersNames = Arrays.asList( config.getParametersNames() );
        this.metadata = config.getMetadata();
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

    public void setValue(String paramName, Object value){
        int index = this.parametersNames.indexOf(paramName);
        this.values[index] = value;
    }

    public void setTimestampEpoch(long timestamp){
        this.timestampEpoch = timestamp;
    }

    public void setParametersNames(ArrayList<String> paramNames){
        this.parametersNames = paramNames;
        if (values == null || values.length != paramNames.size())
            this.values = new Object[this.parametersNames.size()];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "sensorName='" + sensorName + '\'' +
                ", parametersNames size=" + parametersNames.size() +
                ", metadata=" + Arrays.toString(metadata) +
                ", timestampEpoch=" + timestampEpoch +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
