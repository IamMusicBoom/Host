package com.optima.plugin.host.thread;

/**
 * create by wma
 * on 2020/9/17 0017
 */
public interface TestCallback {
    void start();
    void loading(long process,long total);
    void finish();
}
