package com.Danthop.bionet.Tables;
import com.Danthop.bionet.model.InventarioModel;
import java.util.Comparator;

public class ComparatorInventario {

    private ComparatorInventario() {

    }

    public static Comparator<InventarioModel> getInventarioNameComparator() {
        return new InventarioNameComparator();
    }


    private static class InventarioNameComparator implements Comparator<InventarioModel> {

        @Override
        public int compare(final InventarioModel cliente1, final InventarioModel cliente2) {
            return cliente1.getproducto().compareTo(cliente2.getproducto());
        }
    }
}
