package com.Danthop.bionet;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.EventListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_contrasena  extends Activity {
    EditText TextNegocio, TextCodigo;
    ProgressDialog progreso;
    LoginModel Resultado = new LoginModel();
    private SortableInventariosTable tabla_ventas;
    private static final int REQUEST_CODE = 999;
    String Dominio;
    String ID_dispositivo;
    Button Iniciar;
    Button IniciarAdmin;
    private ArrayList<String> ListaDominios;
    private Spinner SpinnerDominios;

    EditText Usuario;
    EditText Codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_contrasena);

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        Usuario = findViewById(R.id.textUsuario);
        Codigo = findViewById(R.id.TextCodigo);
        Iniciar = findViewById(R.id.iniciar_btn);
        IniciarAdmin = findViewById(R.id.iniciar_admin_btn);
        ListaDominios=new ArrayList<>();
        SpinnerDominios=findViewById(R.id.dominios);



        ID_dispositivo = sharedPref.getString("id_dispositivo","");
        loadDominios();

        Intent intent = new Intent(this, MyFirebaseInstanceService.class);
        this.startService(intent);
        VerifyPermisos();

        Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificar_campos();
            }
        });

        IniciarAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_contrasena.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void Login() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        try {
            JSONObject request = new JSONObject();
            try {
                request.put("esApp", 1);
                request.put("usu_usuario", Usuario.getText());
                request.put("id_dispositivo", "\""+ID_dispositivo+"\"");
                request.put("cbn_dominio", SpinnerDominios.getSelectedItem());
                request.put("usu_codigo_acceso", Codigo.getText());
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
                                    finish();

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
                        else
                        {
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
        public void verificar_campos (){

                    if (Usuario.getText().length() == 0) {
                        Toast toast1 = Toast.makeText(getApplicationContext(),
                                "Campo usuario obligatorio ", Toast.LENGTH_SHORT);
                        toast1.show();
                        return;
                    }

                    if (Codigo.getText().length() == 0) {
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

    private void VerifyPermisos()
    {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED)
        {
        }else{
            ActivityCompat.requestPermissions(Login_contrasena.this,
                    permissions,
                    REQUEST_CODE);
        }
    }

    private void loadDominios()
    {
        try{
            JSONObject request = new JSONObject();
            try
            {
                request.put("esApp", "1");
                request.put("dis_mac","\""+ID_dispositivo+"\"");
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


                    try {

                        Resultado.setEstatus( response.getString( "estatus" ) );
                        Resultado.setMensaje( response.getString( "mensaje" ) );

                        int status = Integer.parseInt( Resultado.getEstatus() );

                        if (status == 1)
                        {
                            JSONArray Respuesta = response.getJSONArray("resultado");
                            for(int x = 0; x < Respuesta.length(); x++){
                                String dominio=Respuesta.getString(x);
                                ListaDominios.add(dominio);
                            }
                            SpinnerDominios.setAdapter(new ArrayAdapter<String>(Login_contrasena.this,android.R.layout.simple_spinner_item,ListaDominios));
                        }
                        else{


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
