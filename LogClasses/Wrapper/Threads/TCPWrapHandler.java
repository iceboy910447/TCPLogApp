package LogClasses.Wrapper.Threads;

import LogClasses.ITransform;
import TCPHandler.TCPThread;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class TCPWrapHandler {
    private Socket socket;
    private TCPThread tcp;
    private WrappMaster Wrap;
    private WrappMaster UnWrap;
    private BlockingQueue<ITransform> output=null;


    public TCPWrapHandler(Socket socket){
        this.socket=socket;
        this.start();
    }
    public TCPWrapHandler(String IP, int Port){
        this.startsocket(IP,Port)
            .start();
    }
    public TCPWrapHandler terminate(){
        tcp.terminate();
        Wrap.terminate();
        UnWrap.terminate();
        //Socket bleibt erhalten
        return this;
    }
    private TCPWrapHandler start(){
        this.Wrap = new WrappMaster(0,true)
                .setThreadCount(2);
        this.UnWrap = new WrappMaster(0,false)
                .setThreadCount(2);
        this.tcp = new TCPThread((this.socket))
                .setOutput(this.Wrap.getJSONStringQ())
                .setInput(this.UnWrap.getJSONStringQ());
        this.output=this.Wrap.getInputQ();
        this.Wrap.start();
        this.UnWrap.start();
        this.tcp.start();;
        return this;
    }
    public TCPWrapHandler addUnWrapQ(BlockingQueue<ITransform> Q, ITransform transform){
        this.UnWrap.addUnWrapQueue(Q,transform);
        return this;
    }
    public BlockingQueue<ITransform> getOutputQ(){
        return Wrap.getInputQ();
    }
    public TCPWrapHandler setID(int ID){
        this.Wrap.setID(ID);
        this.UnWrap.setID(ID);
        return this;
    }
    public TCPWrapHandler setGroupID(int groupID){
        this.Wrap.setGroupID(groupID);
        this.UnWrap.setGroupID(groupID);
        return this;
    }
    public TCPWrapHandler setRunnumber(int runnumber){
        this.Wrap.setRunnumber(runnumber);
        this.UnWrap.setRunnumber(runnumber);
        return this;
    }
    private TCPWrapHandler startsocket(String IP, int Port){
        try {
            this.socket = new Socket(IP,Port);
        } catch (ConnectException e){
            String Message = e.getMessage();
            if(Message.contains("Connection refused: connect")){
                System.out.println("Keine Verbindung zum LogMaster möglich!");
                System.out.println("Master-IP = "+IP);
                System.out.println("Master-Port = "+Port);
            }else{
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    public TCPWrapHandler send(ITransform SendObject){
        this.output.add(SendObject);
        return this;
    }
    public BlockingQueue<ITransform> getUnWrapLogWrapperQ(){
        return UnWrap.getLogWrapperQ();
    }


}
