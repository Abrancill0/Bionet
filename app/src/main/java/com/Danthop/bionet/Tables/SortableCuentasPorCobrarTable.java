package com.Danthop.bionet.Tables;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.ContactoModel;
import com.Danthop.bionet.model.CuentaPendienteModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class SortableCuentasPorCobrarTable extends SortableTableView<CuentaPendienteModel> {

    public SortableCuentasPorCobrarTable(final Context context) {
        this(context, null);
    }

    public SortableCuentasPorCobrarTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableCuentasPorCobrarTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Contacto", "Teléfono", "Correo Eléctronico", "Puesto", "Notas");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);
        simpleHeader.setTextSize( 14);
        simpleHeader.setPaddings(5,5,5,5);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, ComparatorCuentasPorCobrar.getCuentasPorCobrarComparator());

    }

}