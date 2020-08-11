package TCPHandler;

import LogClasses.ITransform;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TCPSendThread extends Thread{
    public Socket socket;
    public BlockingQueue<ITransform> output = null;
    public boolean terminate;
    public TCPSendThread(Socket socket){
        this.socket=socket;
        this.output=new LinkedBlockingQueue<ITransform>();
        this.terminate = false;
    }
    public TCPSendThread terminate(){
        this.terminate = true;
        return this;
    }

    public BlockingQueue<ITransform> getOutput() {
        return output;
    }

    public TCPSendThread setOutput(BlockingQueue<ITransform> output) {
        this.output = output;
        return this;
    }

    @Override
    public void run() {

        while (!this.terminate) {
            try {
                ITransform sendobject = output.poll(10, TimeUnit.SECONDS);
                if(sendobject!=null) {
                    String Sendmessage = sendobject.toJSON();
                    send(Sendmessage);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {}
        }
        //System.out.println("SendThread wird beendet");
    }
    public void send(String message) throws  NullPointerException{
        byte[] data = message.getBytes(StandardCharsets.UTF_8);
        try {
            OutputStream outStream = socket.getOutputStream();
            outStream.write(data);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
