package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.DetalleOrdenEspecialAdapter;
import com.Danthop.bionet.Adapters.LealtadArticuloAdapter;
import com.Danthop.bionet.Tables.SortableLealtadArticulosTable;
import com.Danthop.bionet.Tables.SortableOrdenEspecialDetalleTable;
import com.Danthop.bionet.model.LealtadArticuloModel;
import com.Danthop.bionet.model.OrdenEspecialModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLealtadArticulo extends Fragment {

    private SortableLealtadArticulosTable tabla_articulos;
    private FragmentTransaction fr;
    private Button Lealtad;
    private Button Programas;
    private Button Inscribir;
    private Spinner SpinnerSucursal;
    private Dialog dialog;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ProgressDialog progressDialog;
    private String usu_id;

    private SearchView Buscar;
    private LealtadArticuloAdapter articuloAdapter;


    public List<LealtadArticuloModel> articulos;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;


    public FragmentLealtadArticulo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lealtad_articulos,container, false);
        fr = getFragmentManager().beginTransaction();
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        articulos = new ArrayList<>();
        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();

        SpinnerSucursal=(Spinner)v.findViewById(R.id.Sucursal_lealtad);
        tabla_articulos = v.findViewById(R.id.tabla_articulos);
        tabla_articulos.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));
        Lealtad=v.findViewById(R.id.lealtad);
        Programas=v.findViewById(R.id.programas);
        Inscribir=v.findViewById(R.id.inscribir);
        Buscar = v.findViewById(R.id.buscaArticulosLealtad);

        LoadPestanias();
        LoadSpinnerSucursal();



        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                articulos.clear();
                Muestra_articulos();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        TableDataClickListener<LealtadArticuloModel> tablaListener = new TableDataClickListener<LealtadArticuloModel>() {
            @Override
            public void onDataClicked(int rowIndex, final LealtadArticuloModel clickedData) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pop_up_lealtad_ficha_articulo);
                dialog.show();

                TextView Nombre = dialog.findViewById(R.id.articulo_nombre);
                TextView Tipo = dialog.findViewById(R.id.tipo_producto);
                TextView Descripción = dialog.findViewById(R.id.descripcion_producto);
                TextView Categoria = dialog.findViewById(R.id.categoria_producto);
                TextView Variantes = dialog.findViewById(R.id.variante_producto);
                TextView DisponibleVenta = dialog.findViewById(R.id.disponible_venta);
                TextView DisponibleCompra = dialog.findViewById(R.id.disponible_compra);
                TextView AplicaApartados = dialog.findViewById(R.id.aplica_apartados);
                TextView AplicaDevoluciones = dialog.findViewById(R.id.aplica_devoluciones);

                Nombre.setText(clickedData.getArticuloNombre());
                Tipo.setText(clickedData.getArticuloTipo());
                Descripción.setText(clickedData.getArticuloDescripcion());
                Categoria.setText(clickedData.getArticuloCategoria());
                for(int k=0;k<clickedData.getArticuloVariantes().size();k++)
                {
                    Variantes.setText(Variantes.getText()+""+clickedData.getArticuloVariantes().get(k));
                }

                DisponibleVenta.setText(clickedData.getArticuloDispVenta());
                if(clickedData.getArticuloDispVenta().equals("true"))
                {
                    DisponibleVenta.setText("Si");
                }else
                {
                    DisponibleVenta.setText("No");
                }

                DisponibleCompra.setText(clickedData.getArticuloDispCompra());
                if(clickedData.getArticuloDispCompra().equals("true"))
                {
                    DisponibleCompra.setText("Si");
                }else
                {
                    DisponibleCompra.setText("No");
                }

                AplicaApartados.setText(clickedData.getArticuloDispApartados());
                if(clickedData.getArticuloDispApartados().equals("true"))
                {
                    AplicaApartados.setText("Si");
                }else
                {
                    AplicaApartados.setText("No");
                }

                AplicaDevoluciones.setText(clickedData.getArticuloDispCambioDevolucion());
                if(clickedData.getArticuloDispCambioDevolucion().equals("true"))
                {
                    AplicaDevoluciones.setText("Si");
                }else
                {
                    AplicaDevoluciones.setText("No");
                }

                ImageView imagenArticulo = dialog.findViewById(R.id.img);
                String ruta =clickedData.getArticuloImagenes().get(0);
                imageLoader.displayImage(ruta, imagenArticulo);


            }
        };

        tabla_articulos.addDataClickListener(tablaListener);

        Buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                articuloAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return v;

    }

    private void LoadPestanias(){
        Inscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadInscribir()).commit();
                onDetach();

            }
        });
        Programas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtadConfiguraciones()).commit();
                onDetach();

            }
        });
        Lealtad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new FragmentLealtad()).commit();
                onDetach();

            }
        });
    }

    private void Muestra_articulos(){
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("art_programa_lealtad", "true");
            request.put("suc_id", SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/programa-de-lealtad/select-articulos";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Respuesta = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        progressDialog.dismiss();
                        Respuesta = response.getJSONArray("resultado");

                        for(int x = 0; x < Respuesta.length(); x++){
                            JSONObject elemento = Respuesta.getJSONObject(x);
                            String Articulo_nombre = elemento.getString("art_nombre");
                            String Articulo_Tipo = elemento.getString("art_tipo");
                            String Articulo_descripcion = elemento.getString("art_descripcion");
                            String Articulo_categoria = elemento.getString("cat_nombre");

                            ArrayList<String> ListaVariantes = new ArrayList<>();
                            JSONArray Variantes = elemento.getJSONArray("variantes");
                            for(int y= 0; y <Variantes.length(); y++)
                            {
                                JSONObject elementoVariable = Variantes.getJSONObject(y);
                                String varianteNombre = elementoVariable.getString("ava_nombre");
                                ListaVariantes.add(varianteNombre);
                            }

                            ArrayList<String> ListaImagenes = new ArrayList<>();
                            JSONArray RespuestaImagenes = elemento.getJSONArray("art_imagenes");
                            for (int z = 0; z < RespuestaImagenes.length(); z++) {
                                String RutaImagen = RespuestaImagenes.getString(z);
                                ListaImagenes.add(RutaImagen);
                            }

                            String Articulo_DispVenta = elemento.getString("art_disponible_venta");
                            String Articulo_DispCompra = elemento.getString("art_disponible_compra");
                            String Articulo_DispOrdenes = elemento.getString("art_aplica_ordenes_especiales");
                            String Articulo_DispApartados = elemento.getString("art_aplica_apartados");
                            String Articulo_DispCambioDev = elemento.getString("art_aplica_cambio_devolucion");

                           final LealtadArticuloModel articulo = new LealtadArticuloModel(
                                   Articulo_nombre,
                                   Articulo_Tipo,
                                   Articulo_descripcion,
                                   Articulo_categoria,
                                   ListaVariantes,
                                   ListaImagenes,
                                   Articulo_DispVenta,
                                   Articulo_DispCompra,
                                   Articulo_DispOrdenes,
                                   Articulo_DispApartados,
                                   Articulo_DispCambioDev
                                   );

                           articulos.add(articulo);
                        }
                       articuloAdapter = new LealtadArticuloAdapter(getContext(), articulos, tabla_articulos);
                       tabla_articulos.setDataAdapter(articuloAdapter);


                    }
                    else
                    {
                        Toast toast1 = Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();


                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();


                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();


                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);



    }

    private void LoadSpinnerSucursal(){

        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/configuracion/sucursales/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONArray RespuestaNodoSucursales= null;
                JSONObject RespuestaNodoID = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");
                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoSucursales = Respuesta.getJSONArray("aSucursales");

                        for(int x = 0; x < RespuestaNodoSucursales.length(); x++){
                            JSONObject jsonObject1=RespuestaNodoSucursales.getJSONObject(x);
                            String sucursal=jsonObject1.getString("suc_nombre");
                            SucursalName.add(sucursal);
                            RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                            String id=RespuestaNodoID.getString("uuid");
                            SucursalID.add(id);
                        }
                        SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,SucursalName));

                    }
                    else
                    {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();


                    }

                } catch (JSONException e) {

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();


                }

            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();


                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);



    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
