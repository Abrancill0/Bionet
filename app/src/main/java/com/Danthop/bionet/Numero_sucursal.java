package com.Danthop.bionet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.Danthop.bionet.R;
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
    private static final String[] TABLA1_HEADERS = { "Nombre de la sucursal", "Teléfono", "Correo", "Dirección" };
    private String IDUsuario;
    private ProgressDialog progreso;

    private int NumeroSucursales;

    String[][] sucursalModel;

    TableView tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_sucursales);

        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");

         tb = (TableView) findViewById(R.id.tabla2);

        tb.setHeaderBackgroundColor(getResources().getColor(R.color.white));

        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLA1_HEADERS));
    }

    public void eleccion_premium(View view) {

        if(NumeroSucursales!=0){
            Intent intent = new Intent(Numero_sucursal.this, EleccionPremium.class);
            startActivity(intent);
        }
       else {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(), "Debe crear por lo menos una sucursal", Toast.LENGTH_LONG);

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
            request.put("suc_calle", direccion_sucursal.getText());
            request.put("suc_numero_interior", "");
            request.put("suc_numero_exterior", "");
            request.put("suc_colonia", "");
            request.put("suc_ciudad", "");
            request.put("suc_codigo_postal", "");
            request.put("suc_id_pais", "117");
            request.put("suc_estado", "");
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
        final Dialog crear_sucursal_dialog=new Dialog(Numero_sucursal.this);

        crear_sucursal_dialog.setContentView(R.layout.pop_up_crear_sucursal);
        crear_sucursal_dialog.show();

        Button crear = (Button) crear_sucursal_dialog.findViewById(R.id.btn_crear_sucursal);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombre_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_nombre_sucursal);
                telefono_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_telefono_sucursal);
                correo_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_correo_sucursal);
                direccion_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_direccion_sucursal);
                rfc = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_rfc);
                razon_social = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_razon_social);
                valida_datos(crear_sucursal_dialog);
            }
        });
    }

    public void valida_datos(Dialog dialog){
        if(nombre_sucursal.getText().length()==0||telefono_sucursal.getText().length()==0||correo_sucursal.getText().length()==0||direccion_sucursal.getText().length()==0||razon_social.getText().length()==0||rfc.getText().length()==0) {
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

                            ElementoTelefono = elemento.getJSONObject("suc_telefono");
                            telefono = ElementoTelefono.getString("value");

                            ElementoCorreo = elemento.getJSONObject("suc_correo_electronico");
                            correo_electronico = ElementoCorreo.getJSONArray("values").getString( 0);

                            RespuestaNodoDireccion = elemento.getJSONObject("suc_direccion");
                            calle = RespuestaNodoDireccion.getString("suc_calle");

                            //sucursalModel[x][0] = String.valueOf(x);
                            sucursalModel[x][0] =nombre;
                            sucursalModel[x][1] =telefono;
                            sucursalModel[x][2] =correo_electronico;
                            sucursalModel[x][3] =calle;

                        }

                        tb.setDataAdapter(new SimpleTableDataAdapter(getApplicationContext(), sucursalModel));

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
}
