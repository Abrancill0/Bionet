package com.Danthop.bionet;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pop_up_traslados extends DialogFragment {

    private String usu_id;
    private Integer id;
    private String suc_nombre;
    private ArrayList<String> SucursalID;
    private ArrayList<String> SucursalName;
    private Spinner SpinnerSucursal;
    private Spinner SpinnerSucursal2;

    public Fragment_pop_up_traslados() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pop_up_traslados, container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");
        SucursalID = new ArrayList<>();
        SucursalName = new ArrayList<>();
        SpinnerSucursal=(Spinner)v.findViewById(R.id.Sucursal_Origen);
        SpinnerSucursal2=(Spinner)v.findViewById(R.id.Sucursal_Destino);

        Button trasladar = (Button) v.findViewById(R.id.ImgAceptar);
        trasladar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pop_up_traslado_exitoso dialog = new Fragment_pop_up_traslado_exitoso();
                dialog.setTargetFragment(Fragment_pop_up_traslados.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
                onDestroyView();
            }
        });

        Button back = v.findViewById(R.id.btnSalir);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

        SpinnerSucursales();
        return v;
    }

    private void SpinnerSucursales() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/configuracion/sucursales/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONArray RespuestaSucursales = null;
                JSONObject RespuestaUUID = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {
                        Respuesta = response.getJSONObject("resultado");
                        RespuestaSucursales = Respuesta.getJSONArray("aSucursales");

                        for(int x = 0; x < RespuestaSucursales.length(); x++){
                            JSONObject elemento = RespuestaSucursales.getJSONObject(x);

                            RespuestaUUID = elemento.getJSONObject("suc_id");
                            String UUID = RespuestaUUID.getString("uuid");
                            SucursalID.add(UUID);

                            suc_nombre = elemento.getString("suc_nombre");
                            SucursalName.add(suc_nombre);

                        }
                        SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));
                        SpinnerSucursal2.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));
                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    }

                } catch (JSONException e) {
                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }


}