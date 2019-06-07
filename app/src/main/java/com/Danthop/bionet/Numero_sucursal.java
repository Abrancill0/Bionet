package com.Danthop.bionet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.SucursalAdapter;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableSucursalTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.SucursalModel;
import com.Danthop.bionet.model.SucursalesModel;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Numero_sucursal extends Activity {

    private EditText nombre_sucursal;
    private EditText telefono_sucursal;
    private EditText correo_sucursal;
    private EditText direccion_sucursal;
    private EditText rfc;
    private EditText razon_social;
    private String IDUsuario;
    private ProgressDialog progreso;

    private EditText TextCp;
    private EditText TextMunicipio;
    private EditText TextCalle;
    private EditText TextNumExt;
    private EditText TextNumInterior;

    private Spinner SpinnerEstado;
    private Spinner SpinnerColonia;

    private int Estado_id;
    private String Sucursal_id;

    private String estado;
    private Toast toast2;

    private ArrayList<String> EstadoName;
    private ArrayList<Integer> EstadoID;
    private ArrayList<String> ColoniaName;

    private Dialog crear_sucursal_dialog;

    private List<SucursalModel> sucursales;



    private int NumeroSucursales;

    String[][] sucursalModel;

    SortableSucursalTable tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_sucursales);
        findViewById(R.id.numSucursalesLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        EstadoName=new ArrayList<>();
        EstadoID = new ArrayList<>();
        ColoniaName = new ArrayList<>();

        crear_sucursal_dialog=new Dialog(Numero_sucursal.this);
        crear_sucursal_dialog.setContentView(R.layout.pop_up_crear_sucursal);

        sucursales = new ArrayList<>();



        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");

        tb = findViewById(R.id.tabla2);

        tb.setEmptyDataIndicatorView(findViewById(R.id.Tabla_vacia));





    }

    public void Home(View view){

        if(NumeroSucursales!=0){
            Intent intent=new Intent(Numero_sucursal.this,Home.class);
            startActivity(intent);
        }
        else{
            Toast toast1=
                    Toast.makeText(getApplicationContext(),"Debe crear por lo menos una sucursal",Toast.LENGTH_LONG);

            toast1.show();
        }
    }


    private void GuardarDatos(){

        progreso = new ProgressDialog(this);
        progreso.setMessage("Procesando...");
        progreso.show();


        JSONObject request = new JSONObject();
        try
        {
            request.put("suc_nombre", nombre_sucursal.getText());
            request.put("suc_id_emisor", "");
            request.put("suc_telefono", telefono_sucursal.getText());
            request.put("suc_correo_electronico", correo_sucursal.getText());
            request.put("con_propinas", "false");
            request.put("suc_calle", TextCalle.getText());
            request.put("suc_numero_interior", TextNumInterior);
            request.put("suc_numero_exterior", TextNumExt);
            request.put("suc_colonia", SpinnerColonia.getSelectedItem());
            request.put("suc_ciudad", TextMunicipio.getText());
            request.put("suc_codigo_postal", TextCp);
            request.put("suc_id_pais", "117");
            request.put("suc_estado", SpinnerEstado.getSelectedItem());
            request.put("suc_id_estado", "0");
            request.put("suc_pais", "Mexico");
            request.put("usu_id", IDUsuario);
            request.put("esApp", "1");
            request.put("con_propinas", "false");
            request.put("suc_principal", "false");
            request.put("suc_razon_social", razon_social);
            request.put("suc_rfc", rfc);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/configuracion/sucursales/store";

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

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                        Muestra_sucursales();

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();

                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);

    }

    public void crear_sucursal (View v){
        crear_sucursal_dialog.show();

        Button crear = (Button) crear_sucursal_dialog.findViewById(R.id.btn_crear_sucursal);

        nombre_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_nombre_sucursal);
        telefono_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_telefono_sucursal);
        correo_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_correo_sucursal);
        rfc = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_rfc);
        razon_social = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_razon_social);
        TextCp = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_sucursal_cp);
        TextMunicipio = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_sucursal_municipio);
        TextCalle = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_sucursal_calle);
        TextNumExt = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_sucursal_num_ext);
        TextNumInterior= (EditText) crear_sucursal_dialog.findViewById(R.id.Text_sucursal_num_int);
        SpinnerColonia= (Spinner) crear_sucursal_dialog.findViewById(R.id.Text_sucursal_colonia);
        SpinnerEstado= (Spinner) crear_sucursal_dialog.findViewById(R.id.Text_sucursal_estado);
        TextCp = crear_sucursal_dialog.findViewById(R.id.Text_sucursal_cp);
        LoadSpinnerEstado();
        TextCp.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                ColoniaName.clear();
                LoadSpinnerColonias();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valida_datos(crear_sucursal_dialog);
            }
        });
    }

    public void valida_datos(Dialog dialog){
        if(nombre_sucursal.getText().length()==0||telefono_sucursal.getText().length()==0||correo_sucursal.getText().length()==0||razon_social.getText().length()==0||rfc.getText().length()==0
                ||TextCp.getText().length()==0||TextMunicipio.getText().length()==0||TextCalle.getText().length()==0||TextNumExt.getText().length()==0||TextNumInterior.getText().length()==0)
        {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Campos obligatorios ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        String email = String.valueOf(correo_sucursal.getText());

        Matcher mather = pattern.matcher(email);

        if (mather.find() == false) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "El email ingresado es inválido.", Toast.LENGTH_SHORT);

            toast1.show();
            return;
        }

        GuardarDatos();
        dialog.dismiss();
    }

    private void Muestra_sucursales()
    {
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", IDUsuario);
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
                JSONObject RespuestaNodoDireccion= null;
                JSONObject ElementoTelefono=null;
                JSONObject ElementoCorreo=null;
                JSONArray RespuestaNodoSucursal= null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {
                        String nombre;
                        String telefono;
                        String correo_electronico;
                        String calle;

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoSucursal = Respuesta.getJSONArray("aSucursales");

                        sucursalModel = new String[RespuestaNodoSucursal.length()][4];

                        for(int x = 0; x < RespuestaNodoSucursal.length(); x++){
                            NumeroSucursales=x+1;
                            JSONObject elemento = RespuestaNodoSucursal.getJSONObject(x);

                            nombre = elemento.getString("suc_nombre");

                            telefono = elemento.getString("suc_telefono");

                            ElementoCorreo = elemento.getJSONObject("suc_correo_electronico");
                            correo_electronico = ElementoCorreo.getJSONArray("values").getString( 0);

                            RespuestaNodoDireccion = elemento.getJSONObject("suc_direccion");
                            calle = RespuestaNodoDireccion.getString("suc_calle");

                            //sucursalModel[x][0] = String.valueOf(x);
                            sucursalModel[x][0] =nombre;
                            sucursalModel[x][1] =telefono;
                            sucursalModel[x][2] =correo_electronico;
                            sucursalModel[x][3] =calle;

                            final SucursalModel sucursal = new SucursalModel(nombre, telefono, correo_electronico,calle);
                            sucursales.add(sucursal);

                        }

                        final SucursalAdapter sucursalAdapter = new SucursalAdapter(Numero_sucursal.this, sucursales, tb);
                        tb.setDataAdapter(sucursalAdapter);

                        progreso.hide();

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                        progreso.hide();

                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();

                    progreso.hide();

                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();

                        progreso.hide();

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);


    }
    private void LoadSpinnerEstado(){

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", IDUsuario);
            request.put("esApp", "1");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/configuracion/sucursales/select_estados";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>() {
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
                        SpinnerEstado.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,EstadoName));

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();


                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();


                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();


                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(postRequest);


    }

    public void LoadSpinnerColonias() {


        if (TextCp.length() == 5) {


            final String url = "https://api-codigos-postales.herokuapp.com/v2/codigo_postal/" + TextCp.getText().toString();

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            JSONArray RespuestaNodoColonias = null;

                            try {
                                RespuestaNodoColonias = response.getJSONArray("colonias");

                                //Aqui llenar el spiner con el respuesta nodo
                                for (int x = 0; x < RespuestaNodoColonias.length(); x++) {
                                    //Aqui llenas un arreglo para el adapter del spiner
                                    String colonia = RespuestaNodoColonias.getString(x);
                                    ColoniaName.add(colonia);
                                }
                                SpinnerColonia.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, ColoniaName));
                                estado = response.getString("estado");

                                if (estado.equals("")) {
                                    toast2 =
                                            Toast.makeText(getApplicationContext(), "Introduce un código postal válido", Toast.LENGTH_LONG);
                                    toast2.show();
                                    return;
                                } else {
                                    for (int x = 0; x <= EstadoName.size(); x++) {
                                        if (estado != null && estado.equals(EstadoName.get(x))) {
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
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", String.valueOf(error));
                        }
                    }
            );

            // add it to the RequestQueue
            VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(getRequest);

        } else {
            TextMunicipio.setEnabled(true);
            SpinnerEstado.setEnabled(true);
            return;
        }
    }
}
