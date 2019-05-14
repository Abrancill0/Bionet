package com.Danthop.bionet;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ApartadoAdapter;
import com.Danthop.bionet.Adapters.DetalleApartadoAdapter;
import com.Danthop.bionet.Adapters.DetalleOrdenEspecialAdapter;
import com.Danthop.bionet.Adapters.MetodoPagoAdapter;
import com.Danthop.bionet.Adapters.MovimientoAdapter;
import com.Danthop.bionet.Adapters.OrdenEspecialAdapter;
import com.Danthop.bionet.Tables.SortableApartadoDetalleTable;
import com.Danthop.bionet.Tables.SortableApartadoTable;
import com.Danthop.bionet.Tables.SortableMovimientoTable;
import com.Danthop.bionet.Tables.SortableOrdenEspecialDetalleTable;
import com.Danthop.bionet.Tables.SortableOrdenEspecialTable;
import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.Impuestos;
import com.Danthop.bionet.model.MovimientoModel;
import com.Danthop.bionet.model.OrdenEspecialArticuloModel;
import com.Danthop.bionet.model.OrdenEspecialModel;
import com.Danthop.bionet.model.PagoModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ventas_transacciones extends Fragment {

    private Dialog dialog;
    private View layout_movimientos;
    private View layout_apartado;
    private View layout_ordenes;
    private View layout_fechas;
    private View layout_movimientos_spinners;

    private View btn_movimientos;
    private View btn_apartado;
    private View btn_ordenes_especiales;

    private Button btn_corte_caja;
    private Button btn_ventas;

    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;

    private Spinner SpinnerFormaPago;
    private ArrayList<String> FormaPagoName;
    private ArrayList<String> FormaPagoID;

    private Spinner SpinnerUsuarioVenta;
    private ArrayList<String> UsuarioVentaName;
    private ArrayList<String> UsuarioVentaID;

    private List<ApartadoModel> Lista_de_apartados= new ArrayList<>();

    private List<OrdenEspecialModel> Lista_de_ordenes= new ArrayList<>();

    private List<MovimientoModel> Lista_de_movimientos= new ArrayList<>();

    private SortableApartadoTable TablaApartados;
    private SortableOrdenEspecialTable TablaOrdenes;
    private SortableMovimientoTable TablaMovimientos;

    private Button Abrir_calendarioInicial;
    private Button Abrir_calendarioFinal;
    private CalendarPickerView calendarView;
    private Date FechaInicial;

    private String fecha_inicio="";
    private String fecha_final="";

    private String usu_id;

    private FragmentTransaction fr;

    private TextView transacciones;
    private TextView total_ventas;
    private TextView best_seller;

    private String content;
    private WebView webView;


    private String NombreSucursal="";
    private String NumeroSucursal="";
    private String Direccion="";
    private String RazonSocial="";
    private String RFC="";
    private String LogoNegocio="";
    private String FechaCreacion="";




    public Fragment_ventas_transacciones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas_transacciones,container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        fr = getFragmentManager().beginTransaction();
        layout_movimientos = v.findViewById(R.id.layout_movimientos);
        layout_movimientos_spinners = v.findViewById(R.id.movimientos_spinners);
        layout_apartado = v.findViewById(R.id.layout_apartado);
        layout_ordenes = v.findViewById(R.id.layout_ordenes);
        layout_fechas = v.findViewById(R.id.layout_fechas);
        btn_movimientos = v.findViewById(R.id.btn_movimientos);
        btn_apartado = v.findViewById(R.id.btn_apartado);
        btn_ordenes_especiales = v.findViewById(R.id.btn_ordenes_especiales);

        transacciones = v.findViewById(R.id.transacciones);
        total_ventas = v.findViewById(R.id.totalVentas);
        best_seller = v.findViewById(R.id.masVendido);

        Abrir_calendarioInicial = v.findViewById(R.id.abrir_calendarioInicial);
        Abrir_calendarioFinal = v.findViewById(R.id.abrir_calendarioFinal);
        Abrir_calendarioFinal.setEnabled(false);

        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();
        SpinnerSucursal=(Spinner)v.findViewById(R.id.sucursal);

        FormaPagoName=new ArrayList<>();
        FormaPagoID = new ArrayList<>();
        SpinnerFormaPago=(Spinner)v.findViewById(R.id.FormaPago);

        UsuarioVentaName=new ArrayList<>();
        UsuarioVentaID = new ArrayList<>();
        SpinnerUsuarioVenta=(Spinner)v.findViewById(R.id.UsuarioVenta);


        TablaApartados = v.findViewById(R.id.tabla_apartados);
        TablaApartados.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        TablaOrdenes = v.findViewById(R.id.tabla_ordenes);
        TablaOrdenes.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia2));

        TablaMovimientos = v.findViewById(R.id.tabla_movimientos);
        TablaMovimientos.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia3));

        btn_ventas = v.findViewById(R.id.btn_ventas);
        btn_corte_caja = v.findViewById(R.id.btn_corte_caja);
        Fechas();

        TableDataClickListener<MovimientoModel> tablaListener = new TableDataClickListener<MovimientoModel>() {
            @Override
            public void onDataClicked(int rowIndex, final MovimientoModel clickedData) {
                loadTicket(clickedData);
            }
        };
        TablaMovimientos.addDataClickListener(tablaListener);

        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                LoadApartados();
                LoadOrdenes();
                LoadMovimientos();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        SpinnerFormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                LoadApartados();
                LoadOrdenes();
                LoadMovimientos();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        SpinnerUsuarioVenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                LoadApartados();
                LoadOrdenes();
                LoadMovimientos();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



        LoadSucursales();
        loadButtons();
        Layouts();

        return v;
    }

    public void Layouts()
    {
        layout_movimientos.setVisibility(View.GONE);
        layout_apartado.setVisibility(View.GONE);
        layout_ordenes.setVisibility(View.GONE);
        layout_fechas.setVisibility(View.INVISIBLE);
        layout_movimientos_spinners.setVisibility(View.INVISIBLE);

    }

    public void loadButtons(){
        btn_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new Fragment_Ventas()).commit();
            }
        });

        btn_corte_caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_caja_listado()).commit();
            }
        });

        btn_movimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_movimientos.getVisibility()==View.GONE)
                {
                    layout_movimientos.setVisibility(View.VISIBLE);
                    layout_fechas.setVisibility(View.VISIBLE);
                    layout_movimientos_spinners.setVisibility(View.VISIBLE);
                    layout_ordenes.setVisibility(View.GONE);
                    layout_apartado.setVisibility(View.GONE);
                }
                else
                {

                }
            }
        });

        btn_apartado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_apartado.getVisibility()==View.GONE)
                {
                    layout_movimientos.setVisibility(View.GONE);
                    layout_fechas.setVisibility(View.INVISIBLE);
                    layout_movimientos_spinners.setVisibility(View.INVISIBLE);
                    layout_ordenes.setVisibility(View.GONE);
                    layout_apartado.setVisibility(View.VISIBLE);
                }
                else
                {

                }
            }
        });

        btn_ordenes_especiales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_ordenes.getVisibility()==View.GONE)
                {
                    layout_movimientos.setVisibility(View.GONE);
                    layout_fechas.setVisibility(View.INVISIBLE);
                    layout_movimientos_spinners.setVisibility(View.INVISIBLE);
                    layout_ordenes.setVisibility(View.VISIBLE);
                    layout_apartado.setVisibility(View.GONE);
                }
                else
                {

                }
            }
        });

    }

    public void LoadSucursales()
    {
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/configuracion/sucursales/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONArray RespuestaNodoSucursales= null;
                JSONObject RespuestaNodoID = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");
                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoSucursales = Respuesta.getJSONArray("aSucursales");

                        for(int x = 0; x < RespuestaNodoSucursales.length(); x++){
                            JSONObject jsonObject1=RespuestaNodoSucursales.getJSONObject(x);
                            String sucursal=jsonObject1.getString("suc_nombre");
                            SucursalName.add(sucursal);
                            RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                            String id=RespuestaNodoID.getString("uuid");
                            SucursalID.add(id);
                        }
                        SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();


                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();


                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();


                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);


    }

    public void LoadApartados(){
        Lista_de_apartados.clear();
        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/apartados/index?usu_id=" + usu_id + "&esApp=1&suc_id="+ SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject RespuestaResultado = response.getJSONObject("resultado");

                                    JSONArray InfoApartado = RespuestaResultado.getJSONArray("aApartados");
                                    for (int f=0; f<InfoApartado.length();f++)
                                    {
                                        JSONObject elemento = InfoApartado.getJSONObject(f);
                                        JSONObject NodoIDTicket = elemento.getJSONObject("apa_id_ticket");
                                        String IDTicket = NodoIDTicket.getString("uuid");
                                        JSONObject NodoIDSucursal = elemento.getJSONObject("apa_id_sucursal");
                                        String IDSucursal = NodoIDSucursal.getString("uuid");
                                        JSONObject NodoIDCliente = elemento.getJSONObject("apa_id_cliente");
                                        String IDCliente = NodoIDCliente.getString("uuid");
                                        String Cliente = elemento.getString("cli_nombre");
                                        String Sucursal = elemento.getString("suc_nombre");
                                        String FechaDeApartado = elemento.getString("apa_fecha_hora_creo");
                                        String MontoPagado = elemento.getString("apa_importe_pagado");
                                        String MontoRestante = elemento.getString("apa_importe_restante");
                                        String FechaDeVencimiento = elemento.getString("apa_fecha_hora_vencimiento");
                                        String FechaDeCreacion = elemento.getString("apa_fecha_hora_creo");
                                        String Estatus = elemento.getString("apa_estatus");

                                        List<ArticuloApartadoModel> ListaDeArticulosApartados = new ArrayList<>();
                                        JSONArray Articulos = elemento.getJSONArray("aArticulosApartados");
                                        for(int i= 0; i<Articulos.length(); i++)
                                        {
                                            JSONObject elemento2 = Articulos.getJSONObject(i);
                                            String CantidadApartada = elemento2.getString("aar_cantidad");
                                            JSONObject NodoID = elemento2.getJSONObject("aar_id");
                                            String ArticuloIDApartado = NodoID.getString("uuid");
                                            JSONObject NodoIDVariante = elemento2.getJSONObject("aar_id_variante");
                                            String ArticuloIDVariante = NodoIDVariante.getString("uuid");
                                            String ArticuloIDModificador = elemento2.getString("aar_id_modificador");
                                            String ArticuloImportePagado = elemento2.getString("aar_importe_pagado");
                                            String ArticuloImporteRestante = elemento2.getString("aar_importe_restante");
                                            String ArticuloNombreApartado = elemento2.getString("aar_nombre_articulo");
                                            JSONObject NodoExistenciasOrigen = elemento2.getJSONObject("aar_id_existencias_origen");
                                            String ArticuloExistenciasOrigen = NodoExistenciasOrigen.getString("uuid");
                                            String ArticuloAplicaDevolucion = elemento2.getString("aar_aplica_para_devolucion");
                                            JSONObject NodoImporteDescuento = elemento2.getJSONObject("aar_importe_descuento");
                                            String ArticuloImporteDescuento = NodoImporteDescuento.getString("value");
                                            JSONObject NodoImporteTotal = elemento2.getJSONObject("aar_importe_total");
                                            String ArticuloImporteTotal = NodoImporteTotal.getString("value");

                                            JSONObject NodoImpuestos = elemento2.getJSONObject("aar_impuestos");
                                            JSONArray NodoImpuestoID = NodoImpuestos.getJSONArray("keys");
                                            JSONArray NodoImpuestoValue = NodoImpuestos.getJSONArray("values");
                                            List<Impuestos> ListaDeImpuestos = new ArrayList<>();
                                            for(int z=0;z<NodoImpuestoID.length();z++)
                                            {
                                                String ImpuestoID = NodoImpuestoID.getString(z);
                                                String ImpuestoValue = NodoImpuestoValue.getString(z);
                                                Impuestos Impuesto = new Impuestos(ImpuestoID,ImpuestoValue);
                                                ListaDeImpuestos.add(Impuesto);
                                            }

                                            JSONObject NodoPorcentajeDescuento = elemento2.getJSONObject("aar_porcentaje_descuento");
                                            String ArticuloPorcentajeDescuento = NodoPorcentajeDescuento.getString("value");
                                            JSONObject NodoPrecioArticulo = elemento2.getJSONObject("aar_precio_articulo");
                                            String ArticuloPrecio = NodoPrecioArticulo.getString("value");

                                            ArticuloApartadoModel ArticuloApartado = new ArticuloApartadoModel(
                                                    CantidadApartada,
                                                    ArticuloIDApartado,
                                                    ArticuloIDVariante,
                                                    ArticuloIDModificador,
                                                    ArticuloImportePagado,
                                                    ArticuloImporteRestante,
                                                    ArticuloNombreApartado,
                                                    ArticuloExistenciasOrigen,
                                                    ArticuloAplicaDevolucion,
                                                    ArticuloImporteDescuento,
                                                    ArticuloImporteTotal,
                                                    ListaDeImpuestos,
                                                    ArticuloPorcentajeDescuento,
                                                    ArticuloPrecio
                                            );
                                            ListaDeArticulosApartados.add(ArticuloApartado);

                                        }

                                        ApartadoModel Apartado = new ApartadoModel(
                                                IDTicket,
                                                IDCliente,
                                                Cliente,
                                                IDSucursal,
                                                Sucursal,
                                                MontoPagado,
                                                MontoRestante,
                                                FechaDeVencimiento,
                                                FechaDeCreacion,
                                                Estatus,
                                                ListaDeArticulosApartados
                                        );
                                        Lista_de_apartados.add(Apartado);
                                    }

                                    final ApartadoAdapter apartadoAdapter = new ApartadoAdapter(getContext(),Lista_de_apartados,TablaApartados);
                                    apartadoAdapter.notifyDataSetChanged();
                                    TablaApartados.setDataAdapter(apartadoAdapter);


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
        TableDataClickListener<ApartadoModel> tablaListener = new TableDataClickListener<ApartadoModel>() {
            @Override
            public void onDataClicked(int rowIndex, final ApartadoModel clickedData) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pop_up_ventas_detalle_apartado);
                dialog.show();
                SortableApartadoDetalleTable DetalleApartadoTable = dialog.findViewById(R.id.detalle_apartado_table);
                DetalleApartadoTable.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia_detalle_apartado));
                final DetalleApartadoAdapter detalleApartadoAdapter = new DetalleApartadoAdapter(getContext(),clickedData.getArticulosApartados(),DetalleApartadoTable);
                DetalleApartadoTable.setDataAdapter(detalleApartadoAdapter);

                Button aceptar = dialog.findViewById(R.id.btn_aceptar_cerrar);
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        };
        TablaApartados.addDataClickListener(tablaListener);


    }

    public void LoadOrdenes(){
        Lista_de_ordenes.clear();
        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/ordenes_especiales/index?usu_id=" + usu_id + "&esApp=1&suc_id="+ SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject RespuestaResultado = response.getJSONObject("resultado");

                                    JSONArray InfoApartado = RespuestaResultado.getJSONArray("aOrdenesEspeciales");
                                    for (int f=0; f<InfoApartado.length();f++)
                                    {
                                        JSONObject elemento = InfoApartado.getJSONObject(f);
                                        JSONObject NodoIDTicket = elemento.getJSONObject("oes_id_ticket");
                                        String IDTicket = NodoIDTicket.getString("uuid");
                                        JSONObject NodoIDSucursal = elemento.getJSONObject("oes_id_sucursal");
                                        String IDSucursal = NodoIDSucursal.getString("uuid");
                                        JSONObject NodoIDCliente = elemento.getJSONObject("oes_id_cliente");
                                        String IDCliente = NodoIDCliente.getString("uuid");
                                        String Cliente = elemento.getString("cli_nombre");
                                        String Sucursal = elemento.getString("suc_nombre");
                                        String FechaDeApartado = elemento.getString("oes_fecha_hora_creo");
                                        String MontoPagado = elemento.getString("oes_importe_pagado");
                                        String MontoRestante = elemento.getString("oes_importe_restante");
                                        String FechaDeVencimiento = elemento.getString("oes_fecha_hora_vencimiento");
                                        String FechaDeCreacion = elemento.getString("oes_fecha_hora_creo");
                                        String Estatus = elemento.getString("oes_estatus");

                                        List<OrdenEspecialArticuloModel> ListaDeArticulosOrdenados = new ArrayList<>();
                                        JSONArray Articulos = elemento.getJSONArray("aArticulosOrdenados");
                                        for(int i= 0; i<Articulos.length(); i++)
                                        {
                                            JSONObject elemento2 = Articulos.getJSONObject(i);
                                            String CantidadApartada = elemento2.getString("oea_cantidad");
                                            JSONObject NodoID = elemento2.getJSONObject("oea_id");
                                            String ArticuloIDApartado = NodoID.getString("uuid");
                                            JSONObject NodoIDVariante = elemento2.getJSONObject("oea_id_variante");
                                            String ArticuloIDVariante = NodoIDVariante.getString("uuid");
                                            String ArticuloIDModificador = elemento2.getString("oea_id_modificador");
                                            String ArticuloImportePagado = elemento2.getString("oea_importe_pagado");
                                            String ArticuloImporteRestante = elemento2.getString("oea_importe_restante");
                                            String ArticuloNombreApartado = elemento2.getString("oea_nombre_articulo");
                                            String ArticuloAplicaDevolucion = elemento2.getString("oea_aplica_para_devolucion");
                                            JSONObject NodoImporteDescuento = elemento2.getJSONObject("oea_importe_descuento");
                                            String ArticuloImporteDescuento = NodoImporteDescuento.getString("value");
                                            JSONObject NodoImporteTotal = elemento2.getJSONObject("oea_importe_total");
                                            String ArticuloImporteTotal = NodoImporteTotal.getString("value");

                                            JSONObject NodoImpuestos = elemento2.getJSONObject("oea_impuestos");
                                            JSONArray NodoImpuestoID = NodoImpuestos.getJSONArray("keys");
                                            JSONArray NodoImpuestoValue = NodoImpuestos.getJSONArray("values");
                                            List<Impuestos> ListaDeImpuestos = new ArrayList<>();
                                            for(int z=0;z<NodoImpuestoID.length();z++)
                                            {
                                                String ImpuestoID = NodoImpuestoID.getString(z);
                                                String ImpuestoValue = NodoImpuestoValue.getString(z);
                                                Impuestos Impuesto = new Impuestos(ImpuestoID,ImpuestoValue);
                                                ListaDeImpuestos.add(Impuesto);
                                            }

                                            JSONObject NodoPorcentajeDescuento = elemento2.getJSONObject("oea_porcentaje_descuento");
                                            String ArticuloPorcentajeDescuento = NodoPorcentajeDescuento.getString("value");
                                            JSONObject NodoPrecioArticulo = elemento2.getJSONObject("oea_precio_articulo");
                                            String ArticuloPrecio = NodoPrecioArticulo.getString("value");

                                            OrdenEspecialArticuloModel ArticuloOrdenado = new OrdenEspecialArticuloModel(
                                                    CantidadApartada,
                                                    ArticuloIDApartado,
                                                    ArticuloIDVariante,
                                                    ArticuloIDModificador,
                                                    ArticuloImportePagado,
                                                    ArticuloImporteRestante,
                                                    ArticuloNombreApartado,
                                                    ArticuloAplicaDevolucion,
                                                    ArticuloImporteDescuento,
                                                    ArticuloImporteTotal,
                                                    ListaDeImpuestos,
                                                    ArticuloPorcentajeDescuento,
                                                    ArticuloPrecio
                                            );
                                            ListaDeArticulosOrdenados.add(ArticuloOrdenado);

                                        }

                                        OrdenEspecialModel Orden = new OrdenEspecialModel(
                                                IDTicket,
                                                IDCliente,
                                                Cliente,
                                                IDSucursal,
                                                Sucursal,
                                                MontoPagado,
                                                MontoRestante,
                                                FechaDeVencimiento,
                                                FechaDeCreacion,
                                                Estatus,
                                                ListaDeArticulosOrdenados
                                        );
                                        Lista_de_ordenes.add(Orden);
                                    }

                                    final OrdenEspecialAdapter ordenAdapter = new OrdenEspecialAdapter(getContext(),Lista_de_ordenes,TablaOrdenes);
                                    ordenAdapter.notifyDataSetChanged();
                                    TablaOrdenes.setDataAdapter(ordenAdapter);


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
        TableDataClickListener<OrdenEspecialModel> tablaListener = new TableDataClickListener<OrdenEspecialModel>() {
            @Override
            public void onDataClicked(int rowIndex, final OrdenEspecialModel clickedData) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pop_up_ventas_detalle_orden);
                dialog.show();
                SortableOrdenEspecialDetalleTable DetalleOrdenTable = dialog.findViewById(R.id.detalle_orden_table);
                DetalleOrdenTable.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia_detalle_orden));
                final DetalleOrdenEspecialAdapter detalleApartadoAdapter = new DetalleOrdenEspecialAdapter(getContext(),clickedData.getArticulosOrdenados(),DetalleOrdenTable);
                DetalleOrdenTable.setDataAdapter(detalleApartadoAdapter);

                Button aceptar = dialog.findViewById(R.id.btn_aceptar_cerrar);
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        };
        TablaOrdenes.addDataClickListener(tablaListener);


    }


    public void LoadMovimientos(){
        Lista_de_movimientos.clear();
        if(FormaPagoName.isEmpty()&&UsuarioVentaName.isEmpty()) {
            try {

                String url = getString(R.string.Url);

                String ApiPath;

                ApiPath = url + "/api/ventas/movimientos/index?" +
                        "usu_id=" + usu_id +
                        "&esApp=1" +
                        "&suc_id=" + SucursalID.get(SpinnerSucursal.getSelectedItemPosition()) +
                        "&fecha_inicial=" + "" +
                        "&fecha_final=" + "" +
                        "&forma_pago=" + "" +
                        "&usuario_venta=" + "";
                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                    if (EstatusApi == 1) {

                                        JSONObject RespuestaResultado = response.getJSONObject("resultado");

                                        transacciones.setText(RespuestaResultado.getString("floatNumeroTransacciones"));
                                        total_ventas.setText(RespuestaResultado.getString("floatTotalEnVentas"));
                                        best_seller.setText(RespuestaResultado.getString("stringArticuloMasVendido"));

                                        JSONArray Movimientos = RespuestaResultado.getJSONArray("aMovimientos");
                                        for (int f = 0; f < Movimientos.length(); f++) {
                                            JSONObject elemento = Movimientos.getJSONObject(f);
                                            String fecha = elemento.getString("mov_fecha_hora_creo");
                                            String NombreSucursal = elemento.getString("suc_nombre");
                                            String NumSucursal = elemento.getString("suc_numero");
                                            String Monto = elemento.getString("tic_importe_total");

                                            JSONArray Articulos = elemento.getJSONArray("aArticulos");
                                            JSONObject Tic_id = elemento.getJSONObject("tic_id");
                                            String Movimiento_tic_id = Tic_id.getString("uuid");
                                            JSONObject nodo = Articulos.getJSONObject(0);
                                            String Articulo = nodo.getString("tar_nombre_articulo");

                                            MovimientoModel movimiento = new MovimientoModel(fecha, NombreSucursal, NumSucursal, Articulo, Monto,Movimiento_tic_id);
                                            Lista_de_movimientos.add(movimiento);
                                        }

                                        JSONArray FormasPago = RespuestaResultado.getJSONArray("aFormasPago");
                                        for (int z = 0; z < FormasPago.length(); z++) {
                                            JSONObject elemento = FormasPago.getJSONObject(z);
                                            String id = elemento.getString("fpa_id");
                                            String nombre = elemento.getString("fpa_nombre");
                                            FormaPagoName.add(nombre);
                                            FormaPagoID.add(id);
                                        }

                                        JSONArray UsuariosVenta = RespuestaResultado.getJSONArray("aUsuarios");
                                        for (int z = 0; z < UsuariosVenta.length(); z++) {
                                            JSONObject elemento = UsuariosVenta.getJSONObject(z);
                                            JSONObject NodoId = elemento.getJSONObject("usu_id");
                                            String id = NodoId.getString("uuid");
                                            String nombre = elemento.getString("usu_nombre");
                                            UsuarioVentaName.add(nombre);
                                            UsuarioVentaID.add(id);
                                        }

                                        final MovimientoAdapter movimientoAdapter = new MovimientoAdapter(getContext(), Lista_de_movimientos, TablaMovimientos);
                                        movimientoAdapter.notifyDataSetChanged();
                                        TablaMovimientos.setDataAdapter(movimientoAdapter);

                                        SpinnerFormaPago.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, FormaPagoName));

                                        SpinnerUsuarioVenta.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, UsuarioVentaName));


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
        else
        {
            try {

                String url = getString(R.string.Url);

                String ApiPath;

                ApiPath = url + "/api/ventas/movimientos/index?" +
                        "usu_id=" + usu_id +
                        "&esApp=1" +
                        "&suc_id=" + SucursalID.get(SpinnerSucursal.getSelectedItemPosition()) +
                        "&fecha_inicial=" + fecha_inicio +
                        "&fecha_final=" + fecha_final +
                        "&forma_pago=" + FormaPagoID.get(SpinnerFormaPago.getSelectedItemPosition()) +
                        "&usuario_venta=" + UsuarioVentaID.get(SpinnerUsuarioVenta.getSelectedItemPosition());
                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                    if (EstatusApi == 1) {

                                        JSONObject RespuestaResultado = response.getJSONObject("resultado");

                                        transacciones.setText(RespuestaResultado.getString("floatNumeroTransacciones"));
                                        total_ventas.setText(RespuestaResultado.getString("floatTotalEnVentas"));
                                        best_seller.setText(RespuestaResultado.getString("stringArticuloMasVendido"));

                                        JSONArray Movimientos = RespuestaResultado.getJSONArray("aMovimientos");
                                        for (int f = 0; f < Movimientos.length(); f++) {
                                            JSONObject elemento = Movimientos.getJSONObject(f);
                                            String fecha = elemento.getString("mov_fecha_hora_creo");
                                            String NombreSucursal = elemento.getString("suc_nombre");
                                            String NumSucursal = elemento.getString("suc_numero");
                                            String Monto = elemento.getString("tic_importe_total");
                                            JSONObject Tic_id = elemento.getJSONObject("tic_id");
                                            String Movimiento_tic_id = Tic_id.getString("uuid");

                                            JSONArray Articulos = elemento.getJSONArray("aArticulos");
                                            JSONObject nodo = Articulos.getJSONObject(0);
                                            String Articulo = nodo.getString("tar_nombre_articulo");

                                            MovimientoModel movimiento = new MovimientoModel(fecha, NombreSucursal, NumSucursal, Articulo, Monto,Movimiento_tic_id);
                                            Lista_de_movimientos.add(movimiento);
                                        }

                                        final MovimientoAdapter movimientoAdapter = new MovimientoAdapter(getContext(), Lista_de_movimientos, TablaMovimientos);
                                        movimientoAdapter.notifyDataSetChanged();
                                        TablaMovimientos.setDataAdapter(movimientoAdapter);

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



    private void Fechas()
    {
        Abrir_calendarioInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog calendario = new Dialog(getContext());
                calendario.setContentView(R.layout.calendar);
                calendario.show();
                calendarView = calendario.findViewById(R.id.calendar_view);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -1);

                Calendar yearsAgo = Calendar.getInstance();
                yearsAgo.add(Calendar.YEAR, -100);

                Date today = new Date();
                calendarView.init(yearsAgo.getTime(), today).withSelectedDate(calendar.getTime());

                calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date date) {
                        FechaInicial = date;
                        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                        long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                        fecha_inicio = targetFormat.format(s);
                        calendario.dismiss();
                        Abrir_calendarioFinal.setEnabled(true);
                        Abrir_calendarioInicial.setText(fecha_inicio);

                        LoadApartados();
                        LoadOrdenes();
                        LoadMovimientos();
                    }

                    @Override
                    public void onDateUnselected(Date date) {

                    }
                });

            }
        });

        Abrir_calendarioFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog calendario = new Dialog(getContext());
                calendario.setContentView(R.layout.calendar);
                calendario.show();
                calendarView = calendario.findViewById(R.id.calendar_view);

                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);

                Date today = new Date();
                calendarView.init(FechaInicial, today).withSelectedDate(FechaInicial);

                calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date date) {
                        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                        long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                        fecha_final = targetFormat.format(s);
                        calendario.dismiss();
                        Abrir_calendarioFinal.setText(fecha_final);

                        LoadApartados();
                        LoadOrdenes();
                        LoadMovimientos();
                    }

                    @Override
                    public void onDateUnselected(Date date) {

                    }
                });

            }
        });
    }

    private void loadTicket(MovimientoModel movimiento)
    {
        try {

            String url = getString(R.string.Url);

            String ApiPath;

            ApiPath = url + "/api/ventas/movimientos/obtener_detalle_ticket?" +
                    "usu_id=" + usu_id +
                    "&esApp=1" +
                    "&tic_id="+ movimiento.getMovimiento_tic_id()+
                    "&suc_id=" + SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject RespuestaResultado = response.getJSONObject("resultado");

                                    JSONObject NodoASucursal = RespuestaResultado.getJSONObject("aSucursal");
                                    NombreSucursal = NodoASucursal.getString("suc_nombre");
                                    NumeroSucursal = NodoASucursal.getString("suc_numero");
                                    Direccion = NodoASucursal.getString("suc_direccion");
                                    RazonSocial = NodoASucursal.getString("suc_razon_social");
                                    RFC = NodoASucursal.getString("suc_rfc");
                                    LogoNegocio = NodoASucursal.getString("con_logo_negocio");

                                    JSONObject NodoTicket = RespuestaResultado.getJSONObject("aTicket");
                                    FechaCreacion = RespuestaResultado.getString("tic_fecha_hora_creo");


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

        content= "<style type=\"text/css\" media=\"all\">\n" +
                "#tblTicketTemplate {\n" +
                "  width: 100%;\n" +
                "  font-size: 10px;\n" +
                "  font-family: \"Courier New\";\n" +
                "  text-transform: uppercase;\n" +
                "  line-height: 1.3;\n" +
                "  border: solid 1px #D4DADF;\n" +
                "}\n" +
                "#tblTicketTemplate thead tr td {\n" +
                "  padding-left: 10%;\n" +
                "  padding-right: 10%;\n" +
                "}\n" +
                "#tblTicketTemplate #tdLogo {\n" +
                "  text-align: center;\n" +
                "  vertical-align: middle;\n" +
                "}\n" +
                "#tblTicketTemplate #tdFiscal{\n" +
                "  text-align: center;\n" +
                "  vertical-align: middle;\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "#tblTicketTemplate .trDivider{\n" +
                "  border-bottom: dashed 1px black;\n" +
                "}\n" +
                "#tblTicketTemplate .trDivider td {\n" +
                "  padding-bottom: 3px;\n" +
                "}\n" +
                "#tblTicketTemplate .trDivider + tr td {\n" +
                "  padding-top: 3px;\n" +
                "}\n" +
                "#tblTicketTemplate tbody .trInfo{\n" +
                "  font-size: 10px;\n" +
                "  vertical-align: top;\n" +
                "}\n" +
                "#tblTicketTemplate tbody .trInfo td a{\n" +
                "  text-align: center;\n" +
                "}\n" +
                "#tblTicketTemplate tbody #trTituloDetalle{\n" +
                "  font-weight: bold;\n" +
                "  text-align: center;\n" +
                "  border-top: dashed 1px black;\n" +
                "  border-bottom: dashed 1px black;\n" +
                "}\n" +
                "#tblTicketTemplate tbody .trArticulo{\n" +
                "  font-size: 10px;\n" +
                "}\n" +
                "#tblTicketTemplate .importe{\n" +
                "  text-align: right;\n" +
                "}\n" +
                "#tblTicketTemplate tbody .trArticulo + tr td{\n" +
                "  font-size: 9px;\n" +
                "}\n" +
                "#tblTicketTemplate tbody .trTotal{\n" +
                "  font-size: 14px;\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "#tblTicketTemplate tfoot{\n" +
                "  text-align: center;\n" +
                "}\n" +
                "</style>\n" +
                "<table id=\"tblTicketTemplate\">\n" +
                "  <thead>\n" +
                "    <tr>\n" +
                "      <td id=\"tdLogo\" colspan=\"4\">\n" +
                "        <img src="+getString(R.string.Url)+LogoNegocio+" class=\"img_con_logo_ticket\" width=\"100%\" height=\"75\" />\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td id=\"tdFiscal\" colspan=\"4\">\n" +
                "        "+RazonSocial+"<br />"+RFC+"\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr class=\"trDivider\">\n" +
                "      <td id=\"tdFiscal\" colspan=\"4\">\n" +
                "        <!--Funciones::formatDireccion($aDatos['aSucursalPrincipal']['suc_direccion'], \"suc_\")-->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td id=\"tdFiscal2\" colspan=\"4\">\n" +
                "        <!--Funciones::formatDireccion($aDatos['aSucursal']['suc_direccion'], \"suc_\")-->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr class=\"trDivider\">\n" +
                "      <td id=\"tdFiscalTelefono\" colspan=\"4\">\n" +
                "        <!--Tel.:  $aDatos['aSucursal']['suc_telefono'] -->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </thead>\n" +
                "  <tbody>\n" +
                "    <tr class=\"trInfo\">\n" +
                "      <td id=\"tdFechaHora\" colspan=\"2\">\n" +
                "        <!--b>Fec./Hr.:</b>  date(\"d/m/Y H:i\")-->\n" +
                "      </td>\n" +
                "      <td id=\"tdTicketNum\" colspan=\"2\">\n" +
                "        <b>Ticket:</b>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr class=\"trInfo\">\n" +
                "      <td id=\"tdVendedor\" colspan=\"4\">\n" +
                "        <!--b>Vendedor:</b>  Session::get(\"nombre\") -->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr class=\"trInfo\">\n" +
                "      <td id=\"tdCliente\" colspan=\"4\">\n" +
                "        <!--b>Cliente:</b>  Session::get(\"nombre\") -->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr id=\"trTituloDetalle\">\n" +
                "      <td>C.</td>\n" +
                "      <td>Articulo</td>\n" +
                "      <td>P.U.</td>\n" +
                "      <td>Importe</td>\n" +
                "    </tr>\n" +
                "    <tr id=\"tableListArticulos\">\n" +
                "      <!--tr class=\"trArticulo\">\n" +
                "        <td>1</td>\n" +
                "        <td>*Nombre del articulo</td>\n" +
                "        <td class=\"importe\">3.00</td>\n" +
                "        <td class=\"importe\">3.00 A</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td></td>\n" +
                "        <td colspan=\"2\">Descripción del articulo</td>\n" +
                "        <td></td>\n" +
                "      </tr-->\n" +
                "    </tr>\n" +
                "    <tr class=\"trDivider\">\n" +
                "      <td colspan=\"4\"></td>\n" +
                "    </tr>\n" +
                "\n" +
                "    <tr class=\"trTotales\">\n" +
                "      <td colspan=\"2\">Subtotal:</td>\n" +
                "      <td class=\"importe\">$</td>\n" +
                "      <td id=\"tdSubTotal\" class=\"importe\"></td>\n" +
                "    </tr>\n" +
                "    <tr id=\"trImpuestosTotales\" class=\"trTotales\">\n" +
                "      <!--td colspan=\"2\">Impuesto(9.0%):</td>\n" +
                "      <td class=\"importe\">$</td>\n" +
                "      <td class=\"importe\">123</td-->\n" +
                "    </tr>\n" +
                "    <tr class=\"trTotal\">\n" +
                "      <td colspan=\"2\">Total:</td>\n" +
                "      <td class=\"importe\">$</td>\n" +
                "      <td id=\"tdTotal\" class=\"importe\"></td>\n" +
                "    </tr>\n" +
                "\n" +
                "    <tr class=\"trInfo\">\n" +
                "      <td id=\"tdQR\" colspan=\"4\">\n" +
                "        <!--if(trim(@$aDatos['aSucursal']['con_url_encuesta']) != \"\")\n" +
                "          <a href=\" trim(@$aDatos['aSucursal']['con_url_encuesta']) \" target=\"_blank\">\n" +
                "            !! QrCode::size(200)->generate(trim($aDatos['aSucursal']['con_url_encuesta'])); !!\n" +
                "          </a>\n" +
                "          <br />\n" +
                "          <a href=\" trim(@$aDatos['aSucursal']['con_url_encuesta']) \" target=\"_blank\">Nos Interesa su opinión</a>\n" +
                "        endif-->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "\n" +
                "    <tr class=\"trInfo\">\n" +
                "      <td colspan=\"4\">\n" +
                "        <!--b>Tarjeta de Credito:</b> 0099-->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr class=\"trInfo\">\n" +
                "      <td colspan=\"4\">\n" +
                "        <!--b>Folio de facturación:</b><br />0001-001-09-101010-->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr class=\"trInfo trDivider\">\n" +
                "      <td colspan=\"4\">\n" +
                "        <!--Usted cuenta con 132 puntos del programa de lealtad-->\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <!--if (trim($aDatos['aSucursal']['con_texto_politica_devolucion_ticket_digital']) != \"\")\n" +
                "      <tr class=\"trInfo trDivider\">\n" +
                "        <td colspan=\"4\">\n" +
                "          <small><b>*<span id=\"text_con_texto_politica_devolucion_ticket_digital\"> $aDatos['aSucursal']['con_texto_politica_devolucion_ticket_digital'] </span></b></small>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    endif-->\n" +
                "\n" +
                "    <tr class=\"trInfo\">\n" +
                "      <td colspan=\"4\">\n" +
                "        <small><span id=\"text_con_texto_personalidado_ticket_digital\" class=\"h5\"><!-- $aDatos['aSucursal']['con_texto_personalidado_ticket_digital'] --></span></small>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "\n" +
                "  </tbody>\n" +
                "  <tfoot>\n" +
                "    <tr class=\"trInfo\">\n" +
                "      <td colspan=\"4\">\n" +
                "        Este ticket fue creado desde bio-Net Punto de Venta, el mejor sistema para tu negocio, para más información visita <a href=\"bionetpos.com\" target=\"_blank\">bionetpos.com</a>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tfoot>\n" +
                "</table>";
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pop_up_ticket_web);
        webView = (WebView) dialog.findViewById(R.id.simpleWebView);
        // displaying text in WebView
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        dialog.show();

    }


}
