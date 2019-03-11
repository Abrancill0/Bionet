package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortablePreguntasTable;
import com.Danthop.bionet.model.Preguntas_Model;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class PreguntasAdapter extends LongPressAwareTableDataAdapter<Preguntas_Model> {

    int TEXT_SIZE = 12;
    public Dialog pop_up1;
    private TextView Respuesta;
    ProgressDialog progreso;

    public PreguntasAdapter(final Context context, final List<Preguntas_Model> data, final SortablePreguntasTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Preguntas_Model pregunta = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderPregunta(pregunta);
                break;
            case 1:
                renderedView = renderComprador(pregunta);
                break;
            case 2:
                renderedView = renderTitulo(pregunta);
                break;
            case 3:
                renderedView = renderButton1(pregunta);
                break;
            case 4:
                renderedView = renderButton2(pregunta);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Preguntas_Model pregunta = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(pregunta);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final Preguntas_Model pregunta) {
        final EditText editText = new EditText(getContext());
        editText.setText(pregunta.getComprador());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new OrdenNameUpdater(pregunta));
        return editText;
    }

    private View renderPregunta(final Preguntas_Model pregunta) {
        return renderString(pregunta.getPreguntas());
    }
    private View renderComprador(final Preguntas_Model pregunta) {
        return renderString(pregunta.getComprador());
    }
    private View renderTitulo(final Preguntas_Model pregunta) {
        return renderString(pregunta.getTitulo());
    }
    private View renderButton1(final Preguntas_Model pregunta) {
        return ButtonUno(pregunta);
    }
    private View renderButton2(final Preguntas_Model pregunta) {
        return ButtonDos(pregunta);
    }

    private View ButtonUno(final Preguntas_Model pregunta){
        final Button btn = new Button(getContext());
        btn.setText("Responder");
        btn.setPadding(20, 10, 20, 10);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                pop_up1=new Dialog(getContext());
                pop_up1.setContentView(R.layout.pop_up_ecommerce_contestar_pregunta );
                pop_up1.show();

                TextView ArticuloPregunta = pop_up1.findViewById(R.id.text_pregunta_articulo);
                TextView Pregunta = pop_up1.findViewById(R.id.text_pregunta);
                Respuesta = pop_up1.findViewById(R.id.text_respuesta);
                Button BntContestar = pop_up1.findViewById(R.id.btcontestar);
                Button Btncerrar = pop_up1.findViewById(R.id.btpreguntacerrar);

                ArticuloPregunta.setText( "Mensaje por " + pregunta.getTitulo() );
                Pregunta.setText( pregunta.getPreguntas() );

                BntContestar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Contestapregunta(pregunta.getidpregunta(),pregunta.gettoken(), String.valueOf( Respuesta.getText() ) );

                        pop_up1.hide();
                    }

                });

                Btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop_up1.hide();
                    }

                });

            }
        });
        return btn;
    }

    private View ButtonDos (final Preguntas_Model pregunta){
        final Button btn = new Button(getContext());
        btn.setText("Eliminar");
        btn.setPadding(20, 10, 20, 10);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                //pop_up_ecommerce_elimina_pregunta

                EliminaPregunta(pregunta.getidpregunta(),pregunta.gettoken());

            }
        });
        return btn;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private void EliminaPregunta(String itemid,String token)
    {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Eliminando pregunta...");
        progreso.show();

        try {
            final String url = "https://api.mercadolibre.com/questions/"+ itemid + "?access_token="+ token;

            // prepare the Request
            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.DELETE, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            String.valueOf("Pregunta eliminada correctamente"), Toast.LENGTH_LONG);
                            toast1.show();

                            progreso.hide();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast toast1 =
                                    Toast.makeText(getContext(),
                                            error.toString(), Toast.LENGTH_LONG);

                            toast1.show();

                            progreso.hide();
                        }
                    }
            );

            VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(getRequest);


        } catch (Error e) {
            Toast toast1 =
                    Toast.makeText(getContext(),
                            e.toString(), Toast.LENGTH_LONG);
            toast1.show();

            progreso.hide();

        }


    }

    private void Contestapregunta(String itemid,String token,String Respuesta)
    {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Respondiendo pregunta...");
        progreso.show();

        JSONObject request = new JSONObject();
        try
        {
            request.put("question_id", itemid);
            request.put("text", Respuesta);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        String ApiPath = "https://api.mercadolibre.com/answers?access_token=" + token;

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, ApiPath,request, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {


                Toast toast1 =
                        Toast.makeText(getContext(),
                                String.valueOf("Pregunta contestada correctamente"), Toast.LENGTH_LONG);

                    toast1.show();

                progreso.hide();
            }

        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast toast1 =
                                Toast.makeText(getContext(),
                                        "Error de conexion", Toast.LENGTH_SHORT);

                        toast1.show();

                        progreso.hide();

                    }
                }
        );

        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(postRequest);

    }


    private static class OrdenNameUpdater implements TextWatcher {

        private Preguntas_Model ordenToUpdate;

        public OrdenNameUpdater(Preguntas_Model ordenToUpdate) {
            this.ordenToUpdate = ordenToUpdate;
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
            ordenToUpdate.setPreguntas(s.toString());
        }
    }}





