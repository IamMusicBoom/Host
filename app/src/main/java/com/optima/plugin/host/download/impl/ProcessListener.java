package com.optima.plugin.host.download.impl;

import com.optima.plugin.host.download.DownloadTask;
import com.optima.plugin.host.download.module.DownloadModule;

import java.util.List;

/**
 * create by wma
 * on 2020/9/22 0022
 * 下载队列的回调监听
 */
public interface ProcessListener {
    void onStart();//开始执行任务

    void onTotalProcess(int cur, int total);//下载总进度

    void onSingleProcess(long cur, long total);//单个任务下载进度进度

    void cancel();// 取消下载

    void onFinish(List<DownloadModule> downloadModules);// 完成下载

    void onError(DownloadTask task);


}
