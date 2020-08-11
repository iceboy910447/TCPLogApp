package LogClasses.Wrapper.Carrier;

import LogClasses.ITransform;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message implements ITransform, ICarryData {
    String messagetype;
    String data;
    String objectType;
    String object= null;

    public String getObjectType() {
        return objectType;
    }
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    public void setObject(String object) {
        this.object = object;
    }
    public String getMessagetype() {
        return messagetype;
    }
    public String getData() {
        return data;
    }
    public String getObject() {
        return object;
    }
    public Message setMessagetype(String type) {
        messagetype = type;
        return this;
    }
    public Message setData(String data) {
        this.data = data;
        return this;
    }
    public Message setObject(String object, String objectType) {
        this.object = object;
        this.objectType = objectType;
        return this;
    }
    public Message setObject(ITransform object) {
        this.object = object.toJSON();
        this.objectType = object.retrieveClassName();

        return this;
    }

    @Override
    public String retrieveType() {
        return this.objectType;
    }
    @Override
    public String retrievePDU() {
        return this.getObject();
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
        return "Message";
    }

    @Override
    public Message fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Message temp = null;
        try {
            temp = mapper.readValue(json, Message.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }
}
