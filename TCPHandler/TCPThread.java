package TCPHandler;

import LogClasses.ITransform;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TCPThread {
    public Socket socket;
    public InetAddress address;
    public BlockingQueue<ITransform> input = null; //Input = Receive
    public BlockingQueue<ITransform> output = null; //Output = Send
    private TCPSendThread SendThread;
    private TCPReceiveThread ReceiveThread;

    public TCPThread(Socket socket){
        this.socket = socket;
        this.SendThread = new TCPSendThread(socket);
        this.ReceiveThread = new TCPReceiveThread(socket);


    }
    public TCPThread start(){
        if (output == null) {
            SendThread.start();
            this.output = SendThread.getOutput();
        }else {
            SendThread.setOutput(this.output).start();
        }
        if (input == null) {
            ReceiveThread.start();
            this.input=ReceiveThread.getInput();
        }else {
            ReceiveThread.setInput(input).start();
        }
        return this;
    }
    public BlockingQueue<ITransform> getInput(){
        return this.input;
    }
    public TCPThread setInput(BlockingQueue<ITransform> input) {
        this.input = input;
        this.ReceiveThread.setInput(input);
        return this;
    }
    public BlockingQueue<ITransform> getOutput() {
        return output;
    }
    public TCPThread setOutput(BlockingQueue<ITransform> Q){
        this.output = Q;
        this.SendThread.setOutput(Q);
        return this;
    }

    public TCPThread(Socket socket, BlockingQueue<ITransform> input, BlockingQueue<ITransform> output){
        this.socket = socket;
        this.SendThread = new TCPSendThread(socket);
        this.ReceiveThread = new TCPReceiveThread(socket);
        this.output=output;
        this.input = input;


    }
    public TCPThread terminate(){
        SendThread.terminate();
        ReceiveThread.terminate();
        return this;
    }








}
