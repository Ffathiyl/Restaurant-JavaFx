module com.example.restaurant {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.restaurant to javafx.fxml;
    exports com.example.restaurant;
    exports com.example.restaurant.login;
    opens com.example.restaurant.login to javafx.fxml;
    exports com.example.restaurant.menu;
    opens com.example.restaurant.menu to javafx.fxml;
    exports com.example.restaurant.model;
    opens com.example.restaurant.model to javafx.fxml;
}