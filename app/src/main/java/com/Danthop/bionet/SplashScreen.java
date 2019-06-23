package com.Danthop.bionet;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    EditText TextUsuario,TextPassword;
    private ProgressDialog progreso;

    private String ID_dispositivo;
    private String usu_sucursales;

    LoginModel Resultado = new LoginModel();
    private List<LoginModel> IDSucursales;

    private JSONArray jsonArray;
    private static final int REQUEST_CODE = 999;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private String Valor;


    Dialog reestablecer;
    Dialog correo_enviado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_splash_screen);
        progreso = new ProgressDialog(this);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    permissioncheck();

                } else {
                    // Pre-Marshmallow
                    LaunchApp();
                }
            }
        }, 3000);

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        // Set SDK to log events
        Meli.setLoggingEnabled(true);

        // Initialize the MercadoLibre SDK
        Meli.initializeSDK(getApplicationContext());

        Valor = sharedPref.getString("usu_id","0");

        Intent intent = new Intent(this, MyFirebaseInstanceService.class);
        this.startService(intent);


        /*-------------;)----------------------
        String myIMEI = "";
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null){
            myIMEI = mTelephony.getDeviceId();
        }
        //------------:)----------------------*/


    }

    private void VerificarIMEI()
    {
        if (Valor != "0")
        {
            Intent intent2 = new Intent(SplashScreen.this, Home.class);
            startActivity(intent2);
            finish();
        }
        else
        {
            try{
                JSONObject request = new JSONObject();
                try
                {
                    request.put("esApp", "1");
                    request.put("dis_mac",ID_dispositivo+"IMEI");
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
                                SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("id_dispositivo", String.valueOf( ID_dispositivo ) );
                                editor.commit();
                                Intent intent2 = new Intent(SplashScreen.this, Login_contrasena.class);
                                startActivity(intent2);
                                finish();

                            }
                            else{
                                progreso.hide();

                                SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("id_dispositivo", String.valueOf( ID_dispositivo ) );
                                editor.commit();
                                Intent intent2 = new Intent(SplashScreen.this, Login.class);
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

    private void permissioncheck() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("READ");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= 23) {
                                    // Marshmallow+
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);


                                } else {
                                    // Pre-Marshmallow
                                }

                            }
                        });
                return;
            }

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);


            } else {
                // Pre-Marshmallow

            }

            return;
        }else
        {
            LaunchApp();
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {

        Boolean cond;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    //  return false;

                    cond = false;
            }
            //  return true;

            cond = true;


        } else {
            // Pre-Marshmallow
            cond = true;
        }

        return cond;

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 23) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(this, "Permission Needed To Run The App", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<String, Integer>();

            perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);

            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            // Check for ACCESS_FINE_LOCATION
            if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                LaunchApp();

            } else {

                Toast.makeText(SplashScreen.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        finish();
                    }
                }, 3000);
            }

        }
    }

    public void LaunchApp()
    {
        ProgressDialog progreso2 = new ProgressDialog(this);
        progreso2.setMessage("Verificando Dispositivo...");
        progreso2.show();
        Thread background = new Thread() {
            public void run() {

                try {
                    progreso2.dismiss();
                    TelephonyManager telephonyManager;
                    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    ID_dispositivo = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    System.out.println(ID_dispositivo);
                    VerificarIMEI();

                } catch (Exception e) {

                }
            }
        };

        background.start();


    }



}