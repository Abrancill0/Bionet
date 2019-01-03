package com.example.jl.bionet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.android.volley.Request.*;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewResult;

    ProgressDialog progreso;

    RequestQueue request;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }




    private void Login(){

        progreso = new ProgressDialog(this);
        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_usuario", "abrx23@gmail.com");
            request.put("usu_contrasena", "123");

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
                progreso.hide();

                LoginModel Resultado = new LoginModel();

                JSONObject Respuesta = null;

                try {
                    Respuesta = response.getJSONObject("resultado");
                    
                    Resultado.setUsuNombre(Respuesta.getString("usu_nombre"));
                    Resultado.getUsuId(Respuesta.getString("usu_id"));

                } catch (JSONException e) {
                    e.printStackTrace();

                    progreso.hide();
                }

                     Toast toast1 =
                             Toast.makeText(getApplicationContext(),
                                    "Bienvenido " + Resultado.getUsuNombre(), Toast.LENGTH_SHORT);

                     toast1.show();
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

        queue.add(postRequest);


    }

    public void CrearCuenta(View view) {
        Intent intent = new Intent(MainActivity.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    public void IniciarSesion(View view){
        Login();
    }

}
