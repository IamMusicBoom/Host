package com.optima.plugin.host.thread;

/**
 * create by wma
 * on 2020/9/17 0017
 * 下载任务的完成监听器
 */
public interface TaskFinishListener {
    void finish(DownloadTask task);
}
