package com.Danthop.bionet;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableLealtadProgramasTable;
import com.Danthop.bionet.Tables.SortablePuntosTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLealtadProgramas extends Fragment{

    private SortableLealtadProgramasTable tabla_programas;
    private FragmentTransaction fr;
    private Button Lealtad;
    private Button Inscribir;
    private Button Articulos;
    private Button Abrir_calendario;

    public FragmentLealtadProgramas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lealtad_programas,container, false);
        fr = getFragmentManager().beginTransaction();
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

        Abrir_calendario = v.findViewById(R.id.abrir_calendario);
        Abrir_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog calendario=new Dialog(getContext());
                calendario.setContentView(R.layout.calendar);
            }
        });



        return v;
    }

}
