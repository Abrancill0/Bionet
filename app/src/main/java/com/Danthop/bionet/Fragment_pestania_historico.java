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
import android.widget.Button;
import android.widget.SearchView;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private String observacion;
    private String his_fecha_hora_creo;
    private String Sucursal;
    private Long timestamp;
    private String FechaconFormato;
    private ProgressDialog progressDialog;

    private HistoricoAdapter HistoricoAdapter;
    private SearchView Buscar;

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
                onDetach();
            }
        });

        Button btnHistorico = (Button) v.findViewById(R.id.button3);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_traslado()).commit();
                onDetach();
            }
        });

        Button btnInventariExistencias = (Button) v.findViewById(R.id.btnBuscarExistencias);
        btnInventariExistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_pestania_inventario_existencias()).commit();
                onDetach();
            }
        });

        Buscar = v.findViewById(R.id.buscar_historico);
        Buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                HistoricoAdapter.getFilter().filter(newText);
                return false;
            }
        });


        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        historico = new ArrayList<>();
        ver_producto_dialog=new Dialog(getContext());
        ver_producto_dialog.setContentView(R.layout.pop_up_ficha_articulos_historico);


        tabla_historico = (SortableHistoricoTable) v.findViewById(R.id.tabla_historico);
        final SimpleTableHeaderAdapter simpleHeader = new SimpleTableHeaderAdapter(getContext(), "Artículo", "Categoría", "Sucursal", "Movimiento", "Cantidad", "Observaciones", "Fecha");
        simpleHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        simpleHeader.setTextSize( 18 );
        simpleHeader.setPaddings(5,5,5,5);

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(7);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 2);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(5, 2);
        tableColumnWeightModel.setColumnWeight(6, 2);

        tabla_historico.setHeaderAdapter(simpleHeader);
        tabla_historico.setColumnModel(tableColumnWeightModel);

        Muestra_historico();
        return v;
    }

    public void Muestra_historico() {

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/inventario/index?usu_id=" + usu_id + "&esApp=1";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject Resultado = null;
                JSONArray aHistoricos = null;
                JSONObject RespuestaUUID = null;
                JSONObject RespuestaFecha = null;

                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        progressDialog.dismiss();
                        Resultado = response.getJSONObject("resultado");

                        aHistoricos = Resultado.getJSONArray("aHistoricos");
                        HistoricoModel = new String[aHistoricos.length()][4];

                        for (int x = 0; x < aHistoricos.length(); x++) {
                            JSONObject elemento = aHistoricos.getJSONObject(x);

                            RespuestaUUID = elemento.getJSONObject("his_id");
                            String UUID = RespuestaUUID.getString("uuid");

                            art_nombre = elemento.getString("art_nombre");
                            cat_nombre = elemento.getString("cat_nombre");
                            his_tipo = elemento.getString("his_tipo");
                            his_cantidad = elemento.getString("his_cantidad");

                            String his_nombre_sucursal = elemento.getString( "his_nombre_sucursal" );
                            String his_numero_sucursal = elemento.getString( "his_numero_sucursal" );
                            Sucursal = his_nombre_sucursal + "(" + his_numero_sucursal + ")";

                            RespuestaFecha = elemento.getJSONObject("his_fecha_hora_creo");
                            timestamp = RespuestaFecha.getLong("seconds");

                            Timestamp stamp = new Timestamp(timestamp);
                            Date date_f = new Date(stamp.getTime() * 1000L);

                            String Formato = "dd/MM/yyyy";
                            SimpleDateFormat formatter = new SimpleDateFormat(Formato);

                            FechaconFormato = formatter.format(date_f.getTime());

                            String var = elemento.getString("his_observaciones");
                            if (var == "null"){
                                observacion = "Sin observaciones";
                            }else {
                                observacion = elemento.getString("his_observaciones");
                            }

                            final HistoricoModel historicos = new HistoricoModel(
                                    sku,
                                    producto,
                                    existencia,
                                    categoria,
                                    modificadores,
                                    Sucursal,
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
                                    observacion,
                                    FechaconFormato);
                            historico.add(historicos);
                        }
                        HistoricoAdapter = new HistoricoAdapter(getContext(), historico, tabla_historico);
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
        VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );
    }
//----------------------------------------------------------------------------------------------------------------------------------------
   /* private void LoadListenerTable(){
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
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
    }
}