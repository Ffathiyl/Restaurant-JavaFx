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
import java.text.DecimalFormat;
import java.time.LocalDate;
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
    private TextField txtDate;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnBayar;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,Integer> menuId;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,String> menuMenu;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,Integer> menuStok;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,String> menuHarga;
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
        txtDate.setDisable(true);
        txtId.setVisible(false);
        txtDate.setText(String.valueOf(LocalDate.now()));
        loadMenu();

        krId.setVisible(false);
        menuId.setVisible(false);
        menuId.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuMenu.setCellValueFactory(new PropertyValueFactory<>("nama"));
        menuStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        menuHarga.setCellValueFactory(new PropertyValueFactory<>("fHarga"));
        menuHarga.setStyle("-fx-alignment: CENTER-RIGHT;");
        krSub.setStyle("-fx-alignment: CENTER-RIGHT;");

        txtKuantitas.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            if(!event.getCharacter().matches("\\d")){
                event.consume();
            }
        });
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
                if(connection.result.getInt("mnu_status") == 1 && connection.result.getInt("mnu_stok") != 0){
                    listMenu.add(new Menu(connection.result.getInt("mnu_id"),
                            connection.result.getString("mnu_nama"),
                            connection.result.getInt("mnu_stok"),
                            priceFormat(connection.result.getInt("mnu_harga"))));
                }
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
            p.showMessage(Alert.AlertType.WARNING,"Waring" ,"menu belum terpilih!", 18);
            return;
        }
        if(txtCustomer.getText().equals("") || txtKuantitas.getText().equals("")){
            p.showMessage(Alert.AlertType.WARNING,"Waring" ,"nama customer dan kuantitas harus terisi!", 18);
            return;
        }
        if(txtKuantitas.equals("0")){
            p.showMessage(Alert.AlertType.WARNING,"Waring" ,"kuantitas tidak boleh 0!", 18);
            return;
        }
        if(Integer.parseInt(txtStok.getText())<Integer.parseInt(txtKuantitas.getText()) || txtStok.getText().equals("0")){
            p.showMessage(Alert.AlertType.WARNING,"Waring" ,"Kuantitas melebihi batas yang ada!", 18);
            return;
        }
        txtCustomer.setDisable(true);
        total = Integer.parseInt(txtKuantitas.getText()) * convertFormatted(txtHarga.getText());
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
        for (int i=0;i<listMenu.size();i++){
            if(listMenu.get(i).getStok()==0){
                listMenu.remove(i);
            }
        }
        tableMenu.refresh();

        total = 0;
        for (int i=0;i<listKeranjang.size();i++){
            total += listKeranjang.get(i).getTotal();
        }

        LabTotalHarga.setText(priceFormat(total));
        clearTxt();
    }

    public void storeTransaction(ActionEvent actionEvent) {
        DBConnect connect = new DBConnect();
        LoginController l = new LoginController();
        String query;
        int id=0;
        if(listKeranjang.size() == 0){
            LoginController loginController = new LoginController();
            loginController.showMessage(Alert.AlertType.INFORMATION, "Warning", "Keranjang masih kosong!", 18);
            return;
        }
        try {
            query = "EXEC sp_Transaksi ?,?,?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1,txtCustomer.getText());
            connect.pstat.setDouble(2,convertFormatted(LabTotalHarga.getText()));
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
        l.showMessage(Alert.AlertType.INFORMATION,"Information" ,"Transaksi berhasil!", 18);
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

    public String priceFormat(Double price){
        DecimalFormat fmt = new DecimalFormat("###,###");
        return fmt.format(price);
    }

    public String priceFormat(int price){
        DecimalFormat fmt = new DecimalFormat("###,###");
        return fmt.format(price);
    }

    public double convertFormatted(String price){
        price = price.replace(",","");
        try {
            return Double.parseDouble(price);
        }catch (NumberFormatException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public void numericInput(KeyEvent keyEvent) {

    }
}
