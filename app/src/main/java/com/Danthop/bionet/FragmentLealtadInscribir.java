package com.Danthop.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableLealtadProgramasTable;
import com.Danthop.bionet.model.ProgramaModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLealtadInscribir extends Fragment {

    private SortableClientesTable tabla_clientes;
    private FragmentTransaction fr;
    private Button Lealtad;
    private Button Programas;
    private Button Articulos;


    public FragmentLealtadInscribir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lealtad_inscribir,container, false);
        fr = getFragmentManager().beginTransaction();
        tabla_clientes = v.findViewById(R.id.tabla_clientes);
        tabla_clientes.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        Lealtad=v.findViewById(R.id.lealtad);
        Lealtad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtad()).commit();

            }
        });

        Programas=v.findViewById(R.id.programas);
        Programas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadProgramas()).commit();

            }
        });

        Articulos=v.findViewById(R.id.articulo);
        Articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadArticulo()).commit();

            }
        });
        return v;

    }

}
