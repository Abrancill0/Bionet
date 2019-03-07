package com.Danthop.bionet.model;

public class ClienteModel {

    private String cliente_UUID;
    private String cliente_Nombre;
    private String cliente_Correo;
    private String cliente_Telefono;
    private String cliente_Ultima_Visita;
    private String cliente_usu_id;

    private String cliente_estado;
    private String cliente_colonia;
    private String cliente_num_int;
    private String cliente_num_ext;
    private String cliente_cp;
    private String cliente_ciudad;
    private String cliente_municipio;
    private String cliente_rfc;
    private String cliente_razon_social;

    public ClienteModel(String cliente_UUID,String cliente_Nombre,
                        String cliente_Correo, String cliente_Telefono,
                        String cliente_usu_id,String cliente_estado,
                        String cliente_colonia,
                        String cliente_num_int,
                        String cliente_num_ext,
                        String cliente_cp,
                        String cliente_ciudad,
                        String cliente_municipio,
                        String cliente_rfc,
                        String cliente_razon_social)
    {
        this.cliente_UUID = cliente_UUID;
        this.cliente_Nombre = cliente_Nombre;
        this.cliente_Correo = cliente_Correo;
        this.cliente_Telefono = cliente_Telefono;
        this.cliente_Ultima_Visita = cliente_Ultima_Visita;
        this.cliente_usu_id = cliente_usu_id;

        this.cliente_estado = cliente_estado;
        this.cliente_colonia = cliente_colonia;
        this.cliente_num_int = cliente_num_int;
        this.cliente_num_ext = cliente_num_ext;
        this.cliente_cp = cliente_cp;
        this.cliente_ciudad = cliente_ciudad;
        this.cliente_municipio = cliente_municipio;
        this.cliente_rfc = cliente_rfc;
        this.cliente_razon_social = cliente_razon_social;

    }

    public String getCliente_UUID() {
        return cliente_UUID;
    }

    public String getCliente_Nombre() {
        return cliente_Nombre;
    }

    public String getCliente_Correo() {
        return cliente_Correo;
    }

    public String getCliente_Telefono() {
        return cliente_Telefono;
    }

    public String getCliente_Ultima_Visita() {
        return cliente_Ultima_Visita;
    }

    public String getCliente_usu_id() {
        return cliente_usu_id;
    }

    public String getcliente_estado() {
        return cliente_estado;
    }

    public String getcliente_colonia() {
        return cliente_colonia;
    }

    public String getcliente_num_int() {
        return cliente_num_int;
    }

    public String getcliente_num_ext() {
        return cliente_num_ext;
    }

    public String getcliente_cp() {
        return cliente_cp;
    }

    public String getcliente_municipio() {
        return cliente_municipio;
    }

    public String getcliente_ciudad() {
        return cliente_ciudad;
    }

    public String getcliente_rfc() {
        return cliente_rfc;
    }

    public String getcliente_razon_social() {
        return cliente_razon_social;
    }

    public void setCliente_UUID(String cliente_UUID) {
        this.cliente_UUID = cliente_UUID;
    }

    public void setCliente_Nombre(String cliente_Nombre) {
        this.cliente_Nombre = cliente_Nombre;
    }

    public void setCliente_Correo(String cliente_Correo) {
        this.cliente_Correo = cliente_Correo;
    }

    public void setCliente_Telefono(String cliente_Telefono) {
        this.cliente_Telefono = cliente_Telefono;
    }

    public void setCliente_Ultima_Visita(String cliente_Ultima_Visita) {
        this.cliente_Ultima_Visita = cliente_Ultima_Visita;
    }

    public void setcliente_estado(String cliente_estado) {
        this.cliente_estado = cliente_estado;
    }

    public void setcliente_colonia(String cliente_colonia) {
        this.cliente_colonia = cliente_colonia;
    }

    public void setcliente_num_int(String cliente_num_int) {
        this.cliente_num_int = cliente_usu_id;
    }

    public void setcliente_num_ext(String cliente_num_ext) {
        this.cliente_num_ext = cliente_num_ext;
    }

    public void setcliente_cp(String cliente_cp) {
        this.cliente_cp = cliente_cp;
    }

    public void setcliente_ciudad(String cliente_ciudad) {
        this.cliente_ciudad = cliente_ciudad;
    }

    public void setcliente_municipio(String cliente_municipio) {
        this.cliente_municipio = cliente_municipio;
    }

    public void setCliente_usu_id(String cliente_usu_id) {
        this.cliente_usu_id = cliente_usu_id;
    }

    public void setcliente_rfc(String cliente_rfc) {
        this.cliente_rfc = cliente_rfc;
    }

    public void setcliente_razon_social(String cliente_razon_social) {
        this.cliente_razon_social = cliente_razon_social;
    }

}
