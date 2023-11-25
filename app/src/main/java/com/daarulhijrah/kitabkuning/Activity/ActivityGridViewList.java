package com.daarulhijrah.kitabkuning.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.daarulhijrah.kitabkuning.Adapter.AdapterGridViewIsiKitab;

import com.daarulhijrah.kitabkuning.Database.DataSource;
import com.daarulhijrah.kitabkuning.Model.Kitab;
import com.daarulhijrah.kitabkuning.Model.TokoMitra;
import com.daarulhijrah.kitabkuning.R;
import com.daarulhijrah.kitabkuning.Utilities.Config;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityGridViewList extends AppCompatActivity {

    GridView gridView;
    ProgressBar progressBar;
    TextView txtAlert;

    public Kitab itemKitab;
    public static DataSource dataSource;

    ArrayList<Kitab> arrayListDataKitab = new ArrayList<Kitab>();

    AdapterGridViewIsiKitab adapterGridViewIsiKitab;

    int Category_ID=0;

    static final String TAG = "Activity List Menu";

    RequestQueue requestQueue;

    private FrameLayout adContainerView;
    private AdView adView;
    String prefPathAplikasi;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gridlist_menu);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        Intent iGet = getIntent();
        Category_ID = iGet.getIntExtra("category_id",0);

        progressBar = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        gridView = (GridView) findViewById(R.id.gridview_list_kitab);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        prefPathAplikasi = mSharedPreference.getString("prefPathAplikasi", Config.TABEL_NAMA_KITAB);

        dataSource = new DataSource(this);


        if(Category_ID==0) {
            dataSource.open();
            arrayListDataKitab = dataSource.getAllData();
            Log.e("Total", arrayListDataKitab.size()+"");
            dataSource.close();
            if (arrayListDataKitab.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setTitle("Download Data Kitab")
                        .setMessage("Data Kitab kami simpan di cloud, untuk penggunaan pertama silakan download dahulu ")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                getDataKitab();

                            }

                        })
                        .setNegativeButton("Tidak", null)
                        .show();

            }else {
                progressBar.setVisibility(View.GONE);
                showSearchGridView("");
            }
        }else if(Category_ID==1){
            getFavorite();
        }else if(Category_ID==2){
            getRecent();
        }

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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);

        final SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(final String query) {

                dataSource.open();
                arrayListDataKitab = dataSource.getSearchText(query);
                dataSource.close();

                showSearchGridView(query);

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something

                dataSource.open();
                arrayListDataKitab = dataSource.getSearchText(query);
                dataSource.close();

                showSearchGridView(query);

                InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_refresh:
//			Toast.makeText(getApplicationContext(), "Delete Table", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_other_copyright)
                        .setTitle("Reset Database")
                        .setMessage("Your favorite and recent data will be reset ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getDataKitab();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;


            case R.id.action_delete:
                    deleteDataKitab();
                return true;


            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteDataKitab(){
        dataSource.open();
        dataSource.deleteTableKitab();
        dataSource.close();
    }


    private void showSearchGridView(String query) {
        progressBar.setVisibility(View.GONE);
        adapterGridViewIsiKitab = new AdapterGridViewIsiKitab(ActivityGridViewList.this, arrayListDataKitab);
        gridView.setAdapter(adapterGridViewIsiKitab);
        if (arrayListDataKitab.isEmpty()) {
            txtAlert.setVisibility(View.VISIBLE);
        } else {
            txtAlert.setVisibility(View.GONE);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                // go to menu detail page
                Intent iDetail = new Intent(ActivityGridViewList.this, ActivityDetailKitabSlider.class);
                iDetail.putExtra("id_tabel", arrayListDataKitab.get(position).getIdTable());
                iDetail.putExtra("text_cari", query);
                Log.e("Urutan Klik", ""+arrayListDataKitab.get(position).getIdTable());
                startActivity(iDetail);

            }
        });
    }

    private void getDataKitab(){
        progressBar.setVisibility(View.VISIBLE);
//        String URL = MenuAPI+"?auth=bab&id="+email+"&key="+keyword+"&order="+order;
        String URL_KITAB = Config.API_URL+prefPathAplikasi+Config.JSON_FORMAT+Config.ORDER_KITAB_ASC;

        requestQueue = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.GET, URL_KITAB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("result = "+response);
                try {

                    Log.e("URL JSON",URL_KITAB);
                    JSONObject json = new JSONObject(response);
                    JSONArray data = json.getJSONArray(prefPathAplikasi);
//                    Log.e("Result",data.length()+"");
                    final int numberOfItemsInResp = data.length();

                    arrayListDataKitab.clear();

                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject object = data.getJSONObject(i);

                        Log.e("Urutan","ke = "+i);

                        itemKitab = new Kitab(
                                i,
                                object.getInt("id"),
                                object.getString("judul_arab"),
                                object.getString("judul_indonesia"),
                                object.getString("isi_arab"),
                                object.getString("isi_indonesia"),
                                object.getString("url_gambar"),
                                object.getString("url_audio"),
                                0,
                                0
                        );

                        arrayListDataKitab.add(itemKitab);
                    }

                    dataSource.open();
                    dataSource.deleteTableKitab();
                    dataSource.saveKitab(arrayListDataKitab);
                    dataSource.close();

                    // if data available show data on list
                    // otherwise, show alert text
                    Log.e("Dalem",arrayListDataKitab.size()+"");
                    if(arrayListDataKitab.size() > 0){
                        progressBar.setVisibility(View.GONE);
                        showSearchGridView("");

                        Toast.makeText(getApplicationContext(),"Update berhasil...",Toast.LENGTH_SHORT).show();

                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(req);
        if(!progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);
            dataSource.open();
            dataSource.getAllData();
            dataSource.close();
            showSearchGridView("");
        }


    }

    private boolean isOnline()
    {
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

    private void getFavorite(){
        dataSource.open();
        arrayListDataKitab = dataSource.getFavoriteData();
        dataSource.close();
        showSearchGridView("");
        getSupportActionBar().setTitle("Favorite ("+arrayListDataKitab.size()+")");
    }

    private void getRecent(){
        dataSource.open();
        arrayListDataKitab = dataSource.getRecentData();
        dataSource.close();
        showSearchGridView("");
        getSupportActionBar().setTitle("Terakhir Dibaca");
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
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
