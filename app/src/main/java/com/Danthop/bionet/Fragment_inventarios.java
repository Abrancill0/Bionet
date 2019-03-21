package com.Danthop.bionet;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_inventarios extends Fragment {
    private String[] [] inventarioModel;
    private String usu_id;
    //private SortableInventarioTable tabla_inventario;
    //private ProgressDialog progreso;

    private String sku;
    private String producto;
    private String modificadores;
    private String categoria;
    private String existencia;
    private String listado_Inventario;
    private String traslados;
    private String creditos_Proveedores;
    private String agregar_Productos;
    private String solicitar_Traslado;



    public Fragment_inventarios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventarios,container, false);
        SearchView Search = (SearchView) v.findViewById(R.id.search_inventario);
        Search.setQueryHint("Buscar");
        Spinner spinner = (Spinner) v.findViewById(R.id.categoria_inventario);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Giros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);

        usu_id = sharedPref.getString("usu_id","");

        final TableView tabla_inventario = (TableView) v.findViewById(R.id.tabla_inventario);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "SKU", "Producto", "Modificadores", "Categoria", "Existencia");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);


        tabla_inventario.setHeaderAdapter(simpleHeader);
        tabla_inventario.setColumnModel(tableColumnWeightModel);

        Button btn_traslados = (Button) v.findViewById(R.id.fragment_pestania_traslados);
        btn_traslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_traslados()).commit();
            }
        });

        Muestra_Inventario();

        return v;
    }

    private void Muestra_Inventario()
    {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/inventario/index_app";

        JsonObjectRequest postRequets = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Resultado = null;
                JSONObject ElementoInventario = null;
                JSONArray ResultadoNodoInventario = null;


                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {
                        Resultado = response.getJSONArray("resultado");


                        inventarioModel = new String[Resultado.length()][4];

                        for (int x = 0; x < Resultado.length(); x++){
                            JSONObject elemento = Resultado.getJSONObject(x);

                            sku = elemento.getString("ava_sku");
                            /*producto = elemento.getString("art_nombre");
                            categoria = elemento.getString("cat_nombre");
                            existencia = elemento.getString("exi_cantidad");
                            modificadores = elemento.getString("");
                            */
                        }



                    }
                }catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();


                }

            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();


                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequets);


    }

}
