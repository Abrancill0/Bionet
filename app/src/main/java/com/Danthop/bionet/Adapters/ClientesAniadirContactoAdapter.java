package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.Danthop.bionet.model.PagoModel;
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
import org.w3c.dom.Text;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;


public class ClientesAniadirContactoAdapter extends LongPressAwareTableDataAdapter<ContactoModel>{

    int TEXT_SIZE = 16;
    private SortableAniadirContactoTable tabla_contactos;

    private List<ContactoModel> clientesContactosList;

    private List<ContactoModel> clientesContactosListFull = new ArrayList<>();

    private Typeface s;
    private FragmentTransaction fr;



    private Button btn_nuevo_contacto;

    private SortableAniadirContactoTable table;
    private String Valorcheck[] = new String[30];

    private List<ContactoModel> ContactosAguardar;

    private ClientesAniadirContactoAdapter adapter;


    private List<EditText> ListaDeNombresContactos = new ArrayList<>();
    private List<EditText> ListaDeTelefonosContactos = new ArrayList<>();
    private List<EditText> ListaDeCorreosContactos = new ArrayList<>();
    private List<EditText> ListaDePuestosContactos = new ArrayList<>();
    private List<EditText> ListaDeNotasContactos = new ArrayList<>();




    private String UsuarioID;

    public ClientesAniadirContactoAdapter(final Context context, final List<ContactoModel> data, final SortableAniadirContactoTable tableView,
                                          Button Btn_Nuevo_Contacto,
                                          List<ContactoModel> contactosAguardar) {
        super(context, data, tableView);

        if (tabla_contactos ==null ){
            tabla_contactos = tableView;
        }

        clientesContactosList = data;
        table =tableView;

        btn_nuevo_contacto = Btn_Nuevo_Contacto;
        ContactosAguardar=contactosAguardar;

        adapter = this;
        LoadButtonMas();

    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ContactoModel contacto = getRowData(rowIndex);
        View renderedView = null;


        switch (columnIndex) {
            case 0:
                renderedView = renderContacto(contacto,rowIndex);
                break;
            case 1:
                renderedView = renderTelefono(contacto, rowIndex);
                break;
            case 2:
                renderedView = renderCorreoElectronico(contacto, rowIndex);
                break;
            case 3:
                renderedView = renderPuesto(contacto, rowIndex);
                break;
            case 4:
                renderedView = renderNotas(contacto, rowIndex);
                break;
            case 5:
                renderedView = renderSeleccionar(contacto,
                        ListaDeNombresContactos.get( rowIndex ),
                        ListaDeTelefonosContactos.get( rowIndex ),
                        ListaDeCorreosContactos.get( rowIndex ),
                        ListaDePuestosContactos.get( rowIndex ),
                        ListaDeNotasContactos.get( rowIndex ),
                        rowIndex);
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

    private View renderContacto(final ContactoModel contacto, int rowindex) {

        EditText Contacto = new EditText(getContext());
        if(clientesContactosList.get(rowindex).getContacto().equals(""))
        {

            ListaDeNombresContactos.add(Contacto);
            return Contacto;
        }
        ListaDeNombresContactos.add(Contacto);
        Contacto.setText(contacto.getContacto());
        Contacto.setEnabled(false);
        return Contacto;


    }

    private View renderTelefono(final ContactoModel contacto, int rowindex) {

        EditText Telefono = new EditText(getContext());
        Telefono.setInputType(InputType.TYPE_CLASS_PHONE);
        Telefono.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        if(clientesContactosList.get(rowindex).getContacto().equals(""))
        {
            ListaDeTelefonosContactos.add(Telefono);
            return Telefono;
        }
        ListaDeTelefonosContactos.add(Telefono);
        Telefono.setText(contacto.getTelefono());
        Telefono.setEnabled(false);
        return Telefono;

    }

    private View renderCorreoElectronico(final ContactoModel contacto, int rowindex) {

        EditText Correo = new EditText(getContext());
        Correo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        if(clientesContactosList.get(rowindex).getContacto().equals(""))
        {

            ListaDeCorreosContactos.add(Correo);
            return Correo;
        }
        ListaDeCorreosContactos.add(Correo);
        Correo.setText(contacto.getCorreo_electronico());
        Correo.setEnabled(false);
        return Correo;

    }

    private View renderPuesto(final ContactoModel contacto, int rowindex) {


        EditText Puesto = new EditText(getContext());
        if(clientesContactosList.get(rowindex).getContacto().equals(""))
        {
            ListaDePuestosContactos.add(Puesto);
            return Puesto;
        }

        ListaDePuestosContactos.add(Puesto);
        Puesto.setText(contacto.getPuesto());
        Puesto.setEnabled(false);
        return Puesto;
    }

    private View renderNotas(final ContactoModel contacto, int rowindex) {

        EditText Notas = new EditText(getContext());
        if(clientesContactosList.get(rowindex).getContacto().equals(""))
        {
            ListaDeNotasContactos.add(Notas);
            return Notas;
        }

        ListaDeNotasContactos.add(Notas);
        Notas.setText(contacto.getNotas());
        Notas.setEnabled(false);
        return Notas;

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

    private void LoadButtonMas()
    {
        btn_nuevo_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaDeNombresContactos.clear();
                ListaDeTelefonosContactos.clear();
                ListaDeCorreosContactos.clear();
                ListaDePuestosContactos.clear();
                ListaDeNotasContactos.clear();

                ContactoModel contacto = new ContactoModel( "","","","","" ,"");
                clientesContactosList.add( contacto );
                table.setDataAdapter(adapter);

            }
        });
    }


    private View renderSeleccionar(final ContactoModel forma,
                                   final EditText NombreContacto,
                                   final EditText Telefono,
                                   final EditText Correo,
                                   final EditText Puesto,
                                   final EditText Notas,
                                   int Indice) {
        final CheckBox seleccionar = new CheckBox(getContext());
        seleccionar.setGravity(Gravity.CENTER);
        seleccionar.setPadding(10,10,10,10);

        if (Valorcheck[Indice]!=null)
        {
            boolean variable = Boolean.parseBoolean( Valorcheck[Indice] );

            seleccionar.setChecked( variable );

        }

        seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (seleccionar.isChecked())
                {
                    if(NombreContacto.getText().toString().length()==0||Telefono.getText().toString().length()==0||Correo.getText().toString().length()==0
                            ||Puesto.getText().toString().length()==0)
                    {
                        seleccionar.setChecked(false);
                        Toast toast = Toast.makeText(getContext(), "Los campos contacto, teléfono, correo eléctronico y puesto son obligatorios", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else
                    {
                        NombreContacto.setEnabled(false);
                        Telefono.setEnabled(false);
                        Correo.setEnabled(false);
                        Puesto.setEnabled(false);
                        Notas.setEnabled(false);

                        ContactoModel contacto = new ContactoModel(
                                "",
                                NombreContacto.getText().toString(),
                                Telefono.getText().toString(),
                                Correo.getText().toString(),
                                Puesto.getText().toString(),
                                Notas.getText().toString()
                        );
                        ContactosAguardar.add(contacto);
                        clientesContactosList.set(Indice,contacto);
                        Valorcheck[Indice]="true";
                    }
                }else
                {
                    NombreContacto.setEnabled(true);
                    Telefono.setEnabled(true);
                    Correo.setEnabled(true);
                    Puesto.setEnabled(true);
                    Notas.setEnabled(true);

                    for(int i=0;i<ContactosAguardar.size();i++)
                    {
                        if(forma.getCorreo_electronico().equals(ContactosAguardar.get(i).getCorreo_electronico()))
                        {
                            ContactosAguardar.remove(i);
                        }

                    }
                    Valorcheck[Indice]="false";
                }

            }
        });

        return seleccionar;
    }
}