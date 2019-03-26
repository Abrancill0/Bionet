package com.Danthop.bionet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.android.volley.Request.Method;

public class CrearCuentaActivity extends Activity {

    private EditText Usuario;
    private EditText Password1;
    private EditText Password2;
    private CheckBox AceptaTerminos;
    ProgressDialog progreso;
    Dialog terminos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);
        terminos = new Dialog(this);

        Usuario =(EditText) findViewById(R.id.text_new_usuario);
        Password1 =(EditText) findViewById(R.id.text_password);
        Password2 =(EditText) findViewById(R.id.text_repassword);

       //View condiciones = R.layout.pop_up_condiciones;

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

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        String email = String.valueOf(Usuario.getText());

        Matcher mather = pattern.matcher(email);

        if (mather.find() == false) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "El email ingresado es inválido.", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }

        if(Password1.getText().length()==0) {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Campo contrasena obligatorio ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }

        String pass1= String.valueOf(Password1.getText());
        String pass2= String.valueOf(Password2.getText());

        if(!pass1.equals(pass2)) {

            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Las contrasenas debe de coincidir ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }


        terminos.setContentView(R.layout.pop_up_condiciones);
        terminos.show();

    }

    public void Bienvenido(View view) {

        AceptaTerminos =(CheckBox) terminos.findViewById(R.id.check_condiciones);


        if (AceptaTerminos.isChecked())
        {
        GuardarDatos();

        }
        else
        {
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Debe aceptar los terminos y condiciones ", Toast.LENGTH_SHORT);

            toast1.show();

            return;
        }


    }

    private void GuardarDatos(){

        progreso = new ProgressDialog(this);
        progreso.setMessage("procesando...");
        progreso.show();


        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_correo_electronico", Usuario.getText());
            request.put("usu_contrasenia", Password1.getText());
            request.put("cbn_politica_privacidad", "true");
            request.put("cbn_terminos_condiciones", "true");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/cuentas/store";

        JsonObjectRequest postRequest = new JsonObjectRequest(Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject Respuesta = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");
                        String IDUsuario =Respuesta.getString("usu_id");

                        Intent intent = new Intent(CrearCuentaActivity.this, BienvenidaActivity.class);
                        intent.putExtra("IDUsuario", IDUsuario);
                        startActivity(intent);

                        progreso.hide();
                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                        progreso.hide();

                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);

                    toast1.show();

                    progreso.hide();
                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();

                        progreso.hide();
                    }
                }
        );

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);

    }


}
