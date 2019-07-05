package com.Danthop.bionet.model;

public class ContactoModel {

    private String id;
    private String contacto;
    private String telefono;
    private String correo_electronico;
    private String puesto;
    private String notas;

    public ContactoModel(String id, String contacto, String telefono, String correo_electronico, String puesto, String notas) {
        this.id = id;
        this.contacto = contacto;
        this.telefono = telefono;
        this.correo_electronico = correo_electronico;
        this.puesto = puesto;
        this.notas = notas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
