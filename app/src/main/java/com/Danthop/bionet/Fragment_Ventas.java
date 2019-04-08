package com.Danthop.bionet;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ArticuloAdapter;
import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.SeleccionarArticuloVentaAdapter;
import com.Danthop.bionet.Adapters.VentaArticuloAdapter;
import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableSeleccionarArticuloTable;
import com.Danthop.bionet.Tables.SortableVentaArticulos;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Ventas extends Fragment {

    private Button btn_agregar_cliente;
    private Button btn_agregar_vendedor;
    private Button btn_agregar_articulo;
    private Button btn_feenicia;
    private Button btn_reporte;
    private Button crear_cliente;
    private Button aceptar_agregar_vendedor;
    private Button Corte_Caja;
    private Dialog dialog;
    private TextView descuento;
    private TextView total;
    private Spinner vendedores;
    private ArrayList<String> VendedorName;

    private String usu_id;
    private String nombre;
    private String UUID;
    private String telefono;
    private String correo_electronico;
    private String calle;
    private String estado;
    private String colonia;
    private String num_int;
    private String num_ext;
    private String cp;
    private String ciudad;
    private String municipio;
    private String rfc;
    private String razon_social;
    private String cp_fiscal;
    private String estado_fiscal;
    private String municipio_fiscal;
    private String colonia_fiscal;
    private String calle_fiscal;
    private String num_ext_fiscal;
    private String num_int_fiscal;
    private String direccion_fiscal;
    private String email_fiscal;
    private String correo_igual;
    private String direccion_igual;
    private SortableClientesTable tabla_clientes;
    private SortableSeleccionarArticuloTable tabla_selecciona_articulo;
    private SortableVentaArticulos tabla_venta_articulos;



    private List<ClienteModel> clientes;
    private List<ArticuloModel> Articulos;
    private List<ArticuloModel> ArticulosVenta;
    private FragmentTransaction fr;

    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;

    private CarouselView carouselView;

    private String TicketIDVenta;
    private String TicketImporteDescuento;
    private String TicketImporteTotal;

    private EditText BuscarPorSKU;



    public Fragment_Ventas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas,container, false);




        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id","");
        fr = getFragmentManager().beginTransaction();
        dialog=new Dialog(getContext());

        ArticulosVenta = new ArrayList<>();
        carouselView = (CarouselView) v.findViewById(R.id.carouselView);
        btn_agregar_cliente = v.findViewById(R.id.btn_agregar_cliente);
        btn_agregar_vendedor = v.findViewById(R.id.btn_agregar_vendedor);
        btn_agregar_articulo = v.findViewById(R.id.btn_agregar_articulo);
        btn_feenicia = v.findViewById(R.id.btn_feenicia);
        btn_reporte = v.findViewById(R.id.btn_reporte);
        Corte_Caja = v.findViewById(R.id.CorteCaja);
        BuscarPorSKU = v.findViewById(R.id.buscarXSKU);
        descuento = v.findViewById(R.id.descuento_text);
        total = v.findViewById(R.id.total_text);
        VendedorName=new ArrayList<>();

        SucursalName=new ArrayList<>();
        SucursalID = new ArrayList<>();
        SpinnerSucursal=(Spinner)v.findViewById(R.id.sucursal);

        tabla_venta_articulos=v.findViewById(R.id.tabla_venta_articulos);
        tabla_venta_articulos.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));




        LoadSucursales();
        LoadImages();
        LoadButtons();


        return v;
    }

    public void LoadSucursales()
    {
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

    public void LoadImages()
    {
        final int[] sampleImages = {R.drawable.milk, R.drawable.bread, R.drawable.strawberrie, R.drawable.lake};
        carouselView.setPageCount(sampleImages.length);
        carouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };
        carouselView.setImageListener(imageListener);
    }

    public void LoadButtons()
    {


        btn_agregar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.pop_up_ventas_seleccionar_cliente);
                dialog.show();
                clientes = new ArrayList<>();
                crear_cliente = dialog.findViewById(R.id.btn_crear_cliente);
                tabla_clientes = dialog.findViewById(R.id.tabla_clientes);
                tabla_clientes.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                Muestra_clientes();
                TableDataClickListener<ClienteModel> tablaListener = new TableDataClickListener<ClienteModel>() {
                    @Override
                    public void onDataClicked(int rowIndex, final ClienteModel clickedData) {
                        dialog.dismiss();
                        nombre= clickedData.getCliente_Nombre();
                        btn_agregar_cliente.setText(nombre);

                    }
                };
                tabla_clientes.addDataClickListener(tablaListener);


                crear_cliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        fr.replace(R.id.fragment_container,new Fragment_ventas_crear_cliente()).commit();
                    }
                });
            }
        });

        btn_agregar_vendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.pop_up_ventas_agregar_vendedor);
                dialog.show();
                VendedorName.add("Roberto Carrera");
                VendedorName.add("Gerardo Rodríguez");
                VendedorName.add("Ricardo Segura");
                vendedores = dialog.findViewById(R.id.Combo_vendedores);
                vendedores.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,VendedorName));
                aceptar_agregar_vendedor = (Button) dialog.findViewById(R.id.aceptar_agregar_vendedor);
                aceptar_agregar_vendedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        btn_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new Fragment_ventas_reporte_ventas()).commit();
            }
        });

        Corte_Caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container,new Fragment_ventas_corte_caja()).commit();
            }
        });

        btn_agregar_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.pop_up_ventas_seleccionar_articulo);
                dialog.show();
                tabla_selecciona_articulo = dialog.findViewById(R.id.tabla_seleccionar_articulos);
                tabla_selecciona_articulo.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                CargaArticulos();
                TableDataClickListener<ArticuloModel> tablaListener = new TableDataClickListener<ArticuloModel>() {
                    @Override
                    public void onDataClicked(int rowIndex, final ArticuloModel clickedData) {
                        dialog.dismiss();
                        String SKU= clickedData.getArticulo_sku();
                        Aniadir_a_venta(SKU);

                    }
                };
                tabla_selecciona_articulo.addDataClickListener(tablaListener);
            }
        });

        btn_feenicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getActivity(), Feenicia_Transaction_Bluetooth.class);
                getActivity().startActivity(myIntent);


                //Intent intent = new Intent(this, Feenicia_Transaction_Bluetooth.class);
               // startActivity(intent);
               //


              //  Button Btn_Buscar_Dispositivo = dialog.findViewById(R.id.Btn_Buscar_Dispositivo);
              //  Button Btn_Conectar_Dispositivo = dialog.findViewById(R.id.Btn_Conectar_Dispositivo);


               // Btn_Buscar_Dispositivo

            }
        });

        BuscarPorSKU.setOnEditorActionListener(
            new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {
                            Aniadir_a_venta(BuscarPorSKU.getText().toString());
                            return true;
                        }
                    }
                    return false;
                }
            });


    }

    private void Aniadir_a_venta(String CBoSKULL)
    {
        ArticulosVenta.clear();
        JSONObject request = new JSONObject();
        try
        {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id_sucursal",SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
            request.put("articulo",CBoSKULL);
            request.put("cantidad",1);
            request.put("tic_id",TicketIDVenta);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/store";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoTicket= null;
                JSONObject TicketID=null;
                JSONArray NodoTicketArticulos=null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoTicket = Respuesta.getJSONObject("aTicket");
                        TicketID = RespuestaNodoTicket.getJSONObject("tic_id");
                        TicketIDVenta = TicketID.getString("uuid");
                        JSONObject TicketDesc = RespuestaNodoTicket.getJSONObject("tic_importe_descuentos");
                        TicketImporteDescuento = TicketDesc.getString("value");
                        descuento.setText(TicketImporteDescuento);
                        JSONObject TicketTotal = RespuestaNodoTicket.getJSONObject("tic_importe_total");
                        TicketImporteTotal = TicketTotal.getString("value");
                        total.setText(TicketImporteTotal);


                        NodoTicketArticulos = Respuesta.getJSONArray("aDetalleTicket");
                        for(int x = 0; x < NodoTicketArticulos.length(); x++){
                            JSONObject elemento = NodoTicketArticulos.getJSONObject(x);

                            String NombreArticulo =  elemento.getString("tar_nombre_articulo");
                            String SKUArticulo = elemento.getString("art_sku");
                            JSONObject cantidadNodo = elemento.getJSONObject("tar_cantidad");
                            String cantidad = cantidadNodo.getString("value");




                            final ArticuloModel articulo = new ArticuloModel("",
                                    NombreArticulo,
                                    "",
                                    "",
                                    "",
                                    "",
                                    SKUArticulo,
                                    "",
                                    cantidad
                            );
                            ArticulosVenta.add(articulo);
                        }
                        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos);
                        tabla_venta_articulos.setDataAdapter(articuloAdapter);
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


    private void Muestra_clientes()
    {
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

        String ApiPath = url + "/api/clientes/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoDireccion= null;
                JSONObject ElementoUsuario=null;
                JSONArray RespuestaNodoClientes= null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1)
                    {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoClientes = Respuesta.getJSONArray("aClientes");

                        for(int x = 0; x < RespuestaNodoClientes.length(); x++){
                            JSONObject elemento = RespuestaNodoClientes.getJSONObject(x);

                            ElementoUsuario =  elemento.getJSONObject("cli_id");

                            UUID = ElementoUsuario.getString( "uuid");
                            nombre = elemento.getString("cli_nombre");
                            correo_electronico = elemento.getString("cli_correo_electronico");

                            telefono = elemento.getString("cli_telefono");
                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion");
                            calle = RespuestaNodoDireccion.getString("cli_calle");

                            estado = RespuestaNodoDireccion.getString( "cli_estado");
                            colonia = RespuestaNodoDireccion.getString( "cli_colonia");
                            num_int = RespuestaNodoDireccion.getString( "cli_numero_interior");
                            num_ext = RespuestaNodoDireccion.getString( "cli_numero_exterior");
                            cp = RespuestaNodoDireccion.getString( "cli_codigo_postal");
                            ciudad = RespuestaNodoDireccion.getString( "cli_ciudad");
                            municipio = RespuestaNodoDireccion.getString( "cli_ciudad");

                            rfc = elemento.getString( "cli_rfc");
                            razon_social = elemento.getString( "cli_razon_social");

                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion_fiscal");
                            cp_fiscal = RespuestaNodoDireccion.getString("cli_codigo_postal");
                            estado_fiscal = RespuestaNodoDireccion.getString("cli_estado");
                            municipio_fiscal = RespuestaNodoDireccion.getString("cli_ciudad");
                            colonia_fiscal = RespuestaNodoDireccion.getString("cli_colonia");
                            calle_fiscal = RespuestaNodoDireccion.getString("cli_calle");
                            num_ext_fiscal = RespuestaNodoDireccion.getString("cli_numero_exterior");
                            num_int_fiscal = RespuestaNodoDireccion.getString("cli_numero_interior");


                            direccion_igual = elemento.getString("cli_direcciones_iguales");
                            if(direccion_igual.equals("false"))
                            {
                                direccion_fiscal = calle_fiscal + " " + num_ext_fiscal + " " + num_int_fiscal + " " +colonia_fiscal + " " + cp_fiscal + " " + estado_fiscal + " " + municipio_fiscal;
                            }
                            else if (direccion_igual.equals("true"))
                            {
                                direccion_fiscal = calle + " " + num_ext + " " + num_int + " " +colonia + " " + cp + " " + estado + " " + municipio;

                            }

                            correo_igual = elemento.getString("cli_correos_iguales");
                            if(correo_igual.equals("false"))
                            {
                                email_fiscal = elemento.getString("cli_correo_electronico_facturacion");
                            }
                            else if (correo_igual.equals("true"))
                            {
                                email_fiscal = correo_electronico;
                            }


                            final ClienteModel cliente = new ClienteModel(UUID,
                                    nombre,
                                    correo_electronico,
                                    telefono,
                                    usu_id,
                                    estado,
                                    colonia,
                                    calle,
                                    num_int,
                                    num_ext,
                                    cp,
                                    ciudad,
                                    municipio,
                                    rfc,
                                    razon_social,
                                    direccion_fiscal,
                                    email_fiscal,
                                    cp_fiscal,
                                    estado_fiscal,
                                    municipio_fiscal,
                                    colonia_fiscal,
                                    calle_fiscal,
                                    num_ext_fiscal,
                                    num_int_fiscal,
                                    correo_igual,
                                    direccion_igual
                            );
                            clientes.add(cliente);
                        }
                        final ClienteAdapter clienteAdapter = new ClienteAdapter(getContext(), clientes, tabla_clientes,fr);
                        tabla_clientes.setDataAdapter(clienteAdapter);
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

    private void CargaArticulos(){

        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/articulos/index_app/?usu_id=" + usu_id + "&esApp=1";

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray RespuestaResultado = null;
                            JSONArray RespuestaImagenes = null;
                            JSONObject RespuestaUUID = null;
                            JSONObject RespuestaPrecio=null;
                            JSONObject RespuestaPrecioModificador=null;
                            JSONArray RespuestaModificadores = null;

                            String RutaImagen1="";
                            String RutaImagen2="";

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                Articulos = new ArrayList<>();

                                if (EstatusApi == 1) {

                                    RespuestaResultado = response.getJSONArray("resultado");

                                    // sacar la ruta de las imagenes y mandarlo en el modelo

                                    for (int x = 0; x < RespuestaResultado.length(); x++) {
                                        JSONObject elemento = RespuestaResultado.getJSONObject(x);

                                        RespuestaUUID = elemento.getJSONObject("art_id");
                                        String UUID = RespuestaUUID.getString( "uuid");

                                        RespuestaPrecio =  elemento.getJSONObject( "ava_precio");
                                        String Precio = RespuestaPrecio.getString( "value");
                                        //VERIFICAR MODIFICADORES
                                        String NombreCompleto="";
                                        String NombreArticulo = elemento.getString("art_nombre");
                                        String NombreVariante = elemento.getString( "ava_nombre");
                                        String Descripcion = elemento.getString( "art_descripcion");
                                        String Categoria = elemento.getString("cat_nombre");
                                        String SKU = elemento.getString("ava_sku");

                                        String NombreModificador="";
                                        String Modificadores = elemento.getString( "ava_tiene_modificadores");

                                        RespuestaImagenes = elemento.getJSONArray( "imagenes");

                                        for (int z = 0; z < RespuestaImagenes.length(); z++) {

                                            JSONObject elemento3 = RespuestaImagenes.getJSONObject(z);

                                            if(RutaImagen1.equals("")) {
                                                RutaImagen1 = elemento3.getString( "aim_url");
                                            }
                                            else
                                            {
                                                RutaImagen2 = elemento3.getString( "aim_url");
                                            }

                                        }

                                        if (Modificadores == "true"){
                                            RespuestaModificadores = elemento.getJSONArray( "modificadores");

                                            for (int i = 0; i < RespuestaModificadores.length(); i++) {

                                                JSONObject elemento2 = RespuestaModificadores.getJSONObject(i);
                                                NombreModificador = elemento2.getString( "mod_nombre");
                                                RespuestaPrecioModificador = elemento2.getJSONObject( "amo_precio");

                                                Precio = RespuestaPrecioModificador.getString( "value");

                                                NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                                final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion, Precio,RutaImagen1,
                                                        RutaImagen2,SKU,Categoria,"");
                                                Articulos.add(Articulo);
                                            }

                                        }
                                        else
                                        {
                                            NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                            final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion,Precio,RutaImagen1,
                                                    RutaImagen2,SKU,Categoria,"");
                                            Articulos.add(Articulo);
                                        }
                                    }
                                    final SeleccionarArticuloVentaAdapter ArticuloAdapter = new SeleccionarArticuloVentaAdapter(getContext(), Articulos, tabla_selecciona_articulo);
                                    tabla_selecciona_articulo.setDataAdapter(ArticuloAdapter);

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

}
