package com.anthony.sena.turisena.ui.menu;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anthony.sena.turisena.MainActivity;
import com.anthony.sena.turisena.R;
import com.anthony.sena.turisena.databinding.FragmentEscanerBinding;
import com.anthony.sena.turisena.model.escaner.EscanerViewModel;
import com.anthony.sena.turisena.model.escaner.StartLector;
import com.anthony.sena.turisena.ui.login.Login;
import com.anthony.sena.turisena.ui.login.Registro;
import com.huawei.hms.hmsscankit.OnLightVisibleCallBack;
import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.ml.scan.HmsScan;


public class EscanerFragment extends Fragment {

    private EscanerViewModel escanerViewModel;
    private FragmentEscanerBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        escanerViewModel =
                new ViewModelProvider(this).get(EscanerViewModel.class);

        binding = FragmentEscanerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.imageView3.setOnClickListener(view -> {intentGo();});

        
        return root;
    }


    private void intentGo() {
        Intent intent = new Intent(getActivity(), StartLector.class);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}