package UDPHandler.Receiver;

import LogClasses.ITransform;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPMessage {
    public DatagramSocket socket;
    public DatagramPacket packet;
    public InetAddress address;
    public int port;
    public int len;
    public byte[] data;

    public UDPMessage(DatagramSocket socket, DatagramPacket packet){
        this.socket=socket;
        this.packet=packet;
    }
    public UDPMessage(DatagramSocket socket){
        this.socket=socket;
    }

    public void unpack(){
        this.address = packet.getAddress();
        this.port    = packet.getPort();
        this.len     = packet.getLength();
        this.data    = packet.getData();
    }
    public void sendString(String res){
        byte[] raw = res.getBytes();
        //DatagramSocket second_Socket;
        try {
            //second_Socket= new DatagramSocket();
            DatagramPacket response_Packet = new DatagramPacket(raw, raw.length,address,port);
            //second_Socket.send(response_Packet);
            socket.send(response_Packet);
        } catch (SocketException e) {
            System.out.println("No new Socket for Response available");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Was able to initialize a Socket for a Response, but couldn't send it due to IOException");
            e.printStackTrace();
        }
    }
    public void sendJSON(ITransform temp){
        sendString(temp.toJSON());
    }
}
