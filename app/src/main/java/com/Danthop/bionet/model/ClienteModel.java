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
    private String cliente_calle;
    private String cliente_num_int;
    private String cliente_num_ext;
    private String cliente_cp;
    private String cliente_ciudad;
    private String cliente_municipio;
    private String cliente_rfc;
    private String cliente_razon_social;


    private String cp_fiscal;
    private String estado_fiscal;
    private String municipio_fiscal;
    private String colonia_fiscal;
    private String calle_fiscal;
    private String num_ext_fiscal;
    private String num_int_fiscal;

    private String cliente_direccion_fiscal;
    private String cliente_email_facturacion;

    private String Correo_igual;
    private String Direccion_igual;




    public ClienteModel(String cliente_UUID,String cliente_Nombre,
                        String cliente_Correo, String cliente_Telefono,
                        String cliente_usu_id,String cliente_estado,
                        String cliente_colonia,
                        String cliente_calle,
                        String cliente_num_int,
                        String cliente_num_ext,
                        String cliente_cp,
                        String cliente_ciudad,
                        String cliente_municipio,
                        String cliente_rfc,
                        String cliente_razon_social,
                        String cliente_direccion_fiscal,
                        String cliente_email_facturacion,
                        String cp_fiscal,
                        String estado_fiscal,
                        String municipio_fiscal,
                        String colonia_fiscal,
                        String calle_fiscal,
                        String num_ext_fiscal,
                        String num_int_fiscal,
                        String correo_igual,
                        String direccion_igual)
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

        this.cliente_direccion_fiscal = cliente_direccion_fiscal;
        this.cliente_email_facturacion = cliente_email_facturacion;

        this.cp_fiscal = cp_fiscal;
        this.estado_fiscal = estado_fiscal;
        this.municipio_fiscal = municipio_fiscal;
        this.colonia_fiscal = colonia_fiscal;
        this.calle_fiscal = calle_fiscal;
        this.num_ext_fiscal = num_ext_fiscal;
        this.num_int_fiscal = num_int_fiscal;
        this.cliente_calle=cliente_calle;
        Correo_igual = correo_igual;
        Direccion_igual = direccion_igual;

    }

    public String getCliente_UUID() {
        return cliente_UUID;
    }

    public String getCliente_Nombre() { return cliente_Nombre; }

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

    public String getCliente_direccion_fiscal() {
        return cliente_direccion_fiscal;
    }

    public void setCliente_direccion_fiscal(String cliente_direccion_fiscal) {
        this.cliente_direccion_fiscal = cliente_direccion_fiscal;
    }

    public String getCliente_email_facturacion() {
        return cliente_email_facturacion;
    }

    public void setCliente_email_facturacion(String cliente_email_facturacion) {
        this.cliente_email_facturacion = cliente_email_facturacion;
    }

    public String getCp_fiscal() {
        return cp_fiscal;
    }

    public void setCp_fiscal(String cp_fiscal) {
        this.cp_fiscal = cp_fiscal;
    }

    public String getEstado_fiscal() {
        return estado_fiscal;
    }

    public void setEstado_fiscal(String estado_fiscal) {
        this.estado_fiscal = estado_fiscal;
    }

    public String getMunicipio_fiscal() {
        return municipio_fiscal;
    }

    public void setMunicipio_fiscal(String municipio_fiscal) {
        this.municipio_fiscal = municipio_fiscal;
    }

    public String getColonia_fiscal() {
        return colonia_fiscal;
    }

    public void setColonia_fiscal(String colonia_fiscal) {
        this.colonia_fiscal = colonia_fiscal;
    }

    public String getCalle_fiscal() {
        return calle_fiscal;
    }

    public void setCalle_fiscal(String calle_fiscal) {
        this.calle_fiscal = calle_fiscal;
    }

    public String getNum_ext_fiscal() {
        return num_ext_fiscal;
    }

    public void setNum_ext_fiscal(String num_ext_fiscal) {
        this.num_ext_fiscal = num_ext_fiscal;
    }

    public String getNum_int_fiscal() {
        return num_int_fiscal;
    }

    public void setNum_int_fiscal(String num_int_fiscal) {
        this.num_int_fiscal = num_int_fiscal;
    }

    public String getCliente_calle() {
        return cliente_calle;
    }

    public void setCliente_calle(String cliente_calle) {
        this.cliente_calle = cliente_calle;
    }

    public String getCorreo_igual() {
        return Correo_igual;
    }

    public void setCorreo_igual(String correo_igual) {
        this.Correo_igual = correo_igual;
    }

    public String getDireccion_igual() {
        return Direccion_igual;
    }

    public void setDireccion_igual(String direccion_igual) {
        this.Direccion_igual = direccion_igual;
    }
}
