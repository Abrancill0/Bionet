package com.Danthop.bionet.Adapters;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.Danthop.bionet.Fragment_ecommerce_Sincronizar_Articulos;
import com.Danthop.bionet.Fragment_ecommerce_Sincronizar_Nuevo_Prod;
import com.Danthop.bionet.R;
import com.Danthop.bionet.model.CategoriaModel;
import com.Danthop.bionet.model.PagoModel;
import com.Danthop.bionet.model.TicketModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoAdapter extends ArrayAdapter<PagoModel> {

    private Context mContext;
    private int mResource;
    private ListView ListaMetodos;

    private String id_pago;
    private String cantidad;
    private String nombre;

    private TextView NombrePago;
    private EditText CantidadDinero;
    private TicketModel Ticket;
    private List<PagoModel> MetodosDePago = new ArrayList<>();
    ;

    public interface NameMetodoSelected {
        void sendInput(String input);
    }

    public MetodoPagoAdapter(Context context, int resource, List<PagoModel> objects, ListView ListMetodos, TicketModel tick) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        ListaMetodos = ListMetodos;
        Ticket = tick;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        id_pago = getItem(position).getId();
        cantidad = getItem(position).getCantidad();
        nombre = getItem(position).getNombre();

        PagoModel pago = new PagoModel(id_pago, cantidad,nombre);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        NombrePago= convertView.findViewById(R.id.TextMetodo);
        NombrePago.setText(nombre);

        CantidadDinero = convertView.findViewById(R.id.TextCantidad);

        return convertView;
    }

    public String obtenerCantidad(int position)
    {
        getItem(position).setCantidad(String.valueOf(CantidadDinero.getText()));

        String Cantidad = getItem(position).getCantidad();

        return Cantidad;
    }
}