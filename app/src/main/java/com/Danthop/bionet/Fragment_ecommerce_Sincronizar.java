package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.Danthop.bionet.Class.MyFirebaseInstanceService;
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

    private Spinner SpinnerArticulo;
    private Spinner SpinnerVariante;

    private RadioButton RadioUsado;
    private RadioButton RadioNuevo;

    private Button BtnGuardaArticulo;

    ProgressDialog progreso;

    private List<SincronizarModel> Sincronizaciones;

    public Fragment_ecommerce_Sincronizar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate( R.layout.fragment_ecommerce_sincronizar, container, false );

        // crear_Producto_dialog=new Dialog(getContext());
        // crear_Producto_dialog.setContentView(R.layout.pop_up_ecommerce_nuevo_producto);
        Sincronizaciones = new ArrayList<>();

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString( "usu_id", "" );

        btn_alta_articulo = (Button) v.findViewById( R.id.btn_alta_articulo );

        LoadButtons();
        LoadSpinners();
        LoadTable();

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

        btn_alta_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 FragmentTransaction fr = getFragmentManager().beginTransaction();
                 fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar_Articulos()).commit();

            }
        });

        tabla_sincronizar.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));



        return  v;
    }

    public void LoadTable(){


        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

       try {
           tabla_sincronizar = (SortableSincronizarTable) v.findViewById(R.id.tabla_sincronizar);

           String url = getString(R.string.Url);

           final String ApiPath = url + "/api/ecomerce/inicio_app/?accesstoken=" + AccesToken  + "&user_id=" + UserML + "&usu_id=" + usu_id + "&esApp=1";

           // prepare the Request
           JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                   new Response.Listener<JSONObject>()
                   {
                       @Override
                       public void onResponse(JSONObject response) {
                           // display response
                           JSONArray RespuestaDatos = null;
                           JSONObject RespuestaDatos2 = null;
                           JSONObject RespuestaObjeto = null;
                           JSONObject Respuestaespecificaciones = null;
                           JSONObject Respuestapicture = null;
                           JSONObject Respuestashipping = null;

                           String Titulo;
                           String Disponibilidad;
                           String Precio;
                           String Imagen;
                           String Envio;

                           try
                           {

                               int EstatusApi = Integer.parseInt( response.getString("estatus") );

                               if (EstatusApi == 1) {

                                   //RespuestaDatos2= response.getJSONObject("aDatos");

                                   RespuestaDatos= response.getJSONArray("aDatos");

                                   int numeroregistro =RespuestaDatos.length();

                                   SincornizarModel = new String[numeroregistro][4];

                                   for (int x = 0; x < RespuestaDatos.length(); x++) {

                                       JSONObject elemento = RespuestaDatos.getJSONObject(x);

                                       Respuestaespecificaciones = elemento.getJSONObject( "especificaciones" );

                                       Titulo = Respuestaespecificaciones.getString( "title" );
                                       Disponibilidad = Respuestaespecificaciones.getString( "available_quantity" );
                                       Precio = Respuestaespecificaciones.getString( "price" );

                                       //  Respuestapicture = Respuestaespecificaciones.getJSONObject("pictures");
                                       //  Imagen = Respuestapicture.getString( "url" );

                                       Respuestashipping = Respuestaespecificaciones.getJSONObject( "shipping" );
                                       Envio = Respuestashipping.getString( "free_shipping" );

                                       if (Envio == "true") {
                                           Envio = "Si";
                                       } else {
                                           Envio = "No";
                                       }

                                       final SincronizarModel sincronizar = new SincronizarModel( Titulo, Disponibilidad, Envio, Precio );
                                       Sincronizaciones.add( sincronizar );
                                   }

                               }
                               final SincronizarAdapter sincronizarAdapter = new SincronizarAdapter(getContext(), Sincronizaciones, tabla_sincronizar);
                               tabla_sincronizar.setDataAdapter(sincronizarAdapter);

                               progreso.hide();

                           }
                           catch (JSONException e)
                           {   e.printStackTrace();

                               Toast toast1 =
                                       Toast.makeText(getContext(),
                                               e.toString(), Toast.LENGTH_LONG);

                               progreso.hide();

                           }


                       }
                   },
                   new Response.ErrorListener()
                   {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                          // Log.d("Error.Response", String.valueOf(error));

                           Toast toast1 =
                                   Toast.makeText(getContext(),
                                           String.valueOf(error), Toast.LENGTH_LONG);

                           progreso.hide();
                       }
                   }
           );

           VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

       }
       catch (Error e)
       {

           Toast toast1 =
                   Toast.makeText(getContext(),
                           String.valueOf(e), Toast.LENGTH_LONG);

           progreso.hide();

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

    