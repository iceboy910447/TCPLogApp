package WrapTasks.impl.specific;

import WrapTasks.impl.WrapTask;

public class CheckWrapTask extends WrapTask {
    /*
    private Checkpoint check;

    public CheckWrapTask(){}
    public CheckWrapTask(int ID, int Group_ID, Checkpoint check, BlockingQueue<ITransform> returnqueue){
        this.ID = ID;
        this.Group_ID = Group_ID;
        this.check=check;
        this.returnqueue = returnqueue;
    }

    @Override
    public void run() {
        LogWrapper result = wrap(check).setType(WrappMaster.CHECKPOINT);
        returnqueue.add(result);
    }

    @Override
    public CheckWrapTask createNew(int ID, int groupID, ITransform temp, BlockingQueue<ITransform> Q) {
        return new CheckWrapTask(ID,groupID,(Checkpoint)temp,Q);
    }

     */
}
