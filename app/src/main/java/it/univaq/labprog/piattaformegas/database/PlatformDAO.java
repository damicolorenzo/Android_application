package it.univaq.labprog.piattaformegas.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.univaq.labprog.piattaformegas.model.Platform;

@Dao
public interface PlatformDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(List<Platform> data);

    @Query("DELETE FROM platforms")
    public void deleteAll();

    @Query("SELECT * FROM platforms ORDER BY denominazione DESC")
    public List<Platform> findAll();
}
