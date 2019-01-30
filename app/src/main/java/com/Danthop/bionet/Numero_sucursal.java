package com.Danthop.bionet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Numero_sucursal extends Activity {

    private EditText nombre_sucursal;
    private EditText telefono_sucursal;
    private EditText correo_sucursal;
    private EditText direccion_sucursal;
    private static final String[] TABLA1_HEADERS = { "Nombre de la sucursal", "Teléfono", "Correo", "Dirección" };
    private static final String[] TABLA2_HEADERS = { "No.","Nombre de la sucursal", "Teléfono", "Correo", "Dirección" };
    private String IDUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_sucursales);

        Bundle datos = this.getIntent().getExtras();
        IDUsuario =  "" + datos.get("IDUsuario");

       // NombreSucursales = (EditText) findViewById(R.id.Text_Nombre_Sucursal);
        TableView tableView2 = (TableView) findViewById(R.id.tabla2);

        tableView2.setHeaderBackgroundColor(getResources().getColor(R.color.white));
        tableView2.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLA2_HEADERS));

    }

    public void eleccion_premium(View view) {

       // GuardarDatos();
        Intent intent = new Intent(Numero_sucursal.this, EleccionPremium.class);
        startActivity(intent);
        }


    private void GuardarDatos(){

        //progreso = new ProgressDialog(this);
        // progreso.setMessage("Iniciando sesion...");
        // progreso.show();


        JSONObject request = new JSONObject();
        try
        {
            request.put("suc_nombre", nombre_sucursal.getText());
            request.put("suc_id_emisor", "0");
            request.put("suc_telefono", telefono_sucursal.getText());
            request.put("suc_correo_electronico", correo_sucursal.getText());
            request.put("con_propinas", "false");
            request.put("suc_calle", direccion_sucursal.getText());
            request.put("suc_numero_interior", "0");
            request.put("suc_numero_exterior", "0");
            request.put("suc_colonia", "");
            request.put("suc_ciudad", "");
            request.put("suc_codigo_postal", "");
            request.put("suc_id_pais", "117");
            request.put("suc_estado", "");
            request.put("suc_id_estado", "0");
            request.put("suc_pais", "Mexico");
            request.put("usu_id", IDUsuario);
            request.put("esApp", "1");


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url); //"https://citycenter-rosario.com.ar/usuarios/loginApp";

        String ApiPath = url + "/api/configuracion/sucursales/store";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                      //  Respuesta = response.getJSONObject("resultado");

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();

                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();

                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);

    }

    public void crear_sucursal (View v){
        final Dialog crear_sucursal_dialog=new Dialog(Numero_sucursal.this);

        crear_sucursal_dialog.setContentView(R.layout.pop_up_crear_sucursal);
        crear_sucursal_dialog.show();

        Button crear = (Button) crear_sucursal_dialog.findViewById(R.id.btn_crear_sucursal);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombre_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_nombre_sucursal);
                telefono_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_telefono_sucursal);
                correo_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_correo_sucursal);
                direccion_sucursal = (EditText) crear_sucursal_dialog.findViewById(R.id.Text_direccion_sucursal);

                GuardarDatos();


                crear_sucursal_dialog.dismiss();
            }
        });
    }


}
