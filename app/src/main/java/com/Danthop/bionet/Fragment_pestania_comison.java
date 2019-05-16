package com.Danthop.bionet;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ComisionesAdapter;
import com.Danthop.bionet.Adapters.CorteCajaAdapter;
import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.model.CorteCajaModel;
import com.Danthop.bionet.model.FormaspagoModel;
import com.Danthop.bionet.model.HistoricoModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Fragment_pestania_comison extends Fragment {
    private String[][] HistoricoModel;
    private SortableCorteCajaTable tabla_comisiones;
    private TableDataClickListener<HistoricoModel> tablaListener;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private FragmentTransaction fr;
    private String suc_id;
    private String usu_id;
    private String cca_id_sucursal;
    private String valueIdSuc;
    private String nombrevendedor;
    private Double montoacomulado;
    private Double montopagado;
    private Double importe_pendiente;
    private String tic_id;
    private List<CorteCajaModel> ListComisiones;
    private List<CorteCajaModel> comision;
    private String id;
    private String cde_id;
    private String id_sucursales;
    private Button btnactualizar;
    ProgressDialog progreso;
    private Dialog dialog;

    public Fragment_pestania_comison() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pestania_comisiones, container, false);
        fr = getFragmentManager().beginTransaction();

        btnactualizar = (Button)v.findViewById(R.id.btn_generarcorte);
        progreso = new ProgressDialog(getContext());

        Button Ventas_btn = (Button) v.findViewById(R.id.btnventas);
        Ventas_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_Ventas()).commit();
            }
        });

        Button btn_pestania_reporte = (Button) v.findViewById(R.id.btntransacciones);
        btn_pestania_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_transacciones()).commit();
            }
        });

        Button corte_caja = (Button) v.findViewById(R.id.btncortecaja);
        corte_caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_cortecaja()).commit();
            }
        });


       Button Comisiones = v.findViewById( R.id.Comisiones);
        Comisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_comison()).commit();
            }
        });

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");

        ListComisiones = new ArrayList<>();
        comision =new ArrayList<>();




        try {
         JSONArray jsonArray = new JSONArray(cca_id_sucursal);
         //for (int i = 0; i < jsonArray.length(); i++) {
             JSONObject JsonObj = jsonArray.getJSONObject(0);
             Iterator<String> iter = JsonObj.keys();
             while (iter.hasNext()) {
                 String key = iter.next();
                 valueIdSuc = String.valueOf(JsonObj.get(key));
             }
        //}
     } catch (JSONException e) {
         Toast toast1 =
                 Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
         toast1.show();
     }



        tabla_comisiones = (SortableCorteCajaTable) v.findViewById(R.id.tabla_comisiones);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Nombre Vendedor", "Monto Acumulado", "Monto Pagado", "Monto Pendiente","");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2 );



        tabla_comisiones.setHeaderAdapter(simpleHeader);
        tabla_comisiones.setColumnModel(tableColumnWeightModel);


        btnactualizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadComisiones();
            }
        });




        return v;
    }


    public void LoadComisiones(){
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Espere por Favor...");
        progreso.show();

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ventas/comisiones/index";


        JSONObject jsonBodyrequest = new JSONObject();
        try {
            jsonBodyrequest.put("esApp", "1" );
            jsonBodyrequest.put("usu_id", usu_id);
            jsonBodyrequest.put("tic_id_sucursal",valueIdSuc);


        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,jsonBodyrequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Resultado = null;
                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");


                    if (status == 1){
                        Resultado = response.getJSONArray( "resultado" );

                        for (int n=0; n<Resultado.length(); n++){
                            JSONObject elemento = Resultado.getJSONObject(n);

                            tic_id = elemento.getString( "tic_id_vendedor" );
                            nombrevendedor = elemento.getString("tic_nombre_vendedor" );
                            montoacomulado = elemento.getDouble("tic_importe_comision" );
                            montopagado = elemento.getDouble("tic_importe_comision_total_pagado");
                            //importe_pendiente = elemento.getDouble( "importe_pendiente" );

                            final CorteCajaModel comisiones = new CorteCajaModel(
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",0.0, 0.0, 0.0, 0.0,
                                    "","","",0.0,0.0,0.0,0.0,0.0,
                                    nombrevendedor,montoacomulado,montopagado,0.0);

                            ListComisiones.add(comisiones);

                        }



                        final ComisionesAdapter comisionAdapter = new ComisionesAdapter(getContext(), ListComisiones, tabla_comisiones);
                        tabla_comisiones.setDataAdapter(comisionAdapter);
                        progreso.hide();


                    }else {
                        Toast toast1 = Toast.makeText(getContext(),"No existen comisiones para la sucursal", Toast.LENGTH_LONG);
                        toast1.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progreso.hide();
                        Toast toast1 = Toast.makeText(getContext(), "Error de conexion", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
        );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }


   /* public void PagarComisiones(){

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ventas/comisiones/pagar-comision";


        JSONObject jsonBodyrequest = new JSONObject();
        try {
            jsonBodyrequest.put("esApp", "1" );
            jsonBodyrequest.put("usu_id", usu_id);
            jsonBodyrequest.put("tic_id_sucursal",valueIdSuc);
            jsonBodyrequest.put("importe_pagar","");
            jsonBodyrequest.put("tic_id_vendedor",tic_id);


        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,jsonBodyrequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Resultado = null;
                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");


                    if (status == 1){
                        Resultado = response.getJSONArray( "resultado" );

                        for (int n=0; n<Resultado.length(); n++){
                            JSONObject elemento = Resultado.getJSONObject(n);



                        }
                        final CorteCajaModel comisiones = new CorteCajaModel(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",0.0, 0.0, 0.0, 0.0,
                                "","","",0.0,0.0,0.0,0.0,0.0,
                                "",0.0,0.0,0.0);

                        ListComisiones.add(comisiones);



                        final ComisionesAdapter comisionAdapter = new ComisionesAdapter(getContext(), ListComisiones, tabla_comisiones);
                        tabla_comisiones.setDataAdapter(comisionAdapter);
                        progreso.hide();


                    }else {
                        Toast toast1 = Toast.makeText(getContext(),"No existen comisiones para la sucursal", Toast.LENGTH_LONG);
                        toast1.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progreso.hide();
                        Toast toast1 = Toast.makeText(getContext(), "Error de conexion", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
        );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }*/






}