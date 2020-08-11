package TCPHandler;

import LogClasses.ITransform;
import LogClasses.Wrapper.Carrier.JSONString;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TCPReceiveThread extends Thread{
    private int numberMessages;
    private Socket socket;
    private BlockingQueue<ITransform> input;
    private long  ConnectionTimeout = 0;
    public BlockingQueue<ITransform> getInput() {
        return input;
    }
    public boolean terminate;

    public TCPReceiveThread setInput(BlockingQueue<ITransform> input) {
        this.input = input;
        return this;
    }
    public TCPReceiveThread terminate(){
        this.terminate=true;
        return this;
    }

    public String fromByteArray(byte[] temp){
        String result = null;
        try {
            result = new String(temp,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void run() {
        numberMessages = 0;
        while(!this.terminate){
            try{
                receive();
            } catch (SocketException e) {
                if (ConnectionReset(e.getMessage())) {
                    System.out.println("Socket wird wegen Connection Reset nach " + (System.currentTimeMillis() - ConnectionTimeout) + " Millisekunden geschlossen");
                    try {
                        socket.close();
                        System.out.println(numberMessages+" Nachrichten wurden empfangen");
                        //System.out.println("ReceiveThread wird beendet");
                        return;
                    } catch (IOException ioException) {
                        System.out.println("TCPReceiveThread kann trotz Connection Reset Socket nicht schließen.");
                        ioException.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){}
        }
        System.out.println(numberMessages+" Nachrichten wurden empfangen");
    }


    public void receive() throws IOException, SocketException , NullPointerException{
        InputStream inStream = socket.getInputStream();
        byte[] Nachrichtenbytes = new byte[80];
        int offset = 0;
        int Laenge = 0;
        String Message = new String();
        //System.out.println("bereit zum empfangen");
        receiveNBytes(Nachrichtenbytes, inStream,0);
        //System.out.println("erste Teilnachricht wird empfangen");
        byte[] Hauptnachricht = null;
        int Hauptlänge = encodeLength(Nachrichtenbytes);
        Message = retrieveFirstPart(Nachrichtenbytes);
        Hauptnachricht = new byte[Hauptlänge];
        offset = 0;
        //System.out.println("Länge = "+Hauptnachricht.length);
        receiveNBytes(Hauptnachricht, inStream,offset);
        //System.out.println("zweite Teilnachricht wird empfangen");
        Message = Message + fromByteArray(Hauptnachricht);
        //System.out.println(Message);
        if(checkEnd(Message)){
            JSONString result = new JSONString().setData(Message);
            try {
                numberMessages++;
                //System.out.println("number Messages"+numberMessages);
                input.put(result);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }else{
            System.out.println("Problem");
        }




    }

    public void receiveNBytes(byte[] Nachrichtenbytes, InputStream inStream, int offset) throws IOException, SocketException {

        while(offset<Nachrichtenbytes.length) {
            int NewOffset;

            NewOffset = inStream.read(Nachrichtenbytes, offset, (Nachrichtenbytes.length-offset));
            if(NewOffset==-1){
                System.out.println("Kein lesen möglich");
            }else {
                //System.out.println("Offset bisher = "+offset);
                offset = offset + NewOffset;
                //System.out.println("Offset neu = "+offset);
            }
            //System.out.println(fromByteArray(Nachrichtenbytes));
        }
    }
    public int encodeLength(byte[] Nachrichtenbytes){
        int Laenge = 0;
        String firstPart = fromByteArray(Nachrichtenbytes);
        int Startindex=0;
        int EndeLaenge=0;
        if(firstPart.contains("Nachrichtenlaenge:")){
            Startindex = firstPart.indexOf("Nachrichtenlaenge:");
            String Laengensuche = firstPart.substring(Startindex);
            EndeLaenge = Laengensuche.indexOf(" ");
            String Laengennummer = firstPart.substring((Startindex+18),Startindex+EndeLaenge);
            try {
                Laenge = Integer.parseInt(Laengennummer);
            }catch (NumberFormatException e){
                System.out.println("Problem beim Parsen der Laenge");
                e.printStackTrace();
            }
        }else{
            System.out.println("Nachrichtenlaenge kann nicht decodiert werden aus "+firstPart);
        }

        if(firstPart.length() >(EndeLaenge+Startindex)){
            int Diff = firstPart.length() - (EndeLaenge+Startindex);
            int Hauptlänge = (Laenge)-(Diff)+1;

            String Message = firstPart.substring((EndeLaenge+Startindex),firstPart.length());
            //System.out.println("Message bis jetzt = "+Message);
            return Hauptlänge;
        }else{
            int Hauptlänge = (Laenge)+1;
            return Hauptlänge;
        }
    }

    public String retrieveFirstPart(byte[] Nachrichtenbytes){
        int Laenge = 0;
        String firstPart = fromByteArray(Nachrichtenbytes);
        int Startindex=0;
        int EndeLaenge=0;
        if(firstPart.contains("Nachrichtenlaenge:")){
            Startindex = firstPart.indexOf("Nachrichtenlaenge:");
            String Laengensuche = firstPart.substring(Startindex);
            EndeLaenge = Laengensuche.indexOf(" ");
        }else{
            System.out.println("Nachrichtenlaenge kann nicht decodiert werden aus "+firstPart);
        }

        if(firstPart.length() >(EndeLaenge+Startindex)){
            int Diff = firstPart.length() - (EndeLaenge+Startindex);
            int Hauptlänge = (Laenge)-(Diff)+1;

            String Message = firstPart.substring((EndeLaenge+Startindex),firstPart.length());
            //System.out.println("Message bis jetzt = "+Message);
            return Message;
        }else{
            int Hauptlänge = (Laenge)+1;
            return null;
        }
    }

    public boolean checkEnd(String Message){
        String Ende = Message.substring(Message.length()-6,Message.length());
        if(Ende.contains("}")){
            return true;
        }else {
            System.out.println("Erfasste Hauptnachricht endet nicht korrekt!");
            return false;
        }
    }
    public TCPReceiveThread(Socket socket){
        this.socket = socket;
        this.input = new LinkedBlockingQueue<ITransform>();
        this.terminate = false;
    }
    public boolean ConnectionReset(String temp){
        boolean Timeout = false;
        if(temp.contains("Connection reset")){
            if(ConnectionTimeout==0){
                ConnectionTimeout=System.currentTimeMillis();
            }else{
                long Differenz = System.currentTimeMillis()-ConnectionTimeout;
                if((10000<Differenz)&&(Differenz<15000)){
                    Timeout = true;
                }else if(Differenz>15000){
                    ConnectionTimeout=System.currentTimeMillis();
                }
            }
        }
        return Timeout;
    }


}
