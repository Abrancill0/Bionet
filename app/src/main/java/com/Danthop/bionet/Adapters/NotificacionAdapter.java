package com.Danthop.bionet.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.Danthop.bionet.R;
import com.Danthop.bionet.model.NotificacionModel;

import java.util.ArrayList;
import java.util.List;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {
    private List<NotificacionModel> mNotificacionList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder{

        public TextView TituloTextView;
        public TextView FechaTextView;
        public TextView MensajeTextView;
        public CheckBox Box;

        public NotificacionViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            TituloTextView = itemView.findViewById(R.id.Titulo_notificacion);
            FechaTextView = itemView.findViewById(R.id.Fecha_notificacion);
            MensajeTextView = itemView.findViewById(R.id.Text_notificacion);
            Box = itemView.findViewById(R.id.checkBox);

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

            Box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if (listener != null){
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION){
                                listener.onDeleteClick(position);
                            }
                        }
                }
            });


        }
    }

    public  NotificacionAdapter(List<NotificacionModel> notificacionList){
        mNotificacionList = notificacionList;
    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.caja_notificacion, viewGroup, false);
        NotificacionViewHolder nth = new NotificacionViewHolder(v, mListener);
        return nth;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder notificacionViewHolder, int i) {
        NotificacionModel currentItem = mNotificacionList.get(i);

        notificacionViewHolder.TituloTextView.setText(currentItem.getTitulo());
        notificacionViewHolder.FechaTextView.setText(currentItem.getFecha());
        notificacionViewHolder.MensajeTextView.setText(currentItem.getMensaje());

    }

    @Override
    public int getItemCount() {
        return mNotificacionList.size();
    }


}
