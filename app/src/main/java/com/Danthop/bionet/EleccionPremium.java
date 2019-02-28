package com.Danthop.bionet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.Danthop.bionet.R;


public class EleccionPremium extends Activity {

    private String IDUsuario;
    private TextView scrollText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eleccion_premium);
        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");
        //scrollText = findViewById(R.id.Text_scroll);
       // scrollText.setMovementMethod(new ScrollingMovementMethod());

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
