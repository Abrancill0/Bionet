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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.android.volley.Request.Method;


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

        String msg = "";
       // String lang = this.getString(2130968583);
      //  if (lang.compareTo("en") == 0) {
            msg = "Division I is a research and development, production and services in one high-tech research and development, production-oriented enterprises, specializing in POS terminals finance, retail, restaurants, bars, songs and other areas, computer terminals, self-service terminal peripheral equipment R & D, manufacturing and sales! \n company's organizational structure concise and practical, pragmatic style of rigorous, efficient operation. Integrity, dedication, unity, and efficient is the company's corporate philosophy, and constantly strive for today, vibrant, the company will be strong scientific and technological strength, eternal spirit of entrepreneurship, the pioneering and innovative attitude, confidence towards the international information industry, with friends to create brilliant information industry !!! \n\n\n";
            this.SendDataString(msg);
     //   } else if (lang.compareTo("ch") == 0) {
          //  msg = "我司是一家集科研开发、生产经营和服务于一体的高技术研发、生产型企业，专业从事金融、商业零售、餐饮、酒吧、歌吧等领域的POS终端、计算机终端、自助终端周边配套设备的研发、制造及销售！\n公司的组织机构简练实用，作风务实严谨，运行高效。诚信、敬业、团结、高效是公司的企业理念和不断追求今天，朝气蓬勃，公司将以雄厚的科技力量，永恒的创业精神，不断开拓创新的姿态，充满信心的朝着国际化信息产业领域，与朋友们携手共创信息产业的辉煌!!!\n\n\n";
            this.SendDataString(msg);
     //   }

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

    private void SendDataString(String data) {
        if (data.length() > 0) {
            usbCtrl.sendMsg(data, "GBK", dev);
        }

    }

}

