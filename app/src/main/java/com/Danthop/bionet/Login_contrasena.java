package com.Danthop.bionet;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.Class.MyFirebaseInstanceService;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.LoginContrasenaModel;
import com.Danthop.bionet.model.LoginModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EventListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_contrasena  extends Activity {
    EditText TextNegocio, TextCodigo;
    ProgressDialog progreso;
    LoginModel Resultado = new LoginModel();
    private SortableInventariosTable tabla_ventas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_contrasena);

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        TextNegocio = (EditText) findViewById(R.id.TextNegocio);
        TextCodigo = (EditText) findViewById(R.id.TextCodigo);
        String Valor = sharedPref.getString("usu_id", "0");

        Intent intent = new Intent(this, MyFirebaseInstanceService.class);
        this.startService(intent);


        if (Valor != "0") {
            Intent intent2 = new Intent(Login_contrasena.this, Home.class);
            startActivity(intent2);
        }
    }

    private void Login() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        try {
            JSONObject request = new JSONObject();
            try {
                request.put("cbn_nombre_negocio", TextNegocio.getText());
                request.put("usu_codigo_acceso", TextCodigo.getText());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            String url = getString(R.string.Url);
            String ApiPath = url + "/api/login/login-codigo";

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Resultado = new LoginModel();
                    JSONArray Respuesta = null;
                    JSONObject RespuestaObjeto = null;
                    JSONObject RespuestaNodoUsuID = null;

                    try {
                        Resultado.setEstatus(response.getString("estatus"));
                        Resultado.setMensaje(response.getString("mensaje"));

                        int status = Integer.parseInt(Resultado.getEstatus());

                        if (status == 1) {
                            Respuesta = response.getJSONArray("resultado");
                            RespuestaObjeto = Respuesta.getJSONObject(0);

                           // Resultado.setusu_codigo_acceso(RespuestaObjeto.getString("usu_codigo_acceso"));
                            Resultado.setUsuNombre( RespuestaObjeto.getString( "usu_nombre" ) );
                            Resultado.setUsuApellidos( RespuestaObjeto.getString( "usu_apellido_paterno" ) + " " + RespuestaObjeto.getString( "usu_apellido_materno" ) );
                            Resultado.setUsuEmail( RespuestaObjeto.getString( "usu_correo_electronico" ) );
                            Resultado.setUsuImagen( RespuestaObjeto.getString( "usu_imagen_perfil" ) );
                            Resultado.setUsu_activo( RespuestaObjeto.getString( "usu_activo" ) );
                            Resultado.setUsu_administrador( RespuestaObjeto.getString( "usu_administrador" ) );

                            RespuestaNodoUsuID = RespuestaObjeto.getJSONObject( "usu_id" );
                            Resultado.setUsuId( RespuestaNodoUsuID.getString( "uuid" ) );

                            JSONObject tipo_id = Respuesta.getJSONObject( 0 );
                            RespuestaNodoUsuID = tipo_id.getJSONObject( "usu_id" );
                            Resultado.setUsuId( RespuestaNodoUsuID.getString( "uuid" ) );

                            new GuardaPreferencia().execute();
                                    Intent intent = new Intent(Login_contrasena.this, Home.class);
                                    startActivity(intent);

                                    Toast toast1 =
                                            Toast.makeText(getApplicationContext(),
                                                    "Bienvenido " + Resultado.getUsuNombre() + " "  + Resultado.getUsuApellidos(), Toast.LENGTH_LONG);
                                    toast1.show();
                                    progreso.hide();

                            progreso.hide();

                            Toast toast2 = Toast.makeText( getApplicationContext(),
                                    Resultado.getMensaje(), Toast.LENGTH_LONG );
                            toast2.show();
                        }
                    }catch (JSONException e) {
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
//Metodos...
        public void IniciarSesion2 (View view){

                    if (TextNegocio.getText().length() == 0) {
                        Toast toast1 = Toast.makeText(getApplicationContext(),
                                "Campo cuenta obligatorio ", Toast.LENGTH_SHORT);
                        toast1.show();
                        return;
                    }

                    if (TextCodigo.getText().length() == 0) {
                        Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Campo password obligatorio ", Toast.LENGTH_SHORT);
                toast1.show();
                return;
            }
            Login();
        }

    private class GuardaPreferencia extends AsyncTask<Void,String,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("usu_nombre", Resultado.getUsuNombre());
            editor.putString("usu_id", Resultado.getUsuId());
            editor.putString("usu_apellidos", Resultado.getUsuApellidos());
            editor.putString("cbn_nombre_negocio", Resultado.getcbn_nombre_negocio());
            editor.putString("usu_imagen_perfil", "http://187.189.192.150:8010"+Resultado.getUsuImagen());
            editor.putString("usu_activo", Resultado.getUsu_activo());
            editor.putString("usu_administrador", Resultado.getUsu_administrador());

            editor.commit();
            return null;
        }
    }
}
