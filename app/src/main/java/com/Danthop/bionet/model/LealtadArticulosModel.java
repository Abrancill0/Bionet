package com.Danthop.bionet.model;

public class LealtadArticulosModel {

    private String Producto;
    private String Categoria;
    private String Existencia;
    private String Precio_lista;
    private String DescuentoActual;
    private String PrecioTotal;

    public LealtadArticulosModel(String producto, String categoria, String existencia, String precio_lista, String descuentoActual, String precioTotal) {
        Producto = producto;
        Categoria = categoria;
        Existencia = existencia;
        Precio_lista = precio_lista;
        DescuentoActual = descuentoActual;
        PrecioTotal = precioTotal;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getExistencia() {
        return Existencia;
    }

    public void setExistencia(String existencia) {
        Existencia = existencia;
    }

    public String getPrecio_lista() {
        return Precio_lista;
    }

    public void setPrecio_lista(String precio_lista) {
        Precio_lista = precio_lista;
    }

    public String getDescuentoActual() {
        return DescuentoActual;
    }

    public void setDescuentoActual(String descuentoActual) {
        DescuentoActual = descuentoActual;
    }

    public String getPrecioTotal() {
        return PrecioTotal;
    }

    public void setPrecioTotal(String precioTotal) {
        PrecioTotal = precioTotal;
    }
}
