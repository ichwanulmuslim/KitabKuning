package com.daarulhijrah.kitabkuning.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntroductionBuilder.INTRODUCTION_REQUEST_CODE && resultCode == RESULT_OK) {
            String result = "User chose: ";
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
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
                .withTitle(getResources().getString(R.string.app_name)+"\n"+getResources().getString(R.string.app_description))
                .withDescription("Selamat Datang di Aplikasi Kitab Kuning Digital")
                .withColorResource(R.color.purple)
        );

        slides.add(1, new Slide()
                .withTitle("Data Cloud")
                .withDescription("Data tersimpan di awan untuk memudahkan dan mempercepat update saat terjadi kesalahan")
                .withColorResource(R.color.indigo)
        );

        slides.add(2, new Slide()
                .withTitle("Beli Buku Aslinya")
                .withDescription("Aplikasi ini hanya memudahkan para pelajar dan pengajar, Belilah Kitab Asli "+getResources().getString(R.string.app_name)+" agar mendapat keberkahan ilmu")
                .withColorResource(R.color.cyan)
        );

        slides.add(3, new Slide()
                .withTitle("Fitur-fitur")
                .withDescription("Tersedia fitur Search, Favorite/Bookmark, dan Recent atau terakhir dibaca")
                .withColorResource(R.color.blue)
        );

        slides.add(4, new Slide()
                .withTitle("Iklan / Ads")
                .withDescription("Segera laporan iklan yang tidak sesuai dgn syariat, Hasil dari iklan untuk operasional sewa server, vps, langganan internet dan ujroh Tim DHTech")
                .withColorResource(R.color.brown)
        );

        new IntroductionBuilder(this)
                .withSlides(slides)
                .withOnSlideListener(new OnSlideListener() {
                    @Override
                    public void onSlideInit(int position, @Nullable TextView title, @NonNull ImageView image,
                                            @Nullable TextView description) {
                        if (position == 0) {
                            Glide.with(image.getContext())
                                    .load(R.drawable.intro_dhtech)
                                    .into(image);
                        }

                        if (position == 1) {
                            Glide.with(image.getContext())
                                    .load(R.drawable.intro_cloud)
                                    .into(image);
                        }

                        if (position == 2) {
                            Glide.with(image.getContext())
                                    .load(R.drawable.intro_kitab)
                                    .into(image);
                        }

                        if (position == 3) {
                            Glide.with(image.getContext())
                                    .load(R.drawable.intro_search)
                                    .into(image);
                        }

                        if (position == 4) {
                            Glide.with(image.getContext())
                                    .load(R.drawable.intro_thanks)
                                    .into(image);
                        }


                    }
                }).introduceMyself();
    }
}
