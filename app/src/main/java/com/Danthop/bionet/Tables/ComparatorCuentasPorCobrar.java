package com.Danthop.bionet.Tables;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.ContactoModel;
import com.Danthop.bionet.model.CuentaPendienteModel;

import java.util.Comparator;

public final class ComparatorCuentasPorCobrar {

    private ComparatorCuentasPorCobrar() {

    }

    public static Comparator<CuentaPendienteModel> getCuentasPorCobrarComparator() {
        return new CuentasPorCobrarComparator();
    }


    private static class CuentasPorCobrarComparator implements Comparator<CuentaPendienteModel> {

        @Override
        public int compare(final CuentaPendienteModel cuenta1, final CuentaPendienteModel cuenta2) {
            return cuenta1.getTicket().compareTo(cuenta2.getTicket());
        }
    }

}
