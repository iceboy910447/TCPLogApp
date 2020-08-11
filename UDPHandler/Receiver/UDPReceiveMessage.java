package UDPHandler.Receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPReceiveMessage implements Runnable{
    public DatagramSocket socket;
    public DatagramPacket packet;
    public InetAddress address;
    public int port;
    public int len;
    public byte[] data;

    public UDPReceiveMessage(DatagramSocket current_socket, DatagramPacket packet){
        this.socket = current_socket;
        this.packet = packet;
    }
    public void setSocket(DatagramSocket socket){
        this.socket=socket;
    }

    public void run(){
        this.unpack();
        String text = new String(data, 0, len);

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


}
