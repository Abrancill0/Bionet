package com.Danthop.bionet.model;

public class LealtadArticuloModel {
    private String ArticuloNombre;
    private String ArticuloDescripcion;
    private String ArticuloCategoria;

    public LealtadArticuloModel(String articuloNombre, String articuloDescripcion, String articuloCategoria) {
        ArticuloNombre = articuloNombre;
        ArticuloDescripcion = articuloDescripcion;
        ArticuloCategoria = articuloCategoria;
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
}
