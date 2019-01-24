package com.Danthop.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Danthop.bionet.R;


public class BienvenidaActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bienvenida);
    }
    public void Datos(View view) {
        Intent intent = new Intent(BienvenidaActivity.this, RegistroDatosActivity.class);
        startActivity(intent);
    }

}
