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
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Fragment_pestania_cortecaja extends Fragment {
    private String[][] HistoricoModel;
    private List<CorteCajaModel> CorteCaja;
    private List<CorteCajaModel> Totalventa;
    private List<FormaspagoModel>FormasPago;
    private SortableCorteCajaTable tabla_Generarcorte;
    private SortableCorteCajaTable tabla_formaspago;
    private TableDataClickListener<HistoricoModel> tablaListener;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private FragmentTransaction fr;
    private String suc_id;
    private String usu_id;
    private String cca_id_sucursal;
    private String idsucursal;
    private String valueIdSuc;
    private String valoresPagos;
    private String cde_importe_total;
    private String tic_numero;
    private String cde_fecha_hora_creo;
    private String tic_nombre_vendedor;
    private String nombrepago;
    private String nompago;
    private String nombrepago01;
    private String nombrepago05;
    private String nombrepago06;
    private String nombrepago08;
    private String id_formapago;
    private String id;
    private String cde_id;
    private String importe;
    private String id_pago;
    private String formapago;
    private EditText Fechainicio;
    private EditText Fechafin;
    private String FechaInicio;
    private String DateInicio;
    private String DateFin;
    private String FechaApiinicio;
    private String FechaApifin;
    private String FechaFin;
    private TextView btn_corte;
    private TextView btn_listado_corte;
    private TextView btn_factura_ventas;

    private Button btn_buscar;
    private Button btn_generarcorte;

    private Button aceptarcorte;
    private Button salircorte;
    private String id_sucursales;
    private String uuid;
    private Double TotalCorte;
    private DatePickerDialog.OnDateSetListener inicioDataSetlistener;
    private DatePickerDialog.OnDateSetListener finDataSetlistener;
    ProgressDialog progreso;
    private Dialog dialog;
    private JSONArray jsonArray;
    private JSONArray ArrayPagos;

    private View layout_tablacortes;
    private View layaut_fechascortes;

    public Fragment_pestania_cortecaja() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas_cortecajas, container, false);
        fr = getFragmentManager().beginTransaction();

        btn_corte = (TextView) v.findViewById(R.id.btn_corte); //opcion 1
        btn_listado_corte = (TextView) v.findViewById(R.id.btn_listado_corte);
        btn_factura_ventas = (TextView) v.findViewById(R.id.btn_factura_ventas);

        Fechainicio=(EditText) v.findViewById(R.id.btnfechainicio);
        Fechafin=(EditText) v.findViewById(R.id.btnfechafin);


        btn_generarcorte=(Button)v.findViewById(R.id.btn_generarcorte);
        btn_buscar =(Button)v.findViewById(R.id.btn_buscar);

        layaut_fechascortes = v.findViewById(R.id.layaut_fechascortes);
        layout_tablacortes = v.findViewById( R.id.layout_tablacortes );


        //aceptarcorte=(Button)v.findViewById( R.id.aceptarcorte );
        //salircorte=(Button)v.findViewById( R.id.salircorte );

        jsonArray = new JSONArray();
        ArrayPagos = new JSONArray();

        Button btn_ventas = (Button) v.findViewById(R.id.btn_ventas);
        btn_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_Ventas()).commit();
            }
        });

        Button btn_traslados = (Button) v.findViewById(R.id.btn_traslados);
        btn_traslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_transacciones()).commit();
            }
        });

       TextView btn_factura_ventas = (TextView) v.findViewById(R.id.btn_factura_ventas);
        btn_factura_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_lista_sinfactura()).commit();
            }
        });

        Button btn_comisiones = (Button) v.findViewById(R.id.Comisiones);
        btn_comisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_comison()).commit();
            }
        });

        btn_listado_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_caja_listado()).commit();
            }
        });

        btn_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_cortecaja()).commit();
            }
        });

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");
        dialog = new Dialog(getContext());
        CorteCaja = new ArrayList<>();
        FormasPago = new ArrayList<>();
        Totalventa = new ArrayList<>();

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



        tabla_Generarcorte = (SortableCorteCajaTable) v.findViewById(R.id.tabla_corte);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Id_venta", "Total", "Forma de pago", "Fecha", "Hora", "Usuario");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize( 18 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 1);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(4, 1);
        tableColumnWeightModel.setColumnWeight(5, 1);


        tabla_Generarcorte.setHeaderAdapter(simpleHeader);
        tabla_Generarcorte.setColumnModel(tableColumnWeightModel);


        btn_buscar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultado = FechaInicio.compareTo( FechaFin );
                    if (resultado == 0 || resultado < 0) {

                        if (resultado < 0) {

                            corte_caja();

                        } else {
                            Toast toast1 = Toast.makeText( getContext(), "Fecha de inicio debe ser diferente a la fecha final", Toast.LENGTH_SHORT );
                            toast1.show();
                        }
                    } else {
                        Toast toast1 = Toast.makeText( getContext(), "Fecha de inicio debe ser menor a la de final", Toast.LENGTH_SHORT );
                        toast1.show();
                    }


            }
        });

        btn_generarcorte.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (CorteCaja.isEmpty()) {
                    dialog.setContentView( R.layout.pop_up_cortecaja_finalizar );
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

                } else {

                    dialog.setContentView( R.layout.pop_up_aceptar_corte_caja );
                    dialog.show();

                    TextView totalcorte = dialog.findViewById(R.id.Total_corte);
                    totalcorte.setText("Total corte: $ " + String.valueOf( TotalCorte));

                    Button aceptarcorte = dialog.findViewById( R.id.aceptarcorte );
                    aceptarcorte.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Generar_Corte();
                            Toast.makeText( getContext(), "Corte de Caja Generado Exitosamente", Toast.LENGTH_LONG ).show();
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace( R.id.fragment_container, new Fragment_ventas_corte_caja_listado() ).commit();
                        }
                    } );


                    Button salircorte = dialog.findViewById( R.id.salircorte );
                    salircorte.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    } );

                    Button cancelar = dialog.findViewById( R.id.cancelar );
                    cancelar.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    } );

                }
            }
        });


        Time today = new Time( Time.getCurrentTimezone() );
        today.setToNow();
        String dia;
        String mes;
        int year = today.year;

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
                Fechainicio.setText( fechausuario );
                FechaInicio = year + "/" + mes + "/" + dia;

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

        return v;
    }


    public void corte_caja() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Buscando...");
        progreso.show();

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ventas/cortes/generar-corte";

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
                JSONArray Resultado = null;
                JSONArray Movimiento = null;
                JSONArray FormaPago = null;
                JSONObject RespuestaId_Corte = null;
                JSONObject RespuestaCorte = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Resultado = response.getJSONArray("resultado");
                       for (int x = 0; x < Resultado.length(); x++) {
                           JSONObject elemento = Resultado.getJSONObject(x);

                           RespuestaCorte = elemento.getJSONObject( "corte" );
                           RespuestaId_Corte = RespuestaCorte.getJSONObject( "cca_id" );
                           uuid = RespuestaId_Corte.getString( "uuid" );

                           Movimiento = elemento.getJSONArray("movimientos");
                           for (int y = 0; y < Movimiento.length(); y++){
                               JSONObject elemento2 = Movimiento.getJSONObject(y);



                               String cde_id_ticket = elemento2.getString("cde_id_ticket");
                               if (cde_id_ticket != "null"){
                                   cde_importe_total = elemento2.getString("cde_importe_total");
                                   cde_fecha_hora_creo = elemento2.getString("cde_fecha_creo");
                                   String hora = elemento2.getString( "cde_hora_creo" );
                                   tic_nombre_vendedor = elemento2.getString("tic_nombre_vendedor");
                                   tic_numero =elemento2.getString("tic_numero");

                                   FormaPago = elemento2.getJSONArray("tic_importe_forma_pago");

                                   for (int z = 0; z < FormaPago.length(); z++){
                                       JSONObject elemento3 = FormaPago.getJSONObject(z);
                                       id = elemento3.getString("id");
                                       Double importe = elemento3.getDouble("importe");
                                       nombrepago = elemento3.getString( "nombre" );


                                      /*JSONObject songs = elemento3.getJSONObject("nombre");
                                    Iterator It = songs.keys();

                                       while (It.hasNext() ) {

                                           String key = (String) It.next();

                                           int Resultado01 = key.compareTo( "Efectivo" );

                                           if (Resultado01 == 0) {
                                               nombrepago01 = elemento3.getString( "nombre" );
                                           }
                                       }*/

                                     /* JSONObject request = new JSONObject();
                                       try {
                                           request.put("id", id);
                                           request.put("valor", importe);
                                           request.put("nombre", nombrepago);
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       jsonArray.put(request);*/

                                      final FormaspagoModel formaspago = new FormaspagoModel(
                                               id, nombrepago,importe);
                                       FormasPago.add(formaspago);
                                   }


                                   final CorteCajaModel corte = new CorteCajaModel(
                                           tic_numero,
                                           cde_importe_total,
                                           nombrepago,
                                           "",
                                           tic_nombre_vendedor,
                                           "",0.0, 0.0, 0.0, 0.0,
                                           cde_fecha_hora_creo,hora,id_formapago,TotalCorte,0.0,0.0,0.0,0.0,
                                            "",0.0,0.0,0.0);
                                   CorteCaja.add(corte);
                                   Totalventa.add(corte);
                               }
                           }


                           Map<String, List<FormaspagoModel>> GroupingFormasPago = null;
                           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                               GroupingFormasPago = FormasPago.stream().collect(Collectors.groupingBy(FormaspagoModel::getid_pago));

                               GroupingFormasPago.keySet().forEach( employee -> {
                                   FormaspagoModel idpago = FormasPago.stream()
                                           .filter(Pagos -> employee.equals(Pagos.getid_pago()))
                                           .findAny()
                                           .orElse(null);
                                   nompago  = String.valueOf(idpago.getformapago());
                                   String IDpago = idpago.getid_pago();
                                   Double total = FormasPago.stream().filter(Pagos -> employee.equals(Pagos.getid_pago())).mapToDouble(FormaspagoModel::getImporte).sum();
                                   TotalCorte = Double.valueOf( FormasPago.stream().mapToDouble( FormaspagoModel::getImporte).sum() );

                                   JSONObject request = new JSONObject();
                                   try {
                                       request.put("id", IDpago);
                                       request.put("valor", total);

                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                                   jsonArray.put(request);
                               });

                           }
                       }

                        final CorteCajaAdapter corteAdapter = new CorteCajaAdapter(getContext(), CorteCaja, tabla_Generarcorte);
                        tabla_Generarcorte.setDataAdapter(corteAdapter);
                        progreso.hide();

                     }else {
                        Toast toast1 = Toast.makeText(getContext(),"No existen cortes abiertos para el periodo proporcionado", Toast.LENGTH_LONG);
                        toast1.show();
                        progreso.hide();
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


    public void Generar_Corte(){

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ventas/cortes/guardar-corte";


        JSONObject jsonBodyrequest = new JSONObject();
        try {
            jsonBodyrequest.put("esApp", "1" );
            jsonBodyrequest.put("usu_id", usu_id);
            jsonBodyrequest.put("cca_id_sucursal",valueIdSuc);
            jsonBodyrequest.put("cca_id",uuid);
            jsonBodyrequest.put("asFP",jsonArray);


        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,jsonBodyrequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Resultado = null;
                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");


                    if (status == 1){
                        Resultado = response.getJSONObject( "resultado" );

                      /*  dialog.setContentView(R.layout.pop_up_aceptar_corte_caja);
                        dialog.show();

                        Button salircorte = dialog.findViewById(R.id.salircorte);
                        salircorte.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.hide();
                            }
                        });

                        Button cancelar = dialog.findViewById(R.id.cancelar);
                        cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.hide();
                            }
                        });*/



                       /* Toast.makeText(getContext(), "Corte de Caja Generado Exitosamente", Toast.LENGTH_LONG).show();
                        FragmentTransaction fr = getFragmentManager().beginTransaction();
                        fr.replace(R.id.fragment_container,new Fragment_ventas_corte_caja_listado()).commit();*/


                    }else {
                        Toast toast1 = Toast.makeText(getContext(),"No existen ventas para generar corte de caja", Toast.LENGTH_LONG);
                        toast1.show();
                       // progreso.hide();
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


}