package com.Danthop.bionet.model;

import java.util.List;

public class ArticuloApartadoModel {

    String Cantidad;
    String Articulo_id;
    String Articulo_id_variante;
    String Articulo_id_modificador;
    String Importe_pagado;
    String Importe_restante;
    String Nombre_articulo;
    String Id_existencias_origen;
    String Aplica_para_devolucion;
    String Importe_descuento;
    String Importe_total;
    List<com.Danthop.bionet.model.Impuestos> Impuestos;
    String Porcentaje_descuento;
    String Precio_articulo;
    String codigo_sat;
    String um_codigo;
    String um_simbologia;

    public ArticuloApartadoModel(String cantidad, String articulo_id, String articulo_id_variante,
                                 String articulo_id_modificador, String importe_pagado, String importe_restante,
                                 String nombre_articulo, String id_existencias_origen, String aplica_para_devolucion,
                                 String importe_descuento, String importe_total, List<com.Danthop.bionet.model.Impuestos>  impuestos, String porcentaje_descuento,
                                 String precio_articulo) {
        Articulo_id = articulo_id;
        Articulo_id_variante = articulo_id_variante;
        Articulo_id_modificador = articulo_id_modificador;
        Importe_pagado = importe_pagado;
        Importe_restante = importe_restante;
        Nombre_articulo = nombre_articulo;
        Id_existencias_origen = id_existencias_origen;
        Aplica_para_devolucion = aplica_para_devolucion;
        Importe_descuento = importe_descuento;
        Importe_total = importe_total;
        Impuestos = impuestos;
        Porcentaje_descuento = porcentaje_descuento;
        Precio_articulo = precio_articulo;
        Cantidad = cantidad;
    }


    public String getArticulo_id() {
        return Articulo_id;
    }

    public void setArticulo_id(String articulo_id) {
        Articulo_id = articulo_id;
    }

    public String getArticulo_id_variante() {
        return Articulo_id_variante;
    }

    public void setArticulo_id_variante(String articulo_id_variante) {
        Articulo_id_variante = articulo_id_variante;
    }

    public String getArticulo_id_modificador() {
        return Articulo_id_modificador;
    }

    public void setArticulo_id_modificador(String articulo_id_modificador) {
        Articulo_id_modificador = articulo_id_modificador;
    }

    public String getImporte_pagado() {
        return Importe_pagado;
    }

    public void setImporte_pagado(String importe_pagado) {
        Importe_pagado = importe_pagado;
    }

    public String getImporte_restante() {
        return Importe_restante;
    }

    public void setImporte_restante(String importe_restante) {
        Importe_restante = importe_restante;
    }

    public String getNombre_articulo() {
        return Nombre_articulo;
    }

    public void setNombre_articulo(String nombre_articulo) {
        Nombre_articulo = nombre_articulo;
    }

    public String getId_existencias_origen() {
        return Id_existencias_origen;
    }

    public void setId_existencias_origen(String id_existencias_origen) {
        Id_existencias_origen = id_existencias_origen;
    }

    public String getAplica_para_devolucion() {
        return Aplica_para_devolucion;
    }

    public void setAplica_para_devolucion(String aplica_para_devolucion) {
        Aplica_para_devolucion = aplica_para_devolucion;
    }

    public String getImporte_descuento() {
        return Importe_descuento;
    }

    public void setImporte_descuento(String importe_descuento) {
        Importe_descuento = importe_descuento;
    }

    public String getImporte_total() {
        return Importe_total;
    }

    public void setImporte_total(String importe_total) {
        Importe_total = importe_total;
    }

    public List<com.Danthop.bionet.model.Impuestos>  getImpuestos() {
        return Impuestos;
    }

    public void setImpuestos(List<com.Danthop.bionet.model.Impuestos>  impuestos) {
        Impuestos = impuestos;
    }

    public String getPorcentaje_descuento() {
        return Porcentaje_descuento;
    }

    public void setPorcentaje_descuento(String porcentaje_descuento) {
        Porcentaje_descuento = porcentaje_descuento;
    }

    public String getPrecio_articulo() {
        return Precio_articulo;
    }

    public void setPrecio_articulo(String precio_articulo) {
        Precio_articulo = precio_articulo;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getCodigo_sat() {
        return codigo_sat;
    }

    public void setCodigo_sat(String codigo_sat) {
        this.codigo_sat = codigo_sat;
    }

    public String getUm_codigo() {
        return um_codigo;
    }

    public void setUm_codigo(String um_codigo) {
        this.um_codigo = um_codigo;
    }

    public String getUm_simbologia() {
        return um_simbologia;
    }

    public void setUm_simbologia(String um_simbologia) {
        this.um_simbologia = um_simbologia;
    }
}
