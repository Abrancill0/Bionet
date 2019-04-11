package com.Danthop.bionet;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.SincronizarAdapter;
import com.Danthop.bionet.Tables.SortableSincronizarTable;
import com.Danthop.bionet.model.SincronizarModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;

public class Fragment_ecommerce_Sincronizar extends Fragment {
    private SortableSincronizarTable tabla_sincronizar;
    private View v;
    private String usu_id;
    private String accesstoken;
    private String user_id_mercado_libre;
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
    private ElegantNumberButton TextCantidad;
    private EditText TextGarantia;
    private Spinner SpinnerArticulo;
    private Spinner SpinnerVariante;
    private String RespuestaTodo;
    private JSONObject RespuestaTodoJSON;
    ProgressDialog progreso;
    Dialog FichaTecnica;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<SincronizarModel> Sincronizaciones;

    public Fragment_ecommerce_Sincronizar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ecommerce_sincronizar, container, false);

        FichaTecnica = new Dialog(getContext());
        Sincronizaciones = new ArrayList<>();

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", getActivity().MODE_PRIVATE);
        UserML = sharedPref.getString("UserIdML", "");
        AccesToken = sharedPref.getString("AccessToken", "");
        TokenLife = sharedPref.getString("TokenLifetime", "");
        usu_id = sharedPref.getString("usu_id", "");

        Bundle bundle = getArguments();
        int resultado = 0;

        if (bundle == null) {
            resultado = LoadTable();
        } else {
            String json = bundle.getString("Resultado");
            RespuestaTodo = bundle.getString("Resultado");
            try {
                JSONObject obj = new JSONObject(json);
                CargaDatos(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        btn_alta_articulo = (Button) v.findViewById(R.id.btn_alta_articulo);

        LoadButtons();
        LoadSpinners();
        // LoadTable();
        //Inventario_Ecommerce();

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

        tabla_sincronizar.addDataClickListener(new TableDataClickListener<SincronizarModel>() {
            @Override
            public void onDataClicked(int rowIndex, final SincronizarModel clickedData) {

                FichaTecnica.setContentView(R.layout.pop_up_ecommerce_ficha_tecnica_articulo);
                FichaTecnica.show();

                ImageView FotoProducto = FichaTecnica.findViewById(R.id.image_ficha_Producto);
                TextView NombrePublicacion = FichaTecnica.findViewById(R.id.text_ficha_nombre_publicacion);
                TextView DescricpionArticulo = FichaTecnica.findViewById(R.id.text_ficha_descripcion_articulo);
                TextView DescripcionCategoria = FichaTecnica.findViewById(R.id.text_ficha_descripcion_categoria);
                TextView Precio = FichaTecnica.findViewById(R.id.text_ficha_precio);
                TextView Envio = FichaTecnica.findViewById(R.id.text_ficha_envio);

                final EditText Cantidad = (EditText) FichaTecnica.findViewById(R.id.text_ficha_cantidad);

                Cantidad.setText(clickedData.getDisponible());

                final TextView EstadoArticulo = FichaTecnica.findViewById(R.id.text_ficha_estado_Articulo);


                Button BtnActivarPublicacion = FichaTecnica.findViewById(R.id.btnFichaActivarPublicacion);
                Button BtnCerrarPublicacion = FichaTecnica.findViewById(R.id.btnFichaCerrarPublicacion);
                Button BtnPausarPublicacion = FichaTecnica.findViewById(R.id.btnFichaPausarPublicacion);
                Button BtnEliminarPublicacion = FichaTecnica.findViewById(R.id.btnFichEliminarPublicacion);

                // Button BtnRepublicarPublicacion =FichaTecnica.findViewById(R.id.btnFichEliminarPublicacion);
                Button BtnActualizarInventarioPublicacion = FichaTecnica.findViewById(R.id.btnFichaActualizarCantidad);

                //btnFichEliminarPublicacion

                imageLoader.displayImage(clickedData.getImagen(), FotoProducto);

                NombrePublicacion.setText(clickedData.getArticulo());
                DescricpionArticulo.setText(clickedData.getDescripcionLarga());

                double Importe = Double.parseDouble(clickedData.getPrecio());
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                Precio.setText(formatter.format(Importe));

                DescripcionCategoria.setText(clickedData.getCategoria());

                Envio.setText(clickedData.getEnvio_gratis());
                EstadoArticulo.setText(clickedData.getEstadoOrden());


                BtnActivarPublicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Estatus = String.valueOf(EstadoArticulo.getText());

                        if (Estatus.equals("paused")) {
                            ActivarPublicacion(clickedData.getIDPublicacion());
                        } else {
                            Toast.makeText(getContext(), "La publicacion debe de estar en estatus pausa", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                BtnCerrarPublicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Estatus = String.valueOf(EstadoArticulo.getText());

                        if (Estatus.equals("active") || Estatus.equals("paused")) {
                            CierraPublicacion(clickedData.getIDPublicacion());
                        } else {
                            Toast.makeText(getContext(), "La publicacion debe de estar en estatus activo o pausa", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                BtnPausarPublicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Estatus = String.valueOf(EstadoArticulo.getText());

                        if (Estatus.equals("active")) {
                            PausarPublicacion(clickedData.getIDPublicacion());
                        } else {
                            Toast.makeText(getContext(), "La publicacion debe de estar en estatus activo", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                BtnEliminarPublicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Estatus = String.valueOf(EstadoArticulo.getText());

                        if (!Estatus.equals("closed")) {
                            EliminarPublicacion(clickedData.getIDPublicacion(), 0);
                        } else {
                            EliminarPublicacion(clickedData.getIDPublicacion(), 1);
                        }
                    }
                });


                BtnActualizarInventarioPublicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActualizarStock(clickedData.getIDPublicacion(), String.valueOf(Cantidad.getText()));

                    }
                });


            }
        });

        tabla_sincronizar.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));


        btn_alta_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new Fragment_ecommerce_Sincronizar_Articulos()).commit();

            }
        });

        return v;
    }


    public void CargaDatos(JSONObject Datos) {

        try {

            progreso = new ProgressDialog(getContext());
            progreso.setMessage("Cargando...");
            progreso.show();

            tabla_sincronizar = (SortableSincronizarTable) v.findViewById(R.id.tabla_sincronizar);

            JSONArray RespuestaDatos = null;
            JSONObject RespuestaDescripcion = null;
            JSONObject Respuestacategoria = null;
            JSONObject RespuestaObjeto = null;
            JSONObject Respuestaespecificaciones = null;
            JSONArray Respuestapicture = null;
            JSONObject Respuestashipping = null;

            String Titulo;
            String Disponibilidad;
            String Precio;
            String Imagen = "";
            String Envio;
            String Categoria;
            String DescripcionLarga;
            String Estatus;
            String IDPublicacion;

            try {

                RespuestaDatos = Datos.getJSONArray("aDatos");

                int numeroregistro = RespuestaDatos.length();
                SincornizarModel = new String[numeroregistro][4];

                for (int x = 0; x < RespuestaDatos.length(); x++) {

                    JSONObject elemento = RespuestaDatos.getJSONObject(x);

                    Respuestaespecificaciones = elemento.getJSONObject("especificaciones");
                    RespuestaDescripcion = elemento.getJSONObject("descripcion");
                    DescripcionLarga = RespuestaDescripcion.getString("plain_text");

                    IDPublicacion = Respuestaespecificaciones.getString("id");
                    Titulo = Respuestaespecificaciones.getString("title");
                    Disponibilidad = Respuestaespecificaciones.getString("available_quantity");
                    Precio = Respuestaespecificaciones.getString("price");
                    Estatus = Respuestaespecificaciones.getString("status");

                    Respuestapicture = Respuestaespecificaciones.getJSONArray("pictures");

                    for (int k = 0; k < Respuestapicture.length(); k++) {
                        JSONObject elemento2 = Respuestapicture.getJSONObject(k);
                        Imagen = elemento2.getString("url");
                        break;
                    }

                    Respuestacategoria = elemento.getJSONObject("categoria");
                    Categoria = Respuestacategoria.getString("name");

                    Respuestashipping = Respuestaespecificaciones.getJSONObject("shipping");
                    Envio = Respuestashipping.getString("free_shipping");

                    if (Envio == "true") {
                        Envio = "Si";
                    } else {
                        Envio = "No";
                    }

                    final SincronizarModel sincronizar = new SincronizarModel(Titulo, Disponibilidad, Envio, Precio, Imagen, Categoria, Estatus, DescripcionLarga, IDPublicacion);
                    Sincronizaciones.add(sincronizar);
                }

                final SincronizarAdapter sincronizarAdapter = new SincronizarAdapter(getContext(), Sincronizaciones, tabla_sincronizar);
                tabla_sincronizar.setDataAdapter(sincronizarAdapter);

                progreso.hide();

            } catch (JSONException e) {
                e.printStackTrace();
                progreso.hide();
            }

        } catch (Error e) {

            Toast toast1 =
                    Toast.makeText(getContext(),
                            String.valueOf(e), Toast.LENGTH_LONG);

            progreso.hide();

        }


    }

    public int LoadTable() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        try {
            tabla_sincronizar = (SortableSincronizarTable) v.findViewById(R.id.tabla_sincronizar);

            String url = getString(R.string.Url);

            final String ApiPath = url + "/api/ecomerce/inicio_app/?accesstoken=" + AccesToken + "&user_id=" + UserML + "&usu_id=" + usu_id + "&esApp=1";

            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // display response
                    JSONArray RespuestaDatos = null;
                    JSONObject Respuestacategoria = null;
                    JSONObject RespuestaDescripcion = null;
                    JSONObject RespuestaObjeto = null;
                    JSONObject Respuestaespecificaciones = null;
                    JSONArray Respuestapicture = null;
                    JSONObject Respuestashipping = null;


                    String Titulo;
                    String Disponibilidad;
                    String Precio;
                    String Imagen = "";
                    String Envio;
                    String Categoria;
                    String DescripcionLarga;
                    String Estatus;
                    String IDPublicacion;

                    try {

                        int EstatusApi = Integer.parseInt(response.getString("estatus"));

                        if (EstatusApi == 1) {

                            RespuestaTodoJSON = response;

                            RespuestaDatos = response.getJSONArray("aDatos");

                            int numeroregistro = RespuestaDatos.length();

                            SincornizarModel = new String[numeroregistro][4];

                            for (int x = 0; x < RespuestaDatos.length(); x++) {

                                JSONObject elemento = RespuestaDatos.getJSONObject(x);

                                Respuestaespecificaciones = elemento.getJSONObject("especificaciones");
                                RespuestaDescripcion = elemento.getJSONObject("descripcion");

                                DescripcionLarga = RespuestaDescripcion.getString("plain_text");


                                IDPublicacion = Respuestaespecificaciones.getString("id");
                                Titulo = Respuestaespecificaciones.getString("title");
                                Disponibilidad = Respuestaespecificaciones.getString("available_quantity");
                                Precio = Respuestaespecificaciones.getString("price");
                                Estatus = Respuestaespecificaciones.getString("status");

                                Respuestapicture = Respuestaespecificaciones.getJSONArray("pictures");

                                for (int k = 0; k < Respuestapicture.length(); k++) {
                                    JSONObject elemento2 = Respuestapicture.getJSONObject(x);

                                    Imagen = elemento2.getString("url");

                                    break;
                                }

                                Respuestacategoria = elemento.getJSONObject("categoria");
                                Categoria = Respuestacategoria.getString("name");

                                Respuestashipping = Respuestaespecificaciones.getJSONObject("shipping");
                                Envio = Respuestashipping.getString("free_shipping");

                                if (Envio == "true") {
                                    Envio = "Si";
                                } else {
                                    Envio = "No";
                                }

                                final SincronizarModel sincronizar = new SincronizarModel(Titulo, Disponibilidad, Envio, Precio, Imagen, Categoria, Estatus, DescripcionLarga, IDPublicacion);

                                Sincronizaciones.add(sincronizar);
                            }

                        }
                        final SincronizarAdapter sincronizarAdapter = new SincronizarAdapter(getContext(), Sincronizaciones, tabla_sincronizar);
                        tabla_sincronizar.setDataAdapter(sincronizarAdapter);

                        progreso.hide();

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast toast1 =
                                Toast.makeText(getContext(),
                                        e.toString(), Toast.LENGTH_LONG);

                        progreso.hide();

                    }


                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(error), Toast.LENGTH_LONG);

                            progreso.hide();

                            try {
                                JSONObject jsonObj = new JSONObject("{\"Estatus\":\"0\"}");

                                RespuestaTodoJSON = jsonObj;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
            );

            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);

            return 1;

        } catch (Error e) {

            Toast toast1 =
                    Toast.makeText(getContext(),
                            String.valueOf(e), Toast.LENGTH_LONG);

            progreso.hide();

            return 0;

        }


    }

    public void LoadButtons() {
        btn_pestania_ordenes = v.findViewById(R.id.btn_pestania_ordenes);
        btn_pestania_ordenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                if (RespuestaTodoJSON != null) {
                    bundle.putString("Resultado", String.valueOf(RespuestaTodoJSON));

                    RespuestaTodoJSON = null;
                } else {
                    bundle.putString("Resultado", String.valueOf(RespuestaTodo));
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_ecomerce secondFragment = new Fragment_ecomerce();
                secondFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, secondFragment);
                fragmentTransaction.commit();

                // FragmentTransaction fr = getFragmentManager().beginTransaction();
                // fr.replace( R.id.fragment_container, new Fragment_ecomerce()).commit();


            }
        });
        btn_pestania_preguntas = v.findViewById(R.id.btn_pestania_preguntas);
        btn_pestania_preguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                if (RespuestaTodoJSON != null) {
                    bundle.putString("Resultado", String.valueOf(RespuestaTodoJSON));

                    RespuestaTodoJSON = null;
                } else {
                    bundle.putString("Resultado", String.valueOf(RespuestaTodo));
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                Fragment_ecommerce_preguntas secondFragment = new Fragment_ecommerce_preguntas();
                secondFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, secondFragment);
                fragmentTransaction.commit();
            }
        });
    }

    public void LoadSpinners() {
        Categoria = new ArrayList<>();
        Articulo = new ArrayList<>();
        Variante = new ArrayList<>();

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

        SpinnerCategoria.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Categoria));
        SpinnerArticulo.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Articulo));
        SpinnerVariante.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Variante));

    }
//--------------------------------------------------------------------------------------------------------------
    public void CierraPublicacion(String Item) {
        //https://api.mercadolibre.com/items/" + sIdItem + "?access_token={{$ACCESS_TOKEN}}
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //https://api.mercadolibre.com/items/ITEM_ID?access_token=YOUR_ACCESS_TOKEN
        JSONObject jsonBodyObj = new JSONObject();
        String url = "https://api.mercadolibre.com/items/" + Item + "?access_token=" + AccesToken;
        try {
            jsonBodyObj.put("status", "closed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getContext(), "Se cerro correctamente la publicacion", Toast.LENGTH_LONG).show();
                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                progreso.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

        };

        requestQueue.add(jsonObjectRequest);


    }

    public void EliminarPublicacion(String Item, int Valida) {

        if (Valida == 0) {
            CierraPublicacion(Item);
        }

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //https://api.mercadolibre.com/items/ITEM_ID?access_token=YOUR_ACCESS_TOKEN

        JSONObject jsonBodyObj = new JSONObject();
        String url = "https://api.mercadolibre.com/items/" + Item + "?access_token=" + AccesToken;
        try {
            jsonBodyObj.put("deleted", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getContext(), "Se elimino correctamente la publicacion", Toast.LENGTH_LONG).show();

                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                progreso.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

        };

        requestQueue.add(jsonObjectRequest);


    }

    public void ActualizarStock(String Item, String Cantidad) {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Actualizando...");
        progreso.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //https://api.mercadolibre.com/items/ITEM_ID?access_token=YOUR_ACCESS_TOKEN

        JSONObject jsonBodyObj = new JSONObject();
        String url = "https://api.mercadolibre.com/items/" + Item + "?access_token=" + AccesToken;
        try {
            jsonBodyObj.put("available_quantity", Cantidad);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getContext(), "Se actualizo correctamente el stock", Toast.LENGTH_LONG).show();


                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                progreso.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

        };

        requestQueue.add(jsonObjectRequest);


    }

    public void ActivarPublicacion(String Item) {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject jsonBodyObj = new JSONObject();
        String url = "https://api.mercadolibre.com/items/" + Item + "?access_token=" + AccesToken;
        try {
            jsonBodyObj.put("status", "active");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getContext(), "Se activo correctamente la publicacion", Toast.LENGTH_LONG).show();

                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                progreso.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

        };

        requestQueue.add(jsonObjectRequest);

    }

    public void PausarPublicacion(String Item) {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Procesando...");
        progreso.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject jsonBodyObj = new JSONObject();
        String url = "https://api.mercadolibre.com/items/" + Item + "?access_token=" + AccesToken;
        try {
            jsonBodyObj.put("status", "paused");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(getContext(), "Se pauso correctamente la publicacion", Toast.LENGTH_LONG).show();

                progreso.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                progreso.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

        };

        requestQueue.add(jsonObjectRequest);
    }

    /*private void Inventario_Ecommerce() {
        try {
            RespuestaTodoJSON.put("usu_id", usu_id);
            RespuestaTodoJSON.put("esApp", 1);
            RespuestaTodoJSON.put("accesstoken", accesstoken);
            RespuestaTodoJSON.put("user_id_mercado_libre", user_id_mercado_libre);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = getString(R.string.Url);
        String ApiPath = url + "/api/ecommerce/inicio_app";

        JsonObjectRequest postRequets = new JsonObjectRequest(Request.Method.GET, ApiPath, RespuestaTodoJSON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");




                }catch (JSONException e) {
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
}*/

}