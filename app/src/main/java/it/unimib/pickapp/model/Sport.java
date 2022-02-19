package it.unimib.pickapp.model;

public enum Sport {
    FOOTBALL,
    BASKETBALL,
    SOCCER,
    TENNIS;
    //ritorna un array di String che contine gli sport
    public static String[] names() {
        Sport[] sports = values();
        String[] names = new String[sports.length];

        for (int i = 0; i < sports.length; i++) {
            names[i] = sports[i].name();
        }

        return names;
    }
}
