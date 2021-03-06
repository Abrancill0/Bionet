package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.LealtadConfiguracionesAdapter;
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

import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLealtadConfiguraciones extends Fragment{

    private SortableLealtadConfiguracionesTable tabla_programas;
    private FragmentTransaction fr;
    private Button Lealtad;
    private Button Inscribir;
    private Button Articulos;
    private TextView Abrir_calendario;
    private CheckBox vencen;
    private CalendarPickerView calendarView;
    private String usu_id;
    private CheckBox CheckPtMaximos;
    private EditText IntroducirPuntos;
    private Button AceptarPuntos;
    private View ViewPtMaximos;
    private TextView PuntosMaximos;
    private Button CrearConfiguracion;
    private EditText IntroducirPesos1;
    private EditText IntroducirPesos2;
    private EditText IntroducirPuntos1;
    private EditText IntroducirPuntos2;
    private Button AceptarCrear;
    private Dialog Activar;
    private TableDataClickListener<ConfiguracionLealtadModel> tablaListener;


    private String Lealtad_puntos_maximos;
    private String Lealtad_con_id;
    private String Lealtad_fecha_vencimiento;
    private String Lealtad_status;
    private String cpl_id;

    private Spinner SpinnerSucursal;

    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;


    private List<ConfiguracionLealtadModel> Configuraciones;
    private ProgressDialog progressDialog;

    private Bundle bundle;
    private boolean  Agregar_Articulo=false;
    private boolean  Agregar_Cliente=false;
    private boolean Lista_Articulos=false;
    private boolean Asignar_Articulo=false;
    private boolean Asignar_Cliente=false;
    private boolean  Configuracion_Programa=false;
    private boolean Caducidad_Puntos=false;
    private boolean Inscribir_Programa = false;
    private boolean Puntos_Acumulados = false;
    private boolean Listado_Puntos = false;


    public FragmentLealtadConfiguraciones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lealtad_configuracion,container, false);
        fr = getFragmentManager().beginTransaction();
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Configuraciones = new ArrayList<>();
        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();

        SpinnerSucursal=(Spinner)v.findViewById(R.id.Sucursal_lealtad);
        tabla_programas = v.findViewById(R.id.tabla_programas);
        tabla_programas.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        CheckPtMaximos = v.findViewById(R.id.CheckBox_pt_maximos);
        Lealtad=v.findViewById(R.id.lealtad);
        Inscribir=v.findViewById(R.id.inscribir);
        Articulos=v.findViewById(R.id.articulo);
        vencen = v.findViewById(R.id.vencen);
        Abrir_calendario = v.findViewById(R.id.abrir_calendario);
        CrearConfiguracion = v.findViewById(R.id.crear_configuracion);

        LoadListenerTable();
        LoadPestañas();
        LoadSpinnerSucursal();
        FechaVencimiento();
        AñadirEliminarPuntosMaximos();
        CrearConfiguracion();

        tabla_programas.addDataClickListener(tablaListener);
        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Configuraciones.clear();
                LoadConfiguraciones();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        bundle = getArguments();
        Agregar_Articulo=bundle.getBoolean("Agregar_Articulo");
        Agregar_Cliente=bundle.getBoolean("Agregar_Cliente");
        Lista_Articulos=bundle.getBoolean("Lista_Articulos");
        Asignar_Articulo=bundle.getBoolean("Asignar_Articulo");
        Asignar_Cliente=bundle.getBoolean("Asignar_Cliente");
        Configuracion_Programa=bundle.getBoolean("Configuracion_Programa");
        Caducidad_Puntos=bundle.getBoolean("Caducidad_Puntos");
        Inscribir_Programa=bundle.getBoolean("Inscribir_Programa");
        Puntos_Acumulados=bundle.getBoolean("Puntos_Acumulados");
        Listado_Puntos=bundle.getBoolean("Listado_Puntos");

        LoadPermisosFunciones();



        return v;
    }

    private void CrearConfiguracion()
    {
        CrearConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog crearConfiguracion = new Dialog(getContext());
                crearConfiguracion.setContentView(R.layout.pop_up_crear_configuracion);
                crearConfiguracion.show();

                IntroducirPesos1 = crearConfiguracion.findViewById(R.id.introducirPesos1);
                IntroducirPesos2 = crearConfiguracion.findViewById(R.id.introducirPesos2);
                IntroducirPuntos1 = crearConfiguracion.findViewById(R.id.introducirPuntos1);
                IntroducirPuntos2 = crearConfiguracion.findViewById(R.id.introducirPuntos2);
                AceptarCrear = crearConfiguracion.findViewById(R.id.aceptar_crear);

                AceptarCrear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        JSONObject request = new JSONObject();
                        try
                        {
                            request.put("usu_id", usu_id);
                            request.put("esApp", "1");
                            request.put("cpl_id_sucursal",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
                            request.put("cpl_por_cada_importe",IntroducirPesos1.getText());
                            request.put("cpl_vale_puntos",IntroducirPuntos1.getText());
                            request.put("cpl_por_cada_puntos",IntroducirPuntos2.getText());
                            request.put("cpl_vale_importe",IntroducirPesos2.getText());
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                        String url = getString(R.string.Url);

                        String ApiPath = url + "/api/programa-de-lealtad/store-conf-puntos";

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

                                        progressDialog.dismiss();
                                        Toast toast1 = Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                                        toast1.show();
                                        crearConfiguracion.dismiss();
                                        Configuraciones.clear();
                                        LoadConfiguraciones();

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
                });

            }
        });
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

        String ApiPath = url + "/api/programa-de-lealtad/select-conf-puntos";

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

                        progressDialog.dismiss();
                        Respuesta = response.getJSONObject("resultado");
                            NodoConfiguraciones = Respuesta.getJSONArray("aConfiguracionProgramaLealtad");
                            for(int y = 0; y < NodoConfiguraciones.length();y++)
                            {
                                JSONObject elemento = NodoConfiguraciones.getJSONObject(y);
                                JSONObject cpl_por_cada_importe= elemento.getJSONObject("cpl_por_cada_importe");
                                String pesos1 = cpl_por_cada_importe.getString("value");
                                String puntos1= elemento.getString("cpl_vale_puntos");
                                String puntos2= elemento.getString("cpl_por_cada_puntos");
                                JSONObject vale_importe = elemento.getJSONObject("cpl_vale_importe");
                                String pesos2= vale_importe.getString("value");
                                String pesosxpuntos= "Por cada $ "+ pesos1 + " equivalen a " + puntos1 +" puntos";
                                String puntosxpesos= "Por cada "+ puntos2 + " puntos equivalen a $ " + pesos2;
                                Lealtad_status= elemento.getString("cpl_activo");
                                JSONObject cpl_id_nodo = elemento.getJSONObject("cpl_id");
                                cpl_id = cpl_id_nodo.getString("uuid");

                                final ConfiguracionLealtadModel configuracion = new ConfiguracionLealtadModel(String.valueOf(y+1), pesosxpuntos,puntosxpesos,Lealtad_status,cpl_id);
                                Configuraciones.add(configuracion);
                            }
                        final LealtadConfiguracionesAdapter configuracionesAdapter = new LealtadConfiguracionesAdapter(getContext(), Configuraciones, tabla_programas);
                        tabla_programas.setDataAdapter(configuracionesAdapter);

                        JSONObject NodoConfiguraciones2 = Respuesta.getJSONObject("aConfiguracion");
                        JSONObject elemento_con_id = NodoConfiguraciones2.getJSONObject("con_id");
                        Lealtad_con_id = elemento_con_id.getString("uuid");
                        Lealtad_puntos_maximos = NodoConfiguraciones2.getString("con_puntos_maximos_compra_programa_lealtad");
                        if(Lealtad_puntos_maximos.equals("0"))
                        {
                            CheckPtMaximos.setChecked(false);
                            CheckPtMaximos.setText("Máximo de puntos por compra");

                        }
                        else{
                            CheckPtMaximos.setChecked(true);
                            CheckPtMaximos.setText("Máximo de puntos por compra ("+Lealtad_puntos_maximos+")");
                        }

                        if(NodoConfiguraciones2.getString("con_puntos_vencen_programa_lealtad").equals("true"))
                        {
                            Lealtad_fecha_vencimiento = NodoConfiguraciones2.getString("con_fecha_hora_vencen_puntos");
                            vencen.setChecked(true);
                            Abrir_calendario.setText("el dia "+Lealtad_fecha_vencimiento);
                            Abrir_calendario.setVisibility(View.VISIBLE);

                        }
                        else if(NodoConfiguraciones2.getString("con_puntos_vencen_programa_lealtad").equals("false"))
                        {
                            vencen.setChecked(false);
                            Abrir_calendario.setVisibility(View.INVISIBLE);
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

    private void AñadirEliminarPuntosMaximos()
    {
        CheckPtMaximos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckPtMaximos.isChecked())
                {
                    final Dialog puntosMaximos = new Dialog(getContext());
                    puntosMaximos.setContentView(R.layout.pop_up_puntos_maximos_lealtad);
                    puntosMaximos.show();
                    IntroducirPuntos = puntosMaximos.findViewById(R.id.introducirPuntos);
                    AceptarPuntos = puntosMaximos.findViewById(R.id.aceptar_puntos);
                    AceptarPuntos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Lealtad_puntos_maximos = String.valueOf(IntroducirPuntos.getText());
                            JSONObject request = new JSONObject();
                            try
                            {
                                request.put("usu_id", usu_id);
                                request.put("esApp", "1");
                                request.put("con_id",Lealtad_con_id);
                                request.put("con_puntos_maximos_compra_programa_lealtad",Lealtad_puntos_maximos);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }

                            String url = getString(R.string.Url);

                            String ApiPath = url + "/api/programa-de-lealtad/add-puntos-maximos";

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

                                            Toast toast1 =
                                                    Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                            toast1.show();
                                            puntosMaximos.dismiss();
                                            CheckPtMaximos.setText("Máximo de puntos por compra ("+Lealtad_puntos_maximos+")");

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
                    });


                }else
                {
                    JSONObject request = new JSONObject();
                    try
                    {
                        request.put("usu_id", usu_id);
                        request.put("esApp", "1");
                        request.put("con_id",Lealtad_con_id);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    String url = getString(R.string.Url);

                    String ApiPath = url + "/api/programa-de-lealtad/delete-puntos-maximos";

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
                                    CheckPtMaximos.setText("Máximo de puntos por compra");
                                    Toast toast1 =
                                            Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                    toast1.show();

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
        });
    }

    private void FechaVencimiento()
    {
        vencen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(vencen.isChecked()) {
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

                            long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                            Lealtad_fecha_vencimiento = targetFormat.format(s);
                            calendario.dismiss();

                            JSONObject request = new JSONObject();
                            try {
                                request.put("usu_id", usu_id);
                                request.put("esApp", "1");
                                request.put("con_id", Lealtad_con_id);
                                request.put("con_fecha_hora_vencen_puntos_programa_lealtad", Lealtad_fecha_vencimiento);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String url = getString(R.string.Url);

                            String ApiPath = url + "/api/programa-de-lealtad/add-fecha-vencimiento";

                            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    JSONArray Respuesta = null;
                                    JSONObject NodoClientesSeleccionados = null;
                                    try {

                                        int status = Integer.parseInt(response.getString("estatus"));
                                        String Mensaje = response.getString("mensaje");


                                        if (status == 1) {

                                            Toast toast1 =
                                                    Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                            toast1.show();
                                            Abrir_calendario.setVisibility(View.VISIBLE);
                                            Abrir_calendario.setText("el día "+Lealtad_fecha_vencimiento);

                                        } else {
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

                        @Override
                        public void onDateUnselected(Date date) {

                        }
                    });
                    Abrir_calendario.setOnClickListener(new View.OnClickListener() {
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

                                    long s = calendarView.getSelectedDate().parse(String.valueOf(calendarView.getSelectedDate()));
                                    Lealtad_fecha_vencimiento = targetFormat.format(s);
                                    calendario.dismiss();

                                    JSONObject request = new JSONObject();
                                    try {
                                        request.put("usu_id", usu_id);
                                        request.put("esApp", "1");
                                        request.put("con_id", Lealtad_con_id);
                                        request.put("con_fecha_hora_vencen_puntos_programa_lealtad", Lealtad_fecha_vencimiento);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    String url = getString(R.string.Url);

                                    String ApiPath = url + "/api/programa-de-lealtad/add-fecha-vencimiento";

                                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            JSONArray Respuesta = null;
                                            JSONObject NodoClientesSeleccionados = null;
                                            try {

                                                int status = Integer.parseInt(response.getString("estatus"));
                                                String Mensaje = response.getString("mensaje");


                                                if (status == 1) {

                                                    Toast toast1 =
                                                            Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                                    toast1.show();
                                                    Abrir_calendario.setVisibility(View.VISIBLE);
                                                    Abrir_calendario.setText("el día "+Lealtad_fecha_vencimiento);

                                                } else {
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

                                @Override
                                public void onDateUnselected(Date date) {

                                }
                            });

                        }
                    });

                }
                else
                {
                    JSONObject request = new JSONObject();
                    try
                    {
                        request.put("usu_id", usu_id);
                        request.put("esApp", "1");
                        request.put("con_id",Lealtad_con_id);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    String url = getString(R.string.Url);

                    String ApiPath = url + "/api/programa-de-lealtad/delete-fecha-vencimiento";

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

                                    Toast toast1 =
                                            Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                    toast1.show();

                                    Abrir_calendario.setVisibility(View.INVISIBLE);

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


    private void LoadPestañas(){
        Lealtad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Agregar_Articulo", Agregar_Articulo);
                bundle.putBoolean("Agregar_Cliente", Agregar_Cliente);
                bundle.putBoolean("Lista_Articulos", Lista_Articulos);
                bundle.putBoolean("Asignar_Articulo", Asignar_Articulo);
                bundle.putBoolean("Asignar_Cliente", Asignar_Cliente);
                bundle.putBoolean("Configuracion_Programa", Configuracion_Programa);
                bundle.putBoolean("Caducidad_Puntos", Caducidad_Puntos);
                bundle.putBoolean("Inscribir_Programa", Inscribir_Programa);
                bundle.putBoolean("Puntos_Acumulados", Puntos_Acumulados);
                bundle.putBoolean("Listado_Puntos", Listado_Puntos);
                FragmentLealtad fragment2 = new FragmentLealtad();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();

            }
        });


        Inscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Agregar_Articulo", Agregar_Articulo);
                bundle.putBoolean("Agregar_Cliente", Agregar_Cliente);
                bundle.putBoolean("Lista_Articulos", Lista_Articulos);
                bundle.putBoolean("Asignar_Articulo", Asignar_Articulo);
                bundle.putBoolean("Asignar_Cliente", Asignar_Cliente);
                bundle.putBoolean("Configuracion_Programa", Configuracion_Programa);
                bundle.putBoolean("Caducidad_Puntos", Caducidad_Puntos);
                bundle.putBoolean("Inscribir_Programa", Inscribir_Programa);
                bundle.putBoolean("Puntos_Acumulados", Puntos_Acumulados);
                bundle.putBoolean("Listado_Puntos", Listado_Puntos);
                FragmentLealtadInscribir fragment2 = new FragmentLealtadInscribir();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();

            }
        });


        Articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Agregar_Articulo", Agregar_Articulo);
                bundle.putBoolean("Agregar_Cliente", Agregar_Cliente);
                bundle.putBoolean("Lista_Articulos", Lista_Articulos);
                bundle.putBoolean("Asignar_Articulo", Asignar_Articulo);
                bundle.putBoolean("Asignar_Cliente", Asignar_Cliente);
                bundle.putBoolean("Configuracion_Programa", Configuracion_Programa);
                bundle.putBoolean("Caducidad_Puntos", Caducidad_Puntos);
                bundle.putBoolean("Inscribir_Programa", Inscribir_Programa);
                bundle.putBoolean("Puntos_Acumulados", Puntos_Acumulados);
                bundle.putBoolean("Listado_Puntos", Listado_Puntos);
                FragmentLealtadArticulo fragment2 = new FragmentLealtadArticulo();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onDetach();

            }
        });
    }

    private void LoadListenerTable(){
        tablaListener = new TableDataClickListener<ConfiguracionLealtadModel>() {
            @Override
            public void onDataClicked(int rowIndex, ConfiguracionLealtadModel clickedData) {

                cpl_id = (clickedData.getCpl_id());

                if(clickedData.getStatus().equals("false"))
                {
                    LoadActivar();
                }
                else if(clickedData.getStatus().equals("true"))
                {
                    LoadDesactivar();
                }
            }
        };
    }

    private void LoadActivar(){
        Activar =new Dialog(getContext());
        Activar.setContentView(R.layout.pop_up_lealtad_activar_configuracion);
        Activar.show();
        Button cancelar = Activar.findViewById(R.id.Cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activar.dismiss();
            }
        });
        Button activar = Activar.findViewById(R.id.activar_configuracion);
        activar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject request = new JSONObject();
                try
                {
                    request.put("usu_id", usu_id);
                    request.put("esApp", "1");
                    request.put("cpl_id",cpl_id);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                String url = getString(R.string.Url);

                String ApiPath = url + "/api/programa-de-lealtad/activar-configuracion-puntos";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            int status = Integer.parseInt(response.getString("estatus"));
                            String Mensaje = response.getString("mensaje");


                            if (status == 1)
                            {

                                Toast toast1 =
                                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                toast1.show();
                                Activar.dismiss();
                                Configuraciones.clear();
                                LoadConfiguraciones();

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
        });

    }

    private void LoadDesactivar(){
        final Dialog Desactivar =new Dialog(getContext());
        Desactivar.setContentView(R.layout.pop_up_lealtad_desactivar_configuracion);
        Desactivar.show();
        Button cancelar = Desactivar.findViewById(R.id.Cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Desactivar.dismiss();
            }
        });
        Button desactivar = Desactivar.findViewById(R.id.desactivar_configuracion);
        desactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject request = new JSONObject();
                try
                {
                    request.put("usu_id", usu_id);
                    request.put("esApp", "1");
                    request.put("cpl_id",cpl_id);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                String url = getString(R.string.Url);

                String ApiPath = url + "/api/programa-de-lealtad/desactivar-configuracion-puntos";

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            int status = Integer.parseInt(response.getString("estatus"));
                            String Mensaje = response.getString("mensaje");


                            if (status == 1)
                            {

                                Toast toast1 =
                                        Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                                toast1.show();
                                Desactivar.dismiss();
                                Configuraciones.clear();
                                LoadConfiguraciones();

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
        });

    }

    private void LoadPermisosFunciones()
    {
        if(Inscribir_Programa==false)
        {
            CrearConfiguracion.setEnabled(false);
        }

        if(Caducidad_Puntos==false)
        {
            CheckPtMaximos.setEnabled(false);
        }

        if(Configuracion_Programa==false)
        {
            tablaListener = new TableDataClickListener<ConfiguracionLealtadModel>() {
                @Override
                public void onDataClicked(int rowIndex, ConfiguracionLealtadModel clickedData) {
                    Toast toast1 =
                            Toast.makeText(getContext(), "No cuentas con los permisos necesarios para \n configurar el programa", Toast.LENGTH_LONG);
                    toast1.show();
                }
            };
            tabla_programas.addDataClickListener(tablaListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
