package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.OrdenEcommerceAdapter;
import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
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

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ecomerce extends Fragment {
    private SortableOrdenEcommerceTable tabla_ecomerce;
    private View v;
    private String usu_id;
    private Button btn_pestania_sincronizar;
    private Button btn_pestania_preguntas;
    private Button btn_sincronizar;
    private Dialog dialog;
    private static final int REQUEST_CODE = 999;
    private String UserML;
    private String AccesToken;
    private String TokenLife;
    private JSONObject RespuestaTodo = null;
    private String RespuestaTodoString;

    private List<Ecommerce_orden_Model> Ordenes;

    ProgressDialog progreso;

    private String[][] OrdenesModel;

    public Fragment_ecomerce() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate( R.layout.fragment_ecomerce, container, false );

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );
        Ordenes = new ArrayList<>();

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString( "usu_id", "" );

        Bundle bundle = getArguments();

        if (bundle != null) {
            String json = bundle.getString( "Resultado" );

            RespuestaTodoString = bundle.getString( "Resultado" );

            try {
                JSONObject obj = new JSONObject( json );
                CargaDatos( obj );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            LoadTable();
        }


        LoadButtons();

        tabla_ecomerce.setSwipeToRefreshEnabled( true );
        tabla_ecomerce.setSwipeToRefreshListener( new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_ecomerce.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        Ordenes.clear();
                        LoadTable();
                        refreshIndicator.hide();
                    }
                }, 2000 );
            }
        } );

        tabla_ecomerce.setEmptyDataIndicatorView( v.findViewById( R.id.Tabla_vacia ) );

        return v;
    }

    public void CargaDatos(JSONObject Datos) {
        try {

            tabla_ecomerce = (SortableOrdenEcommerceTable) v.findViewById( R.id.tabla_ecommerce );

            progreso = new ProgressDialog( getContext() );
            progreso.setMessage( "Cargando..." );
            progreso.show();

            JSONObject RespuestaHistorial = null;
            JSONArray RespuestaHistorialResult = null;
            JSONObject RespuestaBuyer = null;
            JSONObject RespuestaShipping = null;
            JSONArray RespuestaShippingItems = null;
            JSONArray RespuestaPayments = null;

            String Estatus;
            String Comprador;
            String Descripcion = "";
            String Cantidad = "";
            String Importe = "";
            String Envio = "";

            try {

                int EstatusApi = Integer.parseInt( Datos.getString( "estatus" ) );

                RespuestaHistorial = Datos.getJSONObject( "aHistorial" );

                RespuestaTodo = Datos;

                RespuestaHistorialResult = RespuestaHistorial.getJSONArray( "results" );

                OrdenesModel = new String[RespuestaHistorialResult.length()][6];

                for (int x = 0; x < RespuestaHistorialResult.length(); x++) {

                    JSONObject elemento = RespuestaHistorialResult.getJSONObject( x );

                    Estatus = elemento.getString( "status" );
                    RespuestaBuyer = elemento.getJSONObject( "buyer" );
                    Comprador = RespuestaBuyer.getString( "first_name" ) + " " + RespuestaBuyer.getString( "last_name" );
                    RespuestaShipping = elemento.getJSONObject( "shipping" );
                    RespuestaShippingItems = RespuestaShipping.getJSONArray( "shipping_items" );

                    for (int i = 0; i < RespuestaShippingItems.length(); i++) {

                        JSONObject elementoSI = RespuestaShippingItems.getJSONObject( i );

                        Descripcion = elementoSI.getString( "description" );
                        Cantidad = elementoSI.getString( "quantity" );
                    }

                    RespuestaPayments = elemento.getJSONArray( "payments" );

                    for (int z = 0; z < RespuestaPayments.length(); z++) {

                        JSONObject elementoP = RespuestaPayments.getJSONObject( z );

                        Importe = elementoP.getString( "transaction_amount" );
                        Envio = elementoP.getString( "shipping_cost" );
                    }

                    final Ecommerce_orden_Model orden = new Ecommerce_orden_Model( Comprador, Descripcion, Cantidad, Envio, Importe, Estatus );
                    Ordenes.add( orden );
                }
                final OrdenEcommerceAdapter ordenAdapter = new OrdenEcommerceAdapter( getContext(), Ordenes, tabla_ecomerce );
                tabla_ecomerce.setDataAdapter( ordenAdapter );


                progreso.hide();


            } catch (JSONException e) {

                Toast toast1 =
                        Toast.makeText( getContext(),
                                e.toString(), Toast.LENGTH_LONG );

                toast1.show();

                progreso.hide();
            }

        } catch (Error e) {

            Toast toast1 =
                    Toast.makeText( getContext(),
                            e.toString(), Toast.LENGTH_LONG );

            toast1.show();

            progreso.hide();

        }

    }

    public void LoadTable() {

        try {

            tabla_ecomerce = (SortableOrdenEcommerceTable) v.findViewById( R.id.tabla_ecommerce );

            progreso = new ProgressDialog( getContext() );
            progreso.setMessage( "Cargando..." );
            progreso.show();

            String url = getString( R.string.Url );

            final String ApiPath = url + "/api/ecomerce/inicio_app/?accesstoken=" + AccesToken + "&user_id=" + UserML + "&usu_id=" + usu_id + "&esApp=1";

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            JSONObject RespuestaHistorial = null;
                            JSONArray RespuestaHistorialResult = null;
                            JSONObject RespuestaBuyer = null;
                            JSONObject RespuestaShipping = null;
                            JSONArray RespuestaShippingItems = null;
                            JSONArray RespuestaPayments = null;

                            String Estatus;
                            String Comprador;
                            String Descripcion = "";
                            String Cantidad = "";
                            String Importe = "";
                            String Envio = "";

                            try {

                                int EstatusApi = Integer.parseInt( response.getString( "estatus" ) );

                                if (EstatusApi == 1) {

                                    RespuestaHistorial = response.getJSONObject( "aHistorial" );

                                    RespuestaTodo = response;

                                    RespuestaHistorialResult = RespuestaHistorial.getJSONArray( "results" );

                                    OrdenesModel = new String[RespuestaHistorialResult.length()][6];

                                    for (int x = 0; x < RespuestaHistorialResult.length(); x++) {

                                        JSONObject elemento = RespuestaHistorialResult.getJSONObject( x );

                                        Estatus = elemento.getString( "status" );
                                        RespuestaBuyer = elemento.getJSONObject( "buyer" );
                                        Comprador = RespuestaBuyer.getString( "first_name" ) + " " + RespuestaBuyer.getString( "last_name" );
                                        RespuestaShipping = elemento.getJSONObject( "shipping" );
                                        RespuestaShippingItems = RespuestaShipping.getJSONArray( "shipping_items" );

                                        for (int i = 0; i < RespuestaShippingItems.length(); i++) {

                                            JSONObject elementoSI = RespuestaShippingItems.getJSONObject( i );

                                            Descripcion = elementoSI.getString( "description" );
                                            Cantidad = elementoSI.getString( "quantity" );
                                        }

                                        RespuestaPayments = elemento.getJSONArray( "payments" );

                                        for (int z = 0; z < RespuestaPayments.length(); z++) {

                                            JSONObject elementoP = RespuestaPayments.getJSONObject( z );

                                            Importe = elementoP.getString( "transaction_amount" );
                                            Envio = elementoP.getString( "shipping_cost" );
                                        }

                                        final Ecommerce_orden_Model orden = new Ecommerce_orden_Model( Comprador, Descripcion, Cantidad, Envio, Importe, Estatus );
                                        Ordenes.add( orden );
                                    }
                                    final OrdenEcommerceAdapter ordenAdapter = new OrdenEcommerceAdapter( getContext(), Ordenes, tabla_ecomerce );
                                    tabla_ecomerce.setDataAdapter( ordenAdapter );


                                    progreso.hide();

                                } else {

                                    String Mensaje = response.getString( "resultado" );
                                    Toast toast1 =
                                            Toast.makeText( getContext(),
                                                    Mensaje, Toast.LENGTH_LONG );

                                    toast1.show();

                                    progreso.hide();

                                }


                            } catch (JSONException e) {

                                Toast toast1 =
                                        Toast.makeText( getContext(),
                                                e.toString(), Toast.LENGTH_LONG );

                                toast1.show();

                                progreso.hide();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.d("Error.Response", String.valueOf(error));

                            Toast toast1 =
                                    Toast.makeText( getContext(),
                                            error.toString(), Toast.LENGTH_LONG );

                            toast1.show();

                            progreso.hide();
                        }
                    }
            );

            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( getRequest );


        } catch (Error e) {

            Toast toast1 =
                    Toast.makeText( getContext(),
                            e.toString(), Toast.LENGTH_LONG );

            toast1.show();

            progreso.hide();

        }


    }

    public void LoadButtons() {
        dialog = new Dialog( getContext() );
        btn_pestania_sincronizar = v.findViewById( R.id.btn_pestania_sincronizar );
        btn_pestania_sincronizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString( "Resultado", String.valueOf( RespuestaTodo ) );

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_ecommerce_Sincronizar secondFragment = new Fragment_ecommerce_Sincronizar();
                secondFragment.setArguments( bundle );

                fragmentTransaction.replace( R.id.fragment_container, secondFragment );
                fragmentTransaction.commit();

            }
        } );
        btn_pestania_preguntas = v.findViewById( R.id.btn_pestania_preguntas );
        btn_pestania_preguntas.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString( "Resultado", String.valueOf( RespuestaTodo ) );

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_popup_ecommerce_preguntas secondFragment = new Fragment_popup_ecommerce_preguntas();
                secondFragment.setArguments( bundle );

                fragmentTransaction.replace( R.id.fragment_container, secondFragment );
                fragmentTransaction.commit();

            }
        } );
        //btn_sincronizar = v.findViewById(R.id.btn_sincronizar);
        // btn_sincronizar.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //   public void onClick(View v) {
        //       dialog.setContentView(R.layout.pop_up_sincronizar);
        //       dialog.show();
        //       btn_sincronizar_no = dialog.findViewById(R.id.btn_sincronizar_no);
        //       btn_sincronizar_si = dialog.findViewById(R.id.btn_sincronizar_si);


        //       btn_sincronizar_no.setOnClickListener(new View.OnClickListener() {
        //           @Override
        //           public void onClick(View v) {
        //               dialog.dismiss();
        //           }
        //       });
        //       btn_sincronizar_si.setOnClickListener(new View.OnClickListener() {
        //           @Override
        //           public void onClick(View v) {
        //               dialog.setContentView(R.layout.pop_up_confimacion_sincronizar);
        //               btn_aceptar_cerrar_ventana = dialog.findViewById(R.id.aceptar_cerrar_ventana1);

        //               btn_aceptar_cerrar_ventana.setOnClickListener(new View.OnClickListener() {
        //                   @Override
        //                   public void onClick(View v) {
        //                       dialog.dismiss();
        //                   }
        //               });
        //          }
        //       });


        //  }
        //});
    }

}
