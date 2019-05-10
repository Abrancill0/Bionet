package com.Danthop.bionet.Tables;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.FormaspagoModel;
import com.Danthop.bionet.model.PagoModel;

import java.util.Comparator;

public final class ComparatorFormaPago {

    private ComparatorFormaPago() {

    }

    public static Comparator<PagoModel> getFormaPagoComparator() {
        return new FormaPagoNameComparator();
    }


    private static class FormaPagoNameComparator implements Comparator<PagoModel> {

        @Override
        public int compare(final PagoModel Forma1, final PagoModel Forma2) {
            return Forma1.getNombre().compareTo(Forma2.getNombre());
        }
    }

}
