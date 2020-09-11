package com.optima.plugin.host.binder;

import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.optima.plugin.host.IViewAidlInterface;
import com.optima.plugin.host.view.download.WaveView;
import com.optima.plugin.repluginlib.Logger;

/**
 * create by wma
 * on 2020/9/7 0007
 */
public class ViewBinder extends IViewAidlInterface.Stub {
    String TAG = "host_ViewBinder";
    private View view;

    public ViewBinder(View view) {
        this.view = view;
    }

    @Override
    public void onClick(int resId) throws RemoteException {

    }

    @Override
    public void onLongClick(int resId) throws RemoteException {

    }

    @Override
    public void setText(int resId, String text) throws RemoteException {
        Logger.d(TAG, "setText: resId = " + resId +  " text = " + text);
        TextView viewById = view.findViewById(resId);
        viewById.setText(text);
    }

    @Override
    public void startAnim(int resId) throws RemoteException {
        WaveView waveView =  view.findViewById(resId);
        waveView.startAnim();
    }


    @Override
    public void setImgId(int resId,int imgId) throws RemoteException {
        ImageView viewById = view.findViewById(resId);
        viewById.setImageResource(imgId);
    }
}
