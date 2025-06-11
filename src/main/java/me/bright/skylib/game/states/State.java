package me.bright.skylib.game.states;

import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameState;
import org.bukkit.entity.Player;

public abstract class State {

    private Game game;

    public State(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract void startState();

    public abstract void end();

    public abstract void actionStartState();

    public abstract void endAction();

    public abstract void setDefaultStateOfPlayer(Player player);

    public abstract GameState getEnum();

    public abstract void addPlayer(Player player);

    public abstract void removePlayer(Player player);

    public abstract int getUpdateScoreboardDelay();

    protected void setDefaultStatesOfPlayers() {
        for (Player p : getGame().getPlayers().stream().map(SPlayer::getPlayer).toList()) {
            SPlayer pl = getGame().getPlayer(p);
            getGame().getScoreboardManager().setBoard(pl);
            setDefaultStateOfPlayer(p);
        }
    }

}
