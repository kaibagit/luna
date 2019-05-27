package org.luna.rpc.core;

import io.netty.util.internal.ConcurrentSet;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JVM关闭以后的清理程序
 */
public class ShutdownCleaner  {

    private static Set<Lifecycle> survivors = new ConcurrentSet<>();

    private static AtomicBoolean haveRegisteredJvmHook = new AtomicBoolean(false);

    public static void register(Lifecycle lifecycle){
        survivors.add(lifecycle);
    }

    public static void unregister(Lifecycle lifecycle){
        survivors.remove(lifecycle);
    }

    private static void registerJvmHook(){
        if(haveRegisteredJvmHook.compareAndSet(false,true)){
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                survivors.stream().forEach(lifecycle -> lifecycle.destroy());
            }, "LunaShutdownHook"));
        }
    }

}
