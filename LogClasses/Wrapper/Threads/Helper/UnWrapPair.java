package LogClasses.Wrapper.Threads.Helper;

import LogClasses.ITransform;

import java.util.concurrent.BlockingQueue;

public class UnWrapPair {
    private ITransform transformableObject;
    private BlockingQueue<ITransform> queue;

    public ITransform getTransformableObject() {
        return transformableObject;
    }

    public UnWrapPair setTransformableObject(ITransform transformableObject) {
        this.transformableObject = transformableObject;
        return this;
    }

    public BlockingQueue<ITransform> getQueue() {
        return queue;
    }

    public UnWrapPair setQueue(BlockingQueue<ITransform> queue) {
        this.queue = queue;
        return this;
    }
}
