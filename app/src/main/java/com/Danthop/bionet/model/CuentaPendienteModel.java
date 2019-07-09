package com.Danthop.bionet.model;

public class CuentaPendienteModel {
    private String Ticket;
    private String Ticket_id;
    private String Fecha;
    private String Cargo;
    private String Abono;
    private String Pendiente;
    private String Tipo;
    private String CxC_ID;

    public CuentaPendienteModel(String cxc_id,String ticket, String ticket_id, String fecha, String cargo, String abono, String pendiente,String tipo) {
        Ticket = ticket;
        Ticket_id=ticket_id;
        Fecha = fecha;
        Cargo = cargo;
        Abono = abono;
        Pendiente = pendiente;
        Tipo=tipo;
        CxC_ID = cxc_id;
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

    public String getCxC_ID() {
        return CxC_ID;
    }

    public void setCxC_ID(String cxC_ID) {
        CxC_ID = cxC_ID;
    }
}
