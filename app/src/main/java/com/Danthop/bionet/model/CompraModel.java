package com.Danthop.bionet.model;

public class CompraModel {
    private String Numero;
    private String Importe;
    private String FechaCompra;

    public CompraModel(String numero, String importe, String fechaCompra) {
        Numero = numero;
        Importe = importe;
        FechaCompra = fechaCompra;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getImporte() {
        return Importe;
    }

    public void setImporte(String importe) {
        Importe = importe;
    }

    public String getFechaCompra() {
        return FechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        FechaCompra = fechaCompra;
    }
}
