package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableClienteFrecuenteTable;
import com.Danthop.bionet.model.ClienteFrecuenteModel;

import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class TopvendidosAdapter extends LongPressAwareTableDataAdapter<ClienteFrecuenteModel> {
    int TEXT_SIZE = 14;

    public TopvendidosAdapter(final Context context, final List<ClienteFrecuenteModel> data, final SortableClienteFrecuenteTable tableView) {
        super(context, data, tableView);
    }
    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ClienteFrecuenteModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderproducto(Invetario);
                break;

            case 1:
                renderedView = rendercantidad(Invetario);
                break;


        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ClienteFrecuenteModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableInventarioName(Invetario);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableInventarioName(final ClienteFrecuenteModel Inventario) {
        final EditText editText = new EditText(getContext());
        editText.setText((CharSequence) Inventario.getnombre_cliente());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new TopvendidosAdapter.OrdenNameUpdater(Inventario));
        return editText;
    }


    private View renderproducto(final ClienteFrecuenteModel Inventario) {
        return renderString(Inventario.gettar_nombre_articulo());
    }

    private View rendercantidad(final ClienteFrecuenteModel Inventario) {
        return renderString(Inventario.getNumTicket());
    }



    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private ClienteFrecuenteModel ordenToUpdate;

        public OrdenNameUpdater(ClienteFrecuenteModel ordenToUpdate) {
            this.ordenToUpdate = ordenToUpdate;
        }

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
            //ordenToUpdate.setNombre_sucursal(s.toString());
        }
    }}
