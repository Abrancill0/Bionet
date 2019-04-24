package com.Danthop.bionet.model;
import android.print.PrinterId;

public class InventarioModel {

    private String sku;
    private String producto;
    private String existencia;
    private String categoria;
    private String modificadores;
    private String nombre_sucursal;
    private String suc_id;
    private String art_descripcion;
    private String art_tipo;
    private String art_disponible_venta;
    private String art_disponible_compra;
    private String ava_aplica_apartados;
    private String ava_aplica_cambio_devolucion;
    private String aim_url;
    private String art_nombre;
    private String cat_nombre;
    private String his_tipo;
    private String his_cantidad;
    private String his_observaciones;
    private String his_fecha_hora_creo;
    private String codigoBarras;
    private String almacen;
    private String RecibidasOrigen;
    private String RecibidasDestino;
    private String tra_nombre_estatus;
    private String suc_numero_sucursal_destino;
    private String suc_numero_sucursal_origen;
    private String fechaSolicitud;
    private String tra_motivo;
    private String UUIDarticulo;
    private String UUIDvariante;
    private String UUIDmodificador;
    private String UUIDexistencias;



    public InventarioModel(String sku,
                           String producto,
                           String existencia,
                           String categoria,
                           String modificadores,
                           String nombre_sucursal,
                           String suc_id,
                           String art_descripcion,
                           String art_tipo,
                           String art_disponible_venta,
                           String art_disponible_compra,
                           String ava_aplica_apartados,
                           String ava_aplica_cambio_devolucion,
                           String aim_url,
                           String art_nombre,
                           String cat_nombre,
                           String his_tipo,
                           String his_cantidad,
                           String his_observaciones,
                           String his_fecha_hora_creo,
                           String codigoBarras,
                           String almacen,
                           String RecibidasOrigen,
                           String RecibidasDestino,
                           String tra_nombre_estatus,
                           String suc_numero_sucursal_destino,
                           String suc_numero_sucursal_origen,
                           String fechaSolicitud,
                           String tra_motivo,
                           String UUIDarticulo,
                           String UUIDvariante,
                           String UUIDmodificador,
                           String UUIDexistencias)
    {

        this.sku = sku;
        this.producto = producto;
        this.existencia = existencia;
        this.categoria = categoria;
        this.modificadores = modificadores;
        this.nombre_sucursal = nombre_sucursal;
        this.suc_id = suc_id;
        this.art_descripcion = art_descripcion;
        this.art_tipo = art_tipo;
        this.art_disponible_venta = art_disponible_venta;
        this.art_disponible_compra = art_disponible_compra;
        this.ava_aplica_apartados = ava_aplica_apartados;
        this.ava_aplica_cambio_devolucion = ava_aplica_cambio_devolucion;
        this.aim_url = aim_url;
        this.art_nombre = art_nombre;
        this.cat_nombre = cat_nombre;
        this.his_tipo = his_tipo;
        this.his_cantidad = his_cantidad;
        this.his_observaciones = his_observaciones;
        this.his_fecha_hora_creo = his_fecha_hora_creo;
        this.codigoBarras = codigoBarras;
        this.almacen = almacen;
        this.RecibidasOrigen = RecibidasOrigen;
        this.RecibidasDestino = RecibidasDestino;
        this.tra_nombre_estatus = tra_nombre_estatus;
        this.suc_numero_sucursal_destino = suc_numero_sucursal_destino;
        this.suc_numero_sucursal_origen = suc_numero_sucursal_origen;
        this.fechaSolicitud = fechaSolicitud;
        this.tra_motivo = tra_motivo;
        this.UUIDarticulo = UUIDarticulo;
        this.UUIDvariante = UUIDvariante;
        this.UUIDmodificador = UUIDmodificador;
        this.UUIDexistencias = UUIDexistencias;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProducto() { return producto; }
    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getExistencia() { return existencia; }
    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getModificadores() {
        return modificadores;
    }
    public void setModificadores(String modificadores) {
        this.modificadores = modificadores;
    }

    public String getNombre_sucursal() {
        return nombre_sucursal;
    }
    public void setNombre_sucursal(String nombre_sucursal) { this.nombre_sucursal = nombre_sucursal; }

    public String getSuc_id() {
        return suc_id;
    }
    public void setSuc_id(String suc_id) {
        this.suc_id = suc_id;
    }

    public String getArt_descripcion() {
        return art_descripcion;
    }
    public void setArt_descripcion(String art_descripcion) { this.art_descripcion = art_descripcion; }

    public String getArt_tipo() { return art_tipo; }
    public void setArt_tipo(String art_tipo) {
        this.art_tipo = art_tipo;
    }

    public String getart_disponible_venta() {
        return art_disponible_venta;
    }
    public void setart_disponible_venta(String art_disponible_venta) { this.art_disponible_venta = art_disponible_venta; }

    public String getart_disponible_compra() {
        return art_disponible_compra;
    }
    public void setart_disponible_compra(String art_disponible_compra) { this.art_disponible_compra = art_disponible_compra; }

    public String getava_aplica_apartados() {
        return ava_aplica_apartados;
    }
    public void setava_aplica_apartados(String aava_aplica_apartados) { this.ava_aplica_apartados = ava_aplica_apartados; }

    public String getava_aplica_cambio_devolucion() {
        return ava_aplica_cambio_devolucion;
    }
    public void setava_aplica_cambio_devolucion(String ava_aplica_cambio_devolucion) { this.ava_aplica_cambio_devolucion = ava_aplica_cambio_devolucion; }

    public String getaim_url() { return aim_url; }
    public void setaim_url(String aim_url) {this.aim_url = aim_url;}

    public String getArt_nombre() {
        return art_nombre;
    }
    public void setArt_nombre(String art_nombre) {
        this.art_nombre = art_nombre;
    }

    public String getCat_nombre() {
        return cat_nombre;
    }
    public void setCat_nombre(String cat_nombre) {
        this.cat_nombre = cat_nombre;
    }

    public String getHis_tipo() {
        return his_tipo;
    }
    public void setHis_tipo(String his_tipo) {
        this.his_tipo = his_tipo;
    }

    public String getHis_cantidad() {
        return his_cantidad;
    }
    public void setHis_cantidad(String his_cantidad) {
        this.his_cantidad = his_cantidad;
    }

    public String getHis_observaciones() {
        return his_observaciones;
    }
    public void setHis_observaciones(String his_observaciones) { this.his_observaciones = his_observaciones; }

    public String gethis_fecha_hora_creo() {return his_fecha_hora_creo; }
    public void sethis_fecha_hora_creo(String his_fecha_hora_creo) { this.his_fecha_hora_creo = his_fecha_hora_creo; }

    public String getcodigoBarras() {return codigoBarras; }
    public void setcodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getalmacen() {return almacen; }
    public void setalmacen(String almacen) { this.almacen = almacen; }

    public String getRecibidasOrigen() {return RecibidasOrigen; }
    public void setRecibidasOrigen(String RecibidasOrigen) { this.almacen = RecibidasOrigen; }

    public String getRecibidasDestino() {return RecibidasDestino; }
    public void setRecibidasDestino(String RecibidasDestino) { this.RecibidasDestino = RecibidasDestino; }

    public String gettra_nombre_estatus() {return tra_nombre_estatus; }
    public void settra_nombre_estatus(String tra_nombre_estatus) { this.tra_nombre_estatus = tra_nombre_estatus; }

    public String getsuc_numero_sucursal_destino() {return suc_numero_sucursal_destino; }
    public void setsuc_numero_sucursal_destino(String suc_numero_sucursal_destino) { this.suc_numero_sucursal_destino = suc_numero_sucursal_destino; }

    public String getsuc_numero_sucursal_origen() {return suc_numero_sucursal_origen; }
    public void setsuc_numero_sucursal_origen(String suc_numero_sucursal_origen) { this.suc_numero_sucursal_origen = suc_numero_sucursal_origen; }

    public String getfechaSolicitud() {return fechaSolicitud; }
    public void setfechaSolicitud(String fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public String gettra_motivo() {return tra_motivo; }
    public void settra_motivo(String tra_motivo) { this.tra_motivo = tra_motivo; }

    public String getUUIDarticulo() {return UUIDarticulo; }
    public void setUUIDarticulo(String UUIDarticulo) { this.UUIDarticulo = UUIDarticulo; }

    public String getUUIDvariante() {return UUIDvariante; }
    public void setUUIDvariante(String UUIDvariante) { this.UUIDvariante = UUIDvariante; }

    public String getUUIDmodificador() {return UUIDmodificador; }
    public void setUUIDmodificador(String UUIDmodificador) { this.UUIDmodificador = UUIDmodificador; }

    public String getUUIDexistencias() {return UUIDexistencias; }
    public void setUUIDexistencias(String UUIDexistencias) { this.UUIDexistencias = UUIDexistencias; }
}
