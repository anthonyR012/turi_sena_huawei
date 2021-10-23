package com.anthony.sena.turisena.ui.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.anthony.sena.turisena.databinding.FragmentHomeBinding;

import com.anthony.sena.turisena.model.dao.entitis.SitioPojo;
import com.anthony.sena.turisena.model.home.AdapterSitios;
import com.anthony.sena.turisena.model.home.HomeViewModel;
import com.anthony.sena.turisena.ui.PanoramaActivity;

import java.util.List;

public class HomeFragment extends Fragment {


    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private AdapterSitios adapterSitios;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getSitios();
        binding.getPano.setOnClickListener(v -> {getpa();});

        return root;
    }

    private void getpa() {
        Intent intent = new Intent(getActivity(), PanoramaActivity.class);
        startActivity(intent);
    }


    private void getSitios() {
        homeViewModel.setInit(getContext());
        homeViewModel.getSites().observe(getActivity(), new Observer<List<SitioPojo>>() {
            @Override
            public void onChanged(List<SitioPojo> sitioPojos) {

                adapterSitios = new AdapterSitios(sitioPojos,getContext());

                binding.myrecyclesitios.setLayoutManager(new GridLayoutManager(getContext(), 1));
                binding.myrecyclesitios.setAdapter(adapterSitios);

                adapterSitios.setOnclikListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle(sitioPojos.get(binding.myrecyclesitios.getChildAdapterPosition(view)).getTitulo())
                                .setMessage(sitioPojos.get(binding.myrecyclesitios.getChildAdapterPosition(view)).getDescripcion()+
                                        " Año "+sitioPojos.get(binding.myrecyclesitios.getChildAdapterPosition(view)).getAnio())
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                    }
                                })
                                .setCancelable(false)
                                .create().show();

                    }
                });
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}