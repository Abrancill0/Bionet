package com.Danthop.bionet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.model.LoginModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zj.usbsdk.UsbController;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.*;


import static com.android.volley.Request.Method;

import java.util.Base64;

import zj.com.customize.sdk.Other;


public class Login extends Activity {

    EditText TextUsuario, TextPassword;
    ProgressDialog progreso;

    private String ID_dispositivo;
    private String usu_sucursales;

    LoginModel Resultado = new LoginModel();
    private List<LoginModel> IDSucursales;

    private JSONArray jsonArray;
    private static final int REQUEST_CODE = 999;
    private static String Token;
    private byte data[];


    private int[][] u_infor;
    // static UsbController  usbCtrl = null;
    static UsbDevice dev = null;

    static UsbController usbCtrl = null;




    @SuppressLint({"HandlerLeak"})
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                 //   Toast.makeText(PrintDemo.this.getApplicationContext(), PrintDemo.this.getString(2130968584), 0).show();
                    //    PrintDemo.this.btnSend.setEnabled(true);
                    //   PrintDemo.this.btn_test.setEnabled(true);
                    //    PrintDemo.this.btnClose.setEnabled(true);
                    //    PrintDemo.this.btn_printA.setEnabled(true);
                    //    PrintDemo.this.btn_BMP.setEnabled(true);
                    //   PrintDemo.this.btn_ChoseCommand.setEnabled(true);
                    //   PrintDemo.this.btn_prtcodeButton.setEnabled(true);
                    //   PrintDemo.this.btn_prtsma.setEnabled(true);
                    //   PrintDemo.this.btn_prttableButton.setEnabled(true);
                    //    PrintDemo.this.Simplified.setEnabled(true);
                    //   PrintDemo.this.Korean.setEnabled(true);
                    //    PrintDemo.this.big5.setEnabled(true);
                    //    PrintDemo.this.thai.setEnabled(true);
                    //    PrintDemo.this.btn_conn.setEnabled(false);
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progreso = new ProgressDialog(this);

        usbCtrl = new UsbController(this, this.mHandler);

        this.u_infor = new int[8][2];
        this.u_infor[0][0] = 7358;
        this.u_infor[0][1] = 3;
        this.u_infor[1][0] = 7344;
        this.u_infor[1][1] = 3;
        this.u_infor[2][0] = 1155;
        this.u_infor[2][1] = 22336;
        this.u_infor[3][0] = 1171;
        this.u_infor[3][1] = 34656;
        this.u_infor[4][0] = 1046;
        this.u_infor[4][1] = 20497;
        this.u_infor[5][0] = 1046;
        this.u_infor[5][1] = 43707;
        this.u_infor[6][0] = 5721;
        this.u_infor[6][1] = 35173;
        this.u_infor[7][0] = 1155;
        this.u_infor[7][1] = 22337;
        setContentView(R.layout.login);

        TextUsuario = (EditText) findViewById(R.id.TextUsuario);
        TextPassword = (EditText) findViewById(R.id.TextPassword);
        IDSucursales = new ArrayList<>();

        //-------------;)----------------------

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        ID_dispositivo = sharedPref.getString("id_dispositivo","");
        Token = sharedPref.getString("Token","");

        //------------:)----------------------

        VerifyPermisos();

    }


    private void Login(){
        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        Log.i("Token", String.valueOf( Token ) );


        if (Token.length()==0)
        {
            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
            Token = sharedPref.getString("Token","");
        }

        try{
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_correo_electronico", TextUsuario.getText());
            request.put("usu_contrasenia", TextPassword.getText());
            request.put("dis_mac",ID_dispositivo+"IMEI");
            request.put("dis_token",Token);
            request.put("esApp",1);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

            Log.i("Arreglo mando", String.valueOf( request ) );

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

                          Log.i("Arreglo mando", String.valueOf( response ) );

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

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        Token = sharedPref.getString("Token", "");


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

    public void PDFIMprimie (View v)
    {
        usbCtrl.close();

       // int i =0;

        for(int i = 0; i < 8; ++i) {
            dev = usbCtrl.getDev(this.u_infor[i][0], this.u_infor[i][1]);
            if (dev != null) {
                break;
            }
        }

        if (dev != null) {
            if (!usbCtrl.isHasPermission(dev)) {
                usbCtrl.getPermission(dev);
            } else {
               // Toast.makeText(this.getApplicationContext(), this.getString(2130968584), 0).show();

            }
        }

        byte[] bytes = new byte[0];
        try {
            bytes = convertDocToByteArray("/storage/emulated/0/DCIM/PDFTicket/Ticket.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stream = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            stream = Base64.getEncoder().encodeToString(bytes);
        }
        byte[] newBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            newBytes = Base64.getDecoder().decode(stream);
        }
        try {
            convertByteArrayToDoc("/storage/emulated/0/DCIM/PDFTicket/Ticket.pdf", newBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String datastring = bytesToHex(bytes);
        //this.SendDataString("datastring");
        this.SendDataByte(bytes);





    }

    public static byte[] convertDocToByteArray(String path)throws FileNotFoundException, IOException {
        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
           // Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    public static void convertByteArrayToDoc(String path, byte[] bytes)throws FileNotFoundException, IOException {
        File someFile = new File(path);
        FileOutputStream fos = new FileOutputStream(someFile);
        fos.write(bytes);
        fos.flush();
        fos.close();
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
            ActivityCompat.requestPermissions(Login.this,
                    permissions,
                    REQUEST_CODE);
        }
    }

    //Este metodo se debe de llevar al fracgment de ventas
    private void SendDataString(String data) {
        if (data.length() > 0) {
            usbCtrl.sendMsg(data, "GBK", dev);
        }

    }

    //Este metodo se debe de llevar al fracgment de ventas
    private void SendDataByte(byte[] data){
        if(data.length>0)
            usbCtrl.sendByte(data, dev);
    }

//Este metodo se debe de llevar al fracgment de ventas
    @Override
    public void onDestroy() {
        super.onDestroy();
        usbCtrl.close();
    }




}

