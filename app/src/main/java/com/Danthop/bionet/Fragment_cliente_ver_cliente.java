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
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.HistorialClientesAdapter;
import com.Danthop.bionet.Tables.SortableClientesHistorialTable;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_cliente_ver_cliente extends Fragment {


    public Fragment_cliente_ver_cliente() {
        // Required empty public constructor
    }

    private Button btn_informacion_general;
    private Button btn_cuentas_cobrar;
    private View layout_informacion_general;
    private View layout_cuentas_cobrar;
    private ImageView Regresar;
    private FragmentTransaction fr;
    private ProgressDialog progressDialog;
    private String usu_id="";
    private String code="";

    private TextView Sucursal;
    private TextView CorreoElectronico;
    private TextView Telefono;
    private TextView Calle;
    private TextView CodigoPostal;
    private TextView Ciudad;
    private TextView NumeroInterior;
    private TextView Colonia;
    private TextView Estado;
    private TextView NumeroExterior;
    private TextView RFC;
    private TextView CorreoFact;
    private TextView RazonSocial;
    private TextView CalleFact;
    private TextView CodigoPostalFact;
    private TextView CiudadFact;
    private TextView NumeroInteriorFact;
    private TextView ColoniaFact;
    private TextView EstadoFact;
    private TextView NumeroExteriorFact;
    private Bundle bundle;
    private String Cli_id="";
    private Button Eliminar;
    private Button Editar;
    private SortableClientesHistorialTable HistorialTable;

    private ClienteModel cliente;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientes_ver, container, false);
        LoadLayoutsAndWidgets(v);
        LoadCliente();
        return v;
    }

    private void LoadLayoutsAndWidgets(View v)
    {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        code = sharedPref.getString("sso_code","");
        bundle = getArguments();
        Cli_id=bundle.getString("cli_id");
        fr = getFragmentManager().beginTransaction();
        btn_informacion_general = v.findViewById(R.id.btn_informacion_general);
        btn_cuentas_cobrar = v.findViewById(R.id.btn_cuentas_por_cobrar);
        layout_informacion_general = v.findViewById(R.id.layout_informacion);
        layout_cuentas_cobrar = v.findViewById(R.id.layout_cuentas_cobrar);
        Regresar = v.findViewById(R.id.atras);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btn_informacion_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_informacion_general.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
                btn_cuentas_cobrar.setBackgroundResource(R.drawable.pestanas_desplegables);
                layout_informacion_general.setVisibility(View.VISIBLE);
                layout_cuentas_cobrar.setVisibility(View.GONE);
            }
        });

        btn_cuentas_cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cuentas_cobrar.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
                btn_informacion_general.setBackgroundResource(R.drawable.pestanas_desplegables);
                layout_cuentas_cobrar.setVisibility(View.VISIBLE);
                layout_informacion_general.setVisibility(View.GONE);
            }
        });

        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Fragment_clientes fragment_clientes = new Fragment_clientes();
                    fr.replace(R.id.fragment_container, fragment_clientes).commit();
                }catch (IllegalStateException s)
                {

                }

            }
        });

        Sucursal = v.findViewById(R.id.sucursal);
        CorreoElectronico = v.findViewById(R.id.correo);
        Telefono = v.findViewById(R.id.telefono);
        Calle = v.findViewById(R.id.calle);
        CodigoPostal = v.findViewById(R.id.codigo_postal);
        Ciudad = v.findViewById(R.id.ciudad);
        NumeroInterior = v.findViewById(R.id.numero_interior);
        Colonia = v.findViewById(R.id.colonia);
        Estado= v.findViewById(R.id.estado);
        NumeroExterior = v.findViewById(R.id.numero_exterior);
        RFC = v.findViewById(R.id.rfc);
        CorreoFact = v.findViewById(R.id.correoFact);
        RazonSocial = v.findViewById(R.id.razonSocial);
        CalleFact = v.findViewById(R.id.calleFacturacion);
        CodigoPostalFact = v.findViewById(R.id.codigo_postalFacturacion);
        CiudadFact = v.findViewById(R.id.ciudadFacturacion);
        NumeroInteriorFact = v.findViewById(R.id.numero_interiorFacturacion);
        ColoniaFact = v.findViewById(R.id.coloniaFacturacion);
        EstadoFact = v.findViewById(R.id.estadoFacturacion);
        NumeroExteriorFact = v.findViewById(R.id.numero_exteriorFacturacion);
        HistorialTable = v.findViewById(R.id.tabla_historial);
        HistorialTable.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia2));

        Eliminar=v.findViewById(R.id.eliminar);
        Editar=v.findViewById(R.id.editar);


        EditarCliente();
        EliminarCliente();
    }

    private void LoadCliente()
    {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("cli_id", Cli_id);
            request.put("code",code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/clientes/select-cliente";

        //clienteAdapter.clear();

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoDireccion = null;
                JSONObject elemento = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Respuesta = response.getJSONObject("resultado");

                        elemento = Respuesta.getJSONObject("aCliente");

                            JSONObject ElementoUsuario = elemento.getJSONObject("cli_id");
                            String UUID = ElementoUsuario.getString("uuid");
                            String nombre = elemento.getString("cli_nombre");
                            String correo_electronico = elemento.getString("cli_correo_electronico");

                            String telefono = elemento.getString("cli_telefono");
                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion");
                            String calle = RespuestaNodoDireccion.getString("cli_calle");

                            String estado = RespuestaNodoDireccion.getString("cli_estado");
                            String colonia = RespuestaNodoDireccion.getString("cli_colonia");
                            String num_int = RespuestaNodoDireccion.getString("cli_numero_interior");
                            String num_ext = RespuestaNodoDireccion.getString("cli_numero_exterior");
                            String cp = RespuestaNodoDireccion.getString("cli_codigo_postal");
                            String ciudad = RespuestaNodoDireccion.getString("cli_ciudad");
                            String municipio = RespuestaNodoDireccion.getString("cli_ciudad");

                            String rfc = elemento.getString("cli_rfc");
                            String razon_social = elemento.getString("cli_razon_social");

                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion_fiscal");
                            String cp_fiscal = RespuestaNodoDireccion.getString("cli_codigo_postal");
                            String estado_fiscal = RespuestaNodoDireccion.getString("cli_estado");
                            String municipio_fiscal = RespuestaNodoDireccion.getString("cli_ciudad");
                            String colonia_fiscal = RespuestaNodoDireccion.getString("cli_colonia");
                            String calle_fiscal = RespuestaNodoDireccion.getString("cli_calle");
                            String num_ext_fiscal = RespuestaNodoDireccion.getString("cli_numero_exterior");
                            String num_int_fiscal = RespuestaNodoDireccion.getString("cli_numero_interior");
                            String UltimaCompra = elemento.getString("cli_ultima_compra");
                            String ConsumoPromedio = elemento.getString("cli_promedio_compra");


                            ArrayList<CompraModel> HistorialCompras = new ArrayList<>();
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
                            String email_fiscal="";
                            String correo_igual = elemento.getString("cli_correos_iguales");
                            if (correo_igual.equals("false")) {
                                email_fiscal = elemento.getString("cli_correo_electronico_facturacion");
                            } else if (correo_igual.equals("true")) {
                                email_fiscal = correo_electronico;
                            }

                            String direccion_igual = elemento.getString("cli_direcciones_iguales");


                            cliente = new ClienteModel(
                                    UUID,
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
                                    "",
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
                            cliente.setCiudad_fiscal(RespuestaNodoDireccion.getString("cli_ciudad"));

                            JSONArray Sucursales = elemento.getJSONArray("cli_sucursales");
                            cliente.setSucursal(Sucursales.getString(0));


                        CorreoElectronico.setText(cliente.getCliente_Correo());
                        Telefono.setText(cliente.getCliente_Telefono());
                        Calle.setText(cliente.getCliente_calle());
                        CodigoPostal.setText(cliente.getcliente_cp());
                        Ciudad.setText(cliente.getcliente_ciudad());
                        NumeroInterior.setText(cliente.getcliente_num_int());
                        Colonia.setText(cliente.getcliente_colonia());
                        Estado.setText(cliente.getcliente_estado());
                        NumeroExterior.setText(cliente.getcliente_num_ext());
                        RFC.setText(cliente.getcliente_rfc());
                        CorreoFact.setText(cliente.getCliente_email_facturacion());
                        RazonSocial.setText(cliente.getcliente_razon_social());
                        CalleFact.setText(cliente.getCalle_fiscal());
                        CodigoPostalFact.setText(cliente.getCp_fiscal());
                        CiudadFact.setText(cliente.getCiudad_fiscal());
                        NumeroInteriorFact.setText(cliente.getNum_int_fiscal());
                        ColoniaFact.setText(cliente.getColonia_fiscal());
                        EstadoFact.setText(cliente.getEstado_fiscal());
                        NumeroExteriorFact.setText(cliente.getNum_ext_fiscal());
                        Sucursal.setText(cliente.getSucursal());


                        final HistorialClientesAdapter historialAdapter = new HistorialClientesAdapter(getContext(), HistorialCompras, HistorialTable);
                        HistorialTable.setDataAdapter(historialAdapter);



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

    private void EliminarCliente()
    {
        Eliminar.setOnClickListener(new View.OnClickListener() {
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

                        progressDialog.show();

                        JSONObject request = new JSONObject();
                        try {
                            request.put("usu_id", usu_id);
                            request.put("cli_id", Cli_id);
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
                                        try{
                                            Fragment_clientes fragment_clientes = new Fragment_clientes();
                                            fr.replace(R.id.fragment_container, fragment_clientes).commit();
                                            progressDialog.dismiss();
                                        }catch (IllegalStateException s)
                                        {

                                        }

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
    }

    private void EditarCliente()
    {
        Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString( "nombre",cliente.getCliente_Nombre());
                bundle.putString( "ultima_visita","");
                bundle.putString( "email",cliente.getCliente_Correo());
                bundle.putString( "telefono",cliente.getCliente_Telefono());
                bundle.putString( "cp",cliente.getcliente_cp());
                bundle.putString( "estado",cliente.getcliente_estado());
                bundle.putString( "municipio",cliente.getcliente_municipio());
                bundle.putString( "colonia", cliente.getcliente_colonia());
                bundle.putString( "calle", cliente.getCliente_calle());
                bundle.putString( "numero_interior", cliente.getcliente_num_int());
                bundle.putString( "numero_exterior", cliente.getcliente_num_ext());
                bundle.putString( "sucursal", cliente.getSucursal());
                bundle.putString( "rfc",cliente.getcliente_rfc());
                bundle.putString( "razon_social" , cliente.getcliente_razon_social());
                bundle.putString( "cp_fiscal" , cliente.getCp_fiscal());
                bundle.putString( "estado_fiscal" ,cliente.getEstado_fiscal());
                bundle.putString( "municipio_fiscal" ,cliente.getMunicipio_fiscal());
                bundle.putString( "colonia_fiscal" , cliente.getColonia_fiscal());
                bundle.putString( "calle_fiscal" , cliente.getCalle_fiscal());
                bundle.putString( "numero_interior_fiscal" , cliente.getNum_int_fiscal());
                bundle.putString( "numero_exterior_fiscal" , cliente.getNum_ext_fiscal());
                bundle.putString( "correo_fiscal" , cliente.getCliente_email_facturacion());
                bundle.putString( "correo_igual" , cliente.getCorreo_igual());
                bundle.putString( "direccion_igual" , cliente.getDireccion_igual());
                bundle.putString("UUID", cliente.getCliente_UUID());

                Fragment_editarCliente fragment_editarCliente = new Fragment_editarCliente();
                fragment_editarCliente.setArguments(bundle);
                fr.replace(R.id.fragment_container, fragment_editarCliente).commit();
            }
        });

    }

}
