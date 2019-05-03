package com.Danthop.bionet.model;

public class CorteCajaModel {
    private String id_venta;
    private String total;
    private String forma_pago;
    private String fecha;
    private String hora;
    private String usuario;


    public CorteCajaModel(String id_venta, String total, String forma_pago, String fecha, String hora, String usuario){
        this.id_venta = id_venta;
        this.total = total;
        this.forma_pago = forma_pago;
        this.fecha = fecha;
        this.hora = hora;
        this.usuario = usuario;
    }

    public String getid_venta() {
        return id_venta;
    }
    public String gettotal() { return  total; }
    public String getforma_pago() { return forma_pago; }
    public String getfecha() { return fecha; }
    public String gethora() { return hora; }
    public String getusuario() { return usuario; }


    public void setid_venta(String id_venta) { this.id_venta = id_venta; }
    public void settotal(String total) { this.total = total; }
    public void setforma_pago(String forma_pago) { this.forma_pago = forma_pago; }
    public void setfecha(String fecha) { this.fecha = fecha; }
    public void sethora(String hora) { this.hora = hora; }
    public void setusuario(String usuario) { this.usuario = usuario; }

}
