package tournoi_volley;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tournoi_volley.exception.*;
import tournoi_volley.model.Equipe;
import tournoi_volley.model.Joueur;
import tournoi_volley.model.Tournoi;
import tournoi_volley.util.DateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;

public class Controller {

    private Tournoi tournoi = new Tournoi();
    private Equipe equipeSelectionnee;
    private ObservableList<Equipe> equipesList = FXCollections.observableArrayList();
    private ObservableList<Joueur> joueursList = FXCollections.observableArrayList();

    @FXML
    private Label statutLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private TableView<Equipe> equipesTable;

    @FXML
    private TableColumn<Equipe, String> nomEquipeColumn;

    @FXML
    private TableColumn<Equipe, Integer> nbJoueursColumn;

    @FXML
    private TableView<Joueur> joueursTable;

    @FXML
    private TableColumn<Joueur, String> nomJoueurColumn;

    @FXML
    private TableColumn<Joueur, String> prenomJoueurColumn;

    @FXML
    private TableColumn<Joueur, String> telJoueurColumn;

    @FXML
    private TableColumn<Joueur, String> emailJoueurColumn;

    @FXML
    private TextField nomEquipeField;

    @FXML
    private TextField nomJoueurField;

    @FXML
    private TextField prenomJoueurField;

    @FXML
    private TextField telJoueurField;

    @FXML
    private TextField emailJoueurField;

    @FXML
    private Button ajouterEquipeButton;

    @FXML
    private Button supprimerEquipeButton;

    @FXML
    private Button ajouterJoueurButton;

    @FXML
    private Button supprimerJoueurButton;

    @FXML
    public void initialize() {
        // Initialisation des dates d'inscription
        LocalDate dateDebut = Tournoi.getDateDebutInscription();
        LocalDate dateFin = Tournoi.getDateFinInscription();
        dateLabel.setText("Inscriptions du " + DateUtil.formatDate(dateDebut) + " au " + DateUtil.formatDate(dateFin));

        // Configuration des tables
        nomEquipeColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getNom()));
        
    nbJoueursColumn.setCellValueFactory(cellData -> 
        new SimpleIntegerProperty(cellData.getValue().getJoueurs().size()).asObject());

        nomJoueurColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomJoueurColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telJoueurColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailJoueurColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Mise à jour du statut du tournoi
        mettreAJourStatut();

        // Désactiver les boutons de suppression et d'ajout de joueur au démarrage
        supprimerEquipeButton.setDisable(true);
        ajouterJoueurButton.setDisable(true);
        supprimerJoueurButton.setDisable(true);

        // Ajouter les écouteurs de sélection
        equipesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            equipeSelectionnee = newSelection;
            if (equipeSelectionnee != null) {
                supprimerEquipeButton.setDisable(false);
                ajouterJoueurButton.setDisable(false);
                joueursTable.setItems(FXCollections.observableArrayList(equipeSelectionnee.getJoueurs()));
            } else {
                supprimerEquipeButton.setDisable(true);
                ajouterJoueurButton.setDisable(true);
                joueursTable.setItems(null);
            }
        });

        joueursTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            supprimerJoueurButton.setDisable(newSelection == null);
        });

        // Lier les tables aux listes observables
        equipesTable.setItems(equipesList);
    }

    @FXML
    private void handleAjouterEquipe() {
        String nomEquipe = nomEquipeField.getText().trim();

        if (nomEquipe.isEmpty()) {
            afficherAlerte("Erreur", "Nom d'équipe vide", "Veuillez saisir un nom d'équipe.");
            return;
        }

        // Vérifier si le nom est déjà utilisé
        for (Equipe e : equipesList) {
            if (e.getNom().equalsIgnoreCase(nomEquipe)) {
                afficherAlerte("Erreur", "Nom d'équipe déjà utilisé",
                        "Une équipe avec le nom '" + nomEquipe + "' existe déjà.");
                return;
            }
        }

        Equipe nouvelleEquipe = new Equipe(nomEquipe);

        try {
            tournoi.addEquipe(nouvelleEquipe);
            equipesList.add(nouvelleEquipe);
            System.out.println("Équipe ajoutée: " + nouvelleEquipe.getNom());
            System.out.println("Nombre d'équipes dans la liste: " + equipesList.size());
            equipesTable.refresh(); // Forcer le rafraîchissement du tableau
            nomEquipeField.clear();
            mettreAJourStatut();
        } catch (TournoiCompletException e) {
            afficherAlerte("Erreur", "Tournoi complet", e.getMessage());
        } catch (DateInscriptionException e) {
            afficherAlerte("Erreur", "Hors période d'inscription", e.getMessage());
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur d'inscription", e.getMessage());
        }
    }

    @FXML
    private void handleSupprimerEquipe() {
        if (equipeSelectionnee != null) {
            tournoi.removeEquipe(equipeSelectionnee);
            equipesList.remove(equipeSelectionnee);
            joueursTable.setItems(null);
            equipeSelectionnee = null;
            mettreAJourStatut();
        }
    }

    @FXML
    private void handleAjouterJoueur() {
        if (equipeSelectionnee == null) {
            return;
        }

        String nom = nomJoueurField.getText().trim();
        String prenom = prenomJoueurField.getText().trim();
        String telephone = telJoueurField.getText().trim();
        String email = emailJoueurField.getText().trim();

        Joueur nouveauJoueur = null;
        
        try {
            if (nom.isEmpty()) {
                throw new ChampsVideException("Le nom ne peut pas être vide");
            }
            if (prenom.isEmpty()) {
                throw new ChampsVideException("Le prénom ne peut pas être vide");
            }
            if (telephone.isEmpty()) {
                throw new ChampsVideException("Le téléphone ne peut pas être vide");
            }
            if (email.isEmpty()) {
                throw new ChampsVideException("L'email ne peut pas être vide");
            }

            if (!telephone.matches("^\\d{10}$")) {
                throw new NumeroInvalideException("Le numéro de téléphone doit contenir 10 chiffres");
            }
            
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new EmailInvalideException("Format d'email invalide");
            }
            
            nouveauJoueur = new Joueur(nom, prenom, telephone, email);
            
        } catch (ChampsVideException e) {
            afficherAlerte("Erreur", "Champs obligatoires", e.getMessage());
            return;
        } catch (EmailInvalideException e) {
            afficherAlerte("Erreur", "Email non conforme", e.getMessage());
            return;
        } catch (NumeroInvalideException e) {
            afficherAlerte("Erreur", "Numéro non conforme", e.getMessage());
            return;
        }

        // Si on arrive ici, c'est que le joueur a été créé avec succès
        try {
            equipeSelectionnee.addJoueur(nouveauJoueur);
            joueursTable.setItems(FXCollections.observableArrayList(equipeSelectionnee.getJoueurs()));

            // Mise à jour de la table des équipes pour afficher le nouveau nombre de joueurs
            int index = equipesList.indexOf(equipeSelectionnee);
            if (index >= 0) {
                equipesList.set(index, equipeSelectionnee);
            }

            // Effacer les champs
            nomJoueurField.clear();
            prenomJoueurField.clear();
            telJoueurField.clear();
            emailJoueurField.clear();

        } catch (EquipeCompleteException e) {
            afficherAlerte("Erreur", "Équipe complète", e.getMessage());
        } catch (JoueurDejaInscritException e) {
            afficherAlerte("Erreur", "Joueur déjà inscrit", e.getMessage());
        }
    }

    @FXML
    private void handleSupprimerJoueur() {
        Joueur joueurSelectionne = joueursTable.getSelectionModel().getSelectedItem();
        if (joueurSelectionne != null && equipeSelectionnee != null) {
            equipeSelectionnee.removeJoueur(joueurSelectionne);
            joueursTable.setItems(FXCollections.observableArrayList(equipeSelectionnee.getJoueurs()));

            // Mise à jour de la table des équipes
            int index = equipesList.indexOf(equipeSelectionnee);
            if (index >= 0) {
                equipesList.set(index, equipeSelectionnee);
            }
        }
    }

    private void mettreAJourStatut() {
        int nbEquipes = tournoi.getNombreEquipes();
        int maxEquipes = 8;

        if (tournoi.estComplet()) {
            statutLabel.setText("Le tournoi est complet (" + nbEquipes + "/" + maxEquipes + " équipes)");
            ajouterEquipeButton.setDisable(true);
        } else {
            statutLabel.setText("Places disponibles : " + tournoi.getPlacesRestantes() + "/" + maxEquipes + " équipes");

            // Vérifier si on est dans la période d'inscription
            boolean periodeInscription = DateUtil.estPeriodeInscription();
            ajouterEquipeButton.setDisable(!periodeInscription);

            if (!periodeInscription) {
                statutLabel.setText(statutLabel.getText() + " - Inscriptions fermées");
            }
        }
    }

    private void afficherAlerte(String titre, String entete, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

}