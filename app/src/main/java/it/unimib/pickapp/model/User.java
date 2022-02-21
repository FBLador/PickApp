package it.unimib.pickapp.model;

import androidx.annotation.NonNull;

public class User {
    //attributi utente
    private String name;
    private String surname;
    private String nickname;
    private String fullname;
    private String email;
    private String password;
    private String favouriteSport;
    private double experienceLevel;
    private double reliabilityLevel;

    //percorso immagine profilo
    private String imageurl;
    private String bio;

    //costruttore vuoto
    public User(){
        //lasciare vuoto, serve per firebase realtime
    }

    //costruttore per inserimento su db
    public User(String name, String surname, String nickname, String email, String password, String favouriteSport, double experienceLevel, double reliabilityLevel) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.fullname = name + " " + surname;
        this.email = email;
        this.password = password;
        this.favouriteSport = favouriteSport;
        this.experienceLevel = experienceLevel;
        this.reliabilityLevel = reliabilityLevel;
        //immagine profile di default
        this.imageurl = "https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_960_720.png";
        this.bio = "bio vuota";
    }

    //getter
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullname() {
        return name + " " + surname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFavouriteSport() {
        return favouriteSport;
    }

    public double getExperienceLevel() {
        return experienceLevel;
    }

    public double getReliabilityLevel() {
        return reliabilityLevel;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getBio() {
        return bio;
    }

    //setter
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFavouriteSport(String favouriteSport) {
        this.favouriteSport = favouriteSport;
    }

    public void setExperienceLevel(double experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setReliabilityLevel(double reliabilityLevel) {
        this.reliabilityLevel = reliabilityLevel;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    //toString
    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", favouriteSport='" + favouriteSport + '\'' +
                ", experienceLevel=" + experienceLevel +
                ", reliabilityLevel=" + reliabilityLevel +
                ", imageurl='" + imageurl + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}

