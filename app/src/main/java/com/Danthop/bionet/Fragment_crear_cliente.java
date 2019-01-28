package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.Danthop.bionet.R;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_crear_cliente extends DialogFragment {

    Dialog DireccionFiscal;
    public Fragment_crear_cliente() {
        // Required empty public constructor
    }

    ProgressDialog progreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_cliente,container, false);
        Button guardar = (Button) v.findViewById(R.id.verificar_fiscal);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pop_up_direccion_fiscal dialog = new Fragment_pop_up_direccion_fiscal();
                dialog.setTargetFragment(Fragment_crear_cliente.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });
        return v;
    }


    private void GuardarCliente(){

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Iniciando sesion...");
        progreso.show();

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_usuario", "");
            request.put("usu_contrasena", "");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = "https://citycenter-rosario.com.ar/usuarios/loginApp";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                Intent intent = new Intent(getContext(), Home.class);
                startActivity(intent);

                progreso.hide();

                JSONObject Respuesta = null;

                try {

                    Respuesta = response.getJSONObject("resultado");

                    Toast toast1 =
                            Toast.makeText(getContext(),
                                    "Cliente guardado correctamente", Toast.LENGTH_SHORT);

                    toast1.show();

                } catch (JSONException e) {
                    progreso.hide();
                }


            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        progreso.hide();

                        Toast toast1 =
                                Toast.makeText(getContext(),
                                        "Error de conexion", Toast.LENGTH_SHORT);

                        toast1.show();

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }



}