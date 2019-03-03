package com.Danthop.bionet;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.OrdenEcommerceAdapter;
import com.Danthop.bionet.Adapters.SincronizarAdapter;
import com.Danthop.bionet.Tables.SortableSincronizarTable;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.SincronizarModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ecommerce_Sincronizar extends Fragment {
    private SortableSincronizarTable tabla_sincronizar;
    private View v;
    private String usu_id;
    private Button btn_pestania_ordenes;
    private Button btn_pestania_preguntas;
    private ArrayList<String> Categoria;
    private ArrayList<String> Articulo;
    private ArrayList<String> Variante;
    private Spinner SpinnerCategoria;

    private Spinner SpinnerArticulo;
    private Spinner SpinnerVariante;

    private ArrayList<String> TipoPublicacionName;
    private ArrayList<String> TipoPublicacionID;

    private ArrayList<String> CategoriaID1;
    private ArrayList<String> CategoriaID2;
    private ArrayList<String> CategoriaID3;
    private ArrayList<String> CategoriaID4;
    private ArrayList<String> CategoriaID5;
    private ArrayList<String> CategoriaID6;

    private ArrayList<String> CategoriaName1;
    private ArrayList<String> CategoriaName2;
    private ArrayList<String> CategoriaName3;
    private ArrayList<String> CategoriaName4;
    private ArrayList<String> CategoriaName5;
    private ArrayList<String> CategoriaName6;


    private String UserML;
    private String AccesToken;
    private String TokenLife;
    private String[][] SincornizarModel;
    private Dialog crear_Producto_dialog;
    private Button btn_alta_articulo;

    private EditText TextNombreArticulo;
    private EditText TextDescripcionArticulo;
    private EditText TextprecioArticulo;
    private ElegantNumberButton TextCantidad;
    private EditText TextGarantia;


    private Spinner SpinnerTipoPublicacion;

    private Spinner SpinnerCategoriaArticulo;
    private Spinner SpinnerCategoriaArticulo2;
    private Spinner SpinnerCategoriaArticulo3;
    private Spinner SpinnerCategoriaArticulo4;
    private Spinner SpinnerCategoriaArticulo5;
    private Spinner SpinnerCategoriaArticulo6;


    private RadioButton RadioUsado;
    private RadioButton RadioNuevo;

    private Button BtnGuardaArticulo;

    private List<SincronizarModel> Sincronizaciones;

    public Fragment_ecommerce_Sincronizar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecommerce_sincronizar,container, false);

        crear_Producto_dialog=new Dialog(getContext());
        crear_Producto_dialog.setContentView(R.layout.pop_up_ecommerce_nuevo_producto);
        Sincronizaciones = new ArrayList<>();


        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString("usu_id","");

        btn_alta_articulo = (Button)v.findViewById(R.id.btn_alta_articulo);

        btn_alta_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crear_Producto_dialog.show();

                TextNombreArticulo = (EditText) crear_Producto_dialog.findViewById(R.id.text_nombre_articulo);
                TextDescripcionArticulo= (EditText) crear_Producto_dialog.findViewById(R.id.text_descripcion_articulo);
                TextprecioArticulo= (EditText) crear_Producto_dialog.findViewById(R.id.text_precio_articulo);
                TextCantidad = (ElegantNumberButton) crear_Producto_dialog.findViewById(R.id.text_cantidad);
                TextGarantia = (EditText) crear_Producto_dialog.findViewById(R.id.text_garantia);

                SpinnerCategoriaArticulo = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_categoria_articulo);
                SpinnerTipoPublicacion = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_tipo_publicacion_articulo);

                SpinnerCategoriaArticulo2 = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_categoria_articulo2);
                SpinnerCategoriaArticulo3 = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_categoria_articulo3);
                SpinnerCategoriaArticulo4 = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_categoria_articulo4);
                SpinnerCategoriaArticulo5 = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_categoria_articulo5);
                SpinnerCategoriaArticulo6 = (Spinner) crear_Producto_dialog.findViewById(R.id.Spinner_categoria_articulo6);

                RadioUsado = (RadioButton) crear_Producto_dialog.findViewById(R.id.radioButton_Usado);
                RadioNuevo = (RadioButton) crear_Producto_dialog.findViewById(R.id.radioButton_Nuevo);

                BtnGuardaArticulo = (Button) crear_Producto_dialog.findViewById(R.id.Guardar_articulo);

                CargaPublicaciones();

                BtnGuardaArticulo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String ApiPath = "https://api.mercadolibre.com/items";

                        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, ApiPath,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        JSONObject jObj = null;
                                        try {
                                            jObj = new JSONObject(response);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(getContext(), "Se Agrego correctamente el producto", Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });


                        String Condiciones="";

                        if (RadioNuevo.isChecked()==true)
                        {
                            Condiciones = "new";
                        }
                        else if (RadioUsado.isChecked()==true)
                        {
                            Condiciones = "used";
                        }

                        String TipoPublicacion;

                        TipoPublicacion = TipoPublicacionID.get(SpinnerTipoPublicacion.getSelectedItemPosition());

                        smr.addStringParam("access_token", AccesToken);
                        smr.addStringParam("title", String.valueOf(TextNombreArticulo.getText()));
                        smr.addStringParam("category_id", "");
                        smr.addStringParam("price", String.valueOf(TextprecioArticulo.getText()));
                        smr.addStringParam("currency_id", "MXN");
                        smr.addStringParam("available_quantity", String.valueOf(TextCantidad.getNumber()));
                        smr.addStringParam("condition", Condiciones);
                        smr.addStringParam("buying_mode", "buy_it_now");
                        smr.addStringParam("warranty", String.valueOf(TextGarantia.getText()));
                        smr.addStringParam("listing_type_id", TipoPublicacion);
                        smr.addStringParam("description", String.valueOf(TextDescripcionArticulo.getText()));
                        smr.addFile("pictures", "1");

                        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
                        mRequestQueue.add(smr);

                    }
                });


                SpinnerCategoriaArticulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        CargaCategoriaNivel2();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });

                SpinnerCategoriaArticulo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        CargaCategoriaNivel3();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });
                SpinnerCategoriaArticulo3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });
                SpinnerCategoriaArticulo4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });

                SpinnerCategoriaArticulo5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });

                SpinnerCategoriaArticulo6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });

            }
        });

        LoadTable();
        LoadButtons();
        LoadSpinners();

        tabla_sincronizar.setSwipeToRefreshEnabled(true);
        tabla_sincronizar.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_sincronizar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Sincronizaciones.clear();
                        LoadTable();
                        refreshIndicator.hide();
                    }
                }, 2000);
            }
        });

        return v;
    }

    public void CargaPublicaciones(){


        final String url = "http://187.189.192.150:8010/api/ecomerce/create_app/access_token=" + AccesToken  + "&expires_in=21600&user_id=" + UserML + "&domains=localhost";

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        JSONArray RespuestaTiposPublicacion = null;
                        JSONArray RespuestaCategoria = null;

                        try
                        {

                            int EstatusApi = Integer.parseInt( response.getString("estatus") );

                            if (EstatusApi == 1) {

                                TipoPublicacionID=new ArrayList<>();
                                CategoriaID1=new ArrayList<>();
                                TipoPublicacionName = new ArrayList<>();
                                CategoriaName1=new ArrayList<>();

                                RespuestaTiposPublicacion = response.getJSONArray("aListaTiposPublicacion");

                                RespuestaCategoria = response.getJSONArray("aCategorias");

                                for(int x = 0; x < RespuestaTiposPublicacion.length(); x++){
                                    JSONObject jsonObject1 = RespuestaTiposPublicacion.getJSONObject(x);
                                    String idpublicacion = jsonObject1.getString("id");
                                    String publicacion = jsonObject1.getString("name");

                                    TipoPublicacionID.add(idpublicacion);
                                    TipoPublicacionName.add(publicacion);
                                }

                                SpinnerTipoPublicacion.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,TipoPublicacionName));


                                for(int x = 0; x < RespuestaCategoria.length(); x++){
                                    JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                    String idcategoria = jsonObject1.getString("id");
                                    String categoria = jsonObject1.getString("name");

                                    CategoriaID1.add(idcategoria);
                                    CategoriaName1.add(categoria);
                                }

                                SpinnerCategoriaArticulo.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,CategoriaName1));

                            }



                        }
                        catch (JSONException e)
                        {   e.printStackTrace();    }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

    }


    public void CargaCategoriaNivel2()
    {

       String Cat1 = CategoriaID1.get(SpinnerCategoriaArticulo.getSelectedItemPosition());

        final String url = "http://187.189.192.150:8010/api/ecomerce/obtenerSubcategoriasMercadoLibre?sIdCategoria=" +  Cat1;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        JSONArray RespuestaTiposPublicacion = null;
                        JSONArray RespuestaCategoria = null;

                        try
                        {

                            int EstatusApi = Integer.parseInt( response.getString("estatus") );

                            if (EstatusApi == 1) {

                                SpinnerCategoriaArticulo2.setVisibility( Integer.parseInt( "1" ) );

                                CategoriaID2=new ArrayList<>();
                                CategoriaName2=new ArrayList<>();

                                RespuestaCategoria = response.getJSONArray("resultado");

                                for(int x = 0; x < RespuestaCategoria.length(); x++){
                                    JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                    String idcategoria = jsonObject1.getString("id");
                                    String categoria = jsonObject1.getString("name");

                                    CategoriaID2.add(idcategoria);
                                    CategoriaName2.add(categoria);
                                }

                                SpinnerCategoriaArticulo2.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,CategoriaName2));

                            }



                        }
                        catch (JSONException e)
                        {   e.printStackTrace();    }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

    }

    public void CargaCategoriaNivel3()
    {
        String Cat2 = CategoriaID2.get(SpinnerCategoriaArticulo.getSelectedItemPosition());

        final String url = "http://187.189.192.150:8010/api/ecomerce/obtenerSubcategoriasMercadoLibre?sIdCategoria=" +  Cat2;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        JSONArray RespuestaTiposPublicacion = null;
                        JSONArray RespuestaCategoria = null;

                        try
                        {

                            int EstatusApi = Integer.parseInt( response.getString("estatus") );

                            if (EstatusApi == 1) {

                                SpinnerCategoriaArticulo3.setVisibility( Integer.parseInt( "1" ) );

                                CategoriaID3=new ArrayList<>();
                                CategoriaName3=new ArrayList<>();

                                RespuestaCategoria = response.getJSONArray("resultado");

                                for(int x = 0; x < RespuestaCategoria.length(); x++){
                                    JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                    String idcategoria = jsonObject1.getString("id");
                                    String categoria = jsonObject1.getString("name");

                                    CategoriaID3.add(idcategoria);
                                    CategoriaName3.add(categoria);
                                }

                                SpinnerCategoriaArticulo3.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,CategoriaName3));

                            }



                        }
                        catch (JSONException e)
                        {   e.printStackTrace();    }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

    }

    public void CargaCategoriaNivel4()
    {

        String Cat3 = CategoriaID3.get(SpinnerCategoriaArticulo.getSelectedItemPosition());

        final String url = "http://187.189.192.150:8010/api/ecomerce/obtenerSubcategoriasMercadoLibre?sIdCategoria=" +  Cat3;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        JSONArray RespuestaTiposPublicacion = null;
                        JSONArray RespuestaCategoria = null;

                        try
                        {

                            int EstatusApi = Integer.parseInt( response.getString("estatus") );

                            if (EstatusApi == 1) {

                                SpinnerCategoriaArticulo4.setVisibility( Integer.parseInt( "1" ) );

                                CategoriaID4=new ArrayList<>();
                                CategoriaName4=new ArrayList<>();

                                RespuestaCategoria = response.getJSONArray("resultado");

                                for(int x = 0; x < RespuestaCategoria.length(); x++){
                                    JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                    String idcategoria = jsonObject1.getString("id");
                                    String categoria = jsonObject1.getString("name");

                                    CategoriaID4.add(idcategoria);
                                    CategoriaName4.add(categoria);
                                }

                                SpinnerCategoriaArticulo4.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,CategoriaName4));

                            }



                        }
                        catch (JSONException e)
                        {   e.printStackTrace();    }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);



    }

    public void CargaCategoriaNivel5()
    {

    }

    public void CargaCategoriaNivel6()
    {

    }

    public void LoadTable(){

       try {
           tabla_sincronizar = (SortableSincronizarTable) v.findViewById(R.id.tabla_sincronizar);

           final String url = "http://187.189.192.150:8010/api/ecomerce/inicio_app/?accesstoken=" + AccesToken  + "&user_id=" + UserML;

           // prepare the Request
           JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                   new Response.Listener<JSONObject>()
                   {
                       @Override
                       public void onResponse(JSONObject response) {
                           // display response
                           JSONObject RespuestaDatos = null;
                           JSONObject RespuestaObjeto = null;
                           JSONObject Respuestaespecificaciones = null;
                           JSONObject Respuestapicture = null;
                           JSONObject Respuestashipping = null;

                           String Titulo;
                           String Disponibilidad;
                           String Precio;
                           String Imagen;
                           String Envio;

                           int x =0;

                           try
                           {

                               int EstatusApi = Integer.parseInt( response.getString("estatus") );

                               if (EstatusApi == 1) {

                                   RespuestaDatos= response.getJSONObject("aDatos");

                                   int numeroregistro =RespuestaDatos.length();

                                   SincornizarModel = new String[numeroregistro][4];

                                   Iterator<String> keys = RespuestaDatos.keys();
                                   while( keys.hasNext() )
                                   {
                                       String key = keys.next();

                                       RespuestaObjeto = RespuestaDatos.getJSONObject(key);

                                       Respuestaespecificaciones = RespuestaObjeto.getJSONObject("especificaciones");

                                       Titulo = Respuestaespecificaciones.getString( "title" );
                                       Disponibilidad = Respuestaespecificaciones.getString( "available_quantity" );
                                       Precio = Respuestaespecificaciones.getString( "price" );

                                       //  Respuestapicture = Respuestaespecificaciones.getJSONObject("pictures");
                                       //  Imagen = Respuestapicture.getString( "url" );

                                       Respuestashipping = Respuestaespecificaciones.getJSONObject( "shipping" );
                                       Envio = Respuestashipping.getString( "free_shipping" );

                                       if (Envio == "true"){
                                           Envio="Si";
                                       }
                                       else
                                       {
                                           Envio="No";
                                       }

                                       final SincronizarModel sincronizar = new SincronizarModel(Titulo, Disponibilidad, Envio,Precio);
                                       Sincronizaciones.add(sincronizar);

                                       x += 1 ;

                                   }

                               }
                               final SincronizarAdapter sincronizarAdapter = new SincronizarAdapter(getContext(), Sincronizaciones, tabla_sincronizar);
                               tabla_sincronizar.setDataAdapter(sincronizarAdapter);


                           }
                           catch (JSONException e)
                           {   e.printStackTrace();    }


                       }
                   },
                   new Response.ErrorListener()
                   {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           Log.d("Error.Response", String.valueOf(error));
                       }
                   }
           );

           VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

       }
       catch (Error e)
       {

       }


    }

    public void LoadButtons(){
        btn_pestania_ordenes = v.findViewById(R.id.btn_pestania_ordenes);
        btn_pestania_ordenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecomerce()).commit();
            }
        });
        btn_pestania_preguntas = v.findViewById(R.id.btn_pestania_preguntas);
        btn_pestania_preguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_popup_ecommerce_preguntas()).commit();
            }
        });
    }

    public void LoadSpinners(){
        Categoria=new ArrayList<>();
        Articulo=new ArrayList<>();
        Variante=new ArrayList<>();

        SpinnerCategoria = v.findViewById(R.id.Combo_categoria);
        SpinnerArticulo = v.findViewById(R.id.Combo_articulo);
        SpinnerVariante = v.findViewById(R.id.Combo_variante);


        Categoria.add("Categoria 1");
        Categoria.add("Categoria 2");
        Categoria.add("Categoria 3");

        Articulo.add("Artículo 1");
        Articulo.add("Artículo 2");
        Articulo.add("Artículo 3");

        Variante.add("Variante 1");
        Variante.add("Variante 2");
        Variante.add("Variante 3");

        SpinnerCategoria.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Categoria));
        SpinnerArticulo.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Articulo));
        SpinnerVariante.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Variante));

    }

}
