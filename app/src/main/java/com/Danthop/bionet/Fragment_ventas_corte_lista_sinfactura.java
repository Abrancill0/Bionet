package com.Danthop.bionet;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ListaTicketsAdapter;
import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.Tables.SortableHistoricoTable;
import com.Danthop.bionet.model.CorteCajaModel;
import com.Danthop.bionet.model.FormaspagoModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

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
public class Fragment_ventas_corte_lista_sinfactura extends Fragment {

    Button pestania_ventas;
    Button pestania_reporte;
    TextView btn_corte;
    TextView btn_listado_corte;
    TextView btn_factura_ventas;
    Button btn_buscartickets;
    private EditText Fechainicio;
    private EditText Fechafin;
    private Button btn_facturar;
    private String FechaInicio;
    private String FechaFin;
    private String usu_id;
    private String cca_id_sucursal;
    private String valueIdSuc;
    private String tic_numero;
    private String total;
    private Double tic_importe_recibido;
    private String hora;
    private String fecha;
    private Double efectivo01 = 0.0;
    private Double monedero05 = 0.0;
    private Double dineroelectronico06 = 0.0;
    private Double vales08 = 0.0;
    private List<FormaspagoModel>FormasPago;
    private List<CorteCajaModel> ListaTickets;
    private List<CorteCajaModel> TicketsFactura;
    private SortableCorteCajaTable tabla_ListarTickets;
    private DatePickerDialog.OnDateSetListener inicioDataSetlistener;
    private DatePickerDialog.OnDateSetListener finDataSetlistener;
    private SortableHistoricoTable tabla_historico;
    private Dialog dialog;
    private ArrayList<String> SucursalID;
    private ArrayList<String> ClienteName;
    private ArrayList<String> CFDI;
    private Spinner SpinnerNameClientes;
    private Spinner SpinnerCFDI;
    private String cli_nombre;
    private String ucf_id;


    public Fragment_ventas_corte_lista_sinfactura() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas_facturarcortecajas,container, false);
        pestania_ventas = v.findViewById(R.id.btn_ventas);
        pestania_reporte = v.findViewById(R.id.btn_traslados);

        btn_corte = v.findViewById(R.id.btn_corte); //opcion 1
        btn_listado_corte = v.findViewById(R.id.btn_listado_corte);
        btn_factura_ventas = v.findViewById(R.id.btn_factura_ventas);
        btn_factura_ventas.setBackgroundColor(getResources().getColor(R.color.fondo_azul));


        Fechainicio=(EditText) v.findViewById(R.id.btnfechainicio);
        Fechafin=(EditText) v.findViewById(R.id.btnfechafin);

        btn_facturar=(Button)v.findViewById(R.id.btn_facturar);
        btn_buscartickets=(Button)v.findViewById( R.id.btn_buscartickets );

        TextView btn_factura_ventas = (TextView) v.findViewById(R.id.btn_factura_ventas);
        btn_factura_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_lista_sinfactura()).commit();
                onDetach();
            }
        });


        TextView btn_corte = (TextView) v.findViewById(R.id.btn_corte);
        btn_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_cortecaja()).commit();
                onDetach();
            }
        });

        TextView btn_listado_corte = (TextView) v.findViewById(R.id.btn_listado_corte);
        btn_listado_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_caja_listado()).commit();
                onDetach();
            }
        });

        Button Comisiones = v.findViewById( R.id.Comisiones);
        Comisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_comison()).commit();
                onDetach();
            }
        });

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");
        ListaTickets = new ArrayList<>();
        TicketsFactura =new ArrayList<>();
        FormasPago = new ArrayList<>();
        dialog = new Dialog(getContext());

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

        tabla_ListarTickets = (SortableCorteCajaTable) v.findViewById(R.id.tabla_corte);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "", "Id Venta", "Monto Total", "Efectivo", "Monedero electrónico","Dinero electrónico","Vales de despensa","Fecha");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize( 14 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(8);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(5, 2);
        tableColumnWeightModel.setColumnWeight(6, 2);
        tableColumnWeightModel.setColumnWeight(7, 2);

        tabla_ListarTickets.setHeaderAdapter(simpleHeader);
        tabla_ListarTickets.setColumnModel(tableColumnWeightModel);

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
                onDetach();
            }
        });
        pestania_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_transacciones()).commit();
                onDetach();
            }
        });


        btn_facturar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TicketsFactura.size() > 0) {

                    dialog.setContentView( R.layout.pop_up_corte_facturar_tickets );
                    dialog.show();
                    ClienteName = new ArrayList<>();
                    CFDI = new ArrayList<>();
                    SpinnerNameClientes = dialog.findViewById(R.id.Combo_clientes);
                    SpinnerCFDI = dialog.findViewById(R.id.Combo_cfdi);

                    SpinnerClientes();
                    SpinnerCFDi();

                    Button btnSifacturar = dialog.findViewById( R.id.btnSifacturar );
                    btnSifacturar.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //Generar_Facturacion();
                            Toast.makeText( getContext(), "Facturación Generada Exitosamente", Toast.LENGTH_LONG ).show();
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace( R.id.fragment_container, new Fragment_ventas_corte_lista_sinfactura() ).commit();
                            onDetach();
                        }
                    } );

                    Button btnCerrar = dialog.findViewById(R.id.btnCerrar);
                    btnCerrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });

                    Button btnSalir3 = dialog.findViewById(R.id.btnSalir3);
                    btnSalir3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });


                } else {
                   dialog.setContentView( R.layout.pop_up_cortecaja_finalizar_factura );
                    dialog.show();

                    Button aceptar = dialog.findViewById( R.id.Aceptar );
                    aceptar.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    } );

                    Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                    cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });


                }
            }
        });


        btn_buscartickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultado = FechaInicio.compareTo(FechaFin);
                if (resultado == 0 || resultado < 0) {

                    Buscar_tickets();

                    }else {
                        Toast toast1 = Toast.makeText(getContext(), "Fecha de inicio debe ser menor a la fecha final", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
            }
        });

    }

    public void Buscar_tickets(){

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ventas/cortes/lista-tickets-sin-factura";

        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("tic_id_sucursal", valueIdSuc);
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
                JSONArray aTickets = null;
                JSONObject tic_importe_forma_pago = null;
                JSONArray aFormasPago =  null;


                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Resultado = response.getJSONObject("resultado");
                        aTickets = Resultado.getJSONArray("aTickets");

                            for (int y = 0; y < aTickets.length(); y++){
                                JSONObject elemento = aTickets.getJSONObject(y);

                                tic_numero = elemento.getString("tic_numero");
                                total = elemento.getString( "tic_importe_total" );
                                tic_importe_recibido = elemento.getDouble( "tic_importe_recibido" );
                                fecha = elemento.getString("tic_fecha_venta");
                                hora = elemento.getString("tic_hora_venta");

                                Double tic_importe_total = elemento.getDouble( "tic_importe_pagado" );
                                if (tic_importe_total != 0){
                                    tic_importe_forma_pago = elemento.getJSONObject( "tic_importe_forma_pago" );

                                    JSONObject songs= elemento.getJSONObject("tic_importe_forma_pago");
                                    Iterator x = songs.keys();

                                    efectivo01=0.0;
                                    monedero05=0.0;
                                    dineroelectronico06=0.0;
                                    vales08=0.0;

                                    while (x.hasNext()) {
                                        String key = (String) x.next();


                                            int Resultado01 = key.compareTo( "01" );

                                            if (Resultado01 == 0) {
                                                efectivo01 = tic_importe_forma_pago.getDouble( "01" );
                                            }

                                            int Resultado05 = key.compareTo( "05" );

                                            if (Resultado05 == 0) {
                                                monedero05 = tic_importe_forma_pago.getDouble( "05" );
                                            }


                                            int Resultado06 = key.compareTo( "06" );

                                            if (Resultado06 == 0) {
                                                dineroelectronico06 = tic_importe_forma_pago.getDouble( "06" );
                                            }


                                            int Resultado08 = key.compareTo( "08" );

                                            if (Resultado08 == 0) {
                                                vales08 = tic_importe_forma_pago.getDouble( "08" );
                                            }



                                        }
                                    //Model

                                }else {
                                    efectivo01=0.0;
                                    monedero05=0.0;
                                    dineroelectronico06=0.0;
                                    vales08=0.0;
                                }

                                final CorteCajaModel tickets = new CorteCajaModel(
                                        tic_numero,
                                        total,
                                        "",
                                        "",
                                        "",
                                        "",0.0,0.0,0.0,0.0,
                                        fecha,"","",0.0,efectivo01,monedero05,dineroelectronico06,vales08,
                                        "",0.0,0.0,0.0);
                                ListaTickets.add(tickets);

                            }



                        final ListaTicketsAdapter ListaticketsAdapter = new ListaTicketsAdapter(getContext(), ListaTickets, tabla_ListarTickets,TicketsFactura);
                        tabla_ListarTickets.setDataAdapter(ListaticketsAdapter);

                    }else {
                        Toast toast1 = Toast.makeText(getContext(), "No existen tickets en el periodo seleccionado.", Toast.LENGTH_LONG);
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


    private void SpinnerClientes() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = getString(R.string.Url);
        String ApiPath = url + "/api/clientes/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject Respuesta = null;
                JSONArray aClientes = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Respuesta = response.getJSONObject("resultado");
                        aClientes = Respuesta.getJSONArray("aClientes");

                        for(int x = 0; x < aClientes.length(); x++){
                            JSONObject elemento = aClientes.getJSONObject(x);


                            String cli_numero = elemento.getString( "cli_numero" );
                            String nombre = elemento.getString("cli_nombre");
                            cli_nombre = cli_numero + " " + "-" + " " + nombre;

                            ClienteName.add(cli_nombre);


                        }
                        SpinnerNameClientes.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,ClienteName));
                    }
                    else {
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

    private void SpinnerCFDi() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ventas/tickets/select-uso-cdfi";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray Respuesta = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Respuesta = response.getJSONArray("resultado");

                        for(int x = 0; x < Respuesta.length(); x++){
                            JSONObject elemento = Respuesta.getJSONObject(x);

                            String descripcion = elemento.getString( "ucf_descripcion" );
                            String clave = elemento.getString( "ucf_id" );
                            ucf_id = clave + " " + "-" + " " + descripcion;
                            CFDI.add(ucf_id);

                        }

                        SpinnerCFDI.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,CFDI));
                    }
                    else {
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
    public void onDetach() {
        super.onDetach();
    }

}
