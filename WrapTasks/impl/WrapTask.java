package WrapTasks.impl;

import LogClasses.ITransform;
import LogClasses.Wrapper.Carrier.LogWrapper;

import java.util.concurrent.BlockingQueue;

public  class WrapTask implements Runnable,IWrap{
    public int ID;
    public int Group_ID;
    public int Runnumber;
    public BlockingQueue<ITransform> returnqueue;
    public ITransform TransformableObject;
    public void setReturnqueue(BlockingQueue<ITransform> queue){
        returnqueue=queue;
    }

    public WrapTask(){}
    public WrapTask(int ID, int Group,int Runnumber, ITransform temp, BlockingQueue<ITransform> queue){
        this.ID=ID;
        this.Group_ID=Group;
        this.Runnumber = Runnumber;
        this.TransformableObject = temp;
        this.returnqueue = queue;
    }

    public WrapTask setID(int ID) {
        this.ID = ID;
        return this;
    }

    @Override
    public IWrap setGroupID(int GroupID) {
        Group_ID = GroupID;
        return this;
    }

    @Override
    public IWrap setRunnumber(int runnumber) {
        Runnumber=runnumber;
        return this;
    }

    public WrapTask setGroup_ID(int group_ID) {
        Group_ID = group_ID;
        return this;
    }

    public LogWrapper wrap(ITransform transformableObject){
        String type = transformableObject.retrieveClassName();
        LogWrapper result = new LogWrapper()
                .setPdu(transformableObject.toJSON())
                .setSend_time(System.currentTimeMillis())
                .setRunnumber(Runnumber)
                .setGroup_id(Group_ID)
                .setId(ID)
                .setType(type);
        //System.out.println(transformableObject.getClassName());

        return result;
    }
    @Override
    public void run() {
        //System.out.println("Task gestartet");
        returnqueue.add(wrap(TransformableObject));
        //System.out.println("Task ausgeführt");
    }

    @Override
    public IWrap createNew( ITransform temp, BlockingQueue<ITransform> Q) {
        //System.out.println("Wraptask erstellt");
        return new WrapTask(this.ID,this.Group_ID,this.Runnumber,temp,Q); //has to be Overwritten;
    }
}
