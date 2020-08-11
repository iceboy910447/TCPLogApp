package LogClasses.Wrapper.Carrier;

import LogClasses.ITransform;

public class JSONString implements ITransform {
    public String Nachrichtenlaenge;
    public String data;

    @Override
    public String toJSON() {
        return Nachrichtenlaenge+data;
    }

    @Override
    public String retrieveClassName() {
        return "JSONString";
    }

    public String getData() {
        return data;
    }

    @Override
    public ITransform fromJSON(String json) {
        return this;
    }

    @Override
    public String toString() {
        return Nachrichtenlaenge+data;
    }

    public JSONString() {}

    public JSONString setData(String data) {
        this.Nachrichtenlaenge = "Nachrichtenlaenge:"+(data.length()+1)+" ";
        this.data = data+" ";
        return this;
    }
}
