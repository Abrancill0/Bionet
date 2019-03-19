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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.LealtadInscribirAdapter;
import com.Danthop.bionet.Tables.SortableLealtadConfiguracionesTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.ConfiguracionLealtadModel;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLealtadConfiguraciones extends Fragment{

    private SortableLealtadConfiguracionesTable tabla_programas;
    private FragmentTransaction fr;
    private Button Lealtad;
    private Button Inscribir;
    private Button Articulos;
    private Button Abrir_calendario;
    private Button vencen;
    private Button no_vencen;
    private View Puntos_vencen_dias;
    private CalendarPickerView calendarView;
    private String Fecha_vencimiento;
    private TextView Fecha;
    private View Fecha_puntos_vencen;
    private String usu_id;
    private Button Crear;


    private Spinner SpinnerSucursal;

    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;


    private List<ClienteModel> clientes;


    public FragmentLealtadConfiguraciones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lealtad_configuración,container, false);
        fr = getFragmentManager().beginTransaction();
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");
        clientes = new ArrayList<>();
        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();

        SpinnerSucursal=(Spinner)v.findViewById(R.id.Sucursal_lealtad);
        LoadSpinnerSucursal();

        tabla_programas = v.findViewById(R.id.tabla_programas);
        tabla_programas.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        Lealtad=v.findViewById(R.id.lealtad);
        Lealtad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtad()).commit();

            }
        });

        Inscribir=v.findViewById(R.id.inscribir);
        Inscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadInscribir()).commit();

            }
        });

        Articulos=v.findViewById(R.id.articulo);
        Articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadArticulo()).commit();

            }
        });

        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                LoadConfiguraciones();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        Puntos_vencen_dias=v.findViewById(R.id.layout_puntos_vencen_dias);

        LoadConfiguraciones();
        FechaVencimiento(v);





        return v;
    }

    private void LoadConfiguraciones(){
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("cpl_id_sucursal",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/programa-de-lealtad/select-clientes";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONArray NodoConfiguraciones;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");


                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        for(int x = 0; x < Respuesta.length(); x++){
                            NodoConfiguraciones = Respuesta.getJSONArray("aConfiguracionProgramaLealtad");
                            for(int y = 0; y < NodoConfiguraciones.length();y++)
                            {
                                JSONObject elemento = NodoConfiguraciones.getJSONObject(y);
                                String pesos1= elemento.getString("cpl_por_cada_importe");
                                String puntos1= elemento.getString("cpl_vale_puntos");
                                String puntos2= elemento.getString("cpl_por_cada_puntos");
                                JSONArray vale_importe = elemento.getJSONArray("cpl_vale_importe");
                                String pesos2= vale_importe.getString(1);
                                String pesosxpuntos= "Por cada $ "+ pesos1 + " equivalen a " + puntos1 +" puntos";
                                String puntosxpesos= "Por cada "+ puntos2 + " puntos equivalen a $ " + pesos2;
                                final ConfiguracionLealtadModel configuraciones = new ConfiguracionLealtadModel("d", pesosxpuntos,puntosxpesos);
                            }

                        }

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

    private void CrearConfiguracion()
    {

    }

    private void FechaVencimiento(View v)
    {
        vencen = v.findViewById(R.id.si_vencen);
        no_vencen = v.findViewById(R.id.no_vencen);
        Abrir_calendario = v.findViewById(R.id.abrir_calendario);
        Fecha = v.findViewById(R.id.fecha_vencimiento_puntos);
        Fecha_puntos_vencen = v.findViewById(R.id.fecha_puntos_vencen);


                vencen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_vencen.setEnabled(false);
                vencen.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                vencen.setTextColor(getResources().getColor(R.color.white));
                Puntos_vencen_dias.setVisibility(View.VISIBLE);
                Fecha_vencimiento = "";
                Abrir_calendario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog calendario = new Dialog(getContext());
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

                                long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                                Fecha_vencimiento = targetFormat.format(s);

                                JSONObject request = new JSONObject();
                                try
                                {
                                    request.put("usu_id", usu_id);
                                    request.put("esApp", "1");
                                    request.put("con_fecha_hora_vencen_puntos_programa_lealtad", Fecha_vencimiento);


                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }

                                String url = getString(R.string.Url);

                                String ApiPath = url + "/api/programa-de-lealtad/select-clientes";

                                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        JSONArray Respuesta = null;
                                        JSONObject NodoClientesSeleccionados = null;
                                        try {

                                            int status = Integer.parseInt(response.getString("estatus"));
                                            String Mensaje = response.getString("mensaje");


                                            if (status == 1)
                                            {

                                                Respuesta = response.getJSONArray("resultado");

                                                for(int x = 0; x < Respuesta.length(); x++){
                                                    JSONObject elemento = Respuesta.getJSONObject(x);

                                                }

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



                                if(Fecha_vencimiento.equals(""))
                                {

                                }else{
                                    Fecha_puntos_vencen.setVisibility(View.VISIBLE);
                                    Fecha.setText(Fecha_vencimiento);
                                }
                            }

                            @Override
                            public void onDateUnselected(Date date) {

                            }
                        });

                    }
                });

            }
        });
    }

    private void LoadSpinnerSucursal(){

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
                JSONArray  RespuestaNodoSucursales= null;
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


}
