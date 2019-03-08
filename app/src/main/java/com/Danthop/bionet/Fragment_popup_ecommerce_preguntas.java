package com.Danthop.bionet;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.PreguntasAdapter;
import com.Danthop.bionet.Adapters.SincronizarAdapter;
import com.Danthop.bionet.Tables.SortablePreguntasTable;
import com.Danthop.bionet.model.Preguntas_Model;
import com.Danthop.bionet.model.SincronizarModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class Fragment_popup_ecommerce_preguntas extends Fragment {


    private SortablePreguntasTable tabla_preguntas;
    private View v;
    private String usu_id;
    private Button btn_pestania_sincronizar;
    private Button btn_pestania_ordenes;
    private ArrayList<String> Articulo;
    private Spinner SpinnerArticulo;

    private String UserML;
    private String AccesToken;
    private String TokenLife;
    ProgressDialog progreso;

    private String[][] PreguntasModel;

    private List<Preguntas_Model> Preguntas;


    public Fragment_popup_ecommerce_preguntas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecommerce_preguntas,container, false);

        Preguntas = new ArrayList<>();

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences( "DatosPersistentes", getActivity().MODE_PRIVATE );

        UserML = sharedPref.getString( "UserIdML", "" );
        AccesToken = sharedPref.getString( "AccessToken", "" );
        TokenLife = sharedPref.getString( "TokenLifetime", "" );
        usu_id = sharedPref.getString("usu_id","");

        LoadTable();
        LoadButtons();
        LoadSpinner();

        tabla_preguntas.setSwipeToRefreshEnabled(true);
        tabla_preguntas.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                tabla_preguntas.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Preguntas.clear();
                        LoadTable();
                        refreshIndicator.hide();
                    }
                }, 2000);
            }
        });

        tabla_preguntas.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));


        return v;
    }


    public void LoadTable(){

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        tabla_preguntas = (SortablePreguntasTable) v.findViewById(R.id.tabla_preguntas);
        final String url = "http://187.189.192.150:8010/api/ecomerce/inicio_app/?accesstoken=" + AccesToken  + "&user_id=" + UserML + "&usu_id=" + usu_id + "&esApp=1";

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        JSONArray RespuestaDatos = null;
                        JSONObject RespuestaObjeto = null;
                        JSONObject Respuestapreguntas = null;
                        JSONArray RespuestaQuestions = null;
                        JSONObject Respuestaespecificaciones =null;
                        JSONObject RespuestaComprador = null;


                        String Titulo;
                        String preguntas;
                        String idcomprador;
                        String comprador="";

                        try
                        {

                            int EstatusApi = Integer.parseInt( response.getString("estatus") );

                            if (EstatusApi == 1) {

                                RespuestaDatos= response.getJSONArray("aDatos");

                                int numeroregistro =RespuestaDatos.length();

                                PreguntasModel = new String[numeroregistro][4];

                                for (int x = 0; x < RespuestaDatos.length(); x++) {
                                    //JSONObject elemento = RespuestaDatos.getJSONObject(x);

                                    RespuestaObjeto = RespuestaDatos.getJSONObject(x);


                                    Respuestapreguntas = RespuestaObjeto.getJSONObject("preguntas");

                                    Respuestaespecificaciones = RespuestaObjeto.getJSONObject("especificaciones");

                                    Titulo = Respuestaespecificaciones.getString( "title" );

                                    RespuestaQuestions = Respuestapreguntas.getJSONArray("questions");

                                    for(int i = 0; i < RespuestaQuestions.length(); i++) {

                                        JSONObject elemento = RespuestaQuestions.getJSONObject(i);

                                        preguntas = elemento.getString("text");

                                        idcomprador = elemento.getJSONObject("from").getString( "id");

                                        RespuestaComprador = response.getJSONObject("aUsuariosQuePregunta");

                                        Iterator<String> keys2 = RespuestaComprador.keys();
                                        while( keys2.hasNext() )
                                        {

                                            String keyidcomp = keys2.next();

                                            String Valor1= String.valueOf(idcomprador);
                                            String Valor2= String.valueOf(keyidcomp);

                                            if(!Valor1.equals(Valor2)) {

                                                RespuestaObjeto = RespuestaComprador.getJSONObject(keyidcomp);

                                                comprador = RespuestaObjeto.getString("nickname");
                                            }

                                        }


                                        PreguntasModel[i][0] = preguntas;
                                        PreguntasModel[i][1] = comprador;
                                        PreguntasModel[i][2] = Titulo;

                                        final Preguntas_Model pregunta = new Preguntas_Model(preguntas, comprador, Titulo);
                                        Preguntas.add(pregunta);


                                    }


                                }

                            }

                            final PreguntasAdapter preguntasAdapter = new PreguntasAdapter(getContext(), Preguntas, tabla_preguntas);
                            tabla_preguntas.setDataAdapter(preguntasAdapter);

                            progreso.hide();

                        }
                        catch (JSONException e)
                        {   e.printStackTrace();

                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(e), Toast.LENGTH_LONG);

                            progreso.hide();

                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));

                        Toast toast1 =
                                Toast.makeText(getContext(),
                        String.valueOf(error), Toast.LENGTH_LONG);

                        progreso.hide();
                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

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
        btn_pestania_sincronizar = v.findViewById(R.id.btn_pestania_sincronizar);
        btn_pestania_sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();
            }
        });
    }

    public void LoadSpinner(){
        Articulo=new ArrayList<>();

        SpinnerArticulo = v.findViewById(R.id.Combo_articulo);

        Articulo.add("Artículo 1");
        Articulo.add("Artículo 2");
        Articulo.add("Artículo 3");

        SpinnerArticulo.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,Articulo));

    }


}
