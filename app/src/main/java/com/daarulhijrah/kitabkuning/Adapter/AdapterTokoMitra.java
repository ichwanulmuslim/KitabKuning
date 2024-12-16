package com.daarulhijrah.kitabkuning.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daarulhijrah.kitabkuning.Model.TokoMitra;
import com.daarulhijrah.kitabkuning.R;

import java.util.ArrayList;

/**
 * Created by Mac on 10/24/16.
 */

public class AdapterTokoMitra extends RecyclerView.Adapter<AdapterTokoMitra.ViewHolder> {
    Activity mContext;
    int resourceId;
    ArrayList<TokoMitra> data = new ArrayList<TokoMitra>();

    public AdapterTokoMitra(Activity context, int layoutResourceId, ArrayList<TokoMitra> data) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.resourceId = layoutResourceId;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToko;
        TextView txtNamaToko, txtAsalToko;
        LinearLayout linearLayout1;

        public ViewHolder(View view) {
            super(view);
            imgToko = (ImageView) itemView.findViewById(R.id.img_toko);
            txtNamaToko = (TextView) itemView.findViewById(R.id.tv_nama_toko);
            txtAsalToko = (TextView) itemView.findViewById(R.id.tv_asal_toko);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txtNamaToko.setText(data.get(position).getNamaToko());
        holder.txtAsalToko.setText(data.get(position).getAsalToko());
        Glide.with(mContext).load(data.get(position).getImgToko()).into(holder.imgToko);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
