package com.anthony.sena.turisena.model.dao.entitis;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UsuarioPojo implements Serializable {


        private String id;
        private String usuario;
        private String contrasena;
        private String nombre;
        private String telefono;
        private String correo;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }

        public String getNombre() {
            return nombre;
        }

}

