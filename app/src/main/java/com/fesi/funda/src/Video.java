package com.fesi.funda.src;

import java.io.Serializable;

public class Video implements Serializable {

    private String nombrevideo;
    private boolean videofavorito;
    private String descripcionvideo;
    private int numerolikes;
    private String linkvideo;

    public Video() {
    }

    public String getNombrevideo() {
        return nombrevideo;
    }

    public void setNombrevideo(String nombrevideo) {
        this.nombrevideo = nombrevideo;
    }

    public boolean isVideofavorito() {
        return videofavorito;
    }

    public void setVideofavorito(boolean videofavorito) {
        this.videofavorito = videofavorito;
    }

    public String getDescripcionvideo() {
        return descripcionvideo;
    }

    public void setDescripcionvideo(String descripcionvideo) {
        this.descripcionvideo = descripcionvideo;
    }

    public int getNumerolikes() {
        return numerolikes;
    }

    public void setNumerolikes(int numerolikes) {
        this.numerolikes = numerolikes;
    }

    public String getLinkvideo() {
        return linkvideo;
    }

    public void setLinkvideo(String linkvideo) {
        this.linkvideo = linkvideo;
    }
}
