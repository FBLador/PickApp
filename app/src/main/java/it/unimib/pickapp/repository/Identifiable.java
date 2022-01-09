package it.unimib.pickapp.repository;

import com.google.firebase.database.Exclude;

public interface Identifiable<TKey> {
    @Exclude
    TKey getEntityKey();

    @Exclude
    void setEntityKey(TKey key);
}
