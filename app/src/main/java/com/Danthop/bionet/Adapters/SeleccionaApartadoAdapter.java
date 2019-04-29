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

public class SeleccionaApartadoAdapter extends LongPressAwareTableDataAdapter<ArticuloApartadoModel> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private List<ArticuloApartadoModel> ArticulosApartados;
    private TicketModel Ticket;

    private String usu_id;
    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalID;
    private List<ConfiguracionesApartadoModel> ListaConfiguraciones = new ArrayList<>();

    String ValorTipo;
    String TipoPorcentajeOMonto;

    String Desde;
    String Hasta;


    public SeleccionaApartadoAdapter(final Context context, final List<ArticuloApartadoModel> data, final SortableSeleccionaApartadoTable tableView,
                                     TicketModel ticket, List<ArticuloApartadoModel> articulosApartados,
                                     String Usu_ID, Spinner spinnerSucursal, ArrayList<String> sucursalID ) {
        super(context, data, tableView);
        Ticket = ticket;
        ArticulosApartados = articulosApartados;
        usu_id=Usu_ID;
        SpinnerSucursal=spinnerSucursal;
        SucursalID=sucursalID;

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
        double ImporteTotal = Double.parseDouble( apartado.getImporte_total() );
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( ImporteTotal ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderMontoAApartar(final ArticuloApartadoModel apartado) {
        EditText MontoAApartar = new EditText(getContext());
        LoadConfiguracionesApartados(MontoAApartar,apartado);


        return MontoAApartar;
    }

    private View renderSeleccionar(final ArticuloApartadoModel apartado) {
        final CheckBox seleccionar = new CheckBox(getContext());
        seleccionar.setGravity(Gravity.CENTER);
        seleccionar.setPadding(10,10,10,10);

        seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seleccionar.isChecked())
                {

                    ArticuloApartadoModel apartadoNuevo = new ArticuloApartadoModel(apartado.getCantidad(), apartado.getArticulo_id(),apartado.getArticulo_id_variante(),apartado.getArticulo_id_modificador(),
                            apartado.getImporte_pagado(),apartado.getImporte_restante(),apartado.getNombre_articulo(),apartado.getId_existencias_origen(),apartado.getAplica_para_devolucion(),
                            apartado.getImporte_descuento(),apartado.getImporte_total(),apartado.getImpuestos(),
                            apartado.getPorcentaje_descuento(),apartado.getPrecio_articulo()
                            );
                    ArticulosApartados.add(apartadoNuevo);

                }else
                {
                    for(int i=0;i<ArticulosApartados.size();i++)
                    {
                        if(apartado.getArticulo_id().equals(ArticulosApartados.get(i).getArticulo_id()))
                        {
                            ArticulosApartados.remove(i);
                        }

                    }
                }

            }
        });

        return seleccionar;
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

    private void LoadConfiguracionesApartados(final EditText cantidad,final ArticuloApartadoModel apartado)
    {
        try {


            String ApiPath = "http://187.189.192.150:8010/api/ventas/apartados/index?usu_id=" + usu_id + "&esApp=1&suc_id="+SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject resultado = response.getJSONObject("resultado");
                                    JSONArray configuraciones = resultado.getJSONArray("aConfiguraciones");
                                    for (int x = 0; x < configuraciones.length(); x++) {
                                        JSONObject nodoConfiguracion = configuraciones.getJSONObject(x);
                                        String Habilitada = nodoConfiguracion.getString("cap_habilitada");
                                        if(Habilitada.equals("true"))
                                        {
                                            JSONObject nodoDesde = nodoConfiguracion.getJSONObject("cap_desde");
                                            Desde = nodoDesde.getString("value");
                                            JSONObject nodoHasta = nodoConfiguracion.getJSONObject("cap_hasta");
                                            Hasta = nodoHasta.getString("value");
                                            JSONObject nodoMontoOPorcentaje = nodoConfiguracion.getJSONObject("cap_monto_porcentaje");
                                            TipoPorcentajeOMonto = nodoConfiguracion.getString("cap_tipo_apartado");
                                            ValorTipo = nodoMontoOPorcentaje.getString("value");

                                            ConfiguracionesApartadoModel configuracion = new ConfiguracionesApartadoModel(Desde,Hasta,TipoPorcentajeOMonto,ValorTipo);
                                            ListaConfiguraciones.add(configuracion);

                                        }
                                    }

                                    float precio = Float.parseFloat((apartado.getPrecio_articulo()));
                                    for(int j=0; j<ListaConfiguraciones.size();j++)
                                    {
                                        float DesdePrecio = Float.parseFloat((ListaConfiguraciones.get(j).getDesde()));
                                        float HastaPrecio = Float.parseFloat((ListaConfiguraciones.get(j).getHasta()));
                                        if(precio>=DesdePrecio&&precio<=HastaPrecio)
                                        {
                                            if(ListaConfiguraciones.get(j).getTipo().equals("porcentaje"))
                                            {
                                                cantidad.setHint("0%");
                                                cantidad.setText(ValorTipo+"%");
                                            }
                                            else if (ListaConfiguraciones.get(j).getTipo().equals("monto"))
                                            {
                                                cantidad.setHint("$0.00");
                                                double CantidadConDecimal = Double.parseDouble(ValorTipo);
                                                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                                cantidad.setText(formatter.format(CantidadConDecimal));
                                            }
                                        }
                                    }


                                }
                            } catch (JSONException e) {
                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                String.valueOf(e), Toast.LENGTH_LONG);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(error), Toast.LENGTH_LONG);
                        }
                    }
            );
            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
    }
}