package com.daarulhijrah.kitabkuning.Adapter;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

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

		@SuppressLint("ResourceAsColor")
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

			holder.cardView = (CardView) convertView.findViewById(R.id.card_view_list_kitab);
			holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_layout_list_kitab);
			holder.tvJudulArab = (TextView) convertView.findViewById(R.id.txt_judul_arab);
			holder.tcJudulIndonesia = (TextView) convertView.findViewById(R.id.txt_judul_indonesia);

			final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(activity);
			String FontArab = mSharedPreference.getString("prefFontArab", "arab_kemenag.ttf");
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
//			holder.tcJudulIndonesia.setTextColor(R.color.blue);
			//				holder.cardView.setCardBackgroundColor(R.color.green);
//			if(kitabArrayList.get(position).getJudulIndonesia().contains("Bab ")) {
//				holder.linearLayout.setBackgroundColor(R.color.darkGreen);
//				holder.tvJudulArab.setTextColor(R.color.white);
//				holder.tcJudulIndonesia.setTextColor(R.color.blue);
//			}



			return convertView;
		}
		
		static class ViewHolder {
			TextView tvJudulArab, tcJudulIndonesia;
			CardView cardView;
			LinearLayout linearLayout;

		}
		
		
	}