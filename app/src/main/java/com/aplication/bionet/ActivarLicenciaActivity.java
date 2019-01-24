package com.aplication.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aplication.bionet.R;

public class ActivarLicenciaActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activar_licencia);
    }
    public void Regresar(View view) {
        Intent intent = new Intent(ActivarLicenciaActivity.this, CrearCuentaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void Aceptar(View view) {
        Intent intent = new Intent(ActivarLicenciaActivity.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

}
