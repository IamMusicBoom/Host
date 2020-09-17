package com.optima.plugin.host.download;

import java.util.LinkedHashMap;

/**
 * create by wma
 * on 2020/9/16 0016
 */
public class TaskQueue {

    private int runningTaskCount;
    private int mMaxThreadCount = 3;

    public TaskQueue(int mMaxThreadCount) {
        this.mMaxThreadCount = mMaxThreadCount;
    }

    private LinkedHashMap<String, DownloadTask> taskQueue = new LinkedHashMap<>();

    public void addTask(DownloadTask task) {
        taskQueue.put(task.getDownloadUrl(), task);
    }

    public void excuse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (TaskQueue.this) {
                    for (String key : taskQueue.keySet()) {
                        runningTaskCount++;
                        if (runningTaskCount > mMaxThreadCount) {
                            try {
                                TaskQueue.this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        final DownloadTask curTask = taskQueue.get(key);
                        if (curTask != null) {
                            curTask.addOnFinishListener(new TaskFinishListener() {
                                @Override
                                public void finish(DownloadTask task) {
                                    runningTaskCount--;
                                    if (runningTaskCount <= mMaxThreadCount) {
                                        TaskQueue.this.notify();
                                    }
                                    if (runningTaskCount == 0) {
                                        taskQueue.clear();
                                    }
                                }
                            });
                            curTask.excuse();
                        }

                    }
                }
            }
        }).start();

    }

}
