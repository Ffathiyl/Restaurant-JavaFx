package com.example.restaurant.menu;

import com.example.restaurant.database.DBConnect;
import com.example.restaurant.model.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable{
    @FXML
    private TextField txtIdMenu;
    @FXML
    private TextField txtNamaMenu;
    @FXML
    private TextField txtDeskripsiMenu;
    @FXML
    private TextField txtHargaMenu;
    @FXML
    private TextField txtStokMenu;
    @FXML
    private ComboBox<String> cbJenisMenu;
    @FXML
    private Button btnTambah;
    @FXML
    private TableView tableMenu;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,Integer> mnu_id;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,String> mnu_nama;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,String> mnu_deskripsi;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,Integer> mnu_jenis;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,Integer> mnu_harga;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,Integer> mnu_stok;
    @FXML
    private TableColumn<com.example.restaurant.model.Menu,Integer> mnu_status;
    @FXML
    private ObservableList<com.example.restaurant.model.Menu> listMenu = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> jenisMenuList = FXCollections.observableArrayList(
                "Makanan", "Minuman", "Paket");

        loadMenu();

        cbJenisMenu.setItems(jenisMenuList);
        mnu_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        mnu_nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        mnu_deskripsi.setCellValueFactory(new PropertyValueFactory<>("desc"));
        mnu_jenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        mnu_harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        mnu_stok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        mnu_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        mnu_jenis.setCellFactory(column -> {
            return new TableCell<Menu, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        switch (item) {
                            case 0:
                                setText("Makanan");
                                break;
                            case 1:
                                setText("Minuman");
                                break;
                            case 2:
                                setText("Paket");
                                break;
                            default:
                                setText("Unknown");
                        }
                    }
                }
            };
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
                listMenu.add(new Menu(connection.result.getInt("mnu_id"),
                        connection.result.getInt("mnu_harga"),
                        connection.result.getInt("mnu_status"),
                        connection.result.getInt("mnu_jenis"),
                        connection.result.getInt("mnu_stok"),
                        connection.result.getString("mnu_nama"),
                        connection.result.getString("mnu_deskripsi")));
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Error when load Menu"+ex);
        }
        tableMenu.setItems(listMenu);
    }

    @FXML
    protected void onClickTambah(){
        DBConnect connection = new DBConnect();
        int id,harga,stok,status,jenis;
        String nama,deskripsi;

        String jenisMenu = cbJenisMenu.getValue();
        switch (jenisMenu) {
            case "Makanan":
                jenis = 0;
                break;
            case "Minuman":
                jenis = 1;
                break;
            case "Paket":
                jenis = 2;
                break;
            default:
                jenis = -1;
        }

        harga = Integer.parseInt(txtHargaMenu.getText());
        stok = Integer.parseInt(txtStokMenu.getText());
        nama = txtNamaMenu.getText();
        deskripsi = txtDeskripsiMenu.getText();

        try{
            String query = "EXEC sp_inputMsMenu ?,?,?,?,?,?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1,nama);
            connection.pstat.setString(2,deskripsi);
            connection.pstat.setInt(3,jenis);
            connection.pstat.setInt(4,harga);
            connection.pstat.setInt(5,stok);
            connection.pstat.setInt(6,1);

            connection.pstat.executeUpdate();
            connection.pstat.close();
            loadMenu();
        }catch (Exception ex){
            System.out.println("Error saat tambah menu"+ex);
        }
        clearInput();
    }

    public void clearInput(){
        txtHargaMenu.setText("");
        txtStokMenu.setText("");
        txtDeskripsiMenu.setText("");
        txtIdMenu.setText("");
        txtNamaMenu.setText("");
        cbJenisMenu.setValue("");
    }

    @FXML
    protected void onClickUbah() {
        listMenu.removeAll();
        DBConnect connection = new DBConnect();
        int jenis;
        switch (cbJenisMenu.getValue()) {
            case "Makanan":
                jenis = 0;
                break;
            case "Minuman":
                jenis = 1;
                break;
            case "Paket":
                jenis = 2;
                break;
            default:
                jenis = -1;
        }
        try {
            String query = "EXEC sp_updateMsMenu ?,?,?,?,?,?,?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, txtIdMenu.getText());
            connection.pstat.setString(2, txtNamaMenu.getText());
            connection.pstat.setString(3, txtDeskripsiMenu.getText());
            connection.pstat.setInt(4, jenis);
            connection.pstat.setInt(5, Integer.parseInt(txtHargaMenu.getText()));
            connection.pstat.setInt(6, Integer.parseInt(txtStokMenu.getText()));
            connection.pstat.executeUpdate();
            connection.pstat.close();
            System.out.println("Update Berhasil");
            loadMenu();
            clearInput();
        }catch (Exception ex){
            System.out.println("Gagal Update Menu : "+ex);
        }
    }

    @FXML
    protected void onClickClear(){
        clearInput();
    }

    @FXML
    protected void onClickHapus(){
        DBConnect connection = new DBConnect();
        try{
            String query = "EXEC sp_deleteMsMenu ?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1,txtIdMenu.getText());
            connection.pstat.executeUpdate();
            connection.pstat.close();
            loadMenu();
        }catch (Exception ex){
            System.out.println(txtIdMenu);
            System.out.println("Gagal Hapus Menu : "+ex);
        }
        clearInput();
    }

    @FXML
    protected void onClickTable(){
        Menu ob = (Menu) tableMenu.getSelectionModel().getSelectedItem();
        txtIdMenu.setText(ob.getId().toString());
        txtNamaMenu.setText(ob.getNama());
        txtDeskripsiMenu.setText(ob.getDesc());
        txtHargaMenu.setText(ob.getHarga().toString());
        txtStokMenu.setText(ob.getStok().toString());
        String jenis;
        switch (ob.getJenis()) {
            case 0:
                jenis = "Makanan";
                break;
            case 1:
                jenis = "Minuman";
                break;
            case 2:
                jenis = "Paket";
                break;
            default:
                jenis = "null";
        }
        cbJenisMenu.setValue(jenis);
    }
}
