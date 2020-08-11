package LogHandler;

import LogClasses.ITransform;
import Storer.impl.Storer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueToStorerThread extends Thread{
    public BlockingQueue<ITransform> inputQ;
    public Storer clientStorer;
    public boolean terminate;

    public QueueToStorerThread (BlockingQueue<ITransform> inputQ, Storer client){
        this.inputQ = inputQ;
        this.clientStorer = client;
        this.terminate = false;
    }
    public QueueToStorerThread(){
        this.terminate = false;
    }

    public QueueToStorerThread setInputQ(BlockingQueue<ITransform> inputQ) {
        this.inputQ = inputQ;
        return this;
    }

    public QueueToStorerThread setClientStorer(Storer clientStorer) {
        this.clientStorer = clientStorer;
        return this;
    }
    public QueueToStorerThread terminate(){
        this.terminate = true;
        return this;
    }

    @Override
    public void run() {
        while(!this.terminate) {
            try {
                ITransform transform = inputQ.poll(10, TimeUnit.SECONDS);
                //System.out.println("aus Q genommen");
                if (transform!=null) {
                    clientStorer.store(transform);
                }
            } catch (InterruptedException e) {
            }
        }
        //System.out.println("QueueToStorerThread wird beendet");
    }
}
