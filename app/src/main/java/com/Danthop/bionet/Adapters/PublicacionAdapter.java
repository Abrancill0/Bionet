package com.Danthop.bionet.Adapters;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import com.Danthop.bionet.Fragment_ecommerce_Sincronizar_Articulos;
        import com.Danthop.bionet.Fragment_ecommerce_Sincronizar_Nuevo_Prod;
        import com.Danthop.bionet.Fragment_selecciona_categoria;
        import com.Danthop.bionet.R;
        import com.Danthop.bionet.model.CategoriaModel;
        import com.Danthop.bionet.model.PublicacionModel;
        import com.Danthop.bionet.model.VolleySingleton;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.error.VolleyError;
        import com.android.volley.request.JsonObjectRequest;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.util.ArrayList;

        import de.codecrafters.tableview.listeners.TableDataClickListener;

public class PublicacionAdapter extends ArrayAdapter<PublicacionModel> {

    private Context mContext;
    private int mResource;
    private TextView nombre;
    public String name;
    public String id;
    public ArrayList ex;
    private TextView NombreDialog;
    private String AccesToken;
    private String UserML;
    private Dialog dialog1;
    private Dialog pop_up_tipo_publicacion;
    public NameCategoriaSelcted mOnInputSelected;
    private ListView ListsPublicaciones;
    private ListView ListaPublicaciones;
    private Bundle bundle1;
    private String id_viejo1;
    private String Nombre;
    private String Descripcion;
    private String Precio;
    private String usu_id;
    private String Imagen1;
    private String Imagen2;
    FragmentTransaction fr;
    private Button atras;
    private String idcategoria;

    public interface NameCategoriaSelcted {
        void sendInput(String input);
    }


    public PublicacionAdapter(Context context, int resource, ArrayList<PublicacionModel> objects, ListView ListsPublicaciones,FragmentTransaction fg, Bundle bundle) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        pop_up_tipo_publicacion = new Dialog(getContext());
        ListaPublicaciones = ListsPublicaciones;
        bundle1 = bundle;
        fr = fg;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        name = getItem(position).getName();
        id = getItem(position).getId();
        ex  = getItem( position ).getExceptionsCategory();

        Nombre = bundle1.getString("nombre");
        Descripcion = bundle1.getString("descripcion");
        Precio = bundle1.getString("precio");
        usu_id = bundle1.getString("usu_id");
        Imagen1 = bundle1.getString("image1");
        Imagen2 = bundle1.getString("image2");

        PublicacionModel publicacion = new PublicacionModel(name, id,ex);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        nombre = convertView.findViewById(R.id.TextPublicacion);
        nombre.setText(name);

        bundle1.putString("name", name);
        bundle1.putString("id", id);
        bundle1.putStringArrayList("ex",  ex );

        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = getItem(position).getName();
                id = getItem(position).getId();
                ex  = getItem( position ).getExceptionsCategory();


                Fragment_selecciona_categoria secondFragment = new Fragment_selecciona_categoria();
                secondFragment.setArguments(bundle1);
                fr.replace(R.id.fragment_container, secondFragment).commit();
            }
        });

        return convertView;
    }
}
