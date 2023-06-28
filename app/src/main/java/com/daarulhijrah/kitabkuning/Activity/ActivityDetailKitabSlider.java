package com.daarulhijrah.kitabkuning.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daarulhijrah.kitabkuning.Database.DataSource;
import com.daarulhijrah.kitabkuning.Model.Kitab;
import com.daarulhijrah.kitabkuning.R;
import com.daarulhijrah.kitabkuning.Utilities.Config;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.sackcentury.shinebuttonlib.ShineButton;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

import co.ankurg.expressview.ExpressView;
import co.ankurg.expressview.OnCheckListener;

public class ActivityDetailKitabSlider extends AppCompatActivity {

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

    public static ExpressView shineFavoriteBtn;

    private static String searchedText ;

    static int idKitab;
    static String judulArab, judulIndonesia, isiArab, isiIndonesia, urlGambar, urlAudio;
    static int favorite, recent;

    private static AdView mAdViewBottom;

    private static TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kitab_slider);

        dataSource = new DataSource(this);

        dataSource.open();
        arrayListData = dataSource.getAllData();
        dataSource.close();

        Intent iGet = getIntent();
        int id_tabel = iGet.getIntExtra("id_tabel", 0);
        searchedText = iGet.getStringExtra("text_cari");
        Log.e("cari luar",searchedText +" - "+id_tabel);


        Log.e("HITUNG ",arrayListData.size()+" - "+id_tabel);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(id_tabel);

        mAdViewBottom = (AdView) findViewById(R.id.adViewBottom);
        if(!isOnline(getApplicationContext())){
            mAdViewBottom.setVisibility(View.GONE);
        }else {
            mAdViewBottom.setVisibility(View.VISIBLE);
            mAdViewBottom.loadAd(new AdRequest.Builder().build());
        }

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

            Intent iGet = getActivity().getIntent();
            searchedText = iGet.getStringExtra("text_cari");
            Log.e("cari dalem",searchedText);

            final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
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

            shineFavoriteBtn = (ExpressView) rootView.findViewById(R.id.likeButton);


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

                idKitab = arrayListDataKitab.get(i).getIdTable();
                judulArab = arrayListDataKitab.get(i).getJudulArab();
                judulIndonesia = arrayListDataKitab.get(i).getJudulIndonesia();
                isiArab = arrayListDataKitab.get(i).getIsiArab();
                isiIndonesia = arrayListDataKitab.get(i).getIsiIndonesia();
                urlGambar = arrayListDataKitab.get(i).getUrlGambar();
                urlAudio = arrayListDataKitab.get(i).getUrlAudio();
                favorite = arrayListDataKitab.get(i).getFavorite();
                recent = arrayListDataKitab.get(i).getRecent();



                final int ID_KITAB = idKitab;

                if(favorite==1){
                    shineFavoriteBtn.setChecked(true);
                }

                  shineFavoriteBtn.setOnCheckListener(new OnCheckListener() {
                      @Override
                      public void onChecked(@Nullable ExpressView expressView) {
                          dataSource.open();
                          dataSource.updateFavorite(ID_KITAB,1);
                            Log.e("Favorite","ID - "+ID_KITAB+" - ");
                            Toast.makeText(getContext(), "add to favorite", Toast.LENGTH_SHORT).show();
                            favorite = 1;
                            shineFavoriteBtn.setChecked(true);
                            dataSource.close();
                      }

                      @Override
                      public void onUnChecked(@Nullable ExpressView expressView) {
                          dataSource.open();
                            dataSource.updateFavorite(ID_KITAB,0);
                            Log.e("Favorite","ID - "+ID_KITAB+" - ");
                            Toast.makeText(getContext(), "remove from favorite", Toast.LENGTH_SHORT).show();
                            favorite = 0;
                            shineFavoriteBtn.setChecked(false);
                            dataSource.close();
                      }
                  });

//                shineFavoriteBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        dataSource.open();
//                        if(favorite==0){
//                            dataSource.updateFavorite(ID_KITAB,1);
//                            Log.e("Favorite","ID - "+ID_KITAB+" - ");
//                            Toast.makeText(getContext(), "add to favorite", Toast.LENGTH_SHORT).show();
//                            favorite = 1;
//                            shineFavoriteBtn.setChecked(true);
//                        }else {
//                            dataSource.updateFavorite(ID_KITAB,0);
//                            Log.e("Favorite","ID - "+ID_KITAB+" - ");
//                            Toast.makeText(getContext(), "remove from favorite", Toast.LENGTH_SHORT).show();
//                            favorite = 0;
//                            shineFavoriteBtn.setChecked(false);
//                        }
//                        dataSource.close();
//                    }
//                });


                final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
                String FontArab = mSharedPreference.getString("prefFontArab", "noorehira.ttf");
                String FontLatin = mSharedPreference.getString("prefFontLatin", "nefel_adeti.ttf");
                String sizeFontLatin = mSharedPreference.getString("prefFontLatinSize", "20");
                String sizeFontArab = mSharedPreference.getString("prefFontArabSize", "18");
                String sizeKonten = mSharedPreference.getString("prefKontenSize", "style.css");
                String sizeKontenFont = mSharedPreference.getString("prefKontenFont", "font_nefel_adeti.css");

                Log.e("String","Arab : "+searchedText+" - "+FontArab+", Latin : "+FontLatin+", Size Latin: "+sizeFontLatin+", Size Arab: "+sizeFontArab + sizeKontenFont);

                if(!FontArab.equals(""))
                    tvJudulArab.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), FontArab));

                if(!FontLatin.equals(""))
                    tvJudulIndonesia.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), FontLatin));

                tvJudulArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontArab));
                tvJudulIndonesia.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontLatin));


                tvJudulArab.setText(judulArab);
                tvJudulIndonesia.setText(judulIndonesia);


                String cssStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\""+sizeKonten+"\" />";
                String cssFontStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\""+sizeKontenFont+"\" />";
                wvIsiArab.loadDataWithBaseURL("file:///android_asset/", "<html>"+cssStyle+cssFontStyle+"<body>"+isiArab+"</body></html>", "text/html", "UTF-8", "");
                wvIsiArab.setBackgroundColor(Color.parseColor("#ffffff"));
                wvIsiArab.getSettings().setDefaultTextEncodingName("UTF-8");
                wvIsiArab.findAllAsync(searchedText);
                WebSettings webSettings = wvIsiArab.getSettings();
                webSettings.setJavaScriptEnabled(true);

                Resources res = getResources();
                int fontSize = res.getInteger(R.integer.font_size);
                webSettings.setDefaultFontSize(fontSize);

//                wvIsiArab.setWebViewClient(new WebViewClient(){
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        wvIsiArab.findAllAsync(searchedText);
//                    }
//                });

//                try{
//                    Method m = WebView.class.getMethod("findAllAsync", new Class<?>[]{String.class});
//                    m.invoke(wvIsiArab, searchedText);
//                } catch(Throwable notIgnored){
//                    wvIsiArab.findAllAsync(searchedText);
//                    try {
//                        Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
//                        m.invoke(wvIsiArab, true);
//                    } catch (Throwable ignored){}
//                }


                final String JUDUL_ARAB = judulArab;
                final String JUDUL_INDONESIA = judulIndonesia;
                final String ISI_ARAB= isiArab;
                final String ISI_INDONESIA = isiIndonesia;
                final String URL_GAMBAR = urlGambar;
                final String URL_AUDIO = urlAudio;




                FloatingActionMenu flMenu = (FloatingActionMenu) rootView.findViewById(R.id.fl_menu);
                flMenu.setClosedOnTouchOutside(true);

                FloatingActionButton flTTS_arab = (FloatingActionButton) rootView.findViewById(R.id.fl_tts_arab);
                flTTS_arab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status == TextToSpeech.SUCCESS) {

                                    int result1 = tts.setLanguage(new Locale("ar","SA"));
//                                    int result = tts.setLanguage(Locale.US);
                                    if (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED ||
                                            result1 == TextToSpeech.ERROR) {
                                        Log.e("TTS", "This Language is not supported");
                                        Toast.makeText(getContext(), "Teks terlalu panjang atau ada masalah jaringan", Toast.LENGTH_LONG).show();
                                    }
                                    speak(JUDUL_ARAB+"");
                                    Toast.makeText(getContext(), "TTS Bahasa", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.e("TTS", "Initilization Failed!");
                                }
                            }
                        });
                    }
                });

                FloatingActionButton flTTS = (FloatingActionButton) rootView.findViewById(R.id.fl_tts);
                flTTS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status == TextToSpeech.SUCCESS) {
                                    int result = tts.setLanguage(new Locale("ind","IDN"));
//                                    int result = tts.setLanguage(Locale.US);
                                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED ||
                                            result == TextToSpeech.ERROR) {
                                        Log.e("TTS", "This Language is not supported");
                                        Toast.makeText(getContext(), "Teks terlalu panjang atau ada masalah jaringan", Toast.LENGTH_LONG).show();
                                    }
                                    speak(JUDUL_INDONESIA+"");
                                    Toast.makeText(getContext(), "TTS Bahasa", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.e("TTS", "Initilization Failed!");
                                }
                            }

                        });
                    }
                });

                FloatingActionButton flShare = (FloatingActionButton) rootView.findViewById(R.id.fl_share);
                flShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String arabShare [] = ISI_ARAB.split("\\s");
                        String indoShare [] = ISI_INDONESIA.split("\\s");
                        String urlWeb = getContext().getPackageName().replace(".","-");

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                ">> Kitab - *"+getContext().getString(R.string.app_name)+"*\n\n"+
                                        "*Website* - https://apps.daarulhijrah.com/apps/" + urlWeb + "\n\n" +
                                        "*Playstore* - https://play.google.com/store/apps/details?id=" + getContext().getPackageName()+"\n\n"+
                                        JUDUL_ARAB + "\n\n" +
                                        JUDUL_INDONESIA + "\n\n" +
                                        Html.fromHtml(ISI_ARAB).toString() + "\n"

                        );
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);

                    }
                });

                FloatingActionButton flReport = (FloatingActionButton) rootView.findViewById(R.id.fl_report);
                flReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String subject = "Report "+getResources().getString(R.string.app_name);
                        String message = "Silakan edit bagian mana yang salah\n\n"+
                                JUDUL_ARAB + "\n\n"+
                                JUDUL_INDONESIA + "\n\n"+
                                Html.fromHtml(ISI_ARAB).toString() + "\n\n"+
                                ISI_INDONESIA+"\n\n"+
                                "Silakan Edit Bagian mana yang salah";

                        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + getResources().getString(R.string.app_email)));
                        email.setData(Uri.parse("mailto:"));
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.app_email)});
                        email.putExtra(Intent.EXTRA_SUBJECT, subject);
                        email.putExtra(Intent.EXTRA_TEXT, message);

                        // need this to prompts email client only
                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, "Choose an Email client"));
                    }
                });

            }
            return rootView;
        }
    }

    private static boolean isOnline(Context context)
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            idKitab = idKitab -1;

            dataSource.open();
            dataSource.resetRecent();
            dataSource.updateRecent(idKitab,1);
            Log.e("RECENT","ID - "+idKitab+" - Menu ID (KDMHS) : ");
            if(recent==0){

                Log.e("Favorite","ID - "+recent);
                Toast.makeText(getApplicationContext(), "add to recent", Toast.LENGTH_SHORT).show();
//                recent = 1;
                this.finish();
            }
            else {
                this.finish();
            }
            dataSource.close();

            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    return true;
            }

//            Log.e("TTS", tts.isSpeaking()+"");
        }
        return false;
    }

    private static void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }




}
