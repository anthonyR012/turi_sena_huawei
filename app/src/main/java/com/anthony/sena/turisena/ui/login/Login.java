package com.anthony.sena.turisena.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anthony.sena.turisena.MainActivity;
import com.anthony.sena.turisena.MyApplication;
import com.anthony.sena.turisena.R;
import com.anthony.sena.turisena.databinding.ActivityLoginBinding;
import com.anthony.sena.turisena.databinding.ActivityRegistroBinding;
import com.anthony.sena.turisena.model.dao.entitis.UsuarioPojo;
import com.anthony.sena.turisena.ui.PanoramaActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.volver.setOnClickListener(view -> {returnHome();});
        binding.login.setOnClickListener(view -> {login();});

    }

    private void login() {
        Snackbar mySnackbar;

        if(!binding.usuario.getText().toString().isEmpty() && !binding.pass.getText().toString().isEmpty()){
            String url = "https://kosmeticaavanzada.com/senasoft2/query.php?case=login&&Usuario="+binding.usuario.getText().toString()+"&&Contrasena="+binding.pass.getText().toString()+"";
            url = url.replaceAll(" ", "%20");


            MyApplication.jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                 if (response !=null){
                     JSONObject json = response.optJSONObject("usuarios");
                     Log.i("dato",json+"");
                     if (json != null) {

                         Snackbar mySnackbar = Snackbar.make(binding.getRoot(),
                                 "Credenciales incorrectas", Snackbar.LENGTH_SHORT);
                         mySnackbar.show();

                         UsuarioPojo modelo = new UsuarioPojo();

                         modelo.setNombre(json.optString("Nombre"));
                         modelo.setId(json.optString("Id"));
                         modelo.setContrasena(json.optString("Contrasena"));
                         modelo.setUsuario(json.optString("Usuario"));
                         modelo.setTelefono(json.optString("Telefono"));
                         modelo.setCorreo(json.optString("Correo"));
                         Log.i("usuario",modelo.getUsuario());
                         Log.i("usuario",modelo.getUsuario());

                         sendSerializable(modelo);

                     }else{
                         Snackbar mySnackbar = Snackbar.make(binding.getRoot(),
                                 "Credenciales incorrectas", Snackbar.LENGTH_SHORT);
                         mySnackbar.show();
                     }
                 }else{
                     Snackbar mySnackbar = Snackbar.make(binding.getRoot(),
                             "Credenciales incorrectas", Snackbar.LENGTH_SHORT);
                     mySnackbar.show();
                 }




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("erorvol",error.getMessage()+"");
                }
            });

            MyApplication.requestQueue.add(MyApplication.jsonObjectRequest);
        }else{
            mySnackbar = Snackbar.make(binding.getRoot(),
                    "Complete los campos", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        }

    }

    private void sendSerializable(UsuarioPojo modelo) {

        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, MainActivity.class);
        bundle.putSerializable("usuario",modelo);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    private void returnHome() {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}