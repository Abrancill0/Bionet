package com.Danthop.bionet.Adapters;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.InventarioModel;

import java.text.NumberFormat;
import java.util.List;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class InventarioAdapter extends LongPressAwareTableDataAdapter<InventarioModel> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private SortableInventariosTable tabla_inventario;
    private FragmentTransaction fr;



    public InventarioAdapter(final Context context, final List<InventarioModel> data, final SortableInventariosTable tableView, FragmentTransaction gr) {
        super(context, data, tableView);

        if (tabla_inventario ==null ){
            tabla_inventario = tableView;
        }
        fr = gr;
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        InventarioModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final InventarioModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        return renderedView;
    }


    private static class OrdenNameUpdater implements TextWatcher {

        private ArticuloModel ordenToUpdate;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no used
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }
        @Override
        public void afterTextChanged(Editable s) {
            ordenToUpdate.getarticulo_Nombre();
        }
    }}

