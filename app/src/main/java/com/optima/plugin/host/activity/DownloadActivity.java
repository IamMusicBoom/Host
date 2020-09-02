package com.optima.plugin.host.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.DownloadTask;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;

import java.io.File;

/**
 * create by wma
 * on 2020/9/1 0001
 */
public class DownloadActivity extends BaseActivity implements View.OnClickListener {
    ImageView img;
    //    final String netUrl = "http://seopic.699pic.com/photo/50073/8276.jpg_wh1200.jpg";
    final String pluginName = "tt.jpg";
    final String netUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598953251041&di=a710c8d0ef81ac28393751ebd4181499&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fe%2F59ae3a99a20fb.jpg%3Fdown";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        img = findViewById(R.id.img);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start_download) {// 开始下载
            DownloadTask task = new DownloadTask(getApplicationContext(), 100);
            task.excuse(pluginName, netUrl);
        } else if (v.getId() == R.id.btn_pause_download) {

        } else if (v.getId() == R.id.btn_show_image) {
            String path = getExternalFilesDir(DownloadTask.FOLDER_NAME).getAbsolutePath() + File.separator + pluginName;
            Logger.d(TAG, "onClick: path = " + path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            img.setImageBitmap(bitmap);
        } else if (v.getId() == R.id.btn_delete_image) {
            String path = getExternalFilesDir(DownloadTask.FOLDER_NAME).getAbsolutePath() + File.separator + pluginName;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }


}
