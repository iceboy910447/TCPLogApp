package UDPHandler.Receiver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPSendMessage extends UDPMessage implements Runnable{
    public UDPSendMessage(DatagramSocket socket, DatagramPacket packet) {
        super(socket, packet);
    }

    public UDPSendMessage(DatagramSocket socket) {
        super(socket);
    }

    @Override
    public void run() {

    }
}
