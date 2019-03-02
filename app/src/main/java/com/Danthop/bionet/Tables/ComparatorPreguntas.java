package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.Preguntas_Model;

import java.util.Comparator;



public final class ComparatorPreguntas {

    private ComparatorPreguntas() {

    }

    public static Comparator<Preguntas_Model> getPreguntaComparator() {
        return new PreguntaComparator();
    }


    private static class PreguntaComparator implements Comparator<Preguntas_Model> {

        @Override
        public int compare(final Preguntas_Model pregunta1, final Preguntas_Model pregunta2) {
            return pregunta1.getPreguntas().compareTo(pregunta2.getPreguntas());
        }
    }

}