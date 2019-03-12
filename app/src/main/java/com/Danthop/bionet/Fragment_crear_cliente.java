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
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

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
    private EditText TextEmail;
    private EditText TextCalle;
    private EditText TextNumExt;
    private EditText TextCp;
    private EditText TextMunicipio;
    private EditText TextTelefono;
    private EditText TextRfc;
    private EditText TextRazonSocial;
    private Boolean CorreoIgual;
    private Boolean DireccionIgual;
    private EditText TextFacturacionEmail;
    private Spinner TextFacturacionEstado;
    private Spinner TextFacturacionColonia;
    private EditText TextFacturacionNumExt;
    private EditText TextFacturacionCalle;
    private EditText TextFacturacionCp;
    private EditText TextFacturacionNumInt;
    private EditText TextFacturacionMunicipio;
    private Spinner SpinnerEstado;
    private Spinner SpinnerSucursal;
    private Spinner SpinnerColonia;
    private Spinner SpinnerOpcion;
    private String usu_id;
    private int Estado_id;
    private int Estado_id_fiscal;
    private String Sucursal_id;
    private String estado;
    private Toast toast2;

    String URLGetEstados="http://192.168.100.192:8010/api/configuracion/sucursales/select_estados?usu_id=18807ae8-0a10-540c-91cf-aa7eaccf3cbf&esApp=1";

    //private Spinner SpinnerEstado;
    private ArrayList<String> EstadoName;
    private ArrayList<Integer> EstadoID;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;
    private ArrayList<String> ColoniaName;

    private ArrayList<String> EstadoNameFiscal;
    private ArrayList<Integer> EstadoIDFiscal;
    private ArrayList<String> ColoniaNameFiscal;

    private LinearLayout LayoutDireccionFiscal;
    private LinearLayout LayoutEmail;

    private final static String[] opciones = { "N/A", "Email de Facturación", "Dirección Fiscal", "Ambas"};


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

        EstadoNameFiscal=new ArrayList<>();
        EstadoIDFiscal = new ArrayList<>();
        ColoniaNameFiscal=new ArrayList<>();

        TextNombre=(EditText)v.findViewById(R.id.Text_cliente_Nombre);
        SpinnerColonia=(Spinner)v.findViewById(R.id.Text_cliente_colonia);
        TextNumInterior=(EditText)v.findViewById(R.id.Text_cliente_num_int);
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
        SpinnerOpcion=(Spinner)v.findViewById(R.id.Opcion);

        TextFacturacionEmail=(EditText)v.findViewById(R.id.Text_cliente_correo_facturacion);
        TextFacturacionEstado=(Spinner)v.findViewById(R.id.Text_cliente_estado_facturacion);
        TextFacturacionColonia=(Spinner)v.findViewById(R.id.Text_cliente_colonia_facturacion);
        TextFacturacionNumExt=(EditText)v.findViewById(R.id.Text_cliente_num_ext_facturacion);
        TextFacturacionCalle=(EditText)v.findViewById(R.id.Text_cliente_calle_facturacion);
        TextFacturacionNumInt=(EditText)v.findViewById(R.id.Text_cliente_num_int_facturacion);
        TextFacturacionMunicipio=(EditText)v.findViewById(R.id.Text_cliente_municipio_facturacion);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        LayoutDireccionFiscal = v.findViewById(R.id.LayoutDireccionFiscal);
        LayoutEmail = v.findViewById(R.id.LayoutEmail);

        LayoutDireccionFiscal.setVisibility(View.GONE);
        LayoutEmail.setVisibility(View.GONE);

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, opciones);
        SpinnerOpcion.setAdapter(adapter);

        SpinnerOpcion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                VerificarOpcion();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        VerificarOpcion();
        LoadSpinnerEstado();
        LoadSpinnerSucursal();


        Button guardar = (Button) v.findViewById(R.id.verificar_fiscal);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GuardarCliente();

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

        TextFacturacionCp=(EditText)v.findViewById(R.id.Text_cliente_cp_facturacion);
        TextFacturacionCp.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                ColoniaNameFiscal.clear();
                LoadSpinnerFiscalColonias();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        return v;
    }


    private void VerificarOpcion(){

        String Seleccion;
        Seleccion = SpinnerOpcion.getSelectedItem().toString();
        if(Seleccion.equals("N/A"))
        {
            LayoutDireccionFiscal.setVisibility(View.GONE);
            LayoutEmail.setVisibility(View.GONE);
        }
        else if(Seleccion.equals("Email de Facturación"))
        {
            LayoutEmail.setVisibility(View.VISIBLE);
            LayoutDireccionFiscal.setVisibility(View.GONE);
        }
        else if(Seleccion.equals("Dirección Fiscal"))
        {
            LayoutDireccionFiscal.setVisibility(View.VISIBLE);
            LayoutEmail.setVisibility(View.GONE);
        }
        else if(Seleccion.equals("Ambas"))
        {
            LayoutDireccionFiscal.setVisibility(View.VISIBLE);
            LayoutEmail.setVisibility(View.VISIBLE);
        }

    }


    private void GuardarCliente(){

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();
        Estado_id_fiscal=EstadoIDFiscal.get(TextFacturacionEstado.getSelectedItemPosition());
        Estado_id = EstadoID.get(SpinnerEstado.getSelectedItemPosition());
        Sucursal_id = SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
        int Suc_DatosFisc =0;


        String FactuacionEmail ="";
        String FacturacionCalle ="";
        String FacturacionNumInt ="";
        String FacturacionNumExt ="";
        String FacturacionColonia ="";
        String FacturacionMunicipio ="";
        String FacturacionCp ="";



        String Seleccion;
        Seleccion = SpinnerOpcion.getSelectedItem().toString();
        if(Seleccion.equals("N/A"))
        {
             FactuacionEmail = String.valueOf(TextEmail.getText());
             FacturacionCalle = String.valueOf(TextCalle.getText());
             FacturacionNumInt = String.valueOf(TextNumInterior.getText());
             FacturacionNumExt = String.valueOf(TextNumExt.getText());
             FacturacionColonia = String.valueOf(SpinnerColonia.getSelectedItem());
             FacturacionMunicipio = String.valueOf(TextMunicipio.getText());
             FacturacionCp = String.valueOf(TextCp.getText());
             CorreoIgual= true;
             DireccionIgual = true;
        }
        else if(Seleccion.equals("Email de Facturación"))
        {

            FactuacionEmail = String.valueOf( TextFacturacionEmail.getText() );


            FacturacionCalle = String.valueOf(TextCalle.getText());
            FacturacionNumInt = String.valueOf(TextNumInterior.getText());
            FacturacionNumExt = String.valueOf(TextNumExt.getText());
            FacturacionColonia = String.valueOf(SpinnerColonia.getSelectedItem());
            FacturacionMunicipio = String.valueOf(TextMunicipio.getText());
            FacturacionCp = String.valueOf(TextCp.getText());

            CorreoIgual= false;
            DireccionIgual = true;

        }
        else if(Seleccion.equals("Dirección Fiscal"))
        {
            FactuacionEmail = String.valueOf(TextEmail.getText());

            FacturacionCalle = String.valueOf(TextFacturacionCalle.getText());
            FacturacionNumInt = String.valueOf(TextFacturacionNumInt.getText());
            FacturacionNumExt = String.valueOf(TextFacturacionNumExt.getText());
            FacturacionColonia = String.valueOf(TextFacturacionColonia.getSelectedItem().toString());
            FacturacionCp = String.valueOf(TextFacturacionCp.getText());
            FacturacionMunicipio = String.valueOf( (TextFacturacionMunicipio.getText()) );
            Suc_DatosFisc = EstadoIDFiscal.get(SpinnerSucursal.getSelectedItemPosition());

            CorreoIgual= true;
            DireccionIgual = false;

        }
        else if(Seleccion.equals("Ambas"))
        {
            FactuacionEmail = String.valueOf( TextFacturacionEmail.getText() );
            FacturacionCalle = String.valueOf(TextFacturacionCalle.getText());
            FacturacionNumInt = String.valueOf(TextFacturacionNumInt.getText());
            FacturacionNumExt = String.valueOf(TextFacturacionNumExt.getText());
            FacturacionColonia = String.valueOf(TextFacturacionColonia.getSelectedItem().toString());
            FacturacionCp = String.valueOf(TextFacturacionCp.getText());
            FacturacionMunicipio = String.valueOf( (TextFacturacionMunicipio.getText()) );
            Suc_DatosFisc = EstadoIDFiscal.get(SpinnerSucursal.getSelectedItemPosition());

            CorreoIgual= false;
            DireccionIgual = false;

        }


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
            request.put("cli_ciudad",TextMunicipio.getText());
            request.put("cli_codigo_postal",TextCp.getText());
            request.put("cli_id_pais",117);
            request.put("cli_id_estado",Estado_id);
            request.put("cli_estado",SpinnerEstado.getSelectedItem().toString());
            request.put("cli_pais","México");
            request.put("cli_correo_electronico_facturacion",FactuacionEmail);
            request.put("cli_calle_facturacion",FacturacionCalle);
            request.put("cli_numero_interior_facturacion",FacturacionNumInt);
            request.put("cli_numero_exterior_facturacion",FacturacionNumExt);
            request.put("cli_colonia_facturacion",FacturacionColonia);
            request.put("cli_ciudad_facturacion",FacturacionMunicipio);
            request.put("cli_codigo_postal_facturacion",FacturacionCp);
            request.put("cli_id_pais_facturacion",117);
            request.put("cli_id_estado_facturacion",Suc_DatosFisc);
            request.put("cli_estado_facturacion",FacturacionMunicipio);
            request.put("cli_pais_facturacion","Mexico");

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
                            EstadoNameFiscal.add(estado);
                            EstadoIDFiscal.add(id);
                        }
                        SpinnerEstado.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,EstadoName));
                        TextFacturacionEstado.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,EstadoNameFiscal));

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

                                if(estado.equals("")){
                                    toast2 =
                                            Toast.makeText(getContext(), "Introduce un código postal válido", Toast.LENGTH_LONG);
                                    toast2.show();
                                    return;
                                }
                                else{
                                    for (int x=0;x<=EstadoName.size();x++)
                                    {
                                        if(estado!=null && estado.equals(EstadoName.get(x)))
                                        {
                                            SpinnerEstado.setSelection(x);
                                            SpinnerEstado.setEnabled(false);
                                            TextMunicipio.setText(response.getString("municipio"));
                                            TextMunicipio.setEnabled(false);
                                            return;
                                        }
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
            TextMunicipio.setEnabled(true);
            SpinnerEstado.setEnabled(true);
            return;
        }
    }

    public void LoadSpinnerFiscalColonias(){


        if(TextFacturacionCp.length()==5){


            final String url = "https://api-codigos-postales.herokuapp.com/v2/codigo_postal/"+TextFacturacionCp.getText().toString();

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
                                    ColoniaNameFiscal.add(colonia);
                                }
                                TextFacturacionColonia.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,ColoniaNameFiscal));
                                estado = response.getString("estado");

                                if(estado.equals("")){
                                    toast2 =
                                            Toast.makeText(getContext(), "Introduce un código postal válido", Toast.LENGTH_LONG);
                                    toast2.show();
                                    return;
                                }
                                else{
                                    for (int x=0;x<=EstadoName.size();x++)
                                    {
                                        if(estado!=null && estado.equals(EstadoName.get(x)))
                                        {
                                            TextFacturacionEstado.setSelection(x);
                                            TextFacturacionEstado.setEnabled(false);
                                            TextFacturacionMunicipio.setText(response.getString("municipio"));
                                            TextFacturacionMunicipio.setEnabled(false);
                                            return;
                                        }
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
            TextFacturacionMunicipio.setEnabled(true);
            TextFacturacionEstado.setEnabled(true);
            return;
        }
    }




}
