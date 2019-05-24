package com.Danthop.bionet.model;

import java.util.ArrayList;

public class LealtadArticuloModel {
    private String ArticuloNombre;
    private String ArticuloTipo;
    private String ArticuloDescripcion;
    private String ArticuloCategoria;
    private ArrayList<String> ArticuloVariantes;
    private ArrayList<String> ArticuloImagenes;
    private String ArticuloDispVenta;
    private String ArticuloDispCompra;
    private String ArticuloDispOrdenes;
    private String ArticuloDispApartados;
    private String ArticuloDispCambioDevolucion;

    public LealtadArticuloModel(String articuloNombre, String articuloTipo ,String articuloDescripcion, String articuloCategoria,
                                ArrayList<String> articuloVariantes,
                                ArrayList<String> articuloImagenes,
                                String articuloDispVenta,
                                String articuloDispCompra,
                                String articuloDispOrdenes,
                                String articuloDispApartados,
                                String articuloDispCambioDevolucion) {
        ArticuloNombre = articuloNombre;
        ArticuloTipo = articuloTipo;
        ArticuloDescripcion = articuloDescripcion;
        ArticuloCategoria = articuloCategoria;
        ArticuloVariantes = articuloVariantes;
        ArticuloImagenes = articuloImagenes;
        ArticuloDispVenta = articuloDispVenta;
        ArticuloDispCompra = articuloDispCompra;
        ArticuloDispOrdenes = articuloDispOrdenes;
        ArticuloDispApartados = articuloDispApartados;
        ArticuloDispCambioDevolucion = articuloDispCambioDevolucion;
    }

    public String getArticuloNombre() {
        return ArticuloNombre;
    }

    public void setArticuloNombre(String articuloNombre) {
        ArticuloNombre = articuloNombre;
    }

    public String getArticuloDescripcion() {
        return ArticuloDescripcion;
    }

    public void setArticuloDescripcion(String articuloDescripcion) {
        ArticuloDescripcion = articuloDescripcion;
    }

    public String getArticuloCategoria() {
        return ArticuloCategoria;
    }

    public void setArticuloCategoria(String articuloCategoria) {
        ArticuloCategoria = articuloCategoria;
    }

    public String getArticuloTipo() {
        return ArticuloTipo;
    }

    public void setArticuloTipo(String articuloTipo) {
        ArticuloTipo = articuloTipo;
    }

    public ArrayList<String> getArticuloVariantes() {
        return ArticuloVariantes;
    }

    public void setArticuloVariantes(ArrayList<String> articuloVariantes) {
        ArticuloVariantes = articuloVariantes;
    }

    public String getArticuloDispVenta() {
        return ArticuloDispVenta;
    }

    public void setArticuloDispVenta(String articuloDispVenta) {
        ArticuloDispVenta = articuloDispVenta;
    }

    public String getArticuloDispCompra() {
        return ArticuloDispCompra;
    }

    public void setArticuloDispCompra(String articuloDispCompra) {
        ArticuloDispCompra = articuloDispCompra;
    }

    public String getArticuloDispOrdenes() {
        return ArticuloDispOrdenes;
    }

    public void setArticuloDispOrdenes(String articuloDispOrdenes) {
        ArticuloDispOrdenes = articuloDispOrdenes;
    }

    public String getArticuloDispApartados() {
        return ArticuloDispApartados;
    }

    public void setArticuloDispApartados(String articuloDispApartados) {
        ArticuloDispApartados = articuloDispApartados;
    }

    public String getArticuloDispCambioDevolucion() {
        return ArticuloDispCambioDevolucion;
    }

    public void setArticuloDispCambioDevolucion(String articuloDispCambioDevolucion) {
        ArticuloDispCambioDevolucion = articuloDispCambioDevolucion;
    }

    public ArrayList<String> getArticuloImagenes() {
        return ArticuloImagenes;
    }

    public void setArticuloImagenes(ArrayList<String> articuloImagenes) {
        ArticuloImagenes = articuloImagenes;
    }
}
