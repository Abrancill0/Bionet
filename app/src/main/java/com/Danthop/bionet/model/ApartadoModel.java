package com.Danthop.bionet.model;

import java.util.List;

public class ApartadoModel {
    String Apartado_ticket_id;
    String Apartado_cliente_id;
    String Apartado_nombre_cliente;
    String Apartado_sucursal_id;
    String Apartado_nombre_sucursal;
    String Apartado_importe_pagado;
    String Apartado_importe_restante;
    String Fecha_vencimiento;
    String Fecha_creacion;
    String Apartado_estatus;
    List<ArticuloApartadoModel> ArticulosApartados;

    public ApartadoModel(String apartado_ticket_id, String apartado_cliente_id, String apartado_nombre_cliente, String apartado_sucursal_id, String apartado_nombre_sucursal,String apartado_importe_pagado, String apartado_importe_restante, String fecha_vencimiento,String fecha_creacion,String apartado_estatus, List<ArticuloApartadoModel> articulosApartados) {
        Apartado_ticket_id = apartado_ticket_id;
        Apartado_cliente_id = apartado_cliente_id;
        Apartado_nombre_cliente = apartado_nombre_cliente;
        Apartado_sucursal_id = apartado_sucursal_id;
        Apartado_nombre_sucursal = apartado_nombre_sucursal;
        Apartado_importe_pagado = apartado_importe_pagado;
        Apartado_importe_restante = apartado_importe_restante;
        Fecha_vencimiento = fecha_vencimiento;
        Fecha_creacion = fecha_creacion;
        ArticulosApartados = articulosApartados;
        Apartado_estatus = apartado_estatus;
    }

    public String getApartado_ticket_id() {
        return Apartado_ticket_id;
    }

    public void setApartado_ticket_id(String apartado_ticket_id) {
        Apartado_ticket_id = apartado_ticket_id;
    }

    public String getApartado_cliente_id() {
        return Apartado_cliente_id;
    }

    public void setApartado_cliente_id(String apartado_cliente_id) {
        Apartado_cliente_id = apartado_cliente_id;
    }

    public String getApartado_sucursal_id() {
        return Apartado_sucursal_id;
    }

    public void setApartado_sucursal_id(String apartado_sucursal_id) {
        Apartado_sucursal_id = apartado_sucursal_id;
    }

    public String getApartado_importe_pagado() {
        return Apartado_importe_pagado;
    }

    public void setApartado_importe_pagado(String apartado_importe_pagado) {
        Apartado_importe_pagado = apartado_importe_pagado;
    }

    public String getApartado_importe_restante() {
        return Apartado_importe_restante;
    }

    public void setApartado_importe_restante(String apartado_importe_restante) {
        Apartado_importe_restante = apartado_importe_restante;
    }

    public String getFecha_vencimiento() {
        return Fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        Fecha_vencimiento = fecha_vencimiento;
    }

    public List<ArticuloApartadoModel> getArticulosApartados() {
        return ArticulosApartados;
    }

    public void setArticulosApartados(List<ArticuloApartadoModel> articulosApartados) {
        ArticulosApartados = articulosApartados;
    }

    public String getApartado_nombre_cliente() {
        return Apartado_nombre_cliente;
    }

    public void setApartado_nombre_cliente(String apartado_nombre_cliente) {
        Apartado_nombre_cliente = apartado_nombre_cliente;
    }

    public String getApartado_nombre_sucursal() {
        return Apartado_nombre_sucursal;
    }

    public void setApartado_nombre_sucursal(String apartado_nombre_sucursal) {
        Apartado_nombre_sucursal = apartado_nombre_sucursal;
    }

    public String getFecha_creacion() {
        return Fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        Fecha_creacion = fecha_creacion;
    }

    public String getApartado_estatus() {
        return Apartado_estatus;
    }

    public void setApartado_estatus(String apartado_estatus) {
        Apartado_estatus = apartado_estatus;
    }
}
