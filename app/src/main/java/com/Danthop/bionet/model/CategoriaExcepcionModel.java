package com.Danthop.bionet.model;

public class CategoriaExcepcionModel {

    private String CategoriaID;
    private String CategoriaName;
    private String TipoPublicacion;

    public CategoriaExcepcionModel(String categoriaid,String categorianame,String tipoPublicacion) {

        this.CategoriaID = categoriaid;
        this.CategoriaName = categorianame;
        this.TipoPublicacion = tipoPublicacion;
    }

    public String getCategoriaID() {
        return CategoriaID;
    }

    public String getCategoriaName() {
        return CategoriaName;
    }

    public String getipopublicacion() {
        return TipoPublicacion;
    }

    public void setCategoriaID(String categoriaid) { this.CategoriaID = categoriaid; }

    public void setCategoriaName(String categorianame) { this.CategoriaName = categorianame; }

    public void setTipoProduccion(String tipopublicacion) { this.TipoPublicacion = tipopublicacion; }

}
