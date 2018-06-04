package me.jfenn.feedage.lib.utils.threading;

public interface CancelableRunnable extends Runnable {
    void cancel();
}
