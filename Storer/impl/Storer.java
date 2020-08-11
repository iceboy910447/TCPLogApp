package Storer.impl;

import LogClasses.*;
import LogClasses.Wrapper.Threads.Helper.UnWrapPair;
import PCinfo.Systeminformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Storer implements IStorage{
    public int Checkpointnumber;
    private int ID;
    public Map<String,List<ITransform>> CategoriallList_Map;
    /*
    private List<ITransform> exceptionList = new ArrayList<ITransform>();
    private List<ITransform> progressList = new ArrayList<ITransform>();
    private List<ITransform> logList =new ArrayList<ITransform>();
    private List<ITransform> checkpointList = new ArrayList<ITransform>();

     */
    private long starttime;
    private long runtime;

    public Storer(int id){
        this.ID=id;
        this.starttime= System.currentTimeMillis();
        this.CategoriallList_Map = new ConcurrentHashMap<String,List<ITransform>>();
        this.Checkpointnumber=0;
    }


    public long getStartTime (){
        return this.starttime;
    }
    private Storer updateRunTime(){
        runtime = System.currentTimeMillis()-starttime;
        return this;
    }
    public Storer checkStorerSize(){
        checkListSize(new Checkpoint().retrieveClassName());
        checkListSize(new Progress().retrieveClassName());
        checkListSize(new Log().retrieveClassName());
        checkListSize(new ExceptionLog().retrieveClassName());
        return this;
    }
    private Storer checkListSize(String ListName){
        if(CategoriallList_Map.containsKey(ListName)){
            List<ITransform> temp = CategoriallList_Map.get(ListName);
            System.out.println("Liste für "+ ListName+ " enthält "+temp.size()+" Elemente!");
        }
        return this;
    }

    @Override
    public long getRunTime() {
        return this.runtime;
    }


    public int getID() {
        return ID;
    }

    @Override
    public ITransform getLast(String classname) {
        if(CategoriallList_Map.containsKey(classname)){
            List<ITransform> temp = CategoriallList_Map.get(classname);
            ITransform last = temp.get(temp.size()-1);
            return last;
        }else {
            return null;
        }
    }
    /*
    public List<ITransform> getProgressList() {
        return progressList;
    }

    public List<ITransform> getLogList() {
        return logList;
    }

    public List<ITransform> getCheckpointList() {
        return checkpointList;
    }

    public List<ITransform> getExceptionList() {
        return exceptionList;
    }

     */
    /*
    public String getLastProgress(){
        return progressList.get(progressList.size()-1).toString();
    }
    public String getLastLog(){
        return logList.get(logList.size()-1).toString();
    }
    public String getLastCheckpoint() { return checkpointList.get(checkpointList.size()-1).toString();}
    public String getLastException() { return exceptionList.get(exceptionList.size()-1).toString();}
    */
    /*
    public List<String> getAllProgresses() {
        return getAll(progressList);
    }
    public List<String> getAllLogs() {
        return getAll(logList);
    }
    public List<String> getAllCheckpoints(){
        return getAll(checkpointList);
    }
    public List<String> getAllExceptions(){
        return getAll(exceptionList);
    }
    private List<String> getAll(List<ITransform>temp){
        List<String> Result = new ArrayList<String>();
        for(ITransform obj : temp){
            Result.add(temp.toString());
        }
        return Result;
    }
    */
    public synchronized boolean store(ITransform transform){
        String type = transform.retrieveClassName();
        if(type.contains("Checkpoint")){
            Checkpointnumber++;
            if((Checkpointnumber%1000)==0) {
                System.out.println("Checkpointnumber = " + Checkpointnumber);
            }
        }
        this.updateRunTime();
        synchronized (CategoriallList_Map) {
            if(CategoriallList_Map.containsKey(type)){
                //System.out.println("Type "+ type+ " in Storer enthalten");
                List<ITransform> temp = CategoriallList_Map.get(type);
                temp.add(transform);
                CategoriallList_Map.put(type, temp);
                return true;
            }else{
                //System.out.println("Type "+ type+ " in Storer noch nicht enthalten");
                List<ITransform> temp = new ArrayList<ITransform>();
                temp.add(transform);
                CategoriallList_Map.put(type,temp);
                return true;
            }
        }


    }


    @Override
    public List<ITransform> retrieveAll(String type) {
        if(CategoriallList_Map.containsKey(type)){
            return CategoriallList_Map.get(type);
        }else{
            return null;
        }
    }

}
