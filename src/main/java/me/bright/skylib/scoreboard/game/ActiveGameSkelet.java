package me.bright.skylib.scoreboard.game;


import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;

public abstract class ActiveGameSkelet extends ScoreboardSkelet {


    public ActiveGameSkelet(Game game, SPlayer sp,String title) {
        super(game, sp,title);
    }
}
