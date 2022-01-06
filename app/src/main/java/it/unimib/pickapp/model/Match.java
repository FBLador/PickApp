package it.unimib.pickapp.model;

import java.sql.Time;
import java.util.Map;

public class Match {

    private String titolo;
    private String luogo;
    private String sport;
    private String descrizione;
    private Map<String, User> partecipanti;
    private int numeroSquadre;
    private int day;
    private int month;
    private int year;
    private Time durata;
    private double costo;
    private String tipo; //pubblica o privata
    private String stato;


    /* TODO : eventualmente pensare se usare ad esempio
    Duration duration = Duration.between(Instant.now(), ZonedDateTime.parse("2016-07-27T07:00:00+01:00[Europe/Stockholm]"))

    fonte: https://learntutorials.net/it/java/topic/4813/date-e-ora--java-time----
     */

    public boolean contains(String profileid){
        return partecipanti.containsKey(profileid);
    }

    public void addPartecipante(User user) {
        this.partecipanti.put(user.getNickname(), user);
    }

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

    public CharSequence getDateTime() {
        return day+ "/" +month+ "/" +year;
    }

    public CharSequence getDurata() {
        return (CharSequence) durata;
    }

    public double getCosto() { return costo; }

    public String getTipo() {
        return tipo;
    }

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

    public void setPartecipanti(Map<String, User> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public void setNumeroSquadre(int numeroSquadre) {
        this.numeroSquadre = numeroSquadre;
    }

    public void setDateTime(int day, int month, int year) {

        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void setDurata(Time durata) {
        this.durata = durata;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
