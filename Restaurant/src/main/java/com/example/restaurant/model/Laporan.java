package com.example.restaurant.model;

public class Laporan {
    String nama,tgl,kasir,menu;
    double total,sub;
    Integer kuantitas,id;

    public Laporan(Integer id,String nama,String tanggal,String kasir, double total){
        this.id = id;
        this.nama = nama;
        this.tgl = tanggal;
        this.kasir = kasir;
        this.total = total;
    }

    public Laporan(String menu, Integer kuantitas, double sub){
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

    public double getTotal() {
        return total;
    }

    public double getSub() {
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
