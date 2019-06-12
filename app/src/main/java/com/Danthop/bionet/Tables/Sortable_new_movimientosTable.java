package com.Danthop.bionet.Tables;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.MovimientoModel;
import com.Danthop.bionet.model.OrdenEspecialModel;
import com.sortabletableview.recyclerview.SortableTableView;
import com.sortabletableview.recyclerview.model.TableColumnWeightModel;
import com.sortabletableview.recyclerview.toolkit.SimpleTableHeaderAdapter;


public class Sortable_new_movimientosTable extends SortableTableView<MovimientoModel> {

    public Sortable_new_movimientosTable(final Context context) {
        this(context, null);
    }

    public Sortable_new_movimientosTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public Sortable_new_movimientosTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Fecha", "Ticket", "Sucursal", "Monto");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);
        simpleHeader.setTextSize( 18 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, ComparatorMovimiento.getFechaMovimientoComparator());
    }
}
