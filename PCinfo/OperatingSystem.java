package PCinfo;

import LogClasses.ITransform;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)

public class OperatingSystem implements ITransform {
    private String os;
    private String version;

    public OperatingSystem(String os, String Version){
        this.os = os;
        this.version =Version;
    }
    public OperatingSystem(){}

    public void setOs(String os) {
        this.os = os;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return os;
    }

    public String getVersion() {
        return version;
    }

    public static OperatingSystem autofill(){
        String OSname = System.getProperty("os.name");
        String OSversion = System.getProperty("os.version");
        OperatingSystem myOS = new OperatingSystem(OSname,OSversion);
        return myOS;
    }

    public String toString(){
        String temp = this.os;
        temp= temp+"; "+this.version;
        return temp;
    }
    public String toJSON(){
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
        return "OperatingSystem";
    }

    @Override
    public OperatingSystem fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        OperatingSystem temp = null;
        try {
            temp = mapper.readValue(json, OperatingSystem.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }




}
