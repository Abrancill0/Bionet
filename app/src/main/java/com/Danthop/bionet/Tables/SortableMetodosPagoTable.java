package com.Danthop.bionet.Tables;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.FormaspagoModel;
import com.Danthop.bionet.model.MovimientoModel;
import com.Danthop.bionet.model.OrdenEspecialModel;
import com.Danthop.bionet.model.PagoModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class SortableMetodosPagoTable extends SortableTableView<PagoModel> {

    public SortableMetodosPagoTable(final Context context) {
        this(context, null);
    }

    public SortableMetodosPagoTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableMetodosPagoTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Forma de pago", "Importe","Seleccionar");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(3);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 1);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, ComparatorFormaPago.getFormaPagoComparator());
    }
}
