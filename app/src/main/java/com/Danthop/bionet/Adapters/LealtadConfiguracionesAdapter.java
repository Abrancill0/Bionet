package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableLealtadConfiguracionesTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ConfiguracionLealtadModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class LealtadConfiguracionesAdapter extends LongPressAwareTableDataAdapter<ConfiguracionLealtadModel> {

    int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public LealtadConfiguracionesAdapter(final Context context, final List<ConfiguracionLealtadModel> data, final SortableLealtadConfiguracionesTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ConfiguracionLealtadModel programa = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderNivel(programa);
                break;
            case 1:
                renderedView = renderDineroXPt(programa);
                break;
            case 2:
                renderedView = renderPtXDinero(programa);
                break;
            case 3:
                renderedView = renderStatus(programa);
                break;

        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ConfiguracionLealtadModel programa = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {

        }

        return renderedView;
    }

    private View renderNivel(final ConfiguracionLealtadModel programa) {
        return renderString(programa.getNivel());
    }

    private View renderDineroXPt(final ConfiguracionLealtadModel programa) {
        return renderString(programa.getDineroPorPuntos());
    }

    private View renderPtXDinero(final ConfiguracionLealtadModel programa) {
        return renderString(programa.getPuntosPorDinero());
    }

    private View renderStatus(final ConfiguracionLealtadModel programa) {

        if(programa.getStatus().equals("false"))
        {
            return renderStringInactivo("Inactiva");
        }
        else if(programa.getStatus().equals("true")){
            return renderStringActivo("Activa");
        }

        return renderString("");

    }

    private View renderStringActivo(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(getResources().getColor(R.color.green));
        return textView;
    }
    private View renderStringInactivo(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(getResources().getColor(R.color.red));
        return textView;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private ArticuloModel ordenToUpdate;

        public OrdenNameUpdater(ArticuloModel ordenToUpdate) {
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
            ordenToUpdate.getarticulo_Nombre();
        }
    }}
