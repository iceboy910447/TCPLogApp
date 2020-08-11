package LogClasses;

import Storer.impl.myTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Log implements ITransform {
    private String message;
    private long time;

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
    public Log(long current_time, String current_Message){
        this.time = current_time;
        this.message = current_Message;
    }

    public Log setMessage(String message) {
        this.message = message;
        return this;
    }

    public Log setTime(long time) {
        this.time = time;
        return this;
    }

    public Log(){}

    public String getHumanReadableTime(){
        return myTime.formated_time(this.getTime());
    }

    public String toString(){
        return "Message: "+this.getMessage()+" received at "+this.getHumanReadableTime();
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
        return "Log";
    }

    public Log fromJSON(String json){
        ObjectMapper mapper = new ObjectMapper();
        Log temp = null;
        try {
            temp = mapper.readValue(json, Log.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }


}
