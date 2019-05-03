package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.Tables.SortableHistoricoTable;
import com.Danthop.bionet.model.CorteCajaModel;
import com.Danthop.bionet.model.HistoricoModel;
import java.util.List;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class CorteCajaAdapter extends LongPressAwareTableDataAdapter<CorteCajaModel> {
    int TEXT_SIZE = 12;

    public CorteCajaAdapter(final Context context, final List<CorteCajaModel> data, final SortableCorteCajaTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CorteCajaModel corte = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderid_venta(corte);
                break;
            case 1:
                renderedView = rendertotal(corte);
                break;
            case 2:
                renderedView = renderforma_pago(corte);
                break;
            case 3:
                renderedView = renderfecha(corte);
                break;
            case 4:
                renderedView = renderhora(corte);
                break;
            case 5:
                renderedView = renderusuario(corte);
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
        editText.addTextChangedListener(new CorteCajaAdapter.OrdenNameUpdater(corte));
        return editText;
    }

    private View renderid_venta(final CorteCajaModel corte) {
        return renderString(corte.getid_venta());
    }

    private View rendertotal(final CorteCajaModel corte) {
        return renderString(corte.gettotal());
    }

    private View renderforma_pago(final CorteCajaModel corte) {
        return renderString(corte.getforma_pago());
    }

    private View renderfecha(final CorteCajaModel corte) {
        return renderString(corte.getfecha());
    }
    private View renderhora(final CorteCajaModel corte) {
        return renderString(corte.gethora());
    }
    private View renderusuario(final CorteCajaModel corte){
        return renderString(corte.getusuario());
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