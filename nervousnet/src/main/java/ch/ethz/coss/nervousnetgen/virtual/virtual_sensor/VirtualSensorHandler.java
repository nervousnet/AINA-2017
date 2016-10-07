package ch.ethz.coss.nervousnetgen.virtual.virtual_sensor;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import ch.ethz.coss.nervousnetgen.virtual.database.VirtualSensorDBManager;

/**
 * Created by ales on 07/10/16.
 */
public class VirtualSensorHandler {

    Context context;
    int dimension;
    ArrayList<String> paramNames;
    ArrayList<String> typeNames;
    VirtualSensorDBManager vsDB;

    public VirtualSensorHandler(Context context, String uniqueName, ArrayList<String> paramNames,
                                 ArrayList<String> typeNames){
        this.context = context;
        this.dimension = paramNames.size();
        this.paramNames = paramNames;
        this.typeNames = typeNames;
        this.vsDB = new VirtualSensorDBManager(context, uniqueName, paramNames, typeNames);
    }

    public void removeOlder(long timestampEpoch){

    }

    public void addVirtualSensor(int id, double[] psValues){
        //long idReturn = this.psDB.store(id, psValues);
        //this.psMap.put(new Integer(id), psValues);

    }
}
