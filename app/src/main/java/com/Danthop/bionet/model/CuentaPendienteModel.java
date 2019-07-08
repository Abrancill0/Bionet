package com.Danthop.bionet.model;

public class CuentaPendienteModel {
    private String Ticket;
    private String Ticket_id;
    private String Fecha;
    private String Cargo;
    private String Abono;
    private String Pendiente;
    private String Tipo;

    public CuentaPendienteModel(String ticket, String ticket_id, String fecha, String cargo, String abono, String pendiente,String tipo) {
        Ticket = ticket;
        ticket_id=Ticket_id;
        Fecha = fecha;
        Cargo = cargo;
        Abono = abono;
        Pendiente = pendiente;
        Tipo=tipo;
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

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getTicket_id() {
        return Ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        Ticket_id = ticket_id;
    }
}
