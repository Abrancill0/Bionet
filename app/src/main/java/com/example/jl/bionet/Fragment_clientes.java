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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_clientes extends Fragment {


    static String[][] clientes={
            {"1","Abraham Moreno","a.moreno@Danthop","4446754321","Hace 20 días","$200.00","4555322"},
            {"2","Oscar Antonio","oscarsl7@Danthop","4444654321","Hace 13 días","$100.00","4555323"},
    };


    public Fragment_clientes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientes,container, false);
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
