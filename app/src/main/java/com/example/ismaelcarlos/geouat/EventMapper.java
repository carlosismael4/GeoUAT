package com.example.ismaelcarlos.geouat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ismael Carlos on 2/20/2017.
 */

public class EventMapper implements Serializable {

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("idEvent")
    private int idEvent;

    @SerializedName("snippet")
    private String snippet;

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }
}
