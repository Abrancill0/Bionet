package com.Danthop.bionet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CrearCuentaActivity extends Activity {

    private EditText Usuario;
    private EditText Password1;
    private EditText Password2;
    private CheckBox AceptaTerminos;

    Dialog terminos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);
        terminos = new Dialog(this);

        Usuario =(EditText) findViewById(R.id.text_new_usuario);
        Password1 =(EditText) findViewById(R.id.text_password);
        Password2 =(EditText) findViewById(R.id.text_repassword);
        AceptaTerminos =(CheckBox) findViewById(R.id.check_condiciones);

    }

    public void ActivarLicencia(View view) {
        Intent intent = new Intent(CrearCuentaActivity.this, ActivarLicenciaActivity.class);
        startActivity(intent);
    }

    public void ShowTerminos(View view) {

        if(Usuario.getText().length()==0) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Campo usuario obligatorio ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }

        if(Password1.getText().length()==0) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Campo contrasena obligatorio ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }

        if(Password1.getText() != Password2.getText()) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Las contrasenas debe de coincidir ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }


        terminos.setContentView(R.layout.pop_up_condiciones);
        terminos.show();

    }

    public void Bienvenido(View view) {

        if (AceptaTerminos.isChecked())
        {
            Intent intent = new Intent(CrearCuentaActivity.this, BienvenidaActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Debe aceptar los terminos y condiciones ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }


    }



}
