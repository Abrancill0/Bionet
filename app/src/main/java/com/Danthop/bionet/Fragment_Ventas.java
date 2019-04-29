package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.MetodoPagoAdapter;
import com.Danthop.bionet.Adapters.SeleccionaApartadoAdapter;
import com.Danthop.bionet.Adapters.SeleccionarArticuloVentaAdapter;
import com.Danthop.bionet.Adapters.VentaArticuloAdapter;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableSeleccionaApartadoTable;
import com.Danthop.bionet.Tables.SortableSeleccionarArticuloTable;
import com.Danthop.bionet.Tables.SortableVentaArticulos;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.ImpuestoDeArticuloApartadoModel;
import com.Danthop.bionet.model.PagoModel;
import com.Danthop.bionet.model.TicketModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
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
    private Button btn_finalizar;
    private Button crear_cliente;
    private Button aceptar_agregar_vendedor;
    private Button btn_apartar;
    private Button Corte_Caja;
    private Dialog dialog;
    private TextView descuento;
    private TextView total;
    private TextView impuesto;
    private TextView subtotal;

    private String usu_id;
    private String nombreCliente;
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
    private SortableSeleccionaApartadoTable tabla_apartados_disponibles;
    private String SKUarticulo;

    private String IDCliente;
    private String NombreCli;


    private List<ClienteModel> clientes;
    private List<ArticuloModel> Articulos;
    private List<ArticuloModel> ArticulosVenta;
    private List<PagoModel> ListaDePagosDisponibles;
    private List<PagoModel> ListaDePagos_a_utilizar;
    private List<ImpuestoDeArticuloApartadoModel> ImpuestosDeArticuloApartado;
    private List<ArticuloApartadoModel> ListaDeArticulosApartados;

    private List<ArticuloApartadoModel> ArticulosApartados;

    private TicketModel ticket_de_venta;

    private FragmentTransaction fr;

    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;

    private Spinner SpinnerVendedor;
    private ArrayList<String> VendedorName;
    private ArrayList<String> VendedorID;

    private ArrayList<String> ArticulosName;

    private ArrayList<String> Imagenes;
    private String[][] LPAU;


    private MetodoPagoAdapter pagoAdapter;

    private CarouselView carouselView;

    private String TicketIDVenta;
    private int TicketSubtotal;
    private String TicketImporteDescuento;
    private float TicketIVA;


    private TableDataClickListener<ArticuloModel> VentaArticuloTablaListener;

    private AutoCompleteTextView Buscar;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private ProgressDialog progreso;


    public Fragment_Ventas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas, container, false);


        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        fr = getFragmentManager().beginTransaction();
        dialog = new Dialog(getContext());

        ArticulosVenta = new ArrayList<>();
        carouselView = (CarouselView) v.findViewById(R.id.carouselView);
        btn_agregar_cliente = v.findViewById(R.id.btn_agregar_cliente);
        btn_agregar_vendedor = v.findViewById(R.id.btn_agregar_vendedor);
        btn_agregar_articulo = v.findViewById(R.id.btn_agregar_articulo);
        btn_feenicia = v.findViewById(R.id.btn_feenicia);
        btn_apartar = v.findViewById(R.id.btn_apartar);
        btn_finalizar = v.findViewById(R.id.btn_finalizar);
        btn_reporte = v.findViewById(R.id.btn_reporte);
        Corte_Caja = v.findViewById(R.id.CorteCaja);
        Buscar = v.findViewById(R.id.buscarXSKU);
        descuento = v.findViewById(R.id.descuento_text);
        total = v.findViewById(R.id.total_text);
        subtotal = v.findViewById(R.id.subtotal_text);
        impuesto = v.findViewById(R.id.iva_text);
        VendedorName = new ArrayList<>();
        VendedorID = new ArrayList<>();
        progreso = new ProgressDialog(getContext());


        SucursalName = new ArrayList<>();
        SucursalID = new ArrayList<>();
        SpinnerSucursal = (Spinner) v.findViewById(R.id.sucursal);

        ArticulosName = new ArrayList<>();
        Imagenes = new ArrayList<>();
        ListaDePagosDisponibles = new ArrayList<>();
        ListaDePagos_a_utilizar = new ArrayList<>();
        ImpuestosDeArticuloApartado = new ArrayList<>();
        ListaDeArticulosApartados = new ArrayList<>();
        ArticulosApartados = new ArrayList<>();


        tabla_venta_articulos = v.findViewById(R.id.tabla_venta_articulos);
        tabla_venta_articulos.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));


        InstanciarModeloTicket();
        LoadSucursales();
        LoadAutocomplete();
        LoadButtons();


        return v;
    }

    public void LoadAutocomplete() {
        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/inventario/obtener_existencias_articulos_app?usu_id=" + usu_id + "&esApp=1";

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject RespuestaResultado = null;
                            JSONArray RespuestaImagenes = null;
                            JSONObject RespuestaUUID = null;
                            JSONObject RespuestaPrecio = null;

                            String RutaImagen1 = "";
                            String RutaImagen2 = "";

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                Articulos = new ArrayList<>();

                                if (EstatusApi == 1) {

                                    RespuestaResultado = response.getJSONObject("resultado");
                                    JSONArray NodoArticulos = RespuestaResultado.getJSONArray("aArticulos");

                                    for (int x = 0; x < NodoArticulos.length(); x++) {
                                        JSONObject elemento = NodoArticulos.getJSONObject(x);

                                        RespuestaUUID = elemento.getJSONObject("art_id");
                                        String UUID = RespuestaUUID.getString("uuid");

                                        RespuestaPrecio = elemento.getJSONObject("ava_precio");
                                        String Precio = RespuestaPrecio.getString("value");
                                        //VERIFICAR MODIFICADORES
                                        String NombreCompleto = "";
                                        String NombreArticulo = elemento.getString("art_nombre");
                                        String NombreVariante = elemento.getString("ava_nombre");
                                        String Descripcion = elemento.getString("art_descripcion");
                                        String Categoria = elemento.getString("cat_nombre");
                                        String SKU = elemento.getString("ava_sku");

                                        String NombreModificador = "";
                                        String Modificadores = elemento.getString("ava_tiene_modificadores");

                                        String Sucursal = elemento.getString("suc_nombre");
                                        if (Sucursal.equals(SpinnerSucursal.getSelectedItem())) {
                                            if (Modificadores == "true") {
                                                NombreModificador = elemento.getString("mod_nombre");

                                                NombreCompleto = NombreArticulo + "/" + NombreVariante + "/" + NombreModificador;


                                                ArticulosName.add(NombreCompleto);
                                            } else {
                                                NombreCompleto = NombreArticulo + "/" + NombreVariante;
                                                ArticulosName.add(NombreCompleto);
                                            }
                                        }

                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.articulo_autocomplete_list, R.id.text_view_list_item, ArticulosName);
                                    Buscar.setAdapter(adapter);
                                    progreso.hide();

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
            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }

    }

    public void LoadSucursales() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/configuracion/sucursales/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONArray RespuestaNodoSucursales = null;
                JSONObject RespuestaNodoID = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");
                    if (status == 1) {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoSucursales = Respuesta.getJSONArray("aSucursales");

                        for (int x = 0; x < RespuestaNodoSucursales.length(); x++) {
                            JSONObject jsonObject1 = RespuestaNodoSucursales.getJSONObject(x);
                            String sucursal = jsonObject1.getString("suc_nombre");
                            SucursalName.add(sucursal);
                            RespuestaNodoID = jsonObject1.getJSONObject("suc_id");
                            String id = RespuestaNodoID.getString("uuid");
                            SucursalID.add(id);
                        }
                        SpinnerSucursal.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SucursalName));

                    } else {
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
                new Response.ErrorListener() {
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

    public void LoadVendedores() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/usuarios/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Respuesta = null;
                JSONObject RespuestaNodoID = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");
                    if (status == 1) {

                        Respuesta = response.getJSONArray("resultado");

                        for (int x = 0; x < Respuesta.length(); x++) {
                            JSONObject jsonObject1 = Respuesta.getJSONObject(x);
                            String vendedor = jsonObject1.getString("usu_nombre");
                            VendedorName.add(vendedor);
                            RespuestaNodoID = jsonObject1.getJSONObject("usu_id");
                            String id = RespuestaNodoID.getString("uuid");
                            VendedorID.add(id);
                        }
                        SpinnerVendedor.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, VendedorName));

                    } else {
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
                new Response.ErrorListener() {
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

    public void LoadImages() {

        ViewListener viewListener = new ViewListener() {

            @Override
            public View setViewForPosition(int position) {
                View customView = getLayoutInflater().inflate(R.layout.image_view, null);
                ImageView myImageView = customView.findViewById(R.id.imageViewParaCarousel);
                Picasso.with(getContext()).load(Imagenes.get(position)).into(myImageView);

                return customView;
            }
        };

        carouselView.setViewListener(viewListener);
        carouselView.setPageCount(Imagenes.size());


    }

    public void LoadButtons() {


        btn_agregar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
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
                        ticket_de_venta.setTic_nombre_cliente(clickedData.getCliente_Nombre());
                        ticket_de_venta.setTic_id_cliente(clickedData.getCliente_usu_id());
                        btn_agregar_cliente.setText(ticket_de_venta.getTic_nombre_cliente());
                        AniadirClienteTicket();

                    }
                };
                tabla_clientes.addDataClickListener(tablaListener);


                crear_cliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        fr.replace(R.id.fragment_container, new Fragment_ventas_crear_cliente()).commit();
                    }
                });
            }
        });

        btn_agregar_vendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.pop_up_ventas_agregar_vendedor);
                dialog.show();
                SpinnerVendedor = dialog.findViewById(R.id.Combo_vendedores);
                LoadVendedores();
                aceptar_agregar_vendedor = (Button) dialog.findViewById(R.id.aceptar_agregar_vendedor);
                aceptar_agregar_vendedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        AniadirVendedorTicket("1");
                        btn_agregar_vendedor.setText(ticket_de_venta.getTic_nombre_vendedor());
                    }
                });

            }
        });

        btn_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container, new Fragment_ventas_transacciones()).commit();
            }
        });

        Corte_Caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container, new Fragment_ventas_corte_caja()).commit();
            }
        });

        btn_agregar_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.pop_up_ventas_seleccionar_articulo);
                dialog.show();
                progreso.show();
                tabla_selecciona_articulo = dialog.findViewById(R.id.tabla_seleccionar_articulos);
                tabla_selecciona_articulo.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                CargaArticulos();
                TableDataClickListener<ArticuloModel> tablaListener = new TableDataClickListener<ArticuloModel>() {
                    @Override
                    public void onDataClicked(int rowIndex, final ArticuloModel clickedData) {
                        dialog.dismiss();
                        String SKU = clickedData.getArticulo_sku();
                        BuscarPorSKU(SKU);

                    }
                };
                tabla_selecciona_articulo.addDataClickListener(tablaListener);
            }
        });

        Buscar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                Aniadir_a_venta(String.valueOf(Buscar.getText()), "1");
                Buscar.setText("");

            }
        });

        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(Articulos.size());
                if (ArticulosVenta.isEmpty()) {
                    dialog.setContentView(R.layout.pop_up_venta_finalizar_sin_articulos);
                    dialog.show();
                    Button aceptar = dialog.findViewById(R.id.Aceptar);
                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else if (false == ArticulosVenta.isEmpty()) {
                    dialog.setContentView(R.layout.pop_up_ventas_facturar);
                    dialog.show();
                    Button si_facturar = dialog.findViewById(R.id.si_facturar);
                    si_facturar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //validar si existe cliente
                            String cliente = "";

                            cliente = (String) btn_agregar_cliente.getText();


                            if (cliente.equals("Cliente")) {
                                Toast toast1 =
                                        Toast.makeText(getContext(), "El cliente es obligatorio para poder facturar", Toast.LENGTH_LONG);
                                toast1.show();

                                dialog.dismiss();

                                return;
                            }

                            ticket_de_venta.setTic_facturar("True");
                            dialog.dismiss();
                            dialog.setContentView(R.layout.pop_up_ventas_metodo_pago);
                            dialog.show();
                            final ListView listaPagos = dialog.findViewById(R.id.lista_de_pagos);
                            CargaMetodosPago(listaPagos);

                            TextView TotalAPagar = dialog.findViewById(R.id.total_a_pagar);
                            double ImporteTotalConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_total());
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            TotalAPagar.setText(formatter.format(ImporteTotalConDecimal));

                            Button realizarPago = dialog.findViewById(R.id.realizar_Pago);
                            realizarPago.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    double totalsumaimportes = 0;

                                    double TarjetaCredito = 0;
                                    double TarjetaDebito = 0;

                                    int count = listaPagos.getCount();

                                    System.out.println(ListaDePagosDisponibles.size());

                                    for (int i = 0; i < count; i++) {

                                        // Here's the critical part I was missing
                                        View childView = listaPagos.getChildAt(i);
                                        TextView labeltext = (TextView) childView.findViewById(R.id.TextMetodo);
                                        EditText editText = (EditText) childView.findViewById(R.id.TextCantidad);

                                        String label = (String) labeltext.getText();
                                        String cantPago = String.valueOf(editText.getText());
                                        if (cantPago.equals("")) {
                                            cantPago = "0";
                                        }

                                        if (labeltext.equals("Tarjeta de crédito"))
                                        {
                                            TarjetaCredito = Double.parseDouble(cantPago);
                                        }

                                        if (labeltext.equals("Tarjeta débito "))
                                        {
                                            TarjetaDebito = Double.parseDouble(cantPago);
                                        }

                                        totalsumaimportes += Double.parseDouble(cantPago);

                                    }

                                    //Validar montos antes de pasar de pantallas
                                    String TotalText = String.valueOf(total.getText());
                                    double TotalFormat = 0;
                                    String cleanString = TotalText.replaceAll("\\D", "");
                                    try {
                                        TotalFormat = Double.parseDouble(cleanString);
                                        TotalFormat = TotalFormat / 100;
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    if (TotalFormat > totalsumaimportes) {
                                        Toast toast1 =
                                                Toast.makeText(getContext(), "El monto capturado es menor al total de la venta", Toast.LENGTH_LONG);
                                        toast1.show();

                                        return;
                                    }

                                    dialog.dismiss();
                                    dialog.setContentView(R.layout.pop_up_ventas_confirmacion_venta);
                                    dialog.show();

                                    TextView importe_venta = dialog.findViewById(R.id.importe_venta);
                                    TextView importe_recibido = dialog.findViewById(R.id.importe_recibido);
                                    TextView importe_cambio = dialog.findViewById(R.id.importe_cambio);


                                    for (int i = 0; i < count; i++) {

                                        // Here's the critical part I was missing
                                        View childView = listaPagos.getChildAt(i);
                                        TextView labeltext = (TextView) childView.findViewById(R.id.TextMetodo);
                                        EditText editText = (EditText) childView.findViewById(R.id.TextCantidad);

                                        String label = (String) labeltext.getText();
                                        String cantPago = String.valueOf(editText.getText());
                                        if (cantPago.equals("")) {
                                            cantPago = "0";
                                        }
                                        int valor = Integer.parseInt(cantPago);
                                        if (valor <= 0) {
                                            ListaDePagosDisponibles.remove(i);
                                            count = count - 1;
                                        } else {
                                            String idPago = ListaDePagosDisponibles.get(i).getId();
                                            PagoModel pago = new PagoModel(ListaDePagosDisponibles.get(i).getNombre(),
                                                    idPago, cantPago);
                                            ListaDePagos_a_utilizar.add(pago);
                                        }

                                    }

                                    double valorTarjetas = 0;

                                    valorTarjetas = TarjetaCredito + TarjetaDebito;

                                    if (valorTarjetas > 0)
                                    {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("nombre", "");
                                        bundle.putString("descripcion", "");
                                        bundle.putString("precio", "");


                                       // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                       // FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                                       // Fragment_selecciona_tipo_publicacion secondFragment = new Fragment_selecciona_tipo_publicacion();
                                       //  secondFragment.setArguments(bundle);

                                      //  fragmentTransaction.replace(R.id.fragment_container, secondFragment);
                                      //  fragmentTransaction.commit();

                                        Intent myIntent = new Intent(getActivity(), Feenicia_Transaction_Bluetooth.class);
                                        getActivity().startActivity(myIntent);
                                    }
                                    else
                                    {
                                        FinalizarTicket(importe_cambio, importe_recibido, importe_venta);
                                    }


                                    Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                                    aceptar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog.dismiss();
                                        }
                                    });

                                }
                            });
                        }
                    });
                    Button no_facturar = dialog.findViewById(R.id.no_facturar);
                    no_facturar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ticket_de_venta.setTic_facturar("False");
                            dialog.dismiss();
                            dialog.setContentView(R.layout.pop_up_ventas_metodo_pago);
                            dialog.show();
                            final ListView listaPagos = dialog.findViewById(R.id.lista_de_pagos);
                            CargaMetodosPago(listaPagos);

                            TextView TotalAPagar = dialog.findViewById(R.id.total_a_pagar);
                            double ImporteTotalConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_total());
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            TotalAPagar.setText(formatter.format(ImporteTotalConDecimal));

                            Button realizarPago = dialog.findViewById(R.id.realizar_Pago);
                            realizarPago.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    double totalsumaimportes = 0;

                                    double TarjetaCredito = 0;
                                    double TarjetaDebito = 0;

                                    int count = listaPagos.getCount();

                                    System.out.println(ListaDePagosDisponibles.size());

                                    for (int i = 0; i < count; i++) {

                                        // Here's the critical part I was missing
                                        View childView = listaPagos.getChildAt(i);
                                        TextView labeltext = (TextView) childView.findViewById(R.id.TextMetodo);
                                        EditText editText = (EditText) childView.findViewById(R.id.TextCantidad);

                                        String label = (String) labeltext.getText();
                                        String cantPago = String.valueOf(editText.getText());
                                        if (cantPago.equals("")) {
                                            cantPago = "0";
                                        }

                                        if (labeltext.equals("Tarjeta de crédito"))
                                        {
                                            TarjetaCredito = Double.parseDouble(cantPago);
                                        }

                                        if (labeltext.equals("Tarjeta débito "))
                                        {
                                            TarjetaDebito = Double.parseDouble(cantPago);
                                        }

                                        totalsumaimportes += Double.parseDouble(cantPago);

                                    }

                                    //Validar montos antes de pasar de pantallas
                                    String TotalText = String.valueOf(total.getText());
                                    double TotalFormat = 0;
                                    String cleanString = TotalText.replaceAll("\\D", "");
                                    try {
                                        TotalFormat = Double.parseDouble(cleanString);
                                        TotalFormat = TotalFormat / 100;
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                    if (TotalFormat > totalsumaimportes) {
                                        Toast toast1 =
                                                Toast.makeText(getContext(), "El monto capturado es menor al total de la venta", Toast.LENGTH_LONG);
                                        toast1.show();

                                        return;
                                    }

                                    dialog.dismiss();
                                    dialog.setContentView(R.layout.pop_up_ventas_confirmacion_venta);
                                    dialog.show();

                                    TextView importe_venta = dialog.findViewById(R.id.importe_venta);
                                    TextView importe_recibido = dialog.findViewById(R.id.importe_recibido);
                                    TextView importe_cambio = dialog.findViewById(R.id.importe_cambio);


                                    for (int i = 0; i < count; i++) {

                                        // Here's the critical part I was missing
                                        View childView = listaPagos.getChildAt(i);
                                        TextView labeltext = (TextView) childView.findViewById(R.id.TextMetodo);
                                        EditText editText = (EditText) childView.findViewById(R.id.TextCantidad);

                                        String label = (String) labeltext.getText();
                                        String cantPago = String.valueOf(editText.getText());
                                        if (cantPago.equals("")) {
                                            cantPago = "0";
                                        }
                                        int valor = Integer.parseInt(cantPago);
                                        if (valor <= 0) {
                                            ListaDePagosDisponibles.remove(i);
                                            count = count - 1;
                                        } else {
                                            String idPago = ListaDePagosDisponibles.get(i).getId();
                                            PagoModel pago = new PagoModel(ListaDePagosDisponibles.get(i).getNombre(),
                                                    idPago, cantPago);
                                            ListaDePagos_a_utilizar.add(pago);
                                        }

                                    }

                                    double valorTarjetas = 0;

                                    valorTarjetas = TarjetaCredito + TarjetaDebito;

                                    if (valorTarjetas > 0)
                                    {
                                        Intent myIntent = new Intent(getActivity(), Feenicia_Transaction_Bluetooth.class);
                                        getActivity().startActivity(myIntent);
                                    }
                                    else
                                    {
                                        FinalizarTicket(importe_cambio, importe_recibido, importe_venta);
                                    }


                                    Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                                    aceptar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });

                        }
                    });
                }
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

        btn_apartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.pop_up_ventas_selecciona_apartados);
                dialog.show();
                tabla_apartados_disponibles = dialog.findViewById(R.id.tabla_seleccionar_apartados);
                tabla_apartados_disponibles.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                final SeleccionaApartadoAdapter articuloAdapter = new SeleccionaApartadoAdapter(getContext(), ListaDeArticulosApartados, tabla_apartados_disponibles,ticket_de_venta,ArticulosApartados);
                articuloAdapter.notifyDataSetChanged();
                tabla_apartados_disponibles.setDataAdapter(articuloAdapter);

            }
        });


        // BuscarPorSKU(Buscar.getCompletionHint().toString());


    }


    private void FinalizarTicket(final TextView importeCambio, final TextView importeRecibido, final TextView importeVenta) {


        JSONArray arreglo = new JSONArray();
        try {
            for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {
                JSONObject list1 = new JSONObject();
                list1.put("fpa_id", ListaDePagos_a_utilizar.get(i).getId());
                list1.put("valor", ListaDePagos_a_utilizar.get(i).getCantidad());
                arreglo.put(list1);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }


        JSONObject request = new JSONObject();

        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id", ticket_de_venta.getTic_id());
            request.put("tic_importe_metodo_pago", arreglo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/pagar-ticket";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {

                        JSONObject Respuesta = response.getJSONObject("resultado");
                        JSONObject RespuestaNodoTicket = Respuesta.getJSONObject("aTicket");
                        String tic_importe_total = RespuestaNodoTicket.getString("tic_importe_total");
                        String tic_importe_recibido = RespuestaNodoTicket.getString("tic_importe_recibido");
                        String tic_importe_cambio = RespuestaNodoTicket.getString("tic_importe_cambio");
                        ticket_de_venta.setTic_importe_total(tic_importe_total);
                        ticket_de_venta.setTic_importe_recibido(tic_importe_recibido);
                        ticket_de_venta.setTic_importe_cambio(tic_importe_cambio);

                        double CambioConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_cambio());
                        double RecibidoConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_recibido());
                        double ImporteTotalConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_total());
                        NumberFormat formatter = NumberFormat.getCurrencyInstance();

                        importeCambio.setText(formatter.format(CambioConDecimal));
                        importeRecibido.setText(formatter.format(RecibidoConDecimal));
                        importeVenta.setText(formatter.format(ImporteTotalConDecimal));

                        ArticulosVenta.clear();
                        Imagenes.clear();
                        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                                total, descuento, impuesto, subtotal,
                                carouselView, Imagenes,ImpuestosDeArticuloApartado,ListaDeArticulosApartados);
                        articuloAdapter.notifyDataSetChanged();
                        tabla_venta_articulos.setDataAdapter(articuloAdapter);
                        LoadImages();


                        InstanciarModeloTicket();
                        ListaDePagos_a_utilizar.clear();

                        descuento.setText("$0.00");
                        total.setText("$0.00");
                        subtotal.setText("$0.00");
                        impuesto.setText("$0.00");

                    } else {
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }


    private void Aniadir_a_venta(String CBoSKULL, String Cantidad) {
        ArticulosVenta.clear();
        ListaDeArticulosApartados.clear();
        ticket_de_venta.setTic_id_sucursal(SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal());
            request.put("articulo", CBoSKULL);
            request.put("cantidad", Cantidad);
            request.put("tic_id", ticket_de_venta.getTic_id());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/store";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoTicket = null;
                JSONObject TicketID = null;
                JSONArray NodoTicketArticulos = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {

                        Respuesta = response.getJSONObject("resultado");
                        Imagenes.clear();
                        RespuestaNodoTicket = Respuesta.getJSONObject("aTicket");
                        TicketID = RespuestaNodoTicket.getJSONObject("tic_id");
                        TicketIDVenta = TicketID.getString("uuid");


                        float ImpuestoTotal = 0;
                        float Subtotal = 0;
                        JSONArray tic_impuestos = RespuestaNodoTicket.getJSONArray("tic_impuestos");
                        for (int f = 0; f < tic_impuestos.length(); f++) {
                            JSONObject nodo_impuestos = tic_impuestos.getJSONObject(f);
                            ImpuestoTotal = Float.parseFloat(nodo_impuestos.getString("valor"));
                        }
                        float DescuentoTotal = Float.parseFloat(RespuestaNodoTicket.getString("tic_importe_descuentos"));
                        float PrecioTotal = Float.parseFloat(RespuestaNodoTicket.getString("tic_importe_total"));


                        JSONArray NodoArticuloTicket = Respuesta.getJSONArray("aDetalleTicket");
                        for (int j = 0; j < NodoArticuloTicket.length(); j++) {
                            JSONObject nodo = NodoArticuloTicket.getJSONObject(j);

                            float numero_de_productos = Float.parseFloat((nodo.getString("tar_cantidad")));

                            float PrecioSubTotalProducto = 0;


                            //Sumar Subtotal
                            float SubTot = Float.parseFloat(nodo.getString("art_precio_bruto"));
                            PrecioSubTotalProducto = PrecioSubTotalProducto + SubTot;
                            PrecioSubTotalProducto = PrecioSubTotalProducto * numero_de_productos;

                            Subtotal = Subtotal + PrecioSubTotalProducto;

                            String AplicaApartados = nodo.getString("art_aplica_apartados");

                            if (AplicaApartados.equals("true")) {
                                String ArticuloCantidad = nodo.getString("tar_cantidad");
                                String NombreArticuloApartado = nodo.getString("tar_nombre_articulo");
                                JSONObject NodoIDApartado = nodo.getJSONObject("tar_id_articulo");
                                String ArticuloIDApartado = NodoIDApartado.getString("uuid");
                                JSONObject NodoIDApartadoVariante = nodo.getJSONObject("tar_id_variante");
                                String ArticuloIDApartadoVariante = NodoIDApartadoVariante.getString("uuid");
                                String ArticuloIDApartadoModificador = nodo.getString("tar_id_modificador");
                                String ArticuloApartadoImportePagado = nodo.getString("tar_importe_pagado");
                                String ArticuloIDApartadoExistencia = nodo.getString("tar_id_existencia");
                                String ArticuloApartadoAplicaDevolucion = nodo.getString("tar_aplica_para_devolucion");
                                String ArticuloApartadoImporteDescuento = nodo.getString("tar_importe_descuento");
                                String ArticuloApartadoTotal = nodo.getString("tar_importe_total");
                                JSONObject nodoImpuestos = nodo.getJSONObject("tar_impuestos");
                                Iterator keys = nodoImpuestos.keys();
                                while (keys.hasNext()) {
                                    Object key = keys.next();
                                    String value = nodoImpuestos.getString((String) key);
                                    ImpuestoDeArticuloApartadoModel Impuesto = new ImpuestoDeArticuloApartadoModel((String) key, value);
                                    ImpuestosDeArticuloApartado.add(Impuesto);
                                }
                                String ArticuloApartadoPorcentajeDescuento = nodo.getString("tar_porcentaje_descuento");
                                String ArticuloApartadoPrecio = nodo.getString("tar_precio_articulo");


                                float importePagado = Float.parseFloat((ArticuloApartadoImportePagado));
                                float importeTotal = Float.parseFloat((ArticuloApartadoTotal));
                                float importeRestante = importeTotal - importePagado;

                                ArticuloApartadoModel ArticuloApartado = new ArticuloApartadoModel(
                                        ArticuloCantidad,
                                        ArticuloIDApartado,
                                        ArticuloIDApartadoVariante,
                                        ArticuloIDApartadoModificador,
                                        ArticuloApartadoImportePagado,
                                        String.valueOf(importeRestante),
                                        NombreArticuloApartado,
                                        ArticuloIDApartadoExistencia,
                                        ArticuloApartadoAplicaDevolucion,
                                        ArticuloApartadoImporteDescuento,
                                        ArticuloApartadoTotal,
                                        ImpuestosDeArticuloApartado,
                                        ArticuloApartadoPorcentajeDescuento,
                                        ArticuloApartadoPrecio);
                                ListaDeArticulosApartados.add(ArticuloApartado);
                            }
                        }


                        //Se modifican los datos del ticket de venta
                        ticket_de_venta.setTic_id(TicketIDVenta);
                        ticket_de_venta.setTic_importe_descuentos(String.valueOf(DescuentoTotal));
                        ticket_de_venta.setTic_importe_total(String.valueOf(PrecioTotal));
                        ticket_de_venta.setTic_impuestos(String.valueOf(ImpuestoTotal));


                        NumberFormat formatter = NumberFormat.getCurrencyInstance();

                        double Price = Double.parseDouble(ticket_de_venta.getTic_importe_total());
                        total.setText(formatter.format(Price));

                        double descu = Double.parseDouble(ticket_de_venta.getTic_importe_descuentos());
                        descuento.setText(formatter.format(descu));

                        double Iva = Double.parseDouble(ticket_de_venta.getTic_impuestos());
                        impuesto.setText(formatter.format(Iva));

                        double sub = Double.parseDouble(String.valueOf(Subtotal));
                        subtotal.setText(formatter.format(sub));


                        NodoTicketArticulos = Respuesta.getJSONArray("aDetalleTicket");
                        for (int x = 0; x < NodoTicketArticulos.length(); x++) {
                            JSONObject elemento = NodoTicketArticulos.getJSONObject(x);
                            JSONObject NodoTarID = elemento.getJSONObject("tar_id");
                            String tar_id = NodoTarID.getString("uuid");
                            String NombreArticulo = elemento.getString("tar_nombre_articulo");
                            String SKUArticulo = elemento.getString("art_sku");
                            String cantidad = elemento.getString("tar_cantidad");
                            String precio = elemento.getString("tar_precio_articulo");
                            String descuento = elemento.getString("art_importe_descuento");
                            String importe = elemento.getString("tar_importe_total");

                            JSONArray RespuestaImagenes = elemento.getJSONArray("art_imagenes");
                            for (int z = 0; z < RespuestaImagenes.length(); z++) {
                                String RutaImagen = "http://192.168.100.192:8010" + RespuestaImagenes.getString(z);
                                Imagenes.add(RutaImagen);


                            }


                            final ArticuloModel articulo = new ArticuloModel("",
                                    NombreArticulo,
                                    "",
                                    precio,
                                    "",
                                    "",
                                    SKUArticulo,
                                    "",
                                    cantidad,
                                    tar_id,
                                    descuento,
                                    "",
                                    importe,"","",""
                            );
                            ArticulosVenta.add(articulo);
                        }
                        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                                total, descuento, impuesto, subtotal,
                                carouselView, Imagenes, ImpuestosDeArticuloApartado,ListaDeArticulosApartados);
                        articuloAdapter.notifyDataSetChanged();
                        tabla_venta_articulos.setDataAdapter(articuloAdapter);
                        LoadImages();
                    } else {
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }



    private void Muestra_clientes() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/clientes/index_app";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject Respuesta = null;
                JSONObject RespuestaNodoDireccion = null;
                JSONObject ElementoUsuario = null;
                JSONArray RespuestaNodoClientes = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoClientes = Respuesta.getJSONArray("aClientes");

                        for (int x = 0; x < RespuestaNodoClientes.length(); x++) {
                            JSONObject elemento = RespuestaNodoClientes.getJSONObject(x);

                            ElementoUsuario = elemento.getJSONObject("cli_id");

                            UUID = ElementoUsuario.getString("uuid");
                            nombreCliente = elemento.getString("cli_nombre");
                            correo_electronico = elemento.getString("cli_correo_electronico");

                            telefono = elemento.getString("cli_telefono");
                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion");
                            calle = RespuestaNodoDireccion.getString("cli_calle");

                            estado = RespuestaNodoDireccion.getString("cli_estado");
                            colonia = RespuestaNodoDireccion.getString("cli_colonia");
                            num_int = RespuestaNodoDireccion.getString("cli_numero_interior");
                            num_ext = RespuestaNodoDireccion.getString("cli_numero_exterior");
                            cp = RespuestaNodoDireccion.getString("cli_codigo_postal");
                            ciudad = RespuestaNodoDireccion.getString("cli_ciudad");
                            municipio = RespuestaNodoDireccion.getString("cli_ciudad");

                            rfc = elemento.getString("cli_rfc");
                            razon_social = elemento.getString("cli_razon_social");

                            RespuestaNodoDireccion = elemento.getJSONObject("cli_direccion_fiscal");
                            cp_fiscal = RespuestaNodoDireccion.getString("cli_codigo_postal");
                            estado_fiscal = RespuestaNodoDireccion.getString("cli_estado");
                            municipio_fiscal = RespuestaNodoDireccion.getString("cli_ciudad");
                            colonia_fiscal = RespuestaNodoDireccion.getString("cli_colonia");
                            calle_fiscal = RespuestaNodoDireccion.getString("cli_calle");
                            num_ext_fiscal = RespuestaNodoDireccion.getString("cli_numero_exterior");
                            num_int_fiscal = RespuestaNodoDireccion.getString("cli_numero_interior");


                            direccion_igual = elemento.getString("cli_direcciones_iguales");
                            if (direccion_igual.equals("false")) {
                                direccion_fiscal = calle_fiscal + " " + num_ext_fiscal + " " + num_int_fiscal + " " + colonia_fiscal + " " + cp_fiscal + " " + estado_fiscal + " " + municipio_fiscal;
                            } else if (direccion_igual.equals("true")) {
                                direccion_fiscal = calle + " " + num_ext + " " + num_int + " " + colonia + " " + cp + " " + estado + " " + municipio;

                            }

                            correo_igual = elemento.getString("cli_correos_iguales");
                            if (correo_igual.equals("false")) {
                                email_fiscal = elemento.getString("cli_correo_electronico_facturacion");
                            } else if (correo_igual.equals("true")) {
                                email_fiscal = correo_electronico;
                            }


                            final ClienteModel cliente = new ClienteModel(UUID,
                                    nombreCliente,
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
                        final ClienteAdapter clienteAdapter = new ClienteAdapter(getContext(), clientes, tabla_clientes, fr);
                        tabla_clientes.setDataAdapter(clienteAdapter);
                    } else {
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
                new Response.ErrorListener() {
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

    private void CargaArticulos() {

        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/inventario/obtener_existencias_articulos_app?usu_id=" + usu_id + "&esApp=1";

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject RespuestaResultado = null;
                            JSONArray RespuestaImagenes = null;
                            JSONObject RespuestaUUID = null;
                            JSONObject RespuestaPrecio = null;

                            String RutaImagen1 = "";
                            String RutaImagen2 = "";

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                Articulos = new ArrayList<>();

                                if (EstatusApi == 1) {

                                    RespuestaResultado = response.getJSONObject("resultado");
                                    JSONArray NodoArticulos = RespuestaResultado.getJSONArray("aArticulos");

                                    for (int x = 0; x < NodoArticulos.length(); x++) {
                                        JSONObject elemento = NodoArticulos.getJSONObject(x);

                                        RespuestaUUID = elemento.getJSONObject("art_id");
                                        String UUID = RespuestaUUID.getString("uuid");

                                        RespuestaPrecio = elemento.getJSONObject("ava_precio");
                                        String Precio = RespuestaPrecio.getString("value");
                                        //VERIFICAR MODIFICADORES
                                        String NombreCompleto = "";
                                        String NombreArticulo = elemento.getString("art_nombre");
                                        String NombreVariante = elemento.getString("ava_nombre");
                                        String Descripcion = elemento.getString("art_descripcion");
                                        String Categoria = elemento.getString("cat_nombre");
                                        String SKU = elemento.getString("ava_sku");

                                        String NombreModificador = "";
                                        String Modificadores = elemento.getString("ava_tiene_modificadores");

                                        RespuestaImagenes = elemento.getJSONArray("imagenes");

                                        for (int z = 0; z < RespuestaImagenes.length(); z++) {

                                            JSONObject elemento3 = RespuestaImagenes.getJSONObject(z);

                                            if (RutaImagen1.equals("")) {
                                                RutaImagen1 = elemento3.getString("aim_url");
                                            } else {
                                                RutaImagen2 = elemento3.getString("aim_url");
                                            }

                                        }

                                        String Sucursal = elemento.getString("suc_nombre");
                                        if (Sucursal.equals(SpinnerSucursal.getSelectedItem())) {
                                            if (Modificadores == "true") {
                                                NombreModificador = elemento.getString("mod_nombre");

                                                Precio = elemento.getString("amo_precio_lista");

                                                System.out.println(NombreVariante);
                                                NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                                SKU = elemento.getString("amo_sku");

                                                final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion, Precio,RutaImagen1,
                                                        RutaImagen2,SKU,Categoria,"","","","",
                                                        "","","","");
                                                Articulos.add(Articulo);
                                            } else {
                                                NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;


                                                final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion,Precio,RutaImagen1,
                                                        RutaImagen2,SKU,Categoria,"","","","",
                                                        "","","","");
                                                Articulos.add(Articulo);
                                            }
                                        }

                                    }
                                    final SeleccionarArticuloVentaAdapter ArticuloAdapter = new SeleccionarArticuloVentaAdapter(getContext(), Articulos, tabla_selecciona_articulo);
                                    tabla_selecciona_articulo.setDataAdapter(ArticuloAdapter);
                                    progreso.hide();

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
            getRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    private void CargaMetodosPago(final ListView Listview) {

        ListaDePagosDisponibles.clear();
        ticket_de_venta.setTic_id_sucursal(SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/select-formas-pago";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Respuesta = null;
                JSONObject RespuestaNodoTicket = null;
                JSONObject TicketID = null;
                JSONArray NodoTicketArticulos = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {

                        Respuesta = response.getJSONArray("resultado");

                        for (int x = 0; x < Respuesta.length(); x++) {
                            JSONObject elemento = Respuesta.getJSONObject(x);
                            String id = elemento.getString("fpa_id");
                            String nombre = elemento.getString("fpa_nombre");


                            final PagoModel pago = new PagoModel(
                                    nombre,
                                    id,
                                    ""
                            );
                            ListaDePagosDisponibles.add(pago);
                        }
                        pagoAdapter = new MetodoPagoAdapter(getContext(), R.layout.caja_metodo_pago, ListaDePagosDisponibles, Listview,
                                ticket_de_venta);
                        pagoAdapter.notifyDataSetChanged();
                        Listview.setAdapter(pagoAdapter);
                    } else {
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);


    }


    private void AniadirClienteTicket() {
        ticket_de_venta.setTic_id_sucursal(SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
        //ArticulosVenta.clear();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id", ticket_de_venta.getTic_id());
            request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal());
            request.put("tic_id_cliente", ticket_de_venta.getTic_id_cliente());
            request.put("tic_nombre_cliente", ticket_de_venta.getTic_nombre_cliente());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/store-cliente";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Respuesta = null;
                JSONObject RespuestaNodoTicket = null;
                JSONObject TicketID = null;
                JSONArray NodoTicketArticulos = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    } else {
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
                new Response.ErrorListener() {
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

    private void AniadirVendedorTicket(String Comision) {
        ticket_de_venta.setTic_id_sucursal(SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
        ticket_de_venta.setTic_id_vendedor(VendedorID.get(SpinnerVendedor.getSelectedItemPosition()));
        ticket_de_venta.setTic_nombre_vendedor(String.valueOf(SpinnerVendedor.getSelectedItem()));
        ticket_de_venta.setTic_comision(Comision);
        //ArticulosVenta.clear();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id", ticket_de_venta.getTic_id());
            request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal());
            request.put("tic_id_vendedor", ticket_de_venta.getTic_id_vendedor());
            request.put("tic_nombre_vendedor", ticket_de_venta.getTic_nombre_vendedor());
            request.put("tic_comision", ticket_de_venta.getTic_comision());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/store-vendedor";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray Respuesta = null;
                JSONObject RespuestaNodoTicket = null;
                JSONObject TicketID = null;
                JSONArray NodoTicketArticulos = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    } else {
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
                new Response.ErrorListener() {
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

    private void BuscarPorSKU(String SKU) {
        dialog.setContentView(R.layout.pop_up_ventas_verificar_articulo);
        final TextView art_nombre = dialog.findViewById(R.id.art_name_articulo);
        final TextView art_categoria = dialog.findViewById(R.id.art_name_categoria);
        final TextView art_decription = dialog.findViewById(R.id.art_descripcion);
        final TextView art_precio = dialog.findViewById(R.id.art_precio);
        final ImageView imagenArticulo = dialog.findViewById(R.id.imagen_articulo);
        final ElegantNumberButton art_cantidad = dialog.findViewById(R.id.art_cantidad);
        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/articulos/buscar_sku_articulo?usu_id=" + usu_id + "&esApp=1&sku=" + SKU;

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject RespuestaResultado = null;
                            JSONArray RespuestaImagenes = null;
                            JSONObject RespuestaUUID = null;
                            JSONObject RespuestaPrecio = null;
                            JSONObject RespuestaPrecioModificador = null;
                            JSONArray RespuestaModificadores = null;

                            String RutaImagen1 = "";
                            String RutaImagen2 = "";

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                Articulos = new ArrayList<>();

                                if (EstatusApi == 1) {

                                    RespuestaResultado = response.getJSONObject("resultado");

                                    RespuestaUUID = RespuestaResultado.getJSONObject("art_id");
                                    String UUID = RespuestaUUID.getString("uuid");

                                    RespuestaPrecio = RespuestaResultado.getJSONObject("ava_precio");
                                    String Precio = RespuestaPrecio.getString("value");
                                    //VERIFICAR MODIFICADORES
                                    String NombreCompleto = "";
                                    String NombreArticulo = RespuestaResultado.getString("art_nombre");
                                    String NombreVariante = RespuestaResultado.getString("ava_nombre");
                                    String Descripcion = RespuestaResultado.getString("art_descripcion");
                                    String Categoria = RespuestaResultado.getString("cat_nombre");
                                    SKUarticulo = RespuestaResultado.getString("ava_sku");

                                    String NombreModificador = "";
                                    String Modificadores = RespuestaResultado.getString("ava_tiene_modificadores");

                                    RespuestaImagenes = RespuestaResultado.getJSONArray("imagenes");

                                    for (int z = 0; z < RespuestaImagenes.length(); z++) {

                                        JSONObject elemento3 = RespuestaImagenes.getJSONObject(z);

                                        if (RutaImagen1.equals("")) {
                                            RutaImagen1 = elemento3.getString("aim_url");
                                        } else {
                                            RutaImagen2 = elemento3.getString("aim_url");
                                        }

                                    }

                                    if (Modificadores == "true") {
                                        NombreModificador = RespuestaResultado.getString("mod_nombre");
                                        RespuestaPrecioModificador = RespuestaResultado.getJSONObject("amo_precio");

                                        Precio = RespuestaPrecioModificador.getString("value");

                                        NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;
                                        SKUarticulo = RespuestaResultado.getString("amo_sku");

                                            final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion, Precio,RutaImagen1,
                                                    RutaImagen2,SKUarticulo,Categoria,"","",
                                                    "","","","","","");
                                            Articulos.add(Articulo);

                                    } else {
                                        NombreCompleto = NombreArticulo + " " + NombreVariante + " " + NombreModificador;

                                        final ArticuloModel Articulo = new ArticuloModel(UUID,NombreCompleto,Descripcion,Precio,RutaImagen1,
                                                RutaImagen2,SKUarticulo,Categoria,"","",
                                                "","","","","","");
                                        Articulos.add(Articulo);
                                    }


                                    //Cambiar dato de precio a formato de dinero.
                                    double PrecioConDecimal = Double.parseDouble(Precio);
                                    NumberFormat formatter = NumberFormat.getCurrencyInstance();


                                    art_nombre.setText(NombreCompleto);
                                    art_precio.setText(formatter.format(PrecioConDecimal));
                                    art_categoria.setText(Categoria);
                                    art_decription.setText(Descripcion);
                                    String ruta = "http://192.168.100.192:8010" + RutaImagen1;
                                    imageLoader.displayImage(ruta, imagenArticulo);

                                    if (NombreCompleto.equals("")) {
                                        Toast toast1 =
                                                Toast.makeText(getContext(),
                                                        "No se encontró ningún artículo", Toast.LENGTH_LONG);
                                        toast1.show();
                                    } else {
                                        dialog.show();
                                    }


                                    Button Aniadir = dialog.findViewById(R.id.Aniadir_articulo);
                                    Aniadir.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Aniadir_a_venta(SKUarticulo, art_cantidad.getNumber());
                                            dialog.dismiss();
                                        }
                                    });


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

            getRequest.setShouldCache(false);
            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    private void InstanciarModeloTicket() {
        ticket_de_venta = new TicketModel(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""

        );
    }

}
