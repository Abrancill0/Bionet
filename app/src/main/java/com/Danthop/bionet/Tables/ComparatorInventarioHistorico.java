package com.Danthop.bionet.Tables;
import com.Danthop.bionet.model.HistoricoModel;

import java.util.Comparator;

public class ComparatorInventarioHistorico {

    private ComparatorInventarioHistorico() {

    }

    public static Comparator<HistoricoModel> getInventarioNameComparator() {
        return new InventarioNameComparator();
    }


    private static class InventarioNameComparator implements Comparator<HistoricoModel> {

        @Override
        public int compare(final HistoricoModel cliente1, final HistoricoModel Inventario) {
            return Inventario.getNombre_sucursal().compareTo(Inventario.getNombre_sucursal());
        }
    }
}