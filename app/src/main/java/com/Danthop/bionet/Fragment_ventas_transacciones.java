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
public class Fragment_ventas_transacciones extends Fragment {

    private View layout_movimientos;
    private View layout_apartado;
    private View layout_ordenes;
    private View layout_invisible;

    private View btn_movimientos;
    private View btn_apartado;
    private View btn_ordenes_especiales;

    private Button btn_corte_caja;
    private Button btn_ventas;

    private String usu_id;

    private FragmentTransaction fr;


    public Fragment_ventas_transacciones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas_transacciones,container, false);
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

        btn_ventas = v.findViewById(R.id.btn_ventas);
        btn_corte_caja = v.findViewById(R.id.btn_corte_caja);

        loadButtons();
        Layouts();

        return v;
    }

    public void Layouts()
    {
        layout_movimientos.setVisibility(View.GONE);
        layout_apartado.setVisibility(View.VISIBLE);
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

                }
                else
                {

                }
            }
        });

        btn_apartado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_apartado.getVisibility()==View.GONE)
                {

                }
                else
                {

                }
            }
        });

        btn_ordenes_especiales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_ordenes.getVisibility()==View.GONE)
                {

                }
                else
                {

                }
            }
        });

    }

}
