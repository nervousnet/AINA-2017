package ch.ethz.coss.nervousnetgen.nervousnet.database;

import java.util.HashMap;

/**
 * Created by ales on 21/09/16.
 */
public interface iSensorStore {
    public void store(HashMap<String, Object> sample);
}
