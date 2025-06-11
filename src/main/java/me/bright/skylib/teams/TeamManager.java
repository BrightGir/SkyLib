package me.bright.skylib.teams;

import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.teams.Team;
import me.bright.skylib.teams.TeamColor;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private Game game;
    private Map<TeamColor, Team> teams;

    public TeamManager(Game game) {
        this.game = game;
        this.teams = new HashMap<>();
    }

    public void registerTeam(TeamColor color) {
        teams.put(color,new Team(color,game.getTeamSize()));
    }

    public void registerTeams(int count) {
        for(TeamColor color: TeamColor.values()) {
            if(count == 0) break;
            count--;
            teams.put(color,new Team(color,game.getTeamSize()));
        }
    }

    protected Map<TeamColor,Team> getTeamsMap() {
        return teams;
    }


    public void clearTeams() {
        teams.clear();
    }
    public void addPlayer(SPlayer player, Team team) {
        if(player.getTeam() != null) {
            player.getTeam().removePlayer(player.getPlayer());
        }
        try {
            team.addPlayer(player.getPlayer());
            player.setTeam(team);
        } catch(Exception e) {
        }
    }

    public void removeTeam(SPlayer player) {
        if(player.getTeam() != null) {
            player.getTeam().removePlayer(player.getPlayer());
        }
        player.setTeam(null);
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }

    public Team getTeam(TeamColor color) {
        return teams.get(color);
    }


}
