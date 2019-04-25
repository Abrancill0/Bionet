package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.SucursalModel;

import java.util.Comparator;

public class ComparatorApartado {

    private ComparatorApartado() {

    }

    public static Comparator<ApartadoModel> getArticuloComparator() {
        return new ApartadoNameComparator();
    }


    private static class ApartadoNameComparator implements Comparator<ApartadoModel> {

        @Override
        public int compare(final ApartadoModel apartado1, final ApartadoModel apartado2) {
            return apartado1.getArticulo().compareTo(apartado2.getArticulo());
        }
    }

}
