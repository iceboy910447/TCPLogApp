package LogHandler;

import LogClasses.ITransform;
import PCinfo.Systeminformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Identifier implements ITransform {
    private int id=0;
    private int groupid=0;
    private int runnumber=0;
    private String processname =null;
    private Systeminformation currentsys = null;
    public Identifier(){}

    public int getId() {
        return id;
    }
    public Identifier setId(int id) {
        this.id = id;
        return this;
    }
    public int getGroupid() {
        return groupid;
    }
    public Identifier setGroupid(int groupid) {
        this.groupid = groupid;
        return this;
    }
    public int getRunnumber() {
        return runnumber;
    }
    public Identifier setRunnumber(int runnumber) {
        this.runnumber = runnumber;
        return this;
    }
    public String getProcessname() {
        return processname;
    }
    public Identifier setProcessname(String processname) {
        this.processname = processname;
        return this;
    }
    public Systeminformation getCurrentsys() {
        return currentsys;
    }
    public Identifier setCurrentsys(Systeminformation currentsys) {
        this.currentsys = currentsys;
        return this;
    }

    public static Identifier autofill(String processname){/*
        ObjectMapper mapper=null;
        mapper = new ObjectMapper();
        Identifier identifier = new Identifier();
        try {
            identifier = mapper.readValue(new File("Identifier.json"),Identifier.class);
        } catch (IOException e) {
            identifier.setCurrentsys(Systeminformation.autofill());
        }finally {
            identifier.setProcessname(processname);
        }
        */
        Identifier result = new Identifier();
        result.processname = processname;
        result.currentsys = Systeminformation.autofill();
        return result;
    }

    private Identifier createNew(){
        this.setCurrentsys(Systeminformation.autofill());
        return this;
    }

    @Override
    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString=null;
        try {
            jsonString=mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    @Override
    public String retrieveClassName() {
        return "Identifier";
    }

    @Override
    public Identifier fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Identifier temp = null;
        try {
            temp = mapper.readValue(json, Identifier.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }
}
