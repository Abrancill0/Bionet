package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Fragment_crear_cliente;
import com.Danthop.bionet.Fragment_editarCliente;
import com.Danthop.bionet.Numero_sucursal;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableMetodosPagoTable;
import com.Danthop.bionet.Tables.SortablePuntosTable;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.FormaspagoModel;
import com.Danthop.bionet.model.PagoModel;
import com.Danthop.bionet.model.Puntos_acumulados_model;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class MetodoPagoAdapter extends LongPressAwareTableDataAdapter<PagoModel> {

    int TEXT_SIZE = 16;
    private List<EditText> ListaDeCantidades = new ArrayList<>();
    List<PagoModel> MetodosPagoSeleccionados = new ArrayList<>();



    public MetodoPagoAdapter(final Context context, final List<PagoModel> data, final SortableMetodosPagoTable tableView,
                             List<PagoModel> metodosPagoSeleccionados) {
        super(context, data, tableView);
        MetodosPagoSeleccionados=metodosPagoSeleccionados;

    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        PagoModel forma = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderFormaPagoName(forma);
                break;
            case 1:
                renderedView = renderImporte();
                break;
            case 2:
                renderedView = renderSeleccionar(forma,ListaDeCantidades.get(rowIndex));
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final PagoModel forma = getRowData(rowIndex);
        View renderedView = null;

        return renderedView;
    }
    private View renderFormaPagoName(final PagoModel forma) {
        return renderString(forma.getNombre());
    }

    private View renderImporte() {
        final EditText MontoAApartar = new EditText(getContext());
        MontoAApartar.setInputType(TYPE_NUMBER_FLAG_DECIMAL);
        MontoAApartar.setGravity(View.TEXT_ALIGNMENT_CENTER);
        ListaDeCantidades.add(MontoAApartar);
        return MontoAApartar;
    }

    private View renderSeleccionar(final PagoModel forma, final EditText Monto) {
        final CheckBox seleccionar = new CheckBox(getContext());
        seleccionar.setGravity(Gravity.CENTER);
        seleccionar.setPadding(10,10,10,10);

        seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (seleccionar.isChecked())
                    {
                            Monto.setEnabled(false);
                            PagoModel pago = new PagoModel(
                                    forma.getNombre(),
                                    forma.getId(),
                                    String.valueOf(Monto.getText())
                            );
                            MetodosPagoSeleccionados.add(pago);
                    }else
                    {
                        Monto.setEnabled(true);
                        for(int i=0;i<MetodosPagoSeleccionados.size();i++)
                        {
                            if(forma.getId().equals(MetodosPagoSeleccionados.get(i).getId()))
                            {
                                MetodosPagoSeleccionados.remove(i);
                            }

                        }
                    }

            }
        });

        return seleccionar;
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class FormaPagoUpdater implements TextWatcher {

        private ClienteModel clienteToUpdate;

        public FormaPagoUpdater(ClienteModel clienteToUpdate) {

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