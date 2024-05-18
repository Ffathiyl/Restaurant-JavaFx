package com.example.restaurant.model;

public class Transaksi {
    Integer qty,id;
    double total;
    String menu;

    public Transaksi(Integer qty, Integer id, double total, String menu) {
        this.qty = qty;
        this.id = id;
        this.total = total;
        this.menu = menu;
    }

    public Transaksi(){}

    public Integer getQty() {
        return qty;
    }

    public Integer getId() {
        return id;
    }

    public double getTotal() {
        return total;
    }

    public String getMenu() {
        return menu;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
