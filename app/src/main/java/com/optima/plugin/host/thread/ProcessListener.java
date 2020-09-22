package com.optima.plugin.host.thread;

/**
 * create by wma
 * on 2020/9/22 0022
 * 下载队列的回调监听
 */
public interface ProcessListener {
    void onStart();//开始执行任务

    void onProcess(int cur, int total);//下载进度

    void cancel();// 取消下载

    void onFinish();// 完成下载
}
