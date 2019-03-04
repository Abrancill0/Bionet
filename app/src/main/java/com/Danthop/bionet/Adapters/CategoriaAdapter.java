package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.CategoriaModel;

import java.util.ArrayList;


public class CategoriaAdapter extends ArrayAdapter<CategoriaModel> {

    private Context mContext;
    private int mResource;

    public CategoriaAdapter(Context context, int resource, ArrayList<CategoriaModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getName();
        String id = getItem(position).getId();

        CategoriaModel categoria = new CategoriaModel(name,id);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView nombre = (TextView) convertView.findViewById(R.id.TextCategoria);

        nombre.setText(name);

        return convertView;
    }
}

