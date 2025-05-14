module tournoi_volley {
    requires javafx.controls;
    requires javafx.fxml;

    // Ouvrir le contrôleur pour FXML
    opens tournoi_volley to javafx.fxml;
    
    // Ouvrir le package model pour les liaisons de données JavaFX
    opens tournoi_volley.model to javafx.base;
    
    exports tournoi_volley;
}