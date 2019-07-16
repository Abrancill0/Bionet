package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.Fragment_crear_cliente;
import com.Danthop.bionet.Fragment_editarCliente;
import com.Danthop.bionet.Numero_sucursal;
import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableAniadirContactoTable;
import com.Danthop.bionet.Tables.SortableClienteContactos;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.CompraModel;
import com.Danthop.bionet.model.ContactoModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.sortabletableview.recyclerview.toolkit.FilterHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;


public class ClientesAniadirContactoAdapter extends LongPressAwareTableDataAdapter<ContactoModel>{

    int TEXT_SIZE = 16;
    private SortableAniadirContactoTable tabla_contactos;

    private List<ContactoModel> clientesContactosList;

    private List<ContactoModel> clientesContactosListFull = new ArrayList<>();

    private Typeface s;
    private FragmentTransaction fr;

    private String CadenaContacto="";
    private String CadenaTelefono="";
    private String CadenaCorreo="";
    private String CadenaPuesto="";
    private String CadenaNotas="";




    private String UsuarioID;

    public ClientesAniadirContactoAdapter(final Context context, final List<ContactoModel> data, final SortableAniadirContactoTable tableView, FragmentTransaction gr) {
        super(context, data, tableView);

        if (tabla_contactos ==null ){
            tabla_contactos = tableView;
        }
        fr = gr;

        clientesContactosList = data;
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ContactoModel contacto = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderContacto(contacto);
                break;
            case 1:
                renderedView = renderTelefono(contacto);
                break;
            case 2:
                renderedView = renderCorreoElectronico(contacto);
                break;
            case 3:
                renderedView = renderPuesto(contacto);
                break;
            case 4:
                renderedView = renderNotas(contacto);
                break;
            case 5:
                renderedView = renderMas(contacto);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ContactoModel cliente = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:

            default:

        }

        return renderedView;
    }


    private View renderEditableClienteName(final ClienteModel cliente) {
        final EditText editText = new EditText(getContext());
        editText.setText(cliente.getCliente_Nombre());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new ClienteNameUpdater(cliente));
        return editText;
    }

    private View renderContacto(final ContactoModel contacto) {
        EditText Contacto = new EditText(getContext());
        Contacto.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    CadenaContacto = Contacto.getText().toString();

                    return true;
                }
                return false;
            }
        });
        return Contacto;
    }

    private View renderTelefono(final ContactoModel contacto) {
        EditText Telefono = new EditText(getContext());
        Telefono.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    CadenaTelefono = Telefono.getText().toString();

                    return true;
                }
                return false;
            }
        });
        return Telefono;
    }

    private View renderCorreoElectronico(final ContactoModel contacto) {
        EditText Correo = new EditText(getContext());
        Correo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    CadenaCorreo = Correo.getText().toString();

                    return true;
                }
                return false;
            }
        });
        return Correo;
    }

    private View renderPuesto(final ContactoModel contacto) {
        EditText Puesto = new EditText(getContext());
        Puesto.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    CadenaPuesto = Puesto.getText().toString();

                    return true;
                }
                return false;
            }
        });
        return Puesto;
    }

    private View renderNotas(final ContactoModel contacto) {
        EditText Notas = new EditText(getContext());
        Notas.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    CadenaNotas = Notas.getText().toString();

                    return true;
                }
                return false;
            }
        });
        return Notas;
    }
    private View renderMas(final ContactoModel contacto) {
        Button Mas = new Button(getContext());
        Mas.setText("+");
        Mas.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ContactoModel contacto = new ContactoModel( "",CadenaContacto,CadenaTelefono,CadenaCorreo,CadenaPuesto ,CadenaNotas);
               clientesContactosListFull.add( contacto );
               CadenaContacto="";
               CadenaTelefono="";
               CadenaCorreo="";
               CadenaNotas="";
               CadenaPuesto="";
            }
        } );
        return Mas;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setTextSize(TEXT_SIZE);
        textView.setPadding(5,0,0,5);
        textView.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        return textView;
    }

    private static class ClienteNameUpdater implements TextWatcher {

        private ClienteModel clienteToUpdate;

        public ClienteNameUpdater(ClienteModel clienteToUpdate) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}