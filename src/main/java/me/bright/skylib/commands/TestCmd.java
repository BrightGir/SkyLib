package me.bright.skylib.commands;

import me.bright.skylib.SPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class TestCmd implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()) return false;
        if(!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        SPlayer sp = SPlayer.getPlayer(p);
        if(sp.getGame() != null) {
            sender.sendMessage("set scb");
            sp.getGame().getScoreboardManager().setBoard(sp);
            return true;
        }
        return true;
    }
}
