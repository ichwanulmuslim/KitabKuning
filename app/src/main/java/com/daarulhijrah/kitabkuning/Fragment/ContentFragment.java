package com.daarulhijrah.kitabkuning.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.daarulhijrah.kitabkuning.Activity.ActivityAbout;
import com.daarulhijrah.kitabkuning.Activity.ActivityGridViewList;
import com.daarulhijrah.kitabkuning.Activity.ActivityInformation;
import com.daarulhijrah.kitabkuning.Activity.ActivityProfile;
import com.daarulhijrah.kitabkuning.Activity.ActivitySetting;
import com.daarulhijrah.kitabkuning.Adapter.AdapterChannelYoutube;
import com.daarulhijrah.kitabkuning.Adapter.AdapterGridView;
import com.daarulhijrah.kitabkuning.Adapter.AdapterTokoMitra;
import com.daarulhijrah.kitabkuning.Model.ChannelYoutube;
import com.daarulhijrah.kitabkuning.Model.MenuAwal;
import com.daarulhijrah.kitabkuning.Model.PromotionSlide;
import com.daarulhijrah.kitabkuning.Model.TokoMitra;
import com.daarulhijrah.kitabkuning.R;
import com.daarulhijrah.kitabkuning.Utilities.Config;
import com.daarulhijrah.kitabkuning.Utilities.RecyclerItemClickListener;
import com.daarulhijrah.kitabkuning.Utilities.UtilitiyGridView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by Konstantin on 22.12.2014.
 */
public class ContentFragment extends Fragment implements  AdapterView.OnItemClickListener {
    private View containerView;
    protected ImageView mImageView;
    protected int res;
    private Bitmap bitmap;

    private GridView gridview;
    private CarouselView carouselView;

    private AdapterGridView adapterGridView;

    RequestQueue requestQueue;
    public static ArrayList<PromotionSlide> dataPromotionSlide = new ArrayList<PromotionSlide>();
    private static ArrayList<MenuAwal> dataMenuAwal = new ArrayList<MenuAwal>();

    RecyclerView recyclerView;
    AdapterTokoMitra recyclerAdapterTokoMitra;
    public static ArrayList<TokoMitra> dataTokoMitra = new ArrayList<TokoMitra>();
    public static ArrayList<String> urlToko = new ArrayList<String>();
    String URL_TOKO_MITRA = Config.API_URL+Config.TABEL_TOKO+Config.JSON_FORMAT+Config.STATUS+Config.ORDER_TOKO_DESC;
    LinearLayoutManager layoutTokoMitra;

    RecyclerView recyclerViewChannelYoutube;
    AdapterChannelYoutube recyclerAdapterChannelYoutube;
    public static ArrayList<ChannelYoutube> dataChannelYoutube = new ArrayList<ChannelYoutube>();
    public static ArrayList<String> urlChannelYoutube = new ArrayList<String>();
    String URL_ChannelYoutube = Config.API_URL+Config.TABEL_CHANNEL_YOUTUBE+Config.JSON_FORMAT+Config.STATUS
            +Config.ORDER_TOKO_DESC+Config.FILTER_CHANNELYOUTUBE+Config.TABEL_NAMA_KITAB;
    LinearLayoutManager layoutChannelYoutube;

    TextView textViewChannel, textAdsIklan;
    private FrameLayout adContainerView;
    private AdView adView;



    public static ContentFragment newInstance(int resId) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridview = (GridView) rootView.findViewById(R.id.gridView_main);
        gridview.setOnItemClickListener(this);

        carouselView = (CarouselView) rootView.findViewById(R.id.carouselView);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recViewTokoKitab);
        recyclerViewChannelYoutube = (RecyclerView) rootView.findViewById(R.id.recViewChannelYoutube);

        textViewChannel = (TextView) rootView.findViewById(R.id.nama_channel_kitab);
        textViewChannel.setText("Ngaji Kitab "+getResources().getString(R.string.app_name));

        adContainerView = rootView.findViewById(R.id.ad_view_container);
        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

        loadAds();

        if(isOnline()){
            getData();
            carouselView.setPageCount(dataPromotionSlide.size());
            carouselView.setImageListener(imageListener);
            carouselView.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataPromotionSlide.get(position).getUrlLink())));
                }
            });
        }else{
            carouselView.setPageCount(2);
            carouselView.setImageListener(imageListener2);
        }

        layoutTokoMitra = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutTokoMitra);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                // TODO Handle item click

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlToko.get(position))));
            }

        }));

        layoutChannelYoutube = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewChannelYoutube.setLayoutManager(layoutChannelYoutube);
        recyclerViewChannelYoutube.setHasFixedSize(true);
        recyclerViewChannelYoutube.setItemAnimator(new DefaultItemAnimator());

        recyclerViewChannelYoutube.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                // TODO Handle item click

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlChannelYoutube.get(position))));
            }

        }));

        menuGrid();
        setDataAdapter();
        getDataTokoMitra();
        getDataChannelYoutube();
        UtilitiyGridView.setListViewHeightBasedOnChildren(gridview);

        return rootView;
    }

    public void menuGrid(){
        dataMenuAwal.clear();
        dataMenuAwal.add(new MenuAwal("Daftar Isi", ContextCompat.getDrawable(getActivity(), R.drawable.icon512)));
        dataMenuAwal.add(new MenuAwal("Ditandai", ContextCompat.getDrawable(getActivity(), R.drawable.menu_favorite)));
        dataMenuAwal.add(new MenuAwal("Bacaan Terakhir", ContextCompat.getDrawable(getActivity(), R.drawable.menu_recent)));
        dataMenuAwal.add(new MenuAwal("Setting", ContextCompat.getDrawable(getActivity(), R.drawable.menu_setting)));
        dataMenuAwal.add(new MenuAwal("Beri Nilai", ContextCompat.getDrawable(getActivity(), R.drawable.menu_review)));
        dataMenuAwal.add(new MenuAwal("Aplikasi Lain", ContextCompat.getDrawable(getActivity(), R.drawable.menu_other_apps)));
        dataMenuAwal.add(new MenuAwal("Tentang Apps", ContextCompat.getDrawable(getActivity(), R.drawable.menu_info)));
        dataMenuAwal.add(new MenuAwal("Informasi", ContextCompat.getDrawable(getActivity(), R.drawable.logo_dht_1)));
        dataMenuAwal.add(new MenuAwal("Profil", ContextCompat.getDrawable(getActivity(), R.drawable.logo_pesantren)));
    }

    private void setDataAdapter() {
        adapterGridView = new AdapterGridView(getActivity(), R.layout.adapter_menuawal, dataMenuAwal);
        gridview.setAdapter(adapterGridView);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(position==0){
            Intent iMenuList = new Intent(getActivity(), ActivityGridViewList.class);
            iMenuList.putExtra("category_id", 0);
            displayInterstitial();
            startActivity(iMenuList);
        }else if(position==1){
            Intent iMenuList = new Intent(getActivity(), ActivityGridViewList.class);
            iMenuList.putExtra("category_id", 1);
            displayInterstitial();
            startActivity(iMenuList);
        }else if(position==2){
            Intent iMenuList = new Intent(getActivity(), ActivityGridViewList.class);
            iMenuList.putExtra("category_id", 2);
            displayInterstitial();
            startActivity(iMenuList);
        }else if(position==3){
            startActivity(new Intent(getActivity(), ActivitySetting.class));
        }else if(position==4){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }else if(position==5){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=9023573855525788514")));
        }else if(position==6){
            startActivity(new Intent(getActivity(), ActivityAbout.class));
        }else if(position==7){
            startActivity(new Intent(getActivity(), ActivityInformation.class));
        }else if(position==8){
            startActivity(new Intent(getActivity(), ActivityProfile.class));
        }
    }

    private void getData(){
        String url = Config.API_URL+Config.TABEL_PROMOSI+Config.JSON_FORMAT+Config.STATUS+Config.ORDER_SLIDE_DESC;
        Log.e("URL",url);

        dataPromotionSlide.clear();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("result = "+response);
                try {
                    Log.e("Toko 0",response);
                    JSONObject json = new JSONObject(response);
                    JSONArray data = json.getJSONArray(Config.TABEL_PROMOSI);

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        PromotionSlide promotionSlide = new PromotionSlide();
                        promotionSlide.setTextPhoto(object.getString("text_img"));
                        promotionSlide.setPathPhoto(object.getString("src_img"));
                        promotionSlide.setUrlLink(object.getString("url"));

                        dataPromotionSlide.add(promotionSlide);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.e("Gambar Splash",dataPromotionSlide.size()+"");
                if(dataPromotionSlide.size()>1){
                    carouselView.setPageCount(dataPromotionSlide.size());
                    carouselView.setImageListener(imageListener);
                }else {
                    carouselView.setPageCount(2);
                    carouselView.setImageListener(imageListener2);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(req);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Log.e("FOTO",dataPromotionSlide.get(position).getPathPhoto());
            Glide.with(getContext())
                    .load(dataPromotionSlide.get(position).getPathPhoto())
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };

    ImageListener imageListener2 = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            Glide.with(getContext())
                    .load(R.drawable.banner_offline)
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    };

    private boolean isOnline()
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private InterstitialAd mInterstitial;

    public void loadAds(){

        mInterstitial = new InterstitialAd() {
            @Nullable
            @Override
            public FullScreenContentCallback getFullScreenContentCallback() {
                return null;
            }

            @Nullable
            @Override
            public OnPaidEventListener getOnPaidEventListener() {
                return null;
            }

            @NonNull
            @Override
            public ResponseInfo getResponseInfo() {
                return null;
            }

            @NonNull
            @Override
            public String getAdUnitId() {
                return null;
            }

            @Override
            public void setFullScreenContentCallback(@Nullable FullScreenContentCallback fullScreenContentCallback) {

            }

            @Override
            public void setImmersiveMode(boolean b) {

            }

            @Override
            public void setOnPaidEventListener(@Nullable OnPaidEventListener onPaidEventListener) {

            }

            @Override
            public void show(@NonNull Activity activity) {

            }
        };
//        mInterstitial = new InterstitialAd(getActivity());
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getActivity(), getResources().getString(R.string.interstitilal_ads), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitial = null;
                        super.onAdFailedToLoad(loadAdError);
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitial = interstitialAd;
                        super.onAdLoaded(interstitialAd);
                    }
                });

    }

    public void displayInterstitial() {
        try {
            mInterstitial.show(this.requireActivity());
        }catch (Exception e) {

        }
    }


    /*Tambah Recyler View Toko Mitra*/



    private void setDataTokoMitra() {
        recyclerAdapterTokoMitra = new AdapterTokoMitra(getActivity(), R.layout.adapter_menu_awal_toko_mitra, dataTokoMitra);
        recyclerView.setAdapter(recyclerAdapterTokoMitra);
    }

    private void getDataTokoMitra(){
        Log.e("URL",URL_TOKO_MITRA);

//        String url = Config.API_URL+Config.TABEL_PROMOSI+Config.JSON_FORMAT;
//        Log.e("URL",url);

        dataTokoMitra.clear();
        urlToko.clear();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest req = new StringRequest(Request.Method.GET, URL_TOKO_MITRA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("result = "+response);
                Log.e("Toko 1",response);

                try {

                    Log.e("Toko 2",URL_TOKO_MITRA);
                    JSONObject json = new JSONObject(response);
                    JSONArray data = json.getJSONArray(Config.TABEL_TOKO);
                    Log.e("Toko 3",dataTokoMitra.size()+" - "+response);

                    for (int i = 0; i < data.length(); i++) {

                        JSONObject object = data.getJSONObject(i);

                        dataTokoMitra.add(new TokoMitra(
                                object.getString("nama_toko"),
                                object.getString("logo_toko"),
                                object.getString("link_toko"),
                                object.getString("asal_toko")
                        ));

                        urlToko.add(object.getString("link_toko"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // if data available show data on list
                // otherwise, show alert text
                Log.e("Toko 4",dataTokoMitra.size()+"");
                if(dataTokoMitra.size() > 0){
                    setDataTokoMitra();

                }else{

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(req);

    }


    private void setDataChannelYoutube() {
        recyclerAdapterChannelYoutube = new AdapterChannelYoutube(getActivity(), R.layout.adapter_menu_channel_youtube, dataChannelYoutube);
        recyclerViewChannelYoutube.setAdapter(recyclerAdapterChannelYoutube);
    }

    private void getDataChannelYoutube(){
        Log.e("URL",URL_ChannelYoutube);

//        String url = Config.API_URL+Config.TABEL_PROMOSI+Config.JSON_FORMAT;
//        Log.e("URL",url);

        dataChannelYoutube.clear();
        urlChannelYoutube.clear();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest req = new StringRequest(Request.Method.GET, URL_ChannelYoutube, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("result = "+response);
                Log.e("Toko 1",response);

                try {

                    Log.e("Toko 2",URL_ChannelYoutube);
                    JSONObject json = new JSONObject(response);
                    JSONArray data = json.getJSONArray(Config.TABEL_CHANNEL_YOUTUBE);
                    Log.e("Toko 3",dataChannelYoutube.size()+" - "+response);

                    for (int i = 0; i < data.length(); i++) {

                        JSONObject object = data.getJSONObject(i);

                        dataChannelYoutube.add(new ChannelYoutube(
                                object.getString("nama_toko"),
                                object.getString("logo_toko"),
                                object.getString("link_toko"),
                                object.getString("asal_toko")
                        ));

                        urlChannelYoutube.add(object.getString("link_toko"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // if data available show data on list
                // otherwise, show alert text
                Log.e("Toko 4",dataChannelYoutube.size()+"");
                if(dataChannelYoutube.size() > 0){
                    setDataChannelYoutube();

                }else{

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(req);

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
        adView = new AdView(getActivity());
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
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getActivity(), adWidth);
    }


}

