package com.anthony.sena.turisena.model.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anthony.sena.turisena.R;
import com.anthony.sena.turisena.model.dao.entitis.SitioPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterSitios extends RecyclerView.Adapter<AdapterSitios.ViewHolderDatos> implements View.OnClickListener {


    List<SitioPojo> datos;
    private View.OnClickListener listener;
    private Context context;


    public AdapterSitios(List<SitioPojo> datos, Context applicationContext) {
        this.datos=datos;
        this.context=applicationContext;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sitio,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignardatos(datos.get(position),context,position);

    }


    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setOnclikListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView titulo,descripcion;


        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            imagen=itemView.findViewById(R.id.imgSitio);
            titulo=itemView.findViewById(R.id.tituloSitio);
            descripcion=itemView.findViewById(R.id.descripcionSitio);



        }


        public void asignardatos(final SitioPojo modelo, Context context, int position) {
            titulo.setText(modelo.getTitulo());
            descripcion.setText(modelo.getDescripcion());


            String url = modelo.getUrl().get(1);
            Log.i("myurl",url);

            Picasso.with(context)
                    .load(url)
                    .resize(150,300)
                    .centerCrop()
                    .into(imagen);

            Log.i("entr","");
        }
    }
}
