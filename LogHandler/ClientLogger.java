package LogHandler;

import LogClasses.*;
import LogClasses.Wrapper.Threads.TCPWrapHandler;
import Storer.impl.Storage;
import Storer.impl.Storer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientLogger extends Thread{
    public TCPWrapHandler WrapHandler;
    public BlockingQueue<ITransform> Logs;
    public BlockingQueue<ITransform> Progresses;
    public BlockingQueue<ITransform> Checkpoints;
    public BlockingQueue<ITransform> Exceptions;
    public Socket socket;
    public Storage clientstorage;

    public ClientLogger (Socket socket,Storage storage, TCPWrapHandler handler){
        this.socket = socket;
        this.Logs = new LinkedBlockingQueue<>();
        this.Progresses = new LinkedBlockingQueue<>();
        this.Checkpoints = new LinkedBlockingQueue<>();
        this.Exceptions = new LinkedBlockingQueue<>();
        this.clientstorage = storage;
        this.WrapHandler = handler
                .addUnWrapQ(this.Logs,new Log())
                .addUnWrapQ(this.Progresses, new Progress())
                .addUnWrapQ(this.Checkpoints, new Checkpoint())
                .addUnWrapQ(this.Exceptions, new ExceptionLog());
    }

    @Override
    public void run() {
        this.startThreads();
    }
    public void send(ITransform transform){
        WrapHandler.getOutputQ().add(transform);
    }

    public ClientLogger startThreads(){
        QueueToStorerThread[] QSThreads = new QueueToStorerThread[4];
        Storer clientstorer = clientstorage.getCurrentStorer();
        for (int i = 0; i < 4; i++) {
            QSThreads[i] = new QueueToStorerThread().setClientStorer(clientstorer);
        }
        QSThreads[0].setInputQ(this.Logs);
        QSThreads[1].setInputQ(this.Checkpoints);
        QSThreads[2].setInputQ(this.Progresses);
        QSThreads[3].setInputQ(this.Exceptions);
        for (int i = 0; i<QSThreads.length; i++){
            QSThreads[i].start();
        }
        boolean socketclosed = false;
        while (!socketclosed){
            //System.out.println("Socket wird überprüft");
            if(socket.isClosed()){
                //System.out.println("Socket shutdown erkannt!");
                WrapHandler.terminate();
                socketclosed=true;
                for (int i = 0; i < 4; i++) {
                    QSThreads[i].terminate();
                }
            }else{
                //System.out.println("Socket ist offen");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.println("alleQS Threads wurden gestartet");
        for (int i = 0; i<QSThreads.length; i++){
            try {
                QSThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return this;
    }


}
