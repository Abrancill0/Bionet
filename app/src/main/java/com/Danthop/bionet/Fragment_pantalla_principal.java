package com.Danthop.bionet;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pantalla_principal extends Fragment {

    private ImageView Foto_perfil;
    private TextView Text_nombre;


    public Fragment_pantalla_principal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pantalla_principal,container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getContext().MODE_PRIVATE );
        String ImagenPerfil = sharedPref.getString( "usu_imagen_perfil", "" );
        String Nombre = sharedPref.getString("usu_nombre", "");
        Text_nombre = v.findViewById(R.id.TextNombrePerfil);
        Text_nombre.setText(Nombre);

        Foto_perfil = v.findViewById(R.id.foto_perfil);

        if(ImagenPerfil.equals(""))
        {

        }
        else{
            Picasso.with( getContext() ).load( ImagenPerfil ).into( Foto_perfil );
        }

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





        //=====Programación del carrousel=====
        final int[] sampleImages = {R.drawable.milk, R.drawable.bread, R.drawable.strawberrie, R.drawable.lake};
        CarouselView carouselView;
        carouselView = (CarouselView) v.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };

        carouselView.setImageListener(imageListener);


        return v;
    }

}


