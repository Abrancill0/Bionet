package com.Danthop.bionet.model;

public class ConfiguracionLealtadModel {
    private String Nivel;
    private String DineroPorPuntos;
    private String PuntosPorDinero;
    private String Status;
    private String Cpl_id;

    public ConfiguracionLealtadModel(String nivel, String dineroPorPuntos, String puntosPorDinero, String status, String cpl_id) {
        Nivel = nivel;
        DineroPorPuntos = dineroPorPuntos;
        PuntosPorDinero = puntosPorDinero;
        Status = status;
        Cpl_id = cpl_id;
    }

    public String getNivel() {
        return Nivel;
    }

    public void setNivel(String nivel) {
        Nivel = nivel;
    }



    public String getDineroPorPuntos() {
        return DineroPorPuntos;
    }

    public void setDineroPorPuntos(String dineroPorPuntos) {
        DineroPorPuntos = dineroPorPuntos;
    }

    public String getPuntosPorDinero() {
        return PuntosPorDinero;
    }

    public void setPuntosPorDinero(String puntosPorDinero) {
        PuntosPorDinero = puntosPorDinero;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCpl_id() {
        return Cpl_id;
    }

    public void setCpl_id(String cpl_id) {
        Cpl_id = cpl_id;
    }
}
