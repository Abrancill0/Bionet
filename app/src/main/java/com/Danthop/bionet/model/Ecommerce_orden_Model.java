package com.Danthop.bionet.model;

public class Ecommerce_orden_Model {
    private String Cliente;
    private String Articulo;
    private String Cantidad;
    private String Envio;
    private String Importe;
    private String Estatus;
    private String TipoPago;
    private String Fecha;


    public Ecommerce_orden_Model(String cliente,
                                 String articulo,
                                 String cantidad,
                                 String envio,
                                 String importe,
                                 String estatus,
                                 String tipopago,
                                 String fecha ) {
        Cliente = cliente;
        Articulo = articulo;
        Cantidad = cantidad;
        Envio = envio;
        Importe = importe;
        Estatus = estatus;
        TipoPago = tipopago;
        Fecha = fecha;

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

    public String getTipoPago() {
        return TipoPago;
    }

    public String getFecha() {
        return Fecha;
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

    public void setTipoPago(String tipoPago) {
        TipoPago = tipoPago;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }


}
