package com.Danthop.bionet.model;

import java.util.UUID;

public class SucursalesModel {

    private String suc_id;
    private String nombre;
    private String telefono;
    private String correo_electronico;
    private String calle;


    public String getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(String suc_id) {
        this.suc_id = suc_id;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
