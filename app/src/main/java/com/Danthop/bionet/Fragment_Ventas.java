package com.Danthop.bionet;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.Danthop.bionet.Adapters.ListaTicketsAdapter;
import com.Danthop.bionet.Adapters.MetodoPagoAdapter;
import com.Danthop.bionet.Adapters.SeleccionaApartadoAdapter;
import com.Danthop.bionet.Adapters.SeleccionaOrdenEspecialAdapter;
import com.Danthop.bionet.Adapters.SeleccionarArticuloVentaAdapter;
import com.Danthop.bionet.Adapters.SeleccionarMesesCreditoAdapter;
import com.Danthop.bionet.Adapters.VentaArticuloAdapter;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableMetodosPagoTable;
import com.Danthop.bionet.Tables.SortableSeleccionaApartadoTable;
import com.Danthop.bionet.Tables.SortableSeleccionaOrdenEspecialTable;
import com.Danthop.bionet.Tables.SortableSeleccionarArticuloTable;
import com.Danthop.bionet.Tables.SortableSeleccionarPromocionTable;
import com.Danthop.bionet.Tables.SortableVentaArticulos;
import com.Danthop.bionet.model.ArticuloApartadoModel;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.CorteCajaModel;
import com.Danthop.bionet.model.FormaspagoModel;
import com.Danthop.bionet.model.Impuestos;
import com.Danthop.bionet.model.MovimientoModel;
import com.Danthop.bionet.model.OrdenEspecialArticuloModel;
import com.Danthop.bionet.model.PagoModel;
import com.Danthop.bionet.model.PromocionesModel;
import com.Danthop.bionet.model.TicketModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.webviewtopdf.PdfView;
import com.zj.usbsdk.UsbController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;

import static com.mercadolibre.android.sdk.internal.ApiPoolManager.cancelAll;

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
    private Button btn_ordenar;
    private Button Corte_Caja;
    private Button Comisiones;
    private Dialog dialog;
    private TextView descuento;
    private TextView total;
    private TextView impuesto;
    private TextView subtotal;
    private String usu_id;
    private String cca_id_sucursal;
    private String valueIdSuc;
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
    private SortableSeleccionarPromocionTable tabla_selecciona_meses;
    private SortableVentaArticulos tabla_venta_articulos;
    private SortableSeleccionaApartadoTable tabla_apartados_disponibles;
    private SortableSeleccionaOrdenEspecialTable tabla_ordenes_disponibles;
    private SortableMetodosPagoTable tabla_metodos_pago;
    private String SKUarticulo;
    private String IDCliente;
    private String NombreCli;
    private List<ClienteModel> clientes;
    private List<ArticuloModel> Articulos;
    private List<PromocionesModel> Meses;
    private List<ArticuloModel> ArticulosVenta;
    private List<PagoModel> ListaDePagosDisponibles;
    private List<PagoModel> ListaDePagos_a_utilizar;
    private List<Impuestos> ImpuestosDeArticuloApartado;
    private List<Impuestos> ImpuestosDeArticuloOrdenado;
    private List<ArticuloApartadoModel> ListaDeArticulosApartados;
    private List<OrdenEspecialArticuloModel> ListaDeArticulosOrdenados;
    private List<ArticuloApartadoModel> ArticulosApartados;
    private List<OrdenEspecialArticuloModel> ArticulosOrdenados;
    private TicketModel ticket_de_venta;
    private FragmentTransaction fr;
    private Spinner SpinnerSucursal;
    private ArrayList<String> SucursalName;
    private ArrayList<String> SucursalID;
    private Spinner SpinnerVendedor;
    private ArrayList<String> VendedorName;
    private ArrayList<String> VendedorID;
    private Spinner SpinnerCFDI;
    private ArrayList<String> CFDI_Name;
    private ArrayList<String> CFDI_ID;
    private ArrayList<String> ArticulosName;
    private ArrayList<String> Imagenes;
    private String[][] LPAU;
    private CarouselView carouselView;
    private String TicketIDVenta;
    private int TicketSubtotal;
    private String TicketImporteDescuento;
    private float TicketIVA;
    private String ticketid;
    private ArrayList<String> ArticulosConExistencias = new ArrayList<>();

    private TableDataClickListener<ArticuloModel> VentaArticuloTablaListener;

    private AutoCompleteTextView Buscar;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private ProgressDialog progreso;

    private String DiasApartado;

    private List<ArticuloModel> ListaArticulosTicket = new ArrayList<>();
    private String content;
    private WebView webView;


    private String NombreSucursal="";
    private String NumeroSucursal="";
    private String Direccion="";
    private String RazonSocial="";
    private String RFC="";
    private String LogoNegocio="";
    private String FechaCreacion="";
    private String NombreVendedor="";
    private String NombreCliente="";
    private String NumeroTicket="";
    private String Subtotal = "";
    private String Total = "";
    private String ImpuestosTicket="";
    private float ImpuestosTotal=0;

    private String NomPromoCredito;
    private String SKU_product;
    private String Resptresmeses="";
    private String Respseismeses="";
    private String Respnuevemeses="";
    private String Respdocemeses="";
    private String Respuestamenor="";
    private TextView textViewNombre;
    private String value;
    private int Resp;
    private Double TarjetaCredito = 0.0;
    private Double TarjetaDebito = 0.0;

    private int[][] u_infor;
    static UsbController usbCtrl = null;
    static UsbDevice dev = null;
    private ProgressDialog progressDialog;

    private Button btn_imprimir;

    public Fragment_Ventas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas, container, false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.show();

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");
        fr = getFragmentManager().beginTransaction();
        dialog = new Dialog(getContext());

        usbCtrl = new UsbController(getActivity(),mHandler);


        try {
            JSONArray jsonArray = new JSONArray(cca_id_sucursal);
            //for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject JsonObj = jsonArray.getJSONObject(0);
            Iterator<String> iter = JsonObj.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                valueIdSuc = String.valueOf(JsonObj.get(key));
            }
            //}
        } catch (JSONException e) {
            Toast toast1 =
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
            toast1.show();
        }


        ArticulosVenta = new ArrayList<>();
        carouselView = (CarouselView) v.findViewById(R.id.carouselView);
        btn_agregar_cliente = v.findViewById(R.id.btn_agregar_cliente);
        btn_agregar_vendedor = v.findViewById(R.id.btn_agregar_vendedor);
        btn_agregar_articulo = v.findViewById(R.id.btn_agregar_articulo);
        //btn_feenicia = v.findViewById(R.id.btn_feenicia);
        btn_apartar = v.findViewById(R.id.btn_apartar);
        btn_ordenar = v.findViewById(R.id.btn_orden);
        btn_finalizar = v.findViewById(R.id.btn_finalizar);
        btn_reporte = v.findViewById(R.id.btn_reporte);
        Corte_Caja = v.findViewById(R.id.CorteCaja);
        Comisiones= v.findViewById(R.id.Comisiones);
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

        CFDI_Name = new ArrayList<>();
        CFDI_ID = new ArrayList<>();

        ArticulosName = new ArrayList<>();
        Imagenes = new ArrayList<>();
        ListaDePagosDisponibles = new ArrayList<>();
        ListaDePagos_a_utilizar = new ArrayList<>();
        ImpuestosDeArticuloApartado = new ArrayList<>();
        ImpuestosDeArticuloOrdenado = new ArrayList<>();
        ListaDeArticulosApartados = new ArrayList<>();
        ListaDeArticulosOrdenados = new ArrayList<>();
        ArticulosApartados = new ArrayList<>();
        ArticulosOrdenados = new ArrayList<>();

        Meses = new ArrayList<>();

        tabla_venta_articulos = v.findViewById(R.id.tabla_venta_articulos);
        tabla_venta_articulos.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        InstanciarModeloTicket();
        LoadSucursales();
        LoadAutocomplete();
        LoadButtons();


        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                InstanciarModeloTicket();
                ArticulosVenta.clear();
                Imagenes.clear();
                ListaDeArticulosApartados.clear();
                ListaDeArticulosOrdenados.clear();
                final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                        total, descuento, impuesto, subtotal,
                        carouselView, Imagenes, ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,btn_ordenar,btn_apartar,btn_finalizar);
                articuloAdapter.notifyDataSetChanged();
                tabla_venta_articulos.setDataAdapter(articuloAdapter);
                LoadImages();


            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


      // usbCtrl = new UsbController(this,mHandler);
        //promociones_credito();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        usbCtrl.close();
        cancelAll();
    }

    @SuppressLint("HandlerLeak") private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:

                    btn_imprimir.setEnabled(true);

                    break;
                default:
                    break;
            }
        }
    };


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
                        progressDialog.dismiss();

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
                        progressDialog.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();


                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();

                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);

                    toast1.show();


                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);

                        toast1.show();


                    }
                }
        );
        postRequest.setShouldCache(false);
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

        postRequest.setShouldCache(false);

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }

    public void LoadImages() {

        if(Imagenes.isEmpty())
        {
            carouselView.setVisibility(View.INVISIBLE);
        }else{
            carouselView.setVisibility(View.VISIBLE);
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



    }

    public void LoadButtons() {

        btn_agregar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pop_up_ventas_seleccionar_cliente);
                dialog.show();
                Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });

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
                        ticket_de_venta.setTic_id_cliente(clickedData.getCliente_UUID());
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
                Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.hide();
                    }
                });
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
                fr.replace(R.id.fragment_container, new Fragment_ventas_corte_caja_listado()).commit();
            }
        });

        Comisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.replace(R.id.fragment_container, new Fragment_pestania_comison()).commit();
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
                Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
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
                ticketid=ticket_de_venta.getTic_id();

                if (ArticulosVenta.isEmpty()) {
                    dialog.setContentView(R.layout.pop_up_venta_finalizar_sin_articulos);
                    dialog.show();
                    Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                    cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });
                    Button aceptar = dialog.findViewById(R.id.Aceptar);
                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else if (false == ArticulosVenta.isEmpty()) {

                    //gpromociones_credito

                    dialog.setContentView(R.layout.pop_up_ventas_facturar);
                    dialog.show();
                    Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                    cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });
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
                            dialog.setContentView(R.layout.pop_up_ventas_uso_cfdi);
                            dialog.show();
                            SpinnerCFDI = dialog.findViewById(R.id.combo_cfdi);
                            LoadCFDI();

                            Button Agregar_cfdi = dialog.findViewById(R.id.aceptar_agregar_cfdi);
                            Agregar_cfdi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //aqui
                                    AniadirCFDI();
                                    dialog.dismiss();
                                    dialog.setContentView(R.layout.pop_up_ventas_metodo_pago);
                                    dialog.show();
                                    tabla_metodos_pago = dialog.findViewById(R.id.tabla_seleccionar_metodo_pago);
                                    tabla_metodos_pago.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                                    Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                                    cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.hide();
                                        }
                                    });
                                    CargaMetodosPago();

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
                                            double PagosEfectivo = 0;


                                            for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {

                                                String tipo_pago = ListaDePagos_a_utilizar.get(i).getNombre();
                                                String cantPago = ListaDePagos_a_utilizar.get(i).getCantidad();

                                                if (tipo_pago.equals("Tarjeta de crédito"))
                                                {
                                                    TarjetaCredito = Double.parseDouble(cantPago);
                                                }

                                                if (tipo_pago.equals("Tarjeta débito"))
                                                {
                                                    TarjetaDebito = Double.parseDouble(cantPago);
                                                }

                                                if(tipo_pago.equals("Efectivo")){

                                                    PagosEfectivo +=Double.parseDouble(cantPago);

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

                                            double totalvalida = totalsumaimportes-PagosEfectivo;

                                            Toast toast2 =
                                                    Toast.makeText(getContext(),
                                                            totalvalida + " " + totalsumaimportes + " " + PagosEfectivo,
                                                            Toast.LENGTH_LONG);
                                            toast2.show();

                                            if (totalvalida > TotalFormat){
                                                Toast toast1 =
                                                        Toast.makeText(getContext(),
                                                                "la suma de los montos de medios electronicos o tarjetas no puede sobrepasar el total de la venta",
                                                                Toast.LENGTH_LONG);
                                                toast1.show();

                                                return;
                                            }


                                            if (TotalFormat > totalsumaimportes) {
                                                Toast toast1 =
                                                        Toast.makeText(getContext(), "El monto capturado es menor al total de la venta", Toast.LENGTH_LONG);
                                                toast1.show();

                                                return;
                                            }

                                            dialog.dismiss();

                                            double valorTarjetas = 0;

                                            valorTarjetas = TarjetaCredito + TarjetaDebito;

                                            if (valorTarjetas > 0)
                                            {

                                                Intent myIntent = new Intent(getActivity(), Feenicia_Transaction_Bluetooth.class);
                                                Bundle mBundle = new Bundle();
                                                mBundle.putDouble("TC",TarjetaCredito);
                                                mBundle.putDouble("TD",TarjetaDebito);
                                                mBundle.putString( "Ticket",TicketIDVenta );
                                                mBundle.putString("Sucursal",ticket_de_venta.getTic_id_sucursal());

                                                mBundle.putInt("Tamano",ListaDePagos_a_utilizar.size());

                                                for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {
                                                    mBundle.putInt("fpa_id"+i, Integer.parseInt( ListaDePagos_a_utilizar.get(i).getId()));
                                                    mBundle.putString("valor"+i,ListaDePagos_a_utilizar.get(i).getCantidad());
                                                }

                                                myIntent.putExtras(mBundle);


                                                getActivity().startActivity(myIntent);
                                            }
                                            else
                                            {


                                                dialog.setContentView(R.layout.pop_up_ventas_confirmacion_venta);
                                                dialog.show();

                                                Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                                                cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        loadTicket();
                                                        dialog.hide();
                                                    }
                                                });

                                                TextView importe_venta = dialog.findViewById(R.id.importe_venta);
                                                TextView importe_recibido = dialog.findViewById(R.id.importe_recibido);
                                                TextView importe_cambio = dialog.findViewById(R.id.importe_cambio);

                                                FinalizarTicket(importe_cambio, importe_recibido, importe_venta);

                                                Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                                                aceptar.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        loadTicket();
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }




                                        }
                                    });
//aqui
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
                            tabla_metodos_pago = dialog.findViewById(R.id.tabla_seleccionar_metodo_pago);
                            tabla_metodos_pago.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                            Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                            cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.hide();
                                }
                            });

          //=====================================================

                            CargaMetodosPago();

                            TextView TotalAPagar = dialog.findViewById(R.id.total_a_pagar);
                            double ImporteTotalConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_total());
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            TotalAPagar.setText(formatter.format(ImporteTotalConDecimal));

                            Button realizarPago = dialog.findViewById(R.id.realizar_Pago);
                            realizarPago.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                 //Aqui se realizan las validaciones cuando no lleva factura

                                    double totalsumaimportes = 0;
                                    double TarjetaCredito = 0;
                                    double TarjetaDebito = 0;
                                    double PagosEfectivo = 0;


                                    for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {

                                        String tipo_pago = ListaDePagos_a_utilizar.get(i).getNombre();
                                        String cantPago = ListaDePagos_a_utilizar.get(i).getCantidad();

                                        if (tipo_pago.equals("Tarjeta de crédito"))
                                        {
                                            TarjetaCredito = Double.parseDouble(cantPago);
                                        }

                                        if (tipo_pago.equals("Tarjeta débito"))
                                        {
                                            TarjetaDebito = Double.parseDouble(cantPago);
                                        }

                                        if(tipo_pago.equals("Efectivo"))
                                        {
                                            PagosEfectivo +=Double.parseDouble(cantPago);
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


                                    double totalvalida = totalsumaimportes-PagosEfectivo;


                                    if (totalvalida > TotalFormat){
                                        Toast toast1 =
                                                Toast.makeText(getContext(),
                                                        "la suma de los montos de medios electronicos o tarjetas no puede sobrepasar el total de la venta",
                                                        Toast.LENGTH_LONG);
                                        toast1.show();

                                        return;
                                    }


                                    if (TotalFormat > totalsumaimportes) {
                                        Toast toast1 =
                                                Toast.makeText(getContext(), "El monto capturado es menor al total de la venta", Toast.LENGTH_LONG);
                                        toast1.show();

                                        return;
                                    }

                                    dialog.dismiss();

                                    double valorTarjetas = 0;

                                    valorTarjetas = TarjetaCredito + TarjetaDebito;

                                    if (valorTarjetas > 0) {

                                        //promociones_credito();
                                        //Parte Bundle
                                        Intent myIntent = new Intent(getActivity(), Feenicia_Transaction_Bluetooth.class);
                                        Bundle mBundle = new Bundle();
                                        mBundle.putDouble("TC",TarjetaCredito);
                                        mBundle.putDouble("TD",TarjetaDebito);
                                        mBundle.putString( "Ticket",TicketIDVenta );
                                        mBundle.putString("Sucursal",ticket_de_venta.getTic_id_sucursal());
                                        mBundle.putString( "03meses",Resptresmeses);
                                        mBundle.putString( "06meses",Respseismeses);
                                        mBundle.putString( "09meses",Respnuevemeses);
                                        mBundle.putString( "12meses",Respdocemeses);

                                        mBundle.putInt("Tamano",ListaDePagos_a_utilizar.size());

                                        for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {
                                            mBundle.putInt("fpa_id"+i, Integer.parseInt( ListaDePagos_a_utilizar.get(i).getId()));
                                            mBundle.putString("valor"+i,ListaDePagos_a_utilizar.get(i).getCantidad());
                                        }

                                        myIntent.putExtras(mBundle);
                                        getActivity().startActivity(myIntent);


                                        //parte de pop_meses_credito
                                       /* dialog.setContentView( R.layout.pop_up_ventas_meses_acredito );
                                        dialog.show();
                                        progreso.show();
                                        tabla_selecciona_meses = dialog.findViewById( R.id.tabla_seleccionar_meses );
                                        tabla_selecciona_meses.setEmptyDataIndicatorView( dialog.findViewById( R.id.Tabla_vacia ) );

                                        textViewNombre = dialog.findViewById( R.id.textViewNombre );
                                        textViewNombre.setText( NomPromoCredito );

                                        Button cerrarPop = dialog.findViewById( R.id.btntachita );
                                        cerrarPop.setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.hide();
                                            }
                                        } );

                                        Button cancelar_mes = dialog.findViewById( R.id.cancelar_mes );
                                        cancelar_mes.setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.hide();
                                            }
                                        } );

                                        //Button btnacpetar = dialog.findViewById( R.id.aceptar_mes );
                                        double finalTarjetaCredito = TarjetaCredito;
                                        double finalTarjetaDebito = TarjetaDebito;
                                        btnacpetar.setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent myIntent = new Intent( getActivity(), Feenicia_Transaction_Bluetooth.class );
                                                Bundle mBundle = new Bundle();
                                                mBundle.putDouble( "TC", finalTarjetaCredito );
                                                mBundle.putDouble( "TD", finalTarjetaDebito );
                                                mBundle.putString( "Ticket", TicketIDVenta );
                                                mBundle.putString( "Sucursal", ticket_de_venta.getTic_id_sucursal() );

                                                mBundle.putInt( "Tamano", ListaDePagos_a_utilizar.size() );

                                                for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {
                                                    mBundle.putInt( "fpa_id" + i, Integer.parseInt( ListaDePagos_a_utilizar.get( i ).getId() ) );
                                                    mBundle.putString( "valor" + i, ListaDePagos_a_utilizar.get( i ).getCantidad() );
                                                }

                                                myIntent.putExtras( mBundle );

                                                getActivity().startActivity( myIntent );
                                            }
                                        } );*/
                                    }
                                    else
                                    {
                                                dialog.dismiss();
                                                dialog.setContentView(R.layout.pop_up_ventas_confirmacion_venta);
                                                dialog.show();

                                                Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                                                cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        loadTicket();
                                                        dialog.dismiss();

                                                    }
                                                });

                                                TextView importe_venta = dialog.findViewById(R.id.importe_venta);
                                                TextView importe_recibido = dialog.findViewById(R.id.importe_recibido);
                                                TextView importe_cambio = dialog.findViewById(R.id.importe_cambio);

                                                FinalizarTicket(importe_cambio, importe_recibido, importe_venta);

                                                Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                                                aceptar.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        loadTicket();
                                                        dialog.dismiss();

                                                    }
                                                });

                                    }

                                   /* TableDataClickListener<PromocionesModel> tablaListenermeses = new TableDataClickListener<PromocionesModel>() {
                                        @Override
                                        public void onDataClicked(int rowIndex, final PromocionesModel clickedData) {
                                            dialog.dismiss();
                                        }
                                    };
                                    tabla_selecciona_meses.addDataClickListener(tablaListenermeses);*/

                                }
                            });

                        }
                    });
                }
            }
        });

        btn_apartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ticket_de_venta.getTic_id_cliente().equals(""))
                {
                    dialog.setContentView(R.layout.pop_up_ventas_apartar_sin_cliente);
                    dialog.show();
                    Button aceptar = dialog.findViewById(R.id.Aceptar);
                    Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                    cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    dialog.setContentView(R.layout.pop_up_ventas_selecciona_apartados);
                    dialog.show();
                    ArticulosApartados.clear();
                    tabla_apartados_disponibles = dialog.findViewById(R.id.tabla_seleccionar_apartados);
                    tabla_apartados_disponibles.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                    final SeleccionaApartadoAdapter articuloAdapter = new SeleccionaApartadoAdapter(getContext(), ListaDeArticulosApartados, tabla_apartados_disponibles, ticket_de_venta, ArticulosApartados,
                            usu_id, SpinnerSucursal, SucursalID);
                    articuloAdapter.notifyDataSetChanged();
                    tabla_apartados_disponibles.setDataAdapter(articuloAdapter);

                    Button btn_apartar_articulos = dialog.findViewById(R.id.btn_apartar_articulos);
                    btn_apartar_articulos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoadConfiguracionApartado();
                            ApartarArticulosSeleccionados();
                        }
                    });
                    Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                    cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }

            }
        });

        btn_ordenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ticket_de_venta.getTic_id_cliente().equals(""))
                {
                    dialog.setContentView(R.layout.pop_up_ventas_apartar_sin_cliente);
                    dialog.show();
                    Button aceptar = dialog.findViewById(R.id.Aceptar);
                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                    cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });
                }
                else {
                    dialog.setContentView(R.layout.pop_up_ventas_selecciona_orden);
                    dialog.show();
                    Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                    cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });
                    ArticulosOrdenados.clear();
                    tabla_ordenes_disponibles = dialog.findViewById(R.id.tabla_seleccionar_ordenes);
                    tabla_ordenes_disponibles.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                    final SeleccionaOrdenEspecialAdapter articuloAdapter = new SeleccionaOrdenEspecialAdapter(getContext(), ListaDeArticulosOrdenados, tabla_ordenes_disponibles, ticket_de_venta, ArticulosOrdenados,
                            usu_id, SpinnerSucursal, SucursalID);
                    articuloAdapter.notifyDataSetChanged();
                    tabla_ordenes_disponibles.setDataAdapter(articuloAdapter);

                    Button btn_ordenar_articulos = dialog.findViewById(R.id.btn_ordenar_articulos);
                    btn_ordenar_articulos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoadConfiguracionApartado();
                            OrdenarArticulosSeleccionados();
                        }
                    });
                }

            }
        });

    }

    private void OrdenarArticulosSeleccionados() {

        float PagoTotal=0;
        float ImporteTotal=0;
        JSONArray arreglo = new JSONArray();
        try {

            for (int i = 0; i < ArticulosOrdenados.size(); i++) {
                JSONObject list1 = new JSONObject();
                list1.put("oea_cantidad", ArticulosOrdenados.get(i).getCantidad());
                list1.put("oea_id_articulo", ArticulosOrdenados.get(i).getArticulo_id());
                list1.put("oea_id_variante", ArticulosOrdenados.get(i).getArticulo_id_variante());
                list1.put("oea_id_modificador", ArticulosOrdenados.get(i).getArticulo_id_modificador());
                list1.put("oea_importe_total", ArticulosOrdenados.get(i).getImporte_total());

                float Pagado = Float.parseFloat((ArticulosOrdenados.get(i).getImporte_pagado()).replaceAll("[^0-9.]", ""));
                PagoTotal = PagoTotal + Pagado;
                float Total = Float.parseFloat((ArticulosOrdenados.get(i).getImporte_total()).replaceAll("[^0-9.]", ""));
                ImporteTotal = ImporteTotal + Total;
                float Restante = Total - Pagado;

                list1.put("oea_importe_pagado", Pagado);
                list1.put("oea_importe_restante", String.format("%.2f", Restante));
                list1.put("oea_nombre_articulo", ArticulosOrdenados.get(i).getNombre_articulo());
                list1.put("oea_aplica_para_devolucion", ArticulosOrdenados.get(i).getAplica_para_devolucion());
                list1.put("oea_importe_descuento", ArticulosOrdenados.get(i).getImporte_descuento());
                list1.put("oea_porcentaje_descuento", ArticulosOrdenados.get(i).getPorcentaje_descuento());
                list1.put("oea_precio_articulo", ArticulosOrdenados.get(i).getPrecio_articulo());
                JSONObject impuestos = new JSONObject();
                try{
                    for (int j=0; j<ArticulosOrdenados.get(i).getImpuestos().size();j++)
                    {
                        impuestos.put(ArticulosOrdenados.get(i).getImpuestos().get(j).getImpuestosID(), ArticulosOrdenados.get(i).getImpuestos().get(j).getValorImpuesto());
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                list1.put("oea_impuestos", impuestos);
                arreglo.put(list1);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        if(arreglo.length()==0)
        {
            Toast toast1 =
                    Toast.makeText(getContext(), "No haz seleccionado ningún artículo", Toast.LENGTH_LONG);
            toast1.show();
        }else {


            float RestanteTotal = ImporteTotal - PagoTotal;

            JSONObject request = new JSONObject();
            try {
                request.put("usu_id", usu_id);
                request.put("esApp", "1");
                request.put("oes_id_ticket", ticket_de_venta.getTic_id());
                request.put("oes_id_sucursal", ticket_de_venta.getTic_id_sucursal());
                request.put("oes_id_cliente", ticket_de_venta.getTic_id_cliente());
                request.put("oes_importe_pagado", PagoTotal);
                request.put("oes_importe_restante", String.format("%.2f", RestanteTotal));
                request.put("dias_vencimiento", DiasApartado);
                request.put("aArticulos", arreglo);


            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/ordenes_especiales/store_ordenespecial";

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    try {

                        int status = Integer.parseInt(response.getString("estatus"));
                        String Mensaje = response.getString("mensaje");

                        if (status == 1) {

                            dialog.dismiss();
                            dialog.setContentView(R.layout.pop_up_ventas_confirmacion_transaccion);
                            dialog.show();
                            Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                            cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.hide();
                                }
                            });
                            Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                            aceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

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
    }

    private void ApartarArticulosSeleccionados() {

        float PagoTotal=0;
        float ImporteTotal=0;
        JSONArray arreglo = new JSONArray();
        try {

            for (int i = 0; i < ArticulosApartados.size(); i++) {
                JSONObject list1 = new JSONObject();
                list1.put("aar_cantidad", ArticulosApartados.get(i).getCantidad());
                list1.put("aar_id_articulo", ArticulosApartados.get(i).getArticulo_id());
                list1.put("aar_id_variante", ArticulosApartados.get(i).getArticulo_id_variante());
                list1.put("aar_id_modificador", ArticulosApartados.get(i).getArticulo_id_modificador());
                list1.put("aar_importe_total", ArticulosApartados.get(i).getImporte_total());

                    float Pagado = Float.parseFloat((ArticulosApartados.get(i).getImporte_pagado()).replaceAll("[^0-9.]", ""));
                    PagoTotal = PagoTotal + Pagado;
                    float Total = Float.parseFloat((ArticulosApartados.get(i).getImporte_total()).replaceAll("[^0-9.]", ""));
                    ImporteTotal = ImporteTotal + Total;
                    float Restante = Total - Pagado;

                list1.put("aar_importe_pagado", Pagado);
                list1.put("aar_importe_restante", String.format("%.2f", Restante));
                list1.put("aar_nombre_articulo", ArticulosApartados.get(i).getNombre_articulo());
                list1.put("aar_id_existencias_origen", ArticulosApartados.get(i).getId_existencias_origen());
                list1.put("aar_aplica_para_devolucion", ArticulosApartados.get(i).getAplica_para_devolucion());
                list1.put("aar_importe_descuento", ArticulosApartados.get(i).getImporte_descuento());
                list1.put("aar_porcentaje_descuento", ArticulosApartados.get(i).getPorcentaje_descuento());
                list1.put("aar_precio_articulo", ArticulosApartados.get(i).getPrecio_articulo());
                JSONObject impuestos = new JSONObject();
                    try{
                        for (int j=0; j<ArticulosApartados.get(i).getImpuestos().size();j++)
                        {
                            impuestos.put(ArticulosApartados.get(i).getImpuestos().get(j).getImpuestosID(), ArticulosApartados.get(i).getImpuestos().get(j).getValorImpuesto());
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                list1.put("aar_impuestos", impuestos);
                    arreglo.put(list1);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        if(arreglo.length()==0)
        {
            Toast toast1 =
                    Toast.makeText(getContext(), "No haz seleccionado ningún artículo", Toast.LENGTH_LONG);
            toast1.show();
        }else{

            float RestanteTotal = ImporteTotal - PagoTotal;

            JSONObject request = new JSONObject();
            try {
                request.put("usu_id", usu_id);
                request.put("esApp", "1");
                request.put("apa_id_ticket", ticket_de_venta.getTic_id());
                request.put("apa_id_sucursal", ticket_de_venta.getTic_id_sucursal());
                request.put("apa_id_cliente", ticket_de_venta.getTic_id_cliente());
                request.put("apa_importe_pagado", PagoTotal);
                request.put("apa_importe_restante", String.format("%.2f", RestanteTotal));
                request.put("dias_vencimiento", DiasApartado);
                request.put("aArticulos", arreglo);



            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/apartados/store_apartado";

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    try {

                        int status = Integer.parseInt(response.getString("estatus"));
                        String Mensaje = response.getString("mensaje");

                        if (status == 1) {

                            dialog.dismiss();
                            dialog.setContentView(R.layout.pop_up_ventas_confirmacion_transaccion);
                            dialog.show();
                            Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                            aceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                            cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.hide();
                                }
                            });


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
                                carouselView, Imagenes,ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,btn_ordenar,btn_apartar,btn_finalizar);
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

    private void Aniadir_a_venta(String nombre_articulo, String Cantidad) {

        ArticulosVenta.clear();
        ArticulosConExistencias.clear();
        ListaDeArticulosApartados.clear();
        ListaDeArticulosOrdenados.clear();
        ticket_de_venta.setTic_id_sucursal(SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal()); //suc_id
            request.put("articulo", nombre_articulo); //SKU
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

                            SKU_product = nodo.getString("art_sku");
                            float numero_de_productos = Float.parseFloat((nodo.getString("tar_cantidad")));

                            float PrecioSubTotalProducto = 0;


                            //Sumar Subtotal
                            float SubTot = Float.parseFloat(nodo.getString("art_precio_bruto"));
                            PrecioSubTotalProducto = PrecioSubTotalProducto + SubTot;
                            PrecioSubTotalProducto = PrecioSubTotalProducto * numero_de_productos;

                            Subtotal = Subtotal + PrecioSubTotalProducto;

                            String AplicaApartados = nodo.getString("art_aplica_apartados");
                            String TieneExistencia = nodo.getString("art_id_existencia");

                            //APARTADOS--------------------------------------
                            if (AplicaApartados.equals("false")||(TieneExistencia.equals(""))) {

                            }
                            else {
                                String ArticuloCantidad = nodo.getString("tar_cantidad");
                                String NombreArticuloApartado = nodo.getString("tar_nombre_articulo");
                                JSONObject NodoIDApartado = nodo.getJSONObject("tar_id_articulo");
                                String ArticuloIDApartado = NodoIDApartado.getString("uuid");
                                JSONObject NodoIDApartadoVariante = nodo.getJSONObject("tar_id_variante");
                                String ArticuloIDApartadoVariante = NodoIDApartadoVariante.getString("uuid");
                                String ArticuloIDApartadoModificador = nodo.getString("tar_id_modificador");
                                String ArticuloApartadoImportePagado = nodo.getString("tar_importe_pagado");
                                String ArticuloIDApartadoExistencia = nodo.getString("art_id_existencia");
                                String ArticuloApartadoAplicaDevolucion = nodo.getString("tar_aplica_para_devolucion");
                                String ArticuloApartadoImporteDescuento = nodo.getString("tar_importe_descuento");
                                String ArticuloApartadoTotal = nodo.getString("tar_importe_total");
                                JSONObject nodoImpuestos = nodo.getJSONObject("tar_impuestos");
                                Iterator keys = nodoImpuestos.keys();
                                while (keys.hasNext()) {
                                    Object key = keys.next();
                                    String value = nodoImpuestos.getString((String) key);
                                    Impuestos Impuesto = new Impuestos((String) key, value);
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

                            //ORDENES-------------------------------------------------------
                            String AplicaOrdenes = nodo.getString("art_aplica_ordenes_especiales");
                            if (AplicaOrdenes.equals("true")&&(TieneExistencia.equals(""))) {
                                String ArticuloCantidad = nodo.getString("tar_cantidad");
                                String NombreArticuloApartado = nodo.getString("tar_nombre_articulo");
                                JSONObject NodoIDApartado = nodo.getJSONObject("tar_id_articulo");
                                String ArticuloIDApartado = NodoIDApartado.getString("uuid");
                                JSONObject NodoIDApartadoVariante = nodo.getJSONObject("tar_id_variante");
                                String ArticuloIDApartadoVariante = NodoIDApartadoVariante.getString("uuid");
                                String ArticuloIDApartadoModificador = nodo.getString("tar_id_modificador");
                                String ArticuloApartadoImportePagado = nodo.getString("tar_importe_pagado");
                                String ArticuloIDApartadoExistencia = nodo.getString("art_id_existencia");
                                String ArticuloApartadoAplicaDevolucion = nodo.getString("tar_aplica_para_devolucion");
                                String ArticuloApartadoImporteDescuento = nodo.getString("tar_importe_descuento");
                                String ArticuloApartadoTotal = nodo.getString("tar_importe_total");
                                JSONObject nodoImpuestos = nodo.getJSONObject("tar_impuestos");
                                Iterator keys = nodoImpuestos.keys();
                                while (keys.hasNext()) {
                                    Object key = keys.next();
                                    String value = nodoImpuestos.getString((String) key);
                                    Impuestos Impuesto = new Impuestos((String) key, value);
                                    ImpuestosDeArticuloOrdenado.add(Impuesto);
                                }
                                String ArticuloApartadoPorcentajeDescuento = nodo.getString("tar_porcentaje_descuento");
                                String ArticuloApartadoPrecio = nodo.getString("tar_precio_articulo");


                                float importePagado = Float.parseFloat((ArticuloApartadoImportePagado));
                                float importeTotal = Float.parseFloat((ArticuloApartadoTotal));
                                float importeRestante = importeTotal - importePagado;

                                OrdenEspecialArticuloModel ArticuloOrdenado = new OrdenEspecialArticuloModel(
                                        ArticuloCantidad,
                                        ArticuloIDApartado,
                                        ArticuloIDApartadoVariante,
                                        ArticuloIDApartadoModificador,
                                        ArticuloApartadoImportePagado,
                                        String.valueOf(importeRestante),
                                        NombreArticuloApartado,
                                        ArticuloApartadoAplicaDevolucion,
                                        ArticuloApartadoImporteDescuento,
                                        ArticuloApartadoTotal,
                                        ImpuestosDeArticuloOrdenado,
                                        ArticuloApartadoPorcentajeDescuento,
                                        ArticuloApartadoPrecio);
                                ListaDeArticulosOrdenados.add(ArticuloOrdenado);
                            }
                            else {

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
                        for (int x = 0; x < NodoTicketArticulos.length(); x++)
                        {
                            int Checador=0;
                            JSONObject elemento = NodoTicketArticulos.getJSONObject(x);
                            String NombreArticulo = elemento.getString("tar_nombre_articulo");
                            JSONObject NodoTarID = elemento.getJSONObject("tar_id");
                            String tar_id = NodoTarID.getString("uuid");

                            String SKUArticulo = elemento.getString("art_sku");
                            String cantidad = elemento.getString("tar_cantidad");
                            String precio = elemento.getString("tar_precio_articulo");
                            String descuento = elemento.getString("art_importe_descuento");
                            String importe = elemento.getString("tar_importe_total");
                            String existencia = elemento.getString("art_id_existencia");

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
                                    importe,"","",existencia
                            );
                            ArticulosVenta.add(articulo);

                        }

                        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                                total, descuento, impuesto, subtotal,
                                carouselView, Imagenes, ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,btn_ordenar,btn_apartar,btn_finalizar);
                        articuloAdapter.notifyDataSetChanged();
                        tabla_venta_articulos.setDataAdapter(articuloAdapter);
                        LoadImages();

                        if(ListaDeArticulosOrdenados.isEmpty())
                        {
                            btn_ordenar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            btn_ordenar.setVisibility(View.VISIBLE);
                        }

                        if(ListaDeArticulosApartados.isEmpty())
                        {
                            btn_apartar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            btn_apartar.setVisibility(View.VISIBLE);
                        }

                        for(int k=0; k<ArticulosVenta.size();k++)
                        {
                            if(ArticulosVenta.get(k).getArticulo_articulo_exi_id().equals("")){

                            }
                            else
                            {
                                ArticulosConExistencias.add(ArticulosVenta.get(k).getarticulo_Nombre());
                            }
                        }

                        if(ArticulosConExistencias.isEmpty())
                        {
                            btn_finalizar.setVisibility(View.VISIBLE); //Invisible
                        }
                        else
                        {
                            btn_finalizar.setVisibility(View.VISIBLE);
                        }

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
                JSONObject ElementoCliente = null;
                JSONArray RespuestaNodoClientes = null;

                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {

                        Respuesta = response.getJSONObject("resultado");

                        RespuestaNodoClientes = Respuesta.getJSONArray("aClientes");

                        for (int x = 0; x < RespuestaNodoClientes.length(); x++) {
                            JSONObject elemento = RespuestaNodoClientes.getJSONObject(x);

                            ElementoCliente = elemento.getJSONObject("cli_id");

                            UUID = ElementoCliente.getString("uuid");
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

                            List<CompraModel> HistorialCompras = new ArrayList<>();
                            JSONArray comprasNodo = elemento.getJSONArray("ventas");
                            for(int d=0; d<comprasNodo.length();d++)
                            {
                                JSONObject elementoCompra = comprasNodo.getJSONObject(d);
                                String NoTicket = elementoCompra.getString("tic_numero");
                                String Importe = elementoCompra.getString("tic_importe_total");
                                String Fecha = elementoCompra.getString("fecha_hora_venta");
                                CompraModel compra = new CompraModel(NoTicket,Importe,Fecha);
                                HistorialCompras.add(compra);
                            }



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
                                    direccion_igual,
                                    HistorialCompras
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
        postRequest.setShouldCache(false);
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
//===========================================================================================================
    private void CargaMetodosPago() {

        ListaDePagosDisponibles.clear();
        ListaDePagos_a_utilizar.clear();

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
                        MetodoPagoAdapter metodo = new MetodoPagoAdapter(getContext(), ListaDePagosDisponibles,tabla_metodos_pago,
                                ListaDePagos_a_utilizar);
                        tabla_metodos_pago.setDataAdapter(metodo);
                        metodo.notifyDataSetChanged();

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
//===================================================================================================================

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
        postRequest.setShouldCache(false);
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
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }

    private void AniadirCFDI() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id", ticket_de_venta.getTic_id());
            request.put("tic_id_uso_cfdi", CFDI_ID.get(SpinnerCFDI.getSelectedItemPosition()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/update_uso_cfdi";

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
        postRequest.setShouldCache(false);
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
        art_cantidad.setNumber("1");
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


                                    Button Aniadir = dialog.findViewById(R.id.Guardar_cantidad);
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

    private void LoadConfiguracionApartado()
    {
        try {
            String ApiPath = "http://187.189.192.150:8010/api/ventas/apartados/index?usu_id=" + usu_id + "&esApp=1&suc_id="+SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject resultado = response.getJSONObject("resultado");
                                    JSONObject configuraciones = resultado.getJSONObject("aConfiguracionCuenta");
                                    DiasApartado = configuraciones.getString("con_dias_apartado_vence");
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

    private void LoadCFDI()
    {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/select-uso-cdfi";

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
                            String CFDI = jsonObject1.getString("ucf_descripcion");
                            CFDI_Name.add(CFDI);
                            String CFDI_id = jsonObject1.getString("ucf_id");
                            CFDI_ID.add(CFDI_id);
                        }
                        SpinnerCFDI.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, CFDI_Name));

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


//------------------------------------------

    public void promociones_credito(){

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/articulos/promociones/consultar_promociones_articulo_sku_app";

        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("sku",SKU_product);
            request.put("suc_id", valueIdSuc);

        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest posRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject Resultado = null;
                JSONObject Promociones = null;
                JSONObject Credito = null;
                JsonArray Volumen = null;
                JSONObject Paquetes = null;
                JsonArray Bonificacion = null;


                try {
                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        Resultado = response.getJSONObject("resultado");
                        Promociones = Resultado.getJSONObject("promociones");
                        Iterator y = Promociones.keys();

                        while (y.hasNext()) {
                            String key = (String) y.next();
                            value = String.valueOf(Promociones.get(key));

                            int Respcredito = key.compareTo("credito");
                            if (Respcredito == 0){
                                Resp = value.compareTo("[]");
                                if (Resp == 0){

                                    Toast toast1 = Toast.makeText(getContext(), "No existen promociones", Toast.LENGTH_LONG);
                                    toast1.show();

                                    Respuestamenor = "No aplica meses sin interes a Crédito";

                                }else {

                                    Credito = Promociones.getJSONObject("credito");

                                    JSONObject ResultadoCredito = Credito.getJSONObject("1");
                                    Iterator x = ResultadoCredito.keys();
                                    while (x.hasNext()) {
                                        String key2 = (String) x.next();
                                        NomPromoCredito = ResultadoCredito.getString( "pro_nombre" );

                                        Boolean tresmeses = ResultadoCredito.getBoolean( "apr_tres_meses" );
                                        if (tresmeses == true) {
                                            Resptresmeses = "Aplica 3 meses sin intereses";
                                        }else {
                                            Resptresmeses = "No Aplica meses sin intereses";
                                        }

                                        Boolean seismeses = ResultadoCredito.getBoolean( "apr_seis_meses" );
                                        if (seismeses == true) {
                                            Respseismeses = "Aplica 6 meses sin intereses";
                                        }else {
                                            Respseismeses = "No Aplica meses sin intereses";
                                        }

                                        Boolean nuevemeses = ResultadoCredito.getBoolean( "apr_nueve_meses" );
                                        if ( nuevemeses == true){
                                            Respnuevemeses = "Aplica 9 meses sin intereses";
                                        }else {
                                            Respnuevemeses = "No Aplica meses sin intereses";
                                        }

                                        Boolean docemeses = ResultadoCredito.getBoolean( "apr_doce_meses" );
                                        if (docemeses == true){
                                            Respdocemeses = "Aplica 12 meses sin intereses";
                                        }else {
                                            Respdocemeses = "No Aplica meses sin intereses";
                                        }

                                        //Respuesta menor
                                        int Resultadomenor1 = Resptresmeses.compareTo(Respseismeses); //menor a la segunda sera -entero
                                        if (Resultadomenor1 < 0) {
                                            Respuestamenor = "Aplica 3 meses sin intereses";
                                        }else {
                                            Respuestamenor = "Aplica 6 meses sin intereses";
                                        }


                                        int Resultadomenor2 = Resptresmeses.compareTo(Respnuevemeses);
                                        if (Resultadomenor2 < 0) {
                                            Respuestamenor = "Aplica 3 meses sin intereses";
                                        }else {
                                            Respuestamenor = "Aplica 9 meses sin intereses";
                                        }


                                        int Resultadomenor3 = Resptresmeses.compareTo(Respdocemeses);
                                        if (Resultadomenor3 < 0) {
                                            Respuestamenor = "Aplica 3 meses sin intereses";
                                        }else {
                                            Respuestamenor = "Aplica 12 meses sin intereses";
                                        }

                                        //Respuesta menor
                                       /* int Resulmenor1 = Respdocemeses.compareTo(Resptresmeses); //menor a la segunda sera -entero
                                        int Resulmenor2 = Respdocemeses.compareTo(Respseismeses);
                                        int Resulmenor3 = Respdocemeses.compareTo(Respnuevemeses);

                                        if (Resulmenor1 < 0) {
                                            Respuestamenor = "Aplica 12 meses sin intereses";
                                        }

                                        if (Resulmenor2 < 0) {
                                            Respuestamenor = "Aplica 12 meses sin intereses";
                                        }

                                        if (Resulmenor3 < 0) {
                                            Respuestamenor = "Aplica 12 meses sin intereses";
                                        }*/

                                    }
                            }
                            }
                        }

                        final PromocionesModel tickets = new PromocionesModel(
                                Respuestamenor,"","","");
                        Meses.add(tickets);

                        final SeleccionarMesesCreditoAdapter MesesCreditoAdapter = new SeleccionarMesesCreditoAdapter(getContext(), Meses, tabla_selecciona_meses);
                        tabla_selecciona_meses.setDataAdapter(MesesCreditoAdapter);

                    }else {
                       // Toast toast1 = Toast.makeText(getContext(), "No existen tickets en el periodo seleccionado.", Toast.LENGTH_LONG);
                       // toast1.show();
                    }
                } catch (JSONException e) {
                    Toast toast1 = Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 = Toast.makeText(getContext(), "Error de conexion", Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );

        posRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley( getContext() ).addToRequestQueue( posRequest );
    }
    //______________________________________________

    private void loadTicket()
    {
        try {

            String url = getString(R.string.Url);

            String ApiPath;

            ApiPath = url + "/api/ventas/movimientos/obtener_detalle_ticket?" +
                    "usu_id=" + usu_id +
                    "&esApp=1" +
                    "&tic_id="+ ticketid+
                    "&suc_id=" + SucursalID.get(SpinnerSucursal.getSelectedItemPosition());
            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {

                                    JSONObject RespuestaResultado = response.getJSONObject("resultado");

                                    JSONObject NodoASucursal = RespuestaResultado.getJSONObject("aSucursal");
                                    NombreSucursal = NodoASucursal.getString("suc_nombre");
                                    NumeroSucursal = NodoASucursal.getString("suc_numero");
                                    Direccion = NodoASucursal.getString("suc_direccion");
                                    RazonSocial = NodoASucursal.getString("suc_razon_social");
                                    RFC = NodoASucursal.getString("suc_rfc");
                                    LogoNegocio = NodoASucursal.getString("con_logo_negocio");

                                    JSONObject NodoTicket = RespuestaResultado.getJSONObject("aTicket");
                                    FechaCreacion = NodoTicket.getString("tic_fecha_hora_creo");
                                    NombreVendedor = NodoTicket.getString("tic_nombre_vendedor");
                                    NombreCliente = NodoTicket.getString("tic_nombre_cliente");
                                    Subtotal = NodoTicket.getString("tic_importe_subtotal");
                                    Total = NodoTicket.getString("tic_importe_total");
                                    JSONArray ArregloArticulos = NodoTicket.getJSONArray("aArticulos");
                                    JSONArray NodoImpuestos = RespuestaResultado.getJSONArray("aTicketImpuestos");

                                    ImpuestosTotal=0;
                                    for(int i=0;i<NodoImpuestos.length();i++)
                                    {
                                        JSONObject elemento = NodoImpuestos.getJSONObject(i);
                                        ImpuestosTotal= ImpuestosTotal+Float.parseFloat(elemento.getString("importe_impuesto"));
                                    }

                                    ListaArticulosTicket.clear();
                                    for(int i=0;i<ArregloArticulos.length();i++)
                                    {
                                        JSONObject elemento = ArregloArticulos.getJSONObject(i);
                                        String Cantidad = elemento.getString("tar_cantidad");
                                        String Nombre = elemento.getString("tar_nombre_articulo");
                                        String Precio = elemento.getString("tar_precio_articulo");
                                        String Importe = elemento.getString("tar_importe_total");

                                        ArticuloModel articulo =
                                                new ArticuloModel("",
                                                        Nombre,
                                                        "",
                                                        Precio,
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        Cantidad,
                                                        "",
                                                        "",
                                                        "",
                                                        Importe,
                                                        "",
                                                        "",
                                                        "");

                                        ListaArticulosTicket.add(articulo);
                                    }



                                    JSONObject NodoCuentasBioNet = RespuestaResultado.getJSONObject("aCuentasBioNet");
                                    NumeroTicket = NodoCuentasBioNet.getString("cbn_numero_ticket");

                                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                    String cadenaArticulos="";
                                    for(int j=0; j<ListaArticulosTicket.size();j++)
                                    {
                                        double PrecioArticulo = Double.parseDouble(ListaArticulosTicket.get(j).getarticulo_Precio());
                                        double ImporteArticulo = Double.parseDouble(ListaArticulosTicket.get(j).getArticulo_importe());


                                        cadenaArticulos=cadenaArticulos+
                                                ("<tr> \n"+
                                                        "<td>"+ListaArticulosTicket.get(j).getArticulo_cantidad()+"</td>\n"+
                                                        "<td>"+ListaArticulosTicket.get(j).getarticulo_Nombre()+"</td>\n"+
                                                        "<td>"+formatter.format( PrecioArticulo )+"</td>\n"+
                                                        "<td>"+formatter.format( ImporteArticulo )+"</td>\n"+
                                                        "</tr>");
                                    }

                                    content= "<!DOCTYPE html>\n" +
                                            "<html dir=\"ltr\" lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"utf-8\">\n" +
                                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                                            "<style type=\"text/css\" media=\"all\">\n" +
                                            "#tblTicketTemplate {\n" +
                                            "  width: 100%;\n" +
                                            "  font-size: 10px;\n" +
                                            "  font-family: \"Courier New\";\n" +
                                            "  text-transform: uppercase;\n" +
                                            "  line-height: 1.3;\n" +
                                            "  border: solid 1px #D4DADF;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate thead tr td {\n" +
                                            "  padding-left: 10%;\n" +
                                            "  padding-right: 10%;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate #tdLogo {\n" +
                                            "  text-align: center;\n" +
                                            "  vertical-align: middle;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate #tdFiscal{\n" +
                                            "  text-align: center;\n" +
                                            "  vertical-align: middle;\n" +
                                            "  font-weight: bold;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate .trDivider{\n" +
                                            "  border-bottom: dashed 1px black;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate .trDivider td {\n" +
                                            "  padding-bottom: 3px;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate .trDivider + tr td {\n" +
                                            "  padding-top: 3px;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trInfo{\n" +
                                            "  font-size: 10px;\n" +
                                            "  vertical-align: top;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trInfo td a{\n" +
                                            "  text-align: center;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody #trTituloDetalle{\n" +
                                            "  font-weight: bold;\n" +
                                            "  text-align: center;\n" +
                                            "  border-top: dashed 1px black;\n" +
                                            "  border-bottom: dashed 1px black;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trArticulo{\n" +
                                            "  font-size: 10px;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate .importe{\n" +
                                            "  text-align: right;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trArticulo + tr td{\n" +
                                            "  font-size: 9px;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tbody .trTotal{\n" +
                                            "  font-size: 14px;\n" +
                                            "  font-weight: bold;\n" +
                                            "}\n" +
                                            "#tblTicketTemplate tfoot{\n" +
                                            "  text-align: center;\n" +
                                            "}\n" +
                                            "</style>\n" +
                                            "</head>\n" +
                                            "<body>\n"+
                                            "<table id=\"tblTicketTemplate\">\n" +
                                            "  <thead>\n" +
                                            "    <tr>\n" +
                                            "      <td id=\"tdLogo\" colspan=\"4\">\n" +
                                            "        <img src="+getString(R.string.Url)+LogoNegocio+" class=\"img_con_logo_ticket\" width=\"100%\" height=\"75\" />\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr>\n" +
                                            "      <td id=\"tdFiscal\" colspan=\"4\">\n" +
                                            "        "+RazonSocial+"<br />"+RFC+"\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trDivider\">\n" +
                                            "      <td id=\"tdFiscal\" colspan=\"4\">\n" +
                                            "        <!--Funciones::formatDireccion($aDatos['aSucursalPrincipal']['suc_direccion'], \"suc_\")-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr>\n" +
                                            "      <td id=\"tdFiscal2\" colspan=\"4\">\n" +
                                            "        <!--Funciones::formatDireccion($aDatos['aSucursal']['suc_direccion'], \"suc_\")-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trDivider\">\n" +
                                            "      <td id=\"tdFiscalTelefono\" colspan=\"4\">\n" +
                                            "        <!--Tel.:  $aDatos['aSucursal']['suc_telefono'] -->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "  </thead>\n" +
                                            "  <tbody>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td id=\"tdFechaHora\" colspan=\"2\">\n" +
                                            "        <b>Fec./Hr.:</b> "+FechaCreacion+"\n" +
                                            "      </td>\n" +
                                            "      <td id=\"tdTicketNum\" colspan=\"2\">\n" +
                                            "        <b>Ticket:</b>"+NumeroTicket+"\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td id=\"tdVendedor\" colspan=\"4\">\n" +
                                            "        <b>Vendedor:</b>"+NombreVendedor+"\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td id=\"tdCliente\" colspan=\"4\">\n" +
                                            "        <b>Cliente:</b>"+NombreCliente+"\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr id=\"trTituloDetalle\">\n" +
                                            "      <td>C.</td>\n" +
                                            "      <td>Articulo</td>\n" +
                                            "      <td>P.U.</td>\n" +
                                            "      <td>Importe</td>\n" +
                                            "    </tr>\n" +
                                            "    <tr id=\"tableListArticulos\">\n" +
                                            "      "+ cadenaArticulos +
                                            "      <tr>\n" +
                                            "        <td></td>\n" +
                                            "        <!--td colspan=\"2\">Descripción del articulo</td-->\n" +
                                            "        <td></td>\n" +
                                            "      </tr-->\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trDivider\">\n" +
                                            "      <td colspan=\"4\"></td>\n" +
                                            "    </tr>\n" +
                                            "\n" +
                                            "    <tr class=\"trTotales\">\n" +
                                            "      <td colspan=\"2\">Subtotal:</td>\n" +
                                            "      <td class=\"importe\">$"+Subtotal+"</td>\n" +
                                            "      <td id=\"tdSubTotal\" class=\"importe\"></td>\n" +
                                            "    </tr>\n" +
                                            "    <tr id=\"trImpuestosTotales\" class=\"trTotales\">\n" +
                                            "      <td colspan=\"2\">Impuestos:</td>\n" +
                                            "      <td class=\"importe\">"+formatter.format( ImpuestosTotal)+"</td>\n" +
                                            "      <!--td class=\"importe\">123</td-->\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trTotal\">\n" +
                                            "      <td colspan=\"2\">Total:</td>\n" +
                                            "      <td class=\"importe\">$"+Total+"</td>\n" +
                                            "      <td id=\"tdTotal\" class=\"importe\"></td>\n" +
                                            "    </tr>\n" +
                                            "\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td id=\"tdQR\" colspan=\"4\">\n" +
                                            "        <!--if(trim(@$aDatos['aSucursal']['con_url_encuesta']) != \"\")\n" +
                                            "          <a href=\" trim(@$aDatos['aSucursal']['con_url_encuesta']) \" target=\"_blank\">\n" +
                                            "            !! QrCode::size(200)->generate(trim($aDatos['aSucursal']['con_url_encuesta'])); !!\n" +
                                            "          </a>\n" +
                                            "          <br />\n" +
                                            "          <a href=\" trim(@$aDatos['aSucursal']['con_url_encuesta']) \" target=\"_blank\">Nos Interesa su opinión</a>\n" +
                                            "        endif-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        <!--b>Tarjeta de Credito:</b> 0099-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        <!--b>Folio de facturación:</b><br />0001-001-09-101010-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <tr class=\"trInfo trDivider\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        <!--Usted cuenta con 132 puntos del programa de lealtad-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <!--if (trim($aDatos['aSucursal']['con_texto_politica_devolucion_ticket_digital']) != \"\")\n" +
                                            "      <tr class=\"trInfo trDivider\">\n" +
                                            "        <td colspan=\"4\">\n" +
                                            "          <small><b>*<span id=\"text_con_texto_politica_devolucion_ticket_digital\"> $aDatos['aSucursal']['con_texto_politica_devolucion_ticket_digital'] </span></b></small>\n" +
                                            "        </td>\n" +
                                            "      </tr>\n" +
                                            "    endif-->\n" +
                                            "\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        <small><span id=\"text_con_texto_personalidado_ticket_digital\" class=\"h5\"><!-- $aDatos['aSucursal']['con_texto_personalidado_ticket_digital'] --></span></small>\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "\n" +
                                            "  </tbody>\n" +
                                            "  <tfoot>\n" +
                                            "    <tr class=\"trInfo\">\n" +
                                            "      <td colspan=\"4\">\n" +
                                            "        Este ticket fue creado desde bio-Net Punto de Venta, el mejor sistema para tu negocio, para más información visita <a href=\"bionetpos.com\" target=\"_blank\">bionetpos.com</a>\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "  </tfoot>\n" +
                                            "</table>\n"+
                                            "</body>\n"+
                                            "</html>";
                                    SendDataString(content);
                                    Dialog dialog = new Dialog(getContext());
                                    dialog.setContentView(R.layout.pop_up_ticket_web);
                                    webView = (WebView) dialog.findViewById(R.id.simpleWebView);
                                    btn_imprimir = (Button)dialog.findViewById(R.id.btn_imprimir);

                                    btn_imprimir.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //
                                        }
                                    });

                                    // displaying text in WebView
                                    webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                                    dialog.show();

                                    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTicket/");
                                    final String fileName="Ticket.pdf";

                                    final ProgressDialog progressDialog=new ProgressDialog(getContext());
                                    progressDialog.setMessage("Espere un momento por favor");
                                    progressDialog.show();
                                    PdfView.createWebPrintJob(getActivity(), webView, directory, fileName, new PdfView.Callback() {

                                        @Override
                                        public void success(String path) {
                                            progressDialog.dismiss();
                                            PdfView.openPdfFile(getActivity(),getString(R.string.app_name),"¿Desea abrir el archivo pdf?"+fileName,path);
                                        }

                                        @Override
                                        public void failure() {
                                            progressDialog.dismiss();

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

   private void SendDataString(String data){
       /*  if(data.length()>0)
            usbCtrl.sendMsg(data, "GBK", dev);  //*/
    }




}
