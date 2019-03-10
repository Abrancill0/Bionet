package com.Danthop.bionet;


import android.app.ProgressDialog;
import android.content.Context;
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
    private String RespuestaTodo;

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

        Bundle bundle = getArguments();

        String json = bundle.getString( "Resultado");
        RespuestaTodo = bundle.getString( "Resultado");

        try {
            JSONObject obj = new JSONObject(json);
            CargaDatos(obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        //    LoadTable();
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


    public void CargaDatos(JSONObject Datos)
    {

        try
        {

            progreso = new ProgressDialog(getContext());
            progreso.setMessage("Cargando...");
            progreso.show();

            tabla_preguntas = (SortablePreguntasTable) v.findViewById(R.id.tabla_preguntas);

            JSONArray RespuestaDatos = null;
            JSONArray RespuestaDatos2 = null;
            JSONObject RespuestaObjeto = null;
            JSONObject RespuestaObjeto2 = null;
            JSONObject Respuestapreguntas = null;
            JSONObject Respuestapreguntas2 = null;
            JSONArray RespuestaQuestions = null;
            JSONArray RespuestaQuestions2 = null;
            JSONObject Respuestaespecificaciones =null;
            JSONArray RespuestaComprador = null;

            String Titulo;
            String preguntas;
            String idcomprador;
            String id_pregunta="";
            String comprador="";
            int numeroregistro =0;


                RespuestaDatos= Datos.getJSONArray("aDatos");

                RespuestaDatos2= Datos.getJSONArray("aDatos");

                for (int z = 0; z < RespuestaDatos2.length(); z++) {

                    RespuestaObjeto2 = RespuestaDatos2.getJSONObject(z);

                    Respuestapreguntas2 = RespuestaObjeto2.getJSONObject("preguntas");

                    int preguntitas = Respuestapreguntas2.getInt( "total");

                    numeroregistro += Respuestapreguntas2.getInt( "total");

                }

                PreguntasModel = new String[numeroregistro][5];


                for (int x = 0; x < RespuestaDatos.length(); x++) {

                    RespuestaObjeto = RespuestaDatos.getJSONObject(x);

                    Respuestapreguntas = RespuestaObjeto.getJSONObject("preguntas");

                    Respuestaespecificaciones = RespuestaObjeto.getJSONObject("especificaciones");

                    Titulo = Respuestaespecificaciones.getString( "title" );

                    RespuestaQuestions = Respuestapreguntas.getJSONArray("questions");



                    for(int i = 0; i < RespuestaQuestions.length(); i++) {

                        JSONObject elemento = RespuestaQuestions.getJSONObject(i);

                        preguntas = elemento.getString("text");

                        id_pregunta =  elemento.getString("id");

                        idcomprador = elemento.getJSONObject("from").getString( "id");

                        RespuestaComprador = Datos.getJSONArray("aUsuariosQuePregunta");

                        for(int a = 0; a < RespuestaComprador.length(); a++)
                        {
                            JSONObject elemento2 = RespuestaComprador.getJSONObject(a);

                            String keyidcomp = elemento2.getString("id_comprador") ;

                            String Valor1= String.valueOf(idcomprador);
                            String Valor2= String.valueOf(keyidcomp);

                            if(Valor1.equals(Valor2)) {

                                comprador = elemento2.getString("nickname");

                                break;
                            }

                        }

                        PreguntasModel[i][0] = preguntas;
                        PreguntasModel[i][1] = comprador;
                        PreguntasModel[i][2] = Titulo;
                        PreguntasModel[i][3] = id_pregunta;
                        PreguntasModel[i][4] = AccesToken;

                        final Preguntas_Model pregunta = new Preguntas_Model(preguntas, comprador, Titulo,id_pregunta,AccesToken);
                        Preguntas.add(pregunta);

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

    public void LoadTable(){

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        tabla_preguntas = (SortablePreguntasTable) v.findViewById(R.id.tabla_preguntas);

        final String url = "http://187.189.192.150:8010/api/ecomerce/inicio_app/?accesstoken=" + AccesToken  + "&user_id=" + UserML + "&usu_id=" + usu_id + "&esApp=1";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        JSONArray RespuestaDatos = null;
                        JSONArray RespuestaDatos2 = null;
                        JSONObject RespuestaObjeto = null;
                        JSONObject RespuestaObjeto2 = null;
                        JSONObject Respuestapreguntas = null;
                        JSONObject Respuestapreguntas2 = null;
                        JSONArray RespuestaQuestions = null;
                        JSONArray RespuestaQuestions2 = null;
                        JSONObject Respuestaespecificaciones =null;
                        JSONArray RespuestaComprador = null;

                        String Titulo;
                        String preguntas;
                        String idcomprador;
                        String id_pregunta="";
                        String comprador="";
                        int numeroregistro =0;

                        try
                        {

                            int EstatusApi = Integer.parseInt( response.getString("estatus") );

                            if (EstatusApi == 1) {

                                RespuestaDatos= response.getJSONArray("aDatos");

                                RespuestaDatos2= response.getJSONArray("aDatos");

                                for (int z = 0; z < RespuestaDatos2.length(); z++) {

                                    RespuestaObjeto2 = RespuestaDatos2.getJSONObject(z);

                                    Respuestapreguntas2 = RespuestaObjeto2.getJSONObject("preguntas");

                                    int preguntitas = Respuestapreguntas2.getInt( "total");

                                    numeroregistro += Respuestapreguntas2.getInt( "total");

                                }

                                PreguntasModel = new String[numeroregistro][5];


                                for (int x = 0; x < RespuestaDatos.length(); x++) {

                                    RespuestaObjeto = RespuestaDatos.getJSONObject(x);

                                    Respuestapreguntas = RespuestaObjeto.getJSONObject("preguntas");

                                    Respuestaespecificaciones = RespuestaObjeto.getJSONObject("especificaciones");

                                    Titulo = Respuestaespecificaciones.getString( "title" );

                                    RespuestaQuestions = Respuestapreguntas.getJSONArray("questions");



                                    for(int i = 0; i < RespuestaQuestions.length(); i++) {

                                        JSONObject elemento = RespuestaQuestions.getJSONObject(i);

                                        preguntas = elemento.getString("text");

                                        id_pregunta =  elemento.getString("id");

                                        idcomprador = elemento.getJSONObject("from").getString( "id");

                                        RespuestaComprador = response.getJSONArray("aUsuariosQuePregunta");

                                        for(int a = 0; a < RespuestaComprador.length(); a++)
                                        {
                                            JSONObject elemento2 = RespuestaComprador.getJSONObject(a);

                                            String keyidcomp = elemento2.getString("id_comprador") ;

                                            String Valor1= String.valueOf(idcomprador);
                                            String Valor2= String.valueOf(keyidcomp);

                                            if(Valor1.equals(Valor2)) {

                                                comprador = elemento2.getString("nickname");

                                                break;
                                            }

                                        }

                                        PreguntasModel[i][0] = preguntas;
                                        PreguntasModel[i][1] = comprador;
                                        PreguntasModel[i][2] = Titulo;
                                        PreguntasModel[i][3] = id_pregunta;
                                        PreguntasModel[i][4] = AccesToken;

                                        final Preguntas_Model pregunta = new Preguntas_Model(preguntas, comprador, Titulo,id_pregunta,AccesToken);
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

        getRequest.setShouldCache(false);

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

        //getRequest.
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

                Bundle bundle = new Bundle();
                bundle.putString( "Resultado", String.valueOf( RespuestaTodo ) );

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_ecommerce_Sincronizar secondFragment = new Fragment_ecommerce_Sincronizar();
                secondFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container,secondFragment);
                fragmentTransaction.commit();



               // FragmentTransaction fr = getFragmentManager().beginTransaction();
               // fr.replace(R.id.fragment_container,new Fragment_ecommerce_Sincronizar()).commit();

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
