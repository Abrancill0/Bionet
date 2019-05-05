package com.Danthop.bionet.model;

public class CorteCajaModel {
    private String id_venta;
    private String total;
    private String forma_pago;
    private String fechahora;
    private String usuario;


    public CorteCajaModel(String id_venta, String total, String forma_pago, String fechahora, String usuario){
        this.id_venta = id_venta;
        this.total = total;
        this.forma_pago = forma_pago;
        this.fechahora = fechahora;
        this.usuario = usuario;
    }

    public String getid_venta() { return id_venta; }
    public String gettotal() { return  total; }
    public String getforma_pago() { return forma_pago; }
    public String getfechahora() { return fechahora; }
    public String getusuario() { return usuario; }


    public void setid_venta(String id_venta) { this.id_venta = id_venta; }
    public void settotal(String total) { this.total = total; }
    public void setforma_pago(String forma_pago) { this.forma_pago = forma_pago; }
    public void setfechahora(String fechahora) { this.fechahora = fechahora; }
    public void setusuario(String usuario) { this.usuario = usuario; }

}
