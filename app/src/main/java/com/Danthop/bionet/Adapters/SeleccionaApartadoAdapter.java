package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
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
import com.Danthop.bionet.Tables.SortableSeleccionaApartadoTable;
import com.Danthop.bionet.Tables.SortableSeleccionaOrdenEspecialTable;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.ConfiguracionesApartadoModel;
import com.Danthop.bionet.model.MinMaxFilter;
import com.Danthop.bionet.model.OrdenEspecialArticuloModel;
import com.Danthop.bionet.model.TicketModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class SeleccionaApartadoAdapter extends LongPressAwareTableDataAdapter<ArticuloApartadoModel> {

    int TEXT_SIZE = 16;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private List<ArticuloApartadoModel> ArticulosApartados;
    private TicketModel Ticket;

    private String usu_id;
    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalID;
    private List<ConfiguracionesApartadoModel> ListaConfiguraciones = new ArrayList<>();
    private List<EditText> ListaDeCantidades = new ArrayList<>();

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
                renderedView = renderMontoAApartar(apartado,rowIndex);
                break;
            case 4:
                renderedView = renderSeleccionar(apartado,ListaDeCantidades.get(rowIndex));
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

    private EditText renderMontoAApartar(final ArticuloApartadoModel apartado, int index) {

        EditText MontoAApartar = new EditText(getContext());
        MontoAApartar.setInputType(TYPE_CLASS_NUMBER);
        LoadConfiguracionesApartados(MontoAApartar,apartado);
        ListaDeCantidades.add(MontoAApartar);
        return MontoAApartar;

    }

    private View renderSeleccionar(final ArticuloApartadoModel apartado, final EditText Monto) {
        final CheckBox seleccionar = new CheckBox(getContext());
        seleccionar.setGravity(Gravity.CENTER);
        seleccionar.setPadding(10,10,10,10);

        seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seleccionar.isChecked())
                {
                    Monto.setEnabled(false);
                    ArticuloApartadoModel apartadoNuevo = new ArticuloApartadoModel(apartado.getCantidad(), apartado.getArticulo_id(),apartado.getArticulo_id_variante(),apartado.getArticulo_id_modificador(),
                            String.valueOf(Monto.getText()),apartado.getImporte_restante(),apartado.getNombre_articulo(),apartado.getId_existencias_origen(),apartado.getAplica_para_devolucion(),
                            apartado.getImporte_descuento(),apartado.getImporte_total(),apartado.getImpuestos(),
                            apartado.getPorcentaje_descuento(),apartado.getPrecio_articulo()
                            );
                    ArticulosApartados.add(apartadoNuevo);

                }else
                {
                    Monto.setEnabled(true);
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


            String ApiPath = getContext().getString(R.string.Url)+"/api/ventas/apartados/index?usu_id=" + usu_id + "&esApp=1&suc_id="+SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

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
                                        int DesdePrecio = Integer.parseInt((ListaConfiguraciones.get(j).getDesde()));
                                        int HastaPrecio = Integer.parseInt((ListaConfiguraciones.get(j).getHasta()));
                                        if(precio>=DesdePrecio&&precio<=HastaPrecio)
                                        {
                                            if(ListaConfiguraciones.get(j).getTipo().equals("porcentaje"))
                                            {
                                                cantidad.setHint(ValorTipo+" - 100%");
                                            }
                                            else if (ListaConfiguraciones.get(j).getTipo().equals("monto"))
                                            {
                                                cantidad.setHint(ValorTipo);
                                            }
                                            int cantidadMinima = Integer.parseInt(ValorTipo);
                                            cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                            @Override
                                            public void onFocusChange(View v, boolean hasFocus) {
                                                if (!hasFocus) {
                                                    String cantidad_ingresada = String.valueOf(cantidad.getText());
                                                        if(cantidad_ingresada.equals(""))
                                                        {

                                                        }else
                                                        {
                                                            int cant_int = Integer.parseInt(cantidad_ingresada);
                                                            if(cant_int<cantidadMinima)
                                                            {
                                                                Toast toast1 =
                                                                        Toast.makeText(getContext(), "cantidad menor a la requerida", Toast.LENGTH_LONG);
                                                                toast1.show();
                                                                cantidad.setText("");
                                                            }
                                                        }
                                                    }
                                                }
                                            });
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