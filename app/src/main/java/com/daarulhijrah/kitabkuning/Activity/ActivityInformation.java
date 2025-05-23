package com.daarulhijrah.kitabkuning.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.daarulhijrah.kitabkuning.Fragment.FragmentInfoTab1_HowToBuy;
import com.daarulhijrah.kitabkuning.Fragment.FragmentInfoTab2_Payment;
import com.daarulhijrah.kitabkuning.Fragment.FragmentInfoTab3_ContactUs;
import com.daarulhijrah.kitabkuning.R;
import com.google.android.material.tabs.TabLayout;


public class ActivityInformation extends AppCompatActivity {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_info);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

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

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new FragmentInfoTab1_HowToBuy();
                case 1 : return new FragmentInfoTab2_Payment();
                case 2 : return new FragmentInfoTab3_ContactUs();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return getResources().getString(R.string.information_tab1);
                case 1 :
                    return getResources().getString(R.string.information_tab2);
                case 2 :
                    return getResources().getString(R.string.information_tab3);
            }
            return null;
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    finish();
                    return true;
            }
        }
        return false;
    }

}

