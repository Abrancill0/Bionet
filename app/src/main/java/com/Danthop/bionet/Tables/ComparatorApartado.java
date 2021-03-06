package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ArticuloApartadoModel;

import java.util.Comparator;

public class ComparatorApartado {

    private ComparatorApartado() {

    }

    public static Comparator<ApartadoModel> getApartadoComparator() {
        return new ApartadoNameComparator();
    }


    private static class ApartadoNameComparator implements Comparator<ApartadoModel> {

        @Override
        public int compare(final ApartadoModel apartado1, final ApartadoModel apartado2) {
            return apartado1.getApartado_cliente_id().compareTo(apartado2.getApartado_cliente_id());
        }
    }

}
