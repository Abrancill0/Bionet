package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.SincronizarModel;

import java.util.Comparator;



public final class ComparatorSincronizar {

    private ComparatorSincronizar() {

    }

    public static Comparator<SincronizarModel> getArticuloSincronizar() {
        return new SincronizarNameComparator();
    }


    private static class SincronizarNameComparator implements Comparator<SincronizarModel> {

        @Override
        public int compare(final SincronizarModel sincronizar, final SincronizarModel sincronizar2) {
            return sincronizar.getArticulo().compareTo(sincronizar2.getArticulo());
        }
    }

}