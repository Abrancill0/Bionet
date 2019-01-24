package com.Danthop.bionet;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.Spinner;

import com.Danthop.bionet.R;

import com.nostra13.universalimageloader.core.ImageLoader;

public class RegistroDatosActivity extends FragmentActivity implements Fragment_pop_up_ProfilePhoto.OnPhotoSelectedListener {


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
        Spinner spinner = (Spinner) findViewById(R.id.Giro);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Giros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

    }

    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;

    public void avanzar(View view) {
        Intent intent = new Intent(RegistroDatosActivity.this, Numero_sucursal.class);
        startActivity(intent);
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


}
