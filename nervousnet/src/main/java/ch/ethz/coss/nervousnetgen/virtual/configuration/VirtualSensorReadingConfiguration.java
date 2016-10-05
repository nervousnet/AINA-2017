package ch.ethz.coss.nervousnetgen.virtual.configuration;

import ch.ethz.coss.nervousnetgen.nervousnet.sensor.iSensorReadingConfiguration;

/**
 * Created by ales on 03/10/16.
 */
public class VirtualSensorReadingConfiguration implements iSensorReadingConfiguration {



    @Override
    public String getSensorName() {
        return null;
    }

    @Override
    public String[] getParametersNames() {
        return new String[0];
    }

    @Override
    public String[] getMetadata() {
        return new String[0];
    }

    @Override
    public int getSamplingPeriod() {
        return 0;
    }
}
