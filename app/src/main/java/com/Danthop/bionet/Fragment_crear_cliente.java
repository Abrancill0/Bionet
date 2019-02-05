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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.Danthop.bionet.R;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_crear_cliente extends DialogFragment {


    private CheckBox Mismo_email_personal;
    private CheckBox Mismo_direccion_personal;
    private EditText TextNombre;
    private EditText TextColonia;
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
    private EditText TextEstado;
    private String usu_id;

    String URLGetEstados="http://192.168.100.192:8010/api/configuracion/sucursales/select_estados?usu_id=18807ae8-0a10-540c-91cf-aa7eaccf3cbf&esApp=1";


    //private Spinner SpinnerEstado;
    ArrayList<String> EstadoName;


    public Fragment_crear_cliente() {
        // Required empty public constructor
    }

    ProgressDialog progreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_cliente,container, false);
        Mismo_email_personal =(CheckBox) v.findViewById(R.id.Mismo_email);
        Mismo_direccion_personal =(CheckBox) v.findViewById(R.id.Misma_direccion);
        TextNombre=(EditText)v.findViewById(R.id.Text_cliente_Nombre);
        TextColonia=(EditText)v.findViewById(R.id.Text_cliente_colonia);
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
        TextEstado=(EditText)v.findViewById(R.id.Text_cliente_estado);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

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



        //====ComboBOx====
        /*EstadoName=new ArrayList<>();
        SpinnerEstado=(Spinner)v.findViewById(R.id.Text_cliente_estado);
        loadSpinnerData(URLGetEstados);
        SpinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country=   SpinnerEstado.getItemAtPosition(SpinnerEstado.getSelectedItemPosition()).toString();
                Toast.makeText(getContext(),country,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/



        Button guardar = (Button) v.findViewById(R.id.verificar_fiscal);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Verificar_isChecked(Mismo_email_personal,Mismo_direccion_personal);

            }
        });
        return v;
    }


    private void GuardarCliente(){

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("cli_sucursales","0a0ce072-6bec-5272-9c2c-6fdbf51d1235");
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
            request.put("cli_colonia",TextColonia.getText());
            request.put("cli_ciudad",TextCiudad.getText());
            request.put("cli_codigo_postal",TextCp.getText());
            request.put("cli_id_pais","MX");
            request.put("cli_id_estado",TextEstado.getText());
            request.put("cli_estado","");
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

                Intent intent = new Intent(getContext(), Home.class);
                startActivity(intent);

                progreso.hide();

                JSONObject Respuesta = null;

                try {

                    Respuesta = response.getJSONObject("resultado");

                    Toast toast1 =
                            Toast.makeText(getContext(),
                                    "Cliente guardado correctamente", Toast.LENGTH_SHORT);

                    toast1.show();

                } catch (JSONException e) {
                    progreso.hide();
                }


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

    /*private void loadSpinnerData(String url) {
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getInt("estatus")==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("Name");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String country=jsonObject1.getString("Country");
                            EstadoName.add(country);
                        }
                    }
                    SpinnerEstado.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.fragment_crear_cliente,EstadoName));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }*/
    






}
