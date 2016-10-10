package ch.ethz.coss.nervousnetgen.virtual.clustering;

import java.util.ArrayList;

/**
 * Created by ales on 28/06/16.
 *
 * Interface specifies the exact structure of any clustering algorithm.
 */
public interface iClustering {
    public ArrayList<Cluster> compute(ArrayList<? extends iPoint> points) throws ClusteringException;
    public int classify(iPoint point, ArrayList<Cluster> clusters);
    public int classify(double[] point, ArrayList<Cluster> clusters);
}
