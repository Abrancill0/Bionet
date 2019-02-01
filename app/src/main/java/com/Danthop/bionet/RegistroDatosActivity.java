package com.Danthop.bionet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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

    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        ImageView SelectPhoto = (ImageView) findViewById(R.id.profileImagen);
        SelectPhoto.setImageBitmap(bitmap);
        mSelectedUri = null;
        mSelectedBitmap = bitmap;

    }

    @Override
    public void getImagePath(Uri imagePath) {
        ImageView SelectPhoto = (ImageView) findViewById(R.id.profileImagen);
        Rutaimagen = Uri.parse(imagePath.toString());
        imageLoader.displayImage(imagePath.toString(),SelectPhoto);
        mSelectedBitmap = null;
        mSelectedUri = imagePath;
    }

    private static final String TAG = "RegistroDatos";
    private static final int REQUEST_CODE = 23;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registro_datos);

        Spinner spinner = (Spinner) findViewById(R.id.Combo_Giro_Negocio);

        NombreNegocio = (EditText) findViewById(R.id.Text_Nombre_Negocio);
        NombrePersona = (EditText) findViewById(R.id.Text_Nombre_Persona);
        ApellidoPaterno = (EditText) findViewById(R.id.Text_Apellido_Paterno);
        ApellidoMaterno = (EditText) findViewById(R.id.Text_Apellido_Materno);
        CelularAdministrador = (EditText) findViewById(R.id.Text_Celuar_Administrador);

        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Giros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

    }

    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;

    public void avanzar(View view) {

        GuardarDatos();

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

    private void GuardarDatos(){

        progreso = new ProgressDialog(this);
        progreso.setMessage("procesando...");
        progreso.show();


       // File file = new File(this,Rutaimagen);

        JSONObject request = new JSONObject();
        try
        {
            request.put("cbn_nombre_negocio", NombreNegocio.getText());
            request.put("cbn_id_giro_negocio", "5");
            request.put("usu_nombre", NombrePersona.getText());
            request.put("usu_apellido_paterno", ApellidoPaterno.getText());
            request.put("usu_apellido_materno", ApellidoMaterno.getText());
            request.put("usu_numero_celular", CelularAdministrador.getText());
            request.put("cbn_numero_sucursales", "0");
            request.put("con_logo_negocio", "");
            request.put("esApp", "1");
            request.put("usu_id", IDUsuario);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

        String ApiPath = url + "/api/cuentas/store-config";

        JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                        Intent intent = new Intent(RegistroDatosActivity.this, Numero_sucursal.class);
                        intent.putExtra("IDUsuario", IDUsuario);
                        startActivity(intent);

                        new GuardaPreferencia().execute();

                        progreso.hide();
                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                        progreso.hide();

                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();

                    progreso.hide();
                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "Error de conexion", Toast.LENGTH_SHORT);

                        toast1.show();

                        progreso.hide();

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);

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


}
