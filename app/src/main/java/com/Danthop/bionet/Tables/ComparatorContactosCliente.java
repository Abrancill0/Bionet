package com.Danthop.bionet.Tables;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.ContactoModel;

import java.util.Comparator;

public final class ComparatorContactosCliente {

    private ComparatorContactosCliente() {

    }

    public static Comparator<ContactoModel> getContactosComparator() {
        return new ContactosComparator();
    }


    private static class ContactosComparator implements Comparator<ContactoModel> {

        @Override
        public int compare(final ContactoModel contacto1, final ContactoModel contacto2) {
            return contacto1.getContacto().compareTo(contacto2.getContacto());
        }
    }

}
