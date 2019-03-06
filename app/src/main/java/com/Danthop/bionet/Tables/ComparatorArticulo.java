package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.SucursalModel;

import java.util.Comparator;

public class ComparatorArticulo {

    private ComparatorArticulo() {

    }

    public static Comparator<ArticuloModel> getArticuloComparator() {
        return new ComparatorArticulo.ArticuloNameComparator();
    }


    private static class ArticuloNameComparator implements Comparator<ArticuloModel> {

        @Override
        public int compare(final ArticuloModel Articulo1, final ArticuloModel Articulo2) {
            return Articulo1.getarticulo_Nombre().compareTo(Articulo2.getarticulo_Precio());
        }
    }

}
