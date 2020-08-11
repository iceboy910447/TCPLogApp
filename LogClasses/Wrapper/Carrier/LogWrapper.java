package LogClasses.Wrapper.Carrier;

import LogClasses.ITransform;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogWrapper implements ITransform, ICarryData {
    public int id;
    public int group_id;
    public int runnumber;
    public long send_time;
    public String type;
    public String pdu;

    public LogWrapper(){}

    public LogWrapper setId(int id) {
        this.id = id;
        return this;
    }
    public int getId() {
        return id;
    }

    public LogWrapper setGroup_id(int group_id) {
        this.group_id = group_id;
        return this;
    }
    public int getGroup_id() {
        return group_id;
    }

    public int getRunnumber() {
        return runnumber;
    }
    public LogWrapper setRunnumber(int runnumber) {
        this.runnumber = runnumber;
        return this;
    }

    public LogWrapper setSend_time(long send_time) {
        this.send_time = send_time;
        return this;
    }
    public long getSend_time() {
        return send_time;
    }

    public LogWrapper setType(String type) {
        this.type = type;
        return this;
    }
    public String retrieveType() {
        return type;
    }

    public LogWrapper setPdu(String pdu) {
        this.pdu = pdu;
        return this;
    }

    public String getPdu() {
        return pdu;
    }

    public String getType() {
        return type;
    }

    public String retrievePDU() {
        return pdu;
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
        return "LogWrapper";
    }




    public static LogWrapper wrap(ITransform temp){
        LogWrapper Wrapped = new LogWrapper();
        Wrapped.setPdu(temp.toJSON());
        Wrapped.setSend_time(System.currentTimeMillis());
        return Wrapped;
    }



    @Override
    public LogWrapper fromJSON(String json) {

        ObjectMapper mapper = new ObjectMapper();
        LogWrapper temp = null;
        try {
            temp = mapper.readValue(json, LogWrapper.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }
}

