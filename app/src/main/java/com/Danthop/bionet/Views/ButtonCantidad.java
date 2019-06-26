package com.Danthop.bionet.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.Danthop.bionet.R;

/**
 * TODO: document your custom view class.
 */
public class ButtonCantidad extends LinearLayout {

    private Button  menos,mas;
    private EditText cantidad;
    private OnCustomEventListener mListener;

    public ButtonCantidad(Context context) {
        this(context,null);
    }

    public ButtonCantidad(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ButtonCantidad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ButtonCantidad(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    /**
     * Initialize view
     */
    private void init(){

        //Inflate xml resource, pass "this" as the parent, we use <merge> tag in xml to avoid
        //redundant parent, otherwise a LinearLayout will be added to this LinearLayout ending up
        //with two view groups
        inflate(getContext(),R.layout.sample_button_cantidad,this);

        //Get references to text views
        menos  = (Button)findViewById(R.id.menos);
        cantidad = (EditText) findViewById(R.id.cantidad);
        mas = (Button)findViewById(R.id.mas);

        cantidad.setText("0");


        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restar();
            }
        });








    }


    public void restar()
    {
        String cantidad_string = String.valueOf(cantidad.getText());
        int cantidad_entera = Integer.parseInt(cantidad_string);

        if(cantidad_entera==0){

        }
        else{
            cantidad_entera = cantidad_entera -1;
            cantidad_string = String.valueOf(cantidad_entera);
            cantidad.setText(cantidad_string);
        }
    }

    public String getCantidad()
    {
        return String.valueOf(cantidad.getText());
    }

    public void setNumber(String numero)
    {
        cantidad.setText(numero);
    }

    public interface OnCustomEventListener {
        void onEvent();
    }

    public void setOnSumarCantidad(OnCustomEventListener eventListener) {
        mListener = eventListener;

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad_string = String.valueOf(cantidad.getText());
                int cantidad_entera = Integer.parseInt(cantidad_string);
                cantidad_entera = cantidad_entera +1;
                cantidad_string = String.valueOf(cantidad_entera);
                cantidad.setText(cantidad_string);
                if(mListener!=null)
                    mListener.onEvent();
            }
        });
    }


    public void setOnRestarCantidad (OnCustomEventListener eventListener){
        mListener = eventListener;

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad_string = String.valueOf(cantidad.getText());
                int cantidad_entera = Integer.parseInt(cantidad_string);

                if(cantidad_entera==0){

                }
                else{
                    cantidad_entera = cantidad_entera -1;
                    cantidad_string = String.valueOf(cantidad_entera);
                    cantidad.setText(cantidad_string);
                }
                if(mListener!=null)
                    mListener.onEvent();
            }
        });
    }

    public void setOnCambiarAMano(OnCustomEventListener eventListener) {
        mListener = eventListener;

        cantidad.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(mListener!=null)
                        mListener.onEvent();
                    return true;
                }
                return false;
            }
        });

    }




}