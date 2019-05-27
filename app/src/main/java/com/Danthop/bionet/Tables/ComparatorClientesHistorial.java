package com.Danthop.bionet.Tables;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;

import java.util.Comparator;

public final class ComparatorClientesHistorial {

    private ComparatorClientesHistorial() {

    }

    public static Comparator<CompraModel> getNoHistorialComparator() {
        return new HistorialNoComparator();
    }


    private static class HistorialNoComparator implements Comparator<CompraModel> {

        @Override
        public int compare(final CompraModel compra1, final CompraModel compra2) {
            return compra1.getNumero().compareTo(compra2.getNumero());
        }
    }

}
