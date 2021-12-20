package it.unimib.pickapp.model;

public class User {
    private String name;
    private String surname;
    private String nickname;
    private String email;
    private String password;
    private String favouriteSport;
    private double experienceLevel;
    private double reliabilityLevel;

    public User(){

    }

    public User(String name, String surname, String nickname, String email, String password, String favouriteSport, String experienceLevel, double reliabilityLevel) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.favouriteSport = favouriteSport;
        if(experienceLevel.equals("Beginner"))
            this.experienceLevel = 0;
        if (experienceLevel.equals("Intermediate"))
            this.experienceLevel = 2.5;
        if (experienceLevel.equals("Advanced"))
            this.experienceLevel = 5;
        this.reliabilityLevel = reliabilityLevel;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
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

    public void setExperienceLevel(String experienceLevel) {
        if(experienceLevel.equals("Beginner"))
            this.experienceLevel = 0;
        if (experienceLevel.equals("Intermediate"))
            this.experienceLevel = 2.5;
        if (experienceLevel.equals("Advanced"))
            this.experienceLevel = 5;
    }

    public void setReliabilityLevel(double reliabilityLevel) {
        this.reliabilityLevel = reliabilityLevel;
    }
}
