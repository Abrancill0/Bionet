package com.Danthop.bionet.model;

public class SincronizarModel {

    private String Articulo;
    private String Disponible;
    private String Envio_gratis;
    private String Precio;

    public SincronizarModel(String articulo, String disponible, String envio_gratis, String precio) {
        Articulo = articulo;
        Disponible = disponible;
        Envio_gratis = envio_gratis;
        Precio = precio;
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
}
