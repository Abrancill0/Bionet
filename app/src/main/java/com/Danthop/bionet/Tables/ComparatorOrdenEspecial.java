package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.OrdenEspecialModel;

import java.util.Comparator;

public class ComparatorOrdenEspecial {

    private ComparatorOrdenEspecial() {

    }

    public static Comparator<OrdenEspecialModel> getOrdenComparator() {
        return new OrdenNameComparator();
    }


    private static class OrdenNameComparator implements Comparator<OrdenEspecialModel> {

        @Override
        public int compare(final OrdenEspecialModel orden1, final OrdenEspecialModel orden2) {
            return orden1.getOrden_cliente_id().compareTo(orden2.getOrden_cliente_id());
        }
    }

}
