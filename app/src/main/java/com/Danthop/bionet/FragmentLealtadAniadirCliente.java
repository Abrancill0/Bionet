package com.Danthop.bionet;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.LealtadAniadirClienteAdapter;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableLealtadAniadirCliente;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLealtadAniadirCliente extends Fragment {

    private String nombre;
    private String UUID;
    private String telefono;
    private String correo_electronico;
    private String usu_id;
    private SortableLealtadAniadirCliente tabla_clientes;
    private FragmentTransaction fr;
    private ImageView Back;
    private Spinner SpinnerSucursal;

    private List<ClienteModel> clientes;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;
    private ArrayList<String> clientes_id;
    private Button Aniadir;


    public FragmentLealtadAniadirCliente() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");
        View v = inflater.inflate(R.layout.fragment_lealtad_agregar_cliente,container, false);

        tabla_clientes = v.findViewById(R.id.tabla_clientes);
        SpinnerSucursal=v.findViewById(R.id.Sucursal_lealtad);
        Aniadir=v.findViewById(R.id.aceptar_aniadir);
        clientes = new ArrayList<>();
        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();
        clientes_id = new ArrayList<>();
        fr = getFragmentManager().beginTransaction();
        Back = v.findViewById(R.id.atras);

        LoadButtons();
        LoadSpinnerSucursal();
        Aniadir();


        tabla_clientes.setSwipeToRefreshEnabled(true);
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


    private void Muestra_clientes()
    {
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("suc_id",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
            request.put("cli_programa_lealtad", "false");

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
                JSONObject RespuestaNodoDireccion= null;
                JSONObject ElementoUsuario=null;
                JSONArray RespuestaNodoClientes= null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        Respuesta = response.getJSONArray("resultado");

                        for(int x = 0; x < Respuesta.length(); x++){
                            JSONObject elemento = Respuesta.getJSONObject(x);
                            nombre = elemento.getString("cli_nombre");
                            correo_electronico = elemento.getString("cli_correo_electronico");
                            telefono = elemento.getString("cli_telefono");
                            JSONObject cli_id = elemento.getJSONObject("cli_id");
                            UUID = cli_id.getString("uuid");

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
                                    HistorialCompras

                            );
                            clientes.add(cliente);
                        }
                        final LealtadAniadirClienteAdapter clienteAdapter = new LealtadAniadirClienteAdapter(getContext(), clientes, tabla_clientes,fr,clientes_id);
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

    private void Aniadir()
    {
        Aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject request = new JSONObject();
                try
                {
                    request.put("usu_id", usu_id);
                    request.put("esApp", "1");
                    request.put("aClientes",clientes_id);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                String url = getString(R.string.Url);

                String ApiPath = url + "/api/programa-de-lealtad/inscribir-clientes";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject Respuesta = null;
                        JSONArray NodoConfiguraciones;

                        try {

                            int status = Integer.parseInt(response.getString("estatus"));
                            String Mensaje = response.getString("mensaje");


                            if (status == 1)
                            {

                                Toast toast1 =
                                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                toast1.show();
                                fr.replace(R.id.fragment_container,new FragmentLealtadInscribir()).commit();

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

    private void LoadButtons()
    {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadInscribir()).commit();
            }
        });
    }
}
