package com.anthony.sena.turisena.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anthony.sena.turisena.MyApplication;
import com.anthony.sena.turisena.R;
import com.anthony.sena.turisena.model.dao.entitis.SitioPojo;
import com.anthony.sena.turisena.ui.menu.MapaFragment;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.BitmapDescriptor;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapaViewModel extends ViewModel {

    private static Context context;
    private MutableLiveData<List<SitioPojo>> sitios;



    public void setInit(Context context){
        this.context = context;
        if (sitios==null){
            sitios = new MutableLiveData<>();
            setUbication();
        }

    }




    private void setUbication() {
        MapaViewModel.ThreadImplement hilo = new MapaViewModel.ThreadImplement();
        hilo.start();

    }


    public LiveData<List<SitioPojo>> getSites() {
        return sitios;
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


    public static class LocationService extends LocationCallback {
        private Context context;
        private HuaweiMap huaweiMap;
        private FusedLocationProviderClient fusedLocationProviderClient;
        // Definir un objeto de solicitud de ubicación.
        private LocationRequest mLocationRequest;
        private int count = 0;

        public LocationService(Context context, HuaweiMap myHuaweiMap) {
            this.context = context;
            this.huaweiMap = myHuaweiMap;
        }

        public void StartRequest(){

            SettingsClient settingsClient= LocationServices.getSettingsClient ( context );
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(1000);
            builder.addLocationRequest(mLocationRequest);

            LocationSettingsRequest locationSettingsRequest = builder.build();
// Check the device location settings.
            settingsClient.checkLocationSettings(locationSettingsRequest)
                    // Define the listener for success in calling the API for checking device location settings.
                    .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            if (fusedLocationProviderClient==null){
                                fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);
                                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,LocationService.this, Looper.getMainLooper());
                            }

                        }
                    })
                    // Define callback for failure in checking the device location settings.
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // Processing when the device is a Huawei device and has HMS Core (APK) installed, but its settings do not meet the location requirements.
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    break;
                            }
                        }
                    });

        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult!=null){
                Log.i("location",locationResult.getLastLocation().getLatitude()+" and "
                        +locationResult.getLastLocation().getLongitude());
                if (count ==0){
                    Marker mMarker = null;

                    mMarker = huaweiMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude())));



                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ubicacion);
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                    mMarker.setIcon(bitmapDescriptor);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()),15f);

                    huaweiMap.animateCamera(cameraUpdate);
                    count++;
                }


            }
        }
    }

}