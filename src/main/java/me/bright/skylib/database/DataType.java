package me.bright.skylib.database;

public enum DataType {
    WINS("wins"),
    GAMES("games");

    private String dataString;
    DataType(String dataString) {
        this.dataString = dataString;
    }

    String getDataString() {
        return dataString;
    }
}
