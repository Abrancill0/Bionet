package com.example.jl.bionet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SharedMemory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jl.bionet.model.LoginModel;
import com.example.jl.bionet.model.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.android.volley.Request.*;

public class MainActivity extends AppCompatActivity {


     EditText TextUsuario,TextPassword;

     ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;
        SharedPreferences sharprefs = getSharedPreferences("DatosPersistentes", context.MODE_PRIVATE);

        TextUsuario = (EditText)findViewById(R.id.TextUsuario);
        TextPassword = (EditText)findViewById(R.id.TextPassword);

        SharedPreferences sharedPref = getPreferences(context.MODE_PRIVATE);

        String Valor = sharedPref.getString("usu_id","0");

        if (Valor != "0")
        {
            Intent intent = new Intent(MainActivity.this, Home.class);
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
            request.put("usu_usuario", TextUsuario.getText());
            request.put("usu_contrasena", TextPassword.getText());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = "https://citycenter-rosario.com.ar/usuarios/loginApp";

        JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, url,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);

                progreso.hide();

                LoginModel Resultado = new LoginModel();

                JSONObject Respuesta = null;

                try {

                    Respuesta = response.getJSONObject("resultado");
                    
                    Resultado.setUsuNombre(Respuesta.getString("usu_nombre"));
                    Resultado.setUsuId(Respuesta.getString("usu_id"));

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Bienvenido " + Resultado.getUsuNombre(), Toast.LENGTH_SHORT);

                    toast1.show();

                    SharedPreferences sharepref = getPreferences(getBaseContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor =  sharepref.edit();
                    editor.putString("usu_nombre", Resultado.getUsuNombre());
                    editor.putString("usu_id", Resultado.getUsuId());

                    editor.commit();


                } catch (JSONException e) {
                    progreso.hide();
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
        Intent intent = new Intent(MainActivity.this, CrearCuentaActivity.class);
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

}
