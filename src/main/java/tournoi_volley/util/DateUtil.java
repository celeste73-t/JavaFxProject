package tournoi_volley.util;

import tournoi_volley.model.Tournoi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean estPeriodeInscription() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(Tournoi.getDateDebutInscription()) && !now.isAfter(Tournoi.getDateFinInscription());
    }

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMATTER.format(date);
    }

    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
}