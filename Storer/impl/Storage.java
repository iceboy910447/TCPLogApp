package Storer.impl;

import LogClasses.ITransform;
import LogClasses.Wrapper.Threads.Helper.UnWrapPair;
import PCinfo.Systeminformation;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    public int id;
    public int groupid;
    public int lastrunnumber;
    public String processname;
    public Map<Integer,Storer> runnumbermap;
    public Storer currentStorer;
    public Systeminformation Sys;

    public Storage(int ID){
        this.id = ID;
        this.lastrunnumber=1;
        this.runnumbermap=new ConcurrentHashMap<Integer, Storer>();
        this.currentStorer=new Storer(ID);
        this.runnumbermap.put(this.lastrunnumber,this.currentStorer);
    }

    public int getId() {
        return id;
    }

    public Storage setId(int id, Systeminformation Sys) {
        this.id = id;
        this.Sys = Sys;
        return this;
    }

    public int getGroupid() {
        return groupid;
    }

    public Storage setGroupid(int groupid) {
        this.groupid = groupid;
        return this;
    }
    public long getMeanRuntime(){
        long sum = 0;
        for(Map.Entry<Integer, Storer> entry: runnumbermap.entrySet()){
            //System.out.println("Key "+  entry.getKey().toString());
            Storer temp = entry.getValue();
            sum += temp.getRunTime();
        }
        System.out.println("sum = "+sum);
        double mean = ((double)sum)/((double)runnumbermap.size());
        System.out.println("Mean Runtime for ID "+ this.getId()+" over "+this.getLastrunnumber()+ " is "+mean);
        return (long) mean;
    }

    public int getLastrunnumber() {
        return lastrunnumber;
    }

    public void setLastrunnumber(int lastrunnumber) {
        this.lastrunnumber = lastrunnumber;
    }

    public String getProcessname() {
        return processname;
    }

    public Storage setProcessname(String processname) {
        this.processname = processname;
        return this;
    }

    public Map<Integer, Storer> getRunnumbermap() {
        return runnumbermap;
    }

    public Storage setRunnumbermap(Map<Integer, Storer> runnumbermap) {
        this.runnumbermap = runnumbermap;
        return this;
    }

    public Systeminformation getSys() {
        return Sys;
    }

    public Storage setSys(Systeminformation sys) {
        Sys = sys;
        return this;
    }

    public synchronized Storage createNewRun(){
        this.getMeanRuntime();
        this.currentStorer.checkStorerSize();
        int Number_of_new_Run = ++lastrunnumber;
        Storer newOne = new Storer(this.id);
        runnumbermap.put(Number_of_new_Run,newOne);
        this.currentStorer=newOne;
        this.lastrunnumber=Number_of_new_Run;
        return this;
    }

    public Storer getCurrentStorer() {
        return currentStorer;
    }

    public void store(ITransform temp){
        this.currentStorer.store(temp);
    }
}
