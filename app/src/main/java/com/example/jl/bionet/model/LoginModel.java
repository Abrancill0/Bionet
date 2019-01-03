package com.example.jl.bionet.model;

public class LoginModel {

    public class Resultado {

        private int usu_id;
        private String usu_usuario;
        private String usu_contrasena;
        private String usu_tipo_contrasena;
        private String usu_usuario_bloquedado;
        private String usu_nombre;
        private String usu_apellidos;
        private String usu_email;
        private String usu_telefono;
        private String usu_celular ;
        private String usu_fecha_registro;
        private String usu_fecha_nacimiento;
        private int usu_id_tarjeta_socio;
        private String usu_imagen;
        private String usu_ciudad;
        private int usu_id_rol;
        private int usu_estatus;
        private String created_at;
        private String updated_at;
        private String usu_tipo_documento;
        private String usu_contrasena_temp;
        private String usu_no_documento;


        public int getUsu_id() {
            return usu_id;
        }

        public void setUsu_id(int usu_id) {
            this.usu_id = usu_id;
        }

        public String getUsu_usuario() {
            return usu_usuario;
        }

        public void setUsu_usuario(String usu_usuario) {
            this.usu_usuario = usu_usuario;
        }

        public String getUsu_contrasena() {
            return usu_contrasena;
        }

        public void setUsu_contrasena(String usu_contrasena) {
            this.usu_contrasena = usu_contrasena;
        }

        public String getUsu_tipo_contrasena() {
            return usu_tipo_contrasena;
        }

        public void setUsu_tipo_contrasena(String usu_tipo_contrasena) {
            this.usu_tipo_contrasena = usu_tipo_contrasena;
        }

        public String getUsu_usuario_bloquedado() {
            return usu_usuario_bloquedado;
        }

        public void setUsu_usuario_bloquedado(String usu_usuario_bloquedado) {
            this.usu_usuario_bloquedado = usu_usuario_bloquedado;
        }

        public String getUsu_nombre() {
            return usu_nombre;
        }

        public void setUsu_nombre(String usu_nombre) {
            this.usu_nombre = usu_nombre;
        }

        public String getUsu_apellidos() {
            return usu_apellidos;
        }

        public void setUsu_apellidos(String usu_apellidos) {
            this.usu_apellidos = usu_apellidos;
        }

        public String getUsu_email() {
            return usu_email;
        }

        public void setUsu_email(String usu_email) {
            this.usu_email = usu_email;
        }

        public String getUsu_telefono() {
            return usu_telefono;
        }

        public void setUsu_telefono(String usu_telefono) {
            this.usu_telefono = usu_telefono;
        }

        public String getUsu_celular() {
            return usu_celular;
        }

        public void setUsu_celular(String usu_celular) {
            this.usu_celular = usu_celular;
        }

        public String getUsu_fecha_registro() {
            return usu_fecha_registro;
        }

        public void setUsu_fecha_registro(String usu_fecha_registro) {
            this.usu_fecha_registro = usu_fecha_registro;
        }

        public String getUsu_fecha_nacimiento() {
            return usu_fecha_nacimiento;
        }

        public void setUsu_fecha_nacimiento(String usu_fecha_nacimiento) {
            this.usu_fecha_nacimiento = usu_fecha_nacimiento;
        }

        public int getUsu_id_tarjeta_socio() {
            return usu_id_tarjeta_socio;
        }

        public void setUsu_id_tarjeta_socio(int usu_id_tarjeta_socio) {
            this.usu_id_tarjeta_socio = usu_id_tarjeta_socio;
        }

        public String getUsu_imagen() {
            return usu_imagen;
        }

        public void setUsu_imagen(String usu_imagen) {
            this.usu_imagen = usu_imagen;
        }

        public String getUsu_ciudad() {
            return usu_ciudad;
        }

        public void setUsu_ciudad(String usu_ciudad) {
            this.usu_ciudad = usu_ciudad;
        }

        public int getUsu_id_rol() {
            return usu_id_rol;
        }

        public void setUsu_id_rol(int usu_id_rol) {
            this.usu_id_rol = usu_id_rol;
        }

        public int getUsu_estatus() {
            return usu_estatus;
        }

        public void setUsu_estatus(int usu_estatus) {
            this.usu_estatus = usu_estatus;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getUsu_tipo_documento() {
            return usu_tipo_documento;
        }

        public void setUsu_tipo_documento(String usu_tipo_documento) {
            this.usu_tipo_documento = usu_tipo_documento;
        }

        public String getUsu_contrasena_temp() {
            return usu_contrasena_temp;
        }

        public void setUsu_contrasena_temp(String usu_contrasena_temp) {
            this.usu_contrasena_temp = usu_contrasena_temp;
        }

        public String getUsu_no_documento() {
            return usu_no_documento;
        }

        public void setUsu_no_documento(String usu_no_documento) {
            this.usu_no_documento = usu_no_documento;
        }
    }

    public class LoginReturn {
        private int estatus;
        private String mensaje;
        private Resultado resultado;
    }

}
