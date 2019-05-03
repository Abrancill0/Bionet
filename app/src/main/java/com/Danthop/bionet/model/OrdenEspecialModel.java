package com.Danthop.bionet.model;

import java.util.List;

public class OrdenEspecialModel {
    String Orden_ticket_id;
    String Orden_cliente_id;
    String Orden_nombre_cliente;
    String Orden_sucursal_id;
    String Orden_nombre_sucursal;
    String Orden_importe_pagado;
    String Orden_importe_restante;
    String Fecha_vencimiento;
    String Fecha_creacion;
    String Orden_estatus;
    List<OrdenEspecialArticuloModel> ArticulosOrdenados;

    public OrdenEspecialModel(String orden_ticket_id, String orden_cliente_id, String orden_nombre_cliente, String orden_sucursal_id, String orden_nombre_sucursal, String orden_importe_pagado, String orden_importe_restante, String fecha_vencimiento, String fecha_creacion, String orden_estatus, List<OrdenEspecialArticuloModel> articulosOrdenados) {
        Orden_ticket_id = orden_ticket_id;
        Orden_cliente_id = orden_cliente_id;
        Orden_nombre_cliente = orden_nombre_cliente;
        Orden_sucursal_id = orden_sucursal_id;
        Orden_nombre_sucursal = orden_nombre_sucursal;
        Orden_importe_pagado = orden_importe_pagado;
        Orden_importe_restante = orden_importe_restante;
        Fecha_vencimiento = fecha_vencimiento;
        Fecha_creacion = fecha_creacion;
        ArticulosOrdenados = articulosOrdenados;
        Orden_estatus = orden_estatus;
    }

    public String getOrden_ticket_id() {
        return Orden_ticket_id;
    }

    public void setOrden_ticket_id(String apartado_ticket_id) {
        Orden_ticket_id = apartado_ticket_id;
    }

    public String getOrden_cliente_id() {
        return Orden_cliente_id;
    }

    public void setOrden_cliente_id(String orden_cliente_id) {
        Orden_cliente_id = orden_cliente_id;
    }

    public String getOrden_sucursal_id() {
        return Orden_sucursal_id;
    }

    public void setOrden_sucursal_id(String orden_sucursal_id) {
        Orden_sucursal_id = orden_sucursal_id;
    }

    public String getOrden_importe_pagado() {
        return Orden_importe_pagado;
    }

    public void setOrden_importe_pagado(String orden_importe_pagado) {
        Orden_importe_pagado = orden_importe_pagado;
    }

    public String getOrden_importe_restante() {
        return Orden_importe_restante;
    }

    public void setOrden_importe_restante(String orden_importe_restante) {
        Orden_importe_restante = orden_importe_restante;
    }

    public String getFecha_vencimiento() {
        return Fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        Fecha_vencimiento = fecha_vencimiento;
    }

    public List<OrdenEspecialArticuloModel> getArticulosOrdenados() {
        return ArticulosOrdenados;
    }

    public void setArticulosOrdenados(List<OrdenEspecialArticuloModel> articulosApartados) {
        ArticulosOrdenados = articulosApartados;
    }

    public String getOrden_nombre_cliente() {
        return Orden_nombre_cliente;
    }

    public void setOrden_nombre_cliente(String orden_nombre_cliente) {
        Orden_nombre_cliente = orden_nombre_cliente;
    }

    public String getOrden_nombre_sucursal() {
        return Orden_nombre_sucursal;
    }

    public void setOrden_nombre_sucursal(String orden_nombre_sucursal) {
        Orden_nombre_sucursal = orden_nombre_sucursal;
    }

    public String getFecha_creacion() {
        return Fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        Fecha_creacion = fecha_creacion;
    }

    public String getOrden_estatus() {
        return Orden_estatus;
    }

    public void setOrden_estatus(String orden_estatus) {
        Orden_estatus = orden_estatus;
    }
}
