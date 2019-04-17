package com.Danthop.bionet.model;

import java.util.ArrayList;

public class PublicacionModel {
    private String id;
    private String name;
    private ArrayList ExceptionsCategory;
    //private String ExceptionsCategory[] = {"","","","",""};

    public PublicacionModel(String id, String name,ArrayList exceptionscategory) {
        this.id = id;
        this.name = name;
        this.ExceptionsCategory=exceptionscategory;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList getExceptionsCategory() {
        return ExceptionsCategory;
    }

}
