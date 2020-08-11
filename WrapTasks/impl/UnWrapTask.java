package WrapTasks.impl;

import LogClasses.*;
import LogClasses.Wrapper.Carrier.ICarryData;
import LogClasses.Wrapper.Threads.Helper.UnWrapPair;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class UnWrapTask extends WrapTask {
    private ConcurrentHashMap<String, UnWrapPair> myMap;
    private ICarryData wrapped;

    public UnWrapTask(){}

    public UnWrapTask(ITransform wrapped, ConcurrentHashMap<String,UnWrapPair> currentMap){
        this.wrapped=(ICarryData)wrapped;
        this.myMap=currentMap;
    }
    public UnWrapTask setMap(ConcurrentHashMap<String, UnWrapPair> currentMap){
        this.myMap=currentMap;
        return this;
    }


    @Override
    public void run() {
        String type = wrapped.retrieveType();
        //System.out.println("type = "+type);
        synchronized (myMap) {
            for (Map.Entry<String, UnWrapPair> entry : myMap.entrySet()) {
                //System.out.println("Key "+  entry.getKey().toString());
                if (type.equals(entry.getKey().toString())) {
                    //System.out.println("Treffer");
                    UnWrapPair value = entry.getValue();
                    ITransform helper = value.getTransformableObject();
                    ITransform result = unwrap(wrapped, helper);
                    BlockingQueue<ITransform> Q = value.getQueue();
                    Q.add(result);
                    return;
                }
            }
            //System.out.println("Kein Treffer");
        }

    }

    @Override
    public IWrap createNew(ITransform temp, BlockingQueue<ITransform> Q) {
        //System.out.println("unwraptask wird erstellt");
        return new UnWrapTask(temp,this.myMap);
    } //ReturnQ wird nicht benötigt, da diese direkt aus der Map genommen wird.

    public ITransform unwrap(ICarryData wrapped, ITransform helper){
        ITransform unwrapped = helper.fromJSON(wrapped.retrievePDU());
        return unwrapped;
    }


}
