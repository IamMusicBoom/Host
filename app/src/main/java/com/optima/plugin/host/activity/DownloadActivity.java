package com.optima.plugin.host.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.optima.plugin.host.R;
import com.optima.plugin.host.view.download.WaveView;
import com.optima.plugin.repluginlib.download.CallbackListener;
import com.optima.plugin.repluginlib.download.DownloadTask;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.utils.NotificationUtils;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;

/**
 * create by wma
 * on 2020/9/1 0001
 */
public class DownloadActivity extends BaseActivity implements View.OnClickListener, CallbackListener {
    ImageView img;
    //    final String netUrl = "http://seopic.699pic.com/photo/50073/8276.jpg_wh1200.jpg";
//    final String netUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598953251041&di=a710c8d0ef81ac28393751ebd4181499&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fe%2F59ae3a99a20fb.jpg%3Fdown";
    final String netUrl1 = "http://xiazai.suanmiao-zuida.com/2008/BaBai.HD1280%E9%AB%98%E6%B8%85%E5%9B%BD%E8%AF%AD%E4%B8%AD%E5%AD%97%E7%89%88.mp4";
    final String pluginName1 = "babai.mp4";

    final String pluginName2 = "nezha.mp4";
    final String netUrl2 = "http://caizi.meizuida.com/1910/NZZMT%E9%99%8D%E4%B8%96.HD1280%E9%AB%98%E6%B8%85%E5%9B%BD%E8%AF%AD%E4%B8%AD%E5%AD%97%E7%89%88.mp4";
    WaveView waveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        img = findViewById(R.id.img);
        waveView = findViewById(R.id.wave_view);
    }
    DownloadTask task1;
    DownloadTask task2;
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start_download) {// 开始下载

            task1 = new DownloadTask(this);
            task1.excuse(pluginName1, netUrl1);

            task2 = new DownloadTask(this);
            task2.excuse(pluginName2, netUrl2);
//            NotificationUtils notificationUtils = new NotificationUtils();
//            NotificationCompat.Builder notificationBuilder = notificationBuilder = notificationUtils.createDownloadBuilder(100, 0);
//            notificationUtils.showNotification(250, notificationBuilder.build());
//            notificationUtils.showNotification(251, notificationBuilder.build());
        } else if (v.getId() == R.id.btn_pause_download) {
//            task.cancelDownLoadTasks();
            task1.cancelDownLoadTasks();
            task2.cancelDownLoadTasks();
        } else if (v.getId() == R.id.btn_show_image) {
            String path = getExternalFilesDir(DownloadTask.FOLDER_NAME).getAbsolutePath() + File.separator + pluginName1;
            Logger.d(TAG, "onClick: path = " + path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            img.setImageBitmap(bitmap);
        } else if (v.getId() == R.id.btn_delete_image) {
            String path = getExternalFilesDir(DownloadTask.FOLDER_NAME).getAbsolutePath() + File.separator + pluginName1;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }


    @Override
    public void onSuccess(File result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {

    }

    @Override
    public void onFinished() {
        waveView.stopAnim();
        waveView.reset();
    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        if (waveView.getMax() == -1) {
            waveView.setMax(total);
        }
        waveView.updateProcess(current);
    }
}
