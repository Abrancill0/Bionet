package com.Danthop.bionet.Tables;

import com.Danthop.bionet.model.ClienteModel;

import java.util.Comparator;



public final class ComparatorCliente {

    private ComparatorCliente() {

    }

    public static Comparator<ClienteModel> getClienteNameComparator() {
        return new ClienteNameComparator();
    }


    private static class ClienteNameComparator implements Comparator<ClienteModel> {

        @Override
        public int compare(final ClienteModel cliente1, final ClienteModel cliente2) {
            return cliente1.getCliente_Nombre().compareTo(cliente2.getCliente_Nombre());
        }
    }

}
