package com.Danthop.bionet.model;

public class SucursalModel {
    private String suc_nombre;
    private String suc_telefono;
    private String suc_correo;
    private String calle;

    public SucursalModel(String suc_nombre, String suc_telefono, String suc_correo, String calle) {
        this.suc_nombre = suc_nombre;
        this.suc_telefono = suc_telefono;
        this.suc_correo = suc_correo;
        this.calle = calle;
    }

    public String getSuc_nombre() {
        return suc_nombre;
    }

    public void setSuc_nombre(String suc_nombre) {
        this.suc_nombre = suc_nombre;
    }

    public String getSuc_telefono() {
        return suc_telefono;
    }

    public void setSuc_telefono(String suc_telefono) {
        this.suc_telefono = suc_telefono;
    }

    public String getSuc_correo() {
        return suc_correo;
    }

    public void setSuc_correo(String suc_correo) {
        this.suc_correo = suc_correo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }
}
