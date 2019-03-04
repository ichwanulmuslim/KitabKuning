package com.daarulhijrah.kitabkuning.Activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.daarulhijrah.kitabkuning.R;


public class ActivitySetting extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
        addPreferencesFromResource(R.xml.preference);
    }
}
