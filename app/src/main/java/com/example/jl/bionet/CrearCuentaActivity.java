package com.example.jl.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CrearCuentaActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);
    }

    public void ActivarLicencia(View view) {
        Intent intent = new Intent(CrearCuentaActivity.this, ActivarLicenciaActivity.class);
        startActivity(intent);
    }

    public void Bienvenido(View view) {
        Intent intent = new Intent(CrearCuentaActivity.this, BienvenidaActivity.class);
        startActivity(intent);
    }

}
