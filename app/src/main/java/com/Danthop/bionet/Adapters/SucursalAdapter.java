package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import com.Danthop.bionet.Tables.SortableSincronizarTable;
import com.Danthop.bionet.Tables.SortableSucursalTable;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.SincronizarModel;
import com.Danthop.bionet.model.SucursalModel;
import com.Danthop.bionet.model.SucursalesModel;
import com.google.android.gms.common.api.Api;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class SucursalAdapter extends LongPressAwareTableDataAdapter<SucursalesModel> {

    int TEXT_SIZE = 12;

    public SucursalAdapter(final Context context, final List<SucursalesModel> data, final SortableSucursalTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        SucursalesModel sucursal = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderNombre(sucursal);
                break;
            case 1:
                renderedView = renderTelefono(sucursal);
                break;
            case 2:
                renderedView = renderCorreo(sucursal);
                break;
            case 3:
                renderedView = renderCalle(sucursal);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final SucursalesModel sucursal = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(sucursal);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final SucursalesModel sucursal) {
        final EditText editText = new EditText(getContext());
        editText.setText(sucursal.getNombre());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new OrdenNameUpdater(sucursal));
        return editText;
    }

    private View renderNombre(final SucursalesModel sucursal) {
        return renderString(sucursal.getNombre());
    }

    private View renderTelefono(final SucursalesModel sucursal) {
        return renderString(sucursal.getTelefono());
    }

    private View renderCorreo(final SucursalesModel sucursal) {
        return renderString(sucursal.getCorreo_electronico());
    }

    private View renderCalle(final SucursalesModel sucursal) {
        return renderString(sucursal.getCalle());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private SucursalesModel ordenToUpdate;

        public OrdenNameUpdater(SucursalesModel ordenToUpdate) {
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
            ordenToUpdate.setNombre(s.toString());
        }
    }}
