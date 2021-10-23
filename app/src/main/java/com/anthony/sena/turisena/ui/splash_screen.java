package com.anthony.sena.turisena.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.anthony.sena.turisena.MainActivity;
import com.anthony.sena.turisena.R;

public class splash_screen extends AppCompatActivity {
    private LottieAnimationView animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        animation = findViewById(R.id.mySplash);
        getSupportActionBar().hide();
        startAnimation();

    }
    private void startAnimation() {
        animation.setAnimation(R.raw.travel);
        animation.playAnimation();
        Intent intent = new Intent(this, MainActivity.class);
        //EVENTO AL FINALIZAR LA ANIMACION

        new Handler().postDelayed(new Runnable(){
            public void run(){

                startActivity(intent);
                finish();


            }
        }, 1800);



    }
}