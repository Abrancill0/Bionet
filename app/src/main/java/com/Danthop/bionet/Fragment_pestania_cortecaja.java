package com.Danthop.bionet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.model.CorteCajaModel;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Fragment_pestania_cortecaja extends Fragment {
    private String[][] HistoricoModel;
    private List<CorteCajaModel> CorteCaja;
    private SortableCorteCajaTable tabla_Generarcorte;
    private TableDataClickListener<HistoricoModel> tablaListener;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private FragmentTransaction fr;
    private String suc_id;
    private String usu_id;
    private String cca_id_sucursal;
    private String idsucursal;
    private String valueIdSuc;
    private String cde_importe_total;
    private String tic_numero;
    private String cde_fecha_hora_creo;
    private String tic_nombre_vendedor;
    private String nombrepago;
    private EditText Fechainicio;
    private EditText Fechafin;
    private String FechaInicio;
    private String FechaFin;
    private Button btn_corte;
    private Button btn_buscar;
    private Button btn_listado_corte;
    private Button btn_factura_ventas;
    private Button btn_generarcorte;
    private String id_sucursales;
    private DatePickerDialog.OnDateSetListener inicioDataSetlistener;
    private DatePickerDialog.OnDateSetListener finDataSetlistener;
    ProgressDialog progreso;


    public Fragment_pestania_cortecaja() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pestania_cortecaja, container, false);
        fr = getFragmentManager().beginTransaction();

        btn_corte = (Button) v.findViewById(R.id.btn_corte);
        btn_listado_corte = (Button) v.findViewById(R.id.btn_listado_corte);
        Fechainicio=(EditText) v.findViewById(R.id.btnfechainicio);
        Fechafin=(EditText) v.findViewById(R.id.btnfechafin);
        btn_factura_ventas = (Button) v.findViewById(R.id.btn_factura_ventas);
        btn_generarcorte=(Button)v.findViewById(R.id.btn_generarcorte);
        btn_buscar =(Button)v.findViewById(R.id.btn_buscar);

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

        btn_listado_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_caja_listado()).commit();
            }
        });

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");
        CorteCaja = new ArrayList<>();

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
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Id_venta", "Total", "Forma de pago", "Fecha y Hora", "Usuario");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 1);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(4, 1);

        tabla_Generarcorte.setHeaderAdapter(simpleHeader);
        tabla_Generarcorte.setColumnModel(tableColumnWeightModel);


        btn_buscar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultado = FechaInicio.compareTo(FechaFin);
                if (resultado == 0 || resultado < 0) {

                    if (resultado < 0) {

                            corte_caja();

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

        btn_generarcorte.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //
            }
        });



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

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Resultado = response.getJSONArray("resultado");
                       for (int x = 0; x < Resultado.length(); x++) {
                           JSONObject elemento = Resultado.getJSONObject(x);

                           Movimiento = elemento.getJSONArray("movimientos");
                           for (int y = 0; y < Movimiento.length(); y++){
                               JSONObject elemento2 = Movimiento.getJSONObject(y);

                               String cde_id_ticket = elemento2.getString("cde_id_ticket");
                               if (cde_id_ticket != "null"){
                                   cde_importe_total = elemento2.getString("cde_importe_total");
                                   cde_fecha_hora_creo = elemento2.getString("cde_fecha_hora_creo");
                                   tic_nombre_vendedor = elemento2.getString("tic_nombre_vendedor");
                                   tic_numero =elemento2.getString("tic_numero");

                                   FormaPago = elemento2.getJSONArray("tic_importe_forma_pago");
                                   for (int z = 0; z < FormaPago.length(); z++){
                                       JSONObject elemento3 = FormaPago.getJSONObject(z);
                                       nombrepago = elemento3.getString("nombre");
                                   }


                                   final CorteCajaModel corte = new CorteCajaModel(
                                           tic_numero,
                                           cde_importe_total,
                                           nombrepago,
                                           cde_fecha_hora_creo,
                                           tic_nombre_vendedor);
                                   CorteCaja.add(corte);
                               }
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
}