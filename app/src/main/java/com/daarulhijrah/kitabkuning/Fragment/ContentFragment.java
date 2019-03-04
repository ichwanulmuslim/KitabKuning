package com.daarulhijrah.kitabkuning.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.daarulhijrah.kitabkuning.Activity.ActivityListMenu;
import com.daarulhijrah.kitabkuning.Activity.ActivitySetting;
import com.daarulhijrah.kitabkuning.Adapter.AdapterGridView;
import com.daarulhijrah.kitabkuning.Model.MenuAwal;
import com.daarulhijrah.kitabkuning.Model.PromotionSlide;
import com.daarulhijrah.kitabkuning.R;
import com.daarulhijrah.kitabkuning.Utilities.Config;
import com.daarulhijrah.kitabkuning.Utilities.UtilitiyGridView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Konstantin on 22.12.2014.
 */
public class ContentFragment extends Fragment implements ScreenShotable, AdapterView.OnItemClickListener {
    public static final String CLOSE = "Close";
    public static final String BUILDING = "Building";
    public static final String BOOK = "Book";
    public static final String PAINT = "Paint";
    public static final String CASE = "Case";
    public static final String SHOP = "Shop";
    public static final String PARTY = "Party";
    public static final String MOVIE = "Movie";

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


    public static ContentFragment newInstance(int resId) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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

        if(isOnline()){
            getData();
            carouselView.setPageCount(dataPromotionSlide.size());
            carouselView.setImageListener(imageListener);
        }else{
            carouselView.setPageCount(2);
            carouselView.setImageListener(imageListener2);
        }

        menuGrid();
        setDataAdapter();

        UtilitiyGridView.setListViewHeightBasedOnChildren(gridview);


        return rootView;
    }

    public void menuGrid(){
        dataMenuAwal.clear();
        dataMenuAwal.add(new MenuAwal("Isi Kitab", ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background)));
        dataMenuAwal.add(new MenuAwal("Ditandai", ContextCompat.getDrawable(getActivity(), R.drawable.menu_favorite)));
        dataMenuAwal.add(new MenuAwal("Bacaan Terakhir", ContextCompat.getDrawable(getActivity(), R.drawable.menu_recent)));
        dataMenuAwal.add(new MenuAwal("Setting", ContextCompat.getDrawable(getActivity(), R.drawable.menu_setting)));
        dataMenuAwal.add(new MenuAwal("Beri Nilai", ContextCompat.getDrawable(getActivity(), R.drawable.menu_review)));
//        dataMenuAwal.add(new MenuAwal("Aplikasi Lain", ContextCompat.getDrawable(getActivity(), R.drawable.menu_other_apps)));
    }

    private void setDataAdapter() {
        adapterGridView = new AdapterGridView(getActivity(), R.layout.adapter_menuawal, dataMenuAwal);
        gridview.setAdapter(adapterGridView);
    }


    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                containerView.draw(canvas);
                ContentFragment.this.bitmap = bitmap;
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(position==0){
            startActivity(new Intent(getActivity(), ActivityListMenu.class));
        }else if(position==1){

        }else if(position==2){

        }else if(position==3){
            startActivity(new Intent(getActivity(), ActivitySetting.class));
        }else if(position==4){

        }else if(position==5){

        }
    }

    private void getData(){
        String url = Config.API_URL+Config.TABEL_PROMOSI+Config.JSON_FORMAT;
        Log.e("URL",url);

        dataPromotionSlide.clear();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("result = "+response);
                try {

                    JSONObject json = new JSONObject(response);
                    JSONArray data = json.getJSONArray(Config.TABEL_PROMOSI);

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        PromotionSlide promotionSlide = new PromotionSlide();
                        promotionSlide.setTextPhoto(object.getString("text_img"));
                        promotionSlide.setPathPhoto(object.getString("src_img"));

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


}

