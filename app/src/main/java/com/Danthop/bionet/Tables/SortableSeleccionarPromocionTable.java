package com.Danthop.bionet.Tables;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.PromocionesModel;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class SortableSeleccionarPromocionTable extends SortableTableView<PromocionesModel> {

    public SortableSeleccionarPromocionTable(final Context context) {
        this(context, null);
    }

    public SortableSeleccionarPromocionTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableSeleccionarPromocionTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Meses a Crédito");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);
        simpleHeader.setTextSize( 18 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(1);
        tableColumnWeightModel.setColumnWeight(0,2);

        setColumnModel(tableColumnWeightModel);

        //setColumnComparator(0, ComparatorArticulo.getArticuloComparator());

    }

}