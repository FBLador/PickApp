package it.unimib.pickapp.repository;

import static it.unimib.pickapp.repository.Constants.FIREBASE_DATABASE_URL;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseRepository<TEntity extends Identifiable<String>> implements IRepository<TEntity, String> {

    private static final String TAG = "FirebaseRepository";

    private final Class<TEntity> entityClass;

    private final DatabaseReference databaseReference;
    private final String collectionName;


    public FirebaseRepository(Class<TEntity> entityClass, String collectionName) {
        this.collectionName = collectionName;
        this.entityClass = entityClass;

        databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).
                getReference().child(this.collectionName);
    }

    @Override
    public Task<Boolean> exists(String id) {
        DatabaseReference reference = databaseReference.child(id);
        Log.i(TAG, "Checking existence of '" + id + "' in '" + collectionName + "'.");

        return reference.get().continueWith(task -> {
            Log.d(TAG, "Checking if '" + id + "' exists in '" + collectionName + "'.");
            return task.getResult().exists();
        });
    }

    @Override
    public Task<TEntity> get(String id) {
        DatabaseReference reference = databaseReference.child(id);
        Log.i(TAG, "Getting '" + id + "' in '" + collectionName + "'.");

        return reference.get().continueWith(task -> {
            DataSnapshot dataSnapshot = task.getResult();
            if (dataSnapshot.exists()) {
                TEntity entity = dataSnapshot.getValue(entityClass);
                assert entity != null;
                entity.setEntityKey(id);
                return entity;
            } else {
                Log.d(TAG, "Document '" + id + "' does not exist in '" + collectionName + "'.");
                return entityClass.newInstance();
            }
        });
    }

    @Override
    public Task<Void> create(TEntity entity) {
        final String id;
        if (entity.getEntityKey() == null) {
            id = databaseReference.push().getKey();
        } else {
            id = entity.getEntityKey();
        }
        assert id != null;
        DatabaseReference reference = databaseReference.child(id);
        Log.i(TAG, "Creating '" + id + "' in '" + collectionName + "'.");
        return reference.setValue(entity).addOnFailureListener(e ->
                Log.d(TAG, "There was an error creating '"
                        + id + "' in '" + collectionName + "'!", e));
    }

    @Override
    public Task<Void> update(TEntity entity) {
        final String id = entity.getEntityKey();
        DatabaseReference reference = databaseReference.child(id);
        Log.i(TAG, "Updating '" + id + "' in '" + collectionName + "'.");

        return reference.setValue(entity).addOnFailureListener(e ->
                Log.d(TAG, "There was an error updating '"
                        + id + "' in '" + collectionName + "'.", e));
    }

    @Override
    public Task<Void> delete(final String id) {
        DatabaseReference reference = databaseReference.child(id);
        Log.i(TAG, "Deleting '" + id + "' in '" + collectionName + "'.");

        return reference.removeValue().addOnFailureListener(e ->
                Log.d(TAG, "There was an error deleting '"
                        + id + "' in '" + collectionName + "'.", e));
    }

}