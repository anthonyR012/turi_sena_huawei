package com.anthony.sena.turisena.ui.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.anthony.sena.turisena.databinding.FragmentCuentaBinding;
import com.anthony.sena.turisena.model.CuentaViewModel;
import com.anthony.sena.turisena.model.dao.entitis.UsuarioPojo;
import com.anthony.sena.turisena.ui.login.Login;
import com.anthony.sena.turisena.ui.login.Registro;

public class CuentaFragment extends Fragment {

    private CuentaViewModel cuentaViewModel;
    private FragmentCuentaBinding binding;
    private UsuarioPojo usuario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cuentaViewModel =
                new ViewModelProvider(this).get(CuentaViewModel.class);

        binding = FragmentCuentaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle!=null){
            usuario = (UsuarioPojo) bundle.getSerializable("usuario");
            setDatos();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Aún no estas registrado \uD83D\uDE1E")
                    .setNeutralButton("Registrarme", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), Registro.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    })
                    .setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setCancelable(false)
                    .create().show();
        }

        return root;
    }

    private void setDatos() {
        binding.nombreUp.setHint(usuario.getNombre());
        binding.telefonoUp.setHint(usuario.getTelefono());
        binding.correoUpdate.setHint(usuario.getCorreo());
        binding.usuarioUp.setHint(usuario.getUsuario());
        binding.conUpdate.setHint("******");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}