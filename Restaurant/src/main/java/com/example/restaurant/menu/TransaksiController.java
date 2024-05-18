package com.example.restaurant.menu;

import com.example.restaurant.database.DBConnect;
import com.example.restaurant.login.LoginController;
import com.example.restaurant.model.Menu;
import com.example.restaurant.model.Transaksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class TransaksiController implements Initializable {
    @FXML
    private Label LabTotalHarga;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtMenu;
    @FXML
    private TextField txtStok;
    @FXML
    private TextField txtHarga;
    @FXML
    private TextField txtKuantitas;
    @FXML
    private TextField txtCustomer;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnBayar;
    @FXML
    private TableColumn<com.example.restaurant.model.Transaksi,Integer> menuId;
    @FXML
    private TableColumn<com.example.restaurant.model.Transaksi,String> menuMenu;
    @FXML
    private TableColumn<com.example.restaurant.model.Transaksi,Integer> menuStok;
    @FXML
    private TableColumn<com.example.restaurant.model.Transaksi,Integer> menuHarga;
    @FXML
    private TableColumn<com.example.restaurant.model.Transaksi,Integer> krId;
    @FXML
    private TableColumn<com.example.restaurant.model.Transaksi,String> krMenu;
    @FXML
    private TableColumn<com.example.restaurant.model.Transaksi,Integer> krKuantitas;
    @FXML
    private TableColumn<com.example.restaurant.model.Transaksi,Double> krSub;
    @FXML
    private ObservableList<com.example.restaurant.model.Menu> listMenu = FXCollections.observableArrayList();
    @FXML
    private ObservableList<com.example.restaurant.model.Transaksi> listKeranjang = FXCollections.observableArrayList();
    @FXML
    private TableView tableMenu;
    @FXML
    private TableView tableKeranjang;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtId.setDisable(true);
        txtMenu.setDisable(true);
        txtStok.setDisable(true);
        txtHarga.setDisable(true);
        loadMenu();

        menuId.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuMenu.setCellValueFactory(new PropertyValueFactory<>("nama"));
        menuStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        menuHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
    }

    public void loadMenu(){
        listMenu.clear();
        try{
            int i = 0;
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_selectAllMenu";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                i++;
                listMenu.add(new Menu(connection.result.getInt("mnu_id"),
                        connection.result.getString("mnu_nama"),
                        connection.result.getInt("mnu_stok"),
                        connection.result.getInt("mnu_harga")));
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Error when load Menu"+ex);
        }
        tableMenu.setItems(listMenu);
    }

    public void setTotalHarga(KeyEvent keyEvent) {

    }

    public void clearTxt(){
        txtId.setText("");
        txtHarga.setText("");
        txtKuantitas.setText("");
        txtMenu.setText("");
        txtStok.setText("");
    }

    public void addItem(ActionEvent actionEvent) {
        LoginController p = new LoginController();
        double total;
        boolean exist = false;
        if(txtId.getText().equals("")){
            p.showMessage(Alert.AlertType.INFORMATION,"Waring" ,"menu belum terpilih!", 18);
            return;
        }
        if(txtCustomer.getText().equals("") || txtKuantitas.getText().equals("")){
            p.showMessage(Alert.AlertType.INFORMATION,"Waring" ,"nama customer dan kuantitas harus terisi!", 18);
            return;
        }
        if(txtKuantitas.equals("0")){
            p.showMessage(Alert.AlertType.INFORMATION,"Waring" ,"kuantitas tidak boleh 0!", 18);
            return;
        }
        if(Integer.parseInt(txtStok.getText())<Integer.parseInt(txtKuantitas.getText()) || txtStok.getText().equals("0")){
            p.showMessage(Alert.AlertType.INFORMATION,"Waring" ,"Kuantitas melebihi batas yang ada!", 18);
            return;
        }
        txtCustomer.setDisable(true);
        total = Integer.parseInt(txtKuantitas.getText()) * Double.parseDouble(txtHarga.getText());
        for (int i=0;i<listKeranjang.size();i++){
            if(listKeranjang.get(i).getId() == Integer.parseInt(txtId.getText())){
                listKeranjang.get(i).setQty(listKeranjang.get(i).getQty()+Integer.parseInt(txtKuantitas.getText()));
                listKeranjang.get(i).setTotal(listKeranjang.get(i).getTotal()+total);
                exist = true;
                break;
            }
        }
        tableKeranjang.refresh();
        if(!exist){
            listKeranjang.add(new Transaksi(Integer.parseInt(txtKuantitas.getText()),Integer.parseInt(txtId.getText()),total, txtMenu.getText()));
            tableKeranjang.setItems(listKeranjang);
            krKuantitas.setCellValueFactory(new PropertyValueFactory<>("qty"));
            krId.setCellValueFactory(new PropertyValueFactory<>("id"));
            krSub.setCellValueFactory(new PropertyValueFactory<>("total"));
            krMenu.setCellValueFactory(new PropertyValueFactory<>("menu"));
        }
        for (int i=0;i<listMenu.size();i++){
            if(listMenu.get(i).getId() == Integer.parseInt(txtId.getText())){
                listMenu.get(i).setStok(listMenu.get(i).getStok()-Integer.parseInt(txtKuantitas.getText()));
                break;
            }
        }
        tableMenu.refresh();

        total = 0;
        for (int i=0;i<listKeranjang.size();i++){
            total += listKeranjang.get(i).getTotal();
        }

        LabTotalHarga.setText(String.valueOf(total));
        clearTxt();
    }

    public void storeTransaction(ActionEvent actionEvent) {
        DBConnect connect = new DBConnect();
        LoginController l = new LoginController();
        String query;
        int id=0;
        if(listKeranjang == null){
            System.out.println("keranjang masih kosong!");
            return;
        }
        try {
            query = "EXEC sp_Transaksi ?,?,?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1,txtCustomer.getText());
            connect.pstat.setDouble(2,Double.parseDouble(LabTotalHarga.getText()));
            connect.pstat.setString(3,l.getLoggedInCashierName());

            connect.pstat.executeUpdate();
            connect.pstat.close();
        }catch (Exception ex){
            System.out.println(ex);
        }
        try{
            query = "SELECT MAX(psn_id) as id FROM tr_pesanan";
            connect.result = connect.stat.executeQuery(query);
            connect.result.next();
            id = connect.result.getInt("id");
            connect.result.close();
        }catch(Exception ex){
            System.out.println(ex);
        }
        for (int i=0;i<listKeranjang.size();i++){
            try{
                query = "EXEC sp_DetailTransaksi ?,?,?";
                connect.pstat = connect.conn.prepareStatement(query);
                connect.pstat.setInt(1,id);
                connect.pstat.setInt(2,listKeranjang.get(i).getId());
                connect.pstat.setInt(3,listKeranjang.get(i).getQty());
                connect.pstat.executeUpdate();
                connect.pstat.close();
            }catch (Exception ex){
                System.out.println(ex);
            }
        }
        clearAll();
    }

    public void clearAll(){
        clearTxt();
        loadMenu();
        txtCustomer.setDisable(false);
        txtCustomer.setText("");
        LabTotalHarga.setText("0");
        listKeranjang.clear();
    }

    public void resetAll(ActionEvent actionEvent) {
        clearAll();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        Menu ob = (Menu) tableMenu.getSelectionModel().getSelectedItem();
        txtId.setText(ob.getId().toString());
        txtMenu.setText(ob.getNama());
        txtStok.setText(ob.getStok().toString());
        txtHarga.setText(ob.getHarga().toString());
    }
}
