package com.Danthop.bionet.model;

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


    public InventarioModel(String sku,
                           String producto,
                           String modificadores,
                           String categoria,
                           String existencia,
                           String listado_Inventario,
                           String traslados,
                           String creditos_Proveedores,
                           String agregar_Productos,
                           String solicitar_Traslado)
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
        this.solicitar_Traslado =solicitar_Traslado;
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
}
