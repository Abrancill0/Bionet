package com.Danthop.bionet.Tables;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.ClienteFrecuenteModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class SortableClienteFrecuenteTable extends SortableTableView<ClienteFrecuenteModel> {
    public SortableClienteFrecuenteTable(final Context context) {
        this(context, null);
    }

    public SortableClienteFrecuenteTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableClienteFrecuenteTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Cliente");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(1);
        tableColumnWeightModel.setColumnWeight(0, 2);

        setColumnModel(tableColumnWeightModel);

        //setColumnComparator(0, ComparatorClienteFrecuente.getInventarioNameComparator());

    }


}
