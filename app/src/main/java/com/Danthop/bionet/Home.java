package com.Danthop.bionet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Fragment_pop_up_ProfilePhoto.OnPhotoSelectedListener {
    private DrawerLayout drawer;

    Dialog cerrar;
    private static final int REQUEST_CODE = 23;
    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String usu_id;

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        ImageView SelectPhoto = (ImageView) findViewById( R.id.foto_perfil );
        SelectPhoto.setImageBitmap( bitmap );
        mSelectedUri = null;
        mSelectedBitmap = bitmap;


        //GuardarImagen
    }

    @Override
    public void getImagePath(Uri imagePath) {
        ImageView SelectPhoto = (ImageView) findViewById( R.id.foto_perfil );
        imageLoader.displayImage( imagePath.toString(), SelectPhoto );


        File mFile = new File(imagePath.toString());

        mSelectedBitmap = null;
        mSelectedUri = imagePath;

        GuardarImagen(  mFile.getAbsolutePath() );
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home );

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(Home.this));

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace( R.id.fragment_container, new Fragment_pantalla_principal() );
        tx.commit();
        cerrar = new Dialog( this );

        Toolbar toolbar = findViewById( R.id.toolbar );

        drawer = findViewById( R.id.drawer_layout );

        Context context = this;
        SharedPreferences sharedPref = getSharedPreferences( "DatosPersistentes", context.MODE_PRIVATE );

        String Nombre = sharedPref.getString( "usu_nombre", "" );
        String Apellido = sharedPref.getString( "usu_apellidos", "" );
        String ImagenPerfil = sharedPref.getString( "usu_imagen_perfil", "" );
        usu_id = sharedPref.getString( "usu_id", "" );


        NavigationView navigationView = findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        View headView = navigationView.getHeaderView( 0 );
        ImageView imgProfile = headView.findViewById( R.id.foto_perfil );

        String url = getString( R.string.Url ) + ImagenPerfil;

        Picasso.with( context ).load( url ).into( imgProfile );

        TextView NombreUsuario = (TextView) headView.findViewById( R.id.TextNombrePerfil );

        NombreUsuario.setText( Nombre + " " + Apellido );

        imgProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissions();
                FragmentManager fm = getSupportFragmentManager();
                Fragment_pop_up_ProfilePhoto myDialogFragment = new Fragment_pop_up_ProfilePhoto();
                myDialogFragment.show( fm, "photo_dialog_fragment" );
            }
        } );


    }

    private void verifyPermissions() {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission( this.getApplicationContext(),
                permissions[0] ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission( this.getApplicationContext(),
                permissions[1] ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission( this.getApplicationContext(),
                permissions[2] ) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions( Home.this,
                    permissions,
                    REQUEST_CODE );

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_notificaciones:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Fragment_notificaciones()).commit();
                    break;
                case R.id.nav_Ventas:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Fragment_Ventas()).commit();
                    break;
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Fragment_pantalla_principal()).commit();
                    break;
                case R.id.nav_clientes:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Fragment_clientes()).commit();
                    break;

                case R.id.nav_inventario:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Fragment_inventarios()).commit();
                    break;

                case R.id.nav_cerrar_sesion:
                    cerrar.setContentView(R.layout.pop_up_cerrarsesion);
                    cerrar.show();
                    break;
            }

        drawer.closeDrawer( GravityCompat.START );
        return true;

    }

    public void notificaciones(View view) {
        getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,
                new Fragment_notificaciones() ).commit();
    }

    public void ventas(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_Ventas()).commit();
    }
    public void clientes(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_clientes()).commit();
    }

    public void home(View view) {
        getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,
                new Fragment_pantalla_principal() ).commit();
    }

    public void inventario(View view) {
        getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,
                new Fragment_inventarios() ).commit();
    }

    public void cerrar_sesion(View view) {
        cerrar.setContentView( R.layout.pop_up_cerrarsesion );
        cerrar.show();
    }

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.fragment_container, fragment )
                .commit();
    }

    public void ShowDireccionFiscal(View view) {
        Dialog dialog = new Dialog( this );
        dialog.setContentView( R.layout.pop_up_crear_cliente_diferentes_datos );
        dialog.setTitle( "Dirección fiscal" );
        dialog.show();
    }

    public void Aceptar(View view) {
        Intent intent = new Intent( Home.this, Login.class );
        startActivity( intent );

        SharedPreferences sharedPref = getSharedPreferences( "DatosPersistentes", Context.MODE_PRIVATE );

        sharedPref.edit().clear().commit();

    }

    public void Cancelar(View view) {
        cerrar.dismiss();
    }

    private void GuardarImagen(final String imagePath) {

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/usuarios/cambiar-imagen";

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, ApiPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.d("Response", response);
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        smr.addStringParam("usu_id", usu_id);
        smr.addStringParam("usu_imagen_perfil_old", "");
        smr.addStringParam("esApp", "1");

        smr.addFile("usu_imagen_perfil", imagePath);

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(smr);
    }
}