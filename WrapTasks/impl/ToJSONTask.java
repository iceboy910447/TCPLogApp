package WrapTasks.impl;

import LogClasses.*;
import LogClasses.Wrapper.Carrier.JSONString;
import LogClasses.Wrapper.Carrier.LogWrapper;

import java.util.concurrent.BlockingQueue;

public class ToJSONTask implements Runnable,IWrap {
    public static BlockingQueue<ITransform> returnqueue;
    private ITransform logWrapper;

    public ToJSONTask(){}
    public ToJSONTask(ITransform temp, BlockingQueue<ITransform> returnqueue){
        this.logWrapper=temp;
        this.returnqueue=returnqueue;
    }

    @Override
    public void run() {
        LogWrapper wrapped = (LogWrapper) logWrapper;
        returnqueue.add(new JSONString().setData(logWrapper.toJSON()));
        //System.out.println("ToJSONTASK ausgeführt");
    }

    @Override
    public IWrap createNew(ITransform temp, BlockingQueue<ITransform> Q) {
        return new ToJSONTask(temp,Q);
    }

    @Override
    public IWrap setID(int ID) {
        return this;
    }

    @Override
    public IWrap setGroupID(int GroupID) {
        return this;
    }

    @Override
    public IWrap setRunnumber(int runnumber) {
        return this;
    }
}