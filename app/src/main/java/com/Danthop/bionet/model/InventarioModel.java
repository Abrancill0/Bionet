package com.Danthop.bionet.model;

import android.print.PrinterId;

public class InventarioModel {

    private String sku;
    private String producto;
    private String existencia;
    private String categoria;
    private String modificadores;
    private String nombre_sucursal;
    private String suc_id;
    private String art_descripcion;
    private String art_tipo;



    public InventarioModel(String sku,
                           String producto,
                           String existencia,
                           String categoria,
                           String modificadores,
                           String nombre_sucursal,
                           String suc_id,
                           String art_descripcion,
                           String art_tipo)
    {

        this.sku = sku;
        this.producto = producto;
        this.existencia = existencia;
        this.categoria = categoria;
        this.modificadores = modificadores;
        this.nombre_sucursal = nombre_sucursal;
        this.suc_id = suc_id;
        this.art_descripcion = art_descripcion;
        this.art_tipo = art_tipo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }
    public String getExistencia() { return existencia; }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getModificadores() {
        return modificadores;
    }

    public void setModificadores(String modificadores) {
        this.modificadores = modificadores;
    }

    public String getNombre_sucursal() {
        return nombre_sucursal;
    }

    public void setNombre_sucursal(String nombre_sucursal) {
        this.nombre_sucursal = nombre_sucursal;
    }

    public String getSuc_id() {
        return suc_id;
    }

    public void setSuc_id(String suc_id) {
        this.suc_id = suc_id;
    }

    public String getArt_descripcion() {
        return art_descripcion;
    }

    public void setArt_descripcion(String art_descripcion) {
        this.art_descripcion = art_descripcion;
    }

    public String getArt_tipo() {
        return art_tipo;
    }

    public void setArt_tipo(String art_tipo) {
        this.art_tipo = art_tipo;
    }

}
