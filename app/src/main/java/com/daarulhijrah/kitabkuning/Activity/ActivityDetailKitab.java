package com.daarulhijrah.kitabkuning.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daarulhijrah.kitabkuning.Database.DataSource;
import com.daarulhijrah.kitabkuning.Model.Kitab;
import com.daarulhijrah.kitabkuning.R;
import com.daarulhijrah.kitabkuning.Utilities.Config;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class ActivityDetailKitab extends AppCompatActivity {

    private String searchedText ;
    private int id_tabel;


    static int idKitab;
    static String judulArab, judulIndonesia, isiArab, isiIndonesia, urlGambar, urlAudio;

    public ImageView imgGambar;
    public TextView tvJudulArab, tvJudulIndonesia, tvTitleToolbar;
    public WebView wvIsiArab, wvIsiIndonesia;

    public AdView adView;


    private static DataSource dataSource;
    private static ArrayList<Kitab> arrayListDataKitab = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kitab);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitleToolbar = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        Intent iGet = getIntent();
        id_tabel = iGet.getIntExtra("id_tabel", 0);
        searchedText = iGet.getStringExtra("text_cari");

        imgGambar = (ImageView) findViewById(R.id.img_frag_detail_kitab_gambar);

        tvJudulArab = (TextView) findViewById(R.id.tv_frag_detail_kitab_judul_arab);
        tvJudulIndonesia = (TextView) findViewById(R.id.tv_frag_detail_kitab_judul_indonesia);

        wvIsiArab = (WebView) findViewById(R.id.wv_fragment_detail_kitab_isi_arab);
        wvIsiIndonesia = (WebView) findViewById(R.id.wv_fragment_detail_kitab_isi_indonesia);

        dataSource = new DataSource(this);

        dataSource.open();
        arrayListDataKitab = dataSource.getItemKitab(id_tabel);
        dataSource.close();

        adView = (AdView) findViewById(R.id.adView);

        Log.e("data",searchedText + " - "+id_tabel);

        if(!isOnline()){
            adView.setVisibility(View.GONE);
        }else {
            adView.setVisibility(View.VISIBLE);
            adView.loadAd(new AdRequest.Builder().build());
        }

        idKitab = arrayListDataKitab.get(0).getId();
        judulArab = arrayListDataKitab.get(0).getJudulArab();
        judulIndonesia = arrayListDataKitab.get(0).getJudulIndonesia();
        isiArab = arrayListDataKitab.get(0).getIsiArab();
        isiIndonesia = arrayListDataKitab.get(0).getIsiIndonesia();
        urlGambar = arrayListDataKitab.get(0).getUrlGambar();
        urlAudio = arrayListDataKitab.get(0).getUrlAudio();

        Log.e("data",idKitab + " - "+judulArab);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        String FontArab = mSharedPreference.getString("prefFontArab", "noorehira.ttf");
        String FontLatin = mSharedPreference.getString("prefFontLatin", "nefel_adeti.ttf");
        String sizeFontLatin = mSharedPreference.getString("prefFontLatinSize", "20");
        String sizeFontArab = mSharedPreference.getString("prefFontArabSize", "18");

        Log.e("String","Arab : "+FontArab+", Latin : "+FontLatin+", Size Latin: "+sizeFontLatin+", Size Arab: "+sizeFontArab);

        if(!FontArab.equals(""))
            tvJudulArab.setTypeface(Typeface.createFromAsset(getAssets(), FontArab));

        if(!FontLatin.equals(""))
            tvJudulIndonesia.setTypeface(Typeface.createFromAsset(getAssets(), FontLatin));

        tvJudulArab.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontArab));
        tvJudulIndonesia.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(sizeFontLatin));

        tvJudulArab.setText(judulArab);
        tvJudulIndonesia.setText(judulIndonesia);

        tvTitleToolbar.setText(judulIndonesia);

        String cssStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />";
        wvIsiArab.loadDataWithBaseURL("file:///android_asset/", "<html>"+cssStyle+"<body>"+isiArab+"</body></html>", "text/html", "UTF-8", "");
        wvIsiArab.setBackgroundColor(Color.parseColor("#ffffff"));
        wvIsiArab.getSettings().setDefaultTextEncodingName("UTF-8");
        WebSettings webSettings = wvIsiArab.getSettings();
        Resources res = getResources();
        int fontSize = res.getInteger(R.integer.font_size);
        webSettings.setDefaultFontSize(fontSize);

        wvIsiArab.setWebViewClient(new WebViewClient(){
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageFinished(WebView view, String url) {
                wvIsiArab.findAllAsync(searchedText);
            }
        });

        wvIsiArab.setFindListener(new WebView.FindListener() {

            @Override
            public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
                Toast.makeText(getApplicationContext(), "Matches: " + numberOfMatches, Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean isOnline() {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
