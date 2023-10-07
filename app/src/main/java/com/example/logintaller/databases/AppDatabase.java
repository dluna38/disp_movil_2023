package com.example.logintaller.databases;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

public class AppDatabase{
    private static RoomDatabaseInstance database =null;
    public static final String DATABASE_NAME="app-db";
    private AppDatabase() {
    }
    public static RoomDatabaseInstance getDatabase(Context context){

        synchronized (AppDatabase.class) {
            if (database == null) {
                database = Room.databaseBuilder(context.getApplicationContext(),
                                RoomDatabaseInstance.class, DATABASE_NAME)
                        // Wipes and rebuilds instead of migrating
                        // if no Migration object.
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        return database;
    }
}
