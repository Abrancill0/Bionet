package com.Danthop.bionet;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.TrasladoAdapter;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pop_up_traslados extends DialogFragment {
    private String[][] inventarioModel;
    private String usu_id;
    private Integer id;
    private String suc_nombre;
    private ArrayList<String> SucursalID;
    private ArrayList<String> SucursalName;
    private ArrayList<String> ArticuloID;
    private ArrayList<String> VarianteID;
    private ArrayList<String> ModificadorID;
    private ArrayList<String> ExistenciasID;
    private Spinner SpinnerSucursal;
    private Spinner SpinnerSucursal2;
    private Spinner SpinnerTipoTraslado;
    private EditText observaciones;
    private EditText CantidadSolicitada;
    private String UUIDexist;
    private String UUIDart;
    private String UUIDvar;
    private String UUIDmod;
    private EditText Producto;
    private EditText Cantidad;
    private TextView SucOrigen;
    private TextView SucDestino;
    private ImageView atras;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView articulos;
    private TextView textViewObser;
    private TextView textfecha;
    private Spinner SpinnerFecha;
    ProgressDialog progreso;
    private String SucursalOrigen;
    private String SucursalDestino;
    private String TipoTraslado;
    private String UUIDexistencias;
    private String UUIDarticulo;
    private String UUIDvariante;
    private String UUIDmodificador;
    private LinearLayout LayoutFecha;
    private LinearLayout LayoutelegirArticulos;
    private String existencia = "";
    private String nombre_sucursal = "";
    private String producto = "";
    private String sku = "";
    private String categoria;
    private SortableInventariosTable tabla_inventario;
    private TableDataClickListener<InventarioModel> tablaElegirArticuloListener;
    private List<InventarioModel> inventarios;

    public Fragment_pop_up_traslados() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_traslado_completo, container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        tabla_inventario = (SortableInventariosTable) v.findViewById(R.id.tabla_articulos);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Artículo", "Categoría", "Sucursal", "Existencias");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 2);
        inventarios = new ArrayList<>();
        tabla_inventario.setHeaderAdapter(simpleHeader);
        tabla_inventario.setColumnModel(tableColumnWeightModel);

        atras = (ImageView)v.findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_traslado()).commit();
            }
        });
        SucursalID = new ArrayList<>();
        SucursalName = new ArrayList<>();
        ArticuloID = new ArrayList<>();
        VarianteID = new ArrayList<>();
        ModificadorID  = new ArrayList<>();
        ExistenciasID = new ArrayList<>();

        text1=(TextView)v.findViewById(R.id.text1);
        text2=(TextView)v.findViewById(R.id.text2);
        SucOrigen=(TextView)v.findViewById(R.id.SucOrigen);
        SpinnerSucursal=(Spinner)v.findViewById(R.id.Sucursal_Origen);
        SucDestino=(TextView)v.findViewById(R.id.SucDestino);
        SpinnerSucursal2=(Spinner)v.findViewById(R.id.Sucursal_Destino);
        text3=(TextView)v.findViewById(R.id.text3);
        observaciones=(EditText)v.findViewById(R.id.editObservaciones);
        CantidadSolicitada=(EditText)v.findViewById(R.id.CantidadSolicitada);
        textfecha=(TextView)v.findViewById(R.id.textfecha);
        SpinnerFecha=(Spinner)v.findViewById(R.id.SpinnerFecha);
        LayoutFecha = v.findViewById(R.id.Layoutfecha);
        LayoutFecha.setVisibility(View.GONE);
        LayoutelegirArticulos = v.findViewById(R.id.LayoutelegirArticulos);

        SpinnerTipoTraslado = (Spinner) v.findViewById(R.id.tipos_traslados);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.traslados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        SpinnerTipoTraslado.setAdapter(adapter);

        SpinnerTipoTraslado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VerificarTipoTraslado();
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       Button Solicitar = (Button) v.findViewById(R.id.btnaceptar);
        Solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SolicitarTraslado();
            }
        });

        MuestraArticulos();
        VerificarTipoTraslado();
        SpinnerSucursales();
        LoadListenerTable();

        tabla_inventario.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        tabla_inventario.addDataClickListener(tablaElegirArticuloListener);


        return v;
    }
//--------------------------------------------------------------------------------------------------
private void MuestraArticulos(){
    JSONObject request = new JSONObject();
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
            JSONObject RespuestaUUID = null;
            JSONObject RespuestaExistencias= null;
            JSONObject RespuestaIDExis = null;
            JSONObject RespuestaVariantes = null;
            JSONObject RespuestaModificadores = null;

            try {
                int status = Integer.parseInt(response.getString("estatus"));
                String Mensaje = response.getString("mensaje");

                if (status == 1) {
                    Resultado = response.getJSONObject("resultado");

                    Articulo = Resultado.getJSONArray("aArticulosExistencias");
                    inventarioModel = new String[Articulo.length()][4];

                    for (int x = 0; x < Articulo.length(); x++) {
                        JSONObject elemento = Articulo.getJSONObject(x);

                        RespuestaUUID = elemento.getJSONObject("art_id");
                        UUIDarticulo = RespuestaUUID.getString("uuid");

                        RespuestaVariantes = elemento.getJSONObject("ava_id");
                        UUIDvariante = RespuestaVariantes.getString("uuid");

                        RespuestaIDExis = elemento.getJSONObject("exi_id");
                        UUIDexistencias = RespuestaIDExis.getString("uuid");

                        producto = elemento.getString("art_nombre");
                        categoria = elemento.getString("cat_nombre");
                        nombre_sucursal = elemento.getString("suc_nombre");

                        RespuestaExistencias = elemento.getJSONObject("exi_cantidad");
                        existencia = RespuestaExistencias.getString("value");

                        //Variantes_Modificadores_SKU
                        Boolean Disponible_Variante = Boolean.valueOf(elemento.getString("art_tiene_variantes"));
                        if (Disponible_Variante == true) {
                            String NombreVariante = elemento.getString("ava_nombre");


                            Boolean Disponible_Modificador = Boolean.valueOf(elemento.getString("ava_tiene_modificadores"));
                            RespuestaModificadores = elemento.getJSONObject("amo_id");
                            UUIDmodificador = RespuestaModificadores.getString("uuid");
                            String NombreCompleto;
                            if (Disponible_Modificador == true) {
                                sku = elemento.getString("amo_sku");
                                String NombreModificador = elemento.getString("mod_nombre");
                                NombreCompleto = producto + " " + NombreVariante + " " + NombreModificador;
                                producto = NombreCompleto;
                            } else {
                                NombreCompleto = producto + " " + NombreVariante;
                                producto = NombreCompleto;
                            }
                        }else {
                            String NombreVariante = elemento.getString("ava_nombre");

                            Boolean Disponible_Modificador = Boolean.valueOf(elemento.getString("ava_tiene_modificadores"));
                            String NombreCompleto;
                            if (Disponible_Modificador == true) {
                                sku = elemento.getString("amo_sku");
                                String NombreModificador = elemento.getString("mod_nombre");
                                NombreCompleto = producto + " " + NombreVariante + " " + NombreModificador;
                                producto = NombreCompleto;

                            } else {
                                NombreCompleto = producto + " " + NombreVariante;
                                producto = NombreCompleto;
                            }
                        }

                        final InventarioModel inventario = new InventarioModel(
                                sku, producto, existencia, categoria, "", nombre_sucursal,
                                "","","","","",
                                "","", "","","",
                                "","","", "","","",
                                "","", "","","",
                                "", "",UUIDarticulo,UUIDvariante,UUIDmodificador,UUIDexistencias);
                        inventarios.add(inventario);
                    }final TrasladoAdapter TrasladoAdapter = new TrasladoAdapter(getContext(), inventarios,tabla_inventario);
                    tabla_inventario.setDataAdapter(TrasladoAdapter);
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
    VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );
}

//--------------------------------------------------------------------------------------------------
  private void VerificarTipoTraslado (){
        String opcion;
        opcion = SpinnerTipoTraslado.getSelectedItem().toString();
        if(opcion.equals("Traslado temporal")) {
            LayoutFecha.setVisibility(View.VISIBLE);
            TipoTraslado = "true";
        }else {
            LayoutFecha.setVisibility(View.GONE);
            TipoTraslado = "false";
        }
    }
//--------------------------------------------------------------------------------------------------
private void LoadListenerTable() {
    tablaElegirArticuloListener = new TableDataClickListener<InventarioModel>() {
        @Override
        public void onDataClicked(int rowIndex, final InventarioModel clickedData) {

            UUIDart = clickedData.getUUIDarticulo();
            UUIDvar = clickedData.getUUIDvariante();
            UUIDmod = clickedData.getUUIDmodificador();
            UUIDexist = clickedData.getUUIDexistencias();
        }
    };
}
//--------------------------------------------------------------------------------------------------
   private void  SolicitarTraslado(){
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Solicitando...");
        progreso.show();
       SucursalOrigen = SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
       SucursalDestino = SucursalID.get(SpinnerSucursal2.getSelectedItemPosition());

       String url = getString(R.string.Url);
       String ApiPath = url + "/api/inventario/store_solicitud_traslado";
        JSONObject request = new JSONObject();
        try {
            request.put( "tat_id_existencias_origen", UUIDexist);
            request.put( "tat_id_articulo", UUIDart);
            request.put( "tat_id_variante",UUIDvar);
            request.put( "tat_id_modificador","");
            request.put( "tat_cantidad", CantidadSolicitada.getText());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
       JSONArray Arrayarticulos = new JSONArray();
       Arrayarticulos.put(request);

       JSONObject jsonBodyrequest = new JSONObject();
       try {
           jsonBodyrequest.put("usu_id", usu_id);
           jsonBodyrequest.put("esApp", "1");
           jsonBodyrequest.put("tra_id_sucursal_origen",SucursalOrigen);
           jsonBodyrequest.put("tra_id_sucursal_destino",SucursalDestino);
           jsonBodyrequest.put("tra_traslado_temporal",TipoTraslado);
           jsonBodyrequest.put("tra_fecha_temporal","");
           jsonBodyrequest.put("tra_motivo",observaciones.getText());
           jsonBodyrequest.put("articulos",Arrayarticulos);

       }catch (JSONException e){
           e.printStackTrace();
       }

       JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,jsonBodyrequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getContext(), "Solicitud enviada", Toast.LENGTH_LONG).show();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_traslado()).commit();
                progreso.hide();
           }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progreso.hide();
                        Toast toast1 = Toast.makeText(getContext(), "Error de conexion", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
       );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }

//--------------------------------------------------------------------------------------------------
   private void SpinnerSucursales() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/configuracion/sucursales/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject Respuesta = null;
                JSONArray RespuestaSucursales = null;
                JSONObject RespuestaUUID = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Respuesta = response.getJSONObject("resultado");
                        RespuestaSucursales = Respuesta.getJSONArray("aSucursales");

                        for(int x = 0; x < RespuestaSucursales.length(); x++){
                            JSONObject elemento = RespuestaSucursales.getJSONObject(x);

                            RespuestaUUID = elemento.getJSONObject("suc_id");
                            String UUID = RespuestaUUID.getString("uuid");
                            SucursalID.add(UUID);

                            suc_nombre = elemento.getString("suc_nombre");
                            SucursalName.add(suc_nombre);

                        }SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));
                        SpinnerSucursal2.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));
                    }
                    else {
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
                new Response.ErrorListener() {
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
//--------------------------------------------------------------------------------------------------
}