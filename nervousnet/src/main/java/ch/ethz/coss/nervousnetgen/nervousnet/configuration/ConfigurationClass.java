package ch.ethz.coss.nervousnetgen.nervousnet.configuration;

import java.util.Arrays;

import ch.ethz.coss.nervousnetgen.sensor.iSensorReadingConfiguration;

/**
 * Created by ales on 21/09/16.
 */
public class ConfigurationClass implements iSensorReadingConfiguration {
    private final String sensorName;
    private final int androidSensorType;
    private final String[] parametersNames;
    private final String[] parametersTypes;
    private final String[] metadata;
    private final int[] androidParametersPositions;
    private final int samplingPeriod;

    public ConfigurationClass(String sensorName, int androidSensorType, String[] parametersNames,
                              String[] parametersTypes, String[] metadata,
                              int[] androidParametersPositions, int samplingPeriod) {
        this.sensorName = sensorName;
        this.androidSensorType = androidSensorType;
        this.parametersNames = parametersNames;
        this.parametersTypes = parametersTypes;
        this.metadata = metadata;
        this.androidParametersPositions = androidParametersPositions;
        this.samplingPeriod = samplingPeriod;
    }

    public String getSensorName() {
        return sensorName;
    }

    public int getAndroidSensorType() {
        return androidSensorType;
    }

    public String[] getParametersNames() {
        return parametersNames;
    }

    public String[] getParametersTypes() {
        return parametersTypes;
    }

    public String[] getMetadata() {
        return metadata;
    }

    public int[] getAndroidParametersPositions() {
        return androidParametersPositions;
    }

    public int getSamplingPeriod() {
        return samplingPeriod;
    }

    @Override
    public String toString() {
        return "ConfigurationClass{" +
                "sensorName='" + sensorName + '\'' +
                ", androidSensorType=" + androidSensorType +
                ", parametersNames=" + Arrays.toString(parametersNames) +
                ", parametersTypes=" + Arrays.toString(parametersTypes) +
                ", metadata=" + Arrays.toString(metadata) +
                ", androidParametersPositions=" + Arrays.toString(androidParametersPositions) +
                ", samplingPeriod=" + samplingPeriod +
                '}';
    }
}
