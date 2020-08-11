package WrapTasks.impl;

import LogClasses.ITransform;

import java.util.concurrent.BlockingQueue;



public interface IWrap extends Runnable{
    public IWrap createNew(ITransform temp, BlockingQueue<ITransform> Q);
    public IWrap setID(int ID);
    public IWrap setGroupID(int GroupID);
    public IWrap setRunnumber(int runnumber);
}
