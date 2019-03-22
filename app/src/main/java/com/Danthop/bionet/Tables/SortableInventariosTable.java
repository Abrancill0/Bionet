package com.Danthop.bionet.Tables;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.Adapters.InventarioAdapter;
import com.Danthop.bionet.R;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.InventarioModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class SortableInventariosTable extends SortableTableView<InventarioModel> {
    public SortableInventariosTable(final Context context) {
        this(context, null);
    }

    public SortableInventariosTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableInventariosTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Nombre", "Correo Eléctronico", "Teléfono"," "," ","");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, ComparatorInventario.getInventarioNameComparator());

    }




}