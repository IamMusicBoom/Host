package com.optima.plugin.host.thread;

import com.optima.plugin.repluginlib.download.DownloadTask;

/**
 * create by wma
 * on 2020/9/17 0017
 */
public interface TaskFinishListener {
    void finish(ThreadTask task);
}
