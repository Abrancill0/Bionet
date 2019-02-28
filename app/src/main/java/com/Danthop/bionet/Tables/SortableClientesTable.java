package com.Danthop.bionet.Tables;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.ClienteModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class SortableClientesTable extends SortableTableView<ClienteModel> {

    public SortableClientesTable(final Context context) {
        this(context, null);
    }

    public SortableClientesTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableClientesTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Nombre", "Correo Eléctronico", "Teléfono", "Direccion", "Consumo Promedio","ID Referencia","Editar","Eliminar");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(8);
        tableColumnWeightModel.setColumnWeight(0, 4);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 3);
        tableColumnWeightModel.setColumnWeight(4, 3);
        tableColumnWeightModel.setColumnWeight(5, 3);
        tableColumnWeightModel.setColumnWeight(6, 2);
        tableColumnWeightModel.setColumnWeight(7, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, ComparatorCliente.getClienteNameComparator());

    }

}