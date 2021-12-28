package it.unimib.pickapp.model;

import java.sql.Array;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class Match {

    private String titolo;
    private String luogo;
    private String sport;
    private String descrizione;
    private Map<String, User> partecipanti;
    private int numeroSquadre;

    private LocalDateTime dateTime;
    private Time durata;

    /* TODO : eventualmente pensare se usare ad esempio
    Duration duration = Duration.between(Instant.now(), ZonedDateTime.parse("2016-07-27T07:00:00+01:00[Europe/Stockholm]"))

    fonte: https://learntutorials.net/it/java/topic/4813/date-e-ora--java-time----
     */

    private double costo;
    private String tipo; //pubblica o privata
    private String stato;

    public String getTitolo() {
        return titolo;
    }

    public String getLuogo() {
        return luogo;
    }

    public String getSport() {
        return sport;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Map<String, User> getPartecipanti() {
        return partecipanti;
    }

    public int getNumeroSquadre() {
        return numeroSquadre;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Time getDurata() {
        return durata;
    }

    public double getCosto() {
        return costo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStato() {
        return stato;
    }

    public boolean contains(String profileid){
        return partecipanti.containsKey(profileid);
    }


    public void addPartecipante(User user) {
        this.partecipanti.put(user.getNickname(), user);
    }

    public void removePartecipante(String nickname) {
        this.partecipanti.remove(nickname);
    }

}
