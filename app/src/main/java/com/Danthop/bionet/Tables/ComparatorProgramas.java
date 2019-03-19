package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ConfiguracionLealtadModel;

import java.util.Comparator;



public final class ComparatorProgramas {

    private ComparatorProgramas() {

    }

    public static Comparator<ConfiguracionLealtadModel> getNivelComparator() {
        return new NivelComparator();
    }


    private static class NivelComparator implements Comparator<ConfiguracionLealtadModel> {

        @Override
        public int compare(final ConfiguracionLealtadModel nivel1, final ConfiguracionLealtadModel nivel2) {
            return nivel1.getNivel().compareTo(nivel2.getNivel());
        }
    }

}