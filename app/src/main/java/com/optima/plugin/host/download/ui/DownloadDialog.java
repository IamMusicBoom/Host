package com.optima.plugin.host.download.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.optima.plugin.host.R;
import com.optima.plugin.host.download.ConfigSP;
import com.optima.plugin.host.download.DownloadReceiver;
import com.optima.plugin.host.download.DownloadService;
import com.optima.plugin.host.download.DownloadTask;
import com.optima.plugin.host.download.HandleDownloadFileTask;
import com.optima.plugin.host.download.impl.ProcessListener;
import com.optima.plugin.host.download.module.DownloadModule;
import com.optima.plugin.repluginlib.Logger;

import java.util.List;

/**
 * create by wma
 * on 2020/9/22 0022
 * <p>
 * 下载插件，下载宿主 提示框
 */
public class DownloadDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DownloadDialog.class.getSimpleName();
    private static final int DOWNLOAD_HOST = 0;
    private static final int DOWNLOAD_PLUGIN = 1;
    private TextView mNegativeBtn, mPositiveBtn, mTitleTv, mMessageTv;
    private ProgressView mProgressView;
    // ---------------------------------------------------------- 设置对话框的点击回调  start
    //    private DialogClickListener mListener;
// ---------------------------------------------------------- 设置对话框的点击回调  end
    private String mTitle, mMessage;
    private int mCurType;
    private DownloadService.DownloadBinder binder;
    private DownloadService mDownloadService;

    private DownloadDialog(String mTitle, String mMessage, int type) {
        this.mTitle = mTitle;
        this.mMessage = mMessage;
        this.mCurType = type;
    }

    public static DownloadDialog createDownloadPlugin() {
        DownloadDialog dialog = new DownloadDialog("请等待", "初始化资源中...", DOWNLOAD_PLUGIN);
        dialog.setCancelable(false);
        return dialog;
    }

    public static DownloadDialog createDownloadHostDialog() {
        DownloadDialog dialog = new DownloadDialog("发现新版本", "是否要更新到新版本？", DOWNLOAD_HOST);
        dialog.setCancelable(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_download, container, false);
        mNegativeBtn = view.findViewById(R.id.dialog_negative);
        mPositiveBtn = view.findViewById(R.id.dialog_positive);
        mTitleTv = view.findViewById(R.id.dialog_title);
        mMessageTv = view.findViewById(R.id.dialog_message);
            mProgressView = view.findViewById(R.id.dialog_process);
        switch (mCurType) {
            case DOWNLOAD_HOST:
                break;
            case DOWNLOAD_PLUGIN:
                mPositiveBtn.setVisibility(View.GONE);
                break;
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNegativeBtn.setOnClickListener(this);
        mPositiveBtn.setOnClickListener(this);
        mMessageTv.setText(mMessage);
        mTitleTv.setText(mTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        getDialog().getWindow().setAttributes(params);
    }


    @Override
    public void onClick(View v) {
        if (v == mNegativeBtn) {
// ---------------------------------------------------------- 设置对话框的点击回调  start
//            if (mListener != null) {
//                mListener.onNegativeClick(v);
//            } else {
//                dismiss();
//                mDownloadService.cancelTask();
//            }
// ---------------------------------------------------------- 设置对话框的点击回调  end
            if(mCurType == DOWNLOAD_HOST){
                DownloadReceiver.sendMessage(DownloadReceiver.CANCEL_DOWNLOAD_HOST);// 取消下载宿主
            }else if(mCurType == DOWNLOAD_PLUGIN){
                DownloadReceiver.sendMessage(DownloadReceiver.CANCEL_DOWNLOAD_PLUGIN);// 取消下载插件
            }
            dismiss();
            if (mDownloadService != null) {
                mDownloadService.cancelTask();
            }
        } else if (v == mPositiveBtn) {
// ---------------------------------------------------------- 设置对话框的点击回调  start
//            if (mListener != null) {
//                mListener.onPositiveClick(v);
//            }
// ---------------------------------------------------------- 设置对话框的点击回调  end
            if (mCurType == DOWNLOAD_HOST) {
                setMessage("请稍后...");
                startDownload(getActivity());
            }

        }
    }

    public void setTitle(String title) {
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        } else {
            Logger.d(TAG, "setTitle: mTitleTv = null");
        }
    }

    public void setMessage(String msg) {
        if (mMessageTv != null) {
            mMessageTv.setText(msg);
        } else {
            Logger.d(TAG, "setMessage: mMessageTv = null");
        }
    }


    /**
     * 开启服务还是下载
     *
     * @param activity
     */
    public void startDownload(Activity activity) {
        Intent intent = new Intent(activity, DownloadService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(intent);
        } else {
            activity.startService(intent);
        }
        activity.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    DownloadServiceConnection conn = new DownloadServiceConnection();

    class DownloadServiceConnection implements ServiceConnection, ProcessListener {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            binder = (DownloadService.DownloadBinder) service;

            mDownloadService = binder.getService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onStart() {
            Logger.d(TAG, "onStart: ");
            setMessage("资源下载中：" + "0/" + mDownloadService.downloadQueue.taskCount());
        }

        @Override
        public void onFinish(List<DownloadModule> downloadModules) {
            Logger.d(TAG, "onFinish: ");
            setMessage("资源解析中，请稍后...");
            new HandleDownloadFileTask(downloadModules, new HandleDownloadFileTask.HandleFinishListener() {
                @Override
                public void handleError(String error) {
                    Logger.d(TAG, "handleError: error = " + error);
                }

                @Override
                public void handleFinish() {
                    ConfigSP.putNeedDownloadList("");
                    if (mCurType == DOWNLOAD_HOST) {
                        DownloadReceiver.sendMessage(DownloadReceiver.HANDLE_DOWNLOAD_HOST_FINISH);
                        dismiss();
                    } else if (mCurType == DOWNLOAD_PLUGIN) {
                        DownloadReceiver.sendMessage(DownloadReceiver.HANDLE_DOWNLOAD_PLUGIN_FINISH);
                        dismiss();
                    } else {
                        Logger.e(TAG, "handleFinish: 下载完成，位置类型未做处理");
                    }
                }
            }).start();

        }

        @Override
        public void onError(DownloadTask task) {
            setMessage("资源下载失败");
        }

        @Override
        public void onTotalProcess(int cur, int total) {
            StringBuffer sb = new StringBuffer();
            sb.append("资源下载中：");
            sb.append(cur);
            sb.append("/");
            sb.append(total);
            setMessage(sb.toString());
            mProgressView.setVisibility(View.VISIBLE);
            mProgressView.setMax(total);
            mProgressView.setProcess(cur);
        }

        @Override
        public void onSingleProcess(long cur, long total) {
            if (mCurType == DOWNLOAD_HOST) {
                StringBuffer sb = new StringBuffer();
                sb.append("资源下载中：");
                sb.append((cur*100/total));
                sb.append("/");
                sb.append(100);
                setMessage(sb.toString());
                mProgressView.setVisibility(View.VISIBLE);
                mProgressView.setMax(total);
                mProgressView.setProcess(cur);
            }else if(mCurType == DOWNLOAD_PLUGIN){

            }
        }

        @Override
        public void cancel() {

        }
    }


    @Override
    public void onDestroy() {
        Logger.d(TAG, "onDestroy: ");
        try {
            if(mDownloadService!=null){
                mDownloadService.stopSelf();
                getActivity().unbindService(conn);
            }
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d(TAG, "catch onDestroy: 服务还没有绑定");
        }
        super.onDestroy();
    }

// ---------------------------------------------------------- 设置对话框的点击回调  start
//    public void setOnDialogClickListener(DialogClickListener listener) {
//        this.mListener = listener;
//    }

//    public interface DialogClickListener {
//        void onNegativeClick(View view);
//
//        void onPositiveClick(View view);
//    }
// ---------------------------------------------------------- 设置对话框的点击回调  end

}
