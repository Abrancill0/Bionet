package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.DetalleApartadoAdapter;
import com.Danthop.bionet.Adapters.HistorialClientesAdapter;
import com.Danthop.bionet.Adapters.NewMovimientoAdapter;
import com.Danthop.bionet.Adapters.SeleccionarArticuloVentaAdapter;
import com.Danthop.bionet.Adapters.VentaArticuloAdapter;
import com.Danthop.bionet.Tables.SortableApartadoDetalleTable;
import com.Danthop.bionet.Tables.SortableClientesHistorialTable;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.Sortable_new_movimientosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.ConfiguracionLealtadModel;
import com.Danthop.bionet.model.MovimientoModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.sortabletableview.recyclerview.toolkit.FilterHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_new_movimientos extends Fragment {
    private Sortable_new_movimientosTable Movimientos_table;
    private List<MovimientoModel> Movimientos;

    public Fragment_new_movimientos() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_movimientos,container, false);
        Movimientos_table = v.findViewById(R.id.table_movimientos);
        MovimientoModel movimiento = new MovimientoModel ("EEDFQ",
                "322",
                "DDW",
                "32",
                "E21",
                "DASD");
        Movimientos = new ArrayList<>();
        Movimientos.add(movimiento);







        return v;

    }


    /*private void CargaArticulos() {
        progressDialog.show();

        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/tickets/select-articulos?usu_id=" + usu_id + "&esApp=1&code="+code+"&tic_id_sucursal="+SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

            // prepare the Request
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject RespuestaResultado = null;
                            JSONArray RespuestaImagenes = null;
                            JSONObject RespuestaUUID = null;
                            JSONObject RespuestaPrecio = null;

                            String RutaImagen1 = "";
                            String RutaImagen2 = "";

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                Articulos = new ArrayList<>();

                                if (EstatusApi == 1) {

                                    RespuestaResultado = response.getJSONObject("resultado");
                                    JSONArray NodoArticulos = RespuestaResultado.getJSONArray("aArticulos");

                                    for (int x = 0; x < NodoArticulos.length(); x++) {
                                        JSONObject elemento = NodoArticulos.getJSONObject(x);

                                        RespuestaUUID = elemento.getJSONObject("art_id");
                                        String UUID = RespuestaUUID.getString("uuid");

                                        RespuestaPrecio = elemento.getJSONObject("ava_precio");
                                        String Precio = RespuestaPrecio.getString("value");
                                        //VERIFICAR MODIFICADORES
                                        String NombreCompleto = "";
                                        String NombreArticulo = elemento.getString("art_nombre");
                                        String NombreVariante = elemento.getString("ava_nombre");
                                        String Descripcion = elemento.getString("art_descripcion");
                                        String Categoria = elemento.getString("cat_nombre");
                                        String SKU = elemento.getString("ava_sku");

                                        String NombreModificador = "";
                                        String Modificadores = elemento.getString("ava_tiene_modificadores");

                                        RespuestaImagenes = elemento.getJSONArray("imagenes");

                                        for (int z = 0; z < RespuestaImagenes.length(); z++) {

                                            JSONObject elemento3 = RespuestaImagenes.getJSONObject(z);

                                            if (RutaImagen1.equals("")) {
                                                RutaImagen1 = elemento3.getString("aim_url");
                                            } else {
                                                RutaImagen2 = elemento3.getString("aim_url");
                                            }

                                        }

                                        String Sucursal = elemento.getString("suc_nombre");
                                        if (Sucursal.equals(SpinnerSucursal.getSelectedItem())) {
                                            if (Modificadores == "true") {
                                                NombreModificador = elemento.getString("mod_nombre");

                                                Precio = elemento.getString("amo_precio_lista");

                                                System.out.println(NombreVariante);
                                                NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                                SKU = elemento.getString("amo_sku");

                                                final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion, Precio,RutaImagen1,
                                                        RutaImagen2,SKU,Categoria,"","","","",
                                                        "","","","");
                                                Articulos.add(Articulo);
                                            } else {
                                                NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;


                                                final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion,Precio,RutaImagen1,
                                                        RutaImagen2,SKU,Categoria,"","","","",
                                                        "","","","");
                                                Articulos.add(Articulo);
                                            }
                                        }

                                    }
                                    final SeleccionarArticuloVentaAdapter ArticuloAdapter = new SeleccionarArticuloVentaAdapter(getContext(), Articulos, tabla_selecciona_articulo);
                                    tabla_selecciona_articulo.setDataAdapter(ArticuloAdapter);
                                    progressDialog.dismiss();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast toast1 =
                                            Toast.makeText(getContext(), "Error en el servidor", Toast.LENGTH_LONG);
                                    toast1.show();
                                }
                            } catch (JSONException e) {
                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                String.valueOf(e), Toast.LENGTH_LONG);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(error), Toast.LENGTH_LONG);
                        }
                    }
            );
            postRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
    }*/


}
