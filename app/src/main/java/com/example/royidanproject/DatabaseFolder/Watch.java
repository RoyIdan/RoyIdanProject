package com.example.royidanproject.DatabaseFolder;

import androidx.room.Entity;

@Entity(tableName = "tblWatches")
public class Watch extends Product {
    private WatchColor watchColor;
    private int watchSize;

    public WatchColor getWatchColor() {
        return watchColor;
    }

    public void setWatchColor(WatchColor watchColor) {
        this.watchColor = watchColor;
    }

    public int getWatchSize() {
        return watchSize;
    }

    public void setWatchSize(int watchSize) {
        this.watchSize = watchSize;
    }

    public enum WatchColor {
        שחור, לבן, ירוק,
    }

}
