package WrapTasks.impl.specific;

import WrapTasks.impl.WrapTask;

public class ProgWrapTask extends WrapTask {/*
    Progress prog;

    public ProgWrapTask(){}
    public ProgWrapTask(int ID, int Group_ID,Progress prog, BlockingQueue<ITransform> returnqueue){
        this.ID = ID;
        this.Group_ID = Group_ID;
        this.prog=prog;
        this.returnqueue = returnqueue;
    }

    @Override
    public void run() {
        LogWrapper result = wrap(prog).setType(WrappMaster.PROGRESS);
        returnqueue.add(result);
    }

    @Override
    public IWrap createNew(int ID, int groupID, ITransform temp, BlockingQueue<ITransform> Q) {
        return new ProgWrapTask(ID,groupID,(Progress)temp,Q);
    }*/
}
