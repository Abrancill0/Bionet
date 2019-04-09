package com.Danthop.bionet.model;

public class TicketModel {

    private String tic_id;
    private String tic_comision;
    private String tic_direccion_entrega;
    private String tic_facturar;
    private String tic_fecha_hora_creo;
    private String tic_fecha_hora_modifico;
    private String tic_fecha_hora_pago;
    private String tic_fecha_hora_venta;
    private String tic_folio_facturacion;
    private String tic_id_cliente;
    private String tic_id_corte;
    private String tic_id_cuenta_bionet;
    private String tic_id_devolucion;
    private String tic_id_devolucion_pago;
    private String tic_id_estatus;
    private String tic_id_factura;
    private String tic_id_forma_pago;
    private String tic_id_sucursal;
    private String tic_id_usuario_creo;
    private String tic_id_usuario_modifico;
    private String tic_id_vale;
    private String tic_id_vendedor;
    private String tic_importe_cambio;
    private String tic_importe_cobrado;
    private String tic_importe_descuentos;
    private String tic_importe_pagado;
    private String tic_importe_pagado_devolucion;
    private String tic_importe_pagado_puntos_lealtad;
    private String tic_importe_pagado_vale;
    private String tic_importe_recibido;
    private String tic_importe_total;
    private String tic_impuestos;
    private String tic_nombre_cliente;
    private String tic_nombre_vendedor;
    private String tic_numero;
    private String tic_puntos_lealtad_consumidos;
    private String tic_puntos_lealtad_obtenidos;
    private String tic_tiene_apartados;
    private String tic_tiene_ordenes_especiales;


    public TicketModel(String tic_id, String tic_comision,
                       String tic_direccion_entrega,
                       String tic_facturar,
                       String tic_fecha_hora_creo,
                       String tic_fecha_hora_modifico,
                       String tic_fecha_hora_pago,
                       String tic_fecha_hora_venta,
                       String tic_folio_facturacion,
                       String tic_id_cliente,
                       String tic_id_corte,
                       String tic_id_cuenta_bionet,
                       String tic_id_devolucion,
                       String tic_id_devolucion_pago,
                       String tic_id_estatus,
                       String tic_id_factura,
                       String tic_id_forma_pago,
                       String tic_id_sucursal,
                       String tic_id_usuario_creo,
                       String tic_id_usuario_modifico,
                       String tic_id_vale,
                       String tic_id_vendedor,
                       String tic_importe_cambio,
                       String tic_importe_cobrado,
                       String tic_importe_descuentos,
                       String tic_importe_pagado,
                       String tic_importe_pagado_devolucion,
                       String tic_importe_pagado_puntos_lealtad,
                       String tic_importe_pagado_vale,
                       String tic_importe_recibido,
                       String tic_importe_total,
                       String tic_impuestos,
                       String tic_nombre_cliente,
                       String tic_nombre_vendedor,
                       String tic_numero,
                       String tic_puntos_lealtad_consumidos,
                       String tic_puntos_lealtad_obtenidos,
                       String tic_tiene_apartados,
                       String tic_tiene_ordenes_especiales) {
        this.tic_id = tic_id;
        this.tic_comision = tic_comision;
        this.tic_direccion_entrega = tic_direccion_entrega;
        this.tic_facturar = tic_facturar;
        this.tic_fecha_hora_creo = tic_fecha_hora_creo;
        this.tic_fecha_hora_modifico = tic_fecha_hora_modifico;
        this.tic_fecha_hora_pago = tic_fecha_hora_pago;
        this.tic_fecha_hora_venta = tic_fecha_hora_venta;
        this.tic_folio_facturacion = tic_folio_facturacion;
        this.tic_id_cliente = tic_id_cliente;
        this.tic_id_corte = tic_id_corte;
        this.tic_id_cuenta_bionet = tic_id_cuenta_bionet;
        this.tic_id_devolucion = tic_id_devolucion;
        this.tic_id_devolucion_pago = tic_id_devolucion_pago;
        this.tic_id_estatus = tic_id_estatus;
        this.tic_id_factura = tic_id_factura;
        this.tic_id_forma_pago = tic_id_forma_pago;
        this.tic_id_sucursal = tic_id_sucursal;
        this.tic_id_usuario_creo = tic_id_usuario_creo;
        this.tic_id_usuario_modifico = tic_id_usuario_modifico;
        this.tic_id_vale = tic_id_vale;
        this.tic_id_vendedor = tic_id_vendedor;
        this.tic_importe_cambio = tic_importe_cambio;
        this.tic_importe_cobrado = tic_importe_cobrado;
        this.tic_importe_descuentos = tic_importe_descuentos;
        this.tic_importe_pagado = tic_importe_pagado;
        this.tic_importe_pagado_devolucion = tic_importe_pagado_devolucion;
        this.tic_importe_pagado_puntos_lealtad = tic_importe_pagado_puntos_lealtad;
        this.tic_importe_pagado_vale = tic_importe_pagado_vale;
        this.tic_importe_recibido = tic_importe_recibido;
        this.tic_importe_total = tic_importe_total;
        this.tic_impuestos = tic_impuestos;
        this.tic_nombre_cliente = tic_nombre_cliente;
        this.tic_nombre_vendedor = tic_nombre_vendedor;
        this.tic_numero = tic_numero;
        this.tic_puntos_lealtad_consumidos = tic_puntos_lealtad_consumidos;
        this.tic_puntos_lealtad_obtenidos = tic_puntos_lealtad_obtenidos;
        this.tic_tiene_apartados = tic_tiene_apartados;
        this.tic_tiene_ordenes_especiales = tic_tiene_ordenes_especiales;
    }

    public String getTic_id() {
        return tic_id;
    }

    public void setTic_id(String tic_id) {
        this.tic_id = tic_id;
    }

    public String getTic_comision() {
        return tic_comision;
    }

    public void setTic_comision(String tic_comision) {
        this.tic_comision = tic_comision;
    }

    public String getTic_direccion_entrega() {
        return tic_direccion_entrega;
    }

    public void setTic_direccion_entrega(String tic_direccion_entrega) {
        this.tic_direccion_entrega = tic_direccion_entrega;
    }

    public String getTic_facturar() {
        return tic_facturar;
    }

    public void setTic_facturar(String tic_facturar) {
        this.tic_facturar = tic_facturar;
    }

    public String getTic_fecha_hora_creo() {
        return tic_fecha_hora_creo;
    }

    public void setTic_fecha_hora_creo(String tic_fecha_hora_creo) {
        this.tic_fecha_hora_creo = tic_fecha_hora_creo;
    }

    public String getTic_fecha_hora_modifico() {
        return tic_fecha_hora_modifico;
    }

    public void setTic_fecha_hora_modifico(String tic_fecha_hora_modifico) {
        this.tic_fecha_hora_modifico = tic_fecha_hora_modifico;
    }

    public String getTic_fecha_hora_pago() {
        return tic_fecha_hora_pago;
    }

    public void setTic_fecha_hora_pago(String tic_fecha_hora_pago) {
        this.tic_fecha_hora_pago = tic_fecha_hora_pago;
    }

    public String getTic_fecha_hora_venta() {
        return tic_fecha_hora_venta;
    }

    public void setTic_fecha_hora_venta(String tic_fecha_hora_venta) {
        this.tic_fecha_hora_venta = tic_fecha_hora_venta;
    }

    public String getTic_folio_facturacion() {
        return tic_folio_facturacion;
    }

    public void setTic_folio_facturacion(String tic_folio_facturacion) {
        this.tic_folio_facturacion = tic_folio_facturacion;
    }

    public String getTic_id_cliente() {
        return tic_id_cliente;
    }

    public void setTic_id_cliente(String tic_id_cliente) {
        this.tic_id_cliente = tic_id_cliente;
    }

    public String getTic_id_corte() {
        return tic_id_corte;
    }

    public void setTic_id_corte(String tic_id_corte) {
        this.tic_id_corte = tic_id_corte;
    }

    public String getTic_id_cuenta_bionet() {
        return tic_id_cuenta_bionet;
    }

    public void setTic_id_cuenta_bionet(String tic_id_cuenta_bionet) {
        this.tic_id_cuenta_bionet = tic_id_cuenta_bionet;
    }

    public String getTic_id_devolucion() {
        return tic_id_devolucion;
    }

    public void setTic_id_devolucion(String tic_id_devolucion) {
        this.tic_id_devolucion = tic_id_devolucion;
    }

    public String getTic_id_devolucion_pago() {
        return tic_id_devolucion_pago;
    }

    public void setTic_id_devolucion_pago(String tic_id_devolucion_pago) {
        this.tic_id_devolucion_pago = tic_id_devolucion_pago;
    }

    public String getTic_id_estatus() {
        return tic_id_estatus;
    }

    public void setTic_id_estatus(String tic_id_estatus) {
        this.tic_id_estatus = tic_id_estatus;
    }

    public String getTic_id_factura() {
        return tic_id_factura;
    }

    public void setTic_id_factura(String tic_id_factura) {
        this.tic_id_factura = tic_id_factura;
    }

    public String getTic_id_forma_pago() {
        return tic_id_forma_pago;
    }

    public void setTic_id_forma_pago(String tic_id_forma_pago) {
        this.tic_id_forma_pago = tic_id_forma_pago;
    }

    public String getTic_id_sucursal() {
        return tic_id_sucursal;
    }

    public void setTic_id_sucursal(String tic_id_sucursal) {
        this.tic_id_sucursal = tic_id_sucursal;
    }

    public String getTic_id_usuario_creo() {
        return tic_id_usuario_creo;
    }

    public void setTic_id_usuario_creo(String tic_id_usuario_creo) {
        this.tic_id_usuario_creo = tic_id_usuario_creo;
    }

    public String getTic_id_usuario_modifico() {
        return tic_id_usuario_modifico;
    }

    public void setTic_id_usuario_modifico(String tic_id_usuario_modifico) {
        this.tic_id_usuario_modifico = tic_id_usuario_modifico;
    }

    public String getTic_id_vale() {
        return tic_id_vale;
    }

    public void setTic_id_vale(String tic_id_vale) {
        this.tic_id_vale = tic_id_vale;
    }

    public String getTic_id_vendedor() {
        return tic_id_vendedor;
    }

    public void setTic_id_vendedor(String tic_id_vendedor) {
        this.tic_id_vendedor = tic_id_vendedor;
    }

    public String getTic_importe_cambio() {
        return tic_importe_cambio;
    }

    public void setTic_importe_cambio(String tic_importe_cambio) {
        this.tic_importe_cambio = tic_importe_cambio;
    }

    public String getTic_importe_cobrado() {
        return tic_importe_cobrado;
    }

    public void setTic_importe_cobrado(String tic_importe_cobrado) {
        this.tic_importe_cobrado = tic_importe_cobrado;
    }

    public String getTic_importe_descuentos() {
        return tic_importe_descuentos;
    }

    public void setTic_importe_descuentos(String tic_importe_descuentos) {
        this.tic_importe_descuentos = tic_importe_descuentos;
    }

    public String getTic_importe_pagado() {
        return tic_importe_pagado;
    }

    public void setTic_importe_pagado(String tic_importe_pagado) {
        this.tic_importe_pagado = tic_importe_pagado;
    }

    public String getTic_importe_pagado_devolucion() {
        return tic_importe_pagado_devolucion;
    }

    public void setTic_importe_pagado_devolucion(String tic_importe_pagado_devolucion) {
        this.tic_importe_pagado_devolucion = tic_importe_pagado_devolucion;
    }

    public String getTic_importe_pagado_puntos_lealtad() {
        return tic_importe_pagado_puntos_lealtad;
    }

    public void setTic_importe_pagado_puntos_lealtad(String tic_importe_pagado_puntos_lealtad) {
        this.tic_importe_pagado_puntos_lealtad = tic_importe_pagado_puntos_lealtad;
    }

    public String getTic_importe_pagado_vale() {
        return tic_importe_pagado_vale;
    }

    public void setTic_importe_pagado_vale(String tic_importe_pagado_vale) {
        this.tic_importe_pagado_vale = tic_importe_pagado_vale;
    }

    public String getTic_importe_recibido() {
        return tic_importe_recibido;
    }

    public void setTic_importe_recibido(String tic_importe_recibido) {
        this.tic_importe_recibido = tic_importe_recibido;
    }

    public String getTic_importe_total() {
        return tic_importe_total;
    }

    public void setTic_importe_total(String tic_importe_total) {
        this.tic_importe_total = tic_importe_total;
    }

    public String getTic_impuestos() {
        return tic_impuestos;
    }

    public void setTic_impuestos(String tic_impuestos) {
        this.tic_impuestos = tic_impuestos;
    }

    public String getTic_nombre_cliente() {
        return tic_nombre_cliente;
    }

    public void setTic_nombre_cliente(String tic_nombre_cliente) {
        this.tic_nombre_cliente = tic_nombre_cliente;
    }

    public String getTic_nombre_vendedor() {
        return tic_nombre_vendedor;
    }

    public void setTic_nombre_vendedor(String tic_nombre_vendedor) {
        this.tic_nombre_vendedor = tic_nombre_vendedor;
    }

    public String getTic_numero() {
        return tic_numero;
    }

    public void setTic_numero(String tic_numero) {
        this.tic_numero = tic_numero;
    }

    public String getTic_puntos_lealtad_consumidos() {
        return tic_puntos_lealtad_consumidos;
    }

    public void setTic_puntos_lealtad_consumidos(String tic_puntos_lealtad_consumidos) {
        this.tic_puntos_lealtad_consumidos = tic_puntos_lealtad_consumidos;
    }

    public String getTic_puntos_lealtad_obtenidos() {
        return tic_puntos_lealtad_obtenidos;
    }

    public void setTic_puntos_lealtad_obtenidos(String tic_puntos_lealtad_obtenidos) {
        this.tic_puntos_lealtad_obtenidos = tic_puntos_lealtad_obtenidos;
    }

    public String getTic_tiene_apartados() {
        return tic_tiene_apartados;
    }

    public void setTic_tiene_apartados(String tic_tiene_apartados) {
        this.tic_tiene_apartados = tic_tiene_apartados;
    }

    public String getTic_tiene_ordenes_especiales() {
        return tic_tiene_ordenes_especiales;
    }

    public void setTic_tiene_ordenes_especiales(String tic_tiene_ordenes_especiales) {
        this.tic_tiene_ordenes_especiales = tic_tiene_ordenes_especiales;
    }
}
