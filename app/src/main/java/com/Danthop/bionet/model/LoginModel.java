package com.Danthop.bionet.model;

public class LoginModel {

    private String usuId;
    private String usuUsuario;
    private String usuContrasena;
    private String usuTipoContrasena;
    private String usuUsuarioBloquedado;
    private String usuNombre;
    private String usuApellidos;
    private String usuEmail;
    private String usuTelefono;
    private String usuCelular;
    private String usuFechaRegistro;
    private String usuFechaNacimiento;
    private Integer usuIdTarjetaSocio;
    private String usuCiudad;
    private String usuTipoDocumento;
    private String usuNoDocumento;
    private String usuContrasenaTemp;
    private String usuImagen;
    private Integer usuIdRol;
    private Integer usuEstatus;
    private String Estatus;
    private String Mensaje;
    private String createdAt;
    private String updatedAt;
    private String usu_activo;
    private String usu_administrador;
    private String cbn_nombre_negocio;
    private String usu_correo_electronico;
    private String usu_sucursales;
    //Campos nuevos para el SSO
    private String sso_usu_correo_electronico;
    private String sso_usurname;
    private String sso_nombre;
    private String sso_usu_Id;
    private String sso_usuario_activo;
    private String sso_code;
    private String sso_descripcion_licencia;
    private String sso_perpetua;
    private String sso_expira;
    private String sso_activa;
    private String sso_fecha_creacion;
    private String sso_fecha_expiracion;

    private String sso_token;
    private String sso_token_type;
    private String sso_refresh_Token;
    private String sso_expire;

    public String getUsuId() {
        return usuId;
    }

    public void setUsuId(String usuId) {
        this.usuId = usuId;
    }

    public String getUsuUsuario() {
        return usuUsuario;
    }

    public void setUsuUsuario(String usuUsuario) {
        this.usuUsuario = usuUsuario;
    }

    public String getUsuContrasena() {
        return usuContrasena;
    }

    public void setUsuContrasena(String usuContrasena) {
        this.usuContrasena = usuContrasena;
    }

    public String getUsuTipoContrasena() {
        return usuTipoContrasena;
    }

    public void setUsuTipoContrasena(String usuTipoContrasena) {
        this.usuTipoContrasena = usuTipoContrasena;
    }

    public String getUsuUsuarioBloquedado() {
        return usuUsuarioBloquedado;
    }

    public void setUsuUsuarioBloquedado(String usuUsuarioBloquedado) {
        this.usuUsuarioBloquedado = usuUsuarioBloquedado;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public String getUsuApellidos() {
        return usuApellidos;
    }

    public void setUsuApellidos(String usuApellidos) {
        this.usuApellidos = usuApellidos;
    }

    public String getUsuEmail() {
        return usuEmail;
    }

    public void setUsuEmail(String usuEmail) {
        this.usuEmail = usuEmail;
    }

    public String getUsuTelefono() {
        return usuTelefono;
    }

    public void setUsuTelefono(String usuTelefono) {
        this.usuTelefono = usuTelefono;
    }

    public String getUsuCelular() {
        return usuCelular;
    }

    public void setUsuCelular(String usuCelular) {
        this.usuCelular = usuCelular;
    }

    public String getUsuFechaRegistro() {
        return usuFechaRegistro;
    }

    public void setUsuFechaRegistro(String usuFechaRegistro) {
        this.usuFechaRegistro = usuFechaRegistro;
    }

    public String getUsuFechaNacimiento() {
        return usuFechaNacimiento;
    }

    public void setUsuFechaNacimiento(String usuFechaNacimiento) {
        this.usuFechaNacimiento = usuFechaNacimiento;
    }

    public Integer getUsuIdTarjetaSocio() {
        return usuIdTarjetaSocio;
    }

    public void setUsuIdTarjetaSocio(Integer usuIdTarjetaSocio) {
        this.usuIdTarjetaSocio = usuIdTarjetaSocio;
    }

    public String getUsuCiudad() {
        return usuCiudad;
    }

    public void setUsuCiudad(String usuCiudad) {
        this.usuCiudad = usuCiudad;
    }

    public String getUsuTipoDocumento() {
        return usuTipoDocumento;
    }

    public void setUsuTipoDocumento(String usuTipoDocumento) {
        this.usuTipoDocumento = usuTipoDocumento;
    }

    public String getUsuNoDocumento() {
        return usuNoDocumento;
    }

    public void setUsuNoDocumento(String usuNoDocumento) {
        this.usuNoDocumento = usuNoDocumento;
    }

    public String getUsuContrasenaTemp() {
        return usuContrasenaTemp;
    }

    public void setUsuContrasenaTemp(String usuContrasenaTemp) {
        this.usuContrasenaTemp = usuContrasenaTemp;
    }

    public String getUsuImagen() {
        return usuImagen;
    }

    public void setUsuImagen(String usuImagen) {
        this.usuImagen = usuImagen;
    }

    public Integer getUsuIdRol() {
        return usuIdRol;
    }

    public void setUsuIdRol(Integer usuIdRol) {
        this.usuIdRol = usuIdRol;
    }

    public Integer getUsuEstatus() {
        return usuEstatus;
    }

    public void setUsuEstatus(Integer usuEstatus) {
        this.usuEstatus = usuEstatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsu_activo() {
        return usu_activo;
    }

    public void setUsu_activo(String usu_activo) {
        this.usu_activo = usu_activo;
    }

    public String getUsu_administrador() {
        return usu_administrador;
    }

    public void setUsu_administrador(String usu_administrador) {
        this.usu_administrador = usu_administrador;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setEstatus(String estatus) {
        Estatus = estatus;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getcbn_nombre_negocio() {
        return cbn_nombre_negocio;
    }

    public void setcbn_nombre_negocio(String cbn_nombre_negocio) {
        this.cbn_nombre_negocio = cbn_nombre_negocio;
    }

    public String getusu_correo_electronico() {
        return usu_correo_electronico;
    }

    public void setusu_correo_electronico(String usu_correo_electronico) {
        this.usu_correo_electronico = usu_correo_electronico;
    }

    public String getusu_sucursales() {
        return usu_sucursales;
    }

    public void setusu_sucursales(String usu_sucursales) {
        this.usu_sucursales = usu_sucursales;
    }

    public String getSso_usu_correo_electronico() {
        return sso_usu_correo_electronico;
    }

    public void setSso_usu_correo_electronico(String sso_usu_correo_electronico) {
        this.sso_usu_correo_electronico = sso_usu_correo_electronico;
    }

    public String getSso_usurname() {
        return sso_usurname;
    }

    public void setSso_usurname(String sso_usurname) {
        this.sso_usurname = sso_usurname;
    }

    public String getSso_nombre() {
        return sso_nombre;
    }

    public void setSso_nombre(String sso_nombre) {
        this.sso_nombre = sso_nombre;
    }

    public String getSso_usu_Id() {
        return sso_usu_Id;
    }

    public void setSso_usu_Id(String sso_usu_Id) {
        this.sso_usu_Id = sso_usu_Id;
    }

    public String getSso_usuario_activo() {
        return sso_usuario_activo;
    }

    public void setSso_usuario_activo(String sso_usuario_activo) {
        this.sso_usuario_activo = sso_usuario_activo;
    }

    public String getSso_code() {
        return sso_code;
    }

    public void setSso_code(String sso_code) {
        this.sso_code = sso_code;
    }

    public String getSso_descripcion_licencia() {
        return sso_descripcion_licencia;
    }

    public void setSso_descripcion_licencia(String sso_descripcion_licencia) {
        this.sso_descripcion_licencia = sso_descripcion_licencia;
    }

    public String getSso_perpetua() {
        return sso_perpetua;
    }

    public void setSso_perpetua(String sso_perpetua) {
        this.sso_perpetua = sso_perpetua;
    }

    public String getSso_expira() {
        return sso_expira;
    }

    public void setSso_expira(String sso_expira) {
        this.sso_expira = sso_expira;
    }

    public String getSso_activa() {
        return sso_activa;
    }

    public void setSso_activa(String sso_activa) {
        this.sso_activa = sso_activa;
    }

    public String getSso_fecha_creacion() {
        return sso_fecha_creacion;
    }

    public void setSso_fecha_creacion(String sso_fecha_creacion) {
        this.sso_fecha_creacion = sso_fecha_creacion;
    }

    public String getSso_fecha_expiracion() {
        return sso_fecha_expiracion;
    }

    public void setSso_fecha_expiracion(String sso_fecha_expiracion) {
        this.sso_fecha_expiracion = sso_fecha_expiracion;
    }

    public String getSso_token() {
        return sso_token;
    }

    public void setSso_token(String sso_token) {
        this.sso_token = sso_token;
    }

    public String getSso_token_type() {
        return sso_token_type;
    }

    public void setSso_token_type(String sso_token_type) {
        this.sso_token_type = sso_token_type;
    }

    public String getSso_expire() {
        return sso_expire;
    }

    public void setSso_expire(String sso_expire) {
        this.sso_expire = sso_expire;
    }

    public String getSso_refresh_Token() {
        return sso_refresh_Token;
    }

    public void setSso_refresh_Token(String sso_refresh_Token) {
        this.sso_refresh_Token = sso_refresh_Token;
    }
}

