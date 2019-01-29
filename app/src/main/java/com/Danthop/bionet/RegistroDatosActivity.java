package com.Danthop.bionet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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

import static com.android.volley.Request.Method;

public class RegistroDatosActivity extends FragmentActivity implements Fragment_pop_up_ProfilePhoto.OnPhotoSelectedListener {

    private EditText NombreNegocio;
    private EditText NombrePersona;
    private EditText ApellidoPaterno;
    private EditText ApellidoMaterno;
    private EditText CelularAdministrador;
    private Spinner GiroNegocio;

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
        imageLoader.displayImage(imagePath.toString(),SelectPhoto);
        mSelectedBitmap = null;
        mSelectedUri = imagePath;
    }

    private static final String TAG = "RegistroDatos";
    private static final int REQUEST_CODE = 2312424;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registro_datos);

        Spinner spinner = (Spinner) findViewById(R.id.Combo_Giro_Negocio);

        NombreNegocio = (EditText) findViewById(R.id.Text_Nombre_Negocio);
        NombrePersona = (EditText) findViewById(R.id.Text_Nombre_Persona);
        ApellidoPaterno = (EditText) findViewById(R.id.Text_Apellido_Paterno);
        ApellidoMaterno = (EditText) findViewById(R.id.Text_Apellido_Materno);
        CelularAdministrador = (EditText) findViewById(R.id.Text_Celuar_Administrador);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Giros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

    }

    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;

    public void avanzar(View view) {

        Intent intent = new Intent(RegistroDatosActivity.this, Numero_sucursal.class);
        startActivity(intent);
        //GuardarDatos();

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

        //progreso = new ProgressDialog(this);
       // progreso.setMessage("Iniciando sesion...");
       // progreso.show();


        JSONObject request = new JSONObject();
        try
        {
            request.put("cbn_nombre_negocio", NombreNegocio.getText());
            request.put("cbn_id_giro_negocio", 5);
            request.put("usu_nombre", NombrePersona.getText());
            request.put("usu_apellido_paterno", ApellidoPaterno.getText());
            request.put("usu_apellido_materno", ApellidoMaterno.getText());
            request.put("usu_numero_telefono", CelularAdministrador.getText());
            request.put("cbn_numero_sucursales", 0);
            request.put("con_logo_negocio", "");

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

                        Respuesta = response.getJSONObject("resultado");

                        Intent intent = new Intent(RegistroDatosActivity.this, Numero_sucursal.class);
                        startActivity(intent);


                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();
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

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);

    }


}
