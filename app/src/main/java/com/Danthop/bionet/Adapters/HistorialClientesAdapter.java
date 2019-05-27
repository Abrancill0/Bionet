package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Fragment_crear_cliente;
import com.Danthop.bionet.Fragment_editarCliente;
import com.Danthop.bionet.Numero_sucursal;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableClientesHistorialTable;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class HistorialClientesAdapter extends LongPressAwareTableDataAdapter<CompraModel> {

    int TEXT_SIZE = 12;

    public HistorialClientesAdapter(final Context context, final List<CompraModel> data, final SortableClientesHistorialTable tableView) {
        super(context, data, tableView);

    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CompraModel compra = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderNo(compra);
                break;
            case 1:
                renderedView = renderImporte(compra);
                break;
            case 2:
                renderedView = renderFechaCompra(compra);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final CompraModel compra = getRowData(rowIndex);

        return renderString("");
    }

    private View renderEditableClienteName(final CompraModel compra) {
        final EditText editText = new EditText(getContext());
        editText.setText(compra.getNumero());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new ClienteNameUpdater(compra));
        return editText;
    }

    private View renderNo(final CompraModel compra) {
        return renderString(compra.getNumero());
    }

    private View renderImporte(final CompraModel compra) {
        return renderString(compra.getImporte());
    }

    private View renderFechaCompra(final CompraModel compra) {
        return renderString(compra.getFechaCompra());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class ClienteNameUpdater implements TextWatcher {

        private CompraModel compra;

        public ClienteNameUpdater(CompraModel compra) {

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

        }
    }

}