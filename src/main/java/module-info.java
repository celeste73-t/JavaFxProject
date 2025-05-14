module tournoi_volley {
    requires javafx.controls;
    requires javafx.fxml;


    opens tournoi_volley to javafx.fxml;
    exports tournoi_volley;
}