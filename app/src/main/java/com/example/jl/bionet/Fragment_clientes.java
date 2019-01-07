package com.example.jl.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_clientes extends Fragment {



    public Fragment_clientes() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientes,container, false);
        String[] tabla_clientes_HEADERS = { "ID", "Nombre", "Correo Eléctronico", "Teléfono", "Últ. Visita", "Consumo Promedio","ID Referencia"};

        String[][] DATA_TO_SHOW = { { "1", "Juan Pérez", "a@gdg.mx", "4443264536","25/06/18","100.00","14557" }, };

        TableView tabla_clientes = (TableView) v.findViewById(R.id.tabla_clientes);
        tabla_clientes.setHeaderAdapter(new SimpleTableHeaderAdapter(v.getContext(),tabla_clientes_HEADERS));
        tabla_clientes.setHeaderBackgroundColor(getResources().getColor(R.color.white));
        tabla_clientes.setDataAdapter(new SimpleTableDataAdapter(v.getContext(), DATA_TO_SHOW));

        Button btn_crear_cliente = (Button) v.findViewById(R.id.btn_crear_cliente);
        btn_crear_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_crear_cliente()).commit();
            }
        });

        return v;


    }

}
