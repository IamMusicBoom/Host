package com.optima.plugin.host.thread;

/**
 * create by wma
 * on 2020/9/22 0022
 */
public interface ProcessListener {
    void onStart();
    void onFinish();
    void onProcess(int cur,int total);
}
