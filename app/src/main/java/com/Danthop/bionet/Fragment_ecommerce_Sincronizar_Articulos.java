package com.Danthop.bionet;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ArticuloAdapter;
import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;

public class Fragment_ecommerce_Sincronizar_Articulos extends Fragment {
    private View v;
    private ArrayList<String> TipoPublicacionName;
    private ArrayList<String> TipoPublicacionID;
    private Spinner SpinnerTipoPublicacion;
    private String usu_id;
    private String AccesToken;
    private String UserIdML;
    private Button atras;
    private List<com.Danthop.bionet.model.ArticuloModel> Articulos;
    private String[][] ArticuloModel;
    private FragmentTransaction fr;
    SortableArticulosTable tb;

    public Fragment_ecommerce_Sincronizar_Articulos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecommerce_seleccionarticulos, container, false);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", getActivity().MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        AccesToken = sharedPref.getString("AccessToken", "");
        UserIdML = sharedPref.getString("UserIdML", "");
        fr = getFragmentManager().beginTransaction();

        tb = v.findViewById(R.id.tablaArticulos);
        CargaArticulos();

        ImageView regresar = v.findViewById(R.id.atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();
            }
        });

        final TableDataClickListener<ArticuloModel> ArticuloListener = new TableDataClickListener<ArticuloModel>() {
            @Override
            public void onDataClicked(int rowIndex, ArticuloModel clickedData) {

                String Nombre = (clickedData.getarticulo_Nombre());
                String Precio = (clickedData.getarticulo_Precio());
                String UUID = (clickedData.getarticulo_UUID());
                String Descripcion = (clickedData.getarticulo_Descripcion());
                String image1 = (clickedData.getArticulo_Imagen1());
                String image2 = (clickedData.getArticulo_Imagen2());
                String Cantidad = (clickedData.getArticulo_cantidad());

                String Sucursal = (clickedData.getArticulo_sucursal());
                String Sucursal_UUID = (clickedData.getArticulo_sucursal_uuid());
                String Exi_ID = (clickedData.getArticulo_articulo_exi_id());

                Bundle bundle = new Bundle();
                bundle.putString("nombre", Nombre);
                bundle.putString("descripcion", Descripcion);
                bundle.putString("precio", Precio);
                bundle.putString("usu_id", usu_id);
                bundle.putString("image1", image1);
                bundle.putString("image2", image2);
                bundle.putString("cantidad", Cantidad);
                bundle.putString("Sucursal", Sucursal);
                bundle.putString("Sucursal_UUID", Sucursal_UUID);
                bundle.putString("Exi_ID", Exi_ID);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_selecciona_tipo_publicacion secondFragment = new Fragment_selecciona_tipo_publicacion();
                //Fragment_selecciona_categoria secondFragment = new Fragment_selecciona_categoria();
                secondFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, secondFragment);
                fragmentTransaction.commit();
            }
        };
        tb.addDataClickListener(ArticuloListener);
        tb.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        return v;
    }
//------------------------------------------------------------------------------------------------------
    private void CargaArticulos() {

        try {

       String url = getString(R.string.Url);
        String ApiPath = url + "/api/ecommerce/inicio_app?accesstoken=" + AccesToken + "&user_id_mercado_libre=" + UserIdML + "&usu_id=" + usu_id + "&esApp=1";
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONArray RespuestaArticulos = null;
                    JSONObject RespuestaUUID = null;
                    JSONObject RespuestaPrecio = null;
                    JSONArray RespuestaImagenes = null;

                    JSONObject RespuestaPrecioModificador = null;
                    JSONObject Respuesta_Cantidad = null;
                    JSONObject Respuesta_exi_id = null;

                    String RutaImagen1="";
                    String RutaImagen2="";

                    try {
                        int EstatusApi = Integer.parseInt(response.getString("estatus"));
                        Articulos = new ArrayList<>();

                        if (EstatusApi == 1) {

                            RespuestaArticulos = response.getJSONArray("aArticulos");

                            for (int x = 0; x < RespuestaArticulos.length(); x++) {
                                JSONObject elemento = RespuestaArticulos.getJSONObject(x);

                                RespuestaUUID = elemento.getJSONObject("art_id");
                                String UUID = RespuestaUUID.getString( "uuid");

                                RespuestaPrecio = elemento.getJSONObject("ava_precio");
                                String Precio = RespuestaPrecio.getString( "value");

                                //EXISTENCIAS
                                Respuesta_Cantidad = elemento.getJSONObject("exi_cantidad");
                                String cantidad = Respuesta_Cantidad.getString("value");

                                //exi_id
                                Respuesta_exi_id = elemento.getJSONObject("exi_id");
                                String exi_id = Respuesta_exi_id.getString("uuid");
                                //Sucursal
                                String Sucursal_id =elemento.getString( "suc_id");
                                String Sucursal_nombre =elemento.getString( "suc_nombre");

                                //VERIFICAR MODIFICADORES
                                String NombreCompleto="";
                                String NombreArticulo = elemento.getString("art_nombre");
                                String NombreVariante = elemento.getString( "ava_nombre");
                                String Descripcion = elemento.getString( "art_descripcion");

                                String NombreModificador="";
                                String Modificadores = elemento.getString( "ava_tiene_modificadores");

                                RespuestaImagenes = elemento.getJSONArray( "imagenes");

                                for (int z = 0; z < RespuestaImagenes.length(); z++) {

                                    JSONObject elemento3 = RespuestaImagenes.getJSONObject(z);

                                    if(RutaImagen1.equals("")) {
                                        RutaImagen1 = elemento3.getString( "aim_url");
                                    } else {
                                        RutaImagen2 = elemento3.getString( "aim_url");
                                    }
                                }

                                if (Modificadores == "true"){
                                        NombreModificador = elemento.getString( "mod_nombre");
                                        RespuestaPrecioModificador = elemento.getJSONObject( "amo_precio");
                                        Precio = RespuestaPrecioModificador.getString( "value");

                                        NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                        final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion, Precio,RutaImagen1,RutaImagen2,"","",cantidad,""
                                        ,"","","",Sucursal_nombre,Sucursal_id,exi_id);
                                        Articulos.add(Articulo);
                                }else {
                                    NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                    final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion,Precio,RutaImagen1,RutaImagen2,"","",cantidad,"",
                                            "","","",Sucursal_nombre,Sucursal_id,exi_id);
                                    Articulos.add(Articulo);
                                }
                            }
                            final ArticuloAdapter ArticuloAdapter = new ArticuloAdapter(getContext(), Articulos, tb);
                            tb.setDataAdapter(ArticuloAdapter);
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
            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
    } catch (Error e) {
        e.printStackTrace();
    }
    }
//--------------------------------------------------------------------------------
}