package it.unimib.pickapp.model;

public enum Sport {
    FOOTBALL,
    BASKETBALL,
    VOLLEYBALL,
    TENNIS;

    public static String[] names() {
        Sport[] sports = values();
        String[] names = new String[sports.length];

        for (int i = 0; i < sports.length; i++) {
            names[i] = sports[i].name();
        }

        return names;
    }
}
