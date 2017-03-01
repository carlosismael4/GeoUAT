package com.example.ismaelcarlos.geouat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ismael Carlos on 2/20/2017.
 */

public class EventMapper implements Serializable {

    @SerializedName("Titulo")
    private String Titulo;

    @SerializedName("Descripcion")
    private String Descripcion;

    @SerializedName("IdEvento")
    private String IdEvent;

    @SerializedName("Snippet")
    private String Snippet;

    public String getSnippet() {
        return Snippet;
    }

    public void setSnippet(String snippet) {
        this.Snippet = snippet;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        this.Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    public String getIdEvent() {
        return IdEvent;
    }

    public void setIdEvent(String idEvent) {
        this.IdEvent = idEvent;
    }
}
