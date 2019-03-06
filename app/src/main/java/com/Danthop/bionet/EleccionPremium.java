package com.Danthop.bionet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.Danthop.bionet.R;


public class EleccionPremium extends Activity {

    private String IDUsuario;
    private TextView scrollText;
    private RadioButton Commerce_Premium;
    private RadioButton Commerce_Pro;
    private RadioButton Bussines_Premium;
    private RadioButton Bussines_Pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eleccion_premium);
        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");
        Commerce_Premium = findViewById(R.id.Commerce_Premium);
        Commerce_Pro = findViewById(R.id.Commerce_Pro);
        Bussines_Premium = findViewById(R.id.Bussines_Premium);
        Bussines_Pro = findViewById(R.id.Bussines_Pro);

        Commerce_Premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commerce_Premium.setChecked(true);
                Commerce_Pro.setChecked(false);
                Bussines_Premium.setChecked(false);
                Bussines_Pro.setChecked(false);

            }
        });
        Commerce_Pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commerce_Pro.setChecked(true);
                Commerce_Premium.setChecked(false);
                Bussines_Premium.setChecked(false);
                Bussines_Pro.setChecked(false);

            }
        });
        Bussines_Premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bussines_Premium.setChecked(true);
                Commerce_Premium.setChecked(false);
                Commerce_Pro.setChecked(false);
                Bussines_Pro.setChecked(false);

            }
        });
        Bussines_Pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bussines_Pro.setChecked(true);
                Commerce_Premium.setChecked(false);
                Commerce_Pro.setChecked(false);
                Bussines_Premium.setChecked(false);

            }
        });


    }


    public void Numero_sucursal(View view) {
        Intent intent = new Intent(EleccionPremium.this, Numero_sucursal.class);
        intent.putExtra("IDUsuario", IDUsuario);
        startActivity(intent);
    }

    public void mas_informacion(View view){
        String url = "http://www.bionetpos.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
