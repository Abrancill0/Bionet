package com.Danthop.bionet;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.model.LoginModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.mercadolibre.android.sdk.Identity;
import com.mercadolibre.android.sdk.Meli;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Fragment_pop_up_ProfilePhoto.OnPhotoSelectedListener {
    private DrawerLayout drawer;

    Dialog cerrar;
    private static final int REQUEST_CODE = 23;
    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String usu_id;
    private String url;
    private String img_ruta_servidor;
    private ImageView imgProfile;
    private ImageView img_pantalla_principal;
    private ImageView Logo_empresa;
    LoginModel Resultado = new LoginModel();

    private ImageView op_home;
    private ImageView op_notificaciones;
    private ImageView op_ventas;
    private ImageView op_clientes;
    private ImageView op_lealtad;
    private ImageView op_ecommerce;
    private ImageView op_inventario;
    private ImageView op_bio;
    private ImageView op_salir;


    @Override
    public void getImageBitmap(Bitmap bitmap) {
        mSelectedUri = null;
        mSelectedBitmap = bitmap;

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), bitmap);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));

        GuardarImagen(finalFile.getAbsolutePath());

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


    @Override
    public void getImagePath(Uri imagePath) {

        ImageView SelectPhoto = (ImageView) findViewById(R.id.foto_perfil_hamburguesa);
        imageLoader.displayImage(imagePath.toString(), SelectPhoto);
        mSelectedBitmap = null;
        mSelectedUri = imagePath;


        GuardarImagen(imagePath.getPath());
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(Home.this));


        cerrar = new Dialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);

        Context context = this;
        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", context.MODE_PRIVATE);

        String Nombre = sharedPref.getString("usu_nombre", "");
        String Apellido = sharedPref.getString("usu_apellidos", "");
        String ImagenPerfil = sharedPref.getString("usu_imagen_perfil", "");
        String LogoEmpresa = sharedPref.getString("logo_imagen", "");

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_container, new Fragment_pantalla_principal());
        tx.commit();

        usu_id = sharedPref.getString("usu_id", "");
        img_ruta_servidor = ImagenPerfil;
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView = navigationView.getHeaderView(0);

        img_pantalla_principal = findViewById(R.id.foto_perfil);
        imgProfile = headView.findViewById(R.id.foto_perfil_hamburguesa);
        Logo_empresa = findViewById(R.id.logo_empresa);

        if (img_ruta_servidor.equals("")) {

        } else {
            Picasso.with(getApplicationContext()).load(img_ruta_servidor).into(imgProfile);
        }

        if (LogoEmpresa.equals("")) {

        } else {
            Picasso.with(getApplicationContext()).load(LogoEmpresa).into(Logo_empresa);
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        String url = getString(R.string.Url) + ImagenPerfil;


        TextView NombreUsuario = (TextView) headView.findViewById(R.id.TextNombrePerfil);

        NombreUsuario.setText(Nombre + " " + Apellido);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissions();
                FragmentManager fm = getSupportFragmentManager();
                Fragment_pop_up_ProfilePhoto myDialogFragment = new Fragment_pop_up_ProfilePhoto();
                myDialogFragment.show(fm, "photo_dialog_fragment");
            }
        });


        LoadButtons();


    }

    private void verifyPermissions() {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(Home.this,
                    permissions,
                    REQUEST_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_notificaciones:
                notificaciones();
                break;
            case R.id.nav_Ventas:
                ventas();
                break;
            case R.id.nav_home:
                home();
                break;
            case R.id.nav_clientes:
                clientes();
                break;
            case R.id.nav_lealtad:
                lealtad();
                break;

            case R.id.nav_ecommerce:
                ecomerce();
                break;

            case R.id.nav_inventario:
                inventario();
                break;

            case R.id.nav_cerrar_sesion:
                cerrar_sesion();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void notificaciones() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_notificaciones()).commit();
        op_home.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_notificaciones.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
        op_ventas.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_clientes.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_lealtad.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ecommerce.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_inventario.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_bio.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_salir.setBackgroundColor(getResources().getColor(R.color.gray2));
    }

    public void ventas() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_Ventas()).commit();

        op_home.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_notificaciones.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ventas.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
        op_clientes.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_lealtad.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ecommerce.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_inventario.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_bio.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_salir.setBackgroundColor(getResources().getColor(R.color.gray2));
    }

    public void clientes() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_clientes()).commit();

        op_home.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_notificaciones.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ventas.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_clientes.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
        op_lealtad.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ecommerce.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_inventario.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_bio.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_salir.setBackgroundColor(getResources().getColor(R.color.gray2));
    }

    public void ecomerce() {

        op_home.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_notificaciones.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ventas.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_clientes.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_lealtad.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ecommerce.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
        op_inventario.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_bio.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_salir.setBackgroundColor(getResources().getColor(R.color.gray2));
        Context context = this;
        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", context.MODE_PRIVATE);

        String UserML = sharedPref.getString("UserIdML", "");
        String AccesToken = sharedPref.getString("AccessToken", "");
        String TokenLife = sharedPref.getString("TokenLifetime", "");
        String FechaCreacion = sharedPref.getString("FechaCreacionToken", "");


        if (AccesToken.length() == 0) {

            Meli.startLogin(this, REQUEST_CODE);
        } else {

            Date date1 = null;
            Date date2 = null;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String date = df.format(Calendar.getInstance().getTime());

            try {
                date1 = simpleDateFormat.parse(String.valueOf(date));
                date2 = simpleDateFormat.parse(FechaCreacion);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            long different = date1.getTime() - date2.getTime();

            long secondsInMilli = 1000;

            long elapsedSeconds = different / secondsInMilli;

            long TokenLifeLong = Long.valueOf(TokenLife);

            if (TokenLifeLong > elapsedSeconds) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fragment_ecomerce()).commit();
            } else {
                Meli.startLogin(this, REQUEST_CODE);
            }

        }

    }

    public void home() {

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_pantalla_principal()).commit();
        op_home.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
        op_notificaciones.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ventas.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_clientes.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_lealtad.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ecommerce.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_inventario.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_bio.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_salir.setBackgroundColor(getResources().getColor(R.color.gray2));

    }

    public void inventario() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_inventarios()).commit();

        op_home.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_notificaciones.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ventas.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_clientes.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_lealtad.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ecommerce.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_inventario.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
        op_bio.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_salir.setBackgroundColor(getResources().getColor(R.color.gray2));
    }

    public void lealtad() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentLealtad()).commit();
        op_home.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_notificaciones.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_ventas.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_clientes.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_lealtad.setBackgroundColor(getResources().getColor(R.color.fondo_azul));
        op_ecommerce.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_inventario.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_bio.setBackgroundColor(getResources().getColor(R.color.gray2));
        op_salir.setBackgroundColor(getResources().getColor(R.color.gray2));
    }

    public void cerrar_sesion() {
        cerrar.setContentView(R.layout.pop_up_cerrarsesion);
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
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();
    }


    public void Aceptar(View view) {
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);

        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        sharedPref.edit().clear().commit();

    }

    public void Cancelar(View view) {
        cerrar.dismiss();
    }

    private void GuardarImagen(String RutaReal) {

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/usuarios/cambiar-imagen";

        //String image = getStringImage( imagePath );

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, ApiPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jObj = null;
                        try {
                            jObj = new JSONObject(response);
                            img_ruta_servidor = jObj.getString("resultado");
                            System.out.println(img_ruta_servidor);
                            Picasso.with(getApplicationContext()).load(img_ruta_servidor).into(imgProfile);
                            //Picasso.with( getApplicationContext() ).load( img_ruta_servidor ).into( img_pantalla_principal);

                            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("usu_imagen_perfil", img_ruta_servidor);
                            editor.commit();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), "Se cambió correctamente su imagen de perfil", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        smr.addStringParam("usu_id", usu_id);
        smr.addStringParam("usu_imagen_perfil_old", "lo que sea");
        smr.addStringParam("esApp", "1");
        smr.addFile("usu_imagen_perfil", RutaReal);


        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(smr);
    }

    public String getStringImage(Bitmap bn) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bn.compress(Bitmap.CompressFormat.PNG, 100, ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = android.util.Base64.encodeToString(imagebyte, android.util.Base64.DEFAULT);
        return encode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                processLoginProcessCompleted();
            } else {
                processLoginProcessWithError();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void processLoginProcessCompleted() {
        Identity identity = Meli.getCurrentIdentity(getApplicationContext());

        if (identity != null) {

            SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("UserIdML", identity.getUserId());
            editor.putString("AccessToken", identity.getAccessToken().getAccessTokenValue());
            editor.putString("TokenLifetime", String.valueOf(identity.getAccessToken().getAccessTokenLifetime()));

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String date = df.format(Calendar.getInstance().getTime());

            editor.putString("FechaCreacionToken", date);

            editor.commit();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Fragment_ecomerce()).commit();

        }
    }

    private void processLoginProcessWithError() {
        Toast.makeText(this, "Oooops, something went wrong with the login process", Toast.LENGTH_SHORT).show();
    }

    private void LoadButtons() {
        op_home = findViewById(R.id.op_home);
        op_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home();
            }
        });
        op_notificaciones = findViewById(R.id.op_notificaciones);
        op_notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificaciones();
            }
        });
        op_ventas = findViewById(R.id.op_ventas);
        op_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventas();
            }
        });
        op_clientes = findViewById(R.id.op_clientes);
        op_clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientes();
            }
        });
        op_lealtad = findViewById(R.id.op_lealtad);
        op_lealtad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lealtad();
            }
        });
        op_ecommerce = findViewById(R.id.op_ecommerce);
        op_ecommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ecomerce();
            }
        });
        op_inventario = findViewById(R.id.op_inventario);
        op_inventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventario();
            }
        });
        op_bio = findViewById(R.id.op_bio);
        op_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        op_salir = findViewById(R.id.op_salir);
        op_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar_sesion();
            }
        });
    }
}