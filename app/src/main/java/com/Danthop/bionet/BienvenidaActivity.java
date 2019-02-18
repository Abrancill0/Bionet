package com.Danthop.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;


public class BienvenidaActivity extends Activity {

    private String IDUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bienvenida);


    }
    public void Datos(View view) {
        Intent intent = new Intent(BienvenidaActivity.this, RegistroDatosActivity.class);
        intent.putExtra("IDUsuario", IDUsuario);
        startActivity(intent);

    }



}
