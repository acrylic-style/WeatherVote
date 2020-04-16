package xyz.acrylicstyle.weather.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.acrylicstyle.craftbukkit.CraftUtils;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;
import xyz.acrylicstyle.weather.WeatherVote;
import xyz.acrylicstyle.weather.utils.WeatherType;

import java.lang.reflect.InvocationTargetException;

public class AllWeatherVote extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player player, String[] args) {
        if (Bukkit.getWorld("world") == null) throw new NullPointerException("Couldn't find world!");
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/awv sun/thunder/rain/reset");
            return;
        }
        if (args[0].equalsIgnoreCase("sun") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("reset")) {
            startVoting(player, WeatherType.CLEAR);
        } else if (args[0].equalsIgnoreCase("rain") || args[0].equalsIgnoreCase("r")) {
            startVoting(player, WeatherType.RAIN);
        } else if (args[0].equalsIgnoreCase("thunder") || args[0].equalsIgnoreCase("t")) {
            startVoting(player, WeatherType.THUNDER);
        } else player.sendMessage(ChatColor.RED + "/awv sun/thunder/rain/reset");
    }

    private void startVoting(Player player, WeatherType weatherType) {
        if (WeatherVote.voting) {
            player.sendMessage(ChatColor.RED + "すでに別の投票が開始されています。");
            return;
        }
        WeatherVote.voteYes.add(player.getUniqueId());
        WeatherVote.voting = true;
        WeatherVote.weatherType = weatherType;
        for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.YELLOW + "が天気を" + weatherType.getName() + ChatColor.YELLOW + "に変更する投票を開始しました。");
        TextComponent text = new TextComponent();
        TextComponent yes = new TextComponent(ChatColor.YELLOW + "[" + ChatColor.GREEN + "変更する" + ChatColor.YELLOW + "]");
        yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {
                new TextComponent(ChatColor.YELLOW + "[" + ChatColor.GREEN + "変更する に投票する" + ChatColor.YELLOW + "]")
        }));
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/voteyes"));
        TextComponent no = new TextComponent(ChatColor.YELLOW + "[" + ChatColor.RED + "変更しない" + ChatColor.YELLOW + "]");
        no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {
                new TextComponent(ChatColor.YELLOW + "[" + ChatColor.RED + "変更しない" + ChatColor.GREEN + "に投票する" + ChatColor.YELLOW + "]")
        }));
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/voteno"));
        text.addExtra(yes);
        text.addExtra(" ");
        text.addExtra(no);
        Bukkit.spigot().broadcast(text);
        new BukkitRunnable() {
            @Override
            public void run() {
                WeatherVote.voting = false;
                for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                Bukkit.broadcastMessage(ChatColor.YELLOW + "投票の受付を終了しました。");
                if (WeatherVote.voteYes.size() > WeatherVote.voteNope.size()) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a「変更する」&eが&c" + WeatherVote.voteYes.size()
                            + "&e票、&c「変更しない」&eが&c" + WeatherVote.voteNope.size() + "&e票で&a「変更する」&eが多いため、天気を" + weatherType.getName() + "&eに変更します。"));
                    Bukkit.broadcastMessage(ChatColor.GRAY + "天気が反映されない人は、" + ChatColor.YELLOW + "/wv reset" + ChatColor.GRAY + "を実行してみてください。");
                    World world = Bukkit.getWorld("world");
                    if (world == null) throw new NullPointerException("Couldn't find world!");
                    try {
                        Object nmsWorld = CraftUtils.getHandle(world);
                        Object worldData = nmsWorld.getClass().getMethod("getWorldData").invoke(nmsWorld);
                        worldData.getClass().getMethod("g", int.class).invoke(worldData, weatherType.isStorm() ? 0 : 1000000);
                        worldData.getClass().getMethod("setWeatherDuration", int.class).invoke(worldData, weatherType.isStorm() ? 1000000 : 0);
                        worldData.getClass().getMethod("setThunderDuration", int.class).invoke(worldData, weatherType.isStorm() ? 1000000 : 0);
                        worldData.getClass().getMethod("setStorm", boolean.class).invoke(worldData, weatherType.isStorm());
                        worldData.getClass().getMethod("setThundering", boolean.class).invoke(worldData, weatherType.isThunder());
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a「変更する」&eが&c" + WeatherVote.voteYes.size()
                            + "&e票、&c「変更しない」&eが&c" + WeatherVote.voteNope.size() + "&e票で&c「変更しない」&eが多いため、天気は変更されませんでした。"));
                }
                WeatherVote.voteYes.clear();
                WeatherVote.voteNope.clear();
            }
        }.runTaskLater(WeatherVote.instance, 20 * 30);
    }
}
