package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.LealtadInscribirAdapter;
import com.Danthop.bionet.Tables.SortableLealtadInscribirTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
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

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLealtadInscribir extends Fragment {

    private SortableLealtadInscribirTable tabla_clientes;
    private FragmentTransaction fr;
    private Button Lealtad;
    private Button Programas;
    private Button Articulos;

    private String nombre;
    private String UUID;
    private String telefono;
    private String UltimaCompra;
    private String correo_electronico;
    private String calle;
    private String cli_id;

    private String estado;
    private String colonia;
    private String num_int;
    private String num_ext;
    private String cp;
    private String ciudad;
    private String municipio;
    private String rfc;
    private String razon_social;
    private String cp_fiscal;
    private String estado_fiscal;
    private String municipio_fiscal;
    private String colonia_fiscal;
    private String calle_fiscal;
    private String num_ext_fiscal;
    private String num_int_fiscal;
    private String direccion_fiscal;
    private String email_fiscal;
    private String usu_id;
    private Button aniadir;
    private String Lealtad_con_id;
    private CheckBox AniadirTodos;
    private String TodosAplican;
    private Dialog EliminarAniadirDialog;
    private Button eliminar;
    private TableDataClickListener<ClienteModel> tablaListener;
    private Button cancelarEliminar;
    private String[][] clienteModel;
    private List<ClienteModel> clientes;

    private String correo_igual;
    private String direccion_igual;

    private Dialog ver_cliente_dialog;
    private View tabla_vacia;
    private View tabla_todos_aplican;

    private Spinner SpinnerSucursal;

    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;
    Handler handler;
    private ProgressDialog progressDialog;
    private SearchView Buscar;

    private LealtadInscribirAdapter clienteAdapter;

    private Bundle bundle;
    private boolean  Agregar_Articulo=false;
    private boolean  Agregar_Cliente=false;
    private boolean Lista_Articulos=false;
    private boolean Asignar_Articulo=false;
    private boolean Asignar_Cliente=false;
    private boolean  Configuracion_Programa=false;
    private boolean Caducidad_Puntos=false;
    private boolean Inscribir_Programa = false;
    private boolean Puntos_Acumulados = false;
    private boolean Listado_Puntos = false;

    public FragmentLealtadInscribir() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lealtad_inscribir,container, false);


        fr = getFragmentManager().beginTransaction();
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");
        clientes = new ArrayList<>();
        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();

        tabla_clientes = v.findViewById(R.id.tabla_clientes);
        SpinnerSucursal=v.findViewById(R.id.Sucursal_lealtad);
        Lealtad=v.findViewById(R.id.lealtad);
        Programas=v.findViewById(R.id.programas);
        Articulos=v.findViewById(R.id.articulo);
        aniadir = v.findViewById(R.id.aniadir_cliente);
        AniadirTodos = v.findViewById(R.id.AniadirTodos);
        tabla_vacia = v.findViewById(R.id.Tabla_vacia);
        tabla_todos_aplican = v.findViewById(R.id.todos_aplican);
        Buscar = v.findViewById(R.id.buscarCliente);
        handler= new Handler();
        progressDialog = new ProgressDialog( getContext() );
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        LoadButtons();
        LoadSpinnerSucursal();
        LoadListenerTable();

        tabla_clientes.setSwipeToRefreshEnabled(true);
        tabla_clientes.addDataClickListener(tablaListener);
        tabla_clientes.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_clientes.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clientes.clear();
                        Muestra_clientes();
                        refreshIndicator.hide();
                    }
                }, 2000);
            }
        });


        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                clientes.clear();
                progressDialog.setMessage("Espere un momento por favor");
                progressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoadConfiguraciones();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LoadAniadirTodos(TodosAplican);
                            }
                        }, 5000);
                    }
                }, 2000);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        Buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                clienteAdapter.getFilter().filter(newText);
                return false;
            }
        });

        bundle = getArguments();
        if(bundle!=null)
        {
            Agregar_Articulo=bundle.getBoolean("Agregar_Articulo");
            Agregar_Cliente=bundle.getBoolean("Agregar_Cliente");
            Lista_Articulos=bundle.getBoolean("Lista_Articulos");
            Asignar_Articulo=bundle.getBoolean("Asignar_Articulo");
            Asignar_Cliente=bundle.getBoolean("Asignar_Cliente");
            Configuracion_Programa=bundle.getBoolean("Configuracion_Programa");
            Caducidad_Puntos=bundle.getBoolean("Caducidad_Puntos");
            Inscribir_Programa=bundle.getBoolean("Inscribir_Programa");
            Puntos_Acumulados=bundle.getBoolean("Puntos_Acumulados");
            Listado_Puntos=bundle.getBoolean("Listado_Puntos");
        }




        return v;

    }

    private void LoadListenerTable(){
        tablaListener = new TableDataClickListener<ClienteModel>() {
            @Override
            public void onDataClicked(int rowIndex, ClienteModel clickedData) {

                cli_id = (clickedData.getCliente_UUID());

                LoadDialog();

            }
        };
    }

    private void Muestra_clientes() {
        progressDialog.show();

        tabla_clientes.setEmptyDataIndicatorView(tabla_vacia);
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("suc_id",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
            request.put("cli_programa_lealtad", "true");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/programa-de-lealtad/select-clientes";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Respuesta = null;
                JSONObject NodoClientesSeleccionados = null;
                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");


                    if (status == 1)
                    {

                        Respuesta = response.getJSONArray("resultado");

                        for(int x = 0; x < Respuesta.length(); x++){
                            JSONObject elemento = Respuesta.getJSONObject(x);

                            JSONObject nodo_uuid = elemento.getJSONObject("cli_id");
                            UUID = nodo_uuid.getString("uuid");
                            nombre = elemento.getString("cli_nombre");
                            correo_electronico = elemento.getString("cli_correo_electronico");
                            telefono = elemento.getString("cli_telefono");
                            //UltimaCompra = elemento.getString( "cli_ultima_compra" );

                            List<CompraModel> HistorialCompras = new ArrayList<>();


                            final ClienteModel cliente = new ClienteModel(UUID,
                                    nombre,
                                    correo_electronico,
                                    telefono,
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    HistorialCompras,"",""
                            );
                            clientes.add(cliente);
                        }
                        clienteAdapter = new LealtadInscribirAdapter(getContext(), clientes, tabla_clientes,fr);
                        tabla_clientes.setDataAdapter(clienteAdapter);
                        progressDialog.dismiss();

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();
                        progressDialog.dismiss();



                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();
                    progressDialog.dismiss();



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
                        progressDialog.dismiss();



                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);


    }


    private void LoadSpinnerSucursal(){

        progressDialog.show();
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
                JSONArray  RespuestaNodoSucursales= null;
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
                        LoadConfiguraciones();
                        progressDialog.dismiss();

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();
                        progressDialog.dismiss();



                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();
                    progressDialog.dismiss();



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
                        progressDialog.dismiss();



                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);


    }

    private void LoadDialog(){



        EliminarAniadirDialog=new Dialog(getContext());
        EliminarAniadirDialog.setContentView(R.layout.pop_up_lealtad_eliminar_cliente);
        EliminarAniadirDialog.show();
        cancelarEliminar = EliminarAniadirDialog.findViewById(R.id.Cancelar);
        cancelarEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarAniadirDialog.dismiss();
            }
        });
        eliminar = EliminarAniadirDialog.findViewById(R.id.eliminar_cliente);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject request = new JSONObject();
                try
                {
                    request.put("usu_id", usu_id);
                    request.put("esApp", "1");
                    request.put("cli_id",cli_id);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                String url = getString(R.string.Url);

                String ApiPath = url + "/api/programa-de-lealtad/desactivar-cliente";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            int status = Integer.parseInt(response.getString("estatus"));
                            String Mensaje = response.getString("mensaje");


                            if (status == 1)
                            {

                                Toast toast1 =
                                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                toast1.show();
                                EliminarAniadirDialog.dismiss();
                                clientes.clear();
                                Muestra_clientes();
                                progressDialog.dismiss();


                            }
                            else
                            {
                                Toast toast1 =
                                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                toast1.show();
                                progressDialog.dismiss();



                            }

                        } catch (JSONException e) {

                            Toast toast1 =
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                            toast1.show();
                            progressDialog.dismiss();



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
                                progressDialog.dismiss();



                            }
                        }
                );

                VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

            }
        });

    }

    private void LoadButtons(){

        Lealtad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Agregar_Articulo", Agregar_Articulo);
                bundle.putBoolean("Agregar_Cliente", Agregar_Cliente);
                bundle.putBoolean("Lista_Articulos", Lista_Articulos);
                bundle.putBoolean("Asignar_Articulo", Asignar_Articulo);
                bundle.putBoolean("Asignar_Cliente", Asignar_Cliente);
                bundle.putBoolean("Configuracion_Programa", Configuracion_Programa);
                bundle.putBoolean("Caducidad_Puntos", Caducidad_Puntos);
                bundle.putBoolean("Inscribir_Programa", Inscribir_Programa);
                bundle.putBoolean("Puntos_Acumulados", Puntos_Acumulados);
                bundle.putBoolean("Listado_Puntos", Listado_Puntos);
                FragmentLealtad fragment2 = new FragmentLealtad();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();

            }
        });


        Programas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Agregar_Articulo", Agregar_Articulo);
                bundle.putBoolean("Agregar_Cliente", Agregar_Cliente);
                bundle.putBoolean("Lista_Articulos", Lista_Articulos);
                bundle.putBoolean("Asignar_Articulo", Asignar_Articulo);
                bundle.putBoolean("Asignar_Cliente", Asignar_Cliente);
                bundle.putBoolean("Configuracion_Programa", Configuracion_Programa);
                bundle.putBoolean("Caducidad_Puntos", Caducidad_Puntos);
                bundle.putBoolean("Inscribir_Programa", Inscribir_Programa);
                bundle.putBoolean("Puntos_Acumulados", Puntos_Acumulados);
                bundle.putBoolean("Listado_Puntos", Listado_Puntos);
                FragmentLealtadConfiguraciones fragment2 = new FragmentLealtadConfiguraciones();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();

            }
        });


        Articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Agregar_Articulo", Agregar_Articulo);
                bundle.putBoolean("Agregar_Cliente", Agregar_Cliente);
                bundle.putBoolean("Lista_Articulos", Lista_Articulos);
                bundle.putBoolean("Asignar_Articulo", Asignar_Articulo);
                bundle.putBoolean("Asignar_Cliente", Asignar_Cliente);
                bundle.putBoolean("Configuracion_Programa", Configuracion_Programa);
                bundle.putBoolean("Caducidad_Puntos", Caducidad_Puntos);
                bundle.putBoolean("Inscribir_Programa", Inscribir_Programa);
                bundle.putBoolean("Puntos_Acumulados", Puntos_Acumulados);
                bundle.putBoolean("Listado_Puntos", Listado_Puntos);
                FragmentLealtadArticulo fragment2 = new FragmentLealtadArticulo();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();

            }
        });

        aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadAniadirCliente()).commit();
                onDetach();
            }
        });
    }

    private void LoadAniadirTodos(String AniadirAll)
    {
        if(AniadirAll.equals("true"))
        {
                            AniadirTodos();
                            tabla_vacia.setVisibility(View.GONE);
                            tabla_clientes.setEmptyDataIndicatorView(tabla_todos_aplican);
                            clientes.clear();
                            final LealtadInscribirAdapter clienteAdapter = new LealtadInscribirAdapter(getContext(), clientes, tabla_clientes,fr);
                            tabla_clientes.setDataAdapter(clienteAdapter);
                            AniadirTodos.setChecked(true);
                            progressDialog.hide();

        }
        else if(AniadirAll.equals("false"))
        {
                            EliminarTodos();
                            tabla_todos_aplican.setVisibility(View.GONE);
                            clientes.clear();
                            Muestra_clientes();
                            AniadirTodos.setChecked(false);
                            progressDialog.hide();
        }
        AniadirTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage( "Cargando..." );
                progressDialog.show();
                if(AniadirTodos.isChecked())
                {
                    progressDialog.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LoadConfiguraciones();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AniadirTodos();
                                    tabla_vacia.setVisibility(View.GONE);
                                    tabla_clientes.setEmptyDataIndicatorView(tabla_todos_aplican);
                                    clientes.clear();
                                    final LealtadInscribirAdapter clienteAdapter = new LealtadInscribirAdapter(getContext(), clientes, tabla_clientes,fr);
                                    tabla_clientes.setDataAdapter(clienteAdapter);
                                    AniadirTodos.setChecked(true);
                                    progressDialog.hide();
                                }
                            }, 2000);
                        }
                    }, 2000);
                }
                else
                {
                    progressDialog.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LoadConfiguraciones();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    EliminarTodos();
                                    tabla_todos_aplican.setVisibility(View.GONE);
                                    clientes.clear();
                                    Muestra_clientes();
                                    AniadirTodos.setChecked(false);
                                    progressDialog.hide();
                                }
                            }, 2000);
                        }
                    }, 2000);
                }
            }
        });
    }

    private void LoadConfiguraciones(){
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("cpl_id_sucursal",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/programa-de-lealtad/select-conf-puntos";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");


                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");
                        JSONObject NodoConfiguraciones2 = Respuesta.getJSONObject("aConfiguracion");
                        TodosAplican = NodoConfiguraciones2.getString("con_todos_clientes_programa_lealtad");
                        JSONObject elemento_con_id = NodoConfiguraciones2.getJSONObject("con_id");
                        Lealtad_con_id = elemento_con_id.getString("uuid");
                        progressDialog.dismiss();

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();
                        progressDialog.dismiss();



                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();
                    progressDialog.dismiss();


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
                        progressDialog.dismiss();



                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }

    private void AniadirTodos() {

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("con_id",Lealtad_con_id);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/programa-de-lealtad/marcar-todos-cliente";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");


                    if (status == 1)
                    {

                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        progressDialog.dismiss();



                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();
                        progressDialog.dismiss();


                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();
                    progressDialog.dismiss();


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
                        progressDialog.dismiss();


                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }

    private void EliminarTodos() {

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("con_id",Lealtad_con_id);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/programa-de-lealtad/desmarcar-todos-cliente";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");


                    if (status == 1)
                    {

                        try{
                            Toast toast1 =
                                    Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        }catch (NullPointerException a)
                        {

                        }
                        progressDialog.dismiss();


                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();
                        progressDialog.dismiss();


                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();
                    progressDialog.dismiss();


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
                        progressDialog.dismiss();


                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }

    private void LoadPermisosFunciones()
    {
        if(Asignar_Cliente==false)
        {
            AniadirTodos.setEnabled(false);
        }
        if(Agregar_Cliente==false)
        {
            aniadir.setEnabled(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
