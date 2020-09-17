package com.optima.plugin.host.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.optima.plugin.host.R;
import com.optima.plugin.repluginlib.module.Icon;

import java.util.List;

/**
 * create by wma
 * on 2020/9/17 0017
 */
public class DownloadImgAdapter extends RecyclerView.Adapter<DownloadImgAdapter.DownloadImgHolder> {
    Context context;
    List<Icon> icons;

    public DownloadImgAdapter(List<Icon> icons,Context context) {
        this.icons = icons;
        this.context = context;
    }

    @NonNull
    @Override
    public DownloadImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_icon, parent, false);
        return new DownloadImgHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadImgHolder holder, int position) {
        Icon icon = icons.get(position);
        Glide.with(context).load(icon.getPath()).into(holder.img);
        holder.tv.setText(icon.getName());
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    class DownloadImgHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv;
        public DownloadImgHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_download_img);
            tv = itemView.findViewById(R.id.item_download_title);
        }
    }
}
