package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.LealtadInscribirAdapter;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableLealtadInscribirTable;
import com.Danthop.bionet.Tables.SortableLealtadProgramasTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.ProgramaModel;
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

    private Dialog ver_cliente_dialog;

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
        Muestra_clientes();
        tabla_clientes = v.findViewById(R.id.tabla_clientes);
        tabla_clientes.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
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

        Lealtad=v.findViewById(R.id.lealtad);
        Lealtad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtad()).commit();

            }
        });

        Programas=v.findViewById(R.id.programas);
        Programas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadProgramas()).commit();

            }
        });

        Articulos=v.findViewById(R.id.articulo);
        Articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadArticulo()).commit();

            }
        });
        return v;

    }

    private void Muestra_clientes()
    {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();
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

        String ApiPath = url + "/api/clientes/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoDireccion= null;
                JSONObject ElementoUsuario=null;
                JSONArray RespuestaNodoClientes= null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoClientes = Respuesta.getJSONArray("aClientes");

                        for(int x = 0; x < RespuestaNodoClientes.length(); x++){
                            JSONObject elemento = RespuestaNodoClientes.getJSONObject(x);

                            ElementoUsuario =  elemento.getJSONObject("cli_id");

                            UUID = ElementoUsuario.getString( "uuid");
                            nombre = elemento.getString("cli_nombre");
                            correo_electronico = elemento.getString("cli_correo_electronico");
                            telefono = elemento.getString("cli_telefono");
                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion");
                            calle = RespuestaNodoDireccion.getString("cli_calle");

                            estado = RespuestaNodoDireccion.getString( "cli_estado");
                            colonia = RespuestaNodoDireccion.getString( "cli_colonia");
                            num_int = RespuestaNodoDireccion.getString( "cli_numero_interior");
                            num_ext = RespuestaNodoDireccion.getString( "cli_numero_exterior");
                            cp = RespuestaNodoDireccion.getString( "cli_codigo_postal");
                            ciudad = RespuestaNodoDireccion.getString( "cli_ciudad");
                            municipio = RespuestaNodoDireccion.getString( "cli_ciudad");

                            rfc = elemento.getString( "cli_rfc");
                            razon_social = elemento.getString( "cli_razon_social");

                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion_fiscal");
                            cp_fiscal = RespuestaNodoDireccion.getString("cli_codigo_postal");
                            estado_fiscal = RespuestaNodoDireccion.getString("cli_estado");
                            municipio_fiscal = RespuestaNodoDireccion.getString("cli_ciudad");
                            colonia_fiscal = RespuestaNodoDireccion.getString("cli_colonia");
                            calle_fiscal = RespuestaNodoDireccion.getString("cli_calle");
                            num_ext_fiscal = RespuestaNodoDireccion.getString("cli_numero_exterior");
                            num_int_fiscal = RespuestaNodoDireccion.getString("cli_numero_interior");


                            String direccion_igual = elemento.getString("cli_direcciones_iguales");
                            if(direccion_igual.equals("false"))
                            {
                                direccion_fiscal = calle_fiscal + " " + num_ext_fiscal + " " + num_int_fiscal + " " +colonia_fiscal + " " + cp_fiscal + " " + estado_fiscal + " " + municipio_fiscal;
                            }
                            else if (direccion_igual.equals("true"))
                            {
                                direccion_fiscal = calle + " " + num_ext + " " + num_int + " " +colonia + " " + cp + " " + estado + " " + municipio;

                            }

                            String correo_igual = elemento.getString("cli_correos_iguales");
                            if(correo_igual.equals("false"))
                            {
                                email_fiscal = elemento.getString("cli_correo_electronico_facturacion");
                            }
                            else if (correo_igual.equals("true"))
                            {
                                email_fiscal = correo_electronico;
                            }







                            final ClienteModel cliente = new ClienteModel(UUID,
                                    nombre,
                                    correo_electronico,
                                    telefono,
                                    usu_id,
                                    estado,
                                    colonia,
                                    calle,
                                    num_int,
                                    num_ext,
                                    cp,
                                    ciudad,
                                    municipio,
                                    rfc,
                                    razon_social,
                                    direccion_fiscal,
                                    email_fiscal,
                                    cp_fiscal,
                                    estado_fiscal,
                                    municipio_fiscal,
                                    colonia_fiscal,
                                    calle_fiscal,
                                    num_ext_fiscal,
                                    num_int_fiscal
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

        progreso.hide();


    }


}
