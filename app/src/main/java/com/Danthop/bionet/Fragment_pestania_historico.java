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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.Danthop.bionet.Adapters.HistoricoAdapter;
import com.Danthop.bionet.Tables.SortableHistoricoTable;
import com.Danthop.bionet.model.HistoricoModel;
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

public class Fragment_pestania_historico extends Fragment {
    private String[][] HistoricoModel;
    private List<HistoricoModel> historico;
    private SortableHistoricoTable tabla_historico;
    private Dialog ver_producto_dialog;
    private TableDataClickListener<HistoricoModel> tablaListener;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private FragmentTransaction fr;

    private String suc_id;
    private String modificadores;
    private String existencia;
    private String nombre_sucursal;
    private String producto = "";
    private String art_tipo = "";
    private String sku = "";
    private String articulo_descripcion = "";
    private String categoria = "";
    private String art_disponible_venta = "";
    private String art_disponible_compra = "";
    private String ava_aplica_apartados = "";
    private String ava_aplica_cambio_devolucion = "";
    private String aim_url = "";
    private String usu_id;
    private String art_nombre;
    private String cat_nombre;
    private String his_tipo;
    private String his_cantidad;
    private String his_observaciones;
    private String his_fecha_hora_creo;

    public Fragment_pestania_historico() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pestania_historico, container, false);
        fr = getFragmentManager().beginTransaction();

        Button btn_traslados = (Button) v.findViewById(R.id.fragment_inventarios);
        btn_traslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_inventarios()).commit();
            }
        });

        Button btnHistorico = (Button) v.findViewById(R.id.button3);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_traslados()).commit();
            }
        });

        ver_producto_dialog=new Dialog(getContext());
        ver_producto_dialog.setContentView(R.layout.pop_up_ficha_articulos_historico);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        historico = new ArrayList<>();

        tabla_historico = (SortableHistoricoTable) v.findViewById(R.id.tabla_historico);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Artículo", "Categoría", "Movimiento", "Cantidad", "Observaciones");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 3);

        tabla_historico.setHeaderAdapter(simpleHeader);
        tabla_historico.setColumnModel(tableColumnWeightModel);

        Muestra_historico();
        LoadListenerTable();

        tabla_historico.setSwipeToRefreshEnabled(true);
        tabla_historico.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_historico.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        historico.clear();
                        //Muestra_Inventario();
                        refreshIndicator.hide();
                    }
                }, 2000);
            }
        });

        tabla_historico.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        tabla_historico.addDataClickListener(tablaListener);
        return v;
    }

    public void Muestra_historico() {
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
                JSONObject RespuestaFecha = null;
                JSONArray Historicos = null;
                JSONArray imagenes = null;
                JSONArray Articulo = null;
                JSONObject RespuestaUUID = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Resultado = response.getJSONObject("resultado");

//---------------------------------------------------------------------------------------
                        Articulo = Resultado.getJSONArray("Articulos");
                        HistoricoModel = new String[Articulo.length()][4];

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

                            Boolean Disponible_apartados = Boolean.valueOf(elemento.getString("ava_aplica_apartados"));
                            if (Disponible_apartados == true) {
                                ava_aplica_apartados = "si";
                            } else {
                                ava_aplica_apartados = "no";
                            }

                            Boolean Disponible_devoluciones = Boolean.valueOf(elemento.getString("ava_aplica_cambio_devolucion"));
                            if (Disponible_devoluciones == true) {
                                ava_aplica_cambio_devolucion = "si";
                            } else {
                                ava_aplica_cambio_devolucion = "no";
                            }

                            imagenes = elemento.getJSONArray("imagenes");
                            JSONObject elemento3 = imagenes.getJSONObject(0);
                            aim_url = getString(R.string.Url) + elemento3.getString("aim_url");
                        }
//---------------------------------------------------------------------------------------

                        Historicos = Resultado.getJSONArray("Historicos");
                        //inventarioModel = new String[Historicos.length()][4];

                        for (int y = 0; y < Historicos.length(); y++) {
                            JSONObject elementos = Historicos.getJSONObject(y);

                            art_nombre = elementos.getString("art_nombre");
                            cat_nombre = elementos.getString("cat_nombre");
                            his_tipo = elementos.getString("his_tipo");
                            his_cantidad = elementos.getString("his_cantidad");
                            his_observaciones = elementos.getString("his_observaciones");

                            /*RespuestaFecha = elementos.getJSONObject("his_fecha_hora_creo");
                            String Fecha = RespuestaFecha.getString("");*/

                            final HistoricoModel historicos = new HistoricoModel(
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
                                    his_fecha_hora_creo);
                            historico.add(historicos);
                        }
                        final HistoricoAdapter HistoricoAdapter = new HistoricoAdapter(getContext(), historico, tabla_historico);
                         tabla_historico.setDataAdapter(HistoricoAdapter);
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
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequets);
    }

    private void LoadListenerTable(){
        tablaListener = new TableDataClickListener<HistoricoModel>() {
            @Override
            public void onDataClicked(int rowIndex, final HistoricoModel clickedData) {
                final Dialog ver_producto_dialog;
                ver_producto_dialog = new Dialog(getContext());
                ver_producto_dialog.setContentView(R.layout.pop_up_ficha_articulos_historico);
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