package it.unimib.pickapp.model;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class Match {

    //attributi della partita
    private boolean isPrivate;
    private int hour;
    private String titolo;
    private String luogo;
    private String sport;
    private String descrizione;
    private Map<String, User> partecipanti; //TODO Rimuovere
    private Map<String, Boolean> participants;
    private int numeroSquadre;
    private int day;
    private int month;
    private int year;
    private Time durata;
    private double costo;
    private int minutes;
    private String id;
    private String creatorId;

    public Match() {
        participants = new HashMap<>();
    }

    public Map<String, Boolean> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Boolean> participants) {
        this.participants = participants;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String stato;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


    /* TODO : eventualmente pensare se usare ad esempio
    Duration duration = Duration.between(Instant.now(), ZonedDateTime.parse("2016-07-27T07:00:00+01:00[Europe/Stockholm]"))

    fonte: https://learntutorials.net/it/java/topic/4813/date-e-ora--java-time----
     */

    //verifica se c'è un partecipante
    public boolean contains(String profileid) {
        return partecipanti.containsKey(profileid);
    }

    //aggiunge un partecipante
    public void addPartecipante(User user) {
        this.partecipanti.put(user.getNickname(), user);
    }

    //rimuove un partecipante
    public void removePartecipante(String nickname) {
        this.partecipanti.remove(nickname);
    }


    //Getter e Setter per ogni variabile

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

    public CharSequence getPartecipanti() {
        return (CharSequence) partecipanti;
    }

    public int getNumeroSquadre() {
        return numeroSquadre;
    }

    public int getDay() { return day;}

    public int getMonth() { return month;}

    public int getYear() { return year;}

    public CharSequence getDurata() {
        return (CharSequence) durata;
    }

    public double getCosto() { return costo; }

    public String getStato() {
        return stato;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    //setta la lista dei partecipanti
    public void setPartecipanti(Map<String, User> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public void setNumeroSquadre(int numeroSquadre) {
        this.numeroSquadre = numeroSquadre;
    }

    public void setDay(int day) { this.day = day; }

    public void setMonth(int month) { this.month = month; }

    public void setYear(int year) { this.year = year; }

    public void setDurata(Time durata) {
        this.durata = durata;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
