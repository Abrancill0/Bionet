package com.Danthop.bionet;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private Spinner SpinnerSucursal;
    private Spinner SpinnerSucursal2;
    private Spinner SpinnerTipoTraslado;
    private EditText observaciones;
    private ElegantNumberButton CantidadSolicitada;
    private String UUIDexist;
    private String UUIDart;
    private String UUIDvar;
    private String UUIDmod;
    private TextView SucOrigen;
    private TextView SucDestino;
    private ImageView atras;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView articulos;
    private TextView textViewObser;
    private EditText Fecha;
    private Spinner SpinnerFecha;
    ProgressDialog progreso;
    private String SucursalOrigen;
    private String SucursalDestino;
    private Dialog dialog;
    private String TipoTraslado;
    private String UUIDexistencias;
    private String UUIDarticulo;
    private String UUIDvariante;
    private String UUIDmodificador;
    private String fechaSolicitud;
    private Button btn_agregar_articulo;
    private LinearLayout LayoutFecha;
    private LinearLayout LayoutelegirArticulos;
    private String existencia = "";
    private String nombre_sucursal = "";
    private String producto = "";
    private String sku = "";
    private String categoria;
    private String cantidad;
    private SortableInventariosTable tabla_inventario;
    private TableDataClickListener<InventarioModel> tablaElegirArticuloListener;
    private List<InventarioModel> inventarios;
    private TrasladoAdapter TrasladoAdapter;
    private String FechaApi;
    private String FechaInicio;

    private String code="";

    private JSONObject RespuestaTodo = null;

    private List<InventarioModel> ArticulosTraslados;

    private DatePickerDialog.OnDateSetListener mDataSetlistener;

    private Bundle bundle;
    private boolean Historicos=false;
    private boolean Inventarios =false;
    private boolean Listado_inventarios  =false;
    private boolean Traslado =false;

    public Fragment_pop_up_traslados() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { View v = inflater.inflate(R.layout.fragment_traslado_completo, container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        try{
            bundle = getArguments();
            Historicos=bundle.getBoolean("Historicos");
            Inventarios=bundle.getBoolean("Inventarios");
            Listado_inventarios=bundle.getBoolean("Listado_inventarios");
            Traslado=bundle.getBoolean("Traslado");
        }catch(NullPointerException s)
        {

        }

        usu_id = sharedPref.getString("usu_id","");
        usu_id = sharedPref.getString("usu_id", "");
        code = sharedPref.getString("sso_code","");
        dialog=new Dialog(getContext());
        progreso = new ProgressDialog( getContext() );

        tabla_inventario = (SortableInventariosTable) v.findViewById(R.id.tabla_articulos);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "", "Artículo", "Categoría", "Sucursal", "Existencias", "Cantidad");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize( 16 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);

        inventarios = new ArrayList<>();
        ArticulosTraslados = new ArrayList<>();
        tabla_inventario.setHeaderAdapter(simpleHeader);
        tabla_inventario.setColumnModel(tableColumnWeightModel);

        atras = (ImageView)v.findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean("Historicos", Historicos);
                bundle.putBoolean("Inventarios", Inventarios);
                bundle.putBoolean("Listado_inventarios", Listado_inventarios);
                bundle.putBoolean("Traslado", Traslado);
                Fragment_pestania_traslado fragment2 = new Fragment_pestania_traslado();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();
            }
        });
        SucursalID = new ArrayList<>();
        SucursalName = new ArrayList<>();

        text2=(TextView)v.findViewById(R.id.text2);
        SucOrigen=(TextView)v.findViewById(R.id.SucOrigen);
        SpinnerSucursal=(Spinner)v.findViewById(R.id.Sucursal_Origen);

        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (RespuestaTodo != null)
                {
                    dialog.dismiss();
                    MuestraArticulossinApi();
                    ArticulosTraslados.clear();
                }


            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SucDestino=(TextView)v.findViewById(R.id.SucDestino);
        SpinnerSucursal2=(Spinner)v.findViewById(R.id.Sucursal_Destino);
        SpinnerSucursal2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (RespuestaTodo != null)
                {
                    dialog.dismiss();
                    MuestraArticulossinApi();
                    ArticulosTraslados.clear();
                }


            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        text3=(TextView)v.findViewById(R.id.text3);
        observaciones=(EditText)v.findViewById(R.id.editObservaciones);
        //CantidadSolicitada=(ElegantNumberButton)v.findViewById(R.id.art_cantidad);
        Fecha=(EditText) v.findViewById(R.id.Fecha);

        Time today = new Time( Time.getCurrentTimezone() );
        today.setToNow();
        int year = today.year;
        String dia;
        String mes;
        if(today.monthDay < 10){
            dia = "0" + today.monthDay;
        }else {
            dia = String.valueOf( today.monthDay );
        }
        if(today.month + 1 < 10){
            mes = "0" + (today.month + 1);
        }else {
            mes = String.valueOf(today.month + 1);

        }

        String fechausuario = (dia + "/" + mes + "/" + year );
        Fecha.setText( fechausuario );
        FechaApi = year + "/" + mes + "/" + dia;

        Fecha.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal =Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog( getContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDataSetlistener,Year,Month,Day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDataSetlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String dia="";
                String mes="";
                String ano="";

                if(dayOfMonth<10) {
                    dia = "0" + dayOfMonth;

                }
                else {
                       dia = String.valueOf( dayOfMonth );
                    }

                if(month+1<10) {
                    mes = "0" + (month+1);
                }
                else
                {
                    mes = String.valueOf( month+1 );
                }
                String Date = dia + "/" + mes + "/" + year;

                FechaApi = year + "-" + mes + "-" + dia;

                Fecha.setText( Date );
            }
        };

        //SpinnerFecha=(Spinner)v.findViewById(R.id.SpinnerFecha);
        LayoutFecha = v.findViewById(R.id.Layoutfecha);
       // LayoutFecha.setVisibility(View.GONE);
        //LayoutelegirArticulos = v.findViewById(R.id.LayoutelegirArticulos);
        btn_agregar_articulo = v.findViewById(R.id.btn_agregar_articulo);

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

                //validar sucursales
               String Suc_origen = SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
                String Suc_destino = SucursalID.get(SpinnerSucursal2.getSelectedItemPosition());

                if (Suc_origen != Suc_destino)
                {
                    if (ArticulosTraslados.size() > 0)
                    {
                        SolicitarTraslado();
                    }
                    else
                    {
                        Toast toast1 = Toast.makeText(getContext(), "Se debe seleccionar por lo menos un articulo a traspasar", Toast.LENGTH_SHORT);
                        toast1.show();
                    }

                }
                else
                {
                    Toast toast1 = Toast.makeText(getContext(), "La sucursal origen" +
                            " y la sucursal destino deben de ser diferentes", Toast.LENGTH_SHORT);
                    toast1.show();
                }


                //colocar fecha de vencimiento


            }
        });

        VerificarTipoTraslado();
        LoadListenerTable();
        SpinnerSucursales();

        tabla_inventario.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        tabla_inventario.addDataClickListener(tablaElegirArticuloListener);

        return v;
    }

private void MuestraArticulos(){


        inventarios.clear();
    String url = getString(R.string.Url);
    String ApiPath = url + "/api/inventario/crear_solicitudtraslado?usu_id=" + usu_id + "&esApp=1&code="+code;
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

                RespuestaTodo = response;

                int status = Integer.parseInt(response.getString("estatus"));
                String Mensaje = response.getString("mensaje");

                if (status == 1) {

                    progreso.dismiss();
                    Resultado = response.getJSONObject("resultado");

                    Articulo = Resultado.getJSONArray("aArticulos");
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

                       String suc_id = elemento.getString("suc_id");

                        cantidad = "0";
                        UUIDmodificador="";

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
                                    suc_id,"","","","",
                                    "","", "","","",
                                    "","","", "","","",
                                    "","", "","","",
                                    "", "",UUIDarticulo,UUIDvariante,UUIDmodificador,UUIDexistencias,cantidad,
                                    "","","");
                            inventarios.add(inventario);
                    }



                    for(int i=0;i<inventarios.size();i++)
                    {
                        String suc_art = inventarios.get(i).getSuc_id();
                        String suc_spin = SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
                        if(!suc_art.equals(suc_spin))
                        {
                            inventarios.remove(i);
                            i=i-1;
                        }
                        progreso.dismiss();

                    }

                    for(int i=0;i<inventarios.size();i++)
                    {
                        int checador = 0;
                        String id_art = inventarios.get(i).getTraID();
                        String id_suc_art = inventarios.get(i).getSuc_id();
                        JSONArray Permisos = Resultado.getJSONArray("aArticulosSucursalesPermisos");
                        for (int x = 0; x < Permisos.length(); x++) {
                            JSONObject elemento2 = Permisos.getJSONObject(x);
                            JSONObject permiso_art_id_nodo = elemento2.getJSONObject("sar_id_articulo");
                            String art_id_permiso = permiso_art_id_nodo.getString("uuid");
                            JSONObject permiso_suc_id_nodo = elemento2.getJSONObject("sar_id_sucursal");
                            String suc_id_permiso = permiso_suc_id_nodo.getString("uuid");

                            if(id_art.equals(art_id_permiso)&&id_suc_art.equals(suc_id_permiso))
                            {
                                checador=1;
                                break;
                            }
                            progreso.dismiss();
                        }

                        if(checador!=1)
                        {
                            inventarios.remove(i);
                            i=i-1;
                        }
                    }

                    TrasladoAdapter = new TrasladoAdapter(getContext(), inventarios,tabla_inventario,ArticulosTraslados);
                    tabla_inventario.setDataAdapter(TrasladoAdapter);


                }

            } catch (JSONException e) {
                progreso.dismiss();
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

private void MuestraArticulossinApi(){

    inventarios.clear();
    JSONObject Resultado = null;
        JSONArray Articulo = null;
        JSONObject RespuestaUUID = null;
        JSONObject RespuestaExistencias= null;
        JSONObject RespuestaIDExis = null;
        JSONObject RespuestaVariantes = null;
        JSONObject RespuestaModificadores = null;

        try {
            int status = Integer.parseInt(RespuestaTodo.getString("estatus"));
            String Mensaje = RespuestaTodo.getString("mensaje");

            if (status == 1) {

                progreso.dismiss();
                Resultado = RespuestaTodo.getJSONObject("resultado");

                Articulo = Resultado.getJSONArray("aArticulos");
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

                    String suc_id = elemento.getString("suc_id");

                    cantidad = "0";
                    UUIDmodificador="";

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
                                suc_id,"","","","",
                                "","", "","","",
                                "","","", "","","",
                                "","", "","","",
                                "", "",UUIDarticulo,UUIDvariante,UUIDmodificador,UUIDexistencias,cantidad,
                                "","","");
                        inventarios.add(inventario);
                }


                for(int i=0;i<inventarios.size();i++)
                {
                    String suc_art = inventarios.get(i).getSuc_id();
                    String suc_spin = SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
                    if(!suc_art.equals(suc_spin))
                    {
                        inventarios.remove(i);
                        i=i-1;
                    }


                }

                for(int i=0;i<inventarios.size();i++)
                {
                    int checador = 0;
                    String id_art = inventarios.get(i).getTraID();
                    String id_suc_art = inventarios.get(i).getSuc_id();
                    JSONArray Permisos = Resultado.getJSONArray("aArticulosSucursalesPermisos");
                    for (int x = 0; x < Permisos.length(); x++) {
                        JSONObject elemento2 = Permisos.getJSONObject(x);
                        JSONObject permiso_art_id_nodo = elemento2.getJSONObject("sar_id_articulo");
                        String art_id_permiso = permiso_art_id_nodo.getString("uuid");
                        JSONObject permiso_suc_id_nodo = elemento2.getJSONObject("sar_id_sucursal");
                        String suc_id_permiso = permiso_suc_id_nodo.getString("uuid");

                        if(id_art.equals(art_id_permiso)&&id_suc_art.equals(suc_id_permiso))
                        {
                            checador=1;
                            break;
                        }
                        progreso.dismiss();
                    }

                    if(checador!=1)
                    {
                        inventarios.remove(i);
                        i=i-1;
                    }
                }

                TrasladoAdapter = new TrasladoAdapter(getContext(), inventarios,tabla_inventario,ArticulosTraslados);
                tabla_inventario.setDataAdapter(TrasladoAdapter);
            }

            } catch (JSONException e) {
            progreso.dismiss();
            e.printStackTrace();
        }
    }

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

private void LoadListenerTable() {
    tablaElegirArticuloListener = new TableDataClickListener<InventarioModel>() {
        @Override
        public void onDataClicked(int rowIndex, final InventarioModel clickedData) {
            UUIDart = clickedData.getTraID();
            UUIDvar = clickedData.getUUIDvariante();
            UUIDmod = clickedData.getUUIDmodificador();
            UUIDexist = clickedData.getUUIDexistencias();
        }
    };
}

private void SolicitarTraslado(){

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Solicitando...");
        progreso.show();

       SucursalOrigen = SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
       SucursalDestino = SucursalID.get(SpinnerSucursal2.getSelectedItemPosition());

       String url = getString(R.string.Url);
       String ApiPath = url + "/api/inventario/store_solicitud_traslado";

       JSONArray Arrayarticulos = new JSONArray();

    for(int i=0;i<ArticulosTraslados.size();i++)
    {
        JSONObject request = new JSONObject();
        try {
            request.put( "tat_id_existencias_origen", ArticulosTraslados.get(i).getUUIDexistencias());
            request.put( "tat_id_articulo",ArticulosTraslados.get(i).getTraID());
            request.put( "tat_id_variante",ArticulosTraslados.get(i).getUUIDvariante());
            request.put( "tat_id_modificador",ArticulosTraslados.get(i).getUUIDmodificador());
            request.put( "tat_cantidad", ArticulosTraslados.get(i).getCantidad());
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Arrayarticulos.put(request);
    }



       JSONObject jsonBodyrequest = new JSONObject();
       try {
           jsonBodyrequest.put("usu_id", usu_id);
           jsonBodyrequest.put("esApp", "1");
           jsonBodyrequest.put("tra_id_sucursal_origen",SucursalOrigen);
           jsonBodyrequest.put("tra_id_sucursal_destino",SucursalDestino);
           jsonBodyrequest.put("tra_traslado_temporal",TipoTraslado);
           jsonBodyrequest.put("tra_fecha_temporal",FechaApi);
           jsonBodyrequest.put("tra_motivo",observaciones.getText());
           jsonBodyrequest.put("articulos",Arrayarticulos);
           jsonBodyrequest.put("code",code);



       }catch (JSONException e){
           e.printStackTrace();
       }

       JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,jsonBodyrequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), "Solicitud enviada", Toast.LENGTH_LONG).show();
                Fragment_pop_up_traslado_exitoso dialog = new Fragment_pop_up_traslado_exitoso();
                dialog.setTargetFragment(Fragment_pop_up_traslados.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
                progreso.hide();
           }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progreso.hide();
                        Toast toast1 = Toast.makeText(getContext(), "Error al solicitar traslado", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
       );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }



private void SpinnerSucursales() {

    progreso=new ProgressDialog(getContext());
    progreso.setMessage("Espere un momento por favor");
    progreso.setCanceledOnTouchOutside(false);
    progreso.show();

    JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("code", code);
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

                        progreso.dismiss();
                        Respuesta = response.getJSONObject("resultado");
                        RespuestaSucursales = Respuesta.getJSONArray("aSucursales");

                        for(int x = 0; x < RespuestaSucursales.length(); x++){
                            JSONObject elemento = RespuestaSucursales.getJSONObject(x);

                            RespuestaUUID = elemento.getJSONObject("suc_id");
                            String UUID = RespuestaUUID.getString("uuid");
                            SucursalID.add(UUID);

                            String suc_numero = elemento.getString("suc_numero");
                            String nombre = elemento.getString("suc_nombre");

                            suc_nombre = suc_numero + "(" + nombre + ")";
                            SucursalName.add(suc_nombre);

                        }
                        SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));
                        SpinnerSucursal2.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));

                        MuestraArticulos();
                    }
                    else {
                        progreso.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    }
                } catch (JSONException e) {
                    progreso.dismiss();
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

}