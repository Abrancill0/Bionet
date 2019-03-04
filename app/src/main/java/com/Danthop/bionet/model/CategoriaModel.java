package com.Danthop.bionet.model;

public class CategoriaModel {
    private String id;
    private String name;

    public CategoriaModel(String id, String name) {
        this.id = id;
        this.name = name;

    }

    public String getCliente_Nombre() {
        return id;
    }

    public String getCliente_Correo() {
        return name;
    }

}

