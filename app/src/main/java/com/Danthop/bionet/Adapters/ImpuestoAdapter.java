package com.Danthop.bionet.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.Impuestos;
import com.Danthop.bionet.model.NotificacionModel;

import java.util.ArrayList;
import java.util.List;

public class ImpuestoAdapter extends RecyclerView.Adapter<ImpuestoAdapter.ImpuestoViewHolder> {
    private List<Impuestos> mImpuestosList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ImpuestoViewHolder extends RecyclerView.ViewHolder{

        public TextView ImpuestoNombre;
        public TextView ImpuestoValor;

        public ImpuestoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            ImpuestoNombre = itemView.findViewById(R.id.nombreImpuesto);
            ImpuestoValor = itemView.findViewById(R.id.valorImpuesto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });


        }
    }

    public  ImpuestoAdapter(List<Impuestos> notificacionList){
        mImpuestosList = notificacionList;
    }

    @Override
    public ImpuestoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.caja_impuesto, viewGroup, false);
        ImpuestoViewHolder nth = new ImpuestoViewHolder(v, mListener);
        return nth;
    }

    @Override
    public void onBindViewHolder(@NonNull ImpuestoViewHolder ImpuestoViewHolder, int i) {
        Impuestos currentItem = mImpuestosList.get(i);

        ImpuestoViewHolder.ImpuestoNombre.setText(currentItem.getNombreImpuesto());
        ImpuestoViewHolder.ImpuestoValor.setText(currentItem.getValorImpuesto());

    }

    @Override
    public int getItemCount() {
        return mImpuestosList.size();
    }


}
