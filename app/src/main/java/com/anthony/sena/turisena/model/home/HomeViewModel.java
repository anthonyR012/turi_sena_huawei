package com.anthony.sena.turisena.model.home;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anthony.sena.turisena.MyApplication;
import com.anthony.sena.turisena.model.dao.entitis.SitioPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class HomeViewModel extends ViewModel {

    private static Context context;
    private MutableLiveData<List<SitioPojo>> sitios;


    public void setInit(Context context){
        this.context = context;
        if (sitios==null){
            sitios = new MutableLiveData<>();
            setSites();
        }

    }

    public LiveData<List<SitioPojo>> getSites() {
        return sitios;
    }


    public void setSites(){



        ThreadImplement hilo = new ThreadImplement();
        hilo.start();

    }

    public class ThreadImplement extends Thread {



        @Override
        public void run() {
            super.run();
            String url = "https://kosmeticaavanzada.com/senasoft2/get.php?case=sitios";
            url = url.replaceAll(" ", "%20");
            Log.i("hola", url);
            final ArrayList<SitioPojo> Sitio = new ArrayList<>();
            MyApplication.jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONArray json = null;
                    try {
                        json = response.getJSONArray("sitios");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                            if (json != null) {
                                for (int i = 0; i < json.length(); i++) {
                                    SitioPojo modelo = new SitioPojo();
                                    Log.i("sit",json.toString());
                                    Log.i("json",json.toString());

                                    JSONObject dato = null;
                                    try {
                                        dato = json.getJSONObject(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("mi sitio",dato.optString("Id"));
                                    modelo.setId(dato.optString("Id"));
                                    modelo.setTitulo(dato.optString("Titulo"));
                                    modelo.setDescripcion(dato.optString("Descripcion"));
                                    modelo.setLatitud(dato.optString("Latitud"));
                                    modelo.setLongitud(dato.optString("Longitud"));
                                    modelo.setAnio(dato.optString("Anio"));
                                    int c=0;

                                    JSONObject urlJson = dato.optJSONObject("url");
                                    Log.i("link", String.valueOf(dato.optJSONObject("url")));

                                    ArrayList<String> urlImg = new ArrayList();

                                    while (c < urlJson.length()){
                                        Log.i("test",urlJson.optString("Url["+c+"]"));

                                        urlImg.add(urlJson.optString("Url["+c+"]"));
                                        c++;
                                    }
                                    modelo.setUrl(urlImg);

                                    Sitio.add(modelo);
                                }
                                sitios.setValue(Sitio);

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

