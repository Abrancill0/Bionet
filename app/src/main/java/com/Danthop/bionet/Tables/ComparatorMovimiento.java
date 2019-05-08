package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.MovimientoModel;

import java.util.Comparator;



public final class ComparatorMovimiento {

    private ComparatorMovimiento() {

    }

    public static Comparator<MovimientoModel> getFechaMovimientoComparator() {
        return new FechaMovimientoComparator();
    }


    private static class FechaMovimientoComparator implements Comparator<MovimientoModel> {

        @Override
        public int compare(final MovimientoModel movimiento1, final MovimientoModel movimiento2) {
            return movimiento1.getMovimiento_fecha().compareTo(movimiento2.getMovimiento_fecha());
        }
    }

}