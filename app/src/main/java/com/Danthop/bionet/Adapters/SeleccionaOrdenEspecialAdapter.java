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

public class SeleccionaOrdenEspecialAdapter extends LongPressAwareTableDataAdapter<OrdenEspecialArticuloModel> {

    int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private List<OrdenEspecialArticuloModel> ArticulosOrdenados;
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


    public SeleccionaOrdenEspecialAdapter(final Context context, final List<OrdenEspecialArticuloModel> data, final SortableSeleccionaOrdenEspecialTable tableView,
                                     TicketModel ticket, List<OrdenEspecialArticuloModel> articulosOrdenados,
                                     String Usu_ID, Spinner spinnerSucursal, ArrayList<String> sucursalID ) {
        super(context, data, tableView);
        Ticket = ticket;
        ArticulosOrdenados = articulosOrdenados;
        usu_id=Usu_ID;
        SpinnerSucursal=spinnerSucursal;
        SucursalID=sucursalID;

    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        OrdenEspecialArticuloModel orden = getRowData(rowIndex);
        View renderedView = null;
        EditText Monto=null;

        switch (columnIndex) {
            case 0:
                renderedView = renderNombreArticulo(orden);
                break;
            case 1:
                renderedView = renderCantidad(orden);
                break;
            case 2:
                renderedView = renderImporteTotal(orden);
                break;
            case 3:
                renderedView = renderMontoAApartar(orden,rowIndex);
                break;
            case 4:
                renderedView = renderSeleccionar(orden,ListaDeCantidades.get(rowIndex));
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final OrdenEspecialArticuloModel orden = getRowData(rowIndex);

        return renderString("");
    }

    private View renderNombreArticulo(final OrdenEspecialArticuloModel orden) {
        return renderString(orden.getNombre_articulo());
    }

    private View renderCantidad(final OrdenEspecialArticuloModel orden) {
        return renderString(orden.getCantidad());
    }

    private View renderImporteTotal(final OrdenEspecialArticuloModel orden) {
        double ImporteTotal = Double.parseDouble( orden.getImporte_total() );
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( ImporteTotal ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private EditText renderMontoAApartar(final OrdenEspecialArticuloModel orden, int index) {

        EditText MontoAApartar = new EditText(getContext());
        MontoAApartar.setInputType(TYPE_CLASS_NUMBER);
        LoadConfiguracionesApartados(MontoAApartar,orden);
        ListaDeCantidades.add(MontoAApartar);
        return MontoAApartar;

    }

    private View renderSeleccionar(final OrdenEspecialArticuloModel orden, final EditText Monto) {
        final CheckBox seleccionar = new CheckBox(getContext());
        seleccionar.setGravity(Gravity.CENTER);
        seleccionar.setPadding(10,10,10,10);

        seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seleccionar.isChecked())
                {
                    Monto.setEnabled(false);
                    OrdenEspecialArticuloModel apartadoNuevo = new OrdenEspecialArticuloModel(orden.getCantidad(), orden.getArticulo_id(),orden.getArticulo_id_variante(),orden.getArticulo_id_modificador(),
                            String.valueOf(Monto.getText()),orden.getImporte_restante(),orden.getNombre_articulo(),orden.getAplica_para_devolucion(),
                            orden.getImporte_descuento(),orden.getImporte_total(),orden.getImpuestos(),
                            orden.getPorcentaje_descuento(),orden.getPrecio_articulo()
                    );
                    ArticulosOrdenados.add(apartadoNuevo);

                }else
                {
                    Monto.setEnabled(true);
                    for(int i=0;i<ArticulosOrdenados.size();i++)
                    {
                        if(orden.getArticulo_id().equals(ArticulosOrdenados.get(i).getArticulo_id()))
                        {
                            ArticulosOrdenados.remove(i);
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

    private void LoadConfiguracionesApartados(final EditText cantidad,final OrdenEspecialArticuloModel orden)
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

                                    float precio = Float.parseFloat((orden.getPrecio_articulo()));
                                    for(int j=0; j<ListaConfiguraciones.size();j++)
                                    {
                                        int DesdePrecio = Integer.parseInt((ListaConfiguraciones.get(j).getDesde()));
                                        int HastaPrecio = Integer.parseInt((ListaConfiguraciones.get(j).getHasta()));
                                        if(precio>=DesdePrecio&&precio<=HastaPrecio)
                                        {
                                            if(ListaConfiguraciones.get(j).getTipo().equals("porcentaje"))
                                            {
                                                cantidad.setHint("0 - 100%");
                                                cantidad.setText(ValorTipo);
                                            }
                                            else if (ListaConfiguraciones.get(j).getTipo().equals("monto"))
                                            {
                                                cantidad.setHint(DesdePrecio+" - "+HastaPrecio);
                                            }
                                            cantidad.setFilters(new InputFilter[]{ new MinMaxFilter(DesdePrecio, HastaPrecio)});
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