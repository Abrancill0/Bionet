package com.Danthop.bionet.model;

import java.util.function.Function;

public class ClienteFrecuenteModel {

    private String nombre_cliente;
    private String NumTicket;
    private String UUIDTicketCliente;
    private String tar_nombre_articulo;
    private String tar_id_articulo;

    private String nen_titulo;
    private String nen_fecha_hora_creo;

    public ClienteFrecuenteModel(String nombre_cliente, String NumTicket, String UUIDTicketCliente, String tar_nombre_articulo, String tar_id_articulo,
                                    String nen_titulo, String nen_fecha_hora_creo)
    {
        this.nombre_cliente = nombre_cliente;
        this.NumTicket = NumTicket;
        this.UUIDTicketCliente = UUIDTicketCliente;
        this.tar_nombre_articulo = tar_nombre_articulo;
        this.tar_id_articulo = tar_id_articulo;
        this.nen_titulo = nen_titulo;
        this.nen_fecha_hora_creo = nen_fecha_hora_creo;

    }

    public String getnombre_cliente() {return nombre_cliente; }
    public void setnombre_cliente(String nombre_cliente) { this.nombre_cliente = nombre_cliente; }

    public String getNumTicket() {return NumTicket; }
    public void setNumTicket(String NumTicket) { this.NumTicket = NumTicket; }

    public String getUUIDTicketCliente() {return UUIDTicketCliente; }
    public void setUUIDTicketCliente(String UUIDTicketCliente) { this.UUIDTicketCliente = UUIDTicketCliente; }

    public String gettar_nombre_articulo() {return tar_nombre_articulo; }
    public void settar_nombre_articulo(String tar_nombre_articulo) { this.tar_nombre_articulo = tar_nombre_articulo; }

    public String gettar_id_articulo() {return tar_id_articulo; }
    public void settar_id_articulo(String tar_id_articulo) { this.tar_id_articulo = tar_id_articulo; }

    public  String getnen_titulo() { return  nen_titulo; }
    public void setnen_titulo(String nen_titulo) { this.nen_titulo = nen_titulo; }

    public String getnen_fecha_hora_creo() { return  nen_fecha_hora_creo; }
    public void setnen_fecha_hora_creo(String nen_fecha_hora_creo) { this.nen_fecha_hora_creo=nen_fecha_hora_creo; }
}
