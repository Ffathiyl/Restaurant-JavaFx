package com.example.restaurant.model;

public class Menu {

    Integer id,harga,status,jenis,stok;
    String nama,desc;

    public Menu(Integer id, Integer harga, Integer status, Integer jenis, Integer stok, String nama, String desc) {
        this.id = id;
        this.harga = harga;
        this.status = status;
        this.jenis = jenis;
        this.stok = stok;
        this.nama = nama;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public Integer getHarga() {
        return harga;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getJenis() {
        return jenis;
    }

    public Integer getStok() {
        return stok;
    }

    public String getNama() {
        return nama;
    }

    public String getDesc() {
        return desc;
    }
}
