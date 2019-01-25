package com.Danthop.bionet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.model.LoginModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.Danthop.bionet.R;


import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method;

public class Login extends AppCompatActivity {

     EditText TextUsuario,TextPassword;
     EditText correo_contrasena_olvidada;

     ProgressDialog progreso;

    LoginModel Resultado = new LoginModel();

    Dialog reestablecer;
    Dialog correo_enviado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        reestablecer = new Dialog(this);
        correo_enviado = new Dialog(this);

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        TextUsuario = (EditText)findViewById(R.id.TextUsuario);
        TextPassword = (EditText)findViewById(R.id.TextPassword);
        correo_contrasena_olvidada = (EditText)findViewById(R.id.correo_contrasena_olvidada);

        String Valor = sharedPref.getString("usu_id","0");

        if (Valor != "0")
        {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
        }


    }

    private void Login(){

        progreso = new ProgressDialog(this);
        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_correo_electronico", TextUsuario.getText());
            request.put("usu_contrasenia", TextPassword.getText());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

        String ApiPath = url + "/api/login/login";

        JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                Resultado = new LoginModel();

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoUsuID = null;

                try {

                    Resultado.setEstatus(response.getString("estatus"));
                    Resultado.setMensaje(response.getString("mensaje"));

                    int status = Integer.parseInt(Resultado.getEstatus());

                    if (status == 1)
                    {

                    Respuesta = response.getJSONObject("resultado");

                    Resultado.setUsuNombre(Respuesta.getString("usu_nombre"));
                    Resultado.setUsuApellidos(Respuesta.getString("usu_apellido_paterno") + " " + Respuesta.getString("usu_apellido_materno"));
                    Resultado.setUsuEmail(Respuesta.getString("usu_correo_electronico"));
                    Resultado.setUsuImagen(Respuesta.getString("usu_imagen_perfil"));
                    Resultado.setUsu_activo(Respuesta.getString("usu_activo"));
                    Resultado.setUsu_administrador(Respuesta.getString("usu_administrador"));


                    RespuestaNodoUsuID = Respuesta.getJSONObject("usu_id");
                    Resultado.setUsuId(RespuestaNodoUsuID.getString("uuid"));

                    new GuardaPreferencia().execute();

                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);


                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Bienvenido " + Resultado.getUsuNombre(), Toast.LENGTH_LONG);

                    toast1.show();


                        progreso.hide();

                    }
                    else
                    {
                        progreso.hide();

                        Toast toast2 = Toast.makeText(getApplicationContext(),
                                Resultado.getMensaje(), Toast.LENGTH_LONG);

                        toast2.show();

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

    public void CrearCuenta(View view) {
        Intent intent = new Intent(Login.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    public void IniciarSesion(View view){

        if(TextUsuario.getText().length()==0) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Campo usuario obligatorio ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }


        if(TextPassword.getText().length()==0) {
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

                SharedPreferences.Editor editor =  sharedPref.edit();
                editor.putString("usu_nombre", Resultado.getUsuNombre());
                editor.putString("usu_id", Resultado.getUsuId());
                editor.putString("usu_apellidos", Resultado.getUsuApellidos());
                editor.putString("usu_correo_electronico", Resultado.getUsuEmail());
                editor.putString("usu_imagen_perfil", Resultado.getUsuImagen());
                editor.putString("usu_activo", Resultado.getUsu_activo());
                editor.putString("usu_administrador", Resultado.getUsu_administrador());

                editor.commit();

            return null;
        }
    }


    public void reestablecerContra(View v){

        reestablecer.setContentView(R.layout.pop_up_olvide_contrasenia);
        reestablecer.show();
    }

    public void forgotPassword(View v){

        progreso = new ProgressDialog(this);
        progreso.setMessage("Enviando correo...");
        progreso.show();

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_correo_electronico", correo_contrasena_olvidada.getText());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

        String ApiPath = url + "/api/login/recuperar-contrasena";

        JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoUsuID = null;

                try {

                    String status = response.getString("estatus");
                    String mensaje = response.getString("mensaje");

                    int estatus = Integer.parseInt(status);

                    if (estatus == 1)
                    {
                        correo_enviado.setContentView(R.layout.pop_up_olvide_contrasenia);
                        correo_enviado.show();

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);

                        toast1.show();


                        progreso.hide();

                    }
                    else
                    {
                        progreso.hide();

                        Toast toast2 = Toast.makeText(getApplicationContext(),
                                Resultado.getMensaje(), Toast.LENGTH_LONG);

                        toast2.show();

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




}
