package com.Danthop.bionet.model;

public class Preguntas_Model {

    private String Preguntas;
    private String Comprador;
    private String Titulo;
    private String Token;
    private String IDdpregunta;
    private String UserML;
    private String IDComprador;

    public Preguntas_Model(String preguntas, String comprador, String titulo,String idpregunta,String token,String userML,String idComprador) {
        Preguntas = preguntas;
        Comprador = comprador;
        Titulo = titulo;
        IDdpregunta = idpregunta;
        Token = token;
        UserML = userML;
        IDComprador= idComprador;
    }

    public String getPreguntas() {
        return Preguntas;
    }

    public void setPreguntas(String preguntas) {
        Preguntas = preguntas;
    }

    public String getComprador() {
        return Comprador;
    }

    public void setComprador(String comprador) {
        Comprador = comprador;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public void setidpregunta(String idpregunta) {
        IDdpregunta = idpregunta;
    }

    public String getidpregunta() {
        return IDdpregunta;
    }

    public void settoken(String token) {
        Token = token;
    }

    public String gettoken() {
        return Token;
    }

    public void setUserML(String usurml) {
        UserML = usurml;
    }

    public String getUserML() {
        return UserML;
    }

    public void setIDComprador(String idComprador) {
        IDComprador = idComprador;
    }

    public String getIDComprador() {
        return IDComprador;
    }

}
