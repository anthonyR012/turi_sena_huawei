package com.anthony.sena.turisena.ui.menu;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.anthony.sena.turisena.R;
import com.anthony.sena.turisena.databinding.FragmentMapaBinding;
import com.anthony.sena.turisena.model.MapaViewModel;
import com.anthony.sena.turisena.model.dao.entitis.SitioPojo;
import com.anthony.sena.turisena.model.dao.entitis.UsuarioPojo;
import com.anthony.sena.turisena.ui.PanoramaActivity;
import com.anthony.sena.turisena.ui.login.Login;
import com.anthony.sena.turisena.ui.login.Registro;
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptor;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;

import java.util.List;

public class MapaFragment extends Fragment implements OnMapReadyCallback{

    private static final String API_KEY = "CwEAAAAA4tPjm6QxmiwEnuf+XZDhyUb4btgRzy2f2lwK1GyKcq5Erbi/9DWmeeWZkLc656D+uFInwTyU7y9Ub9QPtZwTWyPpZAQ=";
    private MapaViewModel mapaViewModel;
    private FragmentMapaBinding binding;
    private HuaweiMap myHuaweiMap;
    private MapView myMap;
    private MapaViewModel.LocationService locationService;
    private static final String MAPVIEW_BUNDLE_KEY = "0229CEF5B69B0BA966A6644A13D4CFC5A54C335DA8178170495C6573AECFAA57";
    private List<SitioPojo> sites;
    private UsuarioPojo usuario;
    private int count = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapaViewModel =
                new ViewModelProvider(this).get(MapaViewModel.class);

        binding = FragmentMapaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        PrepareLocationRequest();
        getMap();

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle!=null){
            usuario = (UsuarioPojo) bundle.getSerializable("usuario");
        }


        
        return root;
    }

    private void PrepareLocationRequest() {


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {

            if (ActivityCompat.checkSelfPermission ( getContext() ,
                    Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission ( getContext() ,
                    Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

                String[] strings =
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(getActivity(), strings, 1);

            }
        } else {
            // Solicite dinámicamente el permiso android.permission.ACCESS_BACKGROUND_LOCATION además de los permisos anteriores si el nivel de API es superior a 28.
            if (ActivityCompat.checkSelfPermission ( getContext() ,
                    Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission ( getContext() ,
                    Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission ( getContext() ,
                    "android.permission.ACCESS_BACKGROUND_LOCATION" )!= PackageManager.PERMISSION_GRANTED) {

                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(getActivity(), strings, 2);

            }
        }


    }


    private void getMap() {
        myMap = binding.MyMapView;
        myMap.onCreate(null);
        myMap.getMapAsync(this);
    }



    @Override
    public void onStart() {
        super.onStart();
        myMap.onStart();
    }
    @Override
    public void onResume() {

        super.onResume();
        myMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        myMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        myMap.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        myMap.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        myMap.onSaveInstanceState(mapViewBundle);
    }




    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
        if (huaweiMap!=null) {

            this.myHuaweiMap = huaweiMap;
            setUbications(this.myHuaweiMap);
//            setLoca(3.4296084468195938, -76.50366887661558,14f);

            if (locationService==null){
                locationService = new MapaViewModel.LocationService(getContext(),this.myHuaweiMap);
                locationService.StartRequest();
            }else{
                locationService = null;
            }
        }
    }


    private void setUbications(HuaweiMap myHuaweiMap) {

        mapaViewModel.setInit(getContext());
        mapaViewModel.getSites().observe(getActivity(), new Observer<List<SitioPojo>>() {


            @Override
            public void onChanged(List<SitioPojo> sitioPojos) {
                    sites = sitioPojos;

                for (int i = 0;i < sitioPojos.size();i++){

                    Marker mMarker = MapaFragment.this.myHuaweiMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(sitioPojos.get(i).getLatitud()),Double.parseDouble(sitioPojos.get(i).getLongitud()))));
                    mMarker.setTitle(sitioPojos.get(i).getTitulo());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marcador);
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                    mMarker.setIcon(bitmapDescriptor);

                }


            }
        });

        myHuaweiMap.setOnMarkerClickListener(new HuaweiMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                showDialogOpcion(marker.getTitle());
                return false;

            }
        });
    }

    public void showDialogOpcion(String title){

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("\uD83C\uDF0E\uD83C\uDF0E "+title)
                .setNeutralButton("Marcar visitado", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (usuario!=null){
                            //hacer insert
                            return;
                        }else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Aún no estas registrado \uD83D\uDE1E")
                                    .setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getActivity(), Login.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    })
                                    .setNeutralButton("Registrarme", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getActivity(), Registro.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }).setCancelable(false)
                                .create().show();

                        }

                    }
                })

                .setNegativeButton("Navega", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();

    }
}