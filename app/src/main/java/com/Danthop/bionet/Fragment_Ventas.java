package com.Danthop.bionet;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Adapters.ClienteAdapter;
import com.Danthop.bionet.Adapters.ImpuestoAdapter;
import com.Danthop.bionet.Adapters.MetodoPagoAdapter;
import com.Danthop.bionet.Adapters.SeleccionaApartadoAdapter;
import com.Danthop.bionet.Adapters.SeleccionaOrdenEspecialAdapter;
import com.Danthop.bionet.Adapters.SeleccionarArticuloVentaAdapter;
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
import com.Danthop.bionet.model.Impuestos;
import com.Danthop.bionet.model.OrdenEspecialArticuloModel;
import com.Danthop.bionet.model.PagoModel;
import com.Danthop.bionet.model.PromocionesModel;
import com.Danthop.bionet.model.TicketModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.webviewtopdf.PdfView;
import com.zj.usbsdk.UsbController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import zj.com.customize.sdk.Other;

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
    private List<CompraModel> HistorialCompras;

    private ArrayList<String> CFDI_Name;
    private ArrayList<String> CFDI_ID;
    private ArrayList<String> ArticulosName;
    private ArrayList<String> Imagenes;

    private ArrayList<String> ClientesName;
    private ArrayList<String> ClientesID;

    private String[][] LPAU;
    private CarouselView carouselView;
    private String TicketIDVenta;
    private int TicketSubtotal;
    private String TicketImporteDescuento;
    private float TicketIVA;
    private String ticketid;
    private ArrayList<String> ArticulosConExistencias = new ArrayList<>();
    private SeleccionarArticuloVentaAdapter ArticuloAdapter;

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
    private float Subtotal = 0;
    private float Total = 0;
    private float ImpuestosTotal=0;
    private ProgressDialog progressDialog;
    private String contenidoImprimir;
    private String ImpuestosTicket="";

    private String NomPromoCredito;
    private String SKU_product;
    private int Resptresmeses = 0;
    private int Respseismeses = 0;
    private int Respnuevemeses = 0;
    private int Respdocemeses = 0;
    private String Respuestamenor="";
    private TextView textViewNombre;
    private String value;
    private int Resp;
    private Double TarjetaCredito = 0.0;
    private Double TarjetaDebito = 0.0;

    private Button btn_PagoServicios;

    private Bitmap QR;
    private String LinkEncuesta="";
    private String contenidoImprimir2;

    public final static int QRcodeWidth = 500 ;
    private Boolean ParcialidadesBoleano=false;

    private int[][] u_infor;
    static UsbController usbCtrl = null;
    static UsbDevice dev = null;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    //   Toast.makeText(PrintDemo.this.getApplicationContext(), PrintDemo.this.getString(2130968584), 0).show();
                    //    PrintDemo.this.btnSend.setEnabled(true);
                    //   PrintDemo.this.btn_test.setEnabled(true);
                    //    PrintDemo.this.btnClose.setEnabled(true);
                    //    PrintDemo.this.btn_printA.setEnabled(true);
                    //    PrintDemo.this.btn_BMP.setEnabled(true);
                    //   PrintDemo.this.btn_ChoseCommand.setEnabled(true);
                    //   PrintDemo.this.btn_prtcodeButton.setEnabled(true);
                    //   PrintDemo.this.btn_prtsma.setEnabled(true);
                    //   PrintDemo.this.btn_prttableButton.setEnabled(true);
                    //    PrintDemo.this.Simplified.setEnabled(true);
                    //   PrintDemo.this.Korean.setEnabled(true);
                    //    PrintDemo.this.big5.setEnabled(true);
                    //    PrintDemo.this.thai.setEnabled(true);
                    //    PrintDemo.this.btn_conn.setEnabled(false);
                default:
            }
        }
    };


    private RecyclerView RecyclerImpuesto;
    private RecyclerView.LayoutManager mLayoutManager;
    private NumberFormat formatter;

    private ImpuestoAdapter mAdapter;
    private List<Impuestos> ImpuestosList = new ArrayList<>();

    private Button btn_imprimir;
    private Button btn_eliminar_cliente_ticket;

    private Bundle bundle;

    private JSONArray Roles;

    private boolean Proceso_Venta = false;
    private boolean Transacciones = false;
    private boolean Conte_Caja  = false;
    private boolean Comision = false;

    private String code="";

    public Fragment_Ventas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ventas, container, false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        formatter =  NumberFormat.getCurrencyInstance();
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
        usu_id = sharedPref.getString("usu_id", "");
        cca_id_sucursal = sharedPref.getString("cca_id_sucursal", "");
        code = sharedPref.getString("sso_code","");

        try {
            Roles = new JSONArray(sharedPref.getString("sso_Roles", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean Aplica = false;
        boolean Aplica_Permiso = false;
        String fun_id = "";

        String rol_id = "";

        for (int i = 0; i < Roles.length(); i++) {
            try {

                JSONObject Elemento = Roles.getJSONObject(i);
                rol_id = Elemento.getString("rol_id");

                if (rol_id.equals("cbcf93c0-ed1e-11e8-8a6e-cb097f5c03df")) {
                    Aplica = Elemento.getBoolean("rol_aplica_en_version");
                    Aplica_Permiso = Elemento.getBoolean("rol_permiso");

                    if (Aplica == true) {
                        if (Aplica_Permiso == true) {

                            JSONArray Elemento1 = Elemento.getJSONArray("rol_funciones");

                            for (int x = 0; x < Elemento1.length(); x++) {

                                JSONObject Elemento2 = Elemento1.getJSONObject(x);

                                fun_id = Elemento2.getString("fun_id");

                                switch (fun_id) {

                                    case "7fba9366-82a5-4f87-9ccf-793e5a1148e0":
                                        Proceso_Venta = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "c945507f-9f0b-48ea-a8c3-88320097e8bc":
                                        Transacciones = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    case "3fc97fa7-0bc0-4001-a2a9-2b0b9f35c8f9":
                                        Comision = Elemento2.getBoolean("fun_permiso");
                                        break;
                                    case "0da9f0b0-ed68-4366-acd5-cc99ef1e35dd":
                                        Conte_Caja = Elemento2.getBoolean("fun_permiso");
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //                        "fun_id": "0da9f0b0-ed68-4366-acd5-cc99ef1e35dd",
        //                        "fun_nombre": "Corte de Caja",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "b009825e-4325-469a-8810-465ad7472180",
        //                        "fun_nombre": "Crear cliente",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "20566dc2-b25d-4bc7-95d7-55a67a4fc5a2",
        //                        "fun_nombre": "Descuento",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "ce21c249-e896-48d9-afa4-2665bd9806a4",
        //                        "fun_nombre": "Descuentos",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "6dc09951-0d20-46b0-832f-2ddf74090ac4",
        //                        "fun_nombre": "Entrega de apartado",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "5ac65bc8-5233-4c29-94ca-50b25e8eb6d2",
        //                        "fun_nombre": "Enviar por correo XML, PDF",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "a72ca9c6-3cd3-4524-896b-7aa95461cd27",
        //                        "fun_nombre": "Factura ventas mostrador",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "40a178d2-52a0-486b-b621-02ad99e22135",
        //                        "fun_nombre": "Facturación",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "7d20f4da-c310-469d-81e1-be0a265fc688",
        //                        "fun_nombre": "Formas de pago",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "041e4caa-ed4a-4a57-a35e-7182b88c2780",
        //                        "fun_nombre": "Generar corte de caja",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "b5daa736-83bb-41d9-b0b4-3b5f1d2d94cc",
        //                        "fun_nombre": "Impuestos",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "b7987839-a6e6-415b-a241-b6cddfad03cd",
        //                        "fun_nombre": "Listado de corte de caja",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "29a3e0fc-7136-48d6-82b8-30b22aa7f6b3",
        //                        "fun_nombre": "Listado de facturas",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "3ce3041a-41d2-4d8a-a1e4-07d37c22fa80",
        //                        "fun_nombre": "Movimientos",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "caa9baf3-e67e-4dc5-80b1-d64e7d86f199",
        //                        "fun_nombre": "Proceso de pago",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "7fba9366-82a5-4f87-9ccf-793e5a1148e0",
        //                        "fun_nombre": "Proceso de venta",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "e13f10e6-02b1-4089-a91f-4352ab5feff4",
        //                        "fun_nombre": "Promociones",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "cb2f09f9-c206-46c9-b3cd-618399d92b58",
        //                        "fun_nombre": "Propinas",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "e6d437bd-c43c-49d2-8d91-7e66a5badc6e",
        //                        "fun_nombre": "Reemplazo",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "f8d1f550-927e-4af1-a558-148755577c73",
        //                        "fun_nombre": "Regenerar factura",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "5a49aa8c-4f08-48ee-b95b-6812cd673ffa",
        //                        "fun_nombre": "Reporte de ventas",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "76e6bbac-cb73-4427-82dc-8a7c9a45869f",
        //                        "fun_nombre": "Resumen de ventas",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "e9971817-09f8-4130-aef8-895d9f3e4108",
        //                        "fun_nombre": "Tendencia en ventas",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "c945507f-9f0b-48ea-a8c3-88320097e8bc",
        //                        "fun_nombre": "Transacciones",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "24733c73-0132-4bce-8bdd-cf8f36c16714",
        //                        "fun_nombre": "Vendedores",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "e3da2d59-f19d-436f-9d3d-6809a9cf6806",
        //                        "fun_nombre": "Venta",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "a3fd2fe1-0102-4d1d-8f95-6b6fe171f881",
        //                        "fun_nombre": "Venta por articulos",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "785fe60e-0c9a-4aa6-a9ed-cf94a34eaa7d",
        //                        "fun_nombre": "Venta por categoría",
        //                        "fun_permiso": false
        //                    },
        //                    {
        //                        "fun_id": "bbdaadb5-c45b-4ab7-b5ad-44ff8510f141",
        //                        "fun_nombre": "Ventas por usuario",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "1d85e2ce-e647-4aec-9cb3-c23304bf9ad0",
        //                        "fun_nombre": "Visualizar PDF",
        //                        "fun_permiso": true
        //                    },
        //                    {
        //                        "fun_id": "a380e778-2fff-4e98-ad74-d117ca926fd4",
        //                        "fun_nombre": "Visualizar XML",
        //                        "fun_permiso": true
        //                    }




        fr = getFragmentManager().beginTransaction();
        dialog = new Dialog(getContext());

        usbCtrl = new UsbController(getActivity(),mHandler);
        usbCtrl = new UsbController(getActivity(),mHandler);
        this.u_infor = new int[8][2];
        this.u_infor[0][0] = 7358;
        this.u_infor[0][1] = 3;
        this.u_infor[1][0] = 7344;
        this.u_infor[1][1] = 3;
        this.u_infor[2][0] = 1155;
        this.u_infor[2][1] = 22336;
        this.u_infor[3][0] = 1171;
        this.u_infor[3][1] = 34656;
        this.u_infor[4][0] = 1046;
        this.u_infor[4][1] = 20497;
        this.u_infor[5][0] = 1046;
        this.u_infor[5][1] = 43707;
        this.u_infor[6][0] = 5721;
        this.u_infor[6][1] = 35173;
        this.u_infor[7][0] = 1155;
        this.u_infor[7][1] = 22337;


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
        btn_PagoServicios = v.findViewById(R.id.btn_Servicios);


        RecyclerImpuesto = v.findViewById(R.id.recyclerImpuestos);
        RecyclerImpuesto.setHasFixedSize(true);


        VendedorName = new ArrayList<>();
        VendedorID = new ArrayList<>();
        progreso = new ProgressDialog(getContext());

        SucursalName = new ArrayList<>();
        SucursalID = new ArrayList<>();
        SpinnerSucursal = (Spinner) v.findViewById(R.id.sucursal);

        ClientesName = new ArrayList<>();
        ClientesID = new ArrayList<>();

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
        mAdapter = new ImpuestoAdapter(ImpuestosList);


        Meses = new ArrayList<>();

        tabla_venta_articulos = v.findViewById(R.id.tabla_venta_articulos);
        tabla_venta_articulos.setEmptyDataIndicatorView(v.findViewById(R.id.Tabla_vacia));

        try{
            InstanciarTicket();
            LoadSucursales();
            LoadButtons();
            VerificarParcialidades();
        }catch (NullPointerException e)
        {

        }catch (RuntimeException s)
        {

        }



        SpinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                InstanciarTicket();
                LoadAutocomplete();
                CargaEncuesta();

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



      // usbCtrl = new UsbController(this,mHandler);
        //promociones_credito();

        LoadPermisosFunciones();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        usbCtrl.close();
        cancelAll();
    }

    private void closeKeyboard() {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void CargaEncuesta()
    {
        // /api/configuracion/sucursales/select

        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/configuracion/sucursales/select/"+SucursalID.get(SpinnerSucursal.getSelectedItemPosition())+"?usu_id=" + usu_id + "&esApp=1&code="+code;

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                if (EstatusApi == 1) {
                                    JSONObject Resultado = response.getJSONObject("resultado");
                                    LinkEncuesta = Resultado.getString("con_url_encuesta");
                                    try {
                                        QR = TextToImageEncode(LinkEncuesta);
                                    } catch (WriterException e) {
                                        e.printStackTrace();
                                    }


                                }
                                else{
                                    Toast toast1 =
                                            Toast.makeText(getContext(),
                                                    "Error del servidor", Toast.LENGTH_LONG);

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

    public void EliminarClienteTicket(){

        ticket_de_venta.setTic_id_sucursal(SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
        //ArticulosVenta.clear();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id", ticket_de_venta.getTic_id());
            request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal());
            request.put("tic_id_cliente", "");
            request.put("tic_nombre_cliente", "");
            request.put("code", code);

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


    public void LoadAutocomplete() {
        try {

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/tickets/select-articulos?usu_id=" + usu_id + "&esApp=1&code="+code+"&tic_id_sucursal="+SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

            // prepare the Request
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray RespuestaResultado = null;
                            JSONArray RespuestaImagenes = null;
                            JSONObject RespuestaUUID = null;
                            JSONObject RespuestaPrecio = null;

                            String RutaImagen1 = "";
                            String RutaImagen2 = "";

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                Articulos = new ArrayList<>();

                                if (EstatusApi == 1) {

                                    RespuestaResultado = response.getJSONArray("resultado");
                                    for (int x = 0; x < RespuestaResultado.length(); x++) {
                                        JSONObject elemento = RespuestaResultado.getJSONObject(x);
                                        String NombreCompleto = elemento.getString("art_nombre");
                                        ArticulosName.add(NombreCompleto);

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
            postRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
        } catch (Error e) {
            e.printStackTrace();
        }

    }

    public void LoadSucursales() {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("code", code);

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
        progressDialog.show();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("code", code);

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
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
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
                btn_eliminar_cliente_ticket = dialog.findViewById(R.id.btn_eliminar_Cliente);
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

                btn_eliminar_cliente_ticket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ticket_de_venta.setTic_nombre_cliente("");
                        ticket_de_venta.setTic_id_cliente("");
                        btn_agregar_cliente.setText("Cliente");
                        try{

                        }catch(ArrayIndexOutOfBoundsException s)
                        {
                            EliminarClienteTicket();
                        }

                    }
                });

                crear_cliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        fr.replace(R.id.fragment_container, new Fragment_ventas_crear_cliente()).commit();
                        onDetach();
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
                        try{
                            AniadirVendedorTicket("1");
                            btn_agregar_vendedor.setText(ticket_de_venta.getTic_nombre_vendedor());
                        }catch(ArrayIndexOutOfBoundsException s)
                        {

                        }
                    }
                });

            }
        });

        btn_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                bundle.putBoolean("Transacciones", Transacciones);
                bundle.putBoolean("Comision", Comision);
                bundle.putBoolean("Conte_Caja", Conte_Caja);
                Fragment_ventas_transacciones fragment2 = new Fragment_ventas_transacciones();
                fragment2.setArguments(bundle);
                try{
                    fr.replace(R.id.fragment_container,fragment2).commit();
                    onDetach();
                }catch (IllegalStateException s)
                {

                }

            }
        });

        Corte_Caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                bundle.putBoolean("Transacciones", Transacciones);
                bundle.putBoolean("Comision", Comision);
                bundle.putBoolean("Conte_Caja", Conte_Caja);
                Fragment_ventas_corte_caja_listado fragment2 = new Fragment_ventas_corte_caja_listado();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onStop();
                onDetach();
            }
        });

        Comisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                bundle.putBoolean("Transacciones", Transacciones);
                bundle.putBoolean("Comision", Comision);
                bundle.putBoolean("Conte_Caja", Conte_Caja);
                Fragment_pestania_comison fragment2 = new Fragment_pestania_comison();
                fragment2.setArguments(bundle);
                fr.replace(R.id.fragment_container,fragment2).commit();
                onStop();
                onDetach();
            }
        });

        btn_agregar_articulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.pop_up_ventas_seleccionar_articulo);
                dialog.show();
                tabla_selecciona_articulo = dialog.findViewById(R.id.tabla_seleccionar_articulos);
                tabla_selecciona_articulo.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
                SearchView BuscarArticulo = dialog.findViewById(R.id.Buscar_articulo);

                Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                try{
                    CargaArticulos();
                    BuscarArticulo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            ArticuloAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }catch (ArrayIndexOutOfBoundsException e)
                {

                }


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
                closeKeyboard();

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

                  String SucursalID =  ticket_de_venta.getTic_id_sucursal();

                    for(int x=0; x<ArticulosVenta.size();x++)
                    {
                        promociones_credito(ArticulosVenta.get(x),SucursalID);
                    }


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

                            VerificarPromocionCreditoMenor();
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
                            ticket_de_venta.setTic_facturar("true");
                            facturar(ticket_de_venta.getTic_facturar());
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
                                    if(ParcialidadesBoleano==true)
                                    {
                                        dialog.dismiss();
                                        dialog.setContentView(R.layout.pop_up_ventas_parcialidades_completo);
                                        dialog.show();
                                        Button cancelarVenta = dialog.findViewById(R.id.cancelar);
                                        Button cancelarVenta2 = dialog.findViewById(R.id.btnSalir3);
                                        View Completo = dialog.findViewById(R.id.Completo);
                                        View Parcialidades = dialog.findViewById(R.id.Parcialidades);
                                        cancelarVenta.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        cancelarVenta2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        Completo.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                PagoCompletoFacturar();
                                            }
                                        });
                                        Parcialidades.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                PagoParcialFacturar();
                                            }
                                        });
                                    }else
                                    {
                                        PagoCompletoFacturar();
                                    }


//aqui
                                }
                            });
                        }
                    });
                    Button no_facturar = dialog.findViewById(R.id.no_facturar);
                    no_facturar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ParcialidadesBoleano==true)
                            {
                                dialog.dismiss();
                                dialog.setContentView(R.layout.pop_up_ventas_parcialidades_completo);
                                dialog.show();
                                Button cancelarVenta = dialog.findViewById(R.id.cancelar);
                                Button cancelarVenta2 = dialog.findViewById(R.id.btnSalir3);
                                View Completo = dialog.findViewById(R.id.Completo);
                                View Parcialidades = dialog.findViewById(R.id.Parcialidades);
                                cancelarVenta.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                cancelarVenta2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                Completo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PagoCompletoNoFacturar();
                                    }
                                });
                                Parcialidades.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PagoParcialNoFacturar();
                                    }
                                });
                            }else
                            {
                                PagoCompletoNoFacturar();
                            }


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

        btn_PagoServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), Feenicia_Transaction_Bluetooth.class);
                Bundle ServiciosBundle = new Bundle();
                ServiciosBundle.putString("TipoVenta","Servicios");
                ServiciosBundle.putString("Code",code);
                myIntent.putExtras(ServiciosBundle);
                getActivity().startActivity(myIntent);
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
                list1.put("oea_codigo_categoria_sat", ArticulosOrdenados.get(i).getCodigo_sat());
                list1.put("oea_um_codigo", ArticulosOrdenados.get(i).getUm_codigo());
                list1.put("oea_um_simbologia", ArticulosOrdenados.get(i).getUm_simbologia());
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
                    Toast.makeText(getContext(), "No has seleccionado ningún artículo", Toast.LENGTH_LONG);
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
                request.put("code", code);


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
                            InstanciarTicket();
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

        progressDialog.show();
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
                list1.put("aar_codigo_categoria_sat", ArticulosApartados.get(i).getCodigo_sat());
                list1.put("aar_um_codigo", ArticulosApartados.get(i).getUm_codigo());
                list1.put("aar_um_simbologia", ArticulosApartados.get(i).getUm_simbologia());
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
                    Toast.makeText(getContext(), "No has seleccionado ningún artículo", Toast.LENGTH_LONG);
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
                request.put("code", code);



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
                            InstanciarTicket();

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

                            progressDialog.dismiss();
                        } else {
                            Toast toast1 =
                                    Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                            toast1.show();
                            progressDialog.dismiss();

                        }

                    } catch (JSONException e) {
                        Toast toast1 =
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                        toast1.show();
                        progressDialog.dismiss();

                    }
                }

            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                            toast1.show();
                            progressDialog.dismiss();

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
            request.put("code", code);

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

                        if(!ticket_de_venta.getTic_apa_id().equals(""))
                        {
                            ConcluirApartado(ticket_de_venta.getTic_apa_id());
                        }

                        ArticulosVenta.clear();
                        Imagenes.clear();
                        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                                total, descuento, impuesto, subtotal,
                                carouselView, Imagenes,ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,btn_ordenar,btn_apartar,btn_finalizar,
                                RecyclerImpuesto);
                        articuloAdapter.notifyDataSetChanged();
                        tabla_venta_articulos.setDataAdapter(articuloAdapter);
                        LoadImages();


                        InstanciarTicket();

                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
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

        progressDialog.show();
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
            request.put("code", code);

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
                        ImpuestosList = new ArrayList<>();
                        for (int f = 0; f < tic_impuestos.length(); f++) {
                            JSONObject nodo_impuestos = tic_impuestos.getJSONObject(f);
                            String ValorImpuesto = nodo_impuestos.getString("valor");
                            double ValorEnPesos = Double.parseDouble(ValorImpuesto);
                            if(ValorEnPesos<0)
                            {
                                ValorEnPesos=ValorEnPesos*-1;
                            }
                            ValorImpuesto= String.valueOf(formatter.format(ValorEnPesos));
                            Impuestos impuesto = new Impuestos("",ValorImpuesto);
                            impuesto.setNombreImpuesto(nodo_impuestos.getString("nombre"));
                            ImpuestosList.add(impuesto);
                        }

                        mAdapter = new ImpuestoAdapter(ImpuestosList);
                        RecyclerImpuesto.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getContext());
                        RecyclerImpuesto.setLayoutManager(mLayoutManager);
                        RecyclerImpuesto.setAdapter(mAdapter);


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
                                String ArticuloApartadoSat = nodo.getString("tar_categoria_sat");
                                String ArticuloApartadoUm_codigo = nodo.getString("tar_um_codigo");
                                String ArticuloApartadoUm_simbologia = nodo.getString("tar_um_simbologia");

                                ArticuloApartado.setCodigo_sat(ArticuloApartadoSat);
                                ArticuloApartado.setUm_codigo(ArticuloApartadoUm_codigo);
                                ArticuloApartado.setUm_simbologia(ArticuloApartadoUm_simbologia);


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
                                String ArticuloApartadoSat = nodo.getString("tar_categoria_sat");
                                String ArticuloApartadoUm_codigo = nodo.getString("tar_um_codigo");
                                String ArticuloApartadoUm_simbologia = nodo.getString("tar_um_simbologia");

                                ArticuloOrdenado.setCodigo_sat(ArticuloApartadoSat);
                                ArticuloOrdenado.setUm_codigo(ArticuloApartadoUm_codigo);
                                ArticuloOrdenado.setUm_simbologia(ArticuloApartadoUm_simbologia);

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


                        double Price = Double.parseDouble(ticket_de_venta.getTic_importe_total());
                        total.setText(formatter.format(Price));

                        double descu = Double.parseDouble(ticket_de_venta.getTic_importe_descuentos());
                        descuento.setText(formatter.format(descu));

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
                            String unidad_medida = elemento.getString("art_um_simbologia");

                            JSONArray RespuestaImagenes = elemento.getJSONArray("art_imagenes");
                            for (int z = 0; z < RespuestaImagenes.length(); z++) {
                                String RutaImagen = getString(R.string.Url) + RespuestaImagenes.getString(z);
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
                            articulo.setGetArticulo_UM(unidad_medida);
                            ArticulosVenta.add(articulo);

                        }

                        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                                total, descuento, impuesto, subtotal,
                                carouselView, Imagenes, ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,btn_ordenar,btn_apartar,btn_finalizar,
                                RecyclerImpuesto);
                        articuloAdapter.notifyDataSetChanged();
                        tabla_venta_articulos.setDataAdapter(articuloAdapter);
                        LoadImages();
                        progressDialog.dismiss();

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
        progressDialog.show();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("code", code);

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
                            JSONObject ElementoUsuario = elemento.getJSONObject("cli_id");
                            UUID = ElementoUsuario.getString("uuid");
                            String nombre = elemento.getString("cli_nombre");
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
                            String UltimaCompra = elemento.getString("cli_ultima_compra");
                            String ConsumoPromedio = elemento.getString("cli_promedio_compra");


                            HistorialCompras = new ArrayList<>();
                            JSONArray comprasNodo = elemento.getJSONArray("ventas");
                            for (int d = 0; d < comprasNodo.length(); d++) {
                                JSONObject elementoCompra = comprasNodo.getJSONObject(d);
                                String NoTicket = elementoCompra.getString("tic_numero");
                                String Importe = elementoCompra.getString("tic_importe_total");
                                String Fecha = elementoCompra.getString("fecha_hora_venta");
                                CompraModel compra = new CompraModel(NoTicket, Importe, Fecha);
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
                                    direccion_igual,
                                    HistorialCompras,
                                    UltimaCompra,
                                    ConsumoPromedio
                            );
                            clientes.add(cliente);
                        }
                        final ClienteAdapter clienteAdapter = new ClienteAdapter(getContext(), clientes, tabla_clientes, fr);
                        tabla_clientes.setDataAdapter(clienteAdapter);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
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
        progressDialog.show();

        try {
            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/tickets/select-articulos?usu_id=" + usu_id + "&esApp=1&code="+code+"&tic_id_sucursal="+SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

            // prepare the Request
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray RespuestaResultado = null;
                            JSONArray RespuestaImagenes = null;
                            JSONObject RespuestaUUID = null;
                            JSONObject RespuestaPrecio = null;

                            String RutaImagen1 = "";
                            String RutaImagen2 = "";

                            try {

                                int EstatusApi = Integer.parseInt(response.getString("estatus"));

                                Articulos = new ArrayList<>();

                                if (EstatusApi == 1) {

                                    RespuestaResultado = response.getJSONArray("resultado");
                                    for (int x = 0; x < RespuestaResultado.length(); x++) {
                                        JSONObject elemento = RespuestaResultado.getJSONObject(x);

                                        String UUID = elemento.getString("art_id");
                                        String Precio = elemento.getString("art_precio_lista");
                                        String NombreArticulo = elemento.getString("art_nombre");
                                        String SKU = elemento.getString("art_sku");
                                        String CodigoBarras = elemento.getString("art_codigo_barras");
                                        String num_existencia = elemento.getString("art_existencia");

                                        String NombreModificador = elemento.getString("art_nombre_modificador");

                                        RespuestaImagenes = elemento.getJSONArray("art_imagenes");

                                        for (int z = 0; z < RespuestaImagenes.length(); z++) {

                                            if (RutaImagen1.equals("")) {
                                                RutaImagen1 = RespuestaImagenes.getString(z);
                                            } else {
                                                RutaImagen2 = RespuestaImagenes.getString(z);
                                            }

                                        }
                                                final ArticuloModel Articulo = new ArticuloModel(UUID,NombreArticulo," ",Precio,RutaImagen1,
                                                        RutaImagen2,SKU,"","","","","",
                                                        "","","","");
                                                Articulo.setCodigo_barras(CodigoBarras);
                                                Articulo.setNum_existencia(num_existencia);
                                                Articulos.add(Articulo);
                                    }
                                    ArticuloAdapter = new SeleccionarArticuloVentaAdapter(getContext(), Articulos, tabla_selecciona_articulo);
                                    tabla_selecciona_articulo.setDataAdapter(ArticuloAdapter);
                                    progressDialog.dismiss();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast toast1 =
                                            Toast.makeText(getContext(), "Error en el servidor", Toast.LENGTH_LONG);
                                    toast1.show();
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
            postRequest.setShouldCache(false);

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
    }
//===========================================================================================================
    private void CargaMetodosPago(TextView importeFaltante, TextView importeRecibido) {

        ListaDePagosDisponibles.clear();
        ListaDePagos_a_utilizar.clear();

        ticket_de_venta.setTic_id_sucursal(SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal());
            request.put("code", code);

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

                        String TotalString = String.valueOf(Total);
                        MetodoPagoAdapter metodo = new MetodoPagoAdapter(getContext(), ListaDePagosDisponibles,tabla_metodos_pago,
                                ListaDePagos_a_utilizar,importeRecibido,importeFaltante,
                                ticket_de_venta.getTic_importe_total());
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
        progressDialog.show();
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
            request.put("code", code);
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
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
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
        progressDialog.show();
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
            request.put("code", code);
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
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
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
            request.put("code", code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/update-uso-cfdi";

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
        progressDialog.show();
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

            String ApiPath = url + "/api/articulos/buscar_sku_articulo?usu_id=" + usu_id + "&esApp=1&code="+code+"&sku=" + SKU;

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
                                    String ruta = getString(R.string.Url) + RutaImagen1;
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

                                    progressDialog.dismiss();


                                }
                            } catch (JSONException e) {
                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                String.valueOf(e), Toast.LENGTH_LONG);
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf(error), Toast.LENGTH_LONG);
                            progressDialog.dismiss();
                        }
                    }
            );

            getRequest.setShouldCache(false);
            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    private void InstanciarTicket() {
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
        ticket_de_venta.setTic_apa_id("");
        ArticulosVenta.clear();
        Imagenes.clear();
        total.setText("$0.00");
        subtotal.setText("$0.00");
        descuento.setText("$0.00");
        btn_agregar_vendedor.setText("Vendedor");
        btn_agregar_cliente.setText("Cliente");
        btn_finalizar.setText("Cobrar");
        ListaDeArticulosApartados.clear();
        ListaDeArticulosOrdenados.clear();
        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                total, descuento, impuesto, subtotal,
                carouselView, Imagenes, ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,btn_ordenar,btn_apartar,btn_finalizar,
                RecyclerImpuesto);
        articuloAdapter.notifyDataSetChanged();
        tabla_venta_articulos.setDataAdapter(articuloAdapter);
        LoadImages();
        ListaDePagos_a_utilizar.clear();
        ImpuestosList.clear();
        mAdapter = new ImpuestoAdapter(ImpuestosList);
        RecyclerImpuesto.setAdapter(mAdapter);
        btn_apartar.setVisibility(View.INVISIBLE);
        btn_ordenar.setVisibility(View.INVISIBLE);
        btn_finalizar.setVisibility(View.INVISIBLE);

        bundle = getArguments();
        if(bundle!=null&&!bundle.getString("tic_id").equals("")&&!bundle.getString("suc_id").equals("")&&!bundle.getString("apa_id").equals(""))
        {
            ticket_de_venta.setTic_id(bundle.getString( "tic_id"));
            ticket_de_venta.setTic_id_sucursal(bundle.getString( "suc_id"));
            ticket_de_venta.setTic_apa_id(bundle.getString("apa_id"));
            VerificarTicket();
        }
    }

    private void LoadConfiguracionApartado()
    {
        try {
            String ApiPath = getString(R.string.Url)+"/api/ventas/apartados/index?usu_id=" + usu_id + "&esApp=1&code="+code+"&suc_id="+SucursalID.get(SpinnerSucursal.getSelectedItemPosition());

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
        progressDialog.show();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("code", code);

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
                        progressDialog.dismiss();
                    } else {
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);

                        toast1.show();
                        progressDialog.dismiss();


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

    public void promociones_credito(ArticuloModel articulo,String SucursalID){

        String url = getString(R.string.Url);
        String ApiPath = url + "/api/articulos/promociones/consultar_promociones_articulo_sku_app";

        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("sku", articulo.getArticulo_sku());
            request.put("suc_id", SucursalID);
            request.put("code", code);

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
                                if (Resp == Integer.parseInt("0")){

                                    Toast toast1 = Toast.makeText(getContext(), "No existen promociones", Toast.LENGTH_LONG);
                                    toast1.show();

                                    Respuestamenor = "No aplica meses sin interes a Crédito";

                                }
                                else
                                    {

                                    Credito = Promociones.getJSONObject("credito");

                                    Iterator d = Credito.keys();

                                            while (d.hasNext()) {

                                                String keyCredito = (String) d.next();
                                                JSONObject ResultadoCredito = Credito.getJSONObject(keyCredito);
                                                //for(int i=0;i<Credito.length();i++) {

                                                //JSONObject ResultadoCredito = Credito.getJSONObject( String.valueOf( i + 1) );

                                                NomPromoCredito = ResultadoCredito.getString("pro_nombre");

                                                Boolean tresmeses = ResultadoCredito.getBoolean("apr_tres_meses");
                                                if (tresmeses == true) {
                                                    articulo.setCreditoTresMeses(true);
                                                }
                                                else{
                                                    if(!articulo.getCreditoTresMeses()==true)
                                                    {
                                                        articulo.setCreditoTresMeses(false);
                                                    }
                                                }

                                                Boolean seismeses = ResultadoCredito.getBoolean("apr_seis_meses");
                                                if (seismeses == true) {

                                                    articulo.setCreditoSeisMeses(true);
                                                }
                                                else{
                                                    if(!articulo.getCreditoSeisMeses()==true)
                                                    {
                                                        articulo.setCreditoSeisMeses(false);
                                                    }
                                                }

                                                Boolean nuevemeses = ResultadoCredito.getBoolean("apr_nueve_meses");
                                                if (nuevemeses == true) {

                                                    articulo.setCreditoNueveMeses(true);
                                                }else{
                                                    if(!articulo.getCreditoNueveMeses()==true)
                                                    {
                                                        articulo.setCreditoNueveMeses(false);
                                                    }
                                                }

                                                Boolean docemeses = ResultadoCredito.getBoolean("apr_doce_meses");
                                                if (docemeses == true) {

                                                    articulo.setCreditoDoceMeses(true);
                                                }else{
                                                    if(!articulo.getCreditoDoceMeses()==true)
                                                    {
                                                        articulo.setCreditoDoceMeses(false);
                                                    }
                                                }

                                            }
                            }
                            }
                        }

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

    private void VerificarPromocionCreditoMenor()
    {

        if(ArticulosVenta.size()==1)
        {
            if(ArticulosVenta.get(0).getCreditoTresMeses()==true)
            {
                Resptresmeses=1;
            }else{
                Resptresmeses=2;
            }
            if(ArticulosVenta.get(0).getCreditoSeisMeses()==true)
            {
                Respseismeses=1;
            }else{
                Respseismeses=2;
            }
            if(ArticulosVenta.get(0).getCreditoNueveMeses()==true)
            {
                Respnuevemeses=1;
            }else{
                Respnuevemeses=2;
            }
            if(ArticulosVenta.get(0).getCreditoDoceMeses()==true)
            {
                Respdocemeses=1;
            }else{
                Respdocemeses=2;
            }
        }
        else
        {
            Boolean doce = true;
            Boolean nueve = true;
            Boolean seis = true;
            Boolean tres = true;
            for(int x=0; x<ArticulosVenta.size();x++)
            {
                //-----------------------------------------------------**********
                //-----------------------------------------------------TRES MESES
                if(tres==ArticulosVenta.get(x).getCreditoTresMeses())
                {
                    if(tres==true && ArticulosVenta.get(x).getCreditoTresMeses()==true)
                    {
                        tres=true;
                        Resptresmeses=1;
                    }
                    if(tres==false && ArticulosVenta.get(x).getCreditoTresMeses()==false)
                    {
                        tres=false;
                        Resptresmeses=2;
                    }
                }
                else
                {
                    if(tres==true && ArticulosVenta.get(x).getCreditoTresMeses()==false)
                    {
                        tres=false;
                        Resptresmeses=2;
                    }
                }
                //-----------------------------------------------------**********
                //-----------------------------------------------------SEIS MESES
                if(seis==ArticulosVenta.get(x).getCreditoSeisMeses())
                {
                    if(seis==true && ArticulosVenta.get(x).getCreditoSeisMeses()==true)
                    {
                        seis=true;
                        Respseismeses=1;
                    }
                    if(seis==false && ArticulosVenta.get(x).getCreditoSeisMeses()==false)
                    {
                        seis=false;
                        Respseismeses=2;
                    }
                }
                else
                {
                    if(seis==true && ArticulosVenta.get(x).getCreditoSeisMeses()==false)
                    {
                        seis=false;
                        Respseismeses=2;
                    }
                }
                //-----------------------------------------------------***********
                //-----------------------------------------------------NUEVE MESES
                if(nueve==ArticulosVenta.get(x).getCreditoNueveMeses())
                {
                    if(nueve==true && ArticulosVenta.get(x).getCreditoNueveMeses()==true)
                    {
                        nueve=true;
                        Respnuevemeses=1;
                    }
                    if(nueve==false && ArticulosVenta.get(x).getCreditoNueveMeses()==false)
                    {
                        nueve=false;
                        Respnuevemeses=2;
                    }
                }
                else
                {
                    if(nueve==true && ArticulosVenta.get(x).getCreditoNueveMeses()==false)
                    {
                        nueve=false;
                        Respnuevemeses=2;
                    }
                }
                //-----------------------------------------------------**********
                //-----------------------------------------------------DOCE MESES
                if(doce==ArticulosVenta.get(x).getCreditoDoceMeses())
                {
                    if(doce==true && ArticulosVenta.get(x).getCreditoDoceMeses()==true)
                    {
                        doce=true;
                        Respdocemeses=1;
                    }
                    if(doce==false && ArticulosVenta.get(x).getCreditoDoceMeses()==false)
                    {
                        doce=false;
                        Respdocemeses=2;
                    }
                }
                else
                {
                    if(doce==true && ArticulosVenta.get(x).getCreditoDoceMeses()==false)
                    {
                        doce=false;
                        Respdocemeses=2;
                    }
                }
                //-----------------------------------------------------
            }
        }
    }


    //______________________________________________

    private void loadTicket()
    {
        progressDialog.dismiss();
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pop_up_ticket_web);
        dialog.show();

        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Espere un momento por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        webView = (WebView) dialog.findViewById(R.id.simpleWebView);
        try {

            String url = getString(R.string.Url);

            String ApiPath;

            ApiPath = url + "/api/ventas/movimientos/obtener_detalle_ticket?" +
                    "usu_id=" + usu_id +
                    "&esApp=1" +
                    "&code="+code+
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
                                    progressDialog.dismiss();

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
                                    if(NombreVendedor.equals("null"))
                                    {
                                        NombreVendedor="";
                                    }
                                    NombreCliente = NodoTicket.getString("tic_nombre_cliente");
                                    if(NombreCliente.equals("null"))
                                    {
                                        NombreCliente="";
                                    }
                                    Subtotal = Float.parseFloat(NodoTicket.getString("tic_importe_subtotal"));
                                    Total = Float.parseFloat(NodoTicket.getString("tic_importe_total"));
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

                                    try{
                                        content= "<!DOCTYPE html>\n" +
                                                "<html dir=\"ltr\" lang=\"en\">\n" +
                                                "<head>\n" +
                                                "    <meta charset=\"utf-8\">\n" +
                                                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                                                "<style type=\"text/css\" media=\"all\">\n" +
                                                "#tblTicketTemplate {\n" +
                                                "  width: 70%;\n" +
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
                                    }catch (IllegalStateException a)
                                    {

                                    }


                                    // displaying text in WebView
                                    webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

                                    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTicket/");
                                    final String fileName="Ticket.pdf";

                                    try{
                                        PdfView.createWebPrintJob(getActivity(), webView, directory, fileName, new PdfView.Callback() {

                                            @Override
                                            public void success(String path) {
                                                progressDialog.dismiss();
                                                //PdfView.openPdfFile(getActivity(),getString(R.string.app_name),"¿Desea abrir el archivo pdf?"+fileName,path);
                                            }

                                            @Override
                                            public void failure() {
                                                progressDialog.dismiss();

                                            }
                                        });
                                    }catch (NullPointerException f)
                                    {

                                    }


                                    //Boton para imprimir le manda el archivo PDF---------------------------------------------------------------------
                                    Button ImprimirTick = dialog.findViewById(R.id.btn_imprimir);
                                    ImprimirTick.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String cadenaArticulosImprimir="";
                                            for(int j=0; j<ListaArticulosTicket.size();j++)
                                            {
                                                double PrecioArticulo = Double.parseDouble(ListaArticulosTicket.get(j).getarticulo_Precio());
                                                double ImporteArticulo = Double.parseDouble(ListaArticulosTicket.get(j).getArticulo_importe());


                                                cadenaArticulosImprimir=cadenaArticulosImprimir+
                                                        ("\n"+
                                                                ListaArticulosTicket.get(j).getArticulo_cantidad()+" "+
                                                                ListaArticulosTicket.get(j).getarticulo_Nombre()+"\n                  "+
                                                                formatter.format( ImporteArticulo));
                                            }



                                            contenidoImprimir =
                                                    "      "+RazonSocial+ "\n"+
                                                            "      "+RFC+ "\n"+
                                                            "FEC./HR./:"+FechaCreacion+ "\n"+
                                                            "TICKET:"+NumeroTicket+"\n"+
                                                            "VENDEDOR: "+NombreVendedor + "\n"+
                                                            "CLIENTE: "+NombreCliente + "\n"+
                                                            "C.  ARTICULO       IMPORTE"+
                                                            cadenaArticulosImprimir+"\n\n\n"+
                                                            "SUBTOTAL:         "+formatter.format(Subtotal)+"\n"+
                                                            "IMPUESTOS:        "+formatter.format( ImpuestosTotal)+"\n"+
                                                            "TOTAL:            "+formatter.format( Total)+"\n     ";
                                            contenidoImprimir2=
                                                    "     ESTE TICKET FUE CREADO  \n"+
                                                            "     DESDE BIO-NET PUNTO DE  \n"+
                                                            "     VENTA, EL MEJOR SISTEMA \n"+
                                                            "     PARA TU NEGOCIO, PARA   \n"+
                                                            "     MAS INFORMACION VISITA  \n"+
                                                            "         BIONETPOS.COM\n\n\n";


                                            PDFIMprime(directory.getAbsolutePath()+"/"+fileName);
                                        }
                                    });



                                }
                                else
                                {
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast toast1 =
                                        Toast.makeText(getContext(),
                                                String.valueOf(e), Toast.LENGTH_LONG);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
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

    public void PDFIMprime (String ruta)
    {
        try {

            usbCtrl.close();

            // int i =0;

            for(int i = 0; i < 8; ++i) {
                dev = usbCtrl.getDev(this.u_infor[i][0], this.u_infor[i][1]);
                if (dev != null) {
                    break;
                }
            }

            if (dev != null) {
                if (!usbCtrl.isHasPermission(dev)) {
                    usbCtrl.getPermission(dev);
                } else {
                    // Toast.makeText(this.getApplicationContext(), this.getString(2130968584), 0).show();

                }
            }

            String msg = "";

            byte[] bytes = new byte[0];
            try {
                bytes = convertDocToByteArray(ruta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String stream = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                stream = Base64.getEncoder().encodeToString(bytes);
            }
            byte[] newBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                newBytes = Base64.getDecoder().decode(stream);
            }
            try {
                convertByteArrayToDoc(ruta, newBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(getString(R.string.Url)+LogoNegocio);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                byte[] Data = POS_PrintBMP(image,384,1);
                this.SendDataByte(Data);
                this.SendDataString(contenidoImprimir);
                byte[] DataQR = POS_PrintBMP(QR,320,0);
                this.SendDataByte(DataQR);
                this.SendDataString(contenidoImprimir2);
            } catch(IOException e) {
                System.out.println(e);
            }



        }catch(NullPointerException e) {
            Toast toast1 =
                    Toast.makeText(getContext(),
                            "Hubo un error al tratar de conectar con la impresora", Toast.LENGTH_LONG);
            toast1.show();
        }
    }

    public static byte[] convertDocToByteArray(String path)throws FileNotFoundException, IOException {
        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            // Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    public static void convertByteArrayToDoc(String path, byte[] bytes)throws FileNotFoundException, IOException {
        File someFile = new File(path);
        FileOutputStream fos = new FileOutputStream(someFile);
        fos.write(bytes);
        fos.flush();
        fos.close();
    }

    private void SendDataString(String data) {
        if (data.length() > 0) {
            usbCtrl.sendMsg(data, "GBK", dev);
        }

    }

    private void SendDataByte(byte[] data){
        if(data.length>0)
            usbCtrl.sendByte(data, dev);
    }

    public static byte[] POS_PrintBMP(Bitmap mBitmap, int nWidth, int nMode) {
        // 先转黑白，再调用函数缩放位图
        int width = ((nWidth + 7) / 8) * 8;
        int height = mBitmap.getHeight() * width / mBitmap.getWidth();
        height = ((height + 7) / 8) * 8;

        Bitmap rszBitmap = mBitmap;
        if (mBitmap.getWidth() != width){
            rszBitmap = Other.resizeImage(mBitmap, width, height);
        }

        Bitmap grayBitmap = Other.toGrayscale(rszBitmap);

        byte[] dithered = Other.thresholdToBWPic(grayBitmap);

        byte[] data = Other.eachLinePixToCmd(dithered, width, nMode);

        return data;
    }

    public static byte[] POS_Set_Font(String str, int bold, int font, int widthsize, int heigthsize){

        if(str.length() == 0 | widthsize<0 | widthsize >4 | heigthsize<0 | heigthsize>4
                | font<0 | font>1)
            return null;

        byte[] strData = null;
        try
        {
            strData = str.getBytes("GBK");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }

        byte[] command = new byte[strData.length + 9];

        byte[] intToWidth = { 0x00, 0x10, 0x20, 0x30 };//最大四倍宽
        byte[] intToHeight = { 0x00, 0x01, 0x02, 0x03 };//最大四倍高

        command[0] = 27;
        command[1] = 69;
        command[2] = ((byte)bold);
        command[3] = 27;
        command[4] = 77;
        command[5] = ((byte)font);
        command[6] = 29;
        command[7] = 33;
        command[8] = (byte) (intToWidth[widthsize] + intToHeight[heigthsize]);

        System.arraycopy(strData, 0, command, 9, strData.length);
        return command;
    }

    private void facturar(String opcion)
    {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("tic_id", ticket_de_venta.getTic_id());
            request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal());
            request.put("tic_facturar", opcion);
            request.put("code", code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/store-facturar";

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

    private void VerificarTicket()
    {
        if (ticket_de_venta.getTic_id().equals(""))
        {

        }
        else
        {
            JSONObject request = new JSONObject();
            try {
                request.put("usu_id", usu_id);
                request.put("esApp", "1");
                request.put("tic_id", ticket_de_venta.getTic_id());
                request.put("tic_id_sucursal", ticket_de_venta.getTic_id_sucursal());
                request.put("tic_facturar", "false");
                request.put("code", code);

            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = getString(R.string.Url);

            String ApiPath = url + "/api/ventas/tickets/store-facturar";

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
                            ImpuestosList = new ArrayList<>();
                            for (int f = 0; f < tic_impuestos.length(); f++) {
                                JSONObject nodo_impuestos = tic_impuestos.getJSONObject(f);
                                String ValorImpuesto = nodo_impuestos.getString("valor");
                                double ValorEnPesos = Double.parseDouble(ValorImpuesto);
                                if(ValorEnPesos<0)
                                {
                                    ValorEnPesos=ValorEnPesos*-1;
                                }
                                ValorImpuesto= String.valueOf(formatter.format(ValorEnPesos));
                                Impuestos impuesto = new Impuestos("",ValorImpuesto);
                                impuesto.setNombreImpuesto(nodo_impuestos.getString("nombre"));
                                ImpuestosList.add(impuesto);
                            }

                            mAdapter = new ImpuestoAdapter(ImpuestosList);
                            RecyclerImpuesto.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(getContext());
                            RecyclerImpuesto.setLayoutManager(mLayoutManager);
                            RecyclerImpuesto.setAdapter(mAdapter);


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

                                    String ArticuloApartadoSat = nodo.getString("tar_categoria_sat");
                                    String ArticuloApartadoUm_codigo = nodo.getString("tar_um_codigo");
                                    String ArticuloApartadoUm_simbologia = nodo.getString("tar_um_simbologia");

                                    ArticuloApartado.setCodigo_sat(ArticuloApartadoSat);
                                    ArticuloApartado.setUm_codigo(ArticuloApartadoUm_codigo);
                                    ArticuloApartado.setUm_simbologia(ArticuloApartadoUm_simbologia);

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

                                    String ArticuloApartadoSat = nodo.getString("tar_categoria_sat");
                                    String ArticuloApartadoUm_codigo = nodo.getString("tar_um_codigo");
                                    String ArticuloApartadoUm_simbologia = nodo.getString("tar_um_simbologia");

                                    ArticuloOrdenado.setCodigo_sat(ArticuloApartadoSat);
                                    ArticuloOrdenado.setUm_codigo(ArticuloApartadoUm_codigo);
                                    ArticuloOrdenado.setUm_simbologia(ArticuloApartadoUm_simbologia);

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


                            double Price = Double.parseDouble(ticket_de_venta.getTic_importe_total());
                            total.setText(formatter.format(Price));

                            double descu = Double.parseDouble(ticket_de_venta.getTic_importe_descuentos());
                            descuento.setText(formatter.format(descu));

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
                                String unidad_medida = elemento.getString("art_um_simbologia");

                                JSONArray RespuestaImagenes = elemento.getJSONArray("art_imagenes");
                                for (int z = 0; z < RespuestaImagenes.length(); z++) {
                                    String RutaImagen = getString(R.string.Url) + RespuestaImagenes.getString(z);
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
                                articulo.setGetArticulo_UM(unidad_medida);
                                ArticulosVenta.add(articulo);

                            }

                            final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                                    total, descuento, impuesto, subtotal,
                                    carouselView, Imagenes, ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,btn_ordenar,btn_apartar,btn_finalizar,
                                    RecyclerImpuesto);
                            articuloAdapter.notifyDataSetChanged();
                            tabla_venta_articulos.setDataAdapter(articuloAdapter);
                            LoadImages();
                            progressDialog.dismiss();

                            if(ListaDeArticulosOrdenados.isEmpty())
                            {
                                btn_ordenar.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                btn_ordenar.setVisibility(View.INVISIBLE);
                            }

                            if(ListaDeArticulosApartados.isEmpty())
                            {
                                btn_apartar.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                btn_apartar.setVisibility(View.INVISIBLE);
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

                            btn_finalizar.setText("Concluir Apartado");
                            if(ArticulosConExistencias.isEmpty())
                            {
                                btn_finalizar.setVisibility(View.VISIBLE); //Invisible

                            }
                            else
                            {
                                btn_finalizar.setVisibility(View.VISIBLE);
                            }

                            progressDialog.dismiss();
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

    private void ConcluirApartado(String apa_ide)
    {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("apa_id", apa_ide);
            request.put("code", code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/apartados/concluir_apartado";

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

    private void LoadPermisosFunciones()
    {
        if(Proceso_Venta==false)
        {
            Buscar.setEnabled(false);
            btn_agregar_articulo.setEnabled(false);
            btn_agregar_cliente.setEnabled(false);
            btn_agregar_vendedor.setEnabled(false);
        }
        if(Transacciones==false)
        {
            btn_reporte.setEnabled(false);
        }
        if(Conte_Caja==false)
        {
            Corte_Caja.setEnabled(false);
        }
        if(Comision==false)
        {
            Comisiones.setEnabled(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public void PagoCompletoNoFacturar()
    {
        VerificarPromocionCreditoMenor();
        ticket_de_venta.setTic_facturar("false");
        dialog.dismiss();
        facturar(ticket_de_venta.getTic_facturar());
        dialog.setContentView(R.layout.pop_up_ventas_metodo_pago);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        tabla_metodos_pago = dialog.findViewById(R.id.tabla_seleccionar_metodo_pago);
        tabla_metodos_pago.setEmptyDataIndicatorView(dialog.findViewById(R.id.Tabla_vacia));
        TextView importe_recibido = dialog.findViewById(R.id.text_view_recibido);
        TextView importe_faltante = dialog.findViewById(R.id.text_view_faltante);
        CargaMetodosPago(importe_faltante,importe_recibido);

        Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
        cerrarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        //=====================================================



        TextView TotalAPagar = dialog.findViewById(R.id.total_a_pagar);
        double ImporteTotalConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_total());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        TotalAPagar.setText(formatter.format(ImporteTotalConDecimal));
        Button realizarPago = dialog.findViewById(R.id.realizar_Pago);
        realizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Aqui se realizan las validaciones cuando no lleva factura
                if(!ListaDePagos_a_utilizar.isEmpty())
                {
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

                        //Parte Bundle
                        Intent myIntent = new Intent(getActivity(), Feenicia_Transaction_Bluetooth.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putDouble("TC",TarjetaCredito);
                        mBundle.putDouble("TD",TarjetaDebito);
                        mBundle.putString( "Ticket",TicketIDVenta );
                        mBundle.putString("Sucursal",ticket_de_venta.getTic_id_sucursal());
                        mBundle.putInt( "03meses",Resptresmeses);
                        mBundle.putInt( "06meses",Respseismeses);
                        mBundle.putInt( "09meses",Respnuevemeses);
                        mBundle.putInt( "12meses",Respdocemeses);
                        mBundle.putString("TipoVenta","Venta_Normal");
                        mBundle.putString("Code",code);

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
                        dialog.dismiss();
                        dialog.setContentView(R.layout.pop_up_ventas_confirmacion_venta);
                        dialog.show();
                        progressDialog.show();

                        Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                        cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                loadTicket();
                                dialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                                bundle.putBoolean("Transacciones", Transacciones);
                                bundle.putBoolean("Comision", Comision);
                                bundle.putBoolean("Conte_Caja", Conte_Caja);
                                bundle.putString("tic_id", "");
                                bundle.putString("suc_id", "");
                                bundle.putString("apa_id", "");
                                Fragment_Ventas fragment2 = new Fragment_Ventas();
                                fragment2.setArguments(bundle);
                                fr.replace(R.id.fragment_container,fragment2).commit();

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
                                progressDialog.show();
                                loadTicket();
                                dialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                                bundle.putBoolean("Transacciones", Transacciones);
                                bundle.putBoolean("Comision", Comision);
                                bundle.putBoolean("Conte_Caja", Conte_Caja);
                                bundle.putString("tic_id", "");
                                bundle.putString("suc_id", "");
                                bundle.putString("apa_id", "");
                                Fragment_Ventas fragment2 = new Fragment_Ventas();
                                fragment2.setArguments(bundle);
                                fr.replace(R.id.fragment_container,fragment2).commit();


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

            }
        });
    }

    public void PagoCompletoFacturar()
    {
        dialog.setContentView(R.layout.pop_up_ventas_metodo_pago);
        dialog.setCanceledOnTouchOutside(false);
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
        TextView importe_recibido = dialog.findViewById(R.id.text_view_recibido);
        TextView importe_faltante = dialog.findViewById(R.id.text_view_faltante);
        CargaMetodosPago(importe_faltante,importe_recibido);

        TextView TotalAPagar = dialog.findViewById(R.id.total_a_pagar);
        double ImporteTotalConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_total());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        TotalAPagar.setText(formatter.format(ImporteTotalConDecimal));

        Button realizarPago = dialog.findViewById(R.id.realizar_Pago);
        realizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ListaDePagos_a_utilizar.isEmpty())
                {
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
                    //toast2.show();

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
                        mBundle.putInt( "03meses",Resptresmeses);
                        mBundle.putInt( "06meses",Respseismeses);
                        mBundle.putInt( "09meses",Respnuevemeses);
                        mBundle.putInt( "12meses",Respdocemeses);
                        mBundle.putString("TipoVenta","Venta_Normal");
                        mBundle.putString("Code",code);


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
                        progressDialog.show();

                        Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                        cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadTicket();
                                dialog.hide();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                                bundle.putBoolean("Transacciones", Transacciones);
                                bundle.putBoolean("Comision", Comision);
                                bundle.putBoolean("Conte_Caja", Conte_Caja);
                                bundle.putString("tic_id", "");
                                bundle.putString("suc_id", "");
                                bundle.putString("apa_id", "");
                                Fragment_Ventas fragment2 = new Fragment_Ventas();
                                fragment2.setArguments(bundle);
                                fr.replace(R.id.fragment_container,fragment2).commit();

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
                                progressDialog.show();
                                loadTicket();
                                dialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                                bundle.putBoolean("Transacciones", Transacciones);
                                bundle.putBoolean("Comision", Comision);
                                bundle.putBoolean("Conte_Caja", Conte_Caja);
                                bundle.putString("tic_id", "");
                                bundle.putString("suc_id", "");
                                bundle.putString("apa_id", "");
                                Fragment_Ventas fragment2 = new Fragment_Ventas();
                                fragment2.setArguments(bundle);
                                fr.replace(R.id.fragment_container,fragment2).commit();
                            }
                        });
                    }
                }


            }
        });
    }

    public void PagoParcialNoFacturar()
    {
        VerificarPromocionCreditoMenor();
        ticket_de_venta.setTic_facturar("false");
        dialog.dismiss();
        facturar(ticket_de_venta.getTic_facturar());
        dialog.setContentView(R.layout.pop_up_ventas_metodo_pago_parcialidades);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Spinner Spinner_Nombre_clientes = dialog.findViewById(R.id.SpinnerClientes);
        SpinnerClientes(Spinner_Nombre_clientes);
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

        TextView importe_recibido = dialog.findViewById(R.id.text_view_recibido);
        TextView importe_faltante = dialog.findViewById(R.id.text_view_faltante);
        CargaMetodosPago(importe_faltante,importe_recibido);

        TextView TotalAPagar = dialog.findViewById(R.id.total_a_pagar);
        double ImporteTotalConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_total());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        TotalAPagar.setText(formatter.format(ImporteTotalConDecimal));
        Spinner_Nombre_clientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ticket_de_venta.setTic_nombre_cliente(Spinner_Nombre_clientes.getSelectedItem().toString());
                ticket_de_venta.setTic_id_cliente(ClientesID.get(Spinner_Nombre_clientes.getSelectedItemPosition()));
                AniadirClienteTicket();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        Button realizarPago = dialog.findViewById(R.id.realizar_Pago);
        realizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ListaDePagos_a_utilizar.isEmpty()) {
                    double totalsumaimportes = 0;
                    double TarjetaCredito = 0;
                    double TarjetaDebito = 0;
                    double PagosEfectivo = 0;

                    for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {

                        String tipo_pago = ListaDePagos_a_utilizar.get(i).getNombre();
                        String cantPago = ListaDePagos_a_utilizar.get(i).getCantidad();

                        if (tipo_pago.equals("Tarjeta de crédito")) {
                            TarjetaCredito = Double.parseDouble(cantPago);
                        }

                        if (tipo_pago.equals("Tarjeta débito")) {
                            TarjetaDebito = Double.parseDouble(cantPago);
                        }

                        if (tipo_pago.equals("Efectivo")) {
                            PagosEfectivo += Double.parseDouble(cantPago);
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


                    double totalvalida = totalsumaimportes - PagosEfectivo;


                    if (totalvalida > TotalFormat) {
                        Toast toast1 =
                                Toast.makeText(getContext(),
                                        "la suma de los montos de medios electronicos o tarjetas no puede sobrepasar el total de la venta",
                                        Toast.LENGTH_LONG);
                        toast1.show();

                        return;
                    }

                    dialog.dismiss();

                    double valorTarjetas = 0;

                    valorTarjetas = TarjetaCredito + TarjetaDebito;

                    if (valorTarjetas > 0) {

                        //Parte Bundle
                        Intent myIntent = new Intent(getActivity(), Feenicia_Transaction_Bluetooth.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putDouble("TC", TarjetaCredito);
                        mBundle.putDouble("TD", TarjetaDebito);
                        mBundle.putString("Ticket", TicketIDVenta);
                        mBundle.putString("Sucursal", ticket_de_venta.getTic_id_sucursal());
                        mBundle.putInt("03meses", Resptresmeses);
                        mBundle.putInt("06meses", Respseismeses);
                        mBundle.putInt("09meses", Respnuevemeses);
                        mBundle.putInt("12meses", Respdocemeses);
                        mBundle.putString("TipoVenta","Venta_Normal");
                        mBundle.putString("Code",code);

                        mBundle.putInt("Tamano", ListaDePagos_a_utilizar.size());

                        for (int i = 0; i < ListaDePagos_a_utilizar.size(); i++) {
                            mBundle.putInt("fpa_id" + i, Integer.parseInt(ListaDePagos_a_utilizar.get(i).getId()));
                            mBundle.putString("valor" + i, ListaDePagos_a_utilizar.get(i).getCantidad());
                        }

                        myIntent.putExtras(mBundle);
                        getActivity().startActivity(myIntent);


                    } else {
                        dialog.dismiss();
                        dialog.setContentView(R.layout.pop_up_ventas_confirmacion_venta);
                        dialog.show();
                        progressDialog.show();

                        Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                        cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                loadTicket();
                                dialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                                bundle.putBoolean("Transacciones", Transacciones);
                                bundle.putBoolean("Comision", Comision);
                                bundle.putBoolean("Conte_Caja", Conte_Caja);
                                bundle.putString("tic_id", "");
                                bundle.putString("suc_id", "");
                                bundle.putString("apa_id", "");
                                Fragment_Ventas fragment2 = new Fragment_Ventas();
                                fragment2.setArguments(bundle);
                                fr.replace(R.id.fragment_container, fragment2).commit();


                            }
                        });

                        TextView importe_venta = dialog.findViewById(R.id.importe_venta);
                        TextView importe_recibido = dialog.findViewById(R.id.importe_recibido);
                        TextView importe_cambio = dialog.findViewById(R.id.importe_cambio);
                        FinalizarTicketParcial(importe_cambio, importe_recibido, importe_venta);


                        Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressDialog.show();
                                loadTicket();
                                dialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                                bundle.putBoolean("Transacciones", Transacciones);
                                bundle.putBoolean("Comision", Comision);
                                bundle.putBoolean("Conte_Caja", Conte_Caja);
                                bundle.putString("tic_id", "");
                                bundle.putString("suc_id", "");
                                bundle.putString("apa_id", "");
                                Fragment_Ventas fragment2 = new Fragment_Ventas();
                                fragment2.setArguments(bundle);
                                fr.replace(R.id.fragment_container, fragment2).commit();


                            }
                        });

                    }
                }

            }
        });
    }

    public void PagoParcialFacturar()
    {
        dialog.setContentView(R.layout.pop_up_ventas_metodo_pago);
        dialog.setCanceledOnTouchOutside(false);
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
        TextView importe_recibido = dialog.findViewById(R.id.text_view_recibido);
        TextView importe_faltante = dialog.findViewById(R.id.text_view_faltante);
        CargaMetodosPago(importe_faltante,importe_recibido);

        TextView TotalAPagar = dialog.findViewById(R.id.total_a_pagar);
        double ImporteTotalConDecimal = Double.parseDouble(ticket_de_venta.getTic_importe_total());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        TotalAPagar.setText(formatter.format(ImporteTotalConDecimal));

        Button realizarPago = dialog.findViewById(R.id.realizar_Pago);
        realizarPago.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!ListaDePagos_a_utilizar.isEmpty())
                {
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
                    //toast2.show();

                    if (totalvalida > TotalFormat){
                        Toast toast1 =
                                Toast.makeText(getContext(),
                                        "la suma de los montos de medios electronicos o tarjetas no puede sobrepasar el total de la venta",
                                        Toast.LENGTH_LONG);
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
                        mBundle.putInt( "03meses",Resptresmeses);
                        mBundle.putInt( "06meses",Respseismeses);
                        mBundle.putInt( "09meses",Respnuevemeses);
                        mBundle.putInt( "12meses",Respdocemeses);
                        mBundle.putString("TipoVenta","Venta_Normal");
                        mBundle.putString("Code",code);


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
                        progressDialog.show();

                        Button cerrarPopUp = dialog.findViewById(R.id.btnSalir3);
                        cerrarPopUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadTicket();
                                dialog.hide();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                                bundle.putBoolean("Transacciones", Transacciones);
                                bundle.putBoolean("Comision", Comision);
                                bundle.putBoolean("Conte_Caja", Conte_Caja);
                                bundle.putString("tic_id", "");
                                bundle.putString("suc_id", "");
                                bundle.putString("apa_id", "");
                                Fragment_Ventas fragment2 = new Fragment_Ventas();
                                fragment2.setArguments(bundle);
                                fr.replace(R.id.fragment_container,fragment2).commit();

                            }
                        });

                        TextView importe_venta = dialog.findViewById(R.id.importe_venta);
                        TextView importe_recibido = dialog.findViewById(R.id.importe_recibido);
                        TextView importe_cambio = dialog.findViewById(R.id.importe_cambio);
                        FinalizarTicketParcial(importe_cambio, importe_recibido, importe_venta);


                        Button aceptar = dialog.findViewById(R.id.aceptar_cerrar_ventana);
                        aceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressDialog.show();
                                loadTicket();
                                dialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("Proceso_Venta", Proceso_Venta);
                                bundle.putBoolean("Transacciones", Transacciones);
                                bundle.putBoolean("Comision", Comision);
                                bundle.putBoolean("Conte_Caja", Conte_Caja);
                                bundle.putString("tic_id", "");
                                bundle.putString("suc_id", "");
                                bundle.putString("apa_id", "");
                                Fragment_Ventas fragment2 = new Fragment_Ventas();
                                fragment2.setArguments(bundle);
                                fr.replace(R.id.fragment_container,fragment2).commit();
                            }
                        });
                    }
                }
            }
        });
    }

    public void VerificarParcialidades()
    {
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/cuenta-con-pagos-parciales";

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
                        JSONObject Resultado = response.getJSONObject("resultado");
                        String valor = Resultado.getString("cbn_cxc");
                        if (valor.equals("true"))
                        {
                            ParcialidadesBoleano = true;
                        }
                        else
                        {
                            ParcialidadesBoleano = false;
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

    public void SpinnerClientes(Spinner SpinnerClientes)
    {
        ClientesName.clear();
        ClientesID.clear();
        progressDialog.show();
        JSONObject request = new JSONObject();
        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("usu_suc", SucursalID.get(SpinnerSucursal.getSelectedItemPosition()));
            request.put("code",code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/clientes/index_app";

        //clienteAdapter.clear();

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
                            String UUIDcliente = ElementoUsuario.getString("uuid");
                            String nombre = elemento.getString("cli_nombre");
                            ClientesName.add(nombre);
                            ClientesID.add(UUIDcliente);
                        }
                        SpinnerClientes.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ClientesName));
                        progressDialog.dismiss();

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

    private void FinalizarTicketParcial(final TextView importeCambio, final TextView importeRecibido, final TextView importeVenta) {

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
            request.put("code", code);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/ventas/tickets/pagar-ticket-en-parcialidades";

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

                        if(!ticket_de_venta.getTic_apa_id().equals(""))
                        {
                            ConcluirApartado(ticket_de_venta.getTic_apa_id());
                        }

                        ArticulosVenta.clear();
                        Imagenes.clear();
                        final VentaArticuloAdapter articuloAdapter = new VentaArticuloAdapter(getContext(), ArticulosVenta, tabla_venta_articulos, ticket_de_venta, usu_id,
                                total, descuento, impuesto, subtotal,
                                carouselView, Imagenes,ImpuestosDeArticuloApartado,ListaDeArticulosApartados,ImpuestosDeArticuloOrdenado,ListaDeArticulosOrdenados,btn_ordenar,btn_apartar,btn_finalizar,
                                RecyclerImpuesto);
                        articuloAdapter.notifyDataSetChanged();
                        tabla_venta_articulos.setDataAdapter(articuloAdapter);
                        LoadImages();


                        InstanciarTicket();

                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast toast1 =
                                Toast.makeText(getContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    }

                } catch (JSONException e) {
                    Toast toast1 =
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                    progressDialog.dismiss();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                        progressDialog.dismiss();
                    }
                }
        );
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);
    }





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
