package com.Danthop.bionet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.Class.MyFirebaseInstanceService;
import com.Danthop.bionet.model.LoginModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.mercadolibre.android.sdk.Meli;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NavigatorActivity extends Activity {

    EditText TextUsuario,TextPassword;
    ProgressDialog progreso;

    private String macAddress;
    private String usu_sucursales;

    LoginModel Resultado = new LoginModel();
    private List<LoginModel> IDSucursales;

    private JSONArray jsonArray;
    private static final int REQUEST_CODE = 999;

    Dialog reestablecer;
    Dialog correo_enviado;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progreso = new ProgressDialog(this);
        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        // Set SDK to log events
        Meli.setLoggingEnabled(true);

        // Initialize the MercadoLibre SDK
        Meli.initializeSDK(getApplicationContext());

        String Valor = sharedPref.getString("usu_id","0");

        Intent intent = new Intent(this, MyFirebaseInstanceService.class);
        this.startService(intent);


        //-------------;)----------------------
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();
        System.out.println(macAddress);
        //------------:)----------------------

        if (Valor != "0")
        {
            Intent intent2 = new Intent(NavigatorActivity.this, Home.class);
            startActivity(intent2);
            finish();
        }
        else
            {
                VerificarMac();
        }

    }

    private void VerificarMac()
    {
        try{
            JSONObject request = new JSONObject();
            try
            {
                request.put("esApp", "1");
                request.put("dis_mac",macAddress);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

            String ApiPath = url + "/api/obtener-dominio-mac";

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response) {

                    Resultado = new LoginModel();

                    JSONArray Respuesta = null;
                    JSONObject RespuestaObjeto = null;
                    JSONObject RespuestaNodoUsuID = null;
                    JSONObject RespuestaIdSucursales = null;
                    JSONArray ValorIdSucursales = null;
                    try {

                        Resultado.setEstatus( response.getString( "estatus" ) );
                        Resultado.setMensaje( response.getString( "mensaje" ) );

                        int status = Integer.parseInt( Resultado.getEstatus() );

                        if (status == 1)
                        {
                            Intent intent2 = new Intent(NavigatorActivity.this, Login_contrasena.class);
                            startActivity(intent2);
                            finish();

                        }
                        else{
                            progreso.hide();

                            Intent intent2 = new Intent(NavigatorActivity.this, Login.class);
                            startActivity(intent2);
                            finish();

                        }

                    } catch (JSONException e) {
                        progreso.hide();

                        Toast toast1 = Toast.makeText(getApplicationContext(),
                                "Error al conectarse al servidor", Toast.LENGTH_LONG);

                        toast1.show();
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
                                    Toast.makeText(getApplicationContext(),
                                            "Error de conexion", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }
            );
            VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}