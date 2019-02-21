package com.Danthop.bionet;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ecommerce_preguntas extends Fragment {


    private TableView tabla_preguntas;
    private View v;
    private String usu_id;
    private Button btn_pestania_sincronizar;
    private Button btn_pestania_ordenes;
    private ArrayList<String> Articulo;
    private Spinner SpinnerArticulo;


    public Fragment_ecommerce_preguntas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecommerce_preguntas,container, false);
        LoadTable();
        LoadButtons();
        LoadSpinner();

        return v;
    }


    public void LoadTable(){
        tabla_preguntas = (TableView) v.findViewById(R.id.tabla_preguntas);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Título de la publicación", "Precio de lista", "Usuario", "Pregunta", "Responder");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        usu_id = sharedPref.getString("usu_id","");

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 5);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 3);
        tableColumnWeightModel.setColumnWeight(4, 3);

        tabla_preguntas.setHeaderAdapter(simpleHeader);
        tabla_preguntas.setColumnModel(tableColumnWeightModel);

    }

    public void LoadButtons(){
        btn_pestania_ordenes = v.findViewById(R.id.btn_pestania_ordenes);
        btn_pestania_ordenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecomerce()).commit();
            }
        });
        btn_pestania_sincronizar = v.findViewById(R.id.btn_pestania_sincronizar);
        btn_pestania_sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();
            }
        });
    }

    public void LoadSpinner(){
        Articulo=new ArrayList<>();

        SpinnerArticulo = v.findViewById(R.id.Combo_articulo);

        Articulo.add("Artículo 1");
        Articulo.add("Artículo 2");
        Articulo.add("Artículo 3");

        SpinnerArticulo.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Articulo));

    }


}
