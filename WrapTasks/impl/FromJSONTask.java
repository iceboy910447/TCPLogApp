package WrapTasks.impl;

import LogClasses.ITransform;
import LogClasses.Wrapper.Carrier.JSONString;
import LogClasses.Wrapper.Carrier.LogWrapper;

import java.util.concurrent.BlockingQueue;

public class FromJSONTask extends WrapTask{
    private JSONString json;
    private static LogWrapper LWTH = new LogWrapper();

    public FromJSONTask(){}
    public FromJSONTask(JSONString json, BlockingQueue<ITransform> returnqueue){
        this.json=json;
        this.returnqueue = returnqueue;
    }

    @Override
    public void run() {
        returnqueue.add(fromJson(json.getData()));
    }
    public LogWrapper fromJson(String json){

        LogWrapper result = LWTH.fromJSON(json);
        return result;
    }

    @Override
    public IWrap createNew(ITransform temp, BlockingQueue<ITransform> Q) {
        //System.out.println("FromJSONTask wird erstellt");
        return new FromJSONTask((JSONString)temp,Q);
    }
}
