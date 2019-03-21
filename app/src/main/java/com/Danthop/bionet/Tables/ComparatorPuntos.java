package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.Preguntas_Model;
import com.Danthop.bionet.model.Puntos_acumulados_model;

import java.util.Comparator;



public final class ComparatorPuntos {

    private ComparatorPuntos() {

    }

    public static Comparator<Puntos_acumulados_model> getIdClienteComparator() {
        return new getIdCliente();
    }


    private static class getIdCliente implements Comparator<Puntos_acumulados_model> {

        @Override
        public int compare(final Puntos_acumulados_model puntos1, final Puntos_acumulados_model puntos2) {
            return puntos1.getNumero_cliente().compareTo(puntos2.getNumero_cliente());
        }
    }

}