package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ArticuloApartadoModel;

import java.util.Comparator;

public class ComparatorArticulosApartados {

    private ComparatorArticulosApartados() {

    }

    public static Comparator<ArticuloApartadoModel> getApartadoComparator() {
        return new ApartadoNameComparator();
    }


    private static class ApartadoNameComparator implements Comparator<ArticuloApartadoModel> {

        @Override
        public int compare(final ArticuloApartadoModel apartado1, final ArticuloApartadoModel apartado2) {
            return apartado1.getNombre_articulo().compareTo(apartado2.getNombre_articulo());
        }
    }

}
