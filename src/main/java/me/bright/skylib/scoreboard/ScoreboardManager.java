package me.bright.skylib.scoreboard;


import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.scoreboard.game.ScoreboardSkelet;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;

import java.lang.reflect.InvocationTargetException;

public class ScoreboardManager {

    private Game game;
    private static int i = 0;

    public ScoreboardManager(Game game) {
        this.game = game;
    }

    public void setBoard(SPlayer sp) {
        ScoreboardSkelet skelet = null;
        try {
        switch(game.getState().getEnum()) {
            case WAITING:
                    skelet = game.getWaitingScoreboardSkeletClass().getDeclaredConstructor(Game.class, SPlayer.class)
                            .newInstance(game, sp);
                break;

                case ACTIVEGAME:
                        skelet = game.getActiveGameScoreboardSkeletClass().getDeclaredConstructor(Game.class, SPlayer.class).newInstance(game, sp);
                    break;

                    case END:
                            skelet = game.getEndGameScoreboardSkeletClass().getDeclaredConstructor(Game.class, SPlayer.class).newInstance(game, sp);
                        break;

                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sp.setScoreboard(new BScoreboard(game,sp));
        skelet.updateLines();
        sp.getScoreboard().setScoreboardSkelet(skelet);
        sp.getScoreboard().update();
    }

    private void addRowsToSc(CScoreboard scoreboard, ScoreboardSkelet skelet) {
        for(String rowMsg: skelet.getLines().values()) {
            scoreboard.addRow(rowMsg);
        }
    }

}
