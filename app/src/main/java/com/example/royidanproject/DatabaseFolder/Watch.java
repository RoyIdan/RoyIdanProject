package com.example.royidanproject.DatabaseFolder;

import androidx.room.Entity;

@Entity(tableName = "tblWatches")
public class Watch extends Product {
    private WatchColor watchColor;
    private WatchSize watchSize;

    public WatchColor getWatchColor() {
        return watchColor;
    }

    public void setWatchColor(WatchColor watchColor) {
        this.watchColor = watchColor;
    }

    public WatchSize getWatchSize() {
        return watchSize;
    }

    public void setWatchSize(WatchSize watchSize) {
        this.watchSize = watchSize;
    }

    public enum WatchColor {
        Black, White, Green,
    }

    public enum WatchSize {
        L, M, S,
    }
}
