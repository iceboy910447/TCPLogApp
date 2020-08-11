package LogHandler;

import LogClasses.Checkpoint;
import LogClasses.ITransform;
import LogClasses.Log;
import LogClasses.Progress;
import LogClasses.Wrapper.Carrier.Message;
import LogClasses.Wrapper.Threads.TCPWrapHandler;
import PCinfo.Systeminformation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LogSlave {
    private static LogSlave singleton = null;
    private Identifier ID=null;
    private int NumberofCheckpoints;
    private int NumberofMessages;

    private String MasterIP;
    private int MasterPort;
    //private WrappMaster Wrap = null;
    //private WrappMaster UnWrap = null;
    private BlockingQueue<ITransform> input = null;//Receive
    private BlockingQueue<ITransform> output = null;//Send
    //private Systeminformation currentsys;
    //private Socket socket;
    //private TCPThread tcpThread;
    private TCPWrapHandler handler;


    private LogSlave start(){
        this
                //Startsocket()
                //.startThreads()
                .startTCPWrapHandler()
                .askForID();
        this.NumberofCheckpoints=0;
        return this;
    }

    private LogSlave autofill(String processname){
        this.ID = Identifier.autofill(processname);
        return this;
    }

    private LogSlave(){
        NumberofMessages=0;
    }
    private LogSlave setMasterNode(String IP, int Port){
        this.MasterIP=IP;
        this.MasterPort=Port;
        return this;
    }

    private LogSlave startTCPWrapHandler(){
        this.input = new LinkedBlockingQueue<ITransform>();
        this.handler = new TCPWrapHandler(this.MasterIP,this.MasterPort);
        this.handler.addUnWrapQ(this.handler.getUnWrapLogWrapperQ(),new Message())
                .addUnWrapQ(this.input,new Identifier());
        this.output = handler.getOutputQ();
        return this;
    }

    private LogSlave setID(int i){
        this.ID.setId(i);
        //this.Wrap.setID(i);
        this.handler.setID(i);
        return this;

    }
    private LogSlave askForID(){
        Message temp = new Message()
                .setMessagetype("GET")
                .setData("ID")
                .setObject(this.ID);
        handler.send(temp);
        //output.add(temp);
        try {
            Identifier myID = (Identifier) input.poll(10, TimeUnit.SECONDS);
            if (myID != null) {
                System.out.println("Zugewissene ID = " + myID.getId());
                System.out.println("Zugewissene GroupID = " + myID.getGroupid());
                System.out.println("Zugewissene Runnumber = " + myID.getRunnumber());
                this.ID = myID;
            /*
            this.UnWrap.setID(ID.getId()).setRunnumber(this.ID.getRunnumber());
            this.Wrap.setID(ID.getId()).setRunnumber(this.ID.getRunnumber());
            if(this.ID.getGroupid()!=0){
                this.UnWrap.setGroupID(this.ID.getGroupid());
                this.Wrap.setGroupID(this.ID.getGroupid());
            }*/
                System.out.println("ID ="+ this.ID.getId());
                this.handler.setID(this.ID.getId())
                        .setGroupID(this.ID.getGroupid())
                        .setRunnumber(this.ID.getRunnumber());
                System.out.println("Logslave hat ID erhalten");
            }else{
                this.ID = new Identifier().setId(0).setGroupid(0).setProcessname(this.ID.getProcessname()).setCurrentsys(Systeminformation.autofill());
                this.handler.setID(this.ID.getId())
                        .setGroupID(this.ID.getGroupid())
                        .setRunnumber(this.ID.getRunnumber());
            }
        }catch (InterruptedException e){
        }catch (NullPointerException e){}
        return this;
    }

    public BlockingQueue<ITransform> getInput() {
        return input;
    }
    public BlockingQueue<ITransform> getOutput() {
        return output;
    }

    public LogSlave getMessageCount(){
        System.out.println(NumberofMessages+" Message wurden erstellt");
        return this;
    }

    //Methodes to create Logs, Checkpoints and Progress;
    public LogSlave newLog(String Message){
        NumberofMessages++;
        Log temp = new Log()
                .setTime(System.currentTimeMillis())
                .setMessage(Message);
        output.add(temp);
        return this;
    }
    public LogSlave newProgress(double value){
        NumberofMessages++;
        Progress temp = new Progress()
                .setTime(System.currentTimeMillis())
                .setProgress_value(value);
        output.add(temp);
        return this;
    }
    public LogSlave newCheckpoint(int Number, String Description){
        NumberofMessages++;
        Checkpoint temp = new Checkpoint()
                .setTime(System.currentTimeMillis())
                .setDescription(Description)
                .setNumber(Number);
        output.add(temp);
        return this;
    }
    //private LogSlave askForID(String Prozessname)


    public synchronized static LogSlave getLogger(String Processname, String IP, int port){
        if(singleton==null){
            singleton=new LogSlave()
                    .autofill(Processname)
                    .setMasterNode(IP,port)
                    .start();
        }
        return singleton;
    }
    public synchronized static LogSlave getLogger(String Processname) {
        return LogSlave.getLogger(Processname,"127.0.0.1",10000);
    }

    public synchronized static LogSlave getLogger(String Processname, int Port) {
        return LogSlave.getLogger(Processname,"127.0.0.1",Port);
    }


    /*
        this.ID = new Identifier();
        this.ID.setProcessname(Processname);
        this.currentsys= Systeminformation.autofill();

     */





}
