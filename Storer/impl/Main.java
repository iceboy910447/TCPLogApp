package Storer.impl;

import LogClasses.ITransform;
import LogClasses.Wrapper.Carrier.JSONString;
import LogClasses.Wrapper.Carrier.Message;
import LogClasses.Wrapper.Threads.WrappMaster;
import LogHandler.LogMaster;
import PCinfo.OperatingSystem;
import PCinfo.Systeminformation;

import java.util.concurrent.LinkedBlockingQueue;


public class Main {
    public static void main(String[] args){
        /*OperatingSystem myOS= new OperatingSystem("Windows", "Build 1909");
        Systeminformation mySys = new Systeminformation();
        mySys.setCorecount(20);
        InetAddress myIP = null;
        try {
            myIP = InetAddress.getByName("192.168.178.45");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mySys.setIpaddress(myIP);
        mySys.setMac("E0-4F-43-71-4C-92");
        mySys.setUsername("Jonas Ziegler");
        mySys.setOs(myOS);*/
        /*
        Systeminformation mySys = Systeminformation.autofill();
        try {
            mySys.setIpaddress(InetAddress.getByName("192.168.178.45"));
        }catch (Exception e){

        }
        System.out.println("vorher");
        System.out.println(mySys.toJSON());
        Systeminformation secSys = new Systeminformation().fromJSON(mySys.toJSON());
        System.out.println(secSys.toString());
        */
        /*
        Log p = new Log();
        System.out.println(p.getClass().getName());
        */
        /*
        Systeminformation currentSys = Systeminformation.autofill();
        Message temp = new Message().setMessagetype("GET").setObject(currentSys).setData("OS");
        String result = temp.toJSON();
        System.out.println(result);

        WrappMaster wrap = new WrappMaster(0,true).setThreadCount(2);
        wrap.start();
        wrap.getInputQ().add(temp);
        JSONString temp5 = null;
        ITransform temp4 = null;
        try {
            temp4 = wrap.getJSONStringQ().take();
            temp5 = (JSONString) temp4;
            System.out.println(temp5.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Müsste funktionieren");
        String temp6 = temp5.getData();
        LinkedBlockingQueue<ITransform> Q = new LinkedBlockingQueue<ITransform>();
        WrappMaster unwrap = new WrappMaster(0,false)
                .setThreadCount(2)
                .addUnWrapQueue(Q,new OperatingSystem())
                .addUnWrapQueue(Q,new Systeminformation());
        unwrap.addUnWrapQueue(unwrap.getLogWrapperQ(),new Message()).start();
        unwrap.getJSONStringQ().add(temp5);
        try {
            temp4 = Q.take();
            Systeminformation mySys = (Systeminformation) temp4;
            System.out.println("Version =" + mySys.getOs().getVersion());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        LogMaster Logger = new LogMaster().start();





    }
}
