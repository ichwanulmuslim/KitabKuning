package com.daarulhijrah.kitabkuning.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daarulhijrah.kitabkuning.R;

import com.daarulhijrah.kitabkuning.Utilities.PrefManager;
import com.rubengees.introduction.IntroductionActivity;
import com.rubengees.introduction.IntroductionBuilder;
import com.rubengees.introduction.Option;
import com.rubengees.introduction.Slide;
import com.rubengees.introduction.interfaces.OnSlideListener;

import java.util.ArrayList;
import java.util.List;




public class IntroActivity extends AppCompatActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            introductionPage();
        }else{
            launchHomeScreen();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntroductionBuilder.INTRODUCTION_REQUEST_CODE && resultCode == RESULT_OK) {
            String result = "User chose: ";
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(IntroActivity.this,MainActivity.class));
            finish();

            for (Option option : data.<Option>getParcelableArrayListExtra(IntroductionActivity.OPTION_RESULT)) {
                result += option.getPosition() + (option.isActivated() ? " enabled" : " disabled");
            }
        }
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }


    private void introductionPage(){

        List<Slide> slides = new ArrayList<>();

        slides.add(0, new Slide()
                .withTitle("Asynchronous")
                .withDescription("The next image will be loaded asynchronously")
                .withColorResource(R.color.purple)
        );

        slides.add(1, new Slide()
                .withTitle("Asynchronous")
                .withDescription("This image was loaded asynchronously")
                .withColorResource(R.color.indigo)
        );

        new IntroductionBuilder(this)
                .withSlides(slides)
                .withOnSlideListener(new OnSlideListener() {
                    @Override
                    public void onSlideInit(int position, @Nullable TextView title, @NonNull ImageView image,
                                            @Nullable TextView description) {
                        if (position == 1) {
                            Glide.with(image.getContext())
                                    .load(R.drawable.image3)
                                    .into(image);
                        }
                    }
                }).introduceMyself();
    }
}
