package com.Danthop.bionet;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ecomerce extends Fragment {
    private TableView tabla_ecomerce;
    private View v;
    private String usu_id;
    private Button btn_pestania_sincronizar;
    private Button btn_pestania_preguntas;
    private Button btn_sincronizar;
    private Dialog dialog;
    private Button btn_sincronizar_no;
    private Button btn_sincronizar_si;
    private Button btn_aceptar_cerrar_ventana;


    public Fragment_ecomerce() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecomerce,container, false);
        LoadTable();
        LoadButtons();

        return v;
    }


    public void LoadTable(){
        tabla_ecomerce = (TableView) v.findViewById(R.id.tabla_ecommerce);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "Nombre del Cliente", "Correo", "Artículo", "Cantidad", "Envío","Importe","Status");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        usu_id = sharedPref.getString("usu_id","");

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(7);
        tableColumnWeightModel.setColumnWeight(0, 4);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 3);
        tableColumnWeightModel.setColumnWeight(4, 3);
        tableColumnWeightModel.setColumnWeight(5, 3);
        tableColumnWeightModel.setColumnWeight(6, 3);

        tabla_ecomerce.setHeaderAdapter(simpleHeader);
        tabla_ecomerce.setColumnModel(tableColumnWeightModel);

    }

    public void LoadButtons(){
        dialog=new Dialog(getContext());
        btn_pestania_sincronizar = v.findViewById(R.id.btn_pestania_sincronizar);
        btn_pestania_sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();
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
        btn_sincronizar = v.findViewById(R.id.btn_sincronizar);
        btn_sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.pop_up_sincronizar);
                dialog.show();
                btn_sincronizar_no = dialog.findViewById(R.id.btn_sincronizar_no);
                btn_sincronizar_si = dialog.findViewById(R.id.btn_sincronizar_si);


                btn_sincronizar_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_sincronizar_si.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setContentView(R.layout.pop_up_confimacion_sincronizar);
                        btn_aceptar_cerrar_ventana = dialog.findViewById(R.id.aceptar_cerrar_ventana1);

                        btn_aceptar_cerrar_ventana.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });


            }
        });
    }

}
