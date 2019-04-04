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

    private View btn_movimientos;
    private View btn_apartado;
    private View btn_ordenes_especiales;

    private TextView pestania_movimientos;
    private TextView pestania_apartado;
    private TextView pestania_ordenes_especiales;


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
        btn_movimientos = v.findViewById(R.id.btn_movimientos);
        btn_apartado = v.findViewById(R.id.btn_apartado);
        btn_ordenes_especiales = v.findViewById(R.id.btn_ordenes_especiales);
        pestania_movimientos = v.findViewById(R.id.pestaniaMovimientos);
        pestania_apartado = v.findViewById(R.id.pestaniaApartados);
        pestania_ordenes_especiales = v.findViewById(R.id.pestaniaOrdenesEspeciales);

        btn_ventas = v.findViewById(R.id.btn_ventas);
        btn_corte_caja = v.findViewById(R.id.btn_corte_caja);

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
                    layout_apartado.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.5f));
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
                    layout_ordenes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,.5f));
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

}
