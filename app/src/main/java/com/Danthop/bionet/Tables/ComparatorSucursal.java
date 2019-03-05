package com.Danthop.bionet.Tables;


import com.Danthop.bionet.model.SincronizarModel;
import com.Danthop.bionet.model.SucursalModel;
import com.Danthop.bionet.model.SucursalesModel;

import java.util.Comparator;

public final class ComparatorSucursal {

    private ComparatorSucursal() {

    }

    public static Comparator<SucursalModel> getSucursalComparator() {
        return new SucursalNameComparator();
    }


    private static class SucursalNameComparator implements Comparator<SucursalModel> {

        @Override
        public int compare(final SucursalModel sucursal1, final SucursalModel sucursal2) {
            return sucursal1.getSuc_nombre().compareTo(sucursal2.getSuc_nombre());
        }
    }

}
