package Storer.impl;

import UDPHandler.Receiver.UDPBase;

import java.io.IOException;
import java.net.DatagramPacket;

public class UDPStoreCoordinater extends UDPBase {
    public IStorage Destination;
    public UDPStoreCoordinater(int currentPortnumber, int maxThreadnumber) {
        super(currentPortnumber, maxThreadnumber);
    }
    public void setStorage(IStorage temp){
        this.Destination=temp;
    }

    @Override
    public void run() {
        while(!terminate){
            DatagramPacket packet = new DatagramPacket(new byte[4096], 4096); //Original Value 1024
            try {
                socket.receive(packet);
            }catch (IOException e){
                e.printStackTrace();
            }
            UDPStorage temp = new UDPStorage(socket,packet);
            packettasks.add(temp);
        }
    }
}
