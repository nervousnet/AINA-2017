package ch.ethz.coss.nervousnetgen.virtual.configuration;

import java.util.ArrayList;

/**
 * Created by ales on 03/10/16.
 */
public class SensorConfiguration {
    private String nameID;
    private ArrayList<String> paramNames;

    public SensorConfiguration(String nameID, ArrayList<String> paramNames) {
        this.nameID = nameID;
        this.paramNames = paramNames;
    }

    public String getNameID() {
        return nameID;
    }

    public ArrayList<String> getParamNames() {
        return paramNames;
    }

    @Override
    public String toString() {
        return "SensorConfiguration{" +
                "nameID='" + nameID + '\'' +
                ", paramNames=" + paramNames +
                '}';
    }
}
