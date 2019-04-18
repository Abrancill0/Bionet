package com.Danthop.bionet.model;

import java.security.PrivateKey;
import java.util.ArrayList;

public class PublicacionModel {
    private String id;
    private String name;
    private String Remaining_listings;
    private ArrayList ExceptionsCategory;
    //private String ExceptionsCategory[] = {"","","","",""};

    public PublicacionModel(String id, String name,ArrayList exceptionscategory, String remaining_listings) {
        this.id = id;
        this.name = name;
        this.Remaining_listings = remaining_listings;
        this.ExceptionsCategory=exceptionscategory;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRemaining_listings() {
        return Remaining_listings;
    }

    public ArrayList getExceptionsCategory() {
        return ExceptionsCategory;
    }

}
