package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.Preguntas_Model;
import com.Danthop.bionet.model.ProgramaModel;

import java.util.Comparator;



public final class ComparatorProgramas {

    private ComparatorProgramas() {

    }

    public static Comparator<ProgramaModel> getNivelComparator() {
        return new NivelComparator();
    }


    private static class NivelComparator implements Comparator<ProgramaModel> {

        @Override
        public int compare(final ProgramaModel nivel1, final ProgramaModel nivel2) {
            return nivel1.getNivel().compareTo(nivel2.getNivel());
        }
    }

}