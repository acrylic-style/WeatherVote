package xyz.acrylicstyle.weather.commands;

import org.bukkit.entity.Player;
import xyz.acrylicstyle.tomeito_core.command.PlayerCommandExecutor;
import xyz.acrylicstyle.weather.WeatherVote;

public class VoteYes extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player player, String[] args) {
        WeatherVote.vote(player, true);
    }
}
