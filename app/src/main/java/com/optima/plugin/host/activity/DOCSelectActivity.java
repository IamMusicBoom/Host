package com.optima.plugin.host.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optima.plugin.host.R;
import com.optima.plugin.host.thread.DownloadTask;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.module.Icon;

import org.xutils.common.util.MD5;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

/**
 * create by wma
 * on 2020/9/21 0021
 */
public class DOCSelectActivity extends BaseActivity implements View.OnClickListener {
    final int REQUEST_CODE_CHOOSE_FILE = 30;
    final String DOC = "application/msword";
    final String XLS = "application/vnd.ms-excel";
    final String PPT = "application/vnd.ms-powerpoint";
    final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    final String XLSX = "application/x-excel";
    final String XLS1 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    final String PDF = "application/pdf";
    final String netUrl1 = "http://xiazai.suanmiao-zuida.com/2008/BaBai.HD1280%E9%AB%98%E6%B8%85%E5%9B%BD%E8%AF%AD%E4%B8%AD%E5%AD%97%E7%89%88.mp4";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_select);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_select_doc) {
//            String columnName = MediaStore.Files.FileColumns.DATA;
//
//            Cursor cursor = this.getContentResolver().query(
////数据源
//                    MediaStore.Files.getContentUri("external"),
////查询ID和名称
//                    null,
////条件为文件类型
//                    MediaStore.Files.FileColumns.MIME_TYPE + " in (?,?) ",
////类型为“video/mp4”
//                    new String[]{"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
////默认排序
//                    null);
//
//
//            if (cursor!=null) {
//                while (cursor.moveToNext()) {
//                    String path = cursor.getString(cursor.getColumnIndex(columnName));
//                    File file = new File(path);
//                    Logger.i(TAG, "doInBackground: file = " + file.getAbsolutePath());
//                }
//            }

            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getAbsolutePath());
            long blockSize;
            long totalBlocks;
            long availableBlocks;
            //获取当前系统版本的等级
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
                availableBlocks = stat.getAvailableBlocksLong();
            }
            else{
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
                availableBlocks = stat.getAvailableBlocksLong();
            }
            Logger.d(TAG, "onClick: totalBlocks = " + totalBlocks + " blockSize = " + blockSize + " availableBlocks = " + availableBlocks);
            Logger.d(TAG, "onClick: totalBlocks = " + formatFileSize(path.getTotalSpace()) + " blockSize = " + formatFileSize(blockSize) + " availableBlocks = " + formatFileSize(path.getUsableSpace()));
        }else if(v.getId() == R.id.btn_dialog_test){

            checkMD5();
        }
    }

    /**
     * 查看MD5值
     */
    private void checkMD5() {
        AssetManager assets = getResources().getAssets();
        try {
            InputStream open = assets.open("json.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(open));
            Gson gson = new Gson();
            List<Icon> icons = gson.fromJson(reader, new TypeToken<List<Icon>>() {
            }.getType());
            Logger.d(TAG, "onCreate: icons.size() = " + icons.size());
            for (int i = 0; i < icons.size(); i++) {
                Icon icon = icons.get(i);
                String s = MD5.md5(icon.getPath());
                Logger.d(TAG, "onClick: name = " + icon.getName() + " s = " + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "onActivityResult: " + data.getData());

    }

    public String formatFileSize(long fileLength) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileLength == 0) {
            return wrongSize;
        }
        if (fileLength < 1024) {
            fileSizeString = df.format((double) fileLength) + "B";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
