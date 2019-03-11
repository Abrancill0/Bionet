package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.LealtadArticulosModel;
import com.Danthop.bionet.model.SucursalModel;

import java.util.Comparator;

public class ComparatorLealtadArticulo {

    private ComparatorLealtadArticulo() {

    }

    public static Comparator<LealtadArticulosModel> getArticuloComparator() {
        return new ProductoNameComparator();
    }


    private static class ProductoNameComparator implements Comparator<LealtadArticulosModel> {

        @Override
        public int compare(final LealtadArticulosModel Articulo1, final LealtadArticulosModel Articulo2) {
            return Articulo1.getProducto().compareTo(Articulo2.getProducto());
        }
    }

}