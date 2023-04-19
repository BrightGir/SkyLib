package me.bright.skylib.scoreboard;


import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameState;
import me.bright.skylib.scoreboard.game.ScoreboardSkelet;
import me.bright.skylib.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class BScoreboard {

    private DefaultScoreboard scoreboard;
    private Game game;
    private SPlayer player;
    private ScoreboardUpdater updater;
    private Map<Integer, String> lines;
    private ScoreboardSkelet skelet;

    public BScoreboard(Game game, SPlayer player) {
        this.player = player;
        this.game = game;
        this.scoreboard = null;
        this.updater = new ScoreboardUpdater(game.getArena().getPlugin(),this);
        this.lines = new HashMap<>();
    }

    public void setScoreboardSkelet(ScoreboardSkelet skelet) {
        if(scoreboard == null) {
          //  Bukkit.getLogger().info("create scoreboard (bscore 34)");
            scoreboard = new DefaultScoreboard(skelet.getTitle());
        }

        //scoreboard.setTitle(skelet.getTitle());
        this.skelet = skelet;
        update();
    }

    public void setGame(Game game) {
        this.game = game;
    }



    public Game getGame() {
        return game;
    }

    public DefaultScoreboard getDScoreboard() {
        return scoreboard;
    }

    public void update() {
        skelet.updateLines();
        lines = skelet.getLines();
        for(Map.Entry entry: lines.entrySet()) {
          //  Bukkit.getLogger().info("update");
            scoreboard.add(Messenger.color((String)entry.getValue()),(int)entry.getKey());
        }
        scoreboard.update();
        player.getPlayer().setScoreboard(scoreboard.getScoreboard());
    }

    public Scoreboard getScoreboard() {
        if(scoreboard == null) return null;
        return scoreboard.getScoreboard();
    }


}
