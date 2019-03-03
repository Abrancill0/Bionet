package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Fragment_crear_cliente;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.model.ClienteModel;
import com.google.android.gms.common.api.Api;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import com.Danthop.bionet.Tables.SortableClientesTable;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class ClienteAdapter extends LongPressAwareTableDataAdapter<ClienteModel> {

    int TEXT_SIZE = 12;

    public ClienteAdapter(final Context context, final List<ClienteModel> data, final SortableClientesTable tableView) {
        super(context, data, tableView);
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
            case 3:
                renderedView = renderEliminar(cliente);
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
        editText.addTextChangedListener(new ClienteNameUpdater(cliente));
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

    private View renderEliminar(final ClienteModel cliente) {
        return ButtonEliminar(cliente);
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private View ButtonEliminar(final ClienteModel cliente){
        final Button eliminar = new Button(getContext());
        eliminar.setText("Eliminar");
        eliminar.setPadding(20, 10, 20, 10);
        eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cliente.setCliente_Nombre("");
            }
        });
        return eliminar;
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