package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableApartadoDetalleTable;
import com.Danthop.bionet.Tables.SortableApartadoTable;
import com.Danthop.bionet.Tables.SortableSeleccionaApartadoTable;
import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.ConfiguracionLealtadModel;
import com.Danthop.bionet.model.ConfiguracionesApartadoModel;
import com.Danthop.bionet.model.TicketModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.bouncycastle.asn1.cmp.CAKeyUpdAnnContent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class DetalleApartadoAdapter extends LongPressAwareTableDataAdapter<ArticuloApartadoModel> {

    int TEXT_SIZE = 16;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();

    public DetalleApartadoAdapter(final Context context, final List<ArticuloApartadoModel> data, final SortableApartadoDetalleTable tableView) {
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
                renderedView = renderImporteRestante(apartado);
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
        double ImporteTotal = Double.parseDouble( apartado.getImporte_total() );
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( ImporteTotal ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderImporteRestante(final ArticuloApartadoModel apartado) {
        double ImporteTotal = Double.parseDouble( apartado.getImporte_restante());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( ImporteTotal ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
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