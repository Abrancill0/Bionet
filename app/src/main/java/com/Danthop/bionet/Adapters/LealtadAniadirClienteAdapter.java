package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableLealtadAniadirCliente;
import com.Danthop.bionet.Tables.SortableLealtadInscribirTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.InventarioModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class LealtadAniadirClienteAdapter extends LongPressAwareTableDataAdapter<ClienteModel> {

    int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private FragmentTransaction fr;
    private ArrayList<String> clientes_id;

    public LealtadAniadirClienteAdapter(final Context context, final List<ClienteModel> data, final SortableLealtadAniadirCliente tableView, FragmentTransaction gr,ArrayList<String> ids) {
        super(context, data, tableView);
        fr = gr;
        clientes_id = ids;
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
                renderedView = renderAniadirCliente(cliente);
                break;

        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;

        return renderedView;
    }

    private View renderAniadirCliente(final ClienteModel cliente) {
        final CheckBox Aniadir = new CheckBox(getContext());
        Aniadir.setGravity(Gravity.CENTER);

        Aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Aniadir.isChecked())
                {
                    clientes_id.add(cliente.getCliente_UUID());
                }else
                {
                    for(int i=0;i<=clientes_id.size();i++)
                    {
                        if(cliente.getCliente_UUID().equals(clientes_id.get(i)))
                        {
                            clientes_id.remove(i);
                        }
                    }
                }
            }
        });

        return Aniadir;
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