package com.Danthop.bionet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.model.LoginModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Reestablecer_contrasena extends Activity {

    ProgressDialog progreso;
    EditText NewPassword;
    EditText NewRePassword;
    LoginModel Resultado = new LoginModel();
    private String correo;
    String newPassword;
    String newRePassword;
    EditText Password_temp;
    String contrasena_temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restablece_contrasenia);
        findViewById(R.id.restableceContrasenaLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        NewPassword = (EditText) findViewById(R.id.new_password);
        NewRePassword = (EditText) findViewById(R.id.new_repassword);
        Password_temp = findViewById(R.id.contrasena_temp);
        Bundle datos = this.getIntent().getExtras();
        correo =  "" + datos.get("ParametroCorreo");
    }

    public void Aceptar_cerrar_ventana(Dialog dialog){
        dialog.dismiss();
    }


    public void Reestablecer_contrasenia(View v) {

        newPassword = String.valueOf(NewPassword.getText());
        newRePassword = String.valueOf(NewRePassword.getText());
        contrasena_temp = String.valueOf(Password_temp.getText());


        if(NewPassword.getText().length()==0 || NewRePassword.getText().length()==0 || Password_temp.getText().length()==0   ) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Campo contrasena obligatorio ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }
        if (newPassword.equals(newRePassword))
        {
            progreso = new ProgressDialog(this);
            progreso.setMessage("Procesando...");
            progreso.show();
                JSONObject request = new JSONObject();
                try {
                    request.put("usu_correo_electronico", correo);
                    request.put("usu_contrasenia", NewPassword.getText());
                    request.put("usu_contrasenia_ant", Password_temp.getText());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

                String ApiPath = url + "/api/login/cambiar_contrasena";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject Respuesta = null;
                        JSONObject RespuestaNodoUsuID = null;

                        try {

                            String status=(response.getString("estatus"));
                            String mensaje=(response.getString("mensaje"));

                            int estatus = Integer.parseInt(status);

                            if (estatus == 1) {

                                Respuesta = response.getJSONObject("resultado");
                                Toast toast1 =
                                        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);

                                toast1.show();

                                progreso.hide();
                                final Dialog dialog=new Dialog(Reestablecer_contrasena.this);
                                dialog.setContentView(R.layout.pop_up_confirmacion_contrasena);
                                dialog.show();
                                Button cerrar_ventana = (Button) dialog.findViewById(R.id.aceptar_cerrar_ventana1);
                                cerrar_ventana.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Aceptar_cerrar_ventana(dialog);
                                        Intent intent = new Intent(Reestablecer_contrasena.this, Login.class);
                                        startActivity(intent);
                                    }
                                });


                            } else {
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
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error

                                progreso.hide();

                                Toast toast1 =
                                        Toast.makeText(getApplicationContext(),
                                                error.toString(), Toast.LENGTH_SHORT);

                                toast1.show();
                            }
                        }
                );

                VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);
        }else{
            Toast toast5 =
                    Toast.makeText(getApplicationContext(),
                            "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
            toast5.show();
        }
    }
}
