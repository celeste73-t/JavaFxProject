package tournoi_volley.model;

import tournoi_volley.exception.DateInscriptionException;
import tournoi_volley.exception.JoueurDejaInscritException;
import tournoi_volley.exception.TournoiCompletException;
import tournoi_volley.util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tournoi {
    private static final int MAX_EQUIPES = 8;
    private static final LocalDate DATE_DEBUT_INSCRIPTION = LocalDate.of(2025, 5, 9);
    private static final LocalDate DATE_FIN_INSCRIPTION = LocalDate.of(2025, 5, 16);

    private List<Equipe> equipes;

    public Tournoi() {
        this.equipes = new ArrayList<>();
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public void addEquipe(Equipe equipe) throws TournoiCompletException, DateInscriptionException, JoueurDejaInscritException {
        // Vérifier que la date d'inscription est valide
        if (!DateUtil.estPeriodeInscription()) {
            throw new DateInscriptionException("Les inscriptions sont ouvertes du "
                    + DateUtil.formatDate(DATE_DEBUT_INSCRIPTION) + " au "
                    + DateUtil.formatDate(DATE_FIN_INSCRIPTION));
        }

        // Vérifier que le tournoi n'est pas déjà complet
        if (equipes.size() >= MAX_EQUIPES) {
            throw new TournoiCompletException("Le tournoi est complet (maximum " + MAX_EQUIPES + " équipes)");
        }

        // Vérifier si le nom de l'équipe est déjà utilisé
        for (Equipe e : equipes) {
            if (e.getNom().equalsIgnoreCase(equipe.getNom())) {
                throw new IllegalArgumentException("Une équipe avec le nom '" + equipe.getNom() + "' existe déjà");
            }
        }

        // Vérifier si des joueurs sont déjà inscrits dans d'autres équipes
        for (Joueur joueur : equipe.getJoueurs()) {
            for (Equipe e : equipes) {
                if (e.getJoueurs().contains(joueur)) {
                    throw new JoueurDejaInscritException("Le joueur " + joueur.getNom() +
                            " est déjà inscrit dans l'équipe " + e.getNom());
                }
            }
        }

        equipes.add(equipe);
    }

    public void removeEquipe(Equipe equipe) {
        equipes.remove(equipe);
    }

    public boolean estComplet() {
        return equipes.size() >= MAX_EQUIPES;
    }

    public int getNombreEquipes() {
        return equipes.size();
    }

    public int getPlacesRestantes() {
        return MAX_EQUIPES - equipes.size();
    }

    public static LocalDate getDateDebutInscription() {
        return DATE_DEBUT_INSCRIPTION;
    }

    public static LocalDate getDateFinInscription() {
        return DATE_FIN_INSCRIPTION;
    }
}