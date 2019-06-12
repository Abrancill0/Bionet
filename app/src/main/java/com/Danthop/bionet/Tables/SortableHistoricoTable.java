package com.Danthop.bionet.Tables;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.HistoricoModel;
import com.Danthop.bionet.model.InventarioModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class SortableHistoricoTable extends SortableTableView<HistoricoModel> {
    public SortableHistoricoTable(final Context context) {
        this(context, null);
    }

    public SortableHistoricoTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableHistoricoTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Artículo", "Categoría", "Movimiento", "Cantidad", "Observaciones","Fecha y hora");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);
        simpleHeader.setTextSize( 14 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 1);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        setColumnModel(tableColumnWeightModel);
        setColumnComparator(0, ComparatorInventarioHistorico.getInventarioNameComparator());

    }
}