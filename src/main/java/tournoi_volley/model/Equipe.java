package tournoi_volley.model;

import java.util.ArrayList;
import java.util.List;
import tournoi_volley.exception.EquipeCompleteException;
import tournoi_volley.exception.JoueurDejaInscritException;

public class Equipe {
    private String nom;
    private List<Joueur> joueurs;
    private static final int MAX_JOUEURS = 6;

    public Equipe(String nom) {
        this.nom = nom;
        this.joueurs = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public void addJoueur(Joueur joueur) throws EquipeCompleteException, JoueurDejaInscritException {
        if (joueurs.size() >= MAX_JOUEURS) {
            throw new EquipeCompleteException("L'équipe " + nom + " est déjà complète (maximum " + MAX_JOUEURS + " joueurs)");
        }

        if (joueurs.contains(joueur)) {
            throw new JoueurDejaInscritException("Le joueur " + joueur.getNom() + " est déjà inscrit dans cette équipe");
        }

        joueurs.add(joueur);
        joueur.setEquipe(this);
    }

    public void removeJoueur(Joueur joueur) {
        joueurs.remove(joueur);
        joueur.setEquipe(null);
    }

    public boolean estComplete() {
        return joueurs.size() >= MAX_JOUEURS;
    }

    public int getNombreJoueurs() {
        return joueurs.size();
    }

    @Override
    public String toString() {
        return nom + " (" + joueurs.size() + "/" + MAX_JOUEURS + " joueurs)";
    }
}