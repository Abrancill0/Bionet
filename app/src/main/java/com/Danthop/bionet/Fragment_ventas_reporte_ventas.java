package com.Danthop.bionet;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ventas_reporte_ventas extends Fragment {

    private View layout_titulo;
    private View layout_movimientos;
    private View layout_apartado;
    private View layout_ordenes;
    private View layout_invisible;


    private Button btn_corte_caja;
    private Button btn_ventas;


    private FragmentTransaction fr;


    public Fragment_ventas_reporte_ventas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas_reporte,container, false);

        fr = getFragmentManager().beginTransaction();
        layout_titulo = v.findViewById(R.id.layout_titulo);
        layout_movimientos = v.findViewById(R.id.layout_movimientos);
        layout_apartado = v.findViewById(R.id.layout_apartado);
        layout_ordenes = v.findViewById(R.id.layout_ordenes);
        layout_invisible = v.findViewById(R.id.layout_invisible);

        btn_ventas = v.findViewById(R.id.btn_ventas);
        btn_corte_caja = v.findViewById(R.id.btn_corte_caja);

        loadButtons();
        Layouts();

        return v;
    }

    public void Layouts()
    {
        layout_titulo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.8f));
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

    }

}
