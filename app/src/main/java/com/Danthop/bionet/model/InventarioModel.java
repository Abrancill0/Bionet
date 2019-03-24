package com.Danthop.bionet.model;

import android.print.PrinterId;

public class InventarioModel {

    private String sku;
    private String producto;
    private String modificadores;
    private String categoria;
    private String existencia;
    private String listado_Inventario;
    private String traslados;
    private String creditos_Proveedores;
    private String agregar_Productos;
    private String solicitar_Traslado;
    private String nombre_sucursal;
    private String suc_id;
    private String art_descripcion;
    private String art_tipo;


    public InventarioModel(String sku,
                           String producto,
                           String modificadores,
                           String categoria,
                           String existencia,
                           String listado_Inventario,
                           String traslados,
                           String creditos_Proveedores,
                           String agregar_Productos,
                           String solicitar_Traslado,
                           String nombre_sucursal,
                           String suc_id,
                           String art_descripcion,
                           String art_tipo)
    {
        this.sku = sku;
        this.producto = producto;
        this.modificadores = modificadores;
        this.categoria = categoria;
        this.existencia = existencia;
        this.listado_Inventario = listado_Inventario;
        this.traslados = traslados;
        this.creditos_Proveedores = creditos_Proveedores;
        this.agregar_Productos = agregar_Productos;
        this.solicitar_Traslado = solicitar_Traslado;
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

    public String getModificadores() {
        return modificadores;
    }

    public void setModificadores(String modificadores) {
        this.modificadores = modificadores;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getExistencia() {
        return existencia;
    }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public String getListado_Inventario() {
        return listado_Inventario;
    }

    public void setListado_Inventario(String listado_Inventario) {
        this.listado_Inventario = listado_Inventario;
    }

    public String getTraslados() {
        return traslados;
    }

    public void setTraslados(String traslados) {
        this.traslados = traslados;
    }

    public String getCreditos_Proveedores() {
        return creditos_Proveedores;
    }

    public void setCreditos_Proveedores(String creditos_Proveedores) {
        this.creditos_Proveedores = creditos_Proveedores;
    }

    public String getAgregar_Productos() {
        return agregar_Productos;
    }

    public void setAgregar_Productos(String agregar_Productos) {
        this.agregar_Productos = agregar_Productos;
    }

    public String getSolicitar_Traslado() {
        return solicitar_Traslado;
    }

    public void setSolicitar_Traslado(String solicitar_Traslado) {
        this.solicitar_Traslado = solicitar_Traslado;
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
