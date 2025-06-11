package me.bright.skylib.game;

public enum GameMode {
    TEAM("Командный"),
    SOLO("Одиночный");

    String name;
    GameMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
