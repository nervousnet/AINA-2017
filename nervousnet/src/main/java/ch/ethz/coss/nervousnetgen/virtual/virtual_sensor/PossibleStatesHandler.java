package ch.ethz.coss.nervousnetgen.virtual.virtual_sensor;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import ch.ethz.coss.nervousnetgen.virtual.clustering.Cluster;
import ch.ethz.coss.nervousnetgen.virtual.database.PossibleStatesDBManager;
import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor.PossibleState;

/**
 * Created by ales on 07/10/16.
 */
public class PossibleStatesHandler {

    Context context;
    int dimension;
    ArrayList<String> paramNames;
    ArrayList<String> typeNames;
    PossibleStatesDBManager psDB;
    HashMap<Integer, double[]> psMap;

    public PossibleStatesHandler(Context context, String uniqueName, ArrayList<String> paramNames,
                                 ArrayList<String> typeNames){
        this.context = context;
        this.dimension = paramNames.size();
        this.paramNames = paramNames;
        this.typeNames = typeNames;
        this.psDB = new PossibleStatesDBManager(context, uniqueName, paramNames, typeNames);
        this.psMap = new HashMap<>();
    }

    public void clearPossibleStates(){
        this.psMap.clear();
        this.psDB.deleteAll();
    }

    public void addPossibleState(int id, double[] psValues){
        long idReturn = this.psDB.store(id, psValues);
        this.psMap.put(new Integer(id), psValues);

    }
}
