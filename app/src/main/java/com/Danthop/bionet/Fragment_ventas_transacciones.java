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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ApartadoAdapter;
import com.Danthop.bionet.Adapters.DetalleApartadoAdapter;
import com.Danthop.bionet.Adapters.DetalleOrdenEspecialAdapter;
import com.Danthop.bionet.Adapters.OrdenEspecialAdapter;
import com.Danthop.bionet.Tables.SortableApartadoDetalleTable;
import com.Danthop.bionet.Tables.SortableApartadoTable;
import com.Danthop.bionet.Tables.SortableOrdenEspecialDetalleTable;
import com.Danthop.bionet.Tables.SortableOrdenEspecialTable;
import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.Impuestos;
import com.Danthop.bionet.model.OrdenEspecialArticuloModel;
import com.Danthop.bionet.model.OrdenEspecialModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private View layout_invisible;

    private View btn_movimientos;
    private View btn_apartado;
    private View btn_ordenes_especiales;

    private Button btn_corte_caja;
    private Button btn_ventas;

    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;

    private List<ApartadoModel> Lista_de_apartados= new ArrayList<>();

    private List<OrdenEspecialModel> Lista_de_ordenes= new ArrayList<>();

    private SortableApartadoTable TablaApartados;
    private SortableOrdenEspecialTable TablaOrdenes;

    private String usu_id;

    private FragmentTransaction fr;


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
        layout_apartado = v.findViewById(R.id.layout_apartado);
        layout_ordenes = v.findViewById(R.id.layout_ordenes);
        layout_invisible = v.findViewById(R.id.layout_invisible);
        btn_movimientos = v.findViewById(R.id.btn_movimientos);
        btn_apartado = v.findViewById(R.id.btn_apartado);
        btn_ordenes_especiales = v.findViewById(R.id.btn_ordenes_especiales);

        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();
        SpinnerSucursal=(Spinner)v.findViewById(R.id.sucursal);


        TablaApartados = v.findViewById(R.id.tabla_apartados);
        TablaApartados.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        TablaOrdenes = v.findViewById(R.id.tabla_ordenes);
        TablaOrdenes.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia2));

        btn_ventas = v.findViewById(R.id.btn_ventas);
        btn_corte_caja = v.findViewById(R.id.btn_corte_caja);

        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                LoadApartados();
                LoadOrdenes();
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

}
