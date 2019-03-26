package com.Danthop.bionet.model;


public class LoginContrasenaModel {

    private String estatus;
    private String mensaje;
    private String usu_contrasenia;
    private String usu_tipo_contrasenia;
    private String usu_nombre;
    private String apellidos;
    private String usu_apellido_paterno;
    private String usu_apellido_materno;
    private String usu_correo_electronico;
    private String usu_imagen_perfil;
    private String usu_activo;
    private String usu_administrador;
    private String usu_id;

    public String getestatus() {
        return estatus;
    }
    public void setestatus(String estatus) {
        this.estatus = estatus;
    }

    public String getmensaje() {
        return mensaje;
    }
    public void setmensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getusu_contrasenia() { return usu_contrasenia; }
    public void  setusu_contrasenia(String usu_contrasenia) { this.usu_contrasenia = usu_contrasenia; }

    public String getusu_tipo_contrasenia() { return usu_tipo_contrasenia; }
    public void  setusu_tipo_contrasenia(String usu_tipo_contrasenia) { this.usu_tipo_contrasenia = usu_tipo_contrasenia; }

    public String getusu_nombre() {
        return usu_nombre;
    }
    public void setusu_nombre(String usu_nombre) {
        this.usu_nombre = usu_nombre;
    }

    public String getusu_apellido_paterno() { return usu_apellido_paterno; }
    public void  setusu_apellido_paterno(String usu_apellido_paterno) { this.usu_apellido_paterno = usu_apellido_paterno; }

    public String getusu_apellido_materno() { return usu_apellido_materno; }
    public void  setusu_apellido_materno(String usu_apellido_materno) { this.usu_apellido_materno = usu_apellido_materno; }

    public String getusu_correo_electronico() { return usu_correo_electronico; }
    public void  setusu_correo_electronico(String usu_correo_electronico) { this.usu_correo_electronico = usu_correo_electronico; }

    public String getusu_imagen_perfil() {
        return usu_imagen_perfil;
    }
    public void setusu_imagen_perfil(String usu_imagen_perfil) { this.usu_imagen_perfil = usu_imagen_perfil; }

    public String getusu_activo() { return usu_activo; }
    public void setusu_activo(String usu_activo) {
        this.usu_activo = usu_activo;
    }

    public String getusu_administrador() { return usu_administrador; }
    public void setusu_administrador(String usu_administrador) { this.usu_administrador = usu_administrador; }

    public String getapellidos() {
        return apellidos;
    }
    public void setapellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getusu_id() {
        return usu_id;
    }
    public void setusu_id(String usu_id) {
        this.usu_id = usu_id;
    }


}
