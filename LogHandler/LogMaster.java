package LogHandler;

import LogClasses.ITransform;
import LogClasses.Wrapper.Carrier.Message;
import LogClasses.Wrapper.Threads.TCPWrapHandler;
import PCinfo.Systeminformation;
import Storer.impl.Storage;
import Storer.impl.StoreManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogMaster {
    public ServerSocket serverSocket;
    public BlockingQueue<ITransform> MessageQ = new LinkedBlockingQueue<>();
    public BlockingQueue<ITransform> IDQ = new LinkedBlockingQueue<>();

    //
    public LogMaster start()
    {
        try {
            serverSocket = new ServerSocket(10006);
            while(true) {
                //System.out.println("Neue Anfrage kann akzeptiert werden");
                Socket client = serverSocket.accept();
                TCPWrapHandler Handler = new TCPWrapHandler(client)
                        .addUnWrapQ(MessageQ,new Message())
                        .addUnWrapQ(IDQ,new Identifier())
                        .setID(0);
                BlockingQueue<ITransform> LogWrapQ = Handler.getUnWrapLogWrapperQ();
                ITransform data = null;
                try {
                     data = MessageQ.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String gesucht = new Message().retrieveClassName();
                if(data.retrieveClassName().equals(gesucht)){
                    //System.out.println("gesucht gefunden");
                    LogWrapQ.add(data);
                    Identifier currentID = null;
                    try {
                        currentID = (Identifier) IDQ.take();
                    } catch ( InterruptedException e) {
                        e.printStackTrace();
                    }
                    Systeminformation currentSys = currentID.getCurrentsys();
                    String Processname = currentID.getProcessname();
                    //System.out.println("currentSys = " +currentSys.toString()+ "Processname = "+Processname);
                    Storage myStorage = StoreManager.lookForStorage(Processname,currentSys);
                    //System.out.println("passt alles");
                    //System.out.println("Prozess = "+Processname +" erhält ID "+myStorage.getId());
                    currentID.setId(myStorage.getId())
                             .setGroupid(myStorage.getGroupid())
                             .setRunnumber(myStorage.getLastrunnumber());
                    Message MyMessage = new Message().setObject(currentID)
                            .setMessagetype("SET")
                            .setData("ID");
                    ClientLogger clienthandler = new ClientLogger(client, myStorage, Handler);
                    clienthandler.start();
                    clienthandler.send(MyMessage);
                    //System.out.println("Bereit neuen Thread aufzunehmen");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

}
