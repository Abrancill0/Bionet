package com.Danthop.bionet;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.media.MediaPlayer;

import com.Danthop.bionet.Adapters.ClienteFrecuenteAdapter;
import com.Danthop.bionet.Adapters.HomeExistenciasAdapter;
import com.Danthop.bionet.Adapters.TopNotificacionAdapter;
import com.Danthop.bionet.Adapters.TopvendidosAdapter;
import com.Danthop.bionet.Tables.SortableClienteFrecuenteTable;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.ClienteFrecuenteModel;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.nostra13.universalimageloader.utils.L;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pantalla_principal extends Fragment{
    private String usu_id;
    private String suc_id;
    private String valueIdSuc;
    private long backPressedTime;
    private Toast backToast;
    private SortableInventariosTable tabla_inventario;
    private SortableClienteFrecuenteTable tabla_clientes;
    private SortableClienteFrecuenteTable tabla_notificacion;
    private SortableClienteFrecuenteTable tabla_productos;
    private String[][] inventarioModel;
    private List<InventarioModel> inventarios;
    private List<ClienteFrecuenteModel>Clientes;
    private List<ClienteFrecuenteModel>Productos;
    private List<ClienteFrecuenteModel> Notificaciones;
    private List<ClienteFrecuenteModel>Clientes2;
    private List<ClienteFrecuenteModel>TopClienteMax;
    private List<ClienteFrecuenteModel>Top;
    private List<ClienteFrecuenteModel>Topmax;
    private String existencia;
    private String nombre_cliente;
    private String nombre_sucursal;
    private Date fechaSolicitud;
    private String producto;
    private String NumTicket;
    private String posicion;
    private String UUIDmodificador;
    private String tar_nombre_articulo;
    private int tic_importe_total;
    MediaPlayer mp = new MediaPlayer();
    private ProgressDialog progressDialog;
    private String code="";

    public Fragment_pantalla_principal() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pantalla_principal,container, false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getContext().MODE_PRIVATE );
        String ImagenPerfil = sharedPref.getString( "usu_imagen_perfil", "" );
        String Apellido = sharedPref.getString("usu_apellidos", "");
        String Nombre = sharedPref.getString("usu_nombre", "");
        usu_id = sharedPref.getString("usu_id","");
        suc_id = sharedPref.getString("cca_id_sucursal","");
        code = sharedPref.getString("sso_code","");



        try {
            JSONArray jsonArray = new JSONArray(suc_id);
            //for (int i = 0; i < jsonArray.length(); i++){

                JSONObject JsonObj = jsonArray.getJSONObject(0);
                Iterator<String> iter = JsonObj.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    valueIdSuc = String.valueOf(JsonObj.get(key));
                }
          //  }
        } catch (JSONException e) {
            Toast toast1 =
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
            toast1.show();
        }

        //=====Programación de las tablas=====
        //String[][] DATA_TO_SHOW = { { "Producto 1", "Sucursal 1"},
               // {""}};

        String[][] DATA_TO_SHOW2 = { { "Notificacion 1"},
                {""}};

        tabla_productos = (SortableClienteFrecuenteTable) v.findViewById(R.id.tablaProductos_sucursales);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Producto", "Ventas");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize( 14 );
        simpleHeader.setPaddings(2,2,2,2);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(2);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);


        tabla_productos.setHeaderAdapter(simpleHeader);
        tabla_productos.setColumnModel(tableColumnWeightModel);
        Productos = new ArrayList<>();
        Top = new ArrayList<>();
        Topmax = new ArrayList<>();

//--------------------------------------------------------------------------------------------------

        tabla_notificacion = (SortableClienteFrecuenteTable) v.findViewById(R.id.tablaNotificaciones);
        final SimpleTableHeaderAdapter simpleHeader2 = new SimpleTableHeaderAdapter(getContext(), "Notificación", "Fecha");
        simpleHeader2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader2.setTextSize( 14 );
        simpleHeader2.setPaddings(2,2,2,2);

        final TableColumnWeightModel tableColumnWeightModel2 = new TableColumnWeightModel(2);
        tableColumnWeightModel2.setColumnWeight(0, 2);
        tableColumnWeightModel2.setColumnWeight(1, 2);

        tabla_notificacion.setHeaderAdapter(simpleHeader2);
        tabla_notificacion.setColumnModel(tableColumnWeightModel2);
        Notificaciones = new ArrayList<ClienteFrecuenteModel>();

//--------------------------------------------------------------------------------------------------

        tabla_inventario = (SortableInventariosTable) v.findViewById(R.id.tablaPocas_Existencias);
        final SimpleTableHeaderAdapter simpleHeader3 = new SimpleTableHeaderAdapter(getContext(), "Producto", "Sucursal","Existencia");
        simpleHeader3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader3.setTextSize( 14 );
        simpleHeader3.setPaddings(2,2,2,2);

        final TableColumnWeightModel tableColumnWeightModel3 = new TableColumnWeightModel(3);
        tableColumnWeightModel3.setColumnWeight(0, 2);
        tableColumnWeightModel3.setColumnWeight(1, 2);
        tableColumnWeightModel3.setColumnWeight(2, 2);

        tabla_inventario.setHeaderAdapter(simpleHeader3);
        tabla_inventario.setColumnModel(tableColumnWeightModel3);
        inventarios = new ArrayList<>();

//--------------------------------------------------------------------------------------------------

        tabla_clientes = (SortableClienteFrecuenteTable) v.findViewById(R.id.tablaClientesFrecuentes);
        final SimpleTableHeaderAdapter simpleHeader4 = new SimpleTableHeaderAdapter(getContext(), "Cliente", "Compras realizadas");
        simpleHeader4.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader4.setTextSize( 14 );
        simpleHeader4.setPaddings(2,2,2,2);

        final TableColumnWeightModel tableColumnWeightModel4 = new TableColumnWeightModel(2);
        tableColumnWeightModel4.setColumnWeight(0, 2);
        tableColumnWeightModel4.setColumnWeight(1, 2);


        tabla_clientes.setHeaderAdapter(simpleHeader4);
        tabla_clientes.setColumnModel(tableColumnWeightModel4);
        Clientes = new ArrayList<>();
        Clientes2 = new ArrayList<>();
        TopClienteMax = new ArrayList<>();

//--------------------------------------------------------------------------------------------------

        //=====Programación del carrousel=====
        final int[] sampleImages = {R.drawable.store2, R.drawable.store, R.drawable.museum_store, R.drawable.store3};
        CarouselView carouselView;
        carouselView = (CarouselView) v.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };

        carouselView.setImageListener(imageListener);

        LoadMasVendidos();
        LoadPocasExistencias();
        LoadClientesFrecuentes();
        LoadNotificaciones();
        return v;
    }

//--------------------------------------------------------------------------------------------------
private void LoadPocasExistencias(){
    try {
    } catch (Exception e) {
        e.printStackTrace();
    }
    String url = getString(R.string.Url);
    String ApiPath = url + "/api/inventario/index?usu_id=" + usu_id + "&esApp=1";
    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            JSONObject Resultado = null;
            JSONArray Articulo = null;
            JSONObject RespuestaExistencias= null;
            JSONObject RespuestaModificadores = null;

            try {
                int status = Integer.parseInt(response.getString("estatus"));
                String Mensaje = response.getString("mensaje");

                if (status == 1) {
                    Resultado = response.getJSONObject("resultado");
                    Articulo = Resultado.getJSONArray("aArticuloExistencias");

                    for (int x = 0; x < Articulo.length(); x++) {
                        JSONObject elemento = Articulo.getJSONObject(x);

                        RespuestaExistencias = elemento.getJSONObject("exi_cantidad");
                        int exis = RespuestaExistencias.getInt("value");
                        if (exis <= 10) {
                            existencia= RespuestaExistencias.getString("value");
                            producto = elemento.getString("art_nombre");
                            nombre_sucursal = elemento.getString("suc_nombre");

                            Boolean Disponible_Variante = Boolean.valueOf(elemento.getString("art_tiene_variantes"));
                            if (Disponible_Variante == true) {
                                String NombreVariante = elemento.getString("ava_nombre");
                                Boolean Disponible_Modificador = Boolean.valueOf(elemento.getString("ava_tiene_modificadores"));
                                RespuestaModificadores = elemento.getJSONObject("amo_id");
                                UUIDmodificador = RespuestaModificadores.getString("uuid");
                                String NombreCompleto;
                                if (Disponible_Modificador == true) {
                                    String NombreModificador = elemento.getString("mod_nombre");
                                    NombreCompleto = producto + " " + NombreVariante + " " + NombreModificador;
                                    producto = NombreCompleto;
                                } else {
                                    NombreCompleto = producto + " " + NombreVariante;
                                    producto = NombreCompleto;
                                }
                            } else {
                                String NombreVariante = elemento.getString("ava_nombre");
                                Boolean Disponible_Modificador = Boolean.valueOf(elemento.getString("ava_tiene_modificadores"));
                                String NombreCompleto;
                                if (Disponible_Modificador == true) {
                                    String NombreModificador = elemento.getString("mod_nombre");
                                    NombreCompleto = producto + " " + NombreVariante + " " + NombreModificador;
                                    producto = NombreCompleto;
                                } else {
                                    NombreCompleto = producto + " " + NombreVariante;
                                    producto = NombreCompleto;
                                }
                            }
                            final InventarioModel inventario = new InventarioModel(
                                    "", producto, existencia, "", "", nombre_sucursal,
                                    "","","","","",
                                    "","", "","","",
                                    "","","", "","","",
                                    "","", "","","",
                                    "", "","","","","","",
                                    "","","");
                            inventarios.add(inventario);
                        }
                    }
                }      final HomeExistenciasAdapter ExistenciasAdapter = new HomeExistenciasAdapter(getContext(), inventarios,tabla_inventario);
                tabla_inventario.setDataAdapter(ExistenciasAdapter);

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
private void LoadClientesFrecuentes(){
    try {
    } catch (Exception e) {
        e.printStackTrace();
    }
    String url = getString(R.string.Url);
    String ApiPath = url + "/api/dashboard/clientesfr?usu_id=" + usu_id + "&suc_id=" + valueIdSuc + "&esApp=1&code="+code;
    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, null, new Response.Listener<JSONObject>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onResponse(JSONObject response) {
            JSONArray Resultado = null;
            JSONObject Respuesta_tic_id_cliente = null;

            try {
                int status = Integer.parseInt(response.getString("estatus"));
                String Mensaje = response.getString("mensaje");

                if (status == 1) {
                    Resultado = response.getJSONArray("resultado");

                    for (int x = 0; x < Resultado.length(); x++) {
                        JSONObject elemento = Resultado.getJSONObject(x);
                        String cliente = elemento.getString("tic_id_cliente");

                            String nombre_cliente = elemento.getString("tic_id_nombre");
                            String compras = elemento.getString("compras");

                           final ClienteFrecuenteModel Nombrecliente = new ClienteFrecuenteModel(nombre_cliente,compras,"","", "","","");
                           Clientes.add(Nombrecliente);

                    }

                    final ClienteFrecuenteAdapter FrecuenteAdapter = new ClienteFrecuenteAdapter(getContext(), Clientes,tabla_clientes);
                    tabla_clientes.setDataAdapter(FrecuenteAdapter);
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
    postRequest.setShouldCache(false);
    VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( postRequest );
}
//--------------------------------------------------------------------------------------------------
private void LoadMasVendidos(){

    try {
    } catch (Exception e) {
        e.printStackTrace();
    }
    String url = getString(R.string.Url);
    String ApiPath = url + "/api/dashboard/topProd/?usu_id=" + usu_id + "&suc_id=" + valueIdSuc + "&esApp=1&code="+code;
    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, null, new Response.Listener<JSONObject>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onResponse(JSONObject response) {
            JSONArray ResultadoProductos = null;

            try {
                int status = Integer.parseInt(response.getString("estatus"));
                String Mensaje = response.getString("mensaje");

                if (status == 1) {
                    progressDialog.dismiss();
                    ResultadoProductos = response.getJSONArray("resultado");

                    for (int x = 0; x < ResultadoProductos.length(); x++) {
                        JSONObject elemento = ResultadoProductos.getJSONObject(x);

                        tar_nombre_articulo = elemento.getString( "tar_nombre_articulo" );
                        NumTicket = elemento.getString("cantidad");

                            final ClienteFrecuenteModel Topvendidos = new ClienteFrecuenteModel("",NumTicket,"", tar_nombre_articulo, "","","");
                            Productos.add(Topvendidos);

                    }

                    final TopvendidosAdapter TopVendidoseAdapter = new TopvendidosAdapter(getContext(), Productos ,tabla_productos);
                    tabla_productos.setDataAdapter(TopVendidoseAdapter);
                }
                else
                {
                    progressDialog.dismiss();
                }

            } catch (JSONException e) {
                progressDialog.dismiss();
                Toast toast1 =
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                toast1.show();
            }
        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast toast1 =
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
    );
    postRequest.setShouldCache(false);
    VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( postRequest );
}

//--------------------------------------------------------------------------------------------------
public void LoadNotificaciones() {
    try {
        String ApiPath = getString(R.string.Url)+"/api/notificaciones/index?usu_id=" + usu_id + "&esApp=1&code="+code;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int EstatusApi = Integer.parseInt(response.getString("estatus"));

                            if (EstatusApi == 1) {

                                JSONObject resultado = response.getJSONObject("resultado");
                                JSONArray NodoNotificaciones = resultado.getJSONArray("aNotificaciones");
                                for(int x=0;x<NodoNotificaciones.length();x++)
                                {
                                    JSONObject elemento = NodoNotificaciones.getJSONObject(x);
                                    JSONObject NodoID = elemento.getJSONObject("nen_id");
                                    String ID = NodoID.getString("uuid");
                                    String Titulo = elemento.getString("nen_titulo");
                                    String Fecha = elemento.getString("nen_fecha_hora_creo");



                                    final ClienteFrecuenteModel Notificacion = new ClienteFrecuenteModel(
                                            "",
                                             "",
                                            "",
                                             "",
                                            "",Titulo,Fecha);

                                    Notificaciones.add(Notificacion);
                                }

                                final TopNotificacionAdapter NotificacionAdapter = new TopNotificacionAdapter(getContext(), Notificaciones ,tabla_notificacion);
                                tabla_notificacion.setDataAdapter(NotificacionAdapter);

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


