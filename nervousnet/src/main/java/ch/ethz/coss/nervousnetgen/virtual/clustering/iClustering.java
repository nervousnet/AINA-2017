package ch.ethz.coss.nervousnetgen.virtual.clustering;

import java.util.ArrayList;

/**
 * Created by ales on 28/06/16.
 *
 * Interface specifies the exact structure of any clustering algorithm.
 */
public interface iClustering {
    public ArrayList<Cluster> compute(ArrayList<? extends iPoint> points);
    public Cluster classify(iPoint point);
    public Cluster classify(double[] point);
    public ArrayList<Cluster> getClusters();
}
