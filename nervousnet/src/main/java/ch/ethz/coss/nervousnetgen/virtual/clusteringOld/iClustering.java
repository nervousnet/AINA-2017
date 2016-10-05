package ch.ethz.coss.nervousnetgen.virtual.clusteringOld;

import java.util.ArrayList;

/**
 * Created by ales on 28/06/16.
 *
 * Interface specifies the exact structure of any clustering algorithm.
 */
public interface iClustering {
    public ArrayList<? extends Cluster> compute(ArrayList<? extends iPoint> points);
    public Cluster classify(iPoint point);
    //public ArrayList<Cluster> getClusters();
}
