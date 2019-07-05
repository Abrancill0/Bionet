package com.Danthop.bionet.model;

public class CuentaPendienteModel {
    private String Ticket;
    private String Fecha;
    private String Cargo;
    private String Abono;
    private String Pendiente;
    private Boolean ButtonAbonar;

    public CuentaPendienteModel(String ticket, String fecha, String cargo, String abono, String pendiente, Boolean buttonAbonar) {
        Ticket = ticket;
        Fecha = fecha;
        Cargo = cargo;
        Abono = abono;
        Pendiente = pendiente;
        ButtonAbonar = buttonAbonar;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getCargo() {
        return Cargo;
    }

    public void setCargo(String cargo) {
        Cargo = cargo;
    }

    public String getAbono() {
        return Abono;
    }

    public void setAbono(String abono) {
        Abono = abono;
    }

    public String getPendiente() {
        return Pendiente;
    }

    public void setPendiente(String pendiente) {
        Pendiente = pendiente;
    }

    public Boolean getButtonAbonar() {
        return ButtonAbonar;
    }

    public void setButtonAbonar(Boolean buttonAbonar) {
        ButtonAbonar = buttonAbonar;
    }
}
