package com.optima.plugin.host.thread;

import com.optima.plugin.repluginlib.Logger;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * create by wma
 * on 2020/9/17 0017
 */
public class ThreadQueue {
    final String TAG = ThreadQueue.class.getSimpleName();
    private int runningCount;
    private int mMaxThreadCount = 3;
    private LinkedHashMap<String, ThreadTask> tasks = new LinkedHashMap<>();

    public ThreadQueue(int maxThreadCount) {
        this.mMaxThreadCount = maxThreadCount;
    }

    public void addTask(ThreadTask task) {
        tasks.put(task.getName(), task);
    }

    public int taskCount() {
        return tasks.size();
    }

    public void excuse() {
        if(taskCount()<=0){
            Logger.e(TAG, "excuse: 执行失败，没有任务");
            return;
        }
        new WorkThread().start();
    }

    class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (ThreadQueue.this) {
                for (String name : tasks.keySet()) {
                    Logger.d(TAG, "run: runningCount = " + runningCount);
                    runningCount++;
                    if (runningCount > mMaxThreadCount) {
                        try {
                            ThreadQueue.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ThreadTask task = tasks.get(name);
                    if (task != null) {
                        task.addFinishListener(new TaskFinishListener() {
                            @Override
                            public void finish(ThreadTask task) {
                                synchronized (ThreadQueue.this) {
                                    runningCount--;
                                    String name1 = task.getName();
                                    task = null;
                                    tasks.put(name1, task);
                                    if (runningCount < mMaxThreadCount) {
                                        ThreadQueue.this.notify();
                                    }
                                    if (runningCount == 0) {
                                        tasks.clear();
                                    }
                                    Logger.d(TAG, "finish: runningCount = " + runningCount + " tasks = " + tasks.size() + "--------------------------------------------");
                                }
                            }
                        });
                        task.start();
                    } else {

                    }
                }
            }
        }
    }
}
