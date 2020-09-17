package com.optima.plugin.host.thread;

/**
 * create by wma
 * on 2020/9/17 0017
 */
public class TestTask extends Thread {
    private int count;
    private TestCallback callback;
    private int total =30;

    public TestTask(TestCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        super.run();

        try {
            download();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void download() throws InterruptedException {
        callback.start();
        while (count < total) {
            count++;
            callback.loading(count, total);
            try {
                Thread.sleep((long) (Math.random()*200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count == total) {
                callback.finish();
            }
        }
    }
}
