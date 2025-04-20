package com.daarulhijrah.kitabkuning.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.daarulhijrah.kitabkuning.R;


public class ActivitySetting extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
        addPreferencesFromResource(R.xml.preference);
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
    }
}
