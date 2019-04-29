package com.Danthop.bionet;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Tables.SortableApartadoTable;
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
public class Fragment_ventas_transacciones extends Fragment {

    private View layout_movimientos;
    private View layout_apartado;
    private View layout_ordenes;
    private View layout_invisible;

    private View btn_movimientos;
    private View btn_apartado;
    private View btn_ordenes_especiales;

    private Button btn_corte_caja;
    private Button btn_ventas;

    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;

    private SortableApartadoTable TablaApartados;

    private String usu_id;

    private FragmentTransaction fr;


    public Fragment_ventas_transacciones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas_transacciones,container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        fr = getFragmentManager().beginTransaction();
        layout_movimientos = v.findViewById(R.id.layout_movimientos);
        layout_apartado = v.findViewById(R.id.layout_apartado);
        layout_ordenes = v.findViewById(R.id.layout_ordenes);
        layout_invisible = v.findViewById(R.id.layout_invisible);
        btn_movimientos = v.findViewById(R.id.btn_movimientos);
        btn_apartado = v.findViewById(R.id.btn_apartado);
        btn_ordenes_especiales = v.findViewById(R.id.btn_ordenes_especiales);

        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();
        SpinnerSucursal=(Spinner)v.findViewById(R.id.sucursal);


        TablaApartados = v.findViewById(R.id.tabla_apartados);
        TablaApartados.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        btn_ventas = v.findViewById(R.id.btn_ventas);
        btn_corte_caja = v.findViewById(R.id.btn_corte_caja);

        LoadSucursales();
        loadButtons();
        Layouts();

        return v;
    }

    public void Layouts()
    {
        layout_movimientos.setVisibility(View.GONE);
        layout_apartado.setVisibility(View.VISIBLE);
        layout_ordenes.setVisibility(View.GONE);

    }

    public void loadButtons(){
        btn_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new Fragment_Ventas()).commit();
            }
        });

        btn_corte_caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_caja()).commit();
            }
        });

        btn_movimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_movimientos.getVisibility()==View.GONE)
                {

                }
                else
                {

                }
            }
        });

        btn_apartado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_apartado.getVisibility()==View.GONE)
                {

                }
                else
                {

                }
            }
        });

        btn_ordenes_especiales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_ordenes.getVisibility()==View.GONE)
                {

                }
                else
                {

                }
            }
        });

    }

    public void LoadSucursales()
    {
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/configuracion/sucursales/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONArray RespuestaNodoSucursales= null;
                JSONObject RespuestaNodoID = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");
                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoSucursales = Respuesta.getJSONArray("aSucursales");

                        for(int x = 0; x < RespuestaNodoSucursales.length(); x++){
                            JSONObject jsonObject1=RespuestaNodoSucursales.getJSONObject(x);
                            String sucursal=jsonObject1.getString("suc_nombre");
                            SucursalName.add(sucursal);
                            RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                            String id=RespuestaNodoID.getString("uuid");
                            SucursalID.add(id);
                        }
                        SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));

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
                new Response.ErrorListener()
                {
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

    public void LoadApartados(){
        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/apartados/index?usu_id=" + usu_id + "&esApp=1&suc_id="+ SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject RespuestaResultado = response.getJSONObject("resultado");
                                    JSONArray ArticulosApartados = RespuestaResultado.getJSONArray("aApartados");
                                    for (int f=0; f<ArticulosApartados.length();f++)
                                    {
                                        JSONObject elemento = ArticulosApartados.getJSONObject(f);
                                        String Cliente = elemento.getString("cli_nombre");
                                        String Sucursal = elemento.getString("suc_nombre");
                                        String FechaDeApartado = elemento.getString("apa_fecha_hora_creo");
                                        String MontoPagado = elemento.getString("apa_importe_pagado");
                                        String MontoRestante = elemento.getString("apa_importe_restante");
                                        String FechaDeVencimiento = elemento.getString("apa_fecha_hora_vencimiento");

                                        JSONArray Articulos = elemento.getJSONArray("aArticulosApartados");
                                        for(int i= 0; i<Articulos.length(); i++)
                                        {
                                            String ArticuloApartado = elemento.getString("aar_nombre_articulo");
                                            JSONObject NodoID = elemento.getJSONObject("aar_id");
                                            String ArticuloIDApartado = NodoID.getString("uuid");
                                            JSONObject NodoIDVariante = elemento.getJSONObject("aar_id_variante");
                                            String ArticuloIDVariante = NodoIDVariante.getString("uuid");
                                            String CantidadApartada = elemento.getString("aar_cantidad");
                                        }

                                    }


                                }
                            } catch (JSONException e) {
                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                String.valueOf(e), Toast.LENGTH_LONG);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(error), Toast.LENGTH_LONG);
                        }
                    }
            );
            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }


    }

}
