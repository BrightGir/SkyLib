package me.bright.skylib.scoreboard;

public enum ScoreboardInfo {
    WAITING("waiting"),
    ACTIVE("active"),
    END("end");


    private String key;
    ScoreboardInfo(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
