package com.Danthop.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.Danthop.bionet.R;


public class EleccionPremium extends Activity {

    private String IDUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eleccion_premium);
        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");
    }

    public void Numero_sucursal(View view) {
        Intent intent = new Intent(EleccionPremium.this, Numero_sucursal.class);
        intent.putExtra("IDUsuario", IDUsuario);
        startActivity(intent);
    }
}
