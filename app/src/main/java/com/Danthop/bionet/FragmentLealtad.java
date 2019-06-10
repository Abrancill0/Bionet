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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.LealtadPuntosAdapter;
import com.Danthop.bionet.Tables.SortablePuntosTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.Puntos_acumulados_model;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.JsonObject;

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
public class FragmentLealtad extends Fragment {
    private SortablePuntosTable tabla_puntos;
    private TableDataClickListener<Puntos_acumulados_model> tablaListener;
    private FragmentTransaction fr;
    private Button Programas;
    private Button Inscribir;
    private Button Articulos;
    ProgressDialog progreso;
    private String nombre;
    private String cli_numero;
    private String correo_electronico;
    private String telefono;
    private String direccion;
    private String rfc;
    private String razon_social;
    private String direccion_fiscal;
    private String puntos;
    private SearchView Buscar;
    private LealtadPuntosAdapter clienteAdapter;

    private String usu_id;
    private List<Puntos_acumulados_model> clientes;
    private ProgressDialog progressDialog;



    public FragmentLealtad() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lealtad,container, false);

        fr = getFragmentManager().beginTransaction();
        tabla_puntos = v.findViewById(R.id.tabla_puntos);
        tabla_puntos.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        Buscar = v.findViewById(R.id.buscarPuntos);


        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        clientes = new ArrayList<>();

        Muestra_clientes();

        Programas=v.findViewById(R.id.programas);
        Programas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadConfiguraciones()).commit();

            }
        });

        Inscribir=v.findViewById(R.id.inscribir);


        Inscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadInscribir()).commit();

            }

        });

        Articulos=v.findViewById(R.id.articulo);
        Articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadArticulo()).commit();

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


        LoadListenerTable();

        tabla_puntos.setSwipeToRefreshEnabled(true);
        tabla_puntos.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_puntos.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //inventarios.clear();
                       // Muestra_Inventario();
                        refreshIndicator.hide();
                    }
                }, 2000);
            }
        });

        tabla_puntos.addDataClickListener(tablaListener);

        return v;
    }


    private void Muestra_clientes()
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

        String ApiPath = url + "/api/programa-de-lealtad/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject ElementoUsuario=null;
                JSONArray RespuestaNodoClientes= null;
                JSONObject RespuestaDireccion = null;
                JSONObject RespuestaDireccionFiscal = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {

                        progressDialog.dismiss();
                        Respuesta = response.getJSONObject("resultado");
                        RespuestaNodoClientes = Respuesta.getJSONArray("aClientes");

                        for(int x = 0; x < RespuestaNodoClientes.length(); x++){
                            JSONObject elemento = RespuestaNodoClientes.getJSONObject(x);

                            ElementoUsuario =  elemento.getJSONObject("cli_id");

                            cli_numero = elemento.getString( "cli_numero");
                            nombre = elemento.getString("cli_nombre");
                            correo_electronico = elemento.getString("cli_correo_electronico");
                            telefono = elemento.getString( "cli_telefono" );

                            RespuestaDireccion = elemento.getJSONObject( "cli_direccion" );
                            String Num_Int = RespuestaDireccion.getString( "cli_numero_interior" );
                            String Num_Ext = RespuestaDireccion.getString( "cli_numero_exterior" );
                            String Colonia = RespuestaDireccion.getString( "cli_colonia" );
                            String Calle = RespuestaDireccion.getString( "cli_calle" );
                            String Ciudad = RespuestaDireccion.getString( "cli_ciudad" );
                            String Estado = RespuestaDireccion.getString( "cli_estado" );
                            String Pais = RespuestaDireccion.getString( "cli_pais" );
                            direccion = "No. Int.:" + Num_Int + "," +" "+ "No. Ext.:" + Num_Ext + "," + " " + "Col.:" + Colonia
                                        + "," + " " + "Calle:" + Calle + "," + " " + "Ciudad:" + Ciudad + "," + " " + Estado + "," + " " + Pais;

                            rfc = elemento.getString( "cli_rfc" );
                            razon_social = elemento.getString( "cli_razon_social" );

                            RespuestaDireccionFiscal = elemento.getJSONObject( "cli_direccion_fiscal" );
                            String NumInt = RespuestaDireccionFiscal.getString( "cli_numero_interior" );
                            String NumExt = RespuestaDireccionFiscal.getString( "cli_numero_exterior" );
                            String Col = RespuestaDireccionFiscal.getString( "cli_colonia" );
                            String Calles = RespuestaDireccionFiscal.getString( "cli_calle" );
                            String Cd = RespuestaDireccionFiscal.getString( "cli_ciudad" );
                            String Estad = RespuestaDireccionFiscal.getString( "cli_estado" );
                            String NomPais = RespuestaDireccionFiscal.getString( "cli_pais" );
                            direccion_fiscal = "No. Int.:" + NumInt + "," + " " + "No. Ext.:" + NumExt + "," + " " + "Col.:" + Col
                                    + "," + " " + "Calle:" + Calles + "," + " " + "Ciudad:" + Cd + "," + " " + Estad + "," + " " + NomPais;


                            puntos = elemento.getString("cli_puntos_disponibles");


                            final Puntos_acumulados_model cliente = new Puntos_acumulados_model(
                                    cli_numero,
                                    nombre,
                                    correo_electronico,
                                    telefono,
                                    direccion,
                                    rfc,
                                    razon_social,
                                    direccion_fiscal,
                                    puntos );
                            clientes.add(cliente);
                        }
                        clienteAdapter = new LealtadPuntosAdapter(getContext(), clientes, tabla_puntos);
                        tabla_puntos.setDataAdapter(clienteAdapter);

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


    private void LoadListenerTable(){
        tablaListener = new TableDataClickListener<Puntos_acumulados_model>() {
            @Override
            public void onDataClicked(int rowIndex, final Puntos_acumulados_model clickedData) {
                final Dialog ver_producto_dialog;
                ver_producto_dialog = new Dialog(getContext());
                ver_producto_dialog.setContentView(R.layout.pop_up_ficha_cliente_lealtad);
                ver_producto_dialog.show();

                TextView cliente_nombre = ver_producto_dialog.findViewById(R.id.cliente_nombre);
                TextView numero_cliente = ver_producto_dialog.findViewById(R.id.numero_cliente);
                TextView correo_electronico = ver_producto_dialog.findViewById(R.id.correo_electronico);
                TextView telefono = ver_producto_dialog.findViewById(R.id.telefono);
                TextView direccion = ver_producto_dialog.findViewById(R.id.direccion);
                TextView rfc = ver_producto_dialog.findViewById(R.id.rfc);
                TextView razon_social = ver_producto_dialog.findViewById(R.id.razon_social);
                TextView direccion_fiscal = ver_producto_dialog.findViewById(R.id.direccion_fiscal);
                TextView puntos_disponibles = ver_producto_dialog.findViewById(R.id.puntos_disponibles);

                cliente_nombre.setText(clickedData.getNombre());
                numero_cliente.setText(clickedData.getNumero_cliente());
                correo_electronico.setText(clickedData.getCorreo_cliente());
                telefono.setText(clickedData.getTelefono());
                direccion.setText(clickedData.getrfc());
                rfc.setText(clickedData.getrfc());
                razon_social.setText(clickedData.getRazon_social());
                direccion_fiscal.setText(clickedData.getDireccion_fiscal());
                puntos_disponibles.setText(clickedData.getAcumulado());

            }
        };
    }

}
