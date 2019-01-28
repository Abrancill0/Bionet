package com.Danthop.bionet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.model.LoginModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Reestablecer_contrasena extends Activity {

    ProgressDialog progreso;
    LoginModel Resultado = new LoginModel();
    EditText NewPassword = (EditText) findViewById(R.id.new_password);
    EditText NewRePassword = (EditText) findViewById(R.id.new_repassword);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restablece_contrasenia);
    }


    private void Login(){

        progreso = new ProgressDialog(this);
        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_correo_electronico", Resultado.getUsu_activo());
            request.put("usu_contrasenia", NewPassword.getText());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

        String ApiPath = url + "/api/login/actualiza-contrasena";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
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
