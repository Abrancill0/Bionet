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
public class Fragment_ecommerce_Sincronizar extends Fragment {
    private TableView tabla_sincronizar;
    private View v;
    private String usu_id;
    private Button btn_pestania_ordenes;
    private Button btn_pestania_preguntas;
    private ArrayList<String> Categoria;
    private ArrayList<String> Articulo;
    private ArrayList<String> Variante;
    private Spinner SpinnerCategoria;
    private Spinner SpinnerArticulo;
    private Spinner SpinnerVariante;


    public Fragment_ecommerce_Sincronizar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecommerce_sincronizar,container, false);
        LoadTable();
        LoadButtons();
        LoadSpinners();

        return v;
    }

    public void LoadTable(){
        tabla_sincronizar = (TableView) v.findViewById(R.id.tabla_sincronizar);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Artículo", "SKU", "Modificadores", "Categoría", "Cant. en almacén","Cantidad Disponible","Canal","Envío Gratis","Status");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        usu_id = sharedPref.getString("usu_id","");

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(9);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(5, 2);
        tableColumnWeightModel.setColumnWeight(6, 2);
        tableColumnWeightModel.setColumnWeight(6, 2);
        tableColumnWeightModel.setColumnWeight(6, 2);

        tabla_sincronizar.setHeaderAdapter(simpleHeader);
        tabla_sincronizar.setColumnModel(tableColumnWeightModel);

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
        btn_pestania_preguntas = v.findViewById(R.id.btn_pestania_preguntas);
        btn_pestania_preguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecommerce_preguntas()).commit();
            }
        });
    }

    public void LoadSpinners(){
        Categoria=new ArrayList<>();
        Articulo=new ArrayList<>();
        Variante=new ArrayList<>();

        SpinnerCategoria = v.findViewById(R.id.Combo_categoria);
        SpinnerArticulo = v.findViewById(R.id.Combo_articulo);
        SpinnerVariante = v.findViewById(R.id.Combo_variante);


        Categoria.add("Categoria 1");
        Categoria.add("Categoria 2");
        Categoria.add("Categoria 3");

        Articulo.add("Artículo 1");
        Articulo.add("Artículo 2");
        Articulo.add("Artículo 3");

        Variante.add("Variante 1");
        Variante.add("Variante 2");
        Variante.add("Variante 3");

        SpinnerCategoria.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Categoria));
        SpinnerArticulo.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Articulo));
        SpinnerVariante.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Variante));

    }

}
