package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
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

import com.Danthop.bionet.Fragment_editarCliente;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.Tables.SortableLealtadArticulosTable;
import com.Danthop.bionet.Tables.SortableLealtadInscribirTable;
import com.Danthop.bionet.Tables.SortablePuntosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.LealtadArticulosModel;
import com.Danthop.bionet.model.Puntos_acumulados_model;
import com.Danthop.bionet.model.SucursalModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class LealtadInscribirAdapter extends LongPressAwareTableDataAdapter<ClienteModel> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private FragmentTransaction fr;


    public LealtadInscribirAdapter(final Context context, final List<ClienteModel> data, final SortableLealtadInscribirTable tableView,FragmentTransaction gr) {
        super(context, data, tableView);
        fr = gr;
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ClienteModel cliente = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderClienteName(cliente);
                break;
            case 1:
                renderedView = renderClienteCorreo(cliente);
                break;
            case 2:
                renderedView = renderClienteTelefono(cliente);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ClienteModel cliente = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(cliente);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final ClienteModel cliente) {
        final EditText editText = new EditText(getContext());
        editText.setText(cliente.getCliente_Nombre());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new LealtadInscribirAdapter.ClienteNameUpdater(cliente));
        return editText;
    }

    private View renderClienteName(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Nombre());
    }

    private View renderClienteCorreo(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Correo());
    }

    private View renderClienteTelefono(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Telefono());
    }

    private View renderClienteUltimaVisita(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Ultima_Visita());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }



    private static class ClienteNameUpdater implements TextWatcher {

        private ClienteModel clienteToUpdate;

        public ClienteNameUpdater(ClienteModel clienteToUpdate) {
            this.clienteToUpdate = clienteToUpdate;
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
            clienteToUpdate.setCliente_Nombre(s.toString());
        }
    }



}