package com.example.jl.bionet.Class;

import java.security.PublicKey;

public class HttpRequest<T> {

    public HttpRequest(){

        respuesta = null;

    }

    public void SetRespuesta(T nuevoValor){

        respuesta = nuevoValor;
    }

    public T getRespuesta(){





        return  respuesta;
    }

    private T respuesta;

}
