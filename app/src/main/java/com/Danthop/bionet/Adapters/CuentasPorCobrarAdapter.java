package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Fragment_crear_cliente;
import com.Danthop.bionet.Fragment_editarCliente;
import com.Danthop.bionet.Numero_sucursal;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableClienteContactos;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableCuentasPorCobrarTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.ContactoModel;
import com.Danthop.bionet.model.CuentaPendienteModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.sortabletableview.recyclerview.toolkit.FilterHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;


public class CuentasPorCobrarAdapter extends LongPressAwareTableDataAdapter<CuentaPendienteModel> implements Filterable {

    int TEXT_SIZE = 16;
    private SortableClienteContactos tabla_clientes;

    private List<CuentaPendienteModel> cuentaPendienteList;
    private List<CuentaPendienteModel> cuentaPendienteListFull;

    private Typeface s;
    private FragmentTransaction fr;




    private String UsuarioID;

    public CuentasPorCobrarAdapter(final Context context, final List<CuentaPendienteModel> data, final SortableCuentasPorCobrarTable tableView, FragmentTransaction gr) {
        super(context, data, tableView);

        fr = gr;

        cuentaPendienteList = data;
        cuentaPendienteListFull = new ArrayList<>(data);

    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CuentaPendienteModel cuenta = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderTicket(cuenta);
                break;
            case 1:
                renderedView = renderFecha(cuenta);
                break;
            case 2:
                renderedView = renderCargo(cuenta);
                break;
            case 3:
                renderedView = renderAbono(cuenta);
                break;
            case 4:
                renderedView = renderPendiente(cuenta);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final CuentaPendienteModel cuenta = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:

            default:

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

    private View renderTicket(final CuentaPendienteModel cuenta) {
        return renderString(cuenta.getTicket());
    }

    private View renderFecha(final CuentaPendienteModel cuenta) {
        return renderString(cuenta.getFecha());
    }

    private View renderCargo(final CuentaPendienteModel cuenta) {
        return renderString(cuenta.getCargo());
    }

    private View renderAbono(final CuentaPendienteModel cuenta) {
        return renderString(cuenta.getAbono());
    }

    private View renderPendiente(final CuentaPendienteModel cuenta) {
        return renderString(cuenta.getPendiente());
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setTextSize(TEXT_SIZE);
        textView.setPadding(5,0,0,5);
        textView.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        return textView;
    }

    private static class ClienteNameUpdater implements TextWatcher {

        private ClienteModel clienteToUpdate;

        public ClienteNameUpdater(ClienteModel clienteToUpdate) {

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

    @Override
    public Filter getFilter() {
        return CuentaFilter;
    }

    private Filter CuentaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CuentaPendienteModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(cuentaPendienteListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CuentaPendienteModel item : cuentaPendienteListFull) {
                    if (item.getTicket().toLowerCase().contains(filterPattern)
                            || item.getFecha().toLowerCase().contains(filterPattern)
                            || item.getCargo().toLowerCase().contains(filterPattern)
                            || item.getAbono().toLowerCase().contains(filterPattern)
                            || item.getPendiente().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cuentaPendienteList.clear();
            cuentaPendienteList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}