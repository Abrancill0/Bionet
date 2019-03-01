package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;

import java.util.Comparator;



public final class ComparatorOrden {

    private ComparatorOrden() {

    }

    public static Comparator<Ecommerce_orden_Model> getClienteNameComparator() {
        return new ClienteNameComparator();
    }


    private static class ClienteNameComparator implements Comparator<Ecommerce_orden_Model> {

        @Override
        public int compare(final Ecommerce_orden_Model orden1, final Ecommerce_orden_Model orden2) {
            return orden1.getCliente().compareTo(orden2.getCliente());
        }
    }

}