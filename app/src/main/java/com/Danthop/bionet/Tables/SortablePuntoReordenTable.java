
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

public class SortablePuntoReordenTable extends SortableTableView<InventarioModel> {
    public SortablePuntoReordenTable(final Context context) {
        this(context, null);
    }

    public SortablePuntoReordenTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortablePuntoReordenTable(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "","","","");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setHeaderAdapter(simpleHeader);
        simpleHeader.setTextSize( 18 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 1);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, ComparatorInventario.getInventarioNameComparator());

    }




}
