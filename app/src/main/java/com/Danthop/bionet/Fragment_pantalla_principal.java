package com.Danthop.bionet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.TimerTask;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pantalla_principal extends Fragment {


    public Fragment_pantalla_principal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pantalla_principal,container, false);

        //=====Programación de las tablas=====
        String[][] DATA_TO_SHOW = { { "Producto1", "Sucursal1"},
                {""}};

        String[][] DATA_TO_SHOW2 = { { "Notificacion1"},
                {""}};
        final TableView tabla_Productos = (TableView) v.findViewById(R.id.tablaProductos_sucursales);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Producto", "Sucursal");
        final SimpleTableDataAdapter simpleTableDataAdapter = new SimpleTableDataAdapter(getContext(),DATA_TO_SHOW);
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize(12);
        simpleHeader.setPaddings(10,10,10,10);
        simpleTableDataAdapter.setTextSize(12);
        simpleTableDataAdapter.setPaddingLeft(0);
        simpleTableDataAdapter.setPaddingRight(0);


        final TableView tabla_Notificaciones = (TableView) v.findViewById(R.id.tablaNotificaciones);
        final SimpleTableHeaderAdapter simpleHeader2 = new SimpleTableHeaderAdapter(getContext(), "Notificación");
        final SimpleTableDataAdapter simpleTableDataAdapter2 = new SimpleTableDataAdapter(getContext(),DATA_TO_SHOW2);
        simpleHeader2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader2.setTextSize(12);
        simpleHeader2.setPaddings(10,10,10,10);
        simpleTableDataAdapter2.setTextSize(12);
        simpleTableDataAdapter2.setPaddingLeft(0);
        simpleTableDataAdapter2.setPaddingRight(0);

        tabla_Productos.setHeaderAdapter(simpleHeader);
        tabla_Productos.setDataAdapter(simpleTableDataAdapter);

        tabla_Notificaciones.setHeaderAdapter(simpleHeader2);
        tabla_Notificaciones.setDataAdapter(simpleTableDataAdapter2);

        //=====Programación de la gráfica=====

        return v;
    }

}


