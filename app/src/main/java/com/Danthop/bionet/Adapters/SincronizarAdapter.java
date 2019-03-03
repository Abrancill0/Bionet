package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import com.Danthop.bionet.Tables.SortableSincronizarTable;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.SincronizarModel;
import com.google.android.gms.common.api.Api;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class SincronizarAdapter extends LongPressAwareTableDataAdapter<SincronizarModel> {

    int TEXT_SIZE = 12;

    public SincronizarAdapter(final Context context, final List<SincronizarModel> data, final SortableSincronizarTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        SincronizarModel sincronizar = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderArticulo(sincronizar);
                break;
            case 1:
                renderedView = renderDisponible(sincronizar);
                break;
            case 2:
                renderedView = renderEnvioGratis(sincronizar);
                break;
            case 3:
                renderedView = renderPrecio(sincronizar);
                break;
    }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final SincronizarModel sincronizar = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(sincronizar);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final SincronizarModel sincronizar) {
        final EditText editText = new EditText(getContext());
        editText.setText(sincronizar.getArticulo());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new OrdenNameUpdater(sincronizar));
        return editText;
    }

    private View renderArticulo(final SincronizarModel sincronizar) {
        return renderString(sincronizar.getArticulo());
    }

    private View renderDisponible(final SincronizarModel sincronizar) {
        return renderString(sincronizar.getDisponible());
    }

    private View renderEnvioGratis(final SincronizarModel sincronizar) {
        return renderString(sincronizar.getEnvio_gratis());
    }

    private View renderPrecio(final SincronizarModel sincronizar) {
        return renderString(sincronizar.getPrecio());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private SincronizarModel ordenToUpdate;

        public OrdenNameUpdater(SincronizarModel ordenToUpdate) {
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
            ordenToUpdate.setArticulo(s.toString());
        }
    }}


