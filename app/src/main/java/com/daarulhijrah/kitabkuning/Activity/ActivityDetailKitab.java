package com.daarulhijrah.kitabkuning.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import com.daarulhijrah.kitabkuning.Database.DataSource;
import com.daarulhijrah.kitabkuning.Model.Kitab;
import com.daarulhijrah.kitabkuning.R;

import java.util.ArrayList;

import static com.daarulhijrah.kitabkuning.Activity.ActivityListMenu.dataSource;

public class ActivityDetailKitab extends AppCompatActivity {

    private static DataSource dataSource;
    private static ArrayList<Kitab> arrayListDataKitab = new ArrayList<>();
    private  ArrayList<Kitab> arrayListData = new ArrayList<>();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static ImageView imgGambar;
    public static TextView tvJudulArab, tvJudulIndonesia;
    public static WebView wvIsiArab, wvIsiIndonesia;

    public static TextView tvAlert;
    public static ProgressBar progressBar;
    public static CoordinatorLayout coordinatorLayout;


    public static String searchedText = "";

    static int idKitab;
    static String judulArab, judulIndonesia, isiArab, isiIndonesia, urlGambar, urlAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kitab);
        dataSource = new DataSource(this);

        dataSource.open();
        arrayListData = dataSource.getAllData();
        dataSource.close();

        Intent iGet = getIntent();
        int id_tabel = iGet.getIntExtra("id_tabel", 0);
        searchedText = iGet.getStringExtra("text_cari");

        Log.e("ID PRODUK",id_tabel+" "+searchedText);


        Log.e("HITUNG ",arrayListData.size()+"");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(id_tabel);

        invalidateOptionsMenu();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
//            Log.e("Array Data Pager",arrayListDataKitab.size()+"");
            return arrayListData.size();
        }

    }




    public static class PlaceholderFragment extends Fragment {
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



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail_kitab, container, false);

            final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            final android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            imgGambar = (ImageView) rootView.findViewById(R.id.img_frag_detail_kitab_gambar);

            tvJudulArab = (TextView) rootView.findViewById(R.id.tv_frag_detail_kitab_judul_arab);
            tvJudulIndonesia = (TextView) rootView.findViewById(R.id.tv_frag_detail_kitab_judul_indonesia);

            //txtDescription = (WebView) findViewById(R.id.txtDescription);
            wvIsiArab = (WebView) rootView.findViewById(R.id.wv_fragment_detail_kitab_isi_arab);
            wvIsiIndonesia = (WebView) rootView.findViewById(R.id.wv_fragment_detail_kitab_isi_indonesia);

            coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.main_content);

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
//            arrayListDataKitab = dataSource.getItemKitab(4);
            arrayListDataKitab = dataSource.getItemKitab(getArguments().getInt(ARG_SECTION_NUMBER));
            Log.e("Page ",getArguments().getInt(ARG_SECTION_NUMBER)+"");
//            arrayListDataKitab = dataSource.getAllData();
            Log.e("Array ME",arrayListDataKitab.size()+"");
            dataSource.close();

            for(int i=0 ; i<arrayListDataKitab.size() ; i++) {
                coordinatorLayout.setVisibility(View.VISIBLE);

                idKitab = arrayListDataKitab.get(i).getId();
                judulArab = arrayListDataKitab.get(i).getJudulArab();
                judulIndonesia = arrayListDataKitab.get(i).getJudulIndonesia();
                isiArab = arrayListDataKitab.get(i).getIsiArab();
                isiIndonesia = arrayListDataKitab.get(i).getIsiIndonesia();
                urlGambar = arrayListDataKitab.get(i).getUrlGambar();
                urlAudio = arrayListDataKitab.get(i).getUrlAudio();

                Log.e("Page Data Kitab ", "= "+i);

//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                StringBuilder builder = new StringBuilder();

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
                tvJudulIndonesia.setText(judulIndonesia);


//                if(!searchedText.matches("")||!searchedText.isEmpty()){
//                    highLightArab("nabi");
//                }

                String cssStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />";
                wvIsiArab.loadDataWithBaseURL("file:///android_asset/", "<html>"+cssStyle+"<body>"+isiArab+"</body></html>", "text/html", "UTF-8", "");
                wvIsiArab.setBackgroundColor(Color.parseColor("#ffffff"));
                wvIsiArab.getSettings().setDefaultTextEncodingName("UTF-8");
                WebSettings webSettings = wvIsiArab.getSettings();
                Resources res = getResources();
                int fontSize = res.getInteger(R.integer.font_size);
                webSettings.setDefaultFontSize(fontSize);


//                final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
//                AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
//                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//                    boolean isShow = false;
//                    int scrollRange = -1;
//
//                    @Override
//                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                        if (scrollRange == -1) {
//                            scrollRange = appBarLayout.getTotalScrollRange();
//                        }
//                        if (scrollRange + verticalOffset == 0) {
//                            collapsingToolbarLayout.setTitle(judulArab);
//                            isShow = true;
//                        } else if(isShow) {
//                            collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
//                            isShow = false;
//                        }
//                    }
//                });


            }
            return rootView;
        }
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
        String cssStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />";
        wvIsiArab.loadDataWithBaseURL("file:///android_asset/", "<html>"+cssStyle+"<body>"+isiArab+"</body></html>", "text/html", "UTF-8", "");
        wvIsiArab.setBackgroundColor(Color.parseColor("#ffffff"));
        wvIsiArab.getSettings().setDefaultTextEncodingName("UTF-8");
//        WebSettings webSettings = wvIsiArab.getSettings();
//        Resources res = Resources.getSystem().getResources();
//        int fontSize = res.getInteger(R.integer.font_size);
//        webSettings.setDefaultFontSize(fontSize);
    }


}
