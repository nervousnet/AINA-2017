package ch.ethz.coss.nervousnetgen.virtual.virtual_sensor;

import ch.ethz.coss.nervousnetgen.nervousnet.sensor.SensorReading;
import ch.ethz.coss.nervousnetgen.virtual.clustering.Cluster;
import ch.ethz.coss.nervousnetgen.virtual.clustering.iPoint;

/**
 * Created by ales on 05/10/16.
 */
public class VirtualSensor extends SensorReading implements iPoint {

    double[] coordinates;
    int cluster = -1;

    @Override
    public int getDimensions() {
        return this.getCoordinates().length;
    }

    @Override
    public double[] getCoordinates() {
        return coordinates;
    }

    @Override
    public int getCluster() {
        return cluster;
    }

    @Override
    public void setCluster(int c) {
        this.cluster = c;
    }

    public void setCoordinates(){
        Object[] values = this.getValues();
        int len = values.length;
        this.coordinates = new double[len];
        for (int i = 0; i < len; i++)
            this.coordinates[i] = (Float)values[i];
    }
}
