package com.Danthop.bionet.model;

public class SincronizarModel {

    private String Articulo;
    private String Disponible;
    private String Envio_gratis;
    private String Precio;
    private String Imagen;
    private String Categoria;
    private String EstadoOrden;

    public SincronizarModel(String articulo, String disponible, String envio_gratis, String precio,String imagen,String categoria, String estadoOrden) {
        Articulo = articulo;
        Disponible = disponible;
        Envio_gratis = envio_gratis;
        Precio = precio;
        Imagen = imagen;
        Categoria = categoria;
        EstadoOrden = estadoOrden;
    }

    public String getArticulo() {
        return Articulo;
    }

    public String getDisponible() {
        return Disponible;
    }

    public String getEnvio_gratis() {
        return Envio_gratis;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setArticulo(String articulo) {
        Articulo = articulo;
    }

    public void setDisponible(String disponible) {
        Disponible = disponible;
    }

    public void setEnvio_gratis(String envio_gratis) {
        Envio_gratis = envio_gratis;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getEstadoOrden() {
        return EstadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        EstadoOrden = estadoOrden;
    }
}
