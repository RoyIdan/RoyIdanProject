package com.example.royidanproject.DatabaseFolder;

import androidx.room.Entity;

@Entity(tableName = "tblWatches")
public class Watch extends Product {
    private WatchColor watchColor;
    private WatchSize watchSize;


    public enum WatchColor {
        Black, White, Green,
    }

    public enum WatchSize {
        L, M, S,
    }
}
