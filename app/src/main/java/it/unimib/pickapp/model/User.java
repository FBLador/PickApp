package it.unimib.pickapp.model;

public class User {
    private String name;
    private String surname;
    private String nickname;
    private String email;
    private String password;
    private String favouriteSport;
    private String experienceLevel;
    private int reliabilityLevel;

    public User(String name, String surname, String nickname, String email, String password, String favouriteSport, String experienceLevel, int reliabilityLevel) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.favouriteSport = favouriteSport;
        this.experienceLevel = experienceLevel;
        this.reliabilityLevel = reliabilityLevel;
    }
}
