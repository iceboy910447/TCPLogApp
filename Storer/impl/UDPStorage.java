package Storer.impl;

import UDPHandler.Receiver.UDPReceiveMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPStorage extends UDPReceiveMessage {

    private IStorage Destination;
    public UDPStorage(DatagramSocket socket, DatagramPacket packet) {
        super(socket, packet);
    }

    public void setDestination(IStorage destination) {
        Destination = destination;
    }


    @Override
    public void run() {
        this.unpack();
        String text = new String(this.data, 0, this.len);


    }
    public void decisionResponse(String txt){
        String start = txt.substring(0,6);
        if(start.contains("GET")){
            String frontpart = txt.substring(0,20);
            if(frontpart.contains("new ID")){
                sendNewID();
            }
        }else if (start.contains("STORE")){
            String frontpart;

        }
    }

    public void sendNewID(){
       // String result = "ID: "+Destination.getUnused_ID();
       // sendString(//result);
    }


}
