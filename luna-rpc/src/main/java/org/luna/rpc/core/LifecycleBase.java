package org.luna.rpc.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * LifecycleBase
 * Created by luliru on 2019/5/30.
 */
public abstract class LifecycleBase implements Lifecycle {

    protected static final int INIT = 0;

    protected static final int RUNNING = 1;

    protected static final int SHUTDOWN = -1;

    protected AtomicInteger state = new AtomicInteger(INIT);

    @Override
    public void start() {
        if(state.compareAndSet(INIT,RUNNING)){
            doStart();
        }
    }

    protected abstract void doStart();

    @Override
    public void destroy() {
        if(state.compareAndSet(RUNNING,SHUTDOWN)){
            doDestroy();
        }
    }

    protected abstract void doDestroy();

    public boolean isRunning(){
        return state.get() == RUNNING;
    }

    public boolean isDestryed(){
        return state.get() == SHUTDOWN;
    }
}
