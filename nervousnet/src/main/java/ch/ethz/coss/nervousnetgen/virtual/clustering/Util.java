package ch.ethz.coss.nervousnetgen.virtual.clustering;

/**
 * Created by ales on 30/08/16.
 */
public class Util {

    public static double distance(double[] arr1, double[] arr2, int dimension){
        double sum = 0;
        for (int i = 0; i < dimension; i++){
            double diff = arr1[i] - arr2[i];
            sum += diff * diff;
        }
        sum = Math.sqrt(sum);
        // Return
        return sum;
    }
}
