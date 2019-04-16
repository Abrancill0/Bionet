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
import android.widget.Toast;
import android.view.KeyEvent;
import android.media.MediaPlayer;
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
public class Fragment_pantalla_principal extends Fragment{

    private long backPressedTime;
    private Toast backToast;
    MediaPlayer mp = new MediaPlayer();

    public Fragment_pantalla_principal() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pantalla_principal,container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getContext().MODE_PRIVATE );
        String ImagenPerfil = sharedPref.getString( "usu_imagen_perfil", "" );
        String Apellido = sharedPref.getString("usu_apellidos", "");
        String Nombre = sharedPref.getString("usu_nombre", "");

        //=====Programación de las tablas=====
        String[][] DATA_TO_SHOW = { { "Producto 1", "Sucursal 1"},
                {""}};

        String[][] DATA_TO_SHOW2 = { { "Notificacion 1"},
                {""}};

        String[][] DATA_TO_SHOW3 = { { "Producto 1", "Sucursal 1"},
                {""}};

        String[][] DATA_TO_SHOW4 = { { "Cliente 1"},
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


        final TableView tablaPocas_Existencias = (TableView) v.findViewById(R.id.tablaPocas_Existencias);
        final SimpleTableHeaderAdapter simpleHeader3 = new SimpleTableHeaderAdapter(getContext(), "Producto", "Sucursal");
        final SimpleTableDataAdapter simpleTableDataAdapter3 = new SimpleTableDataAdapter(getContext(),DATA_TO_SHOW3);
        simpleHeader3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader3.setTextSize(12);
        simpleHeader3.setPaddings(10,10,10,10);
        simpleTableDataAdapter3.setTextSize(12);
        simpleTableDataAdapter3.setPaddingLeft(0);
        simpleTableDataAdapter3.setPaddingRight(0);


        final TableView tablaClientesFrecuentes = (TableView) v.findViewById(R.id.tablaClientesFrecuentes);
        final SimpleTableHeaderAdapter simpleHeader4 = new SimpleTableHeaderAdapter(getContext(), "Cliente");
        final SimpleTableDataAdapter simpleTableDataAdapter4 = new SimpleTableDataAdapter(getContext(),DATA_TO_SHOW4);
        simpleHeader4.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader4.setTextSize(12);
        simpleHeader4.setPaddings(10,10,10,10);
        simpleTableDataAdapter4.setTextSize(12);
        simpleTableDataAdapter4.setPaddingLeft(0);
        simpleTableDataAdapter4.setPaddingRight(0);

        tabla_Productos.setHeaderAdapter(simpleHeader);
        tabla_Productos.setDataAdapter(simpleTableDataAdapter);

        tabla_Notificaciones.setHeaderAdapter(simpleHeader2);
        tabla_Notificaciones.setDataAdapter(simpleTableDataAdapter2);

        tablaPocas_Existencias.setHeaderAdapter(simpleHeader3);
        tablaPocas_Existencias.setDataAdapter(simpleTableDataAdapter3);

        tablaClientesFrecuentes.setHeaderAdapter(simpleHeader4);
        tablaClientesFrecuentes.setDataAdapter(simpleTableDataAdapter4);

        //=====Programación del carrousel=====
        final int[] sampleImages = {R.drawable.store2, R.drawable.store, R.drawable.museum_store, R.drawable.store3};
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


