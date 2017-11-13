package com.linder.logginsemana14.model;

/**
 * Created by linderhassinger on 11/13/17.
 */

public class Denuncia {

    private Integer idd;
    private String titulo;
    private String usuario;
    private String direccion;
    private float lat;
    private float lng;
    private String foto;

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Denuncia{" +
                "idd=" + idd +
                ", titulo='" + titulo + '\'' +
                ", usuario='" + usuario + '\'' +
                ", direccion='" + direccion + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", foto='" + foto + '\'' +
                '}';
    }
}
