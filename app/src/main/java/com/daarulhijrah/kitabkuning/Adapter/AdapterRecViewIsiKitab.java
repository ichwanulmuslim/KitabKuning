package com.daarulhijrah.kitabkuning.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daarulhijrah.kitabkuning.Model.Kitab;
import com.daarulhijrah.kitabkuning.R;

import java.util.ArrayList;

public class AdapterRecViewIsiKitab extends RecyclerView.Adapter<AdapterRecViewIsiKitab.ViewHolder> {

    Context context;
    int resourceId;
    ArrayList<Kitab> dataIsiKitab = new ArrayList<>();


    public AdapterRecViewIsiKitab(Context context, int adapter_recview_listkitab, ArrayList<Kitab> dataKitab) {
        this.context = context;
        this.resourceId = adapter_recview_listkitab;
        this.dataIsiKitab = dataKitab;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resourceId, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        Log.e("AdapterRecView",dataIsiKitab.size()+"");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tvJudulArab.setText(dataIsiKitab.get(i).getJudulArab());
        viewHolder.tvJudulIndonesia.setText(dataIsiKitab.get(i).getJudulIndonesia());

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        String FontArab = mSharedPreference.getString("prefFontArab", "noorehira.ttf");
        String FontLatin = mSharedPreference.getString("prefFontLatin", "");
        String sizeFontLatin = mSharedPreference.getString("prefFontLatinSize", "14");
        String sizeFontArab = mSharedPreference.getString("prefFontArabSize", "18");

        if(!FontArab.equals(""))
//                tvTeksArab.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "noorehira.ttf"));
            viewHolder.tvJudulArab.setTypeface(Typeface.createFromAsset(context.getAssets(), FontArab));

        if(!FontLatin.equals(""))
            viewHolder.tvJudulIndonesia.setTypeface(Typeface.createFromAsset(context.getAssets(), FontLatin));

        viewHolder.tvJudulArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontArab));
        viewHolder.tvJudulIndonesia.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontLatin));
    }

    @Override
    public int getItemCount() {
        return dataIsiKitab.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvJudulIndonesia, tvJudulArab;

        public ViewHolder (View view){
            super(view);
            tvJudulIndonesia = (TextView) itemView.findViewById(R.id.txt_judul_indonesia);
            tvJudulArab = (TextView) itemView.findViewById(R.id.txt_judul_arab);
        }
    }

}
