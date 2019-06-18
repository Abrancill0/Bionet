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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.InventarioExistenciasAdapter;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_pestania_inventario_existencias extends Fragment {
    private SortableInventariosTable tabla_inventario_existencias;
    private String[][] inventarioModel;
    private List<InventarioModel> inventarios;
    private String[][] trasladoModel;
    private String usu_id;
    private String suc_id = "";
    private Button btn_buscarexistencias;
    private String modificadores = "";
    private String existencia = "";
    private String nombre_sucursal = "";
    private Date fechaSolicitud;
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

    private InventarioExistenciasAdapter ExistenciasAdapter;
    private SearchView Buscar;


    public Fragment_pestania_inventario_existencias() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pestania_inventario_existencias,container, false);

        Button btn_traslados = (Button) v.findViewById(R.id.btn_inventarios);
        btn_traslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_inventarios()).commit();
                onDetach();
            }
        });

        Button btnHistorico = (Button) v.findViewById(R.id.btn_hist);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_historico()).commit();
                onDetach();
            }
        });

        Button trasladar = (Button) v.findViewById(R.id.btn_tras);
        trasladar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_traslado()).commit();
                onDetach();
            }
        });




        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");

        inventarios = new ArrayList<>();

        tabla_inventario_existencias = (SortableInventariosTable) v.findViewById(R.id.tabla_inventario_existencias);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(),  "SKU","Articulo", "Sucursal", "Existencias");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize( 18 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);

        tabla_inventario_existencias.setHeaderAdapter(simpleHeader);
        tabla_inventario_existencias.setColumnModel(tableColumnWeightModel);


       btn_buscarexistencias = (Button)v.findViewById( R.id.btn_buscarexistencias );
        btn_buscarexistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Buscar = v.findViewById(R.id.search_disponibilidad);
        Buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Load_ExistenciasInventario(newText);
                return false;
            }
        });

        return v;
    }

//--------------------------------------------------------------------------------------------------
private void Load_ExistenciasInventario(String articulo) {
        inventarios.clear();

    String url = getString(R.string.Url);
    String ApiPath = url + "/api/inventario/buscar_disponibilidad_articulo?usu_id=" + usu_id + "&esApp=1&nombre_sku_articulo="+articulo;

    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            JSONArray Resultado = null;
            JSONArray Articulo = null;
            JSONArray imagenes = null;
            JSONObject RespuestaUUID = null;
            JSONObject RespuestaExistencias= null;

            try {
                int status = Integer.parseInt(response.getString("estatus"));
                String Mensaje = response.getString("mensaje");

                if (status == 1) {
                    Resultado = response.getJSONArray("resultado");
                    inventarioModel = new String[Resultado.length()][4];

                    for (int x = 0; x < Resultado.length(); x++) {
                        JSONObject elemento = Resultado.getJSONObject(x);

                        RespuestaUUID = elemento.getJSONObject("art_id");
                        String UUID = RespuestaUUID.getString("uuid");

                        producto = elemento.getString("art_nombre");
                        articulo_descripcion = elemento.getString("art_descripcion");
                        art_tipo = elemento.getString("art_tipo");
                        nombre_sucursal = elemento.getString("suc_nombre");
                        codigoBarras = elemento.getString("exi_codigo_barras");
                        almacen = elemento.getString("alm_nombre");

                        RespuestaExistencias = elemento.getJSONObject("exi_cantidad");
                        existencia = RespuestaExistencias.getString("value");

                        Boolean Disponible_venta = Boolean.valueOf(elemento.getString("art_disponible_venta"));
                        if (Disponible_venta == true) {
                            art_disponible_venta = "si";
                        } else {
                            art_disponible_venta = "no";
                        }

                        Boolean Disponible_compra = Boolean.valueOf(elemento.getString("art_disponible_compra"));
                        if (Disponible_compra == true) {
                            art_disponible_compra = "si";
                        } else {
                            art_disponible_compra = "no";
                        }

                        Boolean Disponible_apartados = Boolean.valueOf(elemento.getString("art_aplica_apartados"));
                        if (Disponible_apartados == true) {
                            ava_aplica_apartados = "si";
                        } else {
                            ava_aplica_apartados = "no";
                        }

                        Boolean Disponible_devoluciones = Boolean.valueOf(elemento.getString("art_aplica_cambio_devolucion"));
                        if (Disponible_devoluciones == true) {
                            ava_aplica_cambio_devolucion = "si";
                        } else {
                            ava_aplica_cambio_devolucion = "no";
                        }

                        //Variantes_Modificadores_SKU
                        Boolean Disponible_Variante = Boolean.valueOf(elemento.getString("art_tiene_variantes"));
                        if (Disponible_Variante == true) {
                            String NombreVariante = elemento.getString("ava_nombre");

                            Boolean Disponible_Modificador = Boolean.valueOf(elemento.getString("ava_tiene_modificadores"));
                            String NombreCompleto;
                            if (Disponible_Modificador == true) {
                                sku = elemento.getString("amo_sku");
                                String NombreModificador = elemento.getString("mod_nombre");
                                NombreCompleto = producto + " " + NombreVariante + " " + NombreModificador;
                                producto = NombreCompleto;

                            } else {
                                NombreCompleto = producto + " " + NombreVariante;
                                producto = NombreCompleto;
                            }
                        }else {
                            String NombreVariante = elemento.getString("ava_nombre");
                            sku = elemento.getString("ava_sku");
                            Boolean Disponible_Modificador = Boolean.valueOf(elemento.getString("ava_tiene_modificadores"));
                            String NombreCompleto;
                            if (Disponible_Modificador == true) {
                                sku = elemento.getString("amo_sku");
                                String NombreModificador = elemento.getString("mod_nombre");
                                NombreCompleto = producto + " " + NombreVariante + " " + NombreModificador;
                                producto = NombreCompleto;

                            } else {
                                sku = elemento.getString("ava_sku");
                                NombreCompleto = producto + " " + NombreVariante;
                                producto = NombreCompleto;
                            }
                        }
                        final InventarioModel inventario = new InventarioModel(
                                sku,
                                producto,
                                existencia,
                                "",
                                "",
                                nombre_sucursal,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "","","","","","","","","");

                        inventarios.add(inventario);
                    }ExistenciasAdapter = new InventarioExistenciasAdapter(getContext(), inventarios, tabla_inventario_existencias);
                    tabla_inventario_existencias.setDataAdapter(ExistenciasAdapter);
                }
            } catch (JSONException e) {
                Toast toast1 =
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                toast1.show();
            }
        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try{
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }catch(NullPointerException e){

                    }

                }
            }
    );
    getRequest.setShouldCache(false);
    VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );
}
//------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
