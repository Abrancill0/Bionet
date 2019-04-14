package com.Danthop.bionet.Adapters;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.Danthop.bionet.Fragment_ecommerce_Sincronizar_Articulos;
import com.Danthop.bionet.Fragment_ecommerce_Sincronizar_Nuevo_Prod;
import com.Danthop.bionet.R;
import com.Danthop.bionet.model.CategoriaModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class CategoriaAdapter extends ArrayAdapter<CategoriaModel> {

    private Context mContext;
    private int mResource;
    private TextView nombre;
    public String name;
    public String id;
    private TextView NombreDialog;
    private String AccesToken;
    private String UserML;
    private Dialog dialog1;
    private Dialog pop_up_tipo_categoria;
    private Dialog pop_up_tipo_publicacion;
    public NameCategoriaSelcted mOnInputSelected;
    private ListView ListaCategoria;
    private ListView ListaPublicaciones;
    private Bundle bundle1;
    private String id_viejo1;
    private String Nombre;
    private String Descripcion;
    private String Precio;
    private String usu_id;
    private String Imagen1;
    private String Imagen2;
    FragmentTransaction fr;
    private ImageView atras;
    private String idcategoria;

    public interface NameCategoriaSelcted {
        void sendInput(String input);
    }

    public CategoriaAdapter(Context context, int resource, ArrayList<CategoriaModel> objects, ListView ListCategoria, Bundle bundle, FragmentTransaction fg, ImageView back, String id_viejo) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        pop_up_tipo_categoria = new Dialog(getContext());
        pop_up_tipo_categoria.setContentView(R.layout.pop_up_tipo_categoria);
        ListaCategoria = ListCategoria;
        bundle1 = bundle;
        fr = fg;
        atras = back;
        id_viejo1 = id_viejo;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        name = getItem(position).getName();
        id = getItem(position).getId();

        Nombre = bundle1.getString("nombre");
        Descripcion = bundle1.getString("descripcion");
        Precio = bundle1.getString("precio");
        usu_id = bundle1.getString("usu_id");
        Imagen1 = bundle1.getString("image1");
        Imagen2 = bundle1.getString("image2");

        CategoriaModel categoria = new CategoriaModel(name, id);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        nombre = convertView.findViewById(R.id.TextCategoria);
        nombre.setText(name);

        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = getItem(position).getName();
                id = getItem(position).getId();

                try {
                    final String url = "http://187.189.192.150:8010/api/ecommerce/obtenerSubcategoriasMercadoLibre?sIdCategoria=" + id + "&usu_id=" + usu_id + "&esApp=1";
                    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray RespuestaTiposPublicacion = null;
                            JSONArray RespuestaCategoria = null;

                            try {
                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    RespuestaCategoria = response.getJSONArray("resultado");

                                    if (RespuestaCategoria.length() != 0) {
                                        ArrayList arrayList = new ArrayList<>();
                                        for (int x = 0; x < RespuestaCategoria.length(); x++) {
                                            JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                            idcategoria = jsonObject1.getString("id");
                                            String categoria = jsonObject1.getString("name");

                                            CategoriaModel cat = new CategoriaModel(idcategoria, categoria);
                                            arrayList.add(cat);//add the hashmap into arrayList
                                        }

                                        CategoriaAdapter adapter = new CategoriaAdapter(getContext(), R.layout.caja_categoria, arrayList, ListaCategoria, bundle1, fr, atras, id);
                                        ListaCategoria.setAdapter(adapter);//sets the adapter for listView
                                    } else {
                                        bundle1.putString("nombre", Nombre);
                                        bundle1.putString("descripcion", Descripcion);
                                        bundle1.putString("precio", Precio);
                                        bundle1.putString("categoria", name);
                                        bundle1.putString("id_categoria", id);
                                        bundle1.putString("image1", Imagen1);
                                        bundle1.putString("image2", Imagen2);

                                        Fragment_ecommerce_Sincronizar_Nuevo_Prod secondFragment = new Fragment_ecommerce_Sincronizar_Nuevo_Prod();
                                        secondFragment.setArguments(bundle1);
                                        fr.replace(R.id.fragment_container, secondFragment).commit();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error.Response", String.valueOf(error));
                                }
                            }
                    );

                    VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
                } catch (Error e) {
                    e.printStackTrace();
                }
            }
        });

//---------------------------------------------------------------------------------------------------
   /* atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences( "DatosPersistentes", getContext().MODE_PRIVATE );
                UserML = sharedPref.getString( "UserIdML", "" );
                AccesToken = sharedPref.getString( "AccessToken", "" );
                if(id_viejo1.equals("")) {
                    fr.replace(R.id.fragment_container, new Fragment_ecommerce_Sincronizar_Articulos()).commit();
                } else {
                    final String url = "http://187.189.192.150:8010/api/ecommerce/create_app?accesstoken=" + AccesToken  + "&user_id_mercado_libre=" + UserML + "&usu_id=" + usu_id + "&esApp=1";
                    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONArray RespuestaTiposPublicacion = null;
                                    JSONArray RespuestaCategoria = null;

                                    try
                                    {
                                        int EstatusApi = Integer.parseInt( response.getString("estatus") );

                                        if (EstatusApi == 1) {
                                            RespuestaCategoria = response.getJSONArray("aCategorias");
                                            ArrayList arrayList = new ArrayList<>();

                                            for(int x = 0; x < RespuestaCategoria.length(); x++){
                                                JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                                String idcategoria = jsonObject1.getString("id");
                                                String categoria = jsonObject1.getString("name");

                                                CategoriaModel cat = new CategoriaModel(idcategoria, categoria );
                                                arrayList.add(cat);//add the hashmap into arrayList
                                            }
                                            CategoriaAdapter adapter = new CategoriaAdapter(getContext(), R.layout.caja_categoria,arrayList,ListaCategoria,bundle1,fr,atras,"");
                                            ListaCategoria.setAdapter(adapter);//sets the adapter for listView
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
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
            }
        });*/
        return convertView;
        }
//---------------------------------------------------------------------------------------------------
}
