package com.Danthop.bionet.model;

public class CorteCajaModel {
    private String id_venta;
    private String total;
    private String forma_pago;
    private String fechahora;
    private String usuario;
    private String monto_total;
    private String efectivo;
    private String fecha;
    private String hora;


    public CorteCajaModel(String id_venta, String total, String forma_pago, String fechahora, String usuario,
                          String monto_total, String efectivo, String fecha, String hora){
        this.id_venta = id_venta;
        this.total = total;
        this.forma_pago = forma_pago;
        this.fechahora = fechahora;
        this.usuario = usuario;

        this.monto_total = monto_total;
        this.efectivo = efectivo;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getid_venta() { return id_venta; }
    public String gettotal() { return  total; }
    public String getforma_pago() { return forma_pago; }
    public String getfechahora() { return fechahora; }
    public String getusuario() { return usuario; }
    public String getmonto_total() { return monto_total; }
    public String getefectivo() { return efectivo; }
    public String getfecha(){ return fecha; }
    public String gethora() { return hora; }


    public void setid_venta(String id_venta) { this.id_venta = id_venta; }
    public void settotal(String total) { this.total = total; }
    public void setforma_pago(String forma_pago) { this.forma_pago = forma_pago; }
    public void setfechahora(String fechahora) { this.fechahora = fechahora; }
    public void setusuario(String usuario) { this.usuario = usuario; }
    public void setmonto_total(String monto_total ) { this.monto_total = monto_total; }
    public void setefectivo(String efectivo ) { this.efectivo = efectivo; }
    public void setfecha(String fecha ) { this.fecha = fecha; }
    public void sethora (String hora ) { this.hora = hora; }

}
