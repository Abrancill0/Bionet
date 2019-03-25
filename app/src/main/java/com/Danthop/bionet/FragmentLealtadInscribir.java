package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.LealtadConfiguracionesAdapter;
import com.Danthop.bionet.Adapters.LealtadInscribirAdapter;
import com.Danthop.bionet.Tables.SortableLealtadInscribirTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.ConfiguracionLealtadModel;
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
    ProgressDialog progreso;


    private String nombre;
    private String UUID;
    private String telefono;
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

    private String correo_igual;
    private String direccion_igual;

    private Dialog ver_cliente_dialog;

    private Spinner SpinnerSucursal;

    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;


    private List<ClienteModel> clientes;



    public FragmentLealtadInscribir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        tabla_clientes.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                clientes.clear();
                Muestra_clientes();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

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
                                    ""
                            );
                            clientes.add(cliente);
                        }
                        final LealtadInscribirAdapter clienteAdapter = new LealtadInscribirAdapter(getContext(), clientes, tabla_clientes,fr);
                        tabla_clientes.setDataAdapter(clienteAdapter);

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

    private void LoadSpinnerSucursal(){

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
        });

    }

    private void LoadButtons(){
        Lealtad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtad()).commit();

            }
        });


        Programas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadConfiguraciones()).commit();

            }
        });


        Articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadArticulo()).commit();

            }
        });

        aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadAniadirCliente()).commit();
            }
        });


        AniadirTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AniadirTodos.isChecked())
                {
                    LoadConfiguraciones();
                    AniadirTodos();
                }
                else
                {
                    LoadConfiguraciones();
                    EliminarTodos();
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

                        if(TodosAplican.equals("true"))
                        {
                            AniadirTodos.setChecked(true);
                        }
                        else{
                            AniadirTodos.setChecked(false);
                        }
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

                        toast1.show();

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

                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

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

}
