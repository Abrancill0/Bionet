package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.OrdenEspecialArticuloModel;

import java.util.Comparator;

public class ComparatorArticulosOrdenesEspeciales {

    private ComparatorArticulosOrdenesEspeciales() {

    }

    public static Comparator<OrdenEspecialArticuloModel> getApartadoComparator() {
        return new ApartadoNameComparator();
    }


    private static class ApartadoNameComparator implements Comparator<OrdenEspecialArticuloModel> {

        @Override
        public int compare(final OrdenEspecialArticuloModel orden1, final OrdenEspecialArticuloModel orden2) {
            return orden1.getNombre_articulo().compareTo(orden2.getNombre_articulo());
        }
    }

}
