package com.optima.plugin.host.thread;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.util.List;

/**
 * create by wma
 * on 2020/9/22 0022
 */
public class HandleDownloadFileTask extends Thread {
    List<FileModule> fileModules;
    HandleFinishListener listener;

    public HandleDownloadFileTask(List<FileModule> fileModules, HandleFinishListener listener) {
        this.fileModules = fileModules;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        if (fileModules == null || fileModules.size() <= 0) {
            if (listener != null) {
                listener.handleError("未发现下载的文件");
            }
            return;
        }

        if(fileModules.size() == 1){

        }else{
            for (int i = 0; i < fileModules.size(); i++) {
                FileModule fileModule = fileModules.get(i);
                if(fileModule.getFileType().equals(DownloadTask.PLUGIN_FOLDER)){
                    String filePath = fileModule.getFilePath();
                    PluginInfo pluginInfo = RePlugin.install(filePath);
                    RePlugin.fetchComponentList(pluginInfo.getName());
                }
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (listener != null) {
            listener.handleFinish();
        }
    }


    public interface HandleFinishListener {
        void handleError(String error);

        void handleFinish();
    }
}
