package com.example.restaurant.model;

public class Laporan {
    String nama,tgl,kasir,menu,total,sub;
    Integer kuantitas,id;

    public Laporan(Integer id,String nama,String tanggal,String kasir, String total){
        this.id = id;
        this.nama = nama;
        this.tgl = tanggal;
        this.kasir = kasir;
        this.total = total;
    }

    public Laporan(String menu, Integer kuantitas, String sub){
        this.menu = menu;
        this.kuantitas = kuantitas;
        this.sub = sub;
    }

    public String getNama() {
        return nama;
    }

    public String getTgl() {
        return tgl;
    }

    public String getKasir() {
        return kasir;
    }

    public String getTotal() {
        return total;
    }

    public String getSub() {
        return sub;
    }

    public String getMenu() {
        return menu;
    }

    public Integer getKuantitas() {
        return kuantitas;
    }

    public Integer getId() {
        return id;
    }
}
