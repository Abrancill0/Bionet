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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.InventarioAdapter;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_inventarios extends Fragment {
    private String[][] inventarioModel;
    private SortableInventariosTable tabla_inventario;
    private List<com.Danthop.bionet.model.ArticuloModel> Articulos;
    private TableDataClickListener<InventarioModel> tablaListener;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<InventarioModel> inventarios;
    private FragmentTransaction fr;
    private Dialog ver_producto_dialog;
    private View v;
    private String Imagen;
    private String img_ruta;
    private ImageView FotoArticulo;

    private String usu_id;
    private String suc_id = "";
    private String modificadores = "";
    private String existencia = "";
    private String nombre_sucursal = "";
    private String producto = "";
    private String art_tipo = "";
    private String sku = "";
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


    public Fragment_inventarios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventarios, container, false);
        fr = getFragmentManager().beginTransaction();

        SearchView Search = (SearchView) v.findViewById(R.id.search_inventario);
        Search.setQueryHint("Buscar");
        Spinner spinner = (Spinner) v.findViewById(R.id.categoria_inventario);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Giros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        ver_producto_dialog=new Dialog(getContext());
        ver_producto_dialog.setContentView(R.layout.pop_up_ficha_articulos);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        inventarios = new ArrayList<>();

        tabla_inventario = (SortableInventariosTable) v.findViewById(R.id.tabla_inventario);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "SKU", "Producto", "Existencia", "Categoria", "Sucursal");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);

        tabla_inventario.setHeaderAdapter(simpleHeader);
        tabla_inventario.setColumnModel(tableColumnWeightModel);

        Button btnTraslados = (Button) v.findViewById(R.id.btn_traslados);
        btnTraslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Fragment_pestania_traslados()).commit();
            }
        });

        Button btnHistorico = (Button) v.findViewById(R.id.btn_historico);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_historico()).commit();
            }
        });

        Muestra_Inventario();
        LoadListenerTable();

        tabla_inventario.setSwipeToRefreshEnabled(true);
        tabla_inventario.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_inventario.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inventarios.clear();
                        Muestra_Inventario();
                        refreshIndicator.hide();
                    }
                }, 2000);
            }
        });

        tabla_inventario.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        tabla_inventario.addDataClickListener(tablaListener);

        return v;
    }

    private void Muestra_Inventario() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = getString(R.string.Url);
        String ApiPath = url + "/api/inventario/index_app";

        JsonObjectRequest postRequets = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Resultado = null;
                JSONArray Articulo = null;
                JSONArray Sucursales = null;
                JSONArray imagenes = null;
                JSONObject RespuestaUUID = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Resultado = response.getJSONObject("resultado");

                        Articulo = Resultado.getJSONArray("Articulos");
                        inventarioModel = new String[Articulo.length()][4];

                        for (int x = 0; x < Articulo.length(); x++) {
                            JSONObject elemento = Articulo.getJSONObject(x);

                            RespuestaUUID = elemento.getJSONObject("art_id");
                            String UUID = RespuestaUUID.getString("uuid");

                            sku = elemento.getString("ava_sku");
                            producto = elemento.getString("art_nombre");
                            categoria = elemento.getString("cat_nombre");
                            articulo_descripcion = elemento.getString("art_descripcion");
                            art_tipo = elemento.getString("art_tipo");

                            Boolean Disponible_venta = Boolean.valueOf(elemento.getString("art_disponible_venta"));
                            if (Disponible_venta == true){
                                art_disponible_venta = "si";
                            }else{
                                art_disponible_venta = "no";
                            }

                            Boolean Disponible_compra = Boolean.valueOf(elemento.getString("art_disponible_compra"));
                            if (Disponible_compra == true){
                                art_disponible_compra = "si";
                            }else{
                                art_disponible_compra = "no";
                            }

                            Boolean Disponible_apartados = Boolean.valueOf(elemento.getString("ava_aplica_apartados"));
                            if (Disponible_apartados == true){
                                ava_aplica_apartados = "si";
                            }else{
                                ava_aplica_apartados = "no";
                            }

                            Boolean Disponible_devoluciones = Boolean.valueOf(elemento.getString("ava_aplica_cambio_devolucion"));
                            if (Disponible_devoluciones == true){
                                ava_aplica_cambio_devolucion = "si";
                            }else{
                                ava_aplica_cambio_devolucion = "no";
                            }

                            imagenes = elemento.getJSONArray( "imagenes");
                            JSONObject elemento3 = imagenes.getJSONObject(0);
                            aim_url = getString(R.string.Url) + elemento3.getString("aim_url");


                            String NombreVariante = elemento.getString("ava_nombre");
                            Boolean Modificadores = Boolean.valueOf(elemento.getString("ava_tiene_modificadores"));
                            String NombreCompleto;

                            if (Modificadores == true) {
                                String NombreModificador = elemento.getString("mod_nombre");
                                NombreCompleto = producto + " " + NombreVariante + " " + NombreModificador;
                                producto = NombreCompleto;

                            } else {
                                NombreCompleto = producto + " " + NombreVariante;
                                producto = NombreCompleto;
                            }

                            Sucursales = elemento.getJSONArray("sucursales");
                            for (int z = 0; z < Sucursales.length(); z++) {
                                JSONObject elemento2 = Sucursales.getJSONObject(z);
                                existencia = elemento2.getString("exi_cantidad");
                                int exist = Integer.parseInt(existencia);
                                if (exist >= 1) {
                                    nombre_sucursal = elemento2.getString("suc_nombre");
                                    suc_id = elemento2.getString("suc_id");

                                    final InventarioModel inventario = new InventarioModel(
                                            sku,
                                            producto,
                                            existencia,
                                            categoria,
                                            modificadores,
                                            nombre_sucursal,
                                            suc_id,
                                            articulo_descripcion,
                                            art_tipo,
                                            art_disponible_venta,
                                            art_disponible_compra,
                                            ava_aplica_apartados,
                                            ava_aplica_cambio_devolucion,
                                            aim_url,
                                            art_nombre,
                                            cat_nombre,
                                            his_tipo,
                                            his_cantidad,
                                            his_observaciones);
                                    inventarios.add(inventario);
                                }
                            }
                        }
                    }final InventarioAdapter InventarioAdapter = new InventarioAdapter(getContext(), inventarios, tabla_inventario);
                    tabla_inventario.setDataAdapter(InventarioAdapter);

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
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequets);
    }

    private void LoadListenerTable(){
        tablaListener = new TableDataClickListener<InventarioModel>() {
            @Override
            public void onDataClicked(int rowIndex, final InventarioModel clickedData) {
                final Dialog ver_producto_dialog;
                ver_producto_dialog = new Dialog(getContext());
                ver_producto_dialog.setContentView(R.layout.pop_up_ficha_articulos);
                ver_producto_dialog.show();

                TextView nombre_producto = ver_producto_dialog.findViewById(R.id.articulo_nombre);
                TextView tipo_articulo = ver_producto_dialog.findViewById(R.id.tipo_producto);
                TextView sku = ver_producto_dialog.findViewById(R.id.sku_producto);
                TextView descripcion = ver_producto_dialog.findViewById(R.id.descripcion_producto);
                TextView categoria = ver_producto_dialog.findViewById(R.id.categoria_producto);
                TextView disponible_venta = ver_producto_dialog.findViewById(R.id.disponible_venta);
                TextView art_disponible_compra = ver_producto_dialog.findViewById(R.id.disponible_compra);
                TextView ava_aplica_apartados = ver_producto_dialog.findViewById(R.id.aplica_apartados);
                TextView ava_aplica_cambio_devolucion = ver_producto_dialog.findViewById(R.id.aplica_devoluciones);
                ImageView FotoArticulo = (ImageView) ver_producto_dialog.findViewById(R.id.img);

                nombre_producto.setText(clickedData.getProducto());
                tipo_articulo.setText(clickedData.getArt_tipo());
                sku.setText(clickedData.getSku());
                descripcion.setText(clickedData.getArt_descripcion());
                categoria.setText(clickedData.getCategoria());
                disponible_venta.setText(clickedData.getart_disponible_venta());
                art_disponible_compra.setText(clickedData.getart_disponible_compra());
                ava_aplica_apartados.setText(clickedData.getava_aplica_apartados());
                ava_aplica_cambio_devolucion.setText(clickedData.getava_aplica_cambio_devolucion());
                imageLoader.displayImage(String.valueOf(clickedData.getaim_url()),FotoArticulo);
            }
        };
    }
}