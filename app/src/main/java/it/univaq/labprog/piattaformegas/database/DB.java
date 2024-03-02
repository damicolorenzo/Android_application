package it.univaq.labprog.piattaformegas.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import it.univaq.labprog.piattaformegas.model.Platform;

@Database(entities = {Platform.class}, version = 1, exportSchema = false)
public abstract class DB extends RoomDatabase {
    public abstract PlatformDAO getPlatformDao();

    private volatile static DB instance = null;

    public static synchronized DB getInstance(Context context) {
        if(instance == null) {
            synchronized (DB.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context, DB.class, "database.db")
                            .build();
                }
            }
        }
        return instance;
    }
}
