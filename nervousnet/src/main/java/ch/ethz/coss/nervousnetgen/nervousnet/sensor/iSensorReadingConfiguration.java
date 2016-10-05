package ch.ethz.coss.nervousnetgen.nervousnet.sensor;

/**
 * Created by ales on 03/10/16.
 */
public interface iSensorReadingConfiguration {

    public String getSensorName();
    public String[] getParametersNames();
    public String[] getMetadata();
    public int getSamplingPeriod();

}
