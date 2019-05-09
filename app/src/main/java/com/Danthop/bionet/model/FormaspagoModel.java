package com.Danthop.bionet.model;

public class FormaspagoModel {
    private String id_pago;
    private String formapago;
    private int importe;



    public FormaspagoModel(String id_pago, String formapago, int importe){
        this.id_pago = id_pago;
        this.formapago = formapago;
        this.importe = importe;

    }

    public String getid_pago() { return id_pago; }
    public String getformapago() { return  formapago; }
    public int getImporte() { return importe; }



    public void setid_pago(String id_pago) { this.id_pago = id_pago; }
    public void setformapago(String formapago) { this.formapago = formapago; }
    public void setImporte(int importe) { this.importe = importe; }



}
