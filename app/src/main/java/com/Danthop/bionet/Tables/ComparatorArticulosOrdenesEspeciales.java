package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.ArticuloOrdenEspecialModel;

import java.util.Comparator;

public class ComparatorArticulosOrdenesEspeciales {

    private ComparatorArticulosOrdenesEspeciales() {

    }

    public static Comparator<ArticuloOrdenEspecialModel> getApartadoComparator() {
        return new ApartadoNameComparator();
    }


    private static class ApartadoNameComparator implements Comparator<ArticuloOrdenEspecialModel> {

        @Override
        public int compare(final ArticuloOrdenEspecialModel orden1, final ArticuloOrdenEspecialModel orden2) {
            return orden1.getNombre_articulo().compareTo(orden2.getNombre_articulo());
        }
    }

}
