package xyz.acrylicstyle.weather.commands;

import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.tomeito_core.command.PlayerCommandExecutor;

/**
 * It's actually isn't a vote
 */
public class WeatherVoteCmd extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/wv sun/thunder/rain/reset");
            return;
        }
        if (args[0].equalsIgnoreCase("sun") || args[0].equalsIgnoreCase("s")) {
            player.setPlayerWeather(WeatherType.CLEAR);
            player.sendMessage(ChatColor.GREEN + "天気を" + ChatColor.GOLD + "晴れ" + ChatColor.GREEN + "に変更しました。");
            player.sendMessage(ChatColor.YELLOW + "元に戻すには、" + ChatColor.AQUA + "/wv reset" + ChatColor.YELLOW + "を使用してください。");
        } else if (args[0].equalsIgnoreCase("rain")
                || args[0].equalsIgnoreCase("r")
                || args[0].equalsIgnoreCase("thunder")
                || args[0].equalsIgnoreCase("t")) {
            player.setPlayerWeather(WeatherType.DOWNFALL);
            player.sendMessage(ChatColor.GREEN + "天気を" + ChatColor.BLUE + "雨" + ChatColor.GREEN + "に変更しました。");
            player.sendMessage(ChatColor.YELLOW + "元に戻すには、" + ChatColor.AQUA + "/wv reset" + ChatColor.YELLOW + "を使用してください。");
        } else if (args[0].equalsIgnoreCase("reset")) {
            player.resetPlayerWeather();
            player.sendMessage(ChatColor.GREEN + "天気をリセットしました。");
        } else player.sendMessage(ChatColor.RED + "/wv sun/thunder/rain/reset");
    }
}
