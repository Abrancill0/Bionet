package com.Danthop.bionet;
import android.app.Dialog;
import android.app.ProgressDialog;
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
    private String fechaSolicitud;
    private String his_observaciones;
    private String his_fecha_hora_creo;
    private String RecibidasOrigen;
    private String RecibidasDestino;
    private String tra_nombre_estatus;
    private String suc_numero_sucursal_destino;
    private String suc_numero_sucursal_origen;
    private String tra_motivo;
    private ProgressDialog progressDialog;
    private InventarioAdapter inventarioAdapter;
    private SearchView Buscar;

    public Fragment_inventarios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventarios, container, false);
        fr = getFragmentManager().beginTransaction();

        Buscar= (SearchView) v.findViewById(R.id.search_inventario);

        Spinner spinner = (Spinner) v.findViewById(R.id.categoria_inventario);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Giros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        ver_producto_dialog=new Dialog(getContext());
        ver_producto_dialog.setContentView(R.layout.pop_up_ficha_articulos);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        inventarios = new ArrayList<>();

        tabla_inventario = (SortableInventariosTable) v.findViewById(R.id.tabla_inventario);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "SKU", "Artículo", "Código de barras", "Categoría", "Existencias", "Sucursal");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize( 18 );

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(5, 2);

        tabla_inventario.setHeaderAdapter(simpleHeader);
        tabla_inventario.setColumnModel(tableColumnWeightModel);

       Button btnTraslados = (Button) v.findViewById(R.id.btn_traslados);
        btnTraslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Fragment_pestania_traslado()).commit();
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

        Button btnInventariExistencias = (Button) v.findViewById(R.id.btnBuscarExistencias);
        btnInventariExistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_inventario_existencias()).commit();
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

        Buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventarioAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }
//------------------------------------------------------------------------------------------------------------------------
    private void Muestra_Inventario() {
        JSONObject request = new JSONObject();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = getString(R.string.Url);
        String ApiPath = url + "/api/inventario/index?usu_id=" + usu_id + "&esApp=1";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Resultado = null;
                JSONArray Articulo = null;
                JSONArray Sucursales = null;
                JSONArray imagenes = null;
                JSONObject RespuestaUUID = null;
                JSONObject RespuestaExistencias= null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        progressDialog.dismiss();
                        Resultado = response.getJSONObject("resultado");

                        Articulo = Resultado.getJSONArray("aArticuloExistencias");
                        inventarioModel = new String[Articulo.length()][4];

                        for (int x = 0; x < Articulo.length(); x++) {
                            JSONObject elemento = Articulo.getJSONObject(x);

                            RespuestaUUID = elemento.getJSONObject("art_id");
                            String UUID = RespuestaUUID.getString("uuid");

                            producto = elemento.getString("art_nombre");
                            categoria = elemento.getString("cat_nombre");
                            articulo_descripcion = elemento.getString("art_descripcion");
                            art_tipo = elemento.getString("art_tipo");
                            codigoBarras = elemento.getString("exi_codigo_barras");
                            almacen = elemento.getString("alm_nombre");

                            String suc_nombre = elemento.getString( "suc_nombre" );
                            String suc_numero = elemento.getString( "suc_numero" );
                            nombre_sucursal = suc_nombre + "(" + suc_numero + ")";

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

                            imagenes = elemento.getJSONArray("imagenes");
                            JSONObject elemento3 = imagenes.getJSONObject(0);
                            aim_url = getString(R.string.Url) + elemento3.getString("aim_url");

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
                                            his_observaciones,
                                            his_fecha_hora_creo,
                                            codigoBarras,
                                            almacen,
                                            RecibidasOrigen,
                                            RecibidasDestino,
                                            tra_nombre_estatus,
                                            suc_numero_sucursal_destino,
                                            suc_numero_sucursal_origen,
                                            fechaSolicitud,
                                            tra_motivo,"","","","","","");
                                    inventarios.add(inventario);
                        }inventarioAdapter = new InventarioAdapter(getContext(), inventarios, tabla_inventario);
                        tabla_inventario.setDataAdapter(inventarioAdapter);
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
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        getRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );
    }
//------------------------------------------------------------------------------------------------------------------------

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