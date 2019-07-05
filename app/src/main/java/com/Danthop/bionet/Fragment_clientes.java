package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.HistorialClientesAdapter;
import com.Danthop.bionet.Tables.SortableClientesHistorialTable;
import com.Danthop.bionet.Tables.SortableClientesTable;
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

import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_clientes extends Fragment {
    private String usu_id;
    private String[][] clienteModel;
    private SortableClientesTable tabla_clientes;
    private ProgressDialog progreso;
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
    private String correo_igual;
    private String direccion_igual;
    private String Estatus;
    private String UltimaCompra;
    private String ConsumoPromedio;
    private Dialog ver_cliente_dialog;
    private FragmentTransaction fr;
    private TableDataClickListener<ClienteModel> tablaListener;
    private ProgressDialog progressDialog;
    private SearchView BuscarCliente;
    private ClienteAdapter clienteAdapter;
    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;
    private JSONArray Roles;

    private List<ClienteModel> clientes;
    private List<CompraModel> HistorialCompras;

    private Boolean Crear_Cliente = false;
    private Boolean Editar_Cliente = false;
    private Boolean Eliminar_Cliente = false;
    private Boolean Listado_Clientes = false;
    private Boolean Ficha_Tecnica_cliente = false;

    private Button btn_crear_cliente;
    private Button verCliente;
    private Button eliminarCliente;
    private String code="";

    public Fragment_clientes() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientes, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        SucursalName = new ArrayList<>();
        SucursalID = new ArrayList<>();
        SpinnerSucursal = (Spinner) v.findViewById(R.id.sucursal);

        tabla_clientes = (SortableClientesTable) v.findViewById(R.id.tabla_clientes);
        ver_cliente_dialog = new Dialog(getContext());
        ver_cliente_dialog.setContentView(R.layout.pop_up_ficha_cliente);
        btn_crear_cliente = v.findViewById(R.id.btn_crear_cliente);

        BuscarCliente = (SearchView) v.findViewById(R.id.TextSearchClientes);

        fr = getFragmentManager().beginTransaction();

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        usu_id = sharedPref.getString("usu_id", "");
        code = sharedPref.getString("sso_code","");

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

                if (rol_id.equals("cbcf9420-ed1e-11e8-8a6e-cb097f5c03df")) {
                    Aplica = Elemento.getBoolean("rol_aplica_en_version");
                    Aplica_Permiso = Elemento.getBoolean("rol_permiso");

                    if (Aplica == true) {
                        if (Aplica_Permiso == true) {

                            JSONArray Elemento1 = Elemento.getJSONArray("rol_funciones");

                            for (int x = 0; x < Elemento1.length(); x++) {

                                JSONObject Elemento2 = Elemento1.getJSONObject(x);

                                fun_id = Elemento2.getString("fun_id");

                                switch (fun_id) {

                                    case "1d8afd39-7569-45a5-9ada-f253e725b5b9":

                                        Crear_Cliente = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "cc3cd942-126e-4b9e-8863-45a1f20b20a8":

                                        Editar_Cliente = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "968767f2-fce8-42f7-9fbd-17723d03d691":

                                        Eliminar_Cliente = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "5d7e98e9-5b20-4a63-8e67-c4c85f2bebe3":

                                        Listado_Clientes = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "14758e15-7bf8-4bdc-8586-2adda7e1c996":

                                        Ficha_Tecnica_cliente = Elemento2.getBoolean("fun_permiso");
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


        //Crear usuario
        //1d8afd39-7569-45a5-9ada-f253e725b5b9
        //Editar Usuario
        //cc3cd942-126e-4b9e-8863-45a1f20b20a8
        //Eliminar Usuario
        //968767f2-fce8-42f7-9fbd-17723d03d691
        //Listado de clientes
        //5d7e98e9-5b20-4a63-8e67-c4c85f2bebe3
        //Ver Ficha tecnica cliente
        //14758e15-7bf8-4bdc-8586-2adda7e1c996


        clientes = new ArrayList<>();


        LoadSucursales();
        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Listado_Clientes==false)
                {
                    BuscarCliente.setEnabled(false);
                    BuscarCliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast1 =
                                    Toast.makeText(getContext(), "No cuentas con los permisos necesarios para \n realizar esta acción", Toast.LENGTH_LONG);

                            toast1.show();
                        }
                    });
                }
                else
                {
                    progressDialog.show();
                    Muestra_clientes();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_crear_cliente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                fr.replace(R.id.fragment_container, new Fragment_crear_cliente()).commit();
                onDetach();
            }
        });

        BuscarCliente.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
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


        tabla_clientes.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        LoadListener();
        LoadPermisosFunciones();


        return v;

    }

    private void Muestra_clientes() {
        clientes.clear();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("usu_suc", SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
            request.put("code",code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/clientes/index_app";

        //clienteAdapter.clear();

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoDireccion = null;
                JSONObject ElementoUsuario = null;
                JSONArray RespuestaNodoClientes = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoClientes = Respuesta.getJSONArray("aClientes");

                        clienteModel = new String[RespuestaNodoClientes.length()][5];

                        for (int x = 0; x < RespuestaNodoClientes.length(); x++) {
                            JSONObject elemento = RespuestaNodoClientes.getJSONObject(x);

                            ElementoUsuario = elemento.getJSONObject("cli_id");

                            UUID = ElementoUsuario.getString("uuid");
                            nombre = elemento.getString("cli_nombre");
                            correo_electronico = elemento.getString("cli_correo_electronico");

                            telefono = elemento.getString("cli_telefono");
                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion");
                            calle = RespuestaNodoDireccion.getString("cli_calle");

                            estado = RespuestaNodoDireccion.getString("cli_estado");
                            colonia = RespuestaNodoDireccion.getString("cli_colonia");
                            num_int = RespuestaNodoDireccion.getString("cli_numero_interior");
                            num_ext = RespuestaNodoDireccion.getString("cli_numero_exterior");
                            cp = RespuestaNodoDireccion.getString("cli_codigo_postal");
                            ciudad = RespuestaNodoDireccion.getString("cli_ciudad");
                            municipio = RespuestaNodoDireccion.getString("cli_ciudad");

                            rfc = elemento.getString("cli_rfc");
                            razon_social = elemento.getString("cli_razon_social");

                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion_fiscal");
                            cp_fiscal = RespuestaNodoDireccion.getString("cli_codigo_postal");
                            estado_fiscal = RespuestaNodoDireccion.getString("cli_estado");
                            municipio_fiscal = RespuestaNodoDireccion.getString("cli_ciudad");
                            colonia_fiscal = RespuestaNodoDireccion.getString("cli_colonia");
                            calle_fiscal = RespuestaNodoDireccion.getString("cli_calle");
                            num_ext_fiscal = RespuestaNodoDireccion.getString("cli_numero_exterior");
                            num_int_fiscal = RespuestaNodoDireccion.getString("cli_numero_interior");
                            UltimaCompra = elemento.getString("cli_ultima_compra");
                            ConsumoPromedio = elemento.getString("cli_promedio_compra");


                            HistorialCompras = new ArrayList<>();
                            JSONArray comprasNodo = elemento.getJSONArray("ventas");
                            for (int d = 0; d < comprasNodo.length(); d++) {
                                JSONObject elementoCompra = comprasNodo.getJSONObject(d);
                                String NoTicket = elementoCompra.getString("tic_numero");
                                String Importe = elementoCompra.getString("tic_importe_total");
                                String Fecha = elementoCompra.getString("fecha_hora_venta");
                                CompraModel compra = new CompraModel(NoTicket, Importe, Fecha);
                                HistorialCompras.add(compra);
                                if(d==4)
                                {
                                    break;
                                }
                            }


                            direccion_igual = elemento.getString("cli_direcciones_iguales");
                            if (direccion_igual.equals("false")) {
                                direccion_fiscal = calle_fiscal + " " + num_ext_fiscal + " " + num_int_fiscal + " " + colonia_fiscal + " " + cp_fiscal + " " + estado_fiscal + " " + municipio_fiscal;
                            } else if (direccion_igual.equals("true")) {
                                direccion_fiscal = calle + " " + num_ext + " " + num_int + " " + colonia + " " + cp + " " + estado + " " + municipio;

                            }

                            correo_igual = elemento.getString("cli_correos_iguales");
                            if (correo_igual.equals("false")) {
                                email_fiscal = elemento.getString("cli_correo_electronico_facturacion");
                            } else if (correo_igual.equals("true")) {
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
                                    num_int_fiscal,
                                    correo_igual,
                                    direccion_igual,
                                    HistorialCompras,
                                    UltimaCompra,
                                    ConsumoPromedio
                            );
                            clientes.add(cliente);
                        }

                        clienteAdapter = new ClienteAdapter(getContext(), clientes, tabla_clientes, fr);
                        tabla_clientes.setDataAdapter(clienteAdapter);
                        progressDialog.dismiss();
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


    public void LoadSucursales() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("code",code);

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

    private void LoadListener()
    {

        tablaListener = new TableDataClickListener<ClienteModel>()

        {
            @Override
            public void onDataClicked(int rowIndex, final ClienteModel clickedData) {
                Dialog ver_cliente_dialog;
                ver_cliente_dialog = new Dialog(getContext());
                ver_cliente_dialog.setContentView(R.layout.pop_up_ficha_cliente);
                ver_cliente_dialog.show();

                HistorialCompras = new ArrayList<>();
                for (int d = 0; d < clickedData.getCompras().size(); d++) {
                    String NoTicket = clickedData.getCompras().get(d).getNumero();
                    String Importe = clickedData.getCompras().get(d).getImporte();
                    String Fecha = clickedData.getCompras().get(d).getFechaCompra();
                    CompraModel compra = new CompraModel(NoTicket, Importe, Fecha);
                    HistorialCompras.add(compra);
                }


                SortableClientesHistorialTable HistorialTable = ver_cliente_dialog.findViewById(R.id.historial_compras);
                final HistorialClientesAdapter historialAdapter = new HistorialClientesAdapter(getContext(), HistorialCompras, HistorialTable);
                HistorialTable.setDataAdapter(historialAdapter);

                ViewCompat.setNestedScrollingEnabled(HistorialTable, true);


                TextView NameCliente = ver_cliente_dialog.findViewById(R.id.cliente_nombre);
                TextView CorreoCliente = ver_cliente_dialog.findViewById(R.id.email_cliente);
                TextView TelefonoCliente = ver_cliente_dialog.findViewById(R.id.telefono_cliente);

                TextView EstadoCliente = ver_cliente_dialog.findViewById(R.id.estado_cliente);
                TextView CalleCliente = ver_cliente_dialog.findViewById(R.id.calle_cliente);

                TextView ColoniaCliente = ver_cliente_dialog.findViewById(R.id.colonia_cliente);
                TextView NumExtCliente = ver_cliente_dialog.findViewById(R.id.numero_exterior_cliente);
                TextView NumIntCliente = ver_cliente_dialog.findViewById(R.id.numero_interior_cliente);
                TextView CPCliente = ver_cliente_dialog.findViewById(R.id.cp_cliente);
                TextView CiudadCliente = ver_cliente_dialog.findViewById(R.id.ciudad_cliente);

                TextView MunicipioCliente = ver_cliente_dialog.findViewById(R.id.municipio_cliente);
                TextView RFCCliente = ver_cliente_dialog.findViewById(R.id.rfc_cliente);
                TextView RazonSocialCliente = ver_cliente_dialog.findViewById(R.id.razon_social_cliente);

                TextView DireccionFiscal = ver_cliente_dialog.findViewById(R.id.direccion_fiscal_cliente);
                TextView EmailFiscal = ver_cliente_dialog.findViewById(R.id.email_facturacion_cliente);

                NameCliente.setText(clickedData.getCliente_Nombre());
                CorreoCliente.setText(clickedData.getCliente_Correo());
                TelefonoCliente.setText(clickedData.getCliente_Telefono());
                EstadoCliente.setText(clickedData.getcliente_estado());

                ColoniaCliente.setText(clickedData.getcliente_colonia());
                NumExtCliente.setText(clickedData.getcliente_num_ext());
                NumIntCliente.setText(clickedData.getcliente_num_int());
                CPCliente.setText(clickedData.getcliente_cp());
                CiudadCliente.setText(clickedData.getcliente_ciudad());

                MunicipioCliente.setText(clickedData.getcliente_municipio());
                RFCCliente.setText(clickedData.getcliente_rfc());
                RazonSocialCliente.setText(clickedData.getcliente_razon_social());
                CalleCliente.setText(clickedData.getCliente_calle());

                DireccionFiscal.setText(clickedData.getCliente_direccion_fiscal());
                EmailFiscal.setText(clickedData.getCliente_email_facturacion());

//
                verCliente = ver_cliente_dialog.findViewById(R.id.ver_cliente);
                  verCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("cli_id", clickedData.getCliente_UUID());
                        Fragment_cliente_ver_cliente fragment_ver_cliente = new Fragment_cliente_ver_cliente();
                        fragment_ver_cliente.setArguments(bundle);
                        fr.replace(R.id.fragment_container, fragment_ver_cliente).commit();
                        ver_cliente_dialog.dismiss();
                    }
                });

                eliminarCliente = ver_cliente_dialog.findViewById(R.id.eliminar_cliente);
                eliminarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog pop_up_confirmacion_eliminar = new Dialog(getContext());
                        pop_up_confirmacion_eliminar.setContentView(R.layout.pop_up_confirmar_eliminar_cliente);
                        pop_up_confirmacion_eliminar.show();
                        Button aceptar = pop_up_confirmacion_eliminar.findViewById(R.id.AceptarEliminar);
                        Button cancelar = pop_up_confirmacion_eliminar.findViewById(R.id.CancelarEliminar);

                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                JSONObject request = new JSONObject();
                                try {
                                    request.put("usu_id", clickedData.getCliente_usu_id());
                                    request.put("cli_id", clickedData.getCliente_UUID());
                                    request.put("esApp", "1");
                                    request.put("code",code);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String url = getString(R.string.Url);

                                String ApiPath = url + "api/clientes/delete";

                                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        JSONObject Respuesta = null;

                                        try {

                                            int status = Integer.parseInt(response.getString("estatus"));
                                            String Mensaje = response.getString("mensaje");

                                            if (status == 1) {

                                                Toast toast1 =
                                                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                                toast1.show();
                                                ver_cliente_dialog.dismiss();
                                                clientes.clear();
                                                Muestra_clientes();

                                            } else {
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

                                pop_up_confirmacion_eliminar.dismiss();


                            }
                        });

                        cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pop_up_confirmacion_eliminar.dismiss();
                            }
                        });

                    }
                });

                if(Editar_Cliente==true){
                    verCliente.setEnabled(true);
                }else{
                    verCliente.setEnabled(false);
                    verCliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast1 =
                                    Toast.makeText(getContext(), "No cuentas con los permisos necesarios para \n realizar esta acción", Toast.LENGTH_LONG);

                            toast1.show();
                        }
                    });
                }

                if(Eliminar_Cliente==true){
                    eliminarCliente.setEnabled(true);
                }else{
                    eliminarCliente.setEnabled(false);
                    eliminarCliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast1 =
                                    Toast.makeText(getContext(), "No cuentas con los permisos necesarios para \n realizar esta acción", Toast.LENGTH_LONG);

                            toast1.show();
                        }
                    });
                }

                if(Ficha_Tecnica_cliente=true)
                {

                }else{
                    tabla_clientes.addDataClickListener(new TableDataClickListener<ClienteModel>() {
                        @Override
                        public void onDataClicked(int rowIndex, ClienteModel clickedData) {
                            Toast toast1 =
                                    Toast.makeText(getContext(), "No cuentas con los permisos necesarios para \n realizar ver la ficha técnica", Toast.LENGTH_LONG);

                            toast1.show();
                        }
                    });
                }

            }
        }

        ;

        tabla_clientes.addDataClickListener(tablaListener);


    }

    private void LoadPermisosFunciones()
    {
        if(Crear_Cliente==false)
        {
            btn_crear_cliente.setEnabled(false);
        }



    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
