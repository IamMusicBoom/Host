package com.optima.plugin.host.thread;

import android.os.Bundle;
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
import com.optima.plugin.repluginlib.Logger;

/**
 * create by wma
 * on 2020/9/22 0022
 */
public class DownloadDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DownloadDialog.class.getSimpleName();
    TextView mNegativeBtn, mPositiveBtn, mTitleTv, mMessageTv;
    DialogClickListener listener;
    String mTitle, mMessage;

    private DownloadDialog(String mTitle, String mMessage) {
        this.mTitle = mTitle;
        this.mMessage = mMessage;
    }

    public static DownloadDialog newInstance(String title, String message, Bundle bundle) {
        DownloadDialog dialog = new DownloadDialog(title, message);
        dialog.setArguments(bundle);
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
            if (listener != null) {
                listener.onNegativeClick(v);
            } else {
                dismiss();
            }
        } else if (v == mPositiveBtn) {
            if (listener != null) {
                listener.onPositiveClick(v);
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


    public void setOnDialogClickListener(DialogClickListener listener) {
        this.listener = listener;
    }

    public interface DialogClickListener {
        void onNegativeClick(View view);

        void onPositiveClick(View view);
    }
}
