package it.unimib.pickapp.model;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class Match {

    private boolean isPrivate;
    private int hour;
    private String titolo;
    private String luogo;
    private String sport;
    private String descrizione;
    private Map<String, User> partecipanti; //TODO Rimuovere
    private Map<String, String> participants;
    private int numeroSquadre;
    private Time durata;
    private double costo;
    private int minutes;
    private String id;
    private String creatorId;
    private String date;
    private String time;

    public Match() {
        participants = new HashMap<>();
    }

    public Map<String, String> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, String> participants) {
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

    public int getMinutes() {
        return minutes;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
