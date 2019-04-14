package com.Danthop.bionet.model;

public class PublicacionModel {
    private String id;
    private String name;
    //private String ExceptionsCategory[] = {"","","","",""};

    public PublicacionModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
