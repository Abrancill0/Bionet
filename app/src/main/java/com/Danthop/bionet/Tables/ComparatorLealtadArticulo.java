package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.LealtadArticuloModel;

import java.util.Comparator;

public class ComparatorLealtadArticulo {

    private ComparatorLealtadArticulo() {

    }

    public static Comparator<LealtadArticuloModel> getArticuloComparator() {
        return new ProductoNameComparator();
    }


    private static class ProductoNameComparator implements Comparator<LealtadArticuloModel> {

        @Override
        public int compare(final LealtadArticuloModel Articulo1, final LealtadArticuloModel Articulo2) {
            return Articulo1.getArticuloNombre().compareTo(Articulo2.getArticuloNombre());
        }
    }

}