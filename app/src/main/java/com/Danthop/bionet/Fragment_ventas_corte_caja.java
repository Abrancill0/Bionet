package com.Danthop.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ventas_corte_caja extends Fragment {

    Button pestania_ventas;
    Button pestania_reporte;
    Button btn_corte;
    Button btn_listado_corte;
    Button btn_factura_ventas;


    public Fragment_ventas_corte_caja() {
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
    }



}
