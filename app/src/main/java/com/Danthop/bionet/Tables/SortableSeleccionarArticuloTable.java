package com.Danthop.bionet.Tables;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.LealtadArticuloModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class SortableSeleccionarArticuloTable extends SortableTableView<ArticuloModel> {

    public SortableSeleccionarArticuloTable(final Context context) {
        this(context, null);
    }

    public SortableSeleccionarArticuloTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableSeleccionarArticuloTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Artículo", "SKU","Descripción","Categoría");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, ComparatorArticulo.getArticuloComparator());

    }

}