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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.CorteCajaAdapter;
import com.Danthop.bionet.Adapters.HistoricoAdapter;
import com.Danthop.bionet.Tables.SortableCorteCajaTable;
import com.Danthop.bionet.Tables.SortableHistoricoTable;
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

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private EditText Fechainicio;
    private EditText Fechafin;
    private Button btn_corte;
    private Button btn_listado_corte;
    private Button btn_factura_ventas;
    private Button btn_generarcorte;
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

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");
        CorteCaja = new ArrayList<>();

        tabla_Generarcorte = (SortableCorteCajaTable) v.findViewById(R.id.tabla_historico);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Id_venta", "Total", "Forma de pago", "Fecha", "Hora", "Usuario");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 1);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(4, 1);
        tableColumnWeightModel.setColumnWeight(5, 1);

        tabla_Generarcorte.setHeaderAdapter(simpleHeader);
        tabla_Generarcorte.setColumnModel(tableColumnWeightModel);



        btn_generarcorte.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                corte_caja();
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
                String Date = dia + "/" + mes + "/" + year;
                Fechainicio.setText( Date );
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
                String Date = dia + "/" + mes + "/" + year;
                Fechafin.setText( Date );
            }
        };

        return v;
    }


    public void corte_caja() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Solicitando...");
        progreso.show();

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ventas/cortes/generar-corte?usu_id=" + usu_id + "&cca_id_sucursal=" + cca_id_sucursal + "&esApp=1";

        JSONObject request = new JSONObject();
        JsonObjectRequest posRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject Resultado = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Resultado = response.getJSONObject("resultado");
                       // for (int x = 0; x < Resultado.length(); x++) {
                         //   JSONObject elemento = Resultado.getJSONObject(x);



                            final CorteCajaModel corte = new CorteCajaModel(
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "");
                        CorteCaja.add(corte);
                        }
                        final CorteCajaAdapter corteAdapter = new CorteCajaAdapter(getContext(), CorteCaja, tabla_Generarcorte);
                        tabla_Generarcorte.setDataAdapter(corteAdapter);


                     //}
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
        VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( posRequest );
    }
}