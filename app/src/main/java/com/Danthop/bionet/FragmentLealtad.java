package com.Danthop.bionet;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Danthop.bionet.Tables.SortablePuntosTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLealtad extends Fragment {
    private SortablePuntosTable tabla_puntos;
    private FragmentTransaction fr;
    private Button Programas;
    private Button Inscribir;
    private Button Articulos;

    private String usu_id;




    public FragmentLealtad() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lealtad,container, false);
        fr = getFragmentManager().beginTransaction();
        tabla_puntos = v.findViewById(R.id.tabla_puntos);
        tabla_puntos.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));


        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        usu_id = sharedPref.getString("usu_id","");



        Programas=v.findViewById(R.id.programas);
        Programas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadConfiguraciones()).commit();

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
        return v;
    }



}
