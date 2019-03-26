package com.Danthop.bionet;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.Class.MyFirebaseInstanceService;
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
    EditText TextCuenta, TextPassword;
    ProgressDialog progreso;
    LoginContrasenaModel Resultado = new LoginContrasenaModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_contrasena);

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        TextCuenta = (EditText) findViewById(R.id.TextCuenta);
        TextPassword = (EditText) findViewById(R.id.TextPassword);

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
                request.put("usu_correo_electronico", TextCuenta.getText());
                request.put("usu_contrasenia", TextPassword.getText());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            String url = getString(R.string.Url);
            String ApiPath = url + "/api/login/login-codigo";


            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Resultado = new LoginContrasenaModel();

                    JSONArray Respuesta = null;
                    JSONObject RespuestaObjeto = null;
                    JSONObject RespuestaNodoUsuID = null;

                    try {

                        Resultado.setestatus(response.getString("estatus"));
                        Resultado.setmensaje(response.getString("mensaje"));

                        int status = Integer.parseInt(Resultado.getestatus());

                        if (status == 1) {
                            Respuesta = response.getJSONArray("resultado");
                            RespuestaObjeto = Respuesta.getJSONObject(0);

                            Resultado.setusu_tipo_contrasenia(RespuestaObjeto.getString("usu_tipo_contrasenia"));
                            String recuperar_contrasena = Resultado.getusu_tipo_contrasenia();

                          /*  if (recuperar_contrasena == "false") {
                                Intent intent = new Intent(Login_contrasena.this, Reestablecer_contrasena.class);
                                intent.putExtra("ParametroCorreo", TextCuenta.getText());
                                startActivity(intent);
                            } else {
                                Resultado.setusu_nombre(RespuestaObjeto.getString("usu_nombre"));
                                Resultado.setapellidos(RespuestaObjeto.getString("usu_apellido_paterno") + " " + RespuestaObjeto.getString("usu_apellido_materno"));
                                Resultado.setusu_correo_electronico(RespuestaObjeto.getString("usu_correo_electronico"));
                                Resultado.setusu_imagen_perfil(RespuestaObjeto.getString("usu_imagen_perfil"));
                                Resultado.setusu_activo(RespuestaObjeto.getString("usu_activo"));
                                Resultado.setusu_administrador(RespuestaObjeto.getString("usu_administrador"));

                                JSONObject tipo_id = Respuesta.getJSONObject(0);
                                Resultado.setusu_tipo_contrasenia(tipo_id.getString("usu_tipo_contrasenia"));


                                if (recuperar_contrasena == "false") {
                                    Intent intent = new Intent(Login_contrasena.this, Reestablecer_contrasena.class);
                                    intent.putExtra("ParametroCorreo", TextCuenta.getText());
                                    startActivity(intent);
                                } else {
                                    Resultado.setusu_nombre(tipo_id.getString("usu_nombre"));
                                    Resultado.setapellidos(tipo_id.getString("usu_apellido_paterno") + " " + tipo_id.getString("usu_apellido_materno"));
                                    Resultado.setusu_correo_electronico(tipo_id.getString("usu_correo_electronico"));
                                    Resultado.setusu_imagen_perfil(tipo_id.getString("usu_imagen_perfil"));
                                    Resultado.setusu_activo(tipo_id.getString("usu_activo"));
                                    Resultado.setusu_administrador(tipo_id.getString("usu_administrador"));

                                    RespuestaNodoUsuID = RespuestaObjeto.getJSONObject("usu_id");
                                    Resultado.setusu_id(RespuestaNodoUsuID.getString("uuid"));

                                    RespuestaNodoUsuID = tipo_id.getJSONObject("usu_id");
                                    Resultado.setusu_id(RespuestaNodoUsuID.getString("uuid"));
                                    }*/

                        } else {
                                    new GuardaPreferencia().execute();
                                    Intent intent = new Intent(Login_contrasena.this, Home.class);
                                    startActivity(intent);

                                    Toast toast1 =
                                            Toast.makeText(getApplicationContext(),
                                                    "Bienvenido " + Resultado.getusu_nombre(), Toast.LENGTH_LONG);
                                    toast1.show();
                                    progreso.hide();

                            progreso.hide();

                            Toast toast2 = Toast.makeText( getApplicationContext(),
                                    Resultado.getmensaje(), Toast.LENGTH_LONG );
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

            if (TextCuenta.getText().length() == 0) {
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Campo cuenta obligatorio ", Toast.LENGTH_SHORT);
                toast1.show();
                return;
            }
            Pattern pattern = Pattern
                    .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

            String cuenta = String.valueOf(TextCuenta.getText());
            Matcher mather = pattern.matcher(cuenta);

            if (mather.find() == false) {
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "La cuenta ingresada es inválida.", Toast.LENGTH_SHORT);
                toast1.show();
                return;
            }

            if (TextPassword.getText().length() == 0) {
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
            editor.putString("usu_nombre", Resultado.getusu_nombre());
            editor.putString("usu_id", Resultado.getusu_id());
            editor.putString("usu_apellidos", Resultado.getapellidos());
            editor.putString("usu_correo_electronico", Resultado.getusu_correo_electronico());
            editor.putString("usu_imagen_perfil", "http://187.189.192.150:8010"+Resultado.getusu_imagen_perfil());
            editor.putString("usu_activo", Resultado.getusu_activo());
            editor.putString("usu_administrador", Resultado.getusu_administrador());

            editor.commit();
            return null;
        }
    }
    public void Aceptar_cerrar_ventana(Dialog dialog){
        dialog.dismiss();
    }

    public void forgotPassword(View v){
        final Dialog dialog=new Dialog(Login_contrasena.this);
        dialog.setContentView(R.layout.pop_up_olvide_contrasenia);
        dialog.show();

        Button enviar_correo = (Button) dialog.findViewById(R.id.enviar_correo_contrasena);
        enviar_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText correo_contrasena_olvidada = (EditText) dialog.findViewById(R.id.correo_contrasena_olvidada);
                if (correo_contrasena_olvidada.getText().length() == 0) {
                    Toast toast1 = Toast.makeText(getApplicationContext(),
                            "Campo usuario obligatorio ", Toast.LENGTH_SHORT);

                    toast1.show();
                    return;
                }
                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                String cuenta = String.valueOf(correo_contrasena_olvidada.getText());
                Matcher mather = pattern.matcher(cuenta);

                if (mather.find() == false) {
                    Toast toast1 = Toast.makeText(getApplicationContext(),
                            "La cuenta ingresada es inválida.", Toast.LENGTH_SHORT);

                    toast1.show();
                    return;
                }else {
                    progreso = new ProgressDialog(dialog.getContext());
                    progreso.setMessage("Enviando cuenta...");
                    progreso.show();
                    JSONObject request = new JSONObject();
                    try {
                        request.put("usu_correo_electronico", correo_contrasena_olvidada.getText());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}
