package com.Danthop.bionet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.Class.AccessToken;
import com.Danthop.bionet.Class.LoginService;
import com.Danthop.bionet.Class.ServiceGenerator;
import com.Danthop.bionet.model.LoginModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.zj.usbsdk.UsbController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.android.volley.Request.Method;

public class Login extends Activity {

    EditText TextUsuario, TextPassword;
    ProgressDialog progreso;

    private String NombreCompleto="";
    private String ID_dispositivo;
    private String usu_sucursales;

    LoginModel Resultado = new LoginModel();
    LoginModel ResultadoUser = new LoginModel();
    LoginModel ResultadoLicencia = new LoginModel();
    LoginModel ResultadoToken = new LoginModel();
    private List<LoginModel> IDSucursales;

    private JSONArray jsonArray;
    private static final int REQUEST_CODE = 999;
    private static String Token;
    private byte data[];
    private String Sucursal;

    private int[][] u_infor;

    private String clientid = "danthop-dev";
    private String clientsecret = "001824";
    private String redirectUri = "bionet://callback";
    private String UrlSend = "http://sso-dev.biocheck.net/oauth/authorize";

    private JSONArray Roles = null;
    private String nombre_perfil="";
    private String version ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progreso = new ProgressDialog(this);

        Resultado = new LoginModel();

        setContentView(R.layout.login_nuevo);
        findViewById(R.id.loginlayoutnuevo).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        IDSucursales = new ArrayList<>();

        //-------------;)----------------------

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        ID_dispositivo = sharedPref.getString("id_dispositivo", "");
        Token = sharedPref.getString("Token", "");

        //------------:)----------------------

        VerifyPermisos();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();

        String URLtratada = "";
        String URlRedirecttratada = "";

        if (uri != null) {
            URLtratada = uri.toString().toLowerCase();
            URlRedirecttratada = redirectUri.toString().toLowerCase();
        }

        if (uri != null && URLtratada.toString().startsWith(URlRedirecttratada)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                StringRequest request = new StringRequest(Request.Method.POST, "http://sso-dev.biocheck.net/oauth/token", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals(null)) {

                            try {
                                JSONObject Respuesta = new JSONObject(response);

                                String token = "";
                                String TokenType = "";
                                String RefreshToken = "";
                                String Expire = "";

                                token = Respuesta.getString("access_token");
                                TokenType = Respuesta.getString("token_type");
                                RefreshToken = Respuesta.getString("refresh_token");
                                Expire = Respuesta.getString("expires_in");

                                ResultadoToken.setSso_token(token);
                                ResultadoToken.setSso_token_type(TokenType);
                                ResultadoToken.setSso_refresh_Token(RefreshToken);
                                ResultadoToken.setSso_expire(Expire);
                                // ObtieneUsuario(token);

                                ObtieneLicencia(token);

                                // Logintemporal();

                                Log.e("Your Array Response", response);
                            } catch (JSONException err) {
                                Log.d("Error", err.toString());
                            }

                        } else {
                            Log.e("Your Array Response", "Data Null");
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error is ", "" + error);
                    }
                }) {

                    //This is for Headers If You Needed
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        String credentials = clientid + ":" + clientsecret;
                        String encodedCredentials = android.util.Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        params.put("Authorization", "Basic " + encodedCredentials);

                        return params;
                    }

                    //Pass Your Parameters here
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("grant_type", "authorization_code");
                        params.put("code", code);
                        params.put("redirect_uri", "bionet://callback");
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);


            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }

    private void Login() {
        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        Log.i("Token", String.valueOf(Token));


        if (Token.length() == 0) {
            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
            Token = sharedPref.getString("Token", "");
        }

        try {
            JSONObject request = new JSONObject();
            try {
                request.put("usu_correo_electronico", TextUsuario.getText());
                request.put("usu_contrasenia", TextPassword.getText());
                request.put("dis_mac", ID_dispositivo + "IMEI");
                request.put("dis_token", Token);
                request.put("esApp", 1);


            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i("Arreglo mando", String.valueOf(request));

            String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

            String ApiPath = url + "/api/login/login";

            JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
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

                        Resultado.setEstatus(response.getString("estatus"));
                        Resultado.setMensaje(response.getString("mensaje"));

                        int status = Integer.parseInt(Resultado.getEstatus());

                        if (status == 1) {

                            Respuesta = response.getJSONArray("resultado");

                            Log.i("Arreglo mando", String.valueOf(response));

                            RespuestaObjeto = Respuesta.getJSONObject(0);

                            Resultado.setUsuTipoContrasena(RespuestaObjeto.getString("usu_tipo_contrasenia"));
                            String recuperar_contrasena = Resultado.getUsuTipoContrasena();

                            if (recuperar_contrasena == "false") {
                                Intent intent = new Intent(Login.this, Reestablecer_contrasena.class);
                                intent.putExtra("ParametroCorreo", TextUsuario.getText());
                                startActivity(intent);
                            } else {
                                Resultado.setUsuNombre(RespuestaObjeto.getString("usu_nombre"));
                                Resultado.setUsuApellidos(RespuestaObjeto.getString("usu_apellido_paterno") + " " + RespuestaObjeto.getString("usu_apellido_materno"));
                                Resultado.setUsuEmail(RespuestaObjeto.getString("usu_correo_electronico"));
                                Resultado.setUsuImagen(RespuestaObjeto.getString("usu_imagen_perfil"));
                                Resultado.setUsu_activo(RespuestaObjeto.getString("usu_activo"));
                                Resultado.setUsu_administrador(RespuestaObjeto.getString("usu_administrador"));
                                JSONObject CuentaBionetNodo = RespuestaObjeto.getJSONObject("usu_id_cuenta_bionet");
                                Resultado.setCuenta_bionet(CuentaBionetNodo.getString("uuid"));
                                JSONObject SucursalesNodo = RespuestaObjeto.getJSONObject("usu_sucursales");
                                JSONArray ArregloSucursales = SucursalesNodo.getJSONArray("values");
                                Sucursal = ArregloSucursales.getString(0);


                                JSONObject tipo_id = Respuesta.getJSONObject(0);

                                Resultado.setUsuTipoContrasena(tipo_id.getString("usu_tipo_contrasenia"));

                                // String recuperar_contrasena = Resultado.getUsuTipoContrasena();

                                if (recuperar_contrasena == "false") {
                                    Intent intent = new Intent(Login.this, Reestablecer_contrasena.class);
                                    intent.putExtra("ParametroCorreo", TextUsuario.getText());
                                    startActivity(intent);
                                } else {
                                    Resultado.setUsuNombre(tipo_id.getString("usu_nombre"));
                                    Resultado.setUsuApellidos(tipo_id.getString("usu_apellido_paterno") + " " + tipo_id.getString("usu_apellido_materno"));
                                    Resultado.setUsuEmail(tipo_id.getString("usu_correo_electronico"));
                                    Resultado.setUsuImagen(tipo_id.getString("usu_imagen_perfil"));
                                    Resultado.setUsu_activo(tipo_id.getString("usu_activo"));
                                    Resultado.setUsu_administrador(tipo_id.getString("usu_administrador"));
                                    JSONObject CuentaBionetNodo2 = RespuestaObjeto.getJSONObject("usu_id_cuenta_bionet");
                                    Resultado.setCuenta_bionet(CuentaBionetNodo2.getString("uuid"));
                                    JSONObject SucursalesNodo2 = RespuestaObjeto.getJSONObject("usu_sucursales");
                                    JSONArray ArregloSucursales2 = SucursalesNodo.getJSONArray("values");
                                    Sucursal = ArregloSucursales2.getString(0);

                                    RespuestaNodoUsuID = RespuestaObjeto.getJSONObject("usu_id");
                                    Resultado.setUsuId(RespuestaNodoUsuID.getString("uuid"));

                                    RespuestaNodoUsuID = tipo_id.getJSONObject("usu_id");
                                    Resultado.setUsuId(RespuestaNodoUsuID.getString("uuid"));

                                    RespuestaIdSucursales = RespuestaObjeto.getJSONObject("usu_sucursales");
                                    ValorIdSucursales = RespuestaIdSucursales.getJSONArray("values");


                                    jsonArray = new JSONArray();
                                    //RespuestaIdSucursales for para sacar los 2 valores
                                    for (int i = 0; i < ValorIdSucursales.length(); i++) {

                                        String elemento = String.valueOf(ValorIdSucursales.get(i));

                                        JSONObject request2 = new JSONObject();
                                        try {
                                            request2.put("usu_sucursales", elemento);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        jsonArray.put(request2);

                                    }

                                    new GuardaPreferencia().execute();

                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);

                                    Toast toast1 =
                                            Toast.makeText(getApplicationContext(),
                                                    "Bienvenido " + Resultado.getUsuNombre(), Toast.LENGTH_LONG);
                                    toast1.show();
                                    progreso.hide();
                                }
                            }
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
                                            "Error de conexion", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }
            );
            VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //metodos...
    public void CrearCuenta(View view) {
        Intent intent = new Intent(Login.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    public void IniciarSesion(View view) {

        if (TextUsuario.getText().length() == 0) {
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
        if (TextPassword.getText().length() == 0) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Campo password obligatorio ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        Token = sharedPref.getString("Token", "");


        Login();
    }

    private class GuardaPreferencia extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("usu_nombre", Resultado.getUsuNombre());
            editor.putString("usu_id", Resultado.getUsuId());
            editor.putString("usu_apellidos", Resultado.getUsuApellidos());
            editor.putString("usu_correo_electronico", Resultado.getUsuEmail());
            editor.putString("usu_sucursal", Sucursal);
            editor.putString("usu_cuenta_bionet", Resultado.getCuenta_bionet());

            //
            editor.putString("usu_imagen_perfil", getString(R.string.Url) + Resultado.getUsuImagen());
            editor.putString("usu_activo", Resultado.getUsu_activo());
            editor.putString("usu_administrador", Resultado.getUsu_administrador());
            editor.putString("cca_id_sucursal", String.valueOf(jsonArray));

            editor.putString("sso_usu_correo_electronico", ResultadoUser.getSso_usu_correo_electronico());
            editor.putString("sso_usurname", ResultadoUser.getSso_usurname());
            editor.putString("sso_nombre", ResultadoUser.getSso_nombre());
            editor.putString("sso_usu_Id", ResultadoUser.getSso_usu_Id());
            editor.putString("sso_usuario_activo", ResultadoUser.getSso_usuario_activo());

            editor.putString("sso_code", ResultadoLicencia.getSso_code());
            editor.putString("sso_descripcion_licencia", ResultadoLicencia.getSso_descripcion_licencia());
            editor.putString("sso_perpetua", ResultadoLicencia.getSso_perpetua());
            editor.putString("sso_expira", ResultadoLicencia.getSso_expira());
            editor.putString("sso_activa", ResultadoLicencia.getSso_activa());
            editor.putString("sso_fecha_creacion", ResultadoLicencia.getSso_fecha_creacion());
            editor.putString("sso_fecha_expiracion", ResultadoLicencia.getSso_fecha_expiracion());

            editor.putString("sso_token", ResultadoToken.getSso_token());
            editor.putString("sso_toke_type", ResultadoToken.getSso_token_type());
            editor.putString("sso_refresh_token", ResultadoToken.getSso_refresh_Token());
            editor.putString("sso_expire", ResultadoToken.getSso_expire());

            editor.commit();

            return null;
        }
    }

    private class GuardaPreferencia_Permisos extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("sso_nombre_perfil", nombre_perfil);
            editor.putString("sso_version", version);
            editor.putString("sso_Roles", String.valueOf(Roles));

            editor.commit();

            return null;
        }
    }

    public void Aceptar_cerrar_ventana(Dialog dialog) {
        dialog.dismiss();
    }

    public void PDFIMprimie(View v) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(UrlSend + "?response_type=code&client_id=" + clientid + "&scope=read+write&redirect_uri=" + redirectUri));
        startActivity(intent);

        // usbCtrl.close();

        //// int i =0;

        // for(int i = 0; i < 8; ++i) {
        //     dev = usbCtrl.getDev(this.u_infor[i][0], this.u_infor[i][1]);
        //     if (dev != null) {
        //         break;
        //     }
        // }

        //  if (dev != null) {
        //      if (!usbCtrl.isHasPermission(dev)) {
        //         usbCtrl.getPermission(dev);
        //     } else {
        //        // Toast.makeText(this.getApplicationContext(), this.getString(2130968584), 0).show();

        //     }
        //  }

        //   byte[] bytes = new byte[0];
        //   try {
        //       bytes = convertDocToByteArray("/storage/emulated/0/DCIM/PDFTicket/Ticket.pdf");
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }

        //    String stream = null;
        //    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        //        stream = Base64.getEncoder().encodeToString(bytes);
        //     }
        //     byte[] newBytes = new byte[0];
        //     if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        //         newBytes = Base64.getDecoder().decode(stream);
        //     }
        //     try {
        //        convertByteArrayToDoc("/storage/emulated/0/DCIM/PDFTicket/Ticket.pdf", newBytes);
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //     }

        //String datastring = bytesToHex(bytes);
        //this.SendDataString("datastring");
        //   this.SendDataByte(bytes);


    }

    public void forgotPassword(View v) {
        final Dialog dialog = new Dialog(Login.this);
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

                String email = String.valueOf(correo_contrasena_olvidada.getText());
                Matcher mather = pattern.matcher(email);

                if (mather.find() == false) {
                    Toast toast1 = Toast.makeText(getApplicationContext(),
                            "El email ingresado es inválido.", Toast.LENGTH_SHORT);
                    toast1.show();
                    return;
                } else {

                    progreso = new ProgressDialog(dialog.getContext());
                    progreso.setMessage("Enviando correo...");
                    progreso.setCanceledOnTouchOutside(false);
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

    private void VerifyPermisos() {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(Login.this,
                    permissions,
                    REQUEST_CODE);
        }
    }

    private void ObtieneLicencia(String Access_Token) {
        StringRequest request1 = new StringRequest(Method.GET, "https://sso-dev.biocheck.net/bionet/licence", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                ResultadoLicencia= new LoginModel();

                try {
                    JSONObject Respuesta = new JSONObject(response);

                    ResultadoLicencia.setSso_descripcion_licencia(Respuesta.getString("description"));
                    ResultadoLicencia.setSso_code(Respuesta.getString("code"));
                    ResultadoLicencia.setSso_activa(Respuesta.getString("active"));
                    ResultadoLicencia.setSso_expira(Respuesta.getString("expired"));
                    ResultadoLicencia.setSso_perpetua(Respuesta.getString("perpetual"));
                    ResultadoLicencia.setSso_fecha_creacion(Respuesta.getString("created"));
                    ResultadoLicencia.setSso_fecha_expiracion(Respuesta.getString("expiration"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ObtieneUsuario(Access_Token);

                Log.i("onResponse", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + Access_Token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request1);
    }

    private void ObtieneUsuario(String Access_Token) {
        StringRequest request1 = new StringRequest(Method.GET, "https://sso-dev.biocheck.net/user", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("onResponse", response.toString());

                ResultadoUser= new LoginModel();

                try {
                    JSONObject Respuesta = new JSONObject(response);

                    String NombreCompleto = Respuesta.getString("name") + " " + Respuesta.getString("lastName");
                    ResultadoUser.setSso_usu_correo_electronico(Respuesta.getString("email"));
                    ResultadoUser.setSso_usurname(Respuesta.getString("username"));
                    ResultadoUser.setSso_nombre(NombreCompleto);
                    ResultadoUser.setSso_usu_Id(Respuesta.getString("accountId"));
                    ResultadoUser.setSso_activa(Respuesta.getString("enabled"));

                    Logintemporal();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + Access_Token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request1);
    }

    private void Logintemporal() {

        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        Log.i("Token", String.valueOf(Token));


        if (Token.length() == 0) {
            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
            Token = sharedPref.getString("Token", "");
        }

        try {
            JSONObject request = new JSONObject();
            try {
                request.put("usu_correo_electronico", "jackson1@gmail.com");
                request.put("usu_contrasenia", "12");
                request.put("dis_mac", ID_dispositivo + "IMEI");
                request.put("dis_token", Token);
                request.put("esApp", 1);


            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i("Arreglo mando", String.valueOf(request));

            String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

            String ApiPath = url + "/api/login/login";

            JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Resultado = new LoginModel();

                    JSONArray Respuesta = null;
                    JSONObject RespuestaObjeto = null;
                    JSONObject RespuestaNodoUsuID = null;
                    JSONObject RespuestaIdSucursales = null;
                    JSONArray ValorIdSucursales = null;

                    try {

                        Resultado.setEstatus(response.getString("estatus"));
                        Resultado.setMensaje(response.getString("mensaje"));

                        int status = Integer.parseInt(Resultado.getEstatus());

                        if (status == 1) {

                            Respuesta = response.getJSONArray("resultado");

                            Log.i("Arreglo mando", String.valueOf(response));

                            RespuestaObjeto = Respuesta.getJSONObject(0);

                            Resultado.setUsuTipoContrasena(RespuestaObjeto.getString("usu_tipo_contrasenia"));
                            String recuperar_contrasena = Resultado.getUsuTipoContrasena();

                            if (recuperar_contrasena == "false") {
                                Intent intent = new Intent(Login.this, Reestablecer_contrasena.class);
                                intent.putExtra("ParametroCorreo", TextUsuario.getText());
                                startActivity(intent);
                            } else {
                                Resultado.setUsuNombre(RespuestaObjeto.getString("usu_nombre"));
                                Resultado.setUsuApellidos(RespuestaObjeto.getString("usu_apellido_paterno") + " " + RespuestaObjeto.getString("usu_apellido_materno"));
                                Resultado.setUsuEmail(RespuestaObjeto.getString("usu_correo_electronico"));
                                Resultado.setUsuImagen(RespuestaObjeto.getString("usu_imagen_perfil"));
                                Resultado.setUsu_activo(RespuestaObjeto.getString("usu_activo"));
                                Resultado.setUsu_administrador(RespuestaObjeto.getString("usu_administrador"));
                                JSONObject CuentaBionetNodo = RespuestaObjeto.getJSONObject("usu_id_cuenta_bionet");
                                Resultado.setCuenta_bionet(CuentaBionetNodo.getString("uuid"));
                                JSONObject SucursalesNodo = RespuestaObjeto.getJSONObject("usu_sucursales");
                                JSONArray ArregloSucursales = SucursalesNodo.getJSONArray("values");
                                Sucursal = ArregloSucursales.getString(0);

                                JSONObject tipo_id = Respuesta.getJSONObject(0);

                                Resultado.setUsuTipoContrasena(tipo_id.getString("usu_tipo_contrasenia"));

                                // String recuperar_contrasena = Resultado.getUsuTipoContrasena();

                                if (recuperar_contrasena == "false") {
                                    Intent intent = new Intent(Login.this, Reestablecer_contrasena.class);
                                    intent.putExtra("ParametroCorreo", TextUsuario.getText());
                                    startActivity(intent);
                                } else {
                                    Resultado.setUsuNombre(tipo_id.getString("usu_nombre"));
                                    Resultado.setUsuApellidos(tipo_id.getString("usu_apellido_paterno") + " " + tipo_id.getString("usu_apellido_materno"));
                                    Resultado.setUsuEmail(tipo_id.getString("usu_correo_electronico"));
                                    Resultado.setUsuImagen(tipo_id.getString("usu_imagen_perfil"));
                                    Resultado.setUsu_activo(tipo_id.getString("usu_activo"));
                                    Resultado.setUsu_administrador(tipo_id.getString("usu_administrador"));
                                    JSONObject CuentaBionetNodo2 = RespuestaObjeto.getJSONObject("usu_id_cuenta_bionet");
                                    Resultado.setCuenta_bionet(CuentaBionetNodo2.getString("uuid"));
                                    JSONObject SucursalesNodo2 = RespuestaObjeto.getJSONObject("usu_sucursales");
                                    JSONArray ArregloSucursales2 = SucursalesNodo.getJSONArray("values");
                                    Sucursal = ArregloSucursales2.getString(0);

                                    RespuestaNodoUsuID = RespuestaObjeto.getJSONObject("usu_id");
                                    Resultado.setUsuId(RespuestaNodoUsuID.getString("uuid"));

                                 //   RespuestaNodoUsuID = tipo_id.getJSONObject("usu_id");
                                 //   Resultado.setUsuId(RespuestaNodoUsuID.getString("uuid"));

                                    RespuestaIdSucursales = RespuestaObjeto.getJSONObject("usu_sucursales");
                                    ValorIdSucursales = RespuestaIdSucursales.getJSONArray("values");


                                    jsonArray = new JSONArray();
                                    //RespuestaIdSucursales for para sacar los 2 valores
                                    for (int i = 0; i < ValorIdSucursales.length(); i++) {

                                        String elemento = String.valueOf(ValorIdSucursales.get(i));

                                        JSONObject request2 = new JSONObject();
                                        try {
                                            request2.put("usu_sucursales", elemento);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        jsonArray.put(request2);

                                    }

                                    new GuardaPreferencia().execute();

                                    String Code = ResultadoLicencia.getSso_code().substring(0,4);

                                    ObtienePermisos(Resultado.getUsuId(), Code,(Resultado.getUsuNombre() + " " + Resultado.getUsuApellidos()));
                                }
                            }
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
                                            "Error de conexion", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }
            );
            VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void ObtienePermisos(String Usu_id, String ver_code,String NombreUsuario) {
        try {
            JSONObject request = new JSONObject();
            try {
                request.put("usu_id", Usu_id);
                request.put("ver_code", ver_code);
                request.put("esApp", 1);


            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/usuarios/perfiles/validar-permisos";

            JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject Resultado = null;

                    int status = 0;
                    String mensaje = "";

                    try {

                        status = response.getInt("estatus");
                        mensaje = response.getString("mensaje");

                        if (status == 1) {

                            Resultado = response.getJSONObject("resultado");
                            nombre_perfil=Resultado.getString("nombre_perfil");
                            version = Resultado.getString("version");
                            Roles = Resultado.getJSONArray("roles");

                            NombreCompleto =  NombreUsuario;

                            new GuardaPreferencia_Permisos().execute();

                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra("sso_Roles", String.valueOf(Roles));
                            startActivity(intent);

                            Toast toast1 =
                                    Toast.makeText(getApplicationContext(),
                                            "Bienvenido " + NombreCompleto, Toast.LENGTH_LONG);
                            toast1.show();

                            progreso.hide();

                        } else {
                            progreso.hide();

                            Toast toast2 = Toast.makeText(getApplicationContext(),
                                    mensaje, Toast.LENGTH_LONG);
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
                                            "Error de conexion", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }
            );
            VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);
            postRequest.setShouldCache(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}

