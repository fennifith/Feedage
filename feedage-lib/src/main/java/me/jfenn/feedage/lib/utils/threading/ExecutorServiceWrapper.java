package me.jfenn.feedage.lib.utils.threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ExecutorServiceWrapper {

    private ExecutorService service;
    private List<CancelableRunnable> runnables;

    public ExecutorServiceWrapper() {
        runnables = new ArrayList<>();
    }

    public void submit(CancelableRunnable runnable) {
        if (service == null || service.isShutdown()) {
            service = Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable) {
                        @Override
                        public void interrupt() {
                            super.interrupt();
                            for (CancelableRunnable cancelable : runnables)
                                cancelable.cancel();
                        }
                    };
                }
            });
        }

        runnables.add(runnable);
        service.submit(runnable);
    }

    public void end() {
        if (service != null && !service.isShutdown())
            service.shutdown();
    }

    public void cancel() {
        if (service != null && !service.isTerminated())
            service.shutdownNow();

        runnables.clear();
    }

}
