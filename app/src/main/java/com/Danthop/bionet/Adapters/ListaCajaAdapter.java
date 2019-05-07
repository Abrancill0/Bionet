package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.model.CorteCajaModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class ListaCajaAdapter extends LongPressAwareTableDataAdapter<CorteCajaModel> {
    int TEXT_SIZE = 12;

    public ListaCajaAdapter(final Context context, final List<CorteCajaModel> data, final SortableCorteCajaTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CorteCajaModel corte = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = rendernombreusuario(corte);
                break;
            case 1:
                renderedView = renderimportetotal(corte);
                break;
            case 2:
                renderedView = renderefectivo(corte);
                break;
            case 3:
                renderedView = renderfecha(corte);
                break;
            case 4:
                renderedView = renderhora(corte);
                break;


        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final CorteCajaModel Historico = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableInventarioName(Historico);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableInventarioName(final CorteCajaModel corte) {
        final EditText editText = new EditText(getContext());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new ListaCajaAdapter.OrdenNameUpdater(corte));
        return editText;
    }

    private View rendernombreusuario(final CorteCajaModel corte) {
        return renderString(corte.getusuario());
    }

    private View renderimportetotal(final CorteCajaModel corte) {
        double Precio = Double.parseDouble( corte.getmonto_total() );
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );
        return textView;
    }

    private View renderefectivo(final CorteCajaModel corte) {
        double Precio = Double.parseDouble( corte.getefectivo() );
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Precio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );
        return textView;
    }

    private View renderfecha(final CorteCajaModel corte) {
        return renderString(corte.getfecha());
    }

    private View renderhora(final CorteCajaModel corte) {
        return renderString(corte.gethora());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private CorteCajaModel ordenToUpdate;

        public OrdenNameUpdater(CorteCajaModel ordenToUpdate) {
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
        public void afterTextChanged(Editable s) { ordenToUpdate.setusuario(s.toString());
        }
    }}