package com.anthony.sena.turisena.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anthony.sena.turisena.MyApplication;
import com.anthony.sena.turisena.R;
import com.anthony.sena.turisena.databinding.ActivityRegistroBinding;
import com.anthony.sena.turisena.model.dao.entitis.SitioPojo;
import com.anthony.sena.turisena.model.dao.entitis.UsuarioPojo;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.safetydetect.UserDetectResponse;
import com.huawei.hms.support.api.safetydetect.SafetyDetect;
import com.huawei.hms.support.api.safetydetect.SafetyDetectClient;
import com.huawei.hms.support.api.safetydetect.SafetyDetectStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registro extends AppCompatActivity {

    private  ActivityRegistroBinding binding;
    private View root;
    SafetyDetectClient client;
    final String APP_ID = "104857255";
    private boolean noRobot = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        root = binding.getRoot();
        setContentView(binding.getRoot());

        client = SafetyDetect.getClient(this);

        initUserDetect();
        binding.registrar.setOnClickListener(view -> {insertdatabase();});
        binding.volver.setOnClickListener(view -> {returnhome();});

    }

    private void initUserDetect() {
        client.initUserDetect().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                // Indicates that communication with the service was successful.
              binding.robot.setOnClickListener(v1 -> {UserDetect();});
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // An error occurred during communication with the service.
            }
        });
    }

    private void UserDetect() {

        client.userDetection(APP_ID)
                .addOnSuccessListener(new OnSuccessListener<UserDetectResponse>() {
                    @Override
                    public void onSuccess(UserDetectResponse userDetectResponse) {
                        if (userDetectResponse!=null){

                            noRobot = true;
                        }else{

                            noRobot = false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // An error occurred during communication with the service.
                        String errorMsg;
                        if (e instanceof ApiException) {
                            // An error with the HMS API contains some additional details.
                            // You can use the apiException.getStatusCode() method to get the status code.
                            ApiException apiException = (ApiException) e;
                            errorMsg = SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": "
                                    + apiException.getMessage();
                        } else {
                            // Unknown type of error has occurred.
                            errorMsg = e.getMessage();
                        }
                        Log.i("TAG", "User detection fail. Error info: " + errorMsg);
                    }
                });
    }
    @Override
    protected void onDestroy() {
        shutdownUserDetect();
        super.onDestroy();

    }
    private void shutdownUserDetect() {
        // Replace with your activity or context as a parameter.
        client.shutdownUserDetect().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                Log.i("destroy","instance");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // An error occurred during communication with the service.
            }
        });
    }

    private void insertdatabase() {


        Snackbar mySnackbar;
        if (!binding.nombre.getText().toString().isEmpty()
                && !binding.telefono.getText().toString().isEmpty()
                && !binding.usuario.getText().toString().isEmpty()
                && !binding.correo.getText().toString().isEmpty()
                && !binding.contrasena.getText().toString().isEmpty()
                && !binding.confirmeContrasena.getText().toString().isEmpty()){
            if (!binding.checkBox.isChecked()){
                mySnackbar = Snackbar.make(root,
                        "Acepte terminos", Snackbar.LENGTH_SHORT);

                mySnackbar.show();
            }else {


                if (binding.contrasena.getText().toString().equals(binding.confirmeContrasena.getText().toString())) {

                    if (noRobot == true) {
                        ThreadImplement hilo = new ThreadImplement(binding.nombre.getText().toString(),
                                binding.telefono.getText().toString(), binding.usuario.getText().toString()
                                , binding.correo.getText().toString(), binding.contrasena.getText().toString());

                        hilo.start();


                        mySnackbar = Snackbar.make(root,
                                "Registro completo", Snackbar.LENGTH_SHORT);

                        mySnackbar.show();

                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        returnhome();

                    } else {
                        mySnackbar = Snackbar.make(root,
                                "Verifique que no es un robot", Snackbar.LENGTH_SHORT);

                        mySnackbar.show();
                    }

                } else {
                    mySnackbar = Snackbar.make(root,
                            "No concuerdan las contraseñas", Snackbar.LENGTH_SHORT);

                    mySnackbar.show();
                }
            }
        }else{
            mySnackbar = Snackbar.make(root,
                    "Complete los campos", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        }



    }

    private void returnhome() {

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        this.finish();

    }

    public class ThreadImplement extends Thread {

        private String nombre;
        private String telefono;
        private String correo;
        private String usuario;
        private String contrasena;

        public ThreadImplement(String nombre,String telefono,String correo,String usuario,String contrasena) {
            this.nombre = nombre;
            this.telefono = telefono;
            this.correo = correo;
            this.usuario = usuario;
            this.contrasena = contrasena;
        }

        @Override
        public void run() {
            super.run();
            String url = "https://kosmeticaavanzada.com/senasoft2/insert.php?case=usuarios&&Nombre="+nombre+"&&Telefono="+telefono+"&&Correo="+correo+"&&Usuario="+usuario+"&&Contrasena="+contrasena+"";
            url = url.replaceAll(" ", "%20");

            MyApplication.jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.i("emtra","");
                        JSONObject json = response.getJSONObject("usuarios");

                        if (json != null) {


                            Log.i("Insert","ok"+ json.optString("result"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


            MyApplication.requestQueue.add(MyApplication.jsonObjectRequest);



        }

    }
}