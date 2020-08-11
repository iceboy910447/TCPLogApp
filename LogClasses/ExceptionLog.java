package LogClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExceptionLog implements ITransform {
    public String description;
    public String methode;
    public String type;
    public long time;

    public String getDescription() {
        return description;
    }

    public ExceptionLog setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMethode() {
        return methode;
    }

    public ExceptionLog setMethode(String methode) {
        this.methode = methode;
        return this;
    }

    public String getType() {
        return type;
    }

    public ExceptionLog setType(String type) {
        this.type = type;
        return this;
    }

    public long getTime() {
        return time;
    }

    public ExceptionLog setTime(long time) {
        this.time = time;
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
        return "ExceptionLog";
    }

    @Override
    public ExceptionLog fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        ExceptionLog temp = null;
        try {
            temp = mapper.readValue(json, ExceptionLog.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }
}
