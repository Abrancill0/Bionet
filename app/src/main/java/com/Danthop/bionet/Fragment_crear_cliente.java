package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;

import com.Danthop.bionet.R;

import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_crear_cliente extends DialogFragment {


    private CheckBox Mismo_email_personal;
    private CheckBox Mismo_direccion_personal;
    private EditText TextNombre;
    private EditText TextNumInterior;
    private EditText TextCiudad;
    private EditText TextEmail;
    private EditText TextCalle;
    private EditText TextNumExt;
    private EditText TextCp;
    private EditText TextMunicipio;
    private EditText TextTelefono;
    private EditText TextRfc;
    private EditText TextRazonSocial;
    private String CorreoIgual;
    private String DireccionIgual;
    private EditText TextFacturacionEmail;
    private EditText TextFacturacionEstado;
    private EditText TextFacturacionColonia;
    private EditText TextFacturacionNumExt;
    private EditText TextFacturacionCiudad;
    private EditText TextFacturacionCalle;
    private EditText TextFacturacionCp;
    private EditText TextFacturacionNumInt;
    private EditText TextFacturacionMunicipio;
    private Spinner SpinnerEstado;
    private Spinner SpinnerSucursal;
    private Spinner SpinnerColonia;
    private String usu_id;
    private int Estado_id;
    private String Sucursal_id;
    private String estado;

    String URLGetEstados="http://192.168.100.192:8010/api/configuracion/sucursales/select_estados?usu_id=18807ae8-0a10-540c-91cf-aa7eaccf3cbf&esApp=1";


    //private Spinner SpinnerEstado;
    private ArrayList<String> EstadoName;
    private ArrayList<Integer> EstadoID;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;
    private ArrayList<String> ColoniaName;



    public Fragment_crear_cliente() {
        // Required empty public constructor
    }

    ProgressDialog progreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_cliente,container, false);
        EstadoName=new ArrayList<>();
        EstadoID = new ArrayList<>();
        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();
        ColoniaName = new ArrayList<>();

        Mismo_email_personal =(CheckBox) v.findViewById(R.id.Mismo_email);
        Mismo_direccion_personal =(CheckBox) v.findViewById(R.id.Misma_direccion);
        TextNombre=(EditText)v.findViewById(R.id.Text_cliente_Nombre);
        SpinnerColonia=(Spinner)v.findViewById(R.id.Text_cliente_colonia);
        TextNumInterior=(EditText)v.findViewById(R.id.Text_cliente_num_int);
        TextCiudad=(EditText)v.findViewById(R.id.Text_cliente_ciudad);
        TextEmail=(EditText)v.findViewById(R.id.Text_cliente_email);
        TextCalle=(EditText)v.findViewById(R.id.Text_cliente_calle);
        TextNumExt=(EditText)v.findViewById(R.id.Text_cliente_num_ext);
        TextCp=(EditText)v.findViewById(R.id.Text_cliente_cp);
        TextMunicipio=(EditText)v.findViewById(R.id.Text_cliente_municipio);
        TextTelefono=(EditText)v.findViewById(R.id.Text_cliente_telefono);
        TextRfc=(EditText)v.findViewById(R.id.Text_cliente_rfc);
        TextRazonSocial=(EditText)v.findViewById(R.id.Text_cliente_razon_social);
        SpinnerEstado=(Spinner)v.findViewById(R.id.Text_cliente_estado);
        SpinnerSucursal=(Spinner)v.findViewById(R.id.Text_cliente_sucursal);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        LoadSpinnerEstado();
        LoadSpinnerSucursal();


        //====Para saber si son los mismos datos de facturación=====
        if(Mismo_email_personal.isChecked()){
            CorreoIgual = "True";
        }
        else{
            CorreoIgual = "False";
        }
        if(Mismo_direccion_personal.isChecked()){
            DireccionIgual = "True";
        }
        else{
            DireccionIgual = "False";
        }

        Button guardar = (Button) v.findViewById(R.id.verificar_fiscal);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Verificar_isChecked(Mismo_email_personal,Mismo_direccion_personal);

            }
        });

        TextCp = v.findViewById(R.id.Text_cliente_cp);
        TextCp.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                ColoniaName.clear();
                LoadSpinnerColonias();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        return v;
    }


    private void GuardarCliente(){

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();
        Estado_id = EstadoID.get(SpinnerEstado.getSelectedItemPosition());
        Sucursal_id = SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("cli_sucursales",Sucursal_id);
            request.put("cli_nombre",TextNombre.getText());
            request.put("cli_correo_electronico",TextEmail.getText());
            request.put("cli_telefono",TextTelefono.getText());
            request.put("cli_razon_social",TextRazonSocial.getText());
            request.put("cli_rfc",TextRfc.getText());
            request.put("cli_correos_iguales",CorreoIgual);
            request.put("cli_direcciones_iguales",DireccionIgual);
            request.put("cli_calle",TextCalle.getText());
            request.put("cli_numero_interior",TextNumInterior.getText());
            request.put("cli_numero_exterior",TextNumExt.getText());
            request.put("cli_colonia",SpinnerColonia.getSelectedItem());
            request.put("cli_ciudad",TextCiudad.getText());
            request.put("cli_codigo_postal",TextCp.getText());
            request.put("cli_id_pais",117);
            request.put("cli_id_estado",Estado_id);
            request.put("cli_estado",SpinnerEstado.getSelectedItem().toString());
            request.put("cli_pais","México");
            request.put("cli_correo_electronico_facturacion","");
            request.put("cli_calle_facturacion","");
            request.put("cli_numero_interior_facturacion","");
            request.put("cli_numero_exterior_facturacion","");
            request.put("cli_colonia_facturacion","");
            request.put("cli_ciudad_facturacion","");
            request.put("cli_codigo_postal_facturacion","");
            request.put("cli_id_pais_facturacion","");
            request.put("cli_id_estado_facturacion","");
            request.put("cli_estado_facturacion","");
            request.put("cli_pais_facturacion","");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/clientes/store";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_clientes()).commit();

                progreso.hide();


                String estatus = "0";
                String mensaje = "";
                try {
                     estatus = response.getString("estatus");
                     mensaje = response.getString("mensaje");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int status = Integer.parseInt(estatus);

                if (status == 1)
                {
                    Toast toast1 =
                            Toast.makeText(getContext(),
                                    mensaje, Toast.LENGTH_SHORT);

                    toast1.show();

                }

                //  Respuesta = response.getJSONObject("resultado");

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        progreso.hide();

                        Toast toast1 =
                                Toast.makeText(getContext(),
                                        "Error de conexion", Toast.LENGTH_SHORT);

                        toast1.show();

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }

    public void Verificar_isChecked(CheckBox Mismo_email, CheckBox Misma_direccion){


        if (Mismo_email.isChecked() && Misma_direccion.isChecked())
        {
            GuardarCliente();
            return;
        }
        if(Mismo_email.isChecked())
        {
            final Dialog dialog=new Dialog(getContext());
            dialog.setContentView(R.layout.pop_up_crear_cliente_diferente_direccion);
            dialog.show();

            //====Campo correo electrónico es igual====
            TextFacturacionEstado=(EditText)dialog.findViewById(R.id.Text_cliente_estado_facturacion);
            TextFacturacionColonia=(EditText)dialog.findViewById(R.id.Text_cliente_colonia_facturacion);
            TextFacturacionNumExt=(EditText)dialog.findViewById(R.id.Text_cliente_num_ext_facturacion);
            TextFacturacionCiudad=(EditText)dialog.findViewById(R.id.Text_cliente_ciudad_facturacion);
            TextFacturacionCalle=(EditText)dialog.findViewById(R.id.Text_cliente_calle_facturacion);
            TextFacturacionCp=(EditText)dialog.findViewById(R.id.Text_cliente_cp_facturacion);
            TextFacturacionNumInt=(EditText)dialog.findViewById(R.id.Text_cliente_num_int_facturacion);
            TextFacturacionMunicipio=(EditText)dialog.findViewById(R.id.Text_cliente_municipio_facturacion);

            Button guardar = (Button) dialog.findViewById(R.id.btn_guardar_cliente);
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            return;
        }
        if(Misma_direccion.isChecked())
        {
            final Dialog dialog=new Dialog(getContext());
            dialog.setContentView(R.layout.pop_up_crear_cliente_diferente_email);
            dialog.show();

            TextFacturacionEmail=(EditText)dialog.findViewById(R.id.Text_cliente_correo_facturacion);
            //====Los demás campos son iguales====



            Button guardar = (Button) dialog.findViewById(R.id.btn_guardar_cliente);
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            return;
        }
        else
        {
            final Dialog dialog=new Dialog(getContext());
            dialog.setContentView(R.layout.pop_up_crear_cliente_diferentes_datos);
            dialog.show();

            //====Todos los campos son diferentes====
            TextFacturacionEmail=(EditText)dialog.findViewById(R.id.Text_cliente_correo_facturacion);
            TextFacturacionEstado=(EditText)dialog.findViewById(R.id.Text_cliente_estado_facturacion);
            TextFacturacionColonia=(EditText)dialog.findViewById(R.id.Text_cliente_colonia_facturacion);
            TextFacturacionNumExt=(EditText)dialog.findViewById(R.id.Text_cliente_num_ext_facturacion);
            TextFacturacionCiudad=(EditText)dialog.findViewById(R.id.Text_cliente_ciudad_facturacion);
            TextFacturacionCalle=(EditText)dialog.findViewById(R.id.Text_cliente_calle_facturacion);
            TextFacturacionCp=(EditText)dialog.findViewById(R.id.Text_cliente_cp_facturacion);
            TextFacturacionNumInt=(EditText)dialog.findViewById(R.id.Text_cliente_num_int_facturacion);
            TextFacturacionMunicipio=(EditText)dialog.findViewById(R.id.Text_cliente_municipio_facturacion);

            Button guardar = (Button) dialog.findViewById(R.id.btn_guardar_cliente);
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            return;
        }
    }

    private void LoadSpinnerEstado(){

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

        String ApiPath = url + "/api/configuracion/sucursales/select_estados";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoEstados = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoEstados = Respuesta.getJSONObject("aEstados");

                        for(int x = 0; x < RespuestaNodoEstados.length(); x++){
                            JSONObject jsonObject1=RespuestaNodoEstados.getJSONObject(String.valueOf(x));
                            String estado=jsonObject1.getString("edo_nombre");
                            int id=jsonObject1.getInt("edo_id");
                            EstadoName.add(estado);
                            EstadoID.add(id);
                        }
                        SpinnerEstado.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,EstadoName));

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

    public void LoadSpinnerColonias(){


        if(TextCp.length()==5){


            final String url = "https://api-codigos-postales.herokuapp.com/v2/codigo_postal/"+TextCp.getText().toString();

                // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            JSONArray RespuestaNodoColonias = null;

                            try {
                                RespuestaNodoColonias = response.getJSONArray("colonias");

                                //Aqui llenar el spiner con el respuesta nodo
                                for(int x = 0; x < RespuestaNodoColonias.length(); x++){
                                    //Aqui llenas un arreglo para el adapter del spiner
                                    String colonia=RespuestaNodoColonias.getString(x);
                                    ColoniaName.add(colonia);
                                }
                                SpinnerColonia.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,ColoniaName));
                                estado = response.getString("estado");

                                for (int x=0;x<=EstadoName.size();x++)
                                {
                                    if(estado!=null && estado.equals(EstadoName.get(x)))
                                    {
                                        SpinnerEstado.setSelection(x);
                                        SpinnerEstado.setEnabled(false);
                                        break;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", String.valueOf(error));
                        }
                    }
            );

                    // add it to the RequestQueue
            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

        }
        else{
            SpinnerEstado.setEnabled(true);
            return;
        }
    }






}
