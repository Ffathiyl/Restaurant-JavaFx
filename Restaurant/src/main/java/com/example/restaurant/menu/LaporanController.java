package com.example.restaurant.menu;

import com.example.restaurant.database.DBConnect;
import com.example.restaurant.login.LoginController;
import com.example.restaurant.model.Laporan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class LaporanController implements Initializable {
    @FXML
    private TextField txtNama;
    String nama;
    @FXML
    private DatePicker datePick;
    @FXML
    private TableColumn<com.example.restaurant.model.Laporan,Integer> trId;
    @FXML
    private TableColumn<com.example.restaurant.model.Laporan,String> trNama;
    @FXML
    private TableColumn<com.example.restaurant.model.Laporan,String> trTanggal;
    @FXML
    private TableColumn<com.example.restaurant.model.Laporan,String> trKasir;
    @FXML
    private TableColumn<com.example.restaurant.model.Laporan,Double> trTotal;
    @FXML
    private TableColumn<com.example.restaurant.model.Laporan,Integer> dtMenu;
    @FXML
    private TableColumn<com.example.restaurant.model.Laporan,Integer> dtKuantitas;
    @FXML
    private TableColumn<com.example.restaurant.model.Laporan,Double> dtSub;
    @FXML
    private TableView TabelTransaksi;
    @FXML
    private TableView TabelDetail;
    @FXML
    private ObservableList<com.example.restaurant.model.Laporan> listTransaksi = FXCollections.observableArrayList();
    @FXML
    private ObservableList<com.example.restaurant.model.Laporan> listDetail = FXCollections.observableArrayList();
    @FXML
    private ObservableList<com.example.restaurant.model.Laporan> listFilter = FXCollections.observableArrayList();
    @FXML
    private Label LabTotal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTransaksi();

        trId.setCellValueFactory(new PropertyValueFactory<>("id"));
        trNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        trTanggal.setCellValueFactory(new PropertyValueFactory<>("tgl"));
        trKasir.setCellValueFactory(new PropertyValueFactory<>("kasir"));
        trTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public void trClicked(MouseEvent mouseEvent) {
        loadDetail();

        dtMenu.setCellValueFactory(new PropertyValueFactory<>("menu"));
        dtKuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        dtSub.setCellValueFactory(new PropertyValueFactory<>("sub"));
    }

    public void onCari(ActionEvent actionEvent) {
        LoginController l = new LoginController();
        listFilter.clear();
        boolean exist = false;
        for (int i=0;i<listTransaksi.size();i++){
            if (listTransaksi.get(i).getNama().equalsIgnoreCase(txtNama.getText())){
                listFilter.add(new Laporan(
                        listTransaksi.get(i).getId(),
                        listTransaksi.get(i).getNama(),
                        listTransaksi.get(i).getTgl(),
                        listTransaksi.get(i).getKasir(),
                        listTransaksi.get(i).getTotal())
                );
                exist = true;
            }
        }
        if(exist){
            TabelTransaksi.setItems(listFilter);
            LabTotal.setText(totalTransaksi(listFilter));
        }else{
            l.showMessage(Alert.AlertType.INFORMATION,"Information" ,"data tidak ada!", 18);
            loadTransaksi();
        }
    }

    public String totalTransaksi(ObservableList<com.example.restaurant.model.Laporan> list){
        double total = 0;
        for (int i = 0;i<list.size();i++){
            total += list.get(i).getTotal();
        }
        return String.valueOf(total);
    }


    public void loadTransaksi() {
        listTransaksi.clear();
        try {
            int i = 0;
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC GetLaporan";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                i++;
                listTransaksi.add(new Laporan(connection.result.getInt("psn_id"),
                        connection.result.getString("psn_pelanggan"),
                        connection.result.getString("psn_tanggal"),
                        connection.result.getString("psn_createby"),
                        connection.result.getDouble("psn_total")));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println("Error when load Menu" + ex);
        }
        TabelTransaksi.setItems(listTransaksi);
        LabTotal.setText(totalTransaksi(listTransaksi));
    }

    public void onRefresh(ActionEvent actionEvent) {
        listDetail.clear();
        loadTransaksi();
    }

    public void loadDetail(){
        listDetail.clear();
        Laporan p = (Laporan) TabelTransaksi.getSelectionModel().getSelectedItem();
        int qty;
        try {
            int i = 0;
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC GetDetail "+p.getId();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                i++;
                qty = connection.result.getInt("dtl_quantity");
                listDetail.add(new Laporan(connection.result.getString("mnu_nama"),
                        qty,
                        connection.result.getDouble("mnu_harga")*qty
                ));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println("Error when load Menu" + ex);
        }
        TabelDetail.setItems(listDetail);
    }

    public void onFilter(ActionEvent actionEvent) {
        LoginController l = new LoginController();
        if (datePick.getValue() == null){
            l.showMessage(Alert.AlertType.INFORMATION,"Information" ,"data tidak ada!", 18);
            return;
        }
        boolean exist = false;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate filterDate = datePick.getValue();
        filterDate.format(fmt);
        listFilter.clear();
        for (int i=0;i<listTransaksi.size();i++){
            if (listTransaksi.get(i).getTgl().equals(String.valueOf(filterDate))){
                listFilter.add(new Laporan(
                        listTransaksi.get(i).getId(),
                        listTransaksi.get(i).getNama(),
                        listTransaksi.get(i).getTgl(),
                        listTransaksi.get(i).getKasir(),
                        listTransaksi.get(i).getTotal())
                );
                exist = true;
            }
        }
        if(exist){
            TabelTransaksi.setItems(listFilter);
            LabTotal.setText(totalTransaksi(listFilter));
        }else{
            l.showMessage(Alert.AlertType.INFORMATION,"Information" ,"data tidak ada!", 18);
            loadTransaksi();
        }
    }
}
