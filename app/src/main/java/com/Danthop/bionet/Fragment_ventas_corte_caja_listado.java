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
import android.text.format.Time;
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
import com.google.gson.JsonArray;
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
    private Button btn_buscar;
    private String FechaInicio;
    private String FechaFin;
    private String usu_id;
    private String cca_id_sucursal;
    private String valueIdSuc;
    private String cca_nombre_usuario;
    private String cca_importe_total;
    private Double efectivo01 = 0.0 ;
    private Double monedero05 = 0.0 ;
    private Double dineroelectronico06 = 0.0;
    private Double vales08 = 0.0 ;
    private String fecha;
    private String hora;
    private Double cca_importe_forma_pago;
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
        Fechainicio=(EditText) v.findViewById(R.id.btnfechainicio);
        Fechafin=(EditText) v.findViewById(R.id.btnfechafin);
        btn_buscar=(Button)v.findViewById(R.id.btn_buscar);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");
        ListaCorte = new ArrayList<>();

        Button Comisiones = v.findViewById( R.id.Comisiones);
        Comisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_comison()).commit();
            }
        });

        Button btn_factura_ventas = (Button) v.findViewById(R.id.btn_factura_ventas);
        btn_factura_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_lista_sinfactura()).commit();
            }
        });


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
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Usuario", "Monto Total", "Efectivo", "Monedero electrónico","Dinero electrónico(06)","Vales de despensa(08)","Fecha", "Hora");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(8);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(5, 2);
        tableColumnWeightModel.setColumnWeight(6, 1);
        tableColumnWeightModel.setColumnWeight(7, 1);

        tabla_Listarcorte.setHeaderAdapter(simpleHeader);
        tabla_Listarcorte.setColumnModel(tableColumnWeightModel);

        Time today = new Time( Time.getCurrentTimezone() );
        today.setToNow();
        int year = today.year;
        String dia;
        String mes;
        if(today.monthDay < 10){
            dia = "0" + today.monthDay;
        }else {
            dia = String.valueOf( today.monthDay );
        }
        if(today.month + 1 < 10){
            mes = "0" + (today.month + 1);
        }else {
            mes = String.valueOf(today.month + 1);

        }

        String fechausuario = (dia + "/" + mes + "/" + year );
        Fechainicio.setText( fechausuario );
        FechaInicio = year + "/" + mes + "/" + dia;

        String fechausuariofin = (dia + "/" + mes + "/" + year );
        Fechafin.setText( fechausuariofin );
        FechaFin = year + "/" + mes + "/" + dia;

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

                String fechausuario = dia + "/" + mes + "/" + year;
                FechaInicio = year + "/" + mes + "/" + dia;
                Fechainicio.setText( fechausuario );
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
                String fechausuariofin = dia + "/" + mes + "/" + year;
                FechaFin= year + "/" + mes + "/" + dia;
                Fechafin.setText( fechausuariofin );
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

            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultado = FechaInicio.compareTo(FechaFin);
                if (resultado == 0 || resultado < 0) {

                    if (resultado < 0) {

                        Enlistar_corte();

                    } else {
                        Toast toast1 = Toast.makeText(getContext(), "Fecha de inicio debe ser diferente a la fecha final", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }
                else {
                    Toast toast1 = Toast.makeText(getContext(), "Fecha de inicio debe ser menor a la de final", Toast.LENGTH_SHORT);
                    toast1.show();
                }
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
                JSONArray aFormasPago = null;
                JSONObject forma_pago = null;


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

                                Double importe_total = elemento.getDouble( "cca_importe_total" );
                                if (importe_total != 0) {

                                    //forma_pago = elemento.getJSONObject( "cca_importe_forma_pago" );
                                    JSONObject songs= elemento.getJSONObject("cca_importe_forma_pago");
                                    Iterator x = songs.keys();

                                    efectivo01=0.0;
                                    monedero05=0.0;
                                    dineroelectronico06=0.0;
                                    vales08=0.0;

                                    while (x.hasNext() ){

                                            String key = (String) x.next();

                                            int Resultado01 = key.compareTo("01");

                                            if (Resultado01 == 0 ){
                                                efectivo01 = songs.getDouble( "01" );
                                            }

                                            int Resultado05 = key.compareTo("05");

                                            if (Resultado05 == 0 ){
                                                monedero05 = songs.getDouble( "05" );
                                            }


                                            int Resultado06 = key.compareTo("06");

                                            if (Resultado06 == 0 ){
                                                dineroelectronico06 = songs.getDouble( "06" );
                                            }


                                            int Resultado08 = key.compareTo("08");

                                            if (Resultado08 == 0 ){
                                                vales08 = songs.getDouble( "08" );
                                            }


                                        }



                                }else {
                                    efectivo01=0.0;
                                    monedero05=0.0;
                                    dineroelectronico06=0.0;
                                    vales08=0.0;
                                }


                                final CorteCajaModel corte = new CorteCajaModel(
                                        "",
                                        "",
                                        "",
                                        "",
                                        cca_nombre_usuario,
                                        cca_importe_total,efectivo01,monedero05,dineroelectronico06,vales08,
                                        fecha,hora,"",0.0,0.0,0.0,0.0,0.0,
                                        "",0.0,0.0,0.0);
                                ListaCorte.add(corte);

                            }



                        final ListaCajaAdapter ListacorteAdapter = new ListaCajaAdapter(getContext(), ListaCorte, tabla_Listarcorte);
                        tabla_Listarcorte.setDataAdapter(ListacorteAdapter);

                    }else {
                        Toast toast1 = Toast.makeText(getContext(), "No existen cortes de caja para el periodo proporcionado", Toast.LENGTH_LONG);
                        toast1.show();
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
