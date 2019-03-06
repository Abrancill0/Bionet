package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.Danthop.bionet.Fragment_ecommerce_Sincronizar;
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
    private ListView listacategoria1;
    private String UserML;
    private Dialog dialog1;
    private Dialog pop_up_tipo_categoria;
    public NameCategoriaSelcted mOnInputSelected;

    public interface NameCategoriaSelcted{
        void sendInput(String input);
    }

    public CategoriaAdapter(Context context, int resource, ArrayList<CategoriaModel> objects,Dialog dialog) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        pop_up_tipo_categoria = new Dialog(getContext());
        pop_up_tipo_categoria.setContentView(R.layout.pop_up_tipo_categoria);
        dialog1=dialog;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        name = getItem(position).getName();
        id = getItem(position).getId();

        CategoriaModel categoria = new CategoriaModel(name,id);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        nombre = convertView.findViewById(R.id.TextCategoria);

        nombre.setText(name);

        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                pop_up_tipo_categoria.show();
                listacategoria1 = (ListView) pop_up_tipo_categoria.findViewById(R.id.categoria_siguiente);
                Button regresar = pop_up_tipo_categoria.findViewById(R.id.Regresar);
                regresar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop_up_tipo_categoria.dismiss();
                    }
                });

                name = getItem(position).getName();
                id = getItem(position).getId();


                        try {
                            final String url = "http://187.189.192.150:8010/api/ecomerce/obtenerSubcategoriasMercadoLibre?sIdCategoria=" + id;

                            // prepare the Request
                            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // display response
                                            JSONArray RespuestaTiposPublicacion = null;
                                            JSONArray RespuestaCategoria = null;

                                            try {

                                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                                if (EstatusApi == 1) {

                                                    RespuestaCategoria = response.getJSONArray("resultado");

                                                    if(RespuestaCategoria.length()!=0)

                                                    {

                                                        ArrayList arrayList = new ArrayList<>();


                                                        for (int x = 0; x < RespuestaCategoria.length(); x++) {
                                                            JSONObject jsonObject1 = RespuestaCategoria.getJSONObject(x);
                                                            String idcategoria = jsonObject1.getString("id");
                                                            String categoria = jsonObject1.getString("name");

                                                            CategoriaModel cat = new CategoriaModel(idcategoria, categoria);



                                                            arrayList.add(cat);//add the hashmap into arrayList


                                                        }

                                                        CategoriaAdapter adapter = new CategoriaAdapter(getContext(), R.layout.caja_categoria, arrayList, pop_up_tipo_categoria);



                                                        listacategoria1.setAdapter(adapter);//sets the adapter for listView


                                                    }
                                                    else
                                                    {
                                                        Fragment_ecommerce_Sincronizar_Nuevo_Prod prueba = new Fragment_ecommerce_Sincronizar_Nuevo_Prod();

                                                        prueba.LlenaTexto( name );

                                                        System.out.println(id);
                                                        pop_up_tipo_categoria.dismiss();
                                                        //mOnInputSelected.sendInput(name);
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


            }});

        return convertView;
        }


}

