package com.Danthop.bionet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.TrasladoEnvioRecibidoAdapter;
import com.Danthop.bionet.Tables.SortableTrasladosTable;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pestania_traslado_recibidos extends Fragment {
    private SortableTrasladosTable tabla_traslados;
    private List<InventarioModel> traslados;
    private TableDataClickListener<InventarioModel> tableListener;
    private String[][] trasladoModel;
    private Dialog ver_dialog_traslado;
    private Long timestamp;
    private String FechaconFormato;
    private String usu_id;
    private String suc_id = "";
    private String modificadores = "";
    private String existencia = "";
    private String nombre_sucursal = "";
    private String producto = "";
    private String art_tipo = "";
    private String sku = "";
    private String codigoBarras = "";
    private String almacen = "";
    private String amo_activo="";
    private String articulo_descripcion;
    private String categoria;
    private String art_disponible_venta;
    private String art_disponible_compra;
    private String ava_aplica_apartados;
    private String ava_aplica_cambio_devolucion;
    private String aim_url;
    private String art_nombre;
    private String cat_nombre;
    private String his_tipo;
    private String his_cantidad;
    private String his_observaciones;
    private String his_fecha_hora_creo;
    private String RecibidasOrigen = "";
    private String RecibidasDestino = "";
    private String tra_nombre_estatus = "";
    private String suc_numero_sucursal_destino = "";
    private String suc_numero_sucursal_origen = "";
    private Long fechaSolicitud;
    private String tra_motivo = "";
    private ProgressDialog progressDialog;
    private SearchView BuscarTraslado;
    private String UUID;
    private TextView UUIDarticulostraslados;
    private TextView StatusSolicitud;
    private String estadosolicitud;
    private String AceptarSolicitud;
    private String CancelarSolicitud;
    private String status_solicitud;
    private String status_nombre;
    private String tipo_traslado = "recibida";

    private String code="";
    private String usu_cuenta_bionet="";

    private TrasladoEnvioRecibidoAdapter TrasladoAdapter;

    private Bundle bundle;
    private boolean Historicos=false;
    private boolean Inventarios =false;
    private boolean Listado_inventarios  =false;
    private boolean Traslado =false;

    private ArrayList<String> ArticulosTrasladados = new ArrayList<>();
    private String cadena_articulos;

    public Fragment_pestania_traslado_recibidos() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pestania_traslados,container, false);

        try{
            bundle = getArguments();
            Historicos=bundle.getBoolean("Historicos");
            Inventarios=bundle.getBoolean("Inventarios");
            Listado_inventarios=bundle.getBoolean("Listado_inventarios");
            Traslado=bundle.getBoolean("Traslado");
        }catch(NullPointerException s)
        {

        }

        ver_dialog_traslado = new Dialog(getContext());
        ver_dialog_traslado.setContentView(R.layout.pop_up_solicitud_traslado_recibidas);


        Button btn_traslados = (Button) v.findViewById(R.id.btn_inventarios);
        btn_traslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean("Historicos", Historicos);
                bundle.putBoolean("Inventarios", Inventarios);
                bundle.putBoolean("Listado_inventarios", Listado_inventarios);
                bundle.putBoolean("Traslado", Traslado);
                Fragment_inventarios fragment2 = new Fragment_inventarios();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();
            }
        });

        Button btnHistorico = (Button) v.findViewById(R.id.btn_hist);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean("Historicos", Historicos);
                bundle.putBoolean("Inventarios", Inventarios);
                bundle.putBoolean("Listado_inventarios", Listado_inventarios);
                bundle.putBoolean("Traslado", Traslado);
                Fragment_pestania_historico fragment2 = new Fragment_pestania_historico();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();
            }
        });

        Button btnInventariExistencias = (Button) v.findViewById(R.id.btnBuscarExistencias);
        btnInventariExistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean("Historicos", Historicos);
                bundle.putBoolean("Inventarios", Inventarios);
                bundle.putBoolean("Listado_inventarios", Listado_inventarios);
                bundle.putBoolean("Traslado", Traslado);
                Fragment_pestania_inventario_existencias fragment2 = new Fragment_pestania_inventario_existencias();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();
            }
        });

       Button trasladar = (Button) v.findViewById(R.id.pop_up_traslados);
        trasladar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Fragment_pop_up_traslados()).commit();
                onDetach();
            }
        });

//--------------------------------------------------------------------------------------------------------------
        Button SolicitudesEnviadas = v.findViewById(R.id.traslados_enviados);
        SolicitudesEnviadas.setBackgroundResource(R.drawable.pestanas_desplegables);
        SolicitudesEnviadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean("Historicos", Historicos);
                bundle.putBoolean("Inventarios", Inventarios);
                bundle.putBoolean("Listado_inventarios", Listado_inventarios);
                bundle.putBoolean("Traslado", Traslado);
                Fragment_pestania_traslado_enviados fragment2 = new Fragment_pestania_traslado_enviados();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();
            }
        });

        Button SolicitudesRecibidas = (Button) v.findViewById(R.id.traslados_recibidos);
        SolicitudesRecibidas.setBackgroundColor(getResources().getColor(R.color.fondo_azul));

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        code = sharedPref.getString("sso_code","");
        usu_cuenta_bionet = sharedPref.getString("usu_cuenta_bionet","");


        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        traslados = new ArrayList<>();

        tabla_traslados = (SortableTrasladosTable) v.findViewById(R.id.tabla_traslados);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Origen", "Destino", "Estatus", "Motivo", "Fecha de solicitud");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize( 18 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 1);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);

        tabla_traslados.setHeaderAdapter(simpleHeader);
        tabla_traslados.setColumnModel(tableColumnWeightModel);

        Traslados_Recibidas();
        BuscarTraslado=v.findViewById(R.id.buscar_traslados);
        BuscarTraslado.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TrasladoAdapter.getFilter().filter(newText);
                return false;
            }
        });


        LoadListenerTable();
        tabla_traslados.setSwipeToRefreshEnabled(true);
        tabla_traslados.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_traslados.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        traslados.clear();
                        refreshIndicator.hide();
                    }
                }, 2000);
            }
        });

        tabla_traslados.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        tabla_traslados.addDataClickListener(tableListener);

        if(Traslado==false)
        {
            trasladar.setEnabled(false);
        }

        return v;
    }

//--------------------------------------------------------------------------------------------------
    public  void Traslados_Recibidas(){
        JSONObject request = new JSONObject();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = getString(R.string.Url);
        String ApiPath = url + "/api/inventario/index?usu_id=" + usu_id + "&esApp=1&code="+code;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Resultado = null;
                JSONObject RespuestaTraslados = null;
                JSONArray RespuestaSolicitudesRecibidas = null;
                JsonArray RespuestaSolicitudEnviadas = null;
                JSONObject RespuestaUUID = null;
                JSONObject RespuestaFecha = null;
                JSONObject TypoFecha = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        progressDialog.dismiss();
                        Resultado = response.getJSONObject("resultado");
                        RespuestaTraslados = Resultado.getJSONObject("aTraslados");
                        RespuestaSolicitudesRecibidas = RespuestaTraslados.getJSONArray("aSolicitudesRecibidas");

                        trasladoModel = new String[RespuestaSolicitudesRecibidas.length()][4];
                        for (int x = 0; x < RespuestaSolicitudesRecibidas.length(); x++) {
                            JSONObject elemento = RespuestaSolicitudesRecibidas.getJSONObject(x);

                            RespuestaUUID = elemento.getJSONObject("tra_id");
                            UUID = RespuestaUUID.getString("uuid");

                            RespuestaFecha = elemento.getJSONObject("tra_fecha_hora_creo");
                            timestamp = RespuestaFecha.getLong("seconds");

                            Timestamp stamp = new Timestamp(timestamp);
                            Date date_f = new Date(stamp.getTime() * 1000L);

                            String Formato = "dd/MM/yyyy";
                            SimpleDateFormat formatter = new SimpleDateFormat(Formato);

                            FechaconFormato = formatter.format(date_f.getTime());

                            RecibidasOrigen = elemento.getString("suc_nombre_sucursal_origen");
                            suc_numero_sucursal_origen = elemento.getString("suc_numero_sucursal_origen");
                            RecibidasDestino = elemento.getString("suc_nombre_sucursal_destino");
                            suc_numero_sucursal_destino = elemento.getString("suc_numero_sucursal_destino");
                            tra_nombre_estatus = elemento.getString("tra_nombre_estatus");


                            String motivo = elemento.getString("tra_motivo");
                            if (motivo.equals("null")){
                                tra_motivo = "Sin observaciones";
                            }else {
                                tra_motivo = elemento.getString("tra_motivo");
                            }

                            String SucursalCodigoOrigen;
                            String SucursalCodigoDestino;

                            SucursalCodigoOrigen = RecibidasOrigen + "(" + suc_numero_sucursal_origen + ")";
                            RecibidasOrigen = SucursalCodigoOrigen;
                            SucursalCodigoDestino = RecibidasDestino + "(" + suc_numero_sucursal_destino + ")";
                            RecibidasDestino = SucursalCodigoDestino;

                            status_solicitud = elemento.getString("tra_id_estatus_type");
                            status_nombre = elemento.getString( "tra_nombre_estatus");



                            final InventarioModel traslado = new InventarioModel(
                                    sku,
                                    producto,
                                    existencia,
                                    categoria,
                                    modificadores,
                                    nombre_sucursal,
                                    suc_id,
                                    articulo_descripcion,
                                    art_tipo,
                                    art_disponible_venta,
                                    art_disponible_compra,
                                    ava_aplica_apartados,
                                    ava_aplica_cambio_devolucion,
                                    aim_url,
                                    art_nombre,
                                    cat_nombre,
                                    his_tipo,
                                    his_cantidad,
                                    his_observaciones,
                                    his_fecha_hora_creo,
                                    codigoBarras,
                                    almacen,
                                    RecibidasOrigen,
                                    RecibidasDestino,
                                    tra_nombre_estatus,
                                    suc_numero_sucursal_destino,
                                    suc_numero_sucursal_origen,
                                    FechaconFormato,
                                    tra_motivo,UUID,"","","","","",
                                    status_solicitud,status_nombre);
                            traslados.add(traslado);

                        }
                        TrasladoAdapter = new TrasladoEnvioRecibidoAdapter(getContext(), traslados,tabla_traslados);
                        tabla_traslados.setDataAdapter(TrasladoAdapter);
                    }
                } catch (JSONException e) {
                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        getRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );
    }
//--------------------------------------------------------------------------------------------------

    private void LoadListenerTable(){
        tableListener = new TableDataClickListener<InventarioModel>() {
            @Override
            public void onDataClicked(int rowIndex, final InventarioModel clickedData) {

                final Dialog ver_dialog_traslado;
                ver_dialog_traslado = new Dialog(getContext());
                ver_dialog_traslado.setContentView(R.layout.pop_up_solicitud_traslado_recibidas);
                ver_dialog_traslado.show();

                TextView motivotraslado = ver_dialog_traslado.findViewById( R.id.motivotraslado );
                Button aceptar_solicitud = ver_dialog_traslado.findViewById(R.id.aceptar_solicitud);
                Button rechazar_solicitud = ver_dialog_traslado.findViewById(R.id.rechazar_solicitud);
                TextView texto_explicacion = ver_dialog_traslado.findViewById(R.id.text_explicacion);

                View descargarPDF = ver_dialog_traslado.findViewById(R.id.btn_descargar_pdf);
                View descargarXML = ver_dialog_traslado.findViewById(R.id.btn_descargar_xml);

                motivotraslado.setText(clickedData.gettra_motivo());

                TextView articulos_trasladados = ver_dialog_traslado.findViewById(R.id.articulos_trasladados);
                detalle_traslado(clickedData.getTraID(),articulos_trasladados);

                Button cerrar_ventana = ver_dialog_traslado.findViewById(R.id.cerrar_ventana);
                cerrar_ventana.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ver_dialog_traslado.dismiss();
                    }
                });

                if (clickedData.getstatus_solicitud().equals("Aceptada")){
                    aceptar_solicitud.setVisibility(View.GONE);
                    rechazar_solicitud.setVisibility(View.GONE);
                    texto_explicacion.setVisibility(View.GONE);
                    descargarPDF.setVisibility(View.VISIBLE);
                    descargarXML.setVisibility(View.VISIBLE);
                }

                if (clickedData.getstatus_solicitud().equals("Rechazada")){
                    aceptar_solicitud.setVisibility(View.GONE);
                    rechazar_solicitud.setVisibility(View.GONE);
                    texto_explicacion.setVisibility(View.GONE);
                }

                if (clickedData.getstatus_solicitud().equals("Concluida")){
                    aceptar_solicitud.setVisibility(View.GONE);
                    rechazar_solicitud.setVisibility(View.GONE);
                    texto_explicacion.setVisibility(View.GONE);
                    descargarPDF.setVisibility(View.VISIBLE);
                    descargarXML.setVisibility(View.VISIBLE);
                }


//---------------------------------------btn Aceptar-----------------------------------------------

                aceptar_solicitud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        responder_solicitud("aceptar",clickedData.getTraID());
                    }
                });
//---------------------------------------btn Rechazar-------------------------------------------

                rechazar_solicitud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        responder_solicitud("rechazar",clickedData.getTraID());
                    }
                });
//---------------------------------------btn DescargarPDF-------------------------------------------

                descargarPDF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        descargarPDF(clickedData.getTra_factura(),usu_cuenta_bionet);
                    }
                });

//---------------------------------------btn DescargarXML-------------------------------------------

                descargarXML.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        descargarXML(clickedData.getTra_factura(),usu_cuenta_bionet);
                    }
                });


            }
        };
    }


    private void responder_solicitud(String tipo,String tra_id) {

        String tiporeturn = tipo;
        String url = getString(R.string.Url);
        String ApiPath = url + "/api/inventario/responder_solicitud";


        JSONObject jsonBodyrequest = new JSONObject();
        try {
            jsonBodyrequest.put("esApp", "1" );
            jsonBodyrequest.put("usu_id", usu_id);
            jsonBodyrequest.put("tra_id",tra_id);
            jsonBodyrequest.put("tipo_traslado",tipo_traslado);
            jsonBodyrequest.put("respuesta",tiporeturn); //cancelar,recibir,aceptar,rechazar
            jsonBodyrequest.put("code",code);

        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,jsonBodyrequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {
                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    Toast toast1 =
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 = Toast.makeText(getContext(),error.getMessage() , Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
        );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }

    private void detalle_traslado(String tra_id,TextView articulosText) {
        progressDialog.show();
        ArticulosTrasladados.clear();
        try {
            String url = getString(R.string.Url);

            String ApiPath = url + "/api/inventario/obtener_articulos_detalle_traslado?usu_id=" + usu_id + "&esApp=1&code="+code+"&tra_id=" + tra_id;

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject Respuesta = response.getJSONObject("resultado");
                                    JSONArray articulosArray = Respuesta.getJSONArray("aTrasladosArticulos");
                                    for(int i=0;i<articulosArray.length();i++)
                                    {
                                        JSONObject elemento = articulosArray.getJSONObject(i);
                                        String articulo_trasladado = elemento.getString("art_nombre_completo");
                                        ArticulosTrasladados.add(articulo_trasladado);
                                    }
                                    cadena_articulos="";
                                    for(int x=0; x<ArticulosTrasladados.size();x++)
                                    {
                                        cadena_articulos = ArticulosTrasladados.get(x)+"\n";
                                    }
                                    articulosText.setText(cadena_articulos);
                                    progressDialog.dismiss();

                                }
                            } catch (JSONException e) {
                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                String.valueOf(e), Toast.LENGTH_LONG);
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(error), Toast.LENGTH_LONG);
                            progressDialog.dismiss();
                        }
                    }
            );

            getRequest.setShouldCache(false);
            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    private void descargarPDF(String tra_factura, String cuenta_bionet){
        String url = getString(R.string.Url);
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(url+"/facturacion/obtener_mi_factura/"+usu_cuenta_bionet+"/traslados/"+tra_factura+"?esApp=1&usu_id="+usu_id));
        startActivity(viewIntent);
    }

    private void descargarXML(String tra_factura, String cuenta_bionet){
        String url = getString(R.string.Url);
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(url+"/facturacion/obtener_mi_xml/"+usu_cuenta_bionet+"/traslados/"+tra_factura+"?esApp=1&usu_id="+usu_id));
        startActivity(viewIntent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
