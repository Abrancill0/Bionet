package com.Danthop.bionet.Tables;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.LealtadArticulosModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class SortableLealtadArticulosTable extends SortableTableView<LealtadArticulosModel> {

    public SortableLealtadArticulosTable(final Context context) {
        this(context, null);
    }

    public SortableLealtadArticulosTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableLealtadArticulosTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Producto", "Categoría", "Existencia", "Precio de Lista", "Descuento Actual", "Precio Total");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(5, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, ComparatorLealtadArticulo.getArticuloComparator());

    }

}