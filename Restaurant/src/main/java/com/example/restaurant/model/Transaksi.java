package com.example.restaurant.model;

public class Transaksi {
    Integer qty,id;
    String menu, total;

    public Transaksi(Integer qty, Integer id, String total, String menu) {
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

    public String getTotal() {
        return total;
    }

    public String getMenu() {
        return menu;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
