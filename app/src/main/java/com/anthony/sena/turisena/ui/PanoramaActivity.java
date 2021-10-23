package com.anthony.sena.turisena.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.anthony.sena.turisena.MainActivity;
import com.anthony.sena.turisena.R;
import com.anthony.sena.turisena.ui.login.Login;
import com.huawei.hms.panorama.PanoramaInterface;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class PanoramaActivity extends AppCompatActivity {
    private PanoramaInterface.PanoramaLocalInterface panoramaInterface;
    private FrameLayout container;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        getSupportActionBar().hide();

        container=findViewById(R.id.container);
        findViewById(R.id.fullscrbtn).setOnClickListener(fullScreenView());
        Uri uri = getUriFromResource(R.raw.senap);
        Log.i("errorUri",uri+"");
        initPanorama(uri);

    }

    private void initPanorama(Uri uri) {

        panoramaInterface= Panorama.getInstance().getLocalInstance(this);
//si se inicializa correctamente
        if (panoramaInterface.init()== 0) {
//si se cargar una imagen
            if(panoramaInterface.setImage(uri, PanoramaInterface.IMAGE_TYPE_RING) ==0){
                panoramaInterface.setControlMode(PanoramaInterface.CONTROL_TYPE_TOUCH);
                container.addView(panoramaInterface.getView());
                panoramaInterface.getView().setOnTouchListener((v, event) -> {
                    panoramaInterface.getView().performClick();
                    panoramaInterface.updateTouchEvent(event);
                    return true;
                });
            }else Log.e("panorama","fallo al cargar panorama");
        }else Log.e("panorama","Faooled to load panorama");
    }



    //Lo que hace es traernos un identificador unico
    private Uri getUriFromResource(int resourceId) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + resourceId);
    }
    private View.OnClickListener fullScreenView() {
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}