package com.Danthop.bionet.model;

public class MovimientoModel {
    String movimiento_fecha;
    String movimiento_suc_nombre;
    String movimiento_suc_numero;
    String movimiento_articulo_ticket;
    String movimiento_monto;

    public MovimientoModel(String movimiento_fecha, String movimiento_suc_nombre, String movimiento_suc_numero, String movimiento_articulo_ticket,
                           String movimiento_monto) {
        this.movimiento_fecha = movimiento_fecha;
        this.movimiento_suc_nombre = movimiento_suc_nombre;
        this.movimiento_suc_numero = movimiento_suc_numero;
        this.movimiento_articulo_ticket = movimiento_articulo_ticket;
        this.movimiento_monto = movimiento_monto;
    }

    public String getMovimiento_fecha() {
        return movimiento_fecha;
    }

    public void setMovimiento_fecha(String movimiento_fecha) {
        this.movimiento_fecha = movimiento_fecha;
    }

    public String getMovimiento_suc_nombre() {
        return movimiento_suc_nombre;
    }

    public void setMovimiento_suc_nombre(String movimiento_suc_nombre) {
        this.movimiento_suc_nombre = movimiento_suc_nombre;
    }

    public String getMovimiento_suc_numero() {
        return movimiento_suc_numero;
    }

    public void setMovimiento_suc_numero(String movimiento_suc_numero) {
        this.movimiento_suc_numero = movimiento_suc_numero;
    }

    public String getMovimiento_articulo_ticket() {
        return movimiento_articulo_ticket;
    }

    public void setMovimiento_articulo_ticket(String movimiento_articulo_ticket) {
        this.movimiento_articulo_ticket = movimiento_articulo_ticket;
    }

    public String getMovimiento_monto() {
        return movimiento_monto;
    }

    public void setMovimiento_monto(String movimiento_monto) {
        this.movimiento_monto = movimiento_monto;
    }
}
