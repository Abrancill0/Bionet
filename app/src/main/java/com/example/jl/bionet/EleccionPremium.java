package com.example.jl.bionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EleccionPremium extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eleccion_premium);
    }

    public void Home(View view) {
        Intent intent = new Intent(EleccionPremium.this, Home.class);
        startActivity(intent);
    }
}
