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
import android.widget.Toast;


import com.Danthop.bionet.Adapters.TrasladoAdapter;
import com.Danthop.bionet.Tables.SortableTrasladosTable;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pestania_traslado extends Fragment {
    private SortableTrasladosTable tabla_traslados;
    private List<InventarioModel> traslados;
    private String[][] trasladoModel;
    private String usu_id;
    private String suc_id = "";
    private String modificadores = "";
    private String existencia = "";
    private String nombre_sucursal = "";
    private String producto = "";
    private String art_tipo = "";
    private String sku = "";
    private String codigoBarras = "";
    private String almacen = "";
    private String amo_activo="";
    private String articulo_descripcion;
    private String categoria;
    private String art_disponible_venta;
    private String art_disponible_compra;
    private String ava_aplica_apartados;
    private String ava_aplica_cambio_devolucion;
    private String aim_url;
    private String art_nombre;
    private String cat_nombre;
    private String his_tipo;
    private String his_cantidad;
    private String his_observaciones;
    private String his_fecha_hora_creo;
    private String RecibidasOrigen = "";
    private String RecibidasDestino = "";
    private String tra_nombre_estatus = "";
    private String suc_numero_sucursal_destino = "";
    private String suc_numero_sucursal_origen = "";
    private String fechaSolicitud = "";

    public Fragment_pestania_traslado() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pestania_traslados,container, false);

       Button btn_traslados = (Button) v.findViewById(R.id.btn_inventarios);
        btn_traslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_inventarios()).commit();
            }
        });

        Button btnHistorico = (Button) v.findViewById(R.id.btn_hist);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_historico()).commit();
            }
        });

        Button trasladar = (Button) v.findViewById(R.id.pop_up_traslados);
        trasladar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pop_up_traslados dialog = new Fragment_pop_up_traslados();
                dialog.setTargetFragment(Fragment_pestania_traslado.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });

        Button agregar = (Button) v.findViewById(R.id.btnAgregarProd);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_pop_up_punto_reorden dialog = new Fragment_pop_up_punto_reorden();
                dialog.setTargetFragment(Fragment_pestania_traslado.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        traslados = new ArrayList<>();

        tabla_traslados = (SortableTrasladosTable) v.findViewById(R.id.tabla_traslados);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Origen", "Destino", "Estatus", "Motivo", "Fecha de solicitud");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 1);
        tableColumnWeightModel.setColumnWeight(2, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(3, 1);

        tabla_traslados.setHeaderAdapter(simpleHeader);
        tabla_traslados.setColumnModel(tableColumnWeightModel);


        Spinner spinner = (Spinner) v.findViewById(R.id.categoria_sku);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.SKU, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        Spinner spinner2 = (Spinner) v.findViewById(R.id.categoria_categoria);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.Categoría, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter2);

        Spinner spinner3 = (Spinner) v.findViewById(R.id.categoria_producto);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.Producto, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner3.setAdapter(adapter3);


        Button SolicitudesRecibidas = (Button) v.findViewById(R.id.traslados_recibidos);
        SolicitudesRecibidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Fragment_pestania_traslado_recibidos()).commit();
            }
        });

        Button SolicitudesEnviadas = v.findViewById(R.id.traslados_enviados);
        SolicitudesEnviadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Fragment_pestania_traslado_enviados()).commit();
            }
        });

        return v;
    }

//-------------------------------------------------------------------------------------------------
}
