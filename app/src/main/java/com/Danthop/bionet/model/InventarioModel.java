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

    public String getsku() { return sku; }
    public String getproducto() { return producto; }
    public String getmodificadores() { return modificadores; }
    public String getcategoria() { return categoria; }
    public String getexistencia() { return existencia; }
    public String getlistado_Inventario() { return listado_Inventario; }
    public String gettraslados() { return traslados; }
    public String getcreditos_Proveedores() { return creditos_Proveedores; }
    public String getagregar_Producto() { return agregar_Productos; }
    public String getsolicitar_Traslado() { return solicitar_Traslado; }
    public String getnombre_sucursal() { return nombre_sucursal; }
    public String getsuc_id() { return suc_id; }
    public String getart_descripcion() { return art_descripcion; }
    public String getart_tipo() { return art_tipo; }
}
