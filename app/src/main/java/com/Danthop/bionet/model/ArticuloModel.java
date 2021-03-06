package com.Danthop.bionet.model;

import java.io.Serializable;

public class ArticuloModel  {
    private String articulo_UUID;
    private String articulo_Nombre;
    private String articulo_Descripcion;
    private String articulo_Precio;
    private String articulo_Imagen1;
    private String articulo_Imagen2;
    private String articulo_sku;
    private String articulo_categoria;
    private String articulo_cantidad;
    private String articulo_tarID;
    private String articulo_descuento;
    private String articulo_iva;
    private String articulo_importe;
    private String articulo_sucursal;
    private String articulo_sucursal_uuid;
    private String articulo_exi_id;
    private String getArticulo_UM;

    private Boolean CreditoTresMeses;
    private Boolean CreditoSeisMeses;
    private Boolean CreditoNueveMeses;
    private Boolean CreditoDoceMeses;

    private String codigo_barras;
    private String num_existencia;


    public ArticuloModel(String articulo_UUID,String articulo_Nombre,String articulo_Descripcion ,String articulo_Precio,String articulo_Imagen1,
                         String articulo_Imagen2, String articulo_SKU, String articulo_Categoria,String articulo_Cantidad,String articulo_TarID,
                         String articulo_Descuento, String articulo_IVA, String articulo_Importe,String articulo_sucursal, String articulo_sucursal_uuid,
                         String articulo_exi_id) {

        this.articulo_UUID = articulo_UUID;
        this.articulo_Nombre = articulo_Nombre;
        this.articulo_Descripcion = articulo_Descripcion;
        this.articulo_Precio = articulo_Precio;
        this.articulo_Imagen1 = articulo_Imagen1;
        this.articulo_Imagen2 = articulo_Imagen2;
        this.articulo_sku = articulo_SKU;
        this.articulo_categoria = articulo_Categoria;
        this.articulo_cantidad = articulo_Cantidad;
        this.articulo_tarID = articulo_TarID;
        this.articulo_descuento = articulo_Descuento;
        this.articulo_importe = articulo_Importe;
        this.articulo_iva = articulo_IVA;
        this.articulo_sucursal = articulo_sucursal;
        this.articulo_sucursal_uuid = articulo_sucursal_uuid;
        this.articulo_exi_id = articulo_exi_id;
        CreditoTresMeses=false;
        CreditoSeisMeses=false;
        CreditoNueveMeses=false;
        CreditoDoceMeses=false;
    }

    public String getarticulo_UUID() {
        return articulo_UUID;
    }

    public String getarticulo_Nombre() {
        return articulo_Nombre;
    }

    public String getarticulo_Descripcion() {
        return articulo_Descripcion;
    }

    public String getarticulo_Precio() {
        return articulo_Precio;
    }

    public void setarticulo_UUID(String articulo_UUID) {
        this.articulo_Precio = articulo_UUID;
    }

    public void setarticulo_Nombre(String articulo_Nombre) { this.articulo_Precio = articulo_Nombre; }

    public void setarticulo_Descripcion(String articulo_Descripcion) { this.articulo_Descripcion = articulo_Descripcion; }

    public void setarticulo_Precio(String articulo_Precio) { this.articulo_Precio = articulo_Precio; }

    public String getArticulo_Imagen1() {
        return articulo_Imagen1;
    }

    public String getArticulo_Imagen2() {
        return articulo_Imagen2;
    }

    public void setArticulo_Imagen1(String articulo_Imagen1) { this.articulo_Imagen1 = articulo_Imagen1; }

    public void setArticulo_Imagen2(String articulo_Imagen2) { this.articulo_Imagen2 = articulo_Imagen2; }

    public String getArticulo_sku() {
        return articulo_sku;
    }

    public void setArticulo_sku(String articulo_sku) {
        this.articulo_sku = articulo_sku;
    }

    public String getArticulo_categoria() {
        return articulo_categoria;
    }

    public void setArticulo_categoria(String articulo_categoria) { this.articulo_categoria = articulo_categoria; }

    public String getArticulo_cantidad() { return articulo_cantidad; }

    public void setArticulo_cantidad(String articulo_cantidad) { this.articulo_cantidad = articulo_cantidad; }

    public String getArticulo_tarID() {
        return articulo_tarID;
    }

    public void setArticulo_tarID(String articulo_tarID) {
        this.articulo_tarID = articulo_tarID;
    }

    public String getArticulo_descuento() {
        return articulo_descuento;
    }

    public void setArticulo_descuento(String articulo_descuento) { this.articulo_descuento = articulo_descuento; }

    public String getArticulo_iva() {
        return articulo_iva;
    }

    public void setArticulo_iva(String articulo_iva) {
        this.articulo_iva = articulo_iva;
    }

    public String getArticulo_importe() {
        return articulo_importe;
    }

    public void setArticulo_importe(String articulo_importe) { this.articulo_importe = articulo_importe; }

    public String getArticulo_sucursal() {
        return articulo_sucursal;
    }

    public void setArticulo_sucursal(String articulo_sucursal) { this.articulo_sucursal = articulo_sucursal; }

    public String getArticulo_sucursal_uuid() {
        return articulo_sucursal_uuid;
    }

    public void setArticulo_sucursal_uuid(String articulo_sucursal_uuid) { this.articulo_sucursal_uuid = articulo_sucursal_uuid; }

    public String getArticulo_articulo_exi_id() {
        return articulo_exi_id;
    }

    public void setArticulo_articulo_exi_id(String articulo_exi_id) { this.articulo_exi_id = articulo_exi_id; }

    public String getGetArticulo_UM() {
        return getArticulo_UM;
    }

    public void setGetArticulo_UM(String getArticulo_UM) {
        this.getArticulo_UM = getArticulo_UM;
    }

    public Boolean getCreditoTresMeses() {
        return CreditoTresMeses;
    }

    public void setCreditoTresMeses(Boolean creditoTresMeses) {
        CreditoTresMeses = creditoTresMeses;
    }

    public Boolean getCreditoSeisMeses() {
        return CreditoSeisMeses;
    }

    public void setCreditoSeisMeses(Boolean creditoSeisMeses) {
        CreditoSeisMeses = creditoSeisMeses;
    }

    public Boolean getCreditoNueveMeses() {
        return CreditoNueveMeses;
    }

    public void setCreditoNueveMeses(Boolean creditoNueveMeses) {
        CreditoNueveMeses = creditoNueveMeses;
    }

    public Boolean getCreditoDoceMeses() {
        return CreditoDoceMeses;
    }

    public void setCreditoDoceMeses(Boolean creditoDoceMeses) {
        CreditoDoceMeses = creditoDoceMeses;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public String getNum_existencia() {
        return num_existencia;
    }

    public void setNum_existencia(String num_existencia) {
        this.num_existencia = num_existencia;
    }
}