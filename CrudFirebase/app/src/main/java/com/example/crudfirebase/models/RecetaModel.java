package com.example.crudfirebase.models;

import java.io.Serializable;

public class RecetaModel implements Serializable {
    private String id;
    private String titulo;
    private String tiempo;
    private String receta;
    private String imagen;

    public RecetaModel() {
    }

    public RecetaModel(String id, String titulo, String tiempo, String receta, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.tiempo = tiempo;
        this.receta = receta;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getReceta() {
        return receta;
    }

    public void setReceta(String receta) {
        this.receta = receta;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
