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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class BienvenidaActivity extends Activity {

    private String UsuarioIntent;
    private String PassIntent;

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
