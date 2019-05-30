package com.Danthop.bionet;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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


public class Home extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, Fragment_pop_up_ProfilePhoto.OnPhotoSelectedListener {
    private DrawerLayout drawer;
    private static AppCompatActivity HOME;
    Dialog cerrar;
    private static final int REQUEST_CODE = 23;
    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String usu_id;
    private String url;
    private String img_ruta_servidor;
    private ImageView imgProfile;
    private ImageView imgLogo_empresa;
    LoginModel Resultado = new LoginModel();

    private String android_id;
    private View Internet;
    private TextView textInternet;
    private Handler handler1;
    private Boolean networkstatus;
    private View layoutCerrar;
    private TextView btn_cerrar_ecommerce;
    private Dialog cerrar_sesion_ecommerce;
    private ProgressDialog progressDialog;

    private WifiManager wifiManager;

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
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        layoutCerrar = findViewById(R.id.layout_cerrar_ecommerce);

        handler1 = new Handler();
        Internet = findViewById(R.id.internet);
        textInternet = findViewById(R.id.internet_text);

        home();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(Home.this));

        cerrar = new Dialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);

        Context context = this;
        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", context.MODE_PRIVATE);

        String Nombre = sharedPref.getString("usu_nombre", "");
        String Apellido = sharedPref.getString("usu_apellidos", "");
        String RutaImagenPerfil = sharedPref.getString("usu_imagen_perfil", "");
        String RutaLogoEmpresa = sharedPref.getString("logo_imagen", "");

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_container, new Fragment_pantalla_principal());
        tx.commit();

        usu_id = sharedPref.getString("usu_id", "");
        img_ruta_servidor = RutaImagenPerfil;
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView = navigationView.getHeaderView(0);

        imgProfile = headView.findViewById(R.id.foto_perfil_hamburguesa);
        imgLogo_empresa = findViewById(R.id.logo_empresa);

        if (img_ruta_servidor.equals("")) {

        } else {
            Picasso.with(getApplicationContext()).load(img_ruta_servidor).into(imgProfile);
        }

        if (RutaLogoEmpresa.equals("")) {

        } else {
            Picasso.with(getApplicationContext()).load(RutaLogoEmpresa).into(imgLogo_empresa);
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        String url = getString(R.string.Url) + RutaImagenPerfil;


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

    }

    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
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
        layoutCerrar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_notificaciones()).addToBackStack(null).commit();
    }

    public void ventas() {
        layoutCerrar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Ventas()).addToBackStack(null).commit();
    }

    public void clientes() {
        layoutCerrar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_clientes()).addToBackStack(null).commit();
    }

    public void ecomerce() {

        Context context = this;
        SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", context.MODE_PRIVATE);

        String UserML = sharedPref.getString("UserIdML", "");
        String AccesToken = sharedPref.getString("AccessToken", "");
        String TokenLife = sharedPref.getString("TokenLifetime", "");
        String FechaCreacion = sharedPref.getString("FechaCreacionToken", "");


        if (AccesToken.length() == 0) {
            SharedPreferences sharedPref1 = getSharedPreferences(context.getPackageName() + ".identity", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor =  sharedPref1.edit();

            editor.putString("user_id", null);
            editor.apply();

            Meli.startLogin( this, REQUEST_CODE );
        }
        else
        {
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_ecomerce()).addToBackStack(null).commit();
                layoutCerrar.setVisibility(View.VISIBLE);
                btn_cerrar_ecommerce = findViewById(R.id.btn_cerrar_ecommerce);
                btn_cerrar_ecommerce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cerrar.setContentView(R.layout.pop_up_cerrarsesion_ecommerce);
                        cerrar.show();
                        Button btn_aceptar = cerrar.findViewById(R.id.Aceptar);
                        btn_aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                SharedPreferences sharedPref = getSharedPreferences("DatosPersistentes", getApplicationContext().MODE_PRIVATE);

                                SharedPreferences.Editor editor =  sharedPref.edit();

                                editor.putString("UserIdML", null);
                                editor.putString("AccessToken", null);
                                editor.putString("TokenLifetime", null);
                                editor.putString("FechaCreacionToken", null);
                                editor.apply();


                                SharedPreferences sharedPref2 = getSharedPreferences(getApplicationContext().getPackageName() + ".identity", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor1 =  sharedPref2.edit();

                                editor1.putString("user_id", null);
                                editor1.apply();


                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_pantalla_principal()).addToBackStack(null).commit();
                                layoutCerrar.setVisibility(View.GONE);
                                cerrar.dismiss();

                            }
                        });
                        Button btn_cancelar = cerrar.findViewById(R.id.Cancelar);
                        btn_cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cerrar.dismiss();
                            }
                        });



                    }
                });
            }
            else
            {
                SharedPreferences sharedPref1 = getSharedPreferences(context.getPackageName() + ".identity", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor =  sharedPref1.edit();

                editor.putString("user_id", null);
                editor.apply();

                Meli.startLogin( this, REQUEST_CODE );

            }

        }

    }

    public void home() {
        layoutCerrar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_pantalla_principal()).addToBackStack(null).commit();

    }

    public void inventario() {
        layoutCerrar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_inventarios()).addToBackStack(null).commit();
    }

    public void lealtad() {
        layoutCerrar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLealtad()).addToBackStack(null).commit();
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
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).addToBackStack(null).commit();
    }

    public void Aceptar(View view) {
        ProgressDialog progreso;
        progreso = new ProgressDialog(this);
        progreso.setMessage("Cerrando sesion...");
        progreso.show();
        Intent intent = new Intent(Home.this, SplashScreen.class);
        startActivity(intent);
        finish();

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
                            /*System.out.println(img_ruta_servidor);
                            Picasso.with(getApplicationContext()).load(img_ruta_servidor).into(imgProfile);
                            //Picasso.with( getApplicationContext() ).load( img_ruta_servidor ).into( img_pantalla_principal);*/

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
                    new Fragment_ecomerce()).addToBackStack(null).commit();
            layoutCerrar.setVisibility(View.VISIBLE);


        }
    }

    private void processLoginProcessWithError() {
        Toast.makeText(this, "Oooops, something went wrong with the login process", Toast.LENGTH_SHORT).show();
    }

    protected void onStart(){
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver,intentFilter);
    }

    protected void onStop(){
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(wifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiStateExtra){
                case WifiManager.WIFI_STATE_ENABLED:

                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Internet.setVisibility(View.VISIBLE);
                    textInternet.setText("No te encuentras conectado a ninguna red");
                    break;

            }
        }
    };




}