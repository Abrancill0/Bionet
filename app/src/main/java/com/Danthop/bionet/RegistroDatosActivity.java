package com.Danthop.bionet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method;

public class RegistroDatosActivity extends FragmentActivity implements Fragment_pop_up_ProfilePhoto.OnPhotoSelectedListener {

    private EditText NombreNegocio;
    private EditText NombrePersona;
    private EditText ApellidoPaterno;
    private EditText ApellidoMaterno;
    private EditText CelularAdministrador;
    private Spinner GiroNegocio;
    ProgressDialog progreso;
    private String IDUsuario;
    private  Uri Rutaimagen;
    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;
    private String img_ruta_servidor;
    private static final String TAG = "RegistroDatos";
    private static final int REQUEST_CODE = 23;
    private ArrayList<String> GiroName;
    private String RealPath;
    private ImageView Image;

    private ImageLoader imageLoader;

    private String RutaReal;

    @Override
    public void getImagePath(Uri imagePath) {

    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        mSelectedUri = null;
        mSelectedBitmap = bitmap;

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), bitmap);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));

        RutaReal=finalFile.getAbsolutePath();
        imageLoader.displayImage(tempUri.toString(),Image);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }



    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registro_datos);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(RegistroDatosActivity.this));
        Image = findViewById( R.id.LogoImagen );

        GiroName=new ArrayList<>();

        GiroNegocio = (Spinner) findViewById(R.id.Combo_Giro_Negocio);

        NombreNegocio = (EditText) findViewById(R.id.Text_Nombre_Negocio);
        NombrePersona = (EditText) findViewById(R.id.Text_Nombre_Persona);
        ApellidoPaterno = (EditText) findViewById(R.id.Text_Apellido_Paterno);
        ApellidoMaterno = (EditText) findViewById(R.id.Text_Apellido_Materno);
        CelularAdministrador = (EditText) findViewById(R.id.Text_Celuar_Administrador);

        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");

        llenarspinner();


    }

    public void llenarspinner(){

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", IDUsuario);
            request.put("esApp", "1");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/cuentas/select_giros_negocio";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        for(int x = 0; x < Respuesta.length(); x++){
                            JSONObject jsonObject1=Respuesta.getJSONObject(String.valueOf(x));
                            String giro=jsonObject1.getString("gne_nombre");
                            GiroName.add(giro);
                        }
                        GiroNegocio.setAdapter(new ArrayAdapter<String>(RegistroDatosActivity.this,android.R.layout.simple_spinner_item,GiroName));

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(RegistroDatosActivity.this, Mensaje, Toast.LENGTH_LONG);

                        toast1.show();


                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(RegistroDatosActivity.this, e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();


                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(RegistroDatosActivity.this, error.toString(), Toast.LENGTH_LONG);

                        toast1.show();


                    }
                }
        );

        VolleySingleton.getInstanciaVolley(RegistroDatosActivity.this).addToRequestQueue(postRequest);

    }

    public void avanzar(View view) {

        GuardarDatos(RutaReal);

    }

    public void tomarFoto(View v){
        verifyPermissions();
        FragmentManager fm = getSupportFragmentManager();
        Fragment_pop_up_ProfilePhoto myDialogFragment = new Fragment_pop_up_ProfilePhoto();
        myDialogFragment.show(fm, "photo_dialog_fragment");

    }

    private void verifyPermissions(){
        Log.d(TAG, "verifyPermissions:asking user for permissions");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0])== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1])== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2])== PackageManager.PERMISSION_GRANTED) {

        }else{
            ActivityCompat.requestPermissions(RegistroDatosActivity.this,
                    permissions,
                    REQUEST_CODE);

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }


    private class GuardaPreferencia extends AsyncTask<Void,String,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor =  sharedPref.edit();
            editor.putString("usu_nombre", String.valueOf(NombrePersona.getText()));
            editor.putString("usu_id", IDUsuario);
            editor.putString("usu_apellidos", ApellidoPaterno.getText() + " " + ApellidoMaterno.getText());
            editor.putString("usu_correo_electronico", "");
            editor.putString("usu_imagen_perfil", "");
            editor.putString("usu_activo", "1");
            editor.putString("usu_administrador", "0");

            editor.commit();

            return null;
        }
    }

    private void GuardarDatos( String RutaReal) {
        progreso = new ProgressDialog(this);
        progreso.setMessage("procesando...");
        progreso.show();

        String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

        String ApiPath = url + "/api/cuentas/store-config";

        //String image = getStringImage( imagePath );

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, ApiPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject jObj = null;
                        JSONObject NodoResultado=null;
                        try {
                            jObj = new JSONObject(response);
                            NodoResultado = jObj.getJSONObject("resultado");

                            img_ruta_servidor = NodoResultado.getString("url_logo");

                            System.out.println(img_ruta_servidor);



                            Picasso.with( getApplicationContext() ).load( img_ruta_servidor ).into( Image );
                            //Picasso.with( getApplicationContext() ).load( img_ruta_servidor ).into( img_pantalla_principal);

                            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor =  sharedPref.edit();
                            editor.putString("logo_imagen",img_ruta_servidor);
                            editor.commit();

                            Intent intent=new Intent(RegistroDatosActivity.this,EleccionPremium.class);
                            intent.putExtra("IDUsuario", IDUsuario);
                            startActivity(intent);

                            new GuardaPreferencia().execute();

                            progreso.hide();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Datos Guardados", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        smr.addStringParam("cbn_nombre_negocio", String.valueOf(NombreNegocio.getText()));
        smr.addStringParam("cbn_id_giro_negocio", "5");
        smr.addStringParam("usu_nombre", String.valueOf(NombrePersona.getText()));
        smr.addStringParam("usu_apellido_paterno", String.valueOf(ApellidoPaterno.getText()));
        smr.addStringParam("usu_apellido_materno", String.valueOf(ApellidoMaterno.getText()));
        smr.addStringParam("usu_numero_celular", String.valueOf(CelularAdministrador.getText()));
        smr.addStringParam("cbn_numero_sucursales", "0");
        smr.addStringParam("esApp", "1");
        smr.addStringParam("usu_id", IDUsuario);
        smr.addFile("con_logo_negocio",  RutaReal);


        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(smr);
    }


}
