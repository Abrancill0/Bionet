package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableApartadoTable;
import com.Danthop.bionet.Tables.SortableSeleccionaApartadoTable;
import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.ClienteModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class SeleccionaApartadoAdapter extends LongPressAwareTableDataAdapter<ArticuloApartadoModel> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public SeleccionaApartadoAdapter(final Context context, final List<ArticuloApartadoModel> data, final SortableSeleccionaApartadoTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ArticuloApartadoModel apartado = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderNombreArticulo(apartado);
                break;
            case 1:
                renderedView = renderCantidad(apartado);
                break;
            case 2:
                renderedView = renderImporteTotal(apartado);
                break;
            case 3:
                renderedView = renderMontoAApartar(apartado);
                break;
            case 4:
                renderedView = renderSeleccionar(apartado);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ArticuloApartadoModel apartado = getRowData(rowIndex);

        return renderString("");
    }


    private View renderNombreArticulo(final ArticuloApartadoModel apartado) {
        return renderString(apartado.getNombre_articulo());
    }

    private View renderCantidad(final ArticuloApartadoModel apartado) {
        return renderString(apartado.getCantidad());
    }

    private View renderImporteTotal(final ArticuloApartadoModel apartado) {
        return renderString(apartado.getImporte_total());
    }

    private View renderMontoAApartar(final ArticuloApartadoModel apartado) {
        EditText MontoAApartar = new EditText(getContext());

        return MontoAApartar;
    }

    private View renderSeleccionar(final ArticuloApartadoModel apartado) {
        final CheckBox Aniadir = new CheckBox(getContext());
        Aniadir.setGravity(Gravity.CENTER);

        Aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Aniadir.isChecked())
                {

                }else
                {

                }

            }
        });

        return Aniadir;
    }

    private View renderString ( final String value){
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private ArticuloApartadoModel ordenToUpdate;

        public OrdenNameUpdater(ArticuloApartadoModel ordenToUpdate) {
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
            ordenToUpdate.getNombre_articulo();
        }
    }
}