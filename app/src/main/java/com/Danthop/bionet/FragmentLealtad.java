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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
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
    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;
    private String usu_id;
    private List<Puntos_acumulados_model> clientes;
    private ProgressDialog progressDialog;

    private JSONArray Roles;

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

        //Funcion para Obtener Permisos
        try {
            Roles = new JSONArray(sharedPref.getString("sso_Roles", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean Aplica = false;
        boolean Aplica_Permiso = false;
        String fun_id = "";

        String rol_id = "";

        for (int i = 0; i < Roles.length(); i++) {
            try {

                JSONObject Elemento = Roles.getJSONObject(i);
                rol_id = Elemento.getString("rol_id");

                if (rol_id.equals("cbcf943e-ed1e-11e8-8a6e-cb097f5c03df")) {
                    Aplica = Elemento.getBoolean("rol_aplica_en_version");
                    Aplica_Permiso = Elemento.getBoolean("rol_permiso");

                    if (Aplica == true) {
                        if (Aplica_Permiso == true) {

                            JSONArray Elemento1 = Elemento.getJSONArray("rol_funciones");

                            for (int x = 0; x < Elemento1.length(); x++) {

                                JSONObject Elemento2 = Elemento1.getJSONObject(x);

                                fun_id = Elemento2.getString("fun_id");

                                switch (fun_id) {

                                    case "e47fd175-4aca-400f-ae27-2cc03c5ab83b":

                                        Agregar_Articulo = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "50f11d32-2c09-4bb2-8bae-2c7adc091442":

                                        Agregar_Cliente = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "8bd3c3f3-c6a1-46b6-ae15-043e55cb3e84":

                                        Lista_Articulos = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "737a6530-1abc-48e5-ad49-29ed6034c786":

                                        Asignar_Articulo = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "67e65f7e-6b83-48bf-8ced-43e924f1f684":

                                        Asignar_Cliente = Elemento2.getBoolean("fun_permiso");
                                        break;
                                    case "484988dc-0995-414b-8489-925d4d15bbab":

                                        Configuracion_Programa = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "baaaccfa-ed7d-4af2-8cdd-77de77ec28b4":

                                        Caducidad_Puntos = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "4394e399-de02-4d7f-ad25-4ea42085dae8":

                                        Inscribir_Programa = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "4291c378-971d-4ce7-be8e-4fd9d0d4c87b":

                                        Puntos_Acumulados = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "3e58b727-48db-4b39-8293-5331e5049fb5":

                                        Listado_Puntos = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //  {
        //                        "fun_id": "e47fd175-4aca-400f-ae27-2cc03c5ab83b",
        //                        "fun_nombre": "Agregar articulo",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "50f11d32-2c09-4bb2-8bae-2c7adc091442",
        //                        "fun_nombre": "Agregar cliente",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "8bd3c3f3-c6a1-46b6-ae15-043e55cb3e84",
        //                        "fun_nombre": "Artículos",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "737a6530-1abc-48e5-ad49-29ed6034c786",
        //                        "fun_nombre": "Asignar artículos a programa de lealtad",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "67e65f7e-6b83-48bf-8ced-43e924f1f684",
        //                        "fun_nombre": "Asignar clientes a programa de lealtad",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "484988dc-0995-414b-8489-925d4d15bbab",
        //                        "fun_nombre": "Configuración",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "baaaccfa-ed7d-4af2-8cdd-77de77ec28b4",
        //                        "fun_nombre": "Configuración de caducidad de puntos de lealtad",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "94e27e69-ec82-495f-81ef-0af0802e450f",
        //                        "fun_nombre": "Crear promociones para puntos de lealtad",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "725b01c9-7597-41b0-a77d-d23c6752622f",
        //                        "fun_nombre": "Editar",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "ad888f97-ab8d-49a0-8785-dc2b95a8a4a9",
        //                        "fun_nombre": "Editar",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "686bbb18-549c-4ae8-bc90-f5425b0b1d6b",
        //                        "fun_nombre": "Editar",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "b19cea7c-3af3-4286-bb9d-f753c8dc86c1",
        //                        "fun_nombre": "Eliminar",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "a6bbd2be-554b-44b7-93df-ac72ec086fa7",
        //                        "fun_nombre": "Eliminar",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "1190843b-aa8d-4de3-9bab-214747f73e4b",
        //                        "fun_nombre": "Eliminar",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "4394e399-de02-4d7f-ad25-4ea42085dae8",
        //                        "fun_nombre": "Inscribir",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "3e58b727-48db-4b39-8293-5331e5049fb5",
        //                        "fun_nombre": "Listado de artículos",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "4291c378-971d-4ce7-be8e-4fd9d0d4c87b",
        //                        "fun_nombre": "Puntos acumulados",
        //                        "fun_permiso": false
        //                    }

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        SucursalName = new ArrayList<>();
        SucursalID = new ArrayList<>();
        SpinnerSucursal = (Spinner) v.findViewById(R.id.sucursal);

        clientes = new ArrayList<>();

        Programas=v.findViewById(R.id.programas);
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

        Inscribir=v.findViewById(R.id.inscribir);


        Inscribir.setOnClickListener(new View.OnClickListener() {
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
                FragmentLealtadInscribir fragment2 = new FragmentLealtadInscribir();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();

            }

        });

        Articulos=v.findViewById(R.id.articulo);
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

        LoadSucursales();
        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(Listado_Puntos==false){
                    SpinnerSucursal.setEnabled(false);
                    Buscar.setEnabled(false);
                    Buscar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast1 =
                                    Toast.makeText(getContext(), "No cuentas con los permisos necesarios para \n realizar esta acción", Toast.LENGTH_LONG);

                            toast1.show();
                        }
                    });
                }else
                {

                    progressDialog.show();
                    Muestra_clientes();
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        return v;
    }


    private void Muestra_clientes()
    {
        clientes.clear();
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("suc_id",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
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
                        progressDialog.dismiss();

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

    public void LoadSucursales() {
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
                JSONArray RespuestaNodoSucursales = null;
                JSONObject RespuestaNodoID = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");
                    if (status == 1) {
                        progressDialog.dismiss();

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoSucursales = Respuesta.getJSONArray("aSucursales");

                        for (int x = 0; x < RespuestaNodoSucursales.length(); x++) {
                            JSONObject jsonObject1 = RespuestaNodoSucursales.getJSONObject(x);
                            String sucursal = jsonObject1.getString("suc_nombre");
                            SucursalName.add(sucursal);
                            RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                            String id = RespuestaNodoID.getString("uuid");
                            SucursalID.add(id);
                        }
                        SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SucursalName));

                    } else {
                        progressDialog.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();


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
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);


    }

    private void LoadPermisosFunciones()
    {
        if(Listado_Puntos==false){
            clientes.clear();
            clienteAdapter.notifyDataSetChanged();
            Buscar.setEnabled(false);
            Buscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast1 =
                            Toast.makeText(getContext(), "No cuentas con los permisos necesarios para \n realizar esta acción", Toast.LENGTH_LONG);

                    toast1.show();
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
