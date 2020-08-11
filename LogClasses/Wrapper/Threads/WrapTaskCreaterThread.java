package LogClasses.Wrapper.Threads;

import LogClasses.ITransform;
import LogClasses.Wrapper.Carrier.LogWrapper;
import WrapTasks.impl.IWrap;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WrapTaskCreaterThread extends Thread{
    public int myID;
    public int GroupID;
    public int Runnumber;
    public BlockingQueue<ITransform> inputqueue;
    public BlockingQueue<ITransform> returnqueue;
    public BlockingQueue<Runnable> taskqueue;
    public IWrap task;
    public ThreadPoolExecutor pool = null;
    public boolean terminate;


    public WrapTaskCreaterThread(){
        this.terminate = false;
    }
    public WrapTaskCreaterThread setID(int ID){
        this.myID=ID;
        this.task.setID(ID);
        return this;
    }
    public WrapTaskCreaterThread setGroupID(int groupID){
        this.GroupID=groupID;
        this.task.setGroupID(groupID);
        return this;
    }
    public WrapTaskCreaterThread setRunnumber(int runnumber){
        this.Runnumber = runnumber;
        this.task.setRunnumber(runnumber);
        return this;
    }
    public WrapTaskCreaterThread setReturnQ(BlockingQueue<ITransform> temp){
        this.returnqueue=temp;
        return this;
    }

    public WrapTaskCreaterThread setInputQ(BlockingQueue<ITransform> temp){
        this.inputqueue=temp;
        return this;
    }
    public WrapTaskCreaterThread setTaskQ(BlockingQueue<Runnable> temp){
        this.taskqueue=temp;
        return this;
    }
    public WrapTaskCreaterThread setTaskType(IWrap task){
        this.task=task;
        return this;
    }
    public WrapTaskCreaterThread setThreadPool(ThreadPoolExecutor pool){
        this.pool=pool;
        return this;
    }
    public WrapTaskCreaterThread terminate(){
        this.terminate=true;
        return this;
    }

    @Override
    public void run() {
        while(!this.terminate) {
            try {
                ITransform temp = inputqueue.poll(10, TimeUnit.SECONDS);
                //System.out.println("Aus inputQ genommen");
                if(temp!=null) {
                    Runnable temp2 = task.createNew(temp, returnqueue);
                    pool.execute(temp2);
                }
            } catch (InterruptedException e) { }
        }
        //System.out.println("WrapTaskCreaterThread wird beendet");
    }
}

