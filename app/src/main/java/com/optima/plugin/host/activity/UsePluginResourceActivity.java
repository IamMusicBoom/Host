package com.optima.plugin.host.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;

import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.Logger;
import com.optima.plugin.repluginlib.base.BaseActivity;
import com.optima.plugin.repluginlib.pluginUtils.P_Constants;
import com.qihoo360.i.IModule;
import com.qihoo360.replugin.RePlugin;

public class UsePluginResourceActivity extends BaseActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_plugin_resourse);
        imageView = findViewById(R.id.img);
    }
}
