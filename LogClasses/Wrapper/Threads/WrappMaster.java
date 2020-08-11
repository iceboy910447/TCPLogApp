package LogClasses.Wrapper.Threads;

import LogClasses.*;
import LogClasses.Wrapper.Carrier.JSONString;
import LogClasses.Wrapper.Carrier.LogWrapper;
import LogClasses.Wrapper.Threads.Helper.UnWrapPair;
import PCinfo.Systeminformation;
import WrapTasks.impl.*;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WrappMaster extends Thread{
    private int ID;
    private int groupID = 0;
    private int runnumber = 1;
    private boolean wrap_or_unwrapp; //TRUE=WRAP, FALSE=unwrapp
    private List<WrapTaskCreaterThread> ThreadList = new ArrayList<WrapTaskCreaterThread>();
    private List<WrapTaskCreaterThread> IQThreadList = new ArrayList<WrapTaskCreaterThread>();
    private List<WrapTaskCreaterThread> JSQThreadList = new ArrayList<WrapTaskCreaterThread>();




    private Systeminformation sys;


    public  BlockingQueue<ITransform> IQ = new LinkedBlockingQueue<ITransform>();
    public  BlockingQueue<ITransform> LWQ = new LinkedBlockingQueue<ITransform>();
    public  BlockingQueue<ITransform> JSQ = new LinkedBlockingQueue<ITransform>();
    private ConcurrentHashMap<String, UnWrapPair> outputPairs= new ConcurrentHashMap<String,UnWrapPair>();
    private BlockingQueue<Runnable> taskqueue = new LinkedBlockingQueue<Runnable>();

    private ThreadPoolExecutor pool;

    public WrappMaster(int ID, boolean wrap_or_unwrapp){
        this.ID = ID;
        this.wrap_or_unwrapp=wrap_or_unwrapp;
        this.sys = Systeminformation.autofill();
        pool = new ThreadPoolExecutor(2,sys.getCorecount(),10, TimeUnit.SECONDS,taskqueue);
    }

    public WrappMaster(int ID, int GroupID, boolean wrap_or_unwrapp){
        this.ID = ID;
        this.groupID = GroupID;
        this.wrap_or_unwrapp=wrap_or_unwrapp;
        this.sys = Systeminformation.autofill();
        pool = new ThreadPoolExecutor(2,sys.getCorecount(),10, TimeUnit.SECONDS,taskqueue);
    }

    public WrappMaster terminate(){
        for (WrapTaskCreaterThread temp:
                ThreadList) {
            temp.terminate();
        }
        pool.shutdown();
        return this;
    }

    public void add (ITransform temp){
        IQ.add(temp);
    }
    public void add (LogWrapper temp) {LWQ.add(temp); }
    public void add (JSONString temp) {JSQ.add(temp);}

    public int getID() {
        return ID;
    }
    public int getGroupID() {
        return groupID;
    }
    public int getRunnumber() {
        return runnumber;
    }

    public WrappMaster setID(int ID) {
        this.ID = ID;
        for (WrapTaskCreaterThread temp : ThreadList){
            temp.setID(ID);
        }
        return this;
    }
    public WrappMaster setGroupID(int groupID) {
        this.groupID = groupID;
        for (WrapTaskCreaterThread temp : ThreadList) {
            temp.setGroupID(this.groupID);
        }
        return this;
    }
    public WrappMaster setRunnumber(int runnumber) {
        this.runnumber = runnumber;
        for (WrapTaskCreaterThread temp : ThreadList) {
            temp.setRunnumber(runnumber);
        }
        return this;
    }


    public BlockingQueue<ITransform>      getInputQ() {
        return IQ;
    }
    public void setIQ(BlockingQueue<ITransform> IQ) {
        this.IQ = IQ;
        if(wrap_or_unwrapp) {
            for (WrapTaskCreaterThread temp : IQThreadList) {
                temp.setInputQ(IQ);
            }
        }else{
            for (WrapTaskCreaterThread temp : IQThreadList) {
                temp.setReturnQ(IQ);
            }
        }
    }
    public BlockingQueue<ITransform> getLogWrapperQ() {
        return LWQ;
    }
    public BlockingQueue<ITransform> getJSONStringQ() {
        return JSQ;
    }
    public void setJSQ(BlockingQueue<ITransform> JSQ) {
        if(wrap_or_unwrapp) {
            for (WrapTaskCreaterThread temp : JSQThreadList) {
                temp.setReturnQ(JSQ);
            }
        }else{
            for (WrapTaskCreaterThread temp : JSQThreadList) {
                temp.setInputQ(JSQ);
            }
        }
        this.JSQ = JSQ;
    }

    public WrappMaster addInputThread(int i){
        if(wrap_or_unwrapp) {
            addWrapThreadToList(i, IQ, LWQ, new WrapTask().setID(this.ID).setGroup_ID(this.groupID),IQThreadList);
        }else{
            addWrapThreadToList(i, JSQ, LWQ, new FromJSONTask(),JSQThreadList);
        }
        return this;
    }
    public WrappMaster addOutputThread(int i){
        if(wrap_or_unwrapp) {
            addWrapThreadToList(i,LWQ,JSQ,new ToJSONTask(),JSQThreadList);
        }else{
            addWrapThreadToList(i, LWQ, IQ, new UnWrapTask().setMap(this.outputPairs),null);
        }
        return this;
    }
    private void addWrapThreadToList(int i, BlockingQueue<ITransform> input, BlockingQueue<ITransform> output, IWrap task, List<WrapTaskCreaterThread> Liste){
        for (int j = 0; j < i; j++) {
            WrapTaskCreaterThread temp = new WrapTaskCreaterThread()
                    .setInputQ(input)
                    .setTaskType(task)
                    .setTaskQ(this.taskqueue)
                    .setThreadPool(pool)
                    .setReturnQ(output);
            ThreadList.add(temp);
            if(Liste!=null) {
                Liste.add(temp);
            }
        }
    }
    private void setStandardsAndStart(){
        for (WrapTaskCreaterThread temp: ThreadList){
            temp.setID(this.ID);
            temp.setGroupID(this.groupID);
            temp.start();
            //System.out.println("Thread gestartet");
        }
    }

    private void joinThreads(){
        for (WrapTaskCreaterThread temp: ThreadList){
            try {
                temp.join();
            } catch (InterruptedException e) {
            }
        }
    }
    public WrappMaster setThreadCount(int i){
        if((i%2)==0){
            this.addInputThread(i/2)
                    .addOutputThread(i/2);
        }else{
            this.addInputThread((i/2+1))
                    .addOutputThread(i/2);
        }
        return this;
    }
    public WrappMaster setThreadCount(int i, int j){
        return this.addInputThread(i).addOutputThread(j);
    }
    public WrappMaster addUnWrapQueue(BlockingQueue<ITransform> Q, ITransform transform){
        UnWrapPair pair = new UnWrapPair()
                .setQueue(Q)
                .setTransformableObject(transform);
        outputPairs.put(pair.getTransformableObject().retrieveClassName(),pair);
        return this;
    }
    public WrappMaster deleteUnWrapQueue(){
        outputPairs = new ConcurrentHashMap<String,UnWrapPair>();
        return this;
    }




    @Override
    public void run() {

        setStandardsAndStart();
        joinThreads();
        //System.out.println("WrapMaster wird beendet");




            /*while(true){
                if(!LWQ.isEmpty()){
                    LogWrapper temp = LWQ.remove();
                    Runnable task = new UnWrapTask(this.ID,this.groupID,temp,CQ,LQ,PQ);
                    taskqueue.add(task);
                }
                if(!JSQ.isEmpty()){
                    ITransform temp = JSQ.remove();
                    Runnable task = new FromJSONTask(temp,LWQ);
                    taskqueue.add(task);
                }
                if(JSQ.isEmpty()&&LWQ.isEmpty()){
                    try {
                        Thread.sleep(1000);
                    }catch(InterruptedException e){}
                }
            }*/

    }
}

