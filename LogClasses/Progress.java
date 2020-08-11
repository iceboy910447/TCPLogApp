package LogClasses;

import Storer.impl.myTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Progress implements ITransform {



    private double progress_value;
    private long time; //between 0 and 1

    public long getTime() {
        return time;
    }

    public double getProgress_value() {
        return progress_value;
    }

    public Progress setProgress_value(double progress_value) {
        this.progress_value = progress_value;
        return this;
    }

    public Progress setTime(long time) {
        this.time = time;
        return this;
    }

    public Progress(long current_time, double current_progress){
        this.time = current_time;
        this.progress_value =current_progress;
    }
    public Progress(){}

    public String getHumanReadableTime(){
        return myTime.formated_time(this.getTime());
    }
    @Override
    public String toString(){
        int temp = (int)(this.getProgress_value()*10000);
        double percent = ((double)temp)/100.0;
        return "Progress: "+percent+"% at "+this.getHumanReadableTime();
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
        //this.getClass().getName();
        return "Progress";
    }



    public Progress fromJSON(String json){
        ObjectMapper mapper = new ObjectMapper();
        Progress temp = null;
        try {
            temp = mapper.readValue(json, Progress.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;

    }

    }