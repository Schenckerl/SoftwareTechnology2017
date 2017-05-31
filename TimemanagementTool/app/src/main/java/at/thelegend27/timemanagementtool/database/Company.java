package at.thelegend27.timemanagementtool.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dominik on 17.05.17.
 */

public class Company {
    public String name;
    public String ceo_id;

    public Company(){

    }

    public Company(String name, String ceo_id){
        this.name = name;
        this.ceo_id = ceo_id;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("ceo_id", ceo_id);

        return ret;
    }
}
