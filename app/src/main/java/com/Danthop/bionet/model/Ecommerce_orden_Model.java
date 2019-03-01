package com.Danthop.bionet.model;

public class Ecommerce_orden_Model {
    private String Cliente;
    private String Articulo;
    private String Cantidad;
    private String Envio;
    private String Importe;
    private String Estatus;


    public Ecommerce_orden_Model(String cliente, String articulo, String cantidad, String envio, String importe, String estatus) {
        Cliente = cliente;
        Articulo = articulo;
        Cantidad = cantidad;
        Envio = envio;
        Importe = importe;
        Estatus = estatus;
    }

    public String getCliente() {
        return Cliente;
    }

    public String getArticulo() {
        return Articulo;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public String getEnvio() {
        return Envio;
    }

    public String getImporte() {
        return Importe;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public void setArticulo(String articulo) {
        Articulo = articulo;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public void setEnvio(String envio) {
        Envio = envio;
    }

    public void setImporte(String importe) {
        Importe = importe;
    }

    public void setEstatus(String estatus) {
        Estatus = estatus;
    }
}
