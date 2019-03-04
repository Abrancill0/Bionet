package com.Danthop.bionet.Tables;


import com.Danthop.bionet.model.SincronizarModel;
import com.Danthop.bionet.model.SucursalModel;
import com.Danthop.bionet.model.SucursalesModel;

import java.util.Comparator;

public final class ComparatorSucursal {

    private ComparatorSucursal() {

    }

    public static Comparator<SucursalesModel> getSucursalComparator() {
        return new SucursalNameComparator();
    }


    private static class SucursalNameComparator implements Comparator<SucursalesModel> {

        @Override
        public int compare(final SucursalesModel sucursal1, final SucursalesModel sucursal2) {
            return sucursal1.getNombre().compareTo(sucursal2.getNombre());
        }
    }

}
