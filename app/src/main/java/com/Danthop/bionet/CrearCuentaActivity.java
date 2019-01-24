package com.Danthop.bionet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Danthop.bionet.R;


public class CrearCuentaActivity extends Activity {

    Dialog terminos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);
        terminos = new Dialog(this);
    }

    public void ActivarLicencia(View view) {
        Intent intent = new Intent(CrearCuentaActivity.this, ActivarLicenciaActivity.class);
        startActivity(intent);
    }

    public void ShowTerminos(View view) {
        terminos.setContentView(R.layout.pop_up_condiciones);
        terminos.show();
    }

    public void Bienvenido(View view) {
        Intent intent = new Intent(CrearCuentaActivity.this, BienvenidaActivity.class);
        startActivity(intent);
    }



}
