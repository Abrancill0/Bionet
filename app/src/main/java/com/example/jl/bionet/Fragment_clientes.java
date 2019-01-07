package com.example.jl.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
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

        String[][] DATA_TO_SHOW = { { "1", "Juan Pérez", "a@gdg.mx", "4443264536","25/06/18","100.00","14557" },
                {""}};

        final TableView tabla_clientes = (TableView) v.findViewById(R.id.tabla_clientes);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "ID", "Nombre", "Correo Eléctronico", "Teléfono", "Últ. Visita", "Consumo Promedio","ID Referencia");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(7);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 3);
        tableColumnWeightModel.setColumnWeight(4, 3);
        tableColumnWeightModel.setColumnWeight(5, 3);
        tableColumnWeightModel.setColumnWeight(6, 3);



        tabla_clientes.setHeaderAdapter(simpleHeader);
        tabla_clientes.setColumnModel(tableColumnWeightModel);
        tabla_clientes.setDataAdapter(new SimpleTableDataAdapter(getContext(), DATA_TO_SHOW));

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
