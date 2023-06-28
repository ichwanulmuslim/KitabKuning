package com.daasuu.easinginterpolator;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.daasuu.ei.Ease;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout flowLayout = findViewById(R.id.layout_flow);

        for (Ease ease : Ease.values()) {
            flowLayout.addView(new EasingGraphView(this, ease));
        }

//        ObjectAnimator animator = ObjectAnimator.ofFloat(flowLayout, "translationY", 0, -300);
//        animator.setInterpolator(new EasingInterpolator(Ease.EASE_IN_OUT_EXPO));
//        animator.setDuration(5000);
//        animator.start();

    }
}
