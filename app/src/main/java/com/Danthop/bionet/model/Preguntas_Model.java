package com.Danthop.bionet.model;

public class Preguntas_Model {

    private String Preguntas;
    private String Comprador;
    private String Titulo;
    private String IDdpregunta;

    public Preguntas_Model(String preguntas, String comprador, String titulo,String idpregunta) {
        Preguntas = preguntas;
        Comprador = comprador;
        Titulo = titulo;
        IDdpregunta = idpregunta;
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
}
