package com.example.jl.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivarLicenciaActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activar_licencia);
    }
    public void Regresar(View view) {
        Intent intent = new Intent(ActivarLicenciaActivity.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    public void Bienvenida(View view) {
        Intent intent = new Intent(ActivarLicenciaActivity.this, BienvenidaActivity.class);
        startActivity(intent);
    }

}
