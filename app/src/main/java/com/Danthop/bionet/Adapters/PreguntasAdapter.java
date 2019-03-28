package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortablePreguntasTable;
import com.Danthop.bionet.model.Preguntas_Model;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class PreguntasAdapter extends LongPressAwareTableDataAdapter<Preguntas_Model> {

    int TEXT_SIZE = 12;
    public Dialog pop_up1;
    public Dialog pop_up2;
    private TextView Respuesta;
    private Spinner spinner1;
    private String UserML;
    ProgressDialog progreso;

    public PreguntasAdapter(final Context context, final List<Preguntas_Model> data, final SortablePreguntasTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Preguntas_Model pregunta = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderPregunta(pregunta);
                break;
            case 1:
                renderedView = renderComprador(pregunta);
                break;
            case 2:
                renderedView = renderTitulo(pregunta);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Preguntas_Model pregunta = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(pregunta);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final Preguntas_Model pregunta) {
        final EditText editText = new EditText(getContext());
        editText.setText(pregunta.getComprador());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new OrdenNameUpdater(pregunta));
        return editText;
    }

    private View renderPregunta(final Preguntas_Model pregunta) {
        return renderString(pregunta.getPreguntas());
    }
    private View renderComprador(final Preguntas_Model pregunta) {
        return renderString(pregunta.getComprador());
    }
    private View renderTitulo(final Preguntas_Model pregunta) {
        return renderString(pregunta.getTitulo());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }
    private static class OrdenNameUpdater implements TextWatcher {

        private Preguntas_Model ordenToUpdate;

        public OrdenNameUpdater(Preguntas_Model ordenToUpdate) {
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
            ordenToUpdate.setPreguntas(s.toString());
        }
    }}





