package com.Danthop.bionet;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/*  DEPENDENCIAS DE FEENICIA */
//import com.Danthop.bionet.Android.BTReceiverConnection;
import com.Danthop.bionet.Adapters.VentaArticuloAdapter;
import com.Danthop.bionet.BTReceiverConnection;
import com.Danthop.bionet.SaleConfiguration;
import com.Danthop.bionet.SaleUtils;
import com.Danthop.bionet.SppHandlerConnection;
import com.Danthop.bionet.core.dto.ResponseCode;
import com.Danthop.bionet.core.dto.SendReceiptResponse;

import com.Danthop.bionet.core.models.FeeniciaCredentials;
import com.Danthop.bionet.model.PagoModel;
import com.Danthop.bionet.model.TicketModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.imagpay.Settings;
import com.imagpay.spp.BTReceiver;
import com.imagpay.spp.SppHandler;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Feenicia on 05/10/2018.
 */

/// Lector Bluetooth ///
public class Feenicia_Transaction_Bluetooth extends AppCompatActivity {
    private Toolbar toolbar;
    private static TextView tvMontoMostrar;
    private static AppCompatActivity FEENICIA_TRANSACCION;
    private static TextView textViewTransaccion;
    static double monto = 0, tip = 0;
    static Integer msi = 0;
    private static Dialog dialog;

    //VARIABLE EVM
    private SppHandler handlerEVM;
    private BTReceiver _receiver;
    private Settings settings;
    private Handler ui;
    private String usu_id;
    private List<PagoModel> ListaDePagos_a_utilizar;
    private List<String> entriesList;
    private TicketModel ticket_de_venta;

    private String RequestIv="";
    private String RequestKey="";
    private String RequestSignatureIv="";
    private String RequestSignatureKey="";
    private String ResponseIv="";
    private String ResponseKey="";
    private String ResponseSignatureIv="";
    private String ResponseSignatureKey="";
    private String afiliacion="";
    private String merchantId="";
    private String userId="";



    private String code;

    private static SaleUtils utils;

    Intent intent;

    //// JAR ////
    Button btn_connect, btn_desconectar;  /// CONECTAR
    private static Button btn_cerrar, btn_terminar_feenicia;
    Button btn_search_devices; /// BUSCAR DISPOSITIVOS
    TextView textView_Devices;  // DISPOSITIVOS ENCONTRADOS
    static TextView textView_resultTx; // RESULTADO DE LA TRANSACCION
    ArrayList<BluetoothDevice> adapter;
    Spinner sp_sale;
    static ProgressWheel progressBar;

    // static CircleProgressView progressBar;

    SppHandlerConnection sppHandlerConnection;
    BTReceiverConnection bTReceiverConnection;

    SaleConfiguration sale = new SaleConfiguration();

    FeeniciaCredentials credentials = new FeeniciaCredentials();

    public static String user;  // USER LOGIN
    public static String pwd;   // PASSWORD LOGIN
    public static Double TarjetaCredito;
    public static Double TarjetaDebio;
    public static String fpa_id;
    public static String valor;
    public static int tamano;
    public static String Ticket;
    public static String Sucursal;
    public static int Tresmeses;
    public static int Seismeses;
    public static int nuevemeses;
    public static int docemeses;
    public static int Contador;
    public static String Mensaje;
    public static String TipoVenta;

    private double ImporteCambio;
    private double ImporteRecibido;
    private double ImporteVenta;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FEENICIA_TRANSACCION = this;

        intent = getIntent();


        askForContactPermission();
        LoadKeys();
        Bundle bundle = getIntent().getExtras();
        //TarjetaCredito = getIntent().getExtras().getDouble("TC");
        TipoVenta = bundle.getString("TipoVenta");
        code = bundle.getString("Code");

        if (TipoVenta.equals("Venta_Normal")) {

            setContentView(R.layout.activity_procesamiento_pagos);

            tvMontoMostrar = (TextView) findViewById(R.id.tvMontoMostrar);
            textViewTransaccion = (TextView) findViewById(R.id.textView_procesando_transaccion);
            textView_resultTx = (TextView) findViewById(R.id.textView_resultTx);

            /// Jar ////
            btn_search_devices = (Button) findViewById(R.id.btn_search_devices);
            btn_connect = (Button) findViewById(R.id.btn_conectar);

            btn_terminar_feenicia = (Button) findViewById(R.id.btnTerminarFeenicia);
            btn_cerrar = (Button) findViewById(R.id.btnCerrar);
            btn_desconectar = (Button) findViewById(R.id.btn_desconectar);
            textView_Devices = (TextView) findViewById(R.id.textView_Devices);
            textView_Devices.setText("");
            sp_sale = (Spinner) findViewById(R.id.sp_sales);
            progressBar = (ProgressWheel) findViewById(R.id.progressBar);
            //Venta Normal
            TarjetaCredito = bundle.getDouble("TC");
            TarjetaDebio = bundle.getDouble("TD");
            tamano = bundle.getInt("Tamano");
            Ticket = bundle.getString("Ticket");
            Sucursal = bundle.getString("Sucursal");
            Tresmeses = bundle.getInt("03meses");
            Seismeses = bundle.getInt("06meses");
            nuevemeses = bundle.getInt("09meses");
            docemeses = bundle.getInt("12meses");


            List<String> entriesList = new ArrayList<>();

            try {

                Log.i("3 meses", String.valueOf(Tresmeses));
                Log.i("6 meses", String.valueOf(Seismeses));
                Log.i("9 meses", String.valueOf(nuevemeses));
                Log.i("12 meses", String.valueOf(docemeses));
            } catch (Exception e) {
                Log.e("Error feenicia bundle", e.toString());
            }


            ListaDePagos_a_utilizar = new ArrayList<>();

            //Llena Spinner en base a Arreglo declarado en String
            String[] entries = getResources().getStringArray(R.array.sale_array);

            //entriesList = new ArrayList<String>(Arrays.asList(entries));

            for (int i = 0; i < entries.length; i++) {

                switch (entries[i].toString()) {

                    case "Venta Retail - Sin propina":
                        entriesList.add(i, entries[i].toString());
                        break;
                    case "Venta Retail - Con propina":
                        entriesList.add(i, entries[i].toString());
                        break;
                    case "Venta MSI_03 - Sin propina":
                        if (Tresmeses == 1) {
                            entriesList.add(i, entries[i].toString());
                        }
                        break;
                    case "Venta MSI_03 - Con propina":
                        if (Tresmeses == 1) {
                            entriesList.add(i, entries[i].toString());
                        }
                        break;
                    case "Venta MSI_06 - Sin propina":
                        if (Seismeses == 1) {
                            entriesList.add(i, entries[i].toString());
                        }
                        break;
                    case "Venta MSI_06 - Con propina":
                        if (Seismeses == 1) {
                            entriesList.add(i, entries[i].toString());
                        }
                        break;
                    case "Venta MSI_09 - Sin propina":
                        if (nuevemeses == 1) {
                            entriesList.add(i, entries[i].toString());
                        }
                        break;
                    case "Venta MSI_09 - Con propina":
                        if (nuevemeses == 1) {
                            entriesList.add(i, entries[i].toString());
                        }
                        break;
                    case "Venta MSI_12 - Sin propina":
                        if (docemeses == 1) {
                            entriesList.add(i, entries[i].toString());
                        }
                        break;
                    case "Venta MSI_12 - Con propina":
                        if (docemeses == 1) {
                            entriesList.add(i, entries[i].toString());
                        }
                        break;
                    default:
                        // sentencias;
                        break;

                }

            }


            ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, entriesList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sp_sale.setAdapter(spinnerAdapter);

            for (int i = 0; i < tamano; i++) {

                fpa_id = bundle.getString("fpa_id" + i);
                valor = bundle.getString("valor" + i);

                PagoModel modelito = new PagoModel(
                        "",
                        fpa_id,
                        valor
                );
                ListaDePagos_a_utilizar.add(modelito);

            }

            double precio = 0;

            if (TarjetaCredito > 0) {
                precio = TarjetaCredito;
                Contador = 2;
                Mensaje = "Tarjeta de Credito";

                show_Toast("Coloque la tarjeta de credito para procesar el cobro");

                btn_terminar_feenicia.setEnabled(false);  /// CONECTAR
                btn_cerrar.setEnabled(true);
            } else {
                precio = TarjetaDebio;
                Contador = 1;
                Mensaje = "Tarjeta de Debito";
                show_Toast("Coloque la tarjeta de debito para procesar el cobro");

                btn_terminar_feenicia.setEnabled(false);  /// CONECTAR
                btn_cerrar.setEnabled(true);
            }

            SharedPreferences sharedPref = this.getSharedPreferences("DatosPersistentes", Context.MODE_PRIVATE);
            usu_id = sharedPref.getString("usu_id", "");

            dialog = new Dialog(this);

            utils = new SaleUtils();
            monto = precio;
            monto = utils.roundTwoDecimals(monto);

            tip = 1.00;
            tip = utils.roundTwoDecimals(tip);

            msi = 3;

            //DIBUJAMOS EL MONTO
            tvMontoMostrar.setText("$ " + monto + " MN");

            // PERMISO DE BLUETOOTH
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
                return;

            } else {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }
            }

            ///// LIBRERIA FEENICIA_BT //////////
            user = "Black_Orange";
            pwd = "Black*2019";

            // 01.- Generar Login para obtener credenciales
            sppHandlerConnection = new SppHandlerConnection();
            sppHandlerConnection.generateLogin(user, pwd);   // (Username,Password)


            handlerEVM = sppHandlerConnection.initialize(handlerEVM, settings, ui, getApplicationContext(), credentials);

            // 02.- Inicializar Conexión BT
            bTReceiverConnection = new BTReceiverConnection();
            bTReceiverConnection.initializeBT(this, "FNZA");

            _receiver = bTReceiverConnection.get_receiver();
            adapter = bTReceiverConnection.getAdapter();

            ////////////////////////////////////

            sale.config(sppHandlerConnection); // Inicializar
            //sale.
            // sale.sale_retail_without_tip(monto); // DEFAULT Monto (SIN MSI y SIN PROPINA

            // EVENTOS DE TIPO DE VENTA
            sp_sale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (position == 0) { // RETAIL - SIN PROPINA
                        sale.sale_retail_without_tip(monto);
                    } else if (position == 1) { // RETAIL - CON PROPINA
                        sale.sale_retail_with_tip(monto, tip);
                    } else if (position == 2) { // MSI_03 - SIN PROPINA
                        msi = 3;
                        sale.sale_msi_without_tip(monto, msi);
                    } else if (position == 3) { // MSI_03 - CON PROPINA
                        msi = 3;
                        sale.sale_msi_with_tip(monto, tip, msi);
                    } else if (position == 4) { // MSI_06 - SIN PROPINA
                        msi = 6;
                        sale.sale_msi_without_tip(monto, msi);
                    } else if (position == 5) { // MSI_06 - CON PROPINA
                        msi = 6;
                        sale.sale_msi_with_tip(monto, tip, msi);
                    } else if (position == 6) { // MSI_09 - SIN PROPINA
                        msi = 9;
                        sale.sale_msi_without_tip(monto, msi);
                    } else if (position == 7) { // MSI_09 - CON PROPINA
                        msi = 9;
                        sale.sale_msi_with_tip(monto, tip, msi);
                    } else if (position == 8) { // MSI_12 - SIN PROPINA
                        msi = 12;
                        sale.sale_msi_without_tip(monto, msi);
                    } else if (position == 9) { // MSI_12 - CON PROPINA
                        msi = 12;
                        sale.sale_msi_with_tip(monto, tip, msi);
                    }

                    Log.i("SppHandlerConnection", "MONTO: " + String.valueOf(sppHandlerConnection.getAmount()));
                    Log.i("SppHandlerConnection", "TIP: " + String.valueOf(sppHandlerConnection.getTip()));
                    Log.i("SppHandlerConnection", "MSI: " + String.valueOf(sppHandlerConnection.getMsi()));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Este metodo no se utilizada
                }

            });

        }
        else if (TipoVenta.equals("Servicios"))
        {
            LoadServicios();
            setContentView(R.layout.activity_procesamiento_pagos_servicio);

            tvMontoMostrar = (TextView) findViewById(R.id.tvMontoMostrar);
            textViewTransaccion = (TextView) findViewById(R.id.textView_procesando_transaccion);
            textView_resultTx = (TextView) findViewById(R.id.textView_resultTx);

            /// Jar ////
            btn_search_devices = (Button) findViewById(R.id.btn_search_devices);
            btn_connect = (Button) findViewById(R.id.btn_conectar);

            btn_terminar_feenicia = (Button) findViewById(R.id.btnTerminarFeenicia);
            btn_cerrar = (Button) findViewById(R.id.btnCerrar);
            btn_desconectar = (Button) findViewById(R.id.btn_desconectar);
            textView_Devices = (TextView) findViewById(R.id.textView_Devices);
            textView_Devices.setText("");
            sp_sale = (Spinner) findViewById(R.id.sp_sales);
            progressBar = (ProgressWheel) findViewById(R.id.progressBar);

            double precio = 0;


            // PERMISO DE BLUETOOTH
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
                return;

            } else {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }
            }

            ///// LIBRERIA FEENICIA_BT //////////
            user = "Black_Orange";
            pwd = "Black*2019";

            // 01.- Generar Login para obtener credenciales
            sppHandlerConnection = new SppHandlerConnection();
            sppHandlerConnection.generateLogin(user, pwd);   // (Username,Password)


            handlerEVM = sppHandlerConnection.initialize(handlerEVM, settings, ui, getApplicationContext(), credentials);

            // 02.- Inicializar Conexión BT
            bTReceiverConnection = new BTReceiverConnection();
            bTReceiverConnection.initializeBT(this, "FNZA");

            _receiver = bTReceiverConnection.get_receiver();
            adapter = bTReceiverConnection.getAdapter();

            ////////////////////////////////////

            sale.config(sppHandlerConnection); // Inicializar
            //sale.
            // sale.sale_retail_without_tip(monto); // DEFAULT Monto (SIN MSI y SIN PROPINA



        }
        //Venta de servicios


        btn_terminar_feenicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FinalizarTicket();

            }
        });


        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        /// BTN CONECTAR CON DISPOSITIVO BT FEENICIA ///
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (adapter.size() < 1) {
                            show_Toast(getResources().getString(R.string.textView_not_device));
                            setText(getResources().getString(R.string.text_feenicia_demo));
                            setTextTx("");
                            return;
                        }

                        Log.i("adapter", adapter.toString()); // Todos los dispositivos
                        final BluetoothDevice dev = adapter.get(0); /// Elejimos el primer device encontrado
                        Log.i("nameOfDevice", dev.getName());

                        // MENSAJE CONECTANDO
                        show_Toast(getResources().getString(R.string.textView_conectando_bluetooth));
                        Boolean resultConnection = sppHandlerConnection.connectDevice(dev);

                        if (resultConnection == true) { // SUCESS
                            setText(getResources().getString(R.string.textView_ejecutarDispositivo_bluetooth));
                            setTextTx("");
                            show_Toast(getResources().getString(R.string.text_conexion_sucess));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_connect.setVisibility(View.GONE);
                                    btn_desconectar.setVisibility(View.VISIBLE);
                                    textView_Devices.setVisibility(View.GONE);
                                    textView_resultTx.setVisibility(View.GONE);
                                    btn_search_devices.setEnabled(false);
                                }
                            });


                        } else if (resultConnection == false) // ERROR
                        {
                            show_Toast(getResources().getString(R.string.text_conexion_error));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_connect.setVisibility(View.VISIBLE);
                                    btn_desconectar.setVisibility(View.GONE);
                                    btn_search_devices.setEnabled(true);
                                    textView_resultTx.setVisibility(View.GONE);
                                    setText(getResources().getString(R.string.text_feenicia_demo));
                                    setTextTx("");
                                }
                            });


                        }


                        _receiver.stop();

                    }
                }).start();


            }
        });

        /// BTN DESCONECTAR DISPOSITIVO///
        btn_desconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_connect.setVisibility(View.VISIBLE);
                            btn_desconectar.setVisibility(View.GONE);
                            btn_search_devices.setEnabled(true);
                            textView_Devices.setText("");
                            setText(getResources().getString(R.string.text_feenicia_demo));
                            textView_resultTx.setVisibility(View.GONE);
                            setTextTx("");
                            adapter.clear(); // Limpiamos
                        }
                    });

                } catch (Exception e) {
                    Log.e("Error_desconectar", e.toString());
                }


            }
        });

        // BTN PARA BUSCAR TODOS LOS DISPOSITIVOS BLUETOOTH
        btn_search_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_connect.setVisibility(View.VISIBLE);
                        btn_desconectar.setVisibility(View.GONE);
                        setText(getResources().getString(R.string.text_feenicia_demo));
                        setTextTx("");
                    }
                });

                _receiver.start();

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    show_Toast(getResources().getString(R.string.text_busquedaFinalizada));
                                    Log.i("Devices_Found", adapter.toString());

                                    int size_devices = adapter.size();
                                    int x = 0;
                                    String devices = getResources().getString(R.string.text_devices_encontrados) + "\n\n";

                                    for (x = 0; x < size_devices; x++) {
                                        devices = devices + x + ") " + adapter.get(x).getName().toString() + "\n";
                                    }

                                    if (adapter.size() == 0) {
                                        devices = getResources().getString(R.string.text_not_devices_encontrados) + "\n\n";
                                    }

                                    textView_Devices.setText(devices);
                                    textView_Devices.setVisibility(View.VISIBLE);

                                }
                            }, 9000);
                        } catch (Exception e) {
                            Log.e("Exception: ", e.toString());
                        }

                    }
                });

                runOnUiThread(new Runnable() {
                    public void run() {
                        adapter.clear();
                        textView_Devices.setText("");
                        setTextTx("");
                        textView_Devices.setVisibility(View.GONE);
                        textView_resultTx.setVisibility(View.GONE);
                        // MENSAJE BUSCANDO
                        show_Toast(getResources().getString(R.string.textView_buscando_bluetooth));
                    }
                });


            }
        });


        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _receiver.registerReceiver();

        //Aqui termina
    }

    public static void resultLoginResponse(final ResponseCode response) {

        try {

            Log.i("LOGIN_RESULT: ", response.toString());

            FEENICIA_TRANSACCION.runOnUiThread(new Runnable() {
                public void run() {
                    String setValue = "ERROR AL INICIALIZAR TERMINAL\nVERIFIQUE CREDENCIALES";

                    /* SI LA RESPUESTA DEL SERVICIO FUE 00 */
                    if (response.getResponseCode() != null) {
                        if (!response.getResponseCode().equals("00")) {
                            textView_resultTx.setVisibility(View.VISIBLE);
                            setTextTx(setValue);

                        }

                    }


                    progressBar.setVisibility(View.GONE);

                }
            });

        } catch (Exception e) {
            Log.e("ERROR_TX", e.toString());
        }

    }

    public static void statusReaderCard(final String result) {
        Log.i("RESULTADO_READ_CARD: ", result);

        FEENICIA_TRANSACCION.runOnUiThread(new Runnable() {
            public void run() {

                textView_resultTx.setVisibility(View.GONE);

                if (result.equals("LEYENDO TARJETA")) {
                    textViewTransaccion.setText(FEENICIA_TRANSACCION.getResources().getString(R.string.msj_read_Card));
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    textViewTransaccion.setText(FEENICIA_TRANSACCION.getResources().getString(R.string.textView_ejecutarDispositivo_bluetooth));
                }

            }
        });
    }

    /* METODO EL CUAL REGRESA EL RESULTADO DE LA TX*/
    public static void resultOfTransaction(final ResponseCode response) {

        try {
            Log.i("RESULT_TX: ", response.toString());

            FEENICIA_TRANSACCION.runOnUiThread(new Runnable() {
                public void run() {
                    textView_resultTx.setVisibility(View.VISIBLE);
                    String setValue = FEENICIA_TRANSACCION.getResources().getString(R.string.msj_tx_result) + "\n";
                    setValue = setValue +
                            "\nresponseCode: " + response.getResponseCode() +
                            "\ndescription: " + response.getDescriptionCode();

                    /* SI LA RESPUESTA DEL SERVICIO FUE 00 */
                    if (response.getResponseCode() != null) {

                        if (response.getTransaction() != null) {
                            setValue = setValue + "\n\n" + response.getTransaction().toStringOrder();
                        }

                        if (response.getResponseCode().equals("00")) {


                            if (Contador == 2) {

                                if (TarjetaDebio > 0) {
                                    monto = TarjetaDebio;
                                    monto = utils.roundTwoDecimals(monto);

                                    tvMontoMostrar.setText("$ " + monto + " MN");

                                    Contador = 1;

                                    Toast.makeText(FEENICIA_TRANSACCION, "Cobro realizado correctamente", Toast.LENGTH_SHORT).show();

                                    Toast.makeText(FEENICIA_TRANSACCION, "Ahora coloque su tarjeta de debito", Toast.LENGTH_SHORT).show();

                                } else {
                                    monto = 0;
                                    monto = utils.roundTwoDecimals(monto);

                                    tvMontoMostrar.setText("$ " + monto + " MN");

                                    Contador = 0;
                                    Toast.makeText(FEENICIA_TRANSACCION, "Cobro realizado correctamente", Toast.LENGTH_SHORT).show();

                                    btn_terminar_feenicia.setEnabled(true);  /// CONECTAR
                                    btn_cerrar.setEnabled(false);


                                }

                                //Toast toast1 = Toast.makeText(context, "El siguiente pago es con tarjeta de debito.", Toast.LENGTH_SHORT);

                                // toast1.show();
                            } else if (Contador == 1) {
                                monto = 0;
                                monto = utils.roundTwoDecimals(monto);

                                tvMontoMostrar.setText("$ " + monto + " MN");

                                Contador = 0;
                                Toast.makeText(FEENICIA_TRANSACCION, "Cobro realizado correctamente", Toast.LENGTH_SHORT).show();


                                //  Toast toast1 = Toast.makeText(context, "Por favor cierre esta ventana para continuar", Toast.LENGTH_SHORT);

                                //  toast1.show();

                            }

                            /*** OPCIONAL ***/
                            /// ENVIAMOS EL RECIBO DE COMPRA /////

                            /**** DESCOMENTAR PARA ENVIAR RECIBO *****/
                            /*final SppHandlerConnection sppHandlerConnection = new SppHandlerConnection();
                            sppHandlerConnection.generateLogin(user,pwd);   // (Username,Password)

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sppHandlerConnection.sendReceipt(response.getTransaction().getReceiptId(),"feenicia@yopmail.com");
                                }
                            }, 3000);*/
                        }
                    }

                    setTextTx(setValue);
                    progressBar.setVisibility(View.GONE);

                }
            });

        } catch (Exception e) {
            Log.e("ERROR_TX", e.toString());
        } finally {
            textViewTransaccion.setText(FEENICIA_TRANSACCION.getResources().getString(R.string.msj_tx_finish));
        }

    }

    public static void onResultSendReceipt(SendReceiptResponse response) {
        if (response != null) {
            Log.i("RESPONSE_SEND_RECEIPT", response.toString());
        }

    }

    /* PINTA EN PANTALLA UN MENSAJE TOAST */
    public void show_Toast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FEENICIA_TRANSACCION, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //METODO PARA CAMBIAR LA LEYENDA DEL TEXTO QUE ESTA DEBAJO DEL PROGRESS BAR
    private void setText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewTransaccion.setText(text);
            }
        });
    }

    //METODO PARA MOSTRAR EL RESULTADO DE LA TRANSACCION
    private static void setTextTx(final String text) {
        FEENICIA_TRANSACCION.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView_resultTx.setText(text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        handlerEVM.close();
        _receiver.stop();
        super.onStop();
    }

    public void onDestroy() {
        _receiver.stop();
        _receiver.unregisterReceiver();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(FEENICIA_TRANSACCION)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.dialog_mensaje_venta_salir))
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.dialog_NO), new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setPositiveButton(R.string.dialog_SI, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int ubicacionPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            List<String> listPermissionsNeeded = new ArrayList<>();

            if (ubicacionPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
                return;
            }

        }
    }

    public static void onPercentageReceived(final int percentage) {
        FEENICIA_TRANSACCION.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // progressBar.setValueAnimated(percentage);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(FEENICIA_TRANSACCION, "Debes dar permisos de Bluetooth", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void FinalizarTicket() {

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
            request.put("tic_id", Ticket);
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

                        double CambioConDecimal = Double.parseDouble(tic_importe_cambio);
                        double RecibidoConDecimal = Double.parseDouble(tic_importe_recibido);
                        double ImporteTotalConDecimal = Double.parseDouble(tic_importe_total);
                        NumberFormat formatter = NumberFormat.getCurrencyInstance();

                        ImporteCambio = CambioConDecimal;
                        ImporteRecibido = RecibidoConDecimal;
                        ImporteVenta = ImporteTotalConDecimal;

                        Intent myIntent = new Intent(getApplicationContext(), Confirmacion_venta.class);

                        Bundle mBundle = new Bundle();
                        mBundle.putDouble("IC", ImporteCambio);
                        mBundle.putDouble("IR", ImporteRecibido);
                        mBundle.putDouble("IV", ImporteVenta);
                        mBundle.putString("Sucursal", Sucursal);
                        mBundle.putString("Ticket", Ticket);

                        myIntent.putExtras(mBundle);

                        getApplicationContext().startActivity(myIntent);

                        finish();


                    } else {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    }

                } catch (JSONException e) {
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);
    }

    private void LoadKeys()
    {
        JSONObject request = new JSONObject();

        try {
            request.put("usu_id", usu_id);
            request.put("esApp", "1");
            request.put("code", code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = url + "/api/select-configuracion-feenicia";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    int status = Integer.parseInt(response.getString("estatus"));
                    String Mensaje = response.getString("mensaje");

                    if (status == 1) {
                        JSONArray Resultado = response.getJSONArray("resultado");
                        for(int x =0; x<Resultado.length();x++)
                        {
                            JSONObject elemento = Resultado.getJSONObject(x);
                            String cfe_id = elemento.getString("cfe_id");

                            if(cfe_id.equals("RequestIv"))
                            {
                                RequestIv=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("RequestKey"))
                            {
                                RequestKey=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("RequestSignatureIv"))
                            {
                                RequestSignatureIv=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("RequestSignatureKey"))
                            {
                                RequestSignatureKey=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("ResponseIv"))
                            {
                                ResponseIv=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("ResponseSignatureIv"))
                            {
                                ResponseSignatureIv=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("ResponseSignatureKey"))
                            {
                                ResponseSignatureKey=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("afiliacion"))
                            {
                                afiliacion=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("merchantId"))
                            {
                                merchantId=elemento.getString("cfe_valor");
                            }
                            if(cfe_id.equals("userId"))
                            {
                                userId=elemento.getString("cfe_valor");
                            }
                        }

                    } else {

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), Mensaje, Toast.LENGTH_LONG);
                        toast1.show();
                    }

                } catch (JSONException e) {
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
        );
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);
    }

    private void LoadServicios()
    {
       /* */
        JSONObject request = new JSONObject();
        HashMap<String, String> headers=new HashMap<>();

        headers.put("Content-Type","application/json");
        headers.put("X-Requested-With","0000000000003728_d30a1dce920aa13d1832505aaa901bdfb56f8df52286397b104d9aa15bc152c33f0af6763b01a69ea1d0ce29c95ab74a14ee4170fb3d7fc49a55764bf9049c02e882f5b6fec215d42e85bade53a886d6");


        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = getString(R.string.Url);

        String ApiPath = "https://18.216.206.86:30080/hml-servicesV2/pagoServicios/categorias";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath, request, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                        toast1.show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }
        };
        postRequest.setShouldCache(false);
        VolleySingleton.getInstanciaVolley(this).addToRequestQueue(postRequest);

    }

}