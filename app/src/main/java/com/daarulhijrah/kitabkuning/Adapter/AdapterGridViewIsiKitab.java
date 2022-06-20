package com.daarulhijrah.kitabkuning.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daarulhijrah.kitabkuning.Model.Kitab;
import com.daarulhijrah.kitabkuning.R;


import java.util.ArrayList;

public class AdapterGridViewIsiKitab extends BaseAdapter {

		private Activity activity;
		private ArrayList<Kitab> kitabArrayList;
		int count;

		public AdapterGridViewIsiKitab(Activity act, ArrayList<Kitab> kitabArrayList) {

			this.kitabArrayList = kitabArrayList;
			this.activity = act;
			this.count = kitabArrayList.size();
		}
		
		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.adapter_recview_listkitab, null);
				holder = new ViewHolder();
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.tvJudulArab = (TextView) convertView.findViewById(R.id.txt_judul_arab);
			holder.tcJudulIndonesia = (TextView) convertView.findViewById(R.id.txt_judul_indonesia);

			final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(activity);
			String FontArab = mSharedPreference.getString("prefFontArab", "noorehira.ttf");
			String FontLatin = mSharedPreference.getString("prefFontLatin", "nefel_adeti.ttf");
			String sizeFontLatin = mSharedPreference.getString("prefFontLatinSize", "18");
			String sizeFontArab = mSharedPreference.getString("prefFontArabSize", "22");

			if(!FontArab.equals(""))
//                tvTeksArab.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "noorehira.ttf"));
				holder.tcJudulIndonesia.setTypeface(Typeface.createFromAsset(activity.getAssets(), FontLatin));

			if(!FontLatin.equals(""))
				holder.tvJudulArab.setTypeface(Typeface.createFromAsset(activity.getAssets(), FontArab));

			holder.tcJudulIndonesia.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontLatin));
			holder.tvJudulArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontArab));

			int idList = kitabArrayList.get(position).getIdTable() + 1;

			holder.tvJudulArab.setText(idList+"-"+kitabArrayList.get(position).getJudulArab());
			holder.tcJudulIndonesia.setText(idList+"-"+kitabArrayList.get(position).getJudulIndonesia());
//
			return convertView;
		}
		
		static class ViewHolder {
			TextView tvJudulArab, tcJudulIndonesia;

		}
		
		
	}