package com.daarulhijrah.kitabkuning.Activity;

import static android.util.Log.e;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
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
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
//    public static TextView tvJudulArab, tvJudulIndonesia;
    public static WebView wvIsiArab, wvIsiIndonesia;

    public static TextView tvAlert;
    public static ProgressBar progressBar;
    public static CoordinatorLayout coordinatorLayout;

    public static ExpressView shineFavoriteBtn;

    private static String searchedText ;

    static int idKitab;
    static String judulArab, judulIndonesia, isiArab, isiIndonesia, urlGambar, urlAudio;
    static int favorite, recent;
    private static TextToSpeech tts;
    public static int id_tabel;

    private FrameLayout adContainerView;
    private AdView adView;
    private Menu createMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kitab_slider);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        dataSource = new DataSource(this);

        dataSource.open();
        arrayListData = dataSource.getAllData();
        dataSource.close();

        Intent iGet = getIntent();
        id_tabel = iGet.getIntExtra("id_tabel", 0);
        searchedText = iGet.getStringExtra("text_cari");
        Log.e("cari luar",searchedText +" - "+id_tabel);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        String isDarkMode = mSharedPreference.getString("prefTheme", "false");

        if (isDarkMode.equals("true")) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
            Log.d("Dark Atas","Disable Dark Mode");
        }
        else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            Log.d("Dark Atas","Enable Dark Mode");
        }


        Log.e("HITUNG ",arrayListData.size()+" - "+id_tabel);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(id_tabel);

        invalidateOptionsMenu();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        adContainerView = findViewById(R.id.ad_view_container);
        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });
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
            Log.e("Array Data Pager",arrayListDataKitab.size()+"");
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

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail_kitab, container, false);

            final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if (actionBar != null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
            }

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    getActivity().finish();
                }
            });

            Intent iGet = getActivity().getIntent();
            searchedText = iGet.getStringExtra("text_cari");
            Log.e("cari dalem",searchedText+" - "+id_tabel);

//            tvJudulArab = (TextView) rootView.findViewById(R.id.tv_frag_detail_kitab_judul_arab);
//            tvJudulIndonesia = (TextView) rootView.findViewById(R.id.tv_frag_detail_kitab_judul_indonesia);

            //txtDescription = (WebView) findViewById(R.id.txtDescription);
            wvIsiArab = (WebView) rootView.findViewById(R.id.wv_fragment_detail_kitab_isi_arab);
            wvIsiIndonesia = (WebView) rootView.findViewById(R.id.wv_fragment_detail_kitab_isi_indonesia);
            coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.main_content);
            NestedScrollView scroller = (NestedScrollView) rootView.findViewById(R.id.sclDetail);
            shineFavoriteBtn = (ExpressView) rootView.findViewById(R.id.likeButton);

//            FloatingActionButton flNext = (FloatingActionButton) rootView.findViewById(R.id.fab_next);
//            FloatingActionButton flPrev = (FloatingActionButton) rootView.findViewById(R.id.fab_prev);

            


//            if (scroller != null) {
//
//                scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                    @Override
//                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                        if (scrollY > oldScrollY) {
//                            Log.i("Halo", "Scroll DOWN");
//                            if (!searchedText.isEmpty()) {
//                                flPrev.setVisibility(View.INVISIBLE);
//                                flNext.setVisibility(View.INVISIBLE);
//                            }
//                        }
//                        if (scrollY < oldScrollY) {
//                            Log.i("Halo", "Scroll UP");
//                            if (!searchedText.isEmpty()){
//                                flPrev.setVisibility(View.VISIBLE);
//                                flNext.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                        if (scrollY == 0) {
//                            Log.i("Halo", "TOP SCROLL");
//                        }
//
//                        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                            Log.i("Halo", "BOTTOM SCROLL");
//                        }
//                    }
//                });
//            }

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

                final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
                String isDarkMode = mSharedPreference.getString("prefTheme", "false");
                String FontArab = mSharedPreference.getString("prefFontArab", "arab_kemenag.ttf");
                String FontLatin = mSharedPreference.getString("prefFontLatin", "arab_kemenag.ttf");
                String sizeFontLatin = mSharedPreference.getString("prefFontLatinSize", "20");
                String sizeFontArab = mSharedPreference.getString("prefFontArabSize", "18");
                String sizeKonten = mSharedPreference.getString("prefKontenSize", "style.css");
                String sizeKontenFont = mSharedPreference.getString("prefKontenFont", "font_arab_kemenag.css");

                Log.e("Dark 1", isDarkMode);
                Log.e("String","Arab : "+searchedText+" - "+FontArab+", Latin : "+FontLatin+", Size Latin: "+sizeFontLatin+", Size Arab: "+sizeFontArab + sizeKontenFont);

//                if(!FontArab.equals(""))
//                    tvJudulArab.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), FontArab));
//
//                if(!FontLatin.equals(""))
//                    tvJudulIndonesia.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), FontLatin));
//
//                tvJudulArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontArab));
//                tvJudulIndonesia.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontLatin));

                int id = (Integer) idKitab + 1;
                int total = (Integer) ActivityGridViewList.numberOfItemsInResp;

//                tvJudulArab.setText(judulArab);
//                tvJudulIndonesia.setText(judulIndonesia+" ("+id+"/"+total+")");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);

                WebSettings webSettings = wvIsiArab.getSettings();
                webSettings.setJavaScriptEnabled(true);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    webSettings.setForceDark(WebSettings.FORCE_DARK_ON);
                }
                if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(wvIsiArab.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                }
                if(WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                    WebSettingsCompat.setAlgorithmicDarkeningAllowed(wvIsiArab.getSettings(), true);
                }
                if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(wvIsiArab.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                }
            String cssStyle;
                if (isDarkMode.equals("true")) {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);
                    Log.d("Dark-web","Disable Dark Mode");
                    cssStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\"night-"+sizeKonten+"\" />";
                    Log.d("Dark-web",cssStyle);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);
                    Log.d("Dark-web","Enable Dark Mode");
                    cssStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\""+sizeKonten+"\" />";
                    Log.d("Dark-web",cssStyle);
                }
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(judulIndonesia);

                String cssFontStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\""+sizeKontenFont+"\" />";
                wvIsiArab.loadDataWithBaseURL("file:///android_asset/", "<html>"+cssStyle+cssFontStyle+"<body>"
                        +"<h2>"+judulArab+"</h2>"
                        +"<h1>"+judulIndonesia+"</h1>"
                        +"<p>("+id+"/"+total+")</p>"
                        +isiArab+
                        "</body></html>", "text/html", "UTF-8", "");
                wvIsiArab.getSettings().setDefaultTextEncodingName("UTF-8");
                wvIsiArab.findAllAsync(searchedText);
//                wvIsiIndonesia.loadDataWithBaseURL(null, "<html><body><h1>Contoh Teks</h1><p>Ini adalah contoh teks yang akan di-highlight.</p></body></html>", "text/html", "UTF-8", null);
//                wvIsiIndonesia.loadUrl("javascript:highlight('" + searchedText + "')");

                wvIsiArab.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        // set the visibility to visible when
                        // the page starts loading
                        progressBar.setVisibility(View.VISIBLE);
                        Log.e("cari","Page Started");
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        // set the visibility to gone when the page
                        // gets loaded completely
                        progressBar.setVisibility(View.GONE);
                        wvIsiArab.findAllAsync(searchedText);
//                        if (searchedText != null && !searchedText.equals("")) {
//                            int i = wvIsiArab.findAll(searchedText);
//                            Toast.makeText(getContext(), "Found " + i + " results !",
//                                    Toast.LENGTH_SHORT).show();
//                            try {
//                                Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
//                                m.invoke(wvIsiArab, true);
//                                Log.e("cari","Page Finished" + searchedText);
//                                wvIsiArab.findAllAsync(searchedText);
//                            } catch (Throwable ignored) {
//                            }
////                            searchedText = "";
//                        }
                        Log.e("cari","Page Finished");
                    }
                });

                Resources res = getResources();
                int fontSize = res.getInteger(R.integer.font_size);
                webSettings.setDefaultFontSize(fontSize);


                final String JUDUL_ARAB = judulArab;
                final String JUDUL_INDONESIA = judulIndonesia;
                final String ISI_ARAB= isiArab;
                final String ISI_INDONESIA = isiIndonesia;
                final String URL_GAMBAR = urlGambar;
                final String URL_AUDIO = urlAudio;


                wvIsiArab.findAllAsync(searchedText);
                wvIsiArab.findFocus();
//                FloatingActionButton flNext = (FloatingActionButton) rootView.findViewById(R.id.fab_next);
//                FloatingActionButton flPrev = (FloatingActionButton) rootView.findViewById(R.id.fab_prev);
//                if (!searchedText.isEmpty()) {
//
//                    flPrev.setVisibility(View.VISIBLE);
//                    flNext.setVisibility(View.VISIBLE);
//
//                } else {
//
//                    flPrev.setVisibility(View.INVISIBLE);
//                    flNext.setVisibility(View.INVISIBLE);
//                }

//                flNext.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        wvIsiArab.findAllAsync(searchedText);
//                        wvIsiArab.findNext(true);
//                        wvIsiArab.setFindListener(new WebView.FindListener() {
//
//                            @Override
//                            public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
//                                wvIsiArab.findNext(true);
//                                wvIsiArab.findFocus();
//                            }
//                        });
//                        Toast.makeText(getContext(), "Next", Toast.LENGTH_SHORT).show();
//                    }
//                });


//                flPrev.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        wvIsiArab.findAllAsync(searchedText);
//                        wvIsiArab.findNext(false);
//                        wvIsiArab.setFindListener(new WebView.FindListener() {
//                            @Override
//                            public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
//                                wvIsiArab.findNext(false);
//                                wvIsiArab.findFocus();
//                            }
//                        });
//                        Toast.makeText(getContext(), "Prev", Toast.LENGTH_SHORT).show();
//                    }
//                });




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
                                    speak(androidx.core.text.HtmlCompat.fromHtml(JUDUL_ARAB, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                                    speak(androidx.core.text.HtmlCompat.fromHtml(ISI_ARAB, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replaceAll("[a-zA-Z:.,)(1-9`~!@#$'%^&*()_+[\\\\]\\\\\\\\;\\',./{}|:\\\"<>?]",""));
                                    Toast.makeText(getContext(), "TTS Bahasa", Toast.LENGTH_LONG).show();
                                    e("TTS Arab", androidx.core.text.HtmlCompat.fromHtml(ISI_ARAB, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replaceAll("[a-zA-Z:.,)(1-9`~!@#$%^&*()_+[\\\\]\\\\\\\\;\\',./{}|:\\\"<>?]",""));

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
                                    speak(JUDUL_INDONESIA);
                                    speak(androidx.core.text.HtmlCompat.fromHtml(ISI_ARAB, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                                    e("TTS", androidx.core.text.HtmlCompat.fromHtml(ISI_ARAB, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
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
                                        "*Website* - https://kitabkuning.id/apps/" + urlWeb + "\n\n" +
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
                Toast.makeText(getApplicationContext(), "Tandai terakhir dibaca : "+idKitab, Toast.LENGTH_SHORT).show();
//                recent = 1;
                this.finish();
            }
            else {
                this.finish();
            }
            dataSource.close();

            try {
                tts.shutdown();
            }catch (Exception e){

            }
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

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
//            wvIsiArab.findAllAsync(searchedText);
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
//            wvIsiArab.findAllAsync(searchedText);
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
//            wvIsiArab.findAllAsync(searchedText);
        }
        try {
            tts.shutdown();
        }catch (Exception e){

        }
        super.onDestroy();
    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(getResources().getString(R.string.banner_ads));
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

}
