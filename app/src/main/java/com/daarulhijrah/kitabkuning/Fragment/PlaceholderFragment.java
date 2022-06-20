package com.daarulhijrah.kitabkuning.Fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daarulhijrah.kitabkuning.Model.Kitab;
import com.daarulhijrah.kitabkuning.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import static com.daarulhijrah.kitabkuning.Activity.ActivityRecViewList.dataSource;

public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageView imgGambar;
    public TextView tvJudulArab, tvJudulIndonesia;
    public WebView wvIsiArab, wvIsiIndonesia;

    public ArrayList <Kitab> arrayListKitab = new ArrayList<>();

    public TextView tvAlert;
    public ProgressBar progressBar;

    private static String searchedText = "";

    int idKitab;
    String  judulArab, judulIndonesia, isiArab, isiIndonesia, urlGambar, urlAudio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_kitab, container, false);

        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imgGambar = (ImageView) rootView.findViewById(R.id.imgPreview);

        tvJudulArab = (TextView) rootView.findViewById(R.id.tv_frag_detail_kitab_judul_arab);
        tvJudulIndonesia = (TextView) rootView.findViewById(R.id.tv_frag_detail_kitab_judul_indonesia);

        //txtDescription = (WebView) findViewById(R.id.txtDescription);
        wvIsiArab = (WebView) rootView.findViewById(R.id.wv_fragment_detail_kitab_isi_arab);
        wvIsiIndonesia = (WebView) rootView.findViewById(R.id.wv_fragment_detail_kitab_isi_indonesia);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.main_content);

        NestedScrollView scroller = (NestedScrollView) rootView.findViewById(R.id.sclDetail);

        if (scroller != null) {

            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("Halo", "Scroll DOWN");

                    }
                    if (scrollY < oldScrollY) {
                        Log.i("Halo", "Scroll UP");

                    }

                    if (scrollY == 0) {
                        Log.i("Halo", "TOP SCROLL");
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        Log.i("Halo", "BOTTOM SCROLL");
                    }
                }
            });
        }



        progressBar = (ProgressBar) rootView.findViewById(R.id.prgLoading);
        tvAlert = (TextView) rootView.findViewById(R.id.txtAlert);

        progressBar.setVisibility(View.GONE);

        dataSource.open();
        arrayListKitab = dataSource.getItemKitab(getArguments().getInt(ARG_SECTION_NUMBER));
        Log.e("Array ME",arrayListKitab.size()+"");
        dataSource.close();



        for(int i=0 ; i<arrayListKitab.size() ; i++) {
            coordinatorLayout.setVisibility(View.VISIBLE);

            idKitab = arrayListKitab.get(i).getIdTable();
            judulArab = arrayListKitab.get(i).getJudulArab();
            judulIndonesia = arrayListKitab.get(i).getJudulIndonesia();
            isiArab = arrayListKitab.get(i).getIsiArab();
            isiIndonesia = arrayListKitab.get(i).getIsiIndonesia();
            urlGambar = arrayListKitab.get(i).getUrlGambar();
            urlAudio = arrayListKitab.get(i).getUrlAudio();


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            StringBuilder builder = new StringBuilder();

            final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
            String FontArab = mSharedPreference.getString("prefFontArab", "noorehira.ttf");
            String FontLatin = mSharedPreference.getString("prefFontLatin", "nefel_adeti.ttf");
            String sizeFontLatin = mSharedPreference.getString("prefFontLatinSize", "20");
            String sizeFontArab = mSharedPreference.getString("prefFontArabSize", "18");

            Log.e("String","Arab : "+FontArab+", Latin : "+FontLatin+", Size Latin: "+sizeFontLatin+", Size Arab: "+sizeFontArab);

            if(!FontArab.equals(""))
//                tvTeksArab.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "noorehira.ttf"));
                tvJudulArab.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), FontArab));

            if(!FontLatin.equals(""))
                tvJudulIndonesia.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), FontLatin));

            tvJudulArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontArab));
            tvJudulIndonesia.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontLatin));


            tvJudulArab.setText(judulArab);
            tvJudulIndonesia.setText(idKitab+"-"+judulIndonesia);


            if(!searchedText.matches("")||!searchedText.isEmpty()){
                highLightArab(searchedText);
            }



            String cssStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />";
            wvIsiArab.loadDataWithBaseURL("file:///android_asset/", "<html>"+cssStyle+"<body>"+isiArab+"</body></html>", "text/html", "UTF-8", "");
            wvIsiArab.setBackgroundColor(Color.parseColor("#ffffff"));
            wvIsiArab.getSettings().setDefaultTextEncodingName("UTF-8");
            WebSettings webSettings = wvIsiArab.getSettings();
            Resources res = getResources();
            int fontSize = res.getInteger(R.integer.font_size);
            webSettings.setDefaultFontSize(fontSize);


            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
            AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.setTitle(judulArab);
                        isShow = true;
                    } else if(isShow) {
                        collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                        isShow = false;
                    }
                }
            });


        }
        return rootView;
    }

    private static void highLightArab(String input) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(input);

        //Get the previous spans and remove them
        BackgroundColorSpan[] backgroundSpans = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan span: backgroundSpans) {
            spannableString.removeSpan(span);
        }

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().indexOf(input);

        while (indexOfKeyword > 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyword, indexOfKeyword + input.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(input, indexOfKeyword + input.length());
        }

        //Set the final text on TextView
//        tv.setText(spannableString);
    }
}
