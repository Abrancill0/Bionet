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
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortablePuntosTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Puntos_acumulados_model;
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

public class LealtadPuntosAdapter extends LongPressAwareTableDataAdapter<Puntos_acumulados_model> {

    int TEXT_SIZE = 14;

    private String[][] clienteModel;

    private SortableClientesTable tabla_clientes;

    private List<ClienteModel> clientes;

    private String nombre;
    private String UUID;
    private String telefono;
    private String correo_electronico;
    private String calle;
    private Typeface s;
    private FragmentTransaction fr;


    private String cp_fiscal;
    private String estado_fiscal;
    private String municipio_fiscal;
    private String colonia_fiscal;
    private String calle_fiscal;
    private String num_ext_fiscal;
    private String num_int_fiscal;
    private String direccion_fiscal;
    private String email_fiscal;


    private String UsuarioID;

    public LealtadPuntosAdapter(final Context context, final List<Puntos_acumulados_model> data, final SortablePuntosTable tableView) {
        super(context, data, tableView);

    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Puntos_acumulados_model cliente = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderClienteNumero(cliente);
                break;
            case 1:
                renderedView = renderClienteName(cliente);
                break;
            case 2:
                renderedView = renderClienteCorreo(cliente);
                break;
            case 3:
                renderedView = renderClientePuntos(cliente);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Puntos_acumulados_model cliente = getRowData(rowIndex);
        View renderedView = null;

        return renderedView;
    }

    private View renderEditableClienteName(final Puntos_acumulados_model cliente) {
        final EditText editText = new EditText(getContext());
        return editText;
    }

    private View renderClienteNumero(final Puntos_acumulados_model cliente) {
        return renderString(cliente.getNumero_cliente());
    }

    private View renderClienteName(final Puntos_acumulados_model cliente) {
        return renderString(cliente.getNombre());
    }

    private View renderClienteCorreo(final Puntos_acumulados_model cliente) {
        return renderString(cliente.getCorreo_cliente());
    }

    private View renderClientePuntos(final Puntos_acumulados_model cliente) {
        return renderString(cliente.getAcumulado());
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