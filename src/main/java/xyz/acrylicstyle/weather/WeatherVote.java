package xyz.acrylicstyle.weather;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import util.CollectionList;
import xyz.acrylicstyle.tomeito_api.TomeitoAPI;
import xyz.acrylicstyle.weather.commands.*;
import xyz.acrylicstyle.weather.utils.WeatherType;

import java.util.Objects;
import java.util.UUID;

public class WeatherVote extends JavaPlugin {
    public static WeatherVote instance = null;

    public static boolean voting = false;
    public static WeatherType weatherType = null;
    public static CollectionList<UUID> voteYes = new CollectionList<>();
    public static CollectionList<UUID> voteNope = new CollectionList<>();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(Bukkit.getPluginCommand("awv")).setTabCompleter(new WeatherVoteTabCompleter());
        Objects.requireNonNull(Bukkit.getPluginCommand("wv")).setTabCompleter(new WeatherVoteTabCompleter());
        TomeitoAPI.registerCommand("awv", new AllWeatherVote());
        TomeitoAPI.registerCommand("wv", new WeatherVoteCmd());
        TomeitoAPI.registerCommand("voteyes", new VoteYes());
        TomeitoAPI.registerCommand("voteno", new VoteNo());
    }

    public static void vote(Player player, boolean yes) {
        if (!voting) {
            player.sendMessage(ChatColor.RED + "現在投票は受け付けていません。");
            return;
        }
        if (voteNope.contains(player.getUniqueId()) || voteYes.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "すでに投票しています。");
            return;
        }
        if (yes) {
            voteYes.add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "「変更する」に投票しました。");
        } else {
            voteNope.add(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "「変更しない」" + ChatColor.GREEN + "に投票しました。");
        }
    }
}
