package com.Danthop.bionet;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.Class.MyFirebaseInstanceService;
import com.Danthop.bionet.model.LoginModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.mercadolibre.android.sdk.Meli;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.android.volley.Request.Method;

public class Login extends Activity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        //Asi puedes verificar la dirección mac del dispositivo
        //-------------;)----------------------
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();
        System.out.println(macAddress);
        //------------:)----------------------

        TextUsuario = (EditText)findViewById(R.id.TextUsuario);
        TextPassword = (EditText)findViewById(R.id.TextPassword);
        IDSucursales = new ArrayList<>();

        // Set SDK to log events
        Meli.setLoggingEnabled(true);

        // Initialize the MercadoLibre SDK
        Meli.initializeSDK(getApplicationContext());

        String Valor = sharedPref.getString("usu_id","0");

        Intent intent = new Intent(this, MyFirebaseInstanceService.class);
        this.startService(intent);

        if (Valor != "0")
        {
            Intent intent2 = new Intent(Login.this, Home.class);
            startActivity(intent2);
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

            JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
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
                            /*Intent intent = new Intent(Login.this, Login_contrasena.class );
                            startActivity(intent);*/
                        }
                        else{
                            progreso.hide();

                            Toast toast2 = Toast.makeText( getApplicationContext(),
                                    Resultado.getMensaje(), Toast.LENGTH_LONG );
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    private void Login(){
        progreso = new ProgressDialog(this);
        progreso.setMessage("Iniciando sesion...");
        progreso.show();


        try{
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_correo_electronico", TextUsuario.getText());
            request.put("usu_contrasenia", TextPassword.getText());
            request.put("dis_mac",macAddress);
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

                JSONArray Respuesta = null;
                JSONObject RespuestaObjeto = null;
                JSONObject RespuestaNodoUsuID = null;
                JSONObject RespuestaIdSucursales = null;
                JSONArray ValorIdSucursales = null;
//VerificarAPI
                try {

                    Resultado.setEstatus( response.getString( "estatus" ) );
                    Resultado.setMensaje( response.getString( "mensaje" ) );

                    int status = Integer.parseInt( Resultado.getEstatus() );

                      if (status == 1)
                      {

                    Respuesta = response.getJSONArray( "resultado" );

                    RespuestaObjeto = Respuesta.getJSONObject( 0 );

                    Resultado.setUsuTipoContrasena( RespuestaObjeto.getString( "usu_tipo_contrasenia" ) );
                    String recuperar_contrasena = Resultado.getUsuTipoContrasena();

                    if (recuperar_contrasena == "false") {
                        Intent intent = new Intent( Login.this, Reestablecer_contrasena.class );
                        intent.putExtra( "ParametroCorreo", TextUsuario.getText() );
                        startActivity( intent );
                    } else {
                        Resultado.setUsuNombre( RespuestaObjeto.getString( "usu_nombre" ) );
                        Resultado.setUsuApellidos( RespuestaObjeto.getString( "usu_apellido_paterno" ) + " " + RespuestaObjeto.getString( "usu_apellido_materno" ) );
                        Resultado.setUsuEmail( RespuestaObjeto.getString( "usu_correo_electronico" ) );
                        Resultado.setUsuImagen( RespuestaObjeto.getString( "usu_imagen_perfil" ) );
                        Resultado.setUsu_activo( RespuestaObjeto.getString( "usu_activo" ) );
                        Resultado.setUsu_administrador( RespuestaObjeto.getString( "usu_administrador" ) );

                        JSONObject tipo_id = Respuesta.getJSONObject( 0 );

                        Resultado.setUsuTipoContrasena( tipo_id.getString( "usu_tipo_contrasenia" ) );

                       // String recuperar_contrasena = Resultado.getUsuTipoContrasena();

                        if (recuperar_contrasena == "false") {
                            Intent intent = new Intent( Login.this, Reestablecer_contrasena.class );
                            intent.putExtra( "ParametroCorreo", TextUsuario.getText() );
                            startActivity( intent );
                        } else {
                            Resultado.setUsuNombre( tipo_id.getString( "usu_nombre" ) );
                            Resultado.setUsuApellidos( tipo_id.getString( "usu_apellido_paterno" ) + " " + tipo_id.getString( "usu_apellido_materno" ) );
                            Resultado.setUsuEmail( tipo_id.getString( "usu_correo_electronico" ) );
                            Resultado.setUsuImagen( tipo_id.getString( "usu_imagen_perfil" ) );
                            Resultado.setUsu_activo( tipo_id.getString( "usu_activo" ) );
                            Resultado.setUsu_administrador( tipo_id.getString( "usu_administrador" ) );

                            RespuestaNodoUsuID = RespuestaObjeto.getJSONObject( "usu_id" );
                            Resultado.setUsuId( RespuestaNodoUsuID.getString( "uuid" ) );

                            RespuestaNodoUsuID = tipo_id.getJSONObject( "usu_id" );
                            Resultado.setUsuId( RespuestaNodoUsuID.getString( "uuid" ) );

                            RespuestaIdSucursales = RespuestaObjeto.getJSONObject("usu_sucursales");
                            ValorIdSucursales = RespuestaIdSucursales.getJSONArray("values");


                            jsonArray = new JSONArray();
                            //RespuestaIdSucursales for para sacar los 2 valores
                            for (int i=0; i<ValorIdSucursales.length(); i++) {

                                String elemento = String.valueOf( ValorIdSucursales.get(i) );

                                JSONObject request2 = new JSONObject();
                                try {
                                    request2.put("usu_sucursales", elemento);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                jsonArray.put(request2);

                            }

                            new GuardaPreferencia().execute();

                            Intent intent = new Intent( Login.this, Home.class );
                            startActivity( intent );

                            Toast toast1 =
                                    Toast.makeText( getApplicationContext(),
                                            "Bienvenido " + Resultado.getUsuNombre(), Toast.LENGTH_LONG );
                            toast1.show();
                            progreso.hide();
                        }
                    }
                }
                else{
                              progreso.hide();

                              Toast toast2 = Toast.makeText( getApplicationContext(),
                                      Resultado.getMensaje(), Toast.LENGTH_LONG );
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void Pdf(View v) {

        Document doc = new Document();
        String outpPath = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ).getPath() +"/pdfsin.pdf";

        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outpPath));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Open to write
        doc.open();

        doc.setPageSize( PageSize.A4 );
        doc.addCreationDate();
        doc.addAuthor( "Android School" );
        doc.addCreator( "Pratik Butani" );

            //  urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);


            BaseColor mColorAccent = new BaseColor( 0, 153, 204, 255 );
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;
/**
 * How to USE FONT....
 */

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor( new BaseColor( 0, 0, 0, 68 ) );

            // Title Order Details...
// Adding Title....
            // Font mOrderDetailsTitleFont = new Font( urName,36.0f, Font.NORMAL, BaseColor.BLACK);
// Creating Chunk
            Chunk mOrderDetailsTitleChunk = new Chunk( "Order Details" );
// Creating Paragraph to add...
            Paragraph mOrderDetailsTitleParagraph = new Paragraph( mOrderDetailsTitleChunk );
// Setting Alignment for Heading
            mOrderDetailsTitleParagraph.setAlignment( Element.ALIGN_CENTER );
// Finally Adding that Chunk
            try {
                doc.add( mOrderDetailsTitleParagraph );
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            try {
                doc.add( new Paragraph( "jcdjckjdvcdjk" ) );
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            try {
                doc.add( new Chunk( lineSeparator ) );
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            try {
                doc.add( new Paragraph( "pruebnitsa" ) );
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            doc.close();



    }
//metodos...
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

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        String email = String.valueOf(TextUsuario.getText());
        Matcher mather = pattern.matcher(email);

        if (mather.find() == false) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "El email ingresado es inválido.", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }

//
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
            //
            editor.putString("usu_imagen_perfil", "http://192.168.100.192:8010"+Resultado.getUsuImagen());
            editor.putString("usu_activo", Resultado.getUsu_activo());
            editor.putString("usu_administrador", Resultado.getUsu_administrador());
            editor.putString("cca_id_sucursal", String.valueOf( jsonArray ) );


            editor.commit();

            return null;
        }
    }

    public void Aceptar_cerrar_ventana(Dialog dialog){
        dialog.dismiss();
    }

    public void forgotPassword(View v){
        final Dialog dialog=new Dialog(Login.this);
        dialog.setContentView(R.layout.pop_up_olvide_contrasenia);
        dialog.show();

        Button enviar_correo = (Button) dialog.findViewById(R.id.enviar_correo_contrasena);
        enviar_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText correo_contrasena_olvidada = (EditText)dialog.findViewById(R.id.correo_contrasena_olvidada);
                if(correo_contrasena_olvidada.getText().length()==0) {
                    Toast toast1 = Toast.makeText(getApplicationContext(),
                            "Campo usuario obligatorio ", Toast.LENGTH_SHORT);

                    toast1.show();
                    return;
                }

                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                String email = String.valueOf(correo_contrasena_olvidada.getText());
                Matcher mather = pattern.matcher(email);

                if (mather.find() == false) {
                    Toast toast1 = Toast.makeText(getApplicationContext(),
                            "El email ingresado es inválido.", Toast.LENGTH_SHORT);
                    toast1.show();
                    return;
                }else {

                    progreso = new ProgressDialog(dialog.getContext());
                    progreso.setMessage("Enviando correo...");
                    progreso.show();
                    JSONObject request = new JSONObject();
                    try {
                        request.put("usu_correo_electronico", correo_contrasena_olvidada.getText());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//Api recuperando contraseña
                    String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

                    String ApiPath = url + "/api/login/recuperar-contrasena";

                    JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            JSONObject Respuesta = null;
                            JSONObject RespuestaNodoUsuID = null;

                            try {

                                String status = response.getString("estatus");
                                String mensaje = response.getString("mensaje");

                                int estatus = Integer.parseInt(status);

                                if (estatus == 1) {
                                    dialog.dismiss();
                                    dialog.setContentView(R.layout.pop_up_confirmacion_correo_contrasenia);
                                    dialog.show();
                                    Button cerrar_ventana = (Button) dialog.findViewById(R.id.aceptar_cerrar_ventana);

                                    Toast toast1 =
                                            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);

                                    toast1.show();

                                    progreso.hide();

                                    cerrar_ventana.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Aceptar_cerrar_ventana(dialog);
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
                    VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(postRequest);
                }
            }
        });
    }

}

