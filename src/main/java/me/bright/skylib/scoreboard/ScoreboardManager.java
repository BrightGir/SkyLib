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
            //    if(sp.getInfo(ScoreboardInfo.WAITING.getKey()) == null) {
                    skelet = game.getWaitingScoreboardSkeletClass().getDeclaredConstructor(Game.class, SPlayer.class).newInstance(game, sp);
              //      sp.putInfo(ScoreboardInfo.WAITING.getKey(), skelet);
           //     } else {
            //        skelet = (ScoreboardSkelet) sp.getInfo(ScoreboardInfo.WAITING.getKey());
           //     }
                break;

                case ACTIVEGAME:
            //        if(sp.getInfo(ScoreboardInfo.ACTIVE.getKey()) == null) {
                        skelet = game.getActiveGameScoreboardSkeletClass().getDeclaredConstructor(Game.class, SPlayer.class).newInstance(game, sp);
           //             sp.putInfo(ScoreboardInfo.ACTIVE.getKey(), skelet);
             //       } else {
            //            skelet = (ScoreboardSkelet) sp.getInfo(ScoreboardInfo.ACTIVE.getKey());
             //       }
                    break;

                    case END:
                  //      if(sp.getInfo(ScoreboardInfo.END.getKey()) == null) {
                            skelet = game.getEndGameScoreboardSkeletClass().getDeclaredConstructor(Game.class, SPlayer.class).newInstance(game, sp);
                  //          sp.putInfo(ScoreboardInfo.END.getKey(), skelet);
                  //      } else {
                  //          skelet = (ScoreboardSkelet) sp.getInfo(ScoreboardInfo.END.getKey());
                  //      }
                        break;

                }
        } catch (Exception e) {
            e.printStackTrace();
        }
     //   Bukkit.getLogger().info("in method");
     //   Bukkit.getLogger().info("skelet size strings " + skelet.getStrings().size());
   //     Bukkit.getLogger().info("sp name " + sp.getPlayer().getName());
   //     Bukkit.getLogger().info("sp uuid " + sp.getPlayer().getUniqueId());
       // sp.setScoreboard(new CScoreboard("sidebar","dummy",skelet.getTitle(), sp.getPlayer()));
        sp.setScoreboard(new BScoreboard(game,sp));
        skelet.updateLines();
        sp.getScoreboard().setScoreboardSkelet(skelet);
   //     addRowsToSc(sp.getScoreboard(),skelet);
     //   sp.getScoreboard().finish();
       // sp.getScoreboard().setScoreboardSkelet(skelet);
        sp.getScoreboard().update();
    }

    private void addRowsToSc(CScoreboard scoreboard, ScoreboardSkelet skelet) {
        for(String rowMsg: skelet.getLines().values()) {
            scoreboard.addRow(rowMsg);
        }
    }

}
