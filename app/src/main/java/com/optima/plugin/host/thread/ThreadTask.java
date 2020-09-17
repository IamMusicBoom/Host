package com.optima.plugin.host.thread;

import com.optima.plugin.repluginlib.Logger;

/**
 * create by wma
 * on 2020/9/17 0017
 */
public class ThreadTask {
    final String TAG = ThreadTask.class.getSimpleName();

    TaskFinishListener finishListener;

    public void addFinishListener(TaskFinishListener finishListener) {
        this.finishListener = finishListener;
    }

    public ThreadTask(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 执行线程
     */
    public void start() {
        TestTask task = new TestTask(new TestCallback() {
            @Override
            public void start() {
                Logger.d(TAG, "start: name = " + name);
            }

            @Override
            public void loading(int process, int total) {
//                Logger.d(TAG, "loading: name = " + name + "  : " + process + "/" + total);
            }

            @Override
            public void finish() {
                Logger.d(TAG, "finish: name = " + name);
                if (finishListener != null) {
                    finishListener.finish(ThreadTask.this);
                }
            }
        });
        task.start();
    }
}