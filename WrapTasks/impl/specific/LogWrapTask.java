package WrapTasks.impl.specific;

import WrapTasks.impl.WrapTask;

public class LogWrapTask extends WrapTask {
    /*
    private Log log;

    public LogWrapTask(){}
    public LogWrapTask(int ID, int Group_ID, Log log, BlockingQueue<ITransform> returnqueue){
        this.ID = ID;
        this.Group_ID = Group_ID;
        this.log = log;
        this.returnqueue = returnqueue;
    }

    @Override
    public void run() {
        LogWrapper result = wrap(log).setType(WrappMaster.LOG);
        returnqueue.add(result);
    }

    @Override
    public IWrap createNew(int ID, int groupID, ITransform temp, BlockingQueue<ITransform> Q) {
        return new LogWrapTask(ID, groupID,(Log) temp, Q);
    }*/
}
