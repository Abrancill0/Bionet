package com.Danthop.bionet;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ventas_reporte_ventas extends Fragment {

    private View layout_movimientos;
    private View layout_apartado;
    private View layout_ordenes;
    private View layout_invisible;

    private View btn_movimientos;
    private View btn_apartado;
    private View btn_ordenes_especiales;

    private TextView pestania_movimientos;
    private TextView pestania_apartado;
    private TextView pestania_ordenes_especiales;


    private Button btn_corte_caja;
    private Button btn_ventas;

    private String usu_id;

    private Spinner SpinnerSucursalMovimientos;
    private ArrayList<String> SucursalNameMovimientos;
    private ArrayList<String> SucursalIDMovimientos;

    private Spinner SpinnerSucursalApartado;
    private ArrayList<String> SucursalNameApartado;
    private ArrayList<String> SucursalIDApartado;

    private Spinner SpinnerSucursalOrdenes;
    private ArrayList<String> SucursalNameOrdenes;
    private ArrayList<String> SucursalIDOrdenes;

    private CalendarPickerView calendarView;
    private Button Abrir_calendarioApartado1;
    private Button Abrir_calendarioApartado2;
    private Date FechaApartado1Formato;
    private String FechaApartado1;
    private String FechaApartado2;

    private Button Abrir_calendarioOrdenes1;
    private Button Abrir_calendarioOrdenes2;
    private Date FechaOrdenes1Formato;
    private String FechaOrdenes1;
    private String FechaOrdenes2;




    private FragmentTransaction fr;


    public Fragment_ventas_reporte_ventas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas_reporte,container, false);
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
        pestania_movimientos = v.findViewById(R.id.pestaniaMovimientos);
        pestania_apartado = v.findViewById(R.id.pestaniaApartados);
        pestania_ordenes_especiales = v.findViewById(R.id.pestaniaOrdenesEspeciales);

        Abrir_calendarioApartado1 = v.findViewById(R.id.abrir_calendarioApartado1);
        Abrir_calendarioApartado2 = v.findViewById(R.id.abrir_calendarioApartado2);

        Abrir_calendarioOrdenes1 = v.findViewById(R.id.abrir_calendarioOrdenes1);
        Abrir_calendarioOrdenes2 = v.findViewById(R.id.abrir_calendarioOrdenes2);


        SpinnerSucursalMovimientos=v.findViewById(R.id.sucursal_Movimientos);
        SpinnerSucursalApartado=v.findViewById(R.id.sucursal_Apartado);
        SpinnerSucursalOrdenes=v.findViewById(R.id.sucursal_Ordenes);

        SucursalNameMovimientos=new ArrayList<>();
        SucursalIDMovimientos = new ArrayList<>();

        SucursalNameApartado=new ArrayList<>();
        SucursalIDApartado= new ArrayList<>();

        SucursalNameOrdenes=new ArrayList<>();
        SucursalIDOrdenes= new ArrayList<>();

        btn_ventas = v.findViewById(R.id.btn_ventas);
        btn_corte_caja = v.findViewById(R.id.btn_corte_caja);

        loadFechas();
        loadSucursales();
        loadButtons();
        Layouts();

        return v;
    }

    public void Layouts()
    {
        layout_movimientos.setVisibility(View.GONE);
        layout_apartado.setVisibility(View.GONE);
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

                    layout_movimientos.setVisibility(View.VISIBLE);
                    layout_apartado.setVisibility(View.GONE);
                    layout_ordenes.setVisibility(View.GONE);
                    pestania_movimientos.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_arriba, 0, 0, 0);
                    pestania_apartado.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    pestania_ordenes_especiales.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    layout_movimientos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.5f));
                    layout_apartado.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.9f));
                    layout_ordenes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.9f));
                    layout_invisible.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.99f));

                }
                else
                {
                    layout_movimientos.setVisibility(View.GONE);
                    pestania_movimientos.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    layout_movimientos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_apartado.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_ordenes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_invisible.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.5f));

                }
            }
        });

        btn_apartado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_apartado.getVisibility()==View.GONE)
                {

                    layout_apartado.setVisibility(View.VISIBLE);
                    layout_movimientos.setVisibility(View.GONE);
                    layout_ordenes.setVisibility(View.GONE);
                    pestania_apartado.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_arriba, 0, 0, 0);
                    pestania_movimientos.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    pestania_ordenes_especiales.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    layout_movimientos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.9f));
                    layout_apartado.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.4f));
                    layout_ordenes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.9f));
                    layout_invisible.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.99f));

                }
                else
                {
                    layout_apartado.setVisibility(View.GONE);
                    pestania_apartado.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    layout_movimientos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_apartado.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_ordenes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_invisible.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.5f));

                }
            }
        });

        btn_ordenes_especiales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_ordenes.getVisibility()==View.GONE)
                {

                    layout_ordenes.setVisibility(View.VISIBLE);
                    layout_apartado.setVisibility(View.GONE);
                    layout_movimientos.setVisibility(View.GONE);
                    pestania_ordenes_especiales.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_arriba, 0, 0, 0);
                    pestania_apartado.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    pestania_movimientos.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    layout_movimientos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.9f));
                    layout_apartado.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.9f));
                    layout_ordenes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.4f));
                    layout_invisible.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.99f));

                }
                else
                {
                    layout_ordenes.setVisibility(View.GONE);
                    pestania_ordenes_especiales.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_flecha_abajo, 0, 0, 0);
                    layout_movimientos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_apartado.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_ordenes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
                    layout_invisible.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.5f));

                }
            }
        });

    }


    public void loadSucursales(){
        SucursalesMovimientos();
        SucursalesApartado();
        SucursalesOrdenes();
        loadSpinnersListeners();
    }


    public void SucursalesMovimientos(){

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
                            SucursalNameMovimientos.add(sucursal);
                            RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                            String id=RespuestaNodoID.getString("uuid");
                            SucursalIDMovimientos.add(id);
                        }
                        SpinnerSucursalMovimientos.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalNameMovimientos));

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


    public void SucursalesApartado()
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
                            SucursalNameApartado.add(sucursal);
                            RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                            String id=RespuestaNodoID.getString("uuid");
                            SucursalIDMovimientos.add(id);
                        }
                        SpinnerSucursalApartado.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalNameApartado));

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


    public void SucursalesOrdenes()
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
                            SucursalNameOrdenes.add(sucursal);
                            RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                            String id=RespuestaNodoID.getString("uuid");
                            SucursalIDMovimientos.add(id);
                        }
                        SpinnerSucursalOrdenes.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalNameOrdenes));

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


    public void loadSpinnersListeners()
    {
        SpinnerSucursalMovimientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        SpinnerSucursalApartado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        SpinnerSucursalOrdenes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public void loadFechas()
    {
        Abrir_calendarioApartado1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog calendario = new Dialog(getContext());
                calendario.setContentView(R.layout.calendar);
                calendario.show();
                calendarView = calendario.findViewById(R.id.calendar_view);

                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);

                Date today = new Date();
                calendarView.init(today, nextYear.getTime())
                        .withSelectedDate(today);


                calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date date) {

                        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

                        FechaApartado1Formato = calendarView.getSelectedDate();
                        long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                        FechaApartado1 = targetFormat.format(s);
                        calendario.dismiss();
                        Abrir_calendarioApartado1.setText(FechaApartado1);
                    }

                    @Override
                    public void onDateUnselected(Date date) {

                    }
                });

            }
        });

        Abrir_calendarioApartado2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FechaApartado1Formato!=null) {
                    final Dialog calendario = new Dialog(getContext());
                    calendario.setContentView(R.layout.calendar);
                    calendario.show();
                    calendarView = calendario.findViewById(R.id.calendar_view);

                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, 1);

                    calendarView.init(FechaApartado1Formato, nextYear.getTime())
                            .withSelectedDate(FechaApartado1Formato);


                    calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(Date date) {

                            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

                            long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                            FechaApartado2 = targetFormat.format(s);
                            calendario.dismiss();
                            Abrir_calendarioApartado2.setText(FechaApartado2);
                        }

                        @Override
                        public void onDateUnselected(Date date) {

                        }
                    });
                }

            }
        });

        Abrir_calendarioOrdenes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog calendario = new Dialog(getContext());
                calendario.setContentView(R.layout.calendar);
                calendario.show();
                calendarView = calendario.findViewById(R.id.calendar_view);

                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);

                Date today = new Date();
                calendarView.init(today, nextYear.getTime())
                        .withSelectedDate(today);


                calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date date) {

                        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

                        FechaOrdenes1Formato = calendarView.getSelectedDate();
                        long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                        FechaOrdenes1 = targetFormat.format(s);
                        calendario.dismiss();
                        Abrir_calendarioOrdenes1.setText(FechaOrdenes1);
                    }

                    @Override
                    public void onDateUnselected(Date date) {

                    }
                });

            }
        });

        Abrir_calendarioOrdenes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FechaOrdenes1Formato!=null) {

                    final Dialog calendario = new Dialog(getContext());
                    calendario.setContentView(R.layout.calendar);
                    calendario.show();
                    calendarView = calendario.findViewById(R.id.calendar_view);

                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, 1);

                    calendarView.init(FechaOrdenes1Formato, nextYear.getTime())
                            .withSelectedDate(FechaOrdenes1Formato);


                    calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(Date date) {

                            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

                            long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                            FechaOrdenes2 = targetFormat.format(s);
                            calendario.dismiss();
                            Abrir_calendarioOrdenes2.setText(FechaOrdenes2);
                        }

                        @Override
                        public void onDateUnselected(Date date) {

                        }
                    });
                }

            }
        });
    }
}
