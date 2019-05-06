package com.Danthop.bionet;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.CorteCajaAdapter;
import com.Danthop.bionet.Adapters.ListaCajaAdapter;
import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.Tables.SortableHistoricoTable;
import com.Danthop.bionet.model.CorteCajaModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ventas_corte_caja_listado extends Fragment {

    Button pestania_ventas;
    Button pestania_reporte;
    Button btn_corte;
    Button btn_listado_corte;
    Button btn_factura_ventas;
    private EditText Fechainicio;
    private EditText Fechafin;
    private String FechaInicio;
    private String FechaFin;
    private String usu_id;
    private String cca_id_sucursal;
    private String valueIdSuc;
    private String cca_nombre_usuario;
    private String cca_importe_total;
    private String fecha;
    private String hora;
    private List<CorteCajaModel> ListaCorte;
    private SortableCorteCajaTable tabla_Listarcorte;




    private DatePickerDialog.OnDateSetListener inicioDataSetlistener;
    private DatePickerDialog.OnDateSetListener finDataSetlistener;
    private SortableHistoricoTable tabla_historico;



    public Fragment_ventas_corte_caja_listado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas_corte_caja_listado,container, false);
        pestania_ventas = v.findViewById(R.id.Ventas_btn);
        pestania_reporte = v.findViewById(R.id.btn_pestania_reporte);
        btn_corte = v.findViewById(R.id.btn_corte);
        btn_listado_corte = v.findViewById(R.id.btn_listado_corte);
        btn_factura_ventas = v.findViewById(R.id.btn_factura_ventas);
        Fechainicio=(EditText) v.findViewById(R.id.btnfechainicio);
        Fechafin=(EditText) v.findViewById(R.id.btnfechafin);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");
        ListaCorte = new ArrayList<>();


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

        tabla_Listarcorte = (SortableCorteCajaTable) v.findViewById(R.id.tabla_listacorte);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Usuario", "Monto Total", "Efectivo(01)", "Fecha", "Hora");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 1);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(4, 1);

        tabla_Listarcorte.setHeaderAdapter(simpleHeader);
        tabla_Listarcorte.setColumnModel(tableColumnWeightModel);

        Fechainicio.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar Fechainicio =Calendar.getInstance();
                int Year = Fechainicio.get(Calendar.YEAR);
                int Month = Fechainicio.get(Calendar.MONTH);
                int Day = Fechainicio.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog( getContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth, inicioDataSetlistener,Year,Month,Day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        inicioDataSetlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String dia="";
                String mes="";
                String ano="";

                if(dayOfMonth<10) {
                    dia = "0" + dayOfMonth;
                }
                else {
                    dia = String.valueOf( dayOfMonth );
                }

                if(month+1<10) {
                    mes = "0" + (month+1);
                }
                else
                {
                    mes = String.valueOf( month+1 );
                }
                FechaInicio = year + "/" + mes + "/" + dia;
                Fechainicio.setText( FechaInicio );
            }
        };

        Fechafin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar Fechafin =Calendar.getInstance();
                int Year = Fechafin.get(Calendar.YEAR);
                int Month = Fechafin.get(Calendar.MONTH);
                int Day = Fechafin.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog( getContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth, finDataSetlistener,Year,Month,Day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        finDataSetlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String dia="";
                String mes="";
                String ano="";

                if(dayOfMonth<10) {
                    dia = "0" + dayOfMonth;
                }
                else {
                    dia = String.valueOf( dayOfMonth );
                }

                if(month+1<10) {
                    mes = "0" + (month+1);
                }
                else
                {
                    mes = String.valueOf( month+1 );
                }
                FechaFin= year + "/" + mes + "/" + dia;
                Fechafin.setText( FechaFin );
            }
        };



        loadButtons();
        return v;
    }

    public void loadButtons(){
        pestania_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_Ventas()).commit();
            }
        });
        pestania_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_transacciones()).commit();
            }
        });

        btn_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_cortecaja()).commit();
            }
        });

        btn_listado_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enlistar_corte();
            }
        });
    }

    public void Enlistar_corte(){

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ventas/cortes/lista-cortes";

        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("cca_id_sucursal", valueIdSuc);
            request.put("esApp", "1");
            request.put("fecha_inicial",FechaInicio);
            request.put("fecha_final",FechaFin);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest posRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject Resultado = null;
                JSONArray aCortes = null;


                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Resultado = response.getJSONObject("resultado");
                        aCortes = Resultado.getJSONArray("aCortes");
                            for (int y = 0; y < aCortes.length(); y++){
                                JSONObject elemento = aCortes.getJSONObject(y);
                                cca_nombre_usuario = elemento.getString("cca_nombre_usuario");
                                cca_importe_total = elemento.getString("cca_importe_total");
                                fecha = elemento.getString("cca_fecha_creo");
                                hora = elemento.getString("cca_hora_creo");





                                    final CorteCajaModel corte = new CorteCajaModel(
                                            "",
                                            "",
                                            "",
                                            "",
                                            cca_nombre_usuario,
                                            cca_importe_total,"",fecha,hora);
                                    ListaCorte.add(corte);

                            }

                        final ListaCajaAdapter ListacorteAdapter = new ListaCajaAdapter(getContext(), ListaCorte, tabla_Listarcorte);
                        tabla_Listarcorte.setDataAdapter(ListacorteAdapter);

                    }
                } catch (JSONException e) {
                    Toast toast1 = Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 = Toast.makeText(getContext(), "Error de conexion", Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( posRequest );
    }

}
