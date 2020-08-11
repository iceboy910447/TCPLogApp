package UDPHandler.Receiver;

import LogClasses.ITransform;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class UDPBase extends Thread{
    public boolean terminate = false;
    public int Portnumber;
    public DatagramSocket socket=null;
    public BlockingQueue<Runnable> packettasks;
    public BlockingQueue<Runnable> Sendtasks;
    public ThreadPoolExecutor pool;

    public UDPBase(int currentPortnumber, int maxThreadnumber){
        this.Portnumber=currentPortnumber;
        try {
            socket = new DatagramSocket(Portnumber);
        }catch(SocketException e){
            if (e instanceof BindException){
                System.out.println("Port seemingly already in use!");
                e.printStackTrace();
            }else{
                e.printStackTrace();
            }
        }
        this.packettasks=new LinkedBlockingDeque<Runnable>();
        this.pool=new ThreadPoolExecutor(2,maxThreadnumber,10, TimeUnit.SECONDS,packettasks);
    }

    public void setTerminate(boolean value){
        this.terminate=value;
    }

    @Override
    public void run() {
        while(!terminate){


        }
    }
    public boolean send(ITransform temp){
        return true;

    }

    public void receive(){
        DatagramPacket packet = new DatagramPacket(new byte[4096], 4096); //Original Value 1024
        try {
            socket.receive(packet);
        }catch (IOException e){
            e.printStackTrace();
        }
        UDPReceiveMessage temp = new UDPReceiveMessage(socket,packet);
        packettasks.add(temp);
    }
}
