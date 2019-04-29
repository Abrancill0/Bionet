package com.Danthop.bionet.model;

public class ConfiguracionesApartadoModel {

    String Desde;
    String Hasta;
    String Tipo;
    String ValorMonto;

    public ConfiguracionesApartadoModel(String desde, String hasta, String tipo, String valorMonto) {
        Desde = desde;
        Hasta = hasta;
        Tipo = tipo;
        ValorMonto = valorMonto;
    }

    public String getDesde() {
        return Desde;
    }

    public void setDesde(String desde) {
        Desde = desde;
    }

    public String getHasta() {
        return Hasta;
    }

    public void setHasta(String hasta) {
        Hasta = hasta;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getValorMonto() {
        return ValorMonto;
    }

    public void setValorMonto(String valorMonto) {
        ValorMonto = valorMonto;
    }
}
