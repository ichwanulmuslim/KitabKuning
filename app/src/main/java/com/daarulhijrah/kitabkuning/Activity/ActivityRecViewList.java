package com.daarulhijrah.kitabkuning.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daarulhijrah.kitabkuning.Adapter.AdapterRecViewIsiKitab;
import com.daarulhijrah.kitabkuning.Database.DataSource;
import com.daarulhijrah.kitabkuning.Model.Kitab;
import com.daarulhijrah.kitabkuning.R;
import com.daarulhijrah.kitabkuning.Utilities.Config;
import com.daarulhijrah.kitabkuning.Utilities.RecyclerItemClickListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityRecViewList extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txtAlert;

    public Kitab kitab;
    public static DataSource dataSource;

    ArrayList<Kitab> arrayListDataKitab = new ArrayList<Kitab>();

    AdapterRecViewIsiKitab adapterRecViewIsiKitab;

    int Category_ID=0;

    static final String TAG = "Activity List Menu";

    LinearLayoutManager linearLayoutManager;
    RequestQueue requestQueue;
    private AdView mAdView;

//    public String textCari;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_menu);

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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_listmenu_isikitab);

        dataSource = new DataSource(this);

        dataSource.open();
        if(Category_ID==0) {
            arrayListDataKitab = dataSource.getAllData();

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
            }
        }else if(Category_ID==1){
//            getFavData();
        }else if(Category_ID==2){
//            getRecentData();
        }
        dataSource.close();


        mAdView = (AdView) findViewById(R.id.adView);
        if(!isOnline()){
            mAdView.setVisibility(View.GONE);
        }else {
            mAdView.setVisibility(View.VISIBLE);
            mAdView.loadAd(new AdRequest.Builder().addTestDevice(Config.TEST_DEVICE).build());
        }

        setRecyclerAdapter("");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//                MenuItemCompat.getActionProvider(menu.findItem(R.id.action_search));

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


                adapterRecViewIsiKitab = new AdapterRecViewIsiKitab(getApplicationContext(), R.layout.adapter_recview_listkitab, arrayListDataKitab);
                recyclerView.setAdapter(adapterRecViewIsiKitab);
                if (arrayListDataKitab.isEmpty()) {
                    txtAlert.setVisibility(View.VISIBLE);
                } else {
                    txtAlert.setVisibility(View.GONE);
                }

                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent iDetail = new Intent(ActivityRecViewList.this, ActivityDetailKitab.class);
                        iDetail.putExtra("id_tabel", arrayListDataKitab.get(position).getIdTable());
                        iDetail.putExtra("text_cari", query);
                        startActivity(iDetail);

                    }
                }));



                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something

                dataSource.open();
                arrayListDataKitab = dataSource.getSearchText(query);
                dataSource.close();


//                mla = new AdapterKitab(ActivityMenuList.this, arrListProduk);
//                if(arrListProduk.isEmpty()){
//                    listMenu.setVisibility(View.GONE);
//                    txtAlert.setVisibility(View.VISIBLE);
//                }else{
//                    listMenu.setVisibility(View.VISIBLE);
//                    txtAlert.setVisibility(View.GONE);
//                    listMenu.setAdapter(mla);
//                }

//                setRecyclerAdapter(query);
//
//                listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                            long arg3) {
//                        // TODO Auto-generated method stub
//                        // go to menu detail page
//                        Intent iDetail = new Intent(ActivityMenuList.this, ActivityDetailPagerNoImage.class);
//                        iDetail.putExtra("menu_id", arrListProduk.get(position).getID_produk());
//                        Log.e("Barang ID",arrListProduk.get(position).getID_produk()+" - "+arrListProduk.get(position).getMenu_name());
//                        startActivity(iDetail);
//                    }
//                });

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
//			listMenu.invalidateViews();
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_other_copyright)
                        .setTitle("Reset Database")
                        .setMessage("Your favorite and recent data will be reset ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteDataKitab();
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


    private void setRecyclerAdapter(String textCari) {
        progressBar.setVisibility(View.GONE);
        adapterRecViewIsiKitab = new AdapterRecViewIsiKitab(getApplicationContext(), R.layout.adapter_recview_listkitab, arrayListDataKitab);
        recyclerView.setAdapter(adapterRecViewIsiKitab);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent iDetail = new Intent(ActivityRecViewList.this, ActivityDetailKitab.class);
                iDetail.putExtra("id_tabel", arrayListDataKitab.get(position).getIdTable());
                iDetail.putExtra("text_cari", textCari);
                startActivity(iDetail);

            }
        }));
    }

    private void getDataKitab(){
//        String URL = MenuAPI+"?auth=bab&id="+email+"&key="+keyword+"&order="+order;
        String URL_KITAB = Config.API_URL+Config.TABEL_NAMA_KITAB+Config.JSON_FORMAT+Config.ORDER_KITAB_ASC;

        requestQueue = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.GET, URL_KITAB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("result = "+response);
                try {

                    Log.e("URL JSON",URL_KITAB);
                    JSONObject json = new JSONObject(response);
                    JSONArray data = json.getJSONArray(Config.TABEL_NAMA_KITAB);
//                    Log.e("Result",data.length()+"");
                    final int numberOfItemsInResp = data.length();

                    arrayListDataKitab.clear();

                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject object = data.getJSONObject(i);

                        arrayListDataKitab.add(new Kitab(
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
                                )
                        );
                    }

                    dataSource.open();
                    dataSource.deleteTableKitab();
                    dataSource.saveKitab(arrayListDataKitab);
                    dataSource.close();

                    // if data available show data on list
                    // otherwise, show alert text
                    Log.e("Dalem",arrayListDataKitab.size()+"");
                    if(arrayListDataKitab.size() > 0){
                        progressBar.setVisibility(View.VISIBLE);
                        setRecyclerAdapter("");

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
}
