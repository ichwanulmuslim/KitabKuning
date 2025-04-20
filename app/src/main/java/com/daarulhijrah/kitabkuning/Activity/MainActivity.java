package com.daarulhijrah.kitabkuning.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;


import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daarulhijrah.kitabkuning.Fragment.ContentFragment;
import com.daarulhijrah.kitabkuning.R;
import com.daarulhijrah.kitabkuning.Utilities.Config;
import com.daarulhijrah.kitabkuning.Utilities.PrefManager;
import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity {

    private  View _decorView;
    private GestureDetector _tapDetector;
    private DrawerLayout drawerLayout;
    private LinearLayout linearLayout;
    SharedPreferences.Editor editor;
    String isDarkMode;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        _decorView = getWindow().getDecorView();
        hideSystemUI();
        setContentView(R.layout.activity_main);
        _tapDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        boolean visible = (_decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
                        if (visible)
                            hideSystemUI();
                        else
                            showSystemUI();
                        return true;
                    }
                });

        ContentFragment contentFragment = ContentFragment.newInstance(R.mipmap.ic_launcher);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, contentFragment)
                .commit();
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        isDarkMode = mSharedPreference.getString("prefTheme", "false");

//        Log.e("Dark", isDarkMode);
//        if (isDarkMode.equals("true")) {
//            AppCompatDelegate.setDefaultNightMode(
//                    AppCompatDelegate.MODE_NIGHT_YES);
//            Log.d("Dark","Disable Dark Mode");
//        }
//        else {
//            AppCompatDelegate.setDefaultNightMode(
//                    AppCompatDelegate.MODE_NIGHT_NO);
//            Log.d("Dark","Enable Dark Mode");
//        }
//
        editor = mSharedPreference.edit();

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//        isDarkMode = sharedPreferences.getString("prefTheme", "false");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        setActionBar();
    }

    private void setActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setSubtitle(R.string.app_description);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        final SwitchCompat sw = (SwitchCompat) menu.findItem(R.id.switch_action_bar).getActionView().findViewById(R.id.switch2);

        if (isDarkMode.equals("true")) {
            sw.setChecked(true);
        }else {
            sw.setChecked(false);
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(MainActivity.this, "Activate Dark Mode", Toast.LENGTH_SHORT).show();
                    editor.putString("prefTheme", "true");
                    editor.apply();
                    restartActivity();

                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(MainActivity.this, "Deactivate Dark Mode", Toast.LENGTH_SHORT).show();
                    editor.putString("prefTheme", "false");
                    editor.apply();
                    restartActivity();

                }
            }
        });

        return true;
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                PrefManager prefManager = new PrefManager(getApplicationContext());
                // make first time launch TRUE
                prefManager.setFirstTimeLaunch(true);

                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        _tapDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void hideSystemUI() {
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        _decorView.setSystemUiVisibility(uiOptions);
    }

    private void showSystemUI() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        _decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onResume() {
        hideSystemUI();
        super.onResume();
    }


}
