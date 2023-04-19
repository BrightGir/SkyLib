package me.bright.skylib.scoreboard.game;

import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.utils.Messenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ScoreboardSkelet {

    private Map<Integer, String> lines;
    private Collection<String> stringLines;
    private Game game;
    private SPlayer sp;
    private String title;

    public ScoreboardSkelet(Game game, SPlayer sp, String title) {
        this.game = game;
        this.sp = sp;
        lines = new HashMap<>();
        stringLines = new ArrayList<>();
        this.title = title;
    }

    public Game getGame() {
        return game;
    }

    public String getTitle() {
        return title;
    }

    public SPlayer getSPlayer() {
        return sp;
    }

    public abstract void updateLines();

    public Map<Integer,String> getLines() {
        return lines;
    }

    public void setLine(String line, int score) {
        lines.put(score,Messenger.color(line));
    }

    public void removeLine(int score) {
        if (lines.containsKey(score)) {
            lines.remove(score);
        }
    }

    public Collection<String> getStrings() {
        return lines.values();
    }
}
