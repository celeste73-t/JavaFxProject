package tournoi_volley.model;

import tournoi_volley.exception.ChampsVideException;
import tournoi_volley.exception.EmailInvalideException;
import tournoi_volley.exception.NumeroInvalideException;

import java.util.Objects;

public class Joueur {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private Equipe equipe;

    public Joueur(String nom, String prenom, String telephone, String email) 
            throws ChampsVideException, EmailInvalideException, NumeroInvalideException {
        setNom(nom);
        setPrenom(prenom);
        setTelephone(telephone);
        setEmail(email);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) throws ChampsVideException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ChampsVideException("Le nom ne peut pas être vide");
        }
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) throws ChampsVideException {
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new ChampsVideException("Le prénom ne peut pas être vide");
        }
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) throws NumeroInvalideException, ChampsVideException {
        if (telephone == null || telephone.trim().isEmpty()) {
            throw new ChampsVideException("Le numéro de téléphone ne peut pas être vide");
        }
        
        // Vérification du format du numéro de téléphone (exemple simple)
        if (!telephone.matches("^\\d{10}$")) {
            throw new NumeroInvalideException("Le numéro de téléphone doit contenir 10 chiffres");
        }
        
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws EmailInvalideException, ChampsVideException {
        if (email == null || email.trim().isEmpty()) {
            throw new ChampsVideException("L'email ne peut pas être vide");
        }
        
        // Vérification simple du format de l'email
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new EmailInvalideException("Format d'email invalide");
        }
        
        this.email = email;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        return nom.equals(joueur.nom) && 
               prenom.equals(joueur.prenom) && 
               email.equals(joueur.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom, email);
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}