package com.Danthop.bionet.model;

public class PromocionesModel {
    private String NomPromoCredito;
    private String NomPromoPaquetes;
    private String NomPromoVolumenes;
    private String NomPromoBonificacion;


    public PromocionesModel(String NomPromoCredito, String NomPromoPaquetes, String NomPromoVolumenes, String NomPromoBonificacion){


        this.NomPromoCredito = NomPromoCredito;
        this.NomPromoPaquetes = NomPromoPaquetes;
        this.NomPromoVolumenes = NomPromoVolumenes;
        this.NomPromoBonificacion = NomPromoBonificacion;

    }

    public String getNomPromoCredito() { return NomPromoCredito; }
    public String getNomPromoPaquetes() { return NomPromoPaquetes; }
    public String getNomPromoVolumenes() { return NomPromoVolumenes; }
    public String getNomPromoBonificacion() { return NomPromoBonificacion; }



    public void setNomPromoCredito(String NomPromoCredito) { this.NomPromoCredito = NomPromoCredito; }
    public void setNomPromoPaquetes(String NomPromoPaquetes) { this.NomPromoPaquetes = NomPromoPaquetes; }
    public void setNomPromoVolumenes(String NomPromoVolumenes) { this.NomPromoVolumenes = NomPromoVolumenes; }
    public void setNomPromoBonificacion(String NomPromoBonificacion) { this.NomPromoBonificacion = NomPromoBonificacion; }

}
