package com.Danthop.bionet.model;

public class CorteCajaModel {
    private String id_venta;
    private String total;
    private String forma_pago;
    private String fechahora;
    private String usuario;
    private String monto_total;

    private Double efectivo;
    private Double monederoElectronico;
    private Double dineroElectronico;
    private Double valesDespensa;

    private String fecha;
    private String hora;
    private String id_formapago;
    private Double TotalCorte;

    private Double efectivo01;
    private Double monederoElectronico05;
    private Double dineroElectronico06;
    private Double valesDespensa08;


    public CorteCajaModel(String id_venta, String total, String forma_pago, String fechahora, String usuario,
                          String monto_total, Double efectivo, Double monederoElectronico, Double dineroElectronico, Double valesDespensa, String fecha, String hora, String id_formapago, Double TotalCorte,
                          Double efectivo01, Double monederoElectronico05, Double dineroElectronico06, Double valesDespensa08){

        this.id_venta = id_venta;
        this.total = total;
        this.forma_pago = forma_pago;
        this.fechahora = fechahora;
        this.usuario = usuario;

        this.monto_total = monto_total;

        this.efectivo = efectivo;
        this.monederoElectronico = monederoElectronico;
        this.dineroElectronico = dineroElectronico;
        this.valesDespensa = valesDespensa;

        this.fecha = fecha;
        this.hora = hora;
        this.id_formapago = id_formapago;
        this.TotalCorte = TotalCorte;

        this.efectivo01 = efectivo01;
        this.monederoElectronico05 = monederoElectronico05;
        this.dineroElectronico06 = dineroElectronico06;
        this.valesDespensa08 = valesDespensa08;

    }

    public String getid_venta() { return id_venta; }
    public String gettotal() { return  total; }
    public String getforma_pago() { return forma_pago; }
    public String getfechahora() { return fechahora; }
    public String getusuario() { return usuario; }
    public String getmonto_total() { return monto_total; }

    public Double getefectivo() { return efectivo; }
    public Double getMonederoElectronico() { return monederoElectronico; }
    public Double getDineroElectronico() { return dineroElectronico; }
    public Double getValesDespensa() { return  valesDespensa; }

    public String getfecha(){ return fecha; }
    public String gethora() { return hora; }
    public String getid_formapago() { return id_formapago; }
    public Double getTotalCorte() { return TotalCorte; }

    public Double getefectivo01() { return efectivo01; }
    public Double getMonederoElectronico05() { return monederoElectronico05; }
    public Double getDineroElectronico06() { return dineroElectronico06; }
    public Double getValesDespensa08() { return  valesDespensa08; }



    public void setid_venta(String id_venta) { this.id_venta = id_venta; }
    public void settotal(String total) { this.total = total; }
    public void setforma_pago(String forma_pago) { this.forma_pago = forma_pago; }
    public void setfechahora(String fechahora) { this.fechahora = fechahora; }
    public void setusuario(String usuario) { this.usuario = usuario; }
    public void setmonto_total(String monto_total ) { this.monto_total = monto_total; }

    public void setefectivo(Double efectivo ) { this.efectivo = efectivo; }
    public void setMonederoElectronico(Double monederoElectronico) { this.monederoElectronico = monederoElectronico; }
    public void setDineroElectronico (Double dineroElectronico) { this.dineroElectronico = dineroElectronico; }
    public void setValesDespensa (Double valesDespensa) { this.valesDespensa = valesDespensa; }

    public void setefectivo01(Double efectivo01 ) { this.efectivo01 = efectivo01; }
    public void setMonederoElectronico05(Double monederoElectronico05) { this.monederoElectronico05 = monederoElectronico05; }
    public void setDineroElectronico06 (Double dineroElectronico06) { this.dineroElectronico06 = dineroElectronico06; }
    public void setValesDespensa08 (Double valesDespensa08) { this.valesDespensa08 = valesDespensa08; }

    public void setfecha(String fecha ) { this.fecha = fecha; }
    public void sethora (String hora ) { this.hora = hora; }
    public void setId_formapago (String id_formapago ) { this.id_formapago = id_formapago; }
    public void setTotalCorte (Double TotalCorte ) { this.TotalCorte = TotalCorte; }



}
