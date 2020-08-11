package LogClasses;

import Storer.impl.myTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Checkpoint implements ITransform {
    private int number;
    private long time;
    private String description;

    public Checkpoint(long current_time, int current_Number, String txt){
        this.time=current_time;
        this.number = current_Number;
        this.description = txt;
    }
    public Checkpoint(){}

    public Checkpoint setNumber(int number) {
        this.number = number;
        return this;
    }

    public Checkpoint setTime(long time) {
        this.time = time;
        return this;
    }

    public Checkpoint setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public long getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getHumanReadableTime(){
        return myTime.formated_time(this.getTime());
    }

    public String toString(){
        if(this.description !=null) {
            return "Checkpoint: " + this.getNumber() + " reached at " + this.getHumanReadableTime() + "\n Description: " + this.getDescription();
        }else{
            return "Checkpoint: " + this.getNumber() + " reached at " + this.getHumanReadableTime();
        }
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
        return "Checkpoint";
    }

    public Checkpoint fromJSON(String json){
        ObjectMapper mapper = new ObjectMapper();
        Checkpoint temp = null;
        try {
            temp = mapper.readValue(json, Checkpoint.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!temp.retrieveClassName().contains("Checkpoint")){
            System.out.println("Problem");
        }
        //System.out.println(temp.toString());
        return temp;
    }

}
