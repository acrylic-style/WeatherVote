package xyz.acrylicstyle.weather;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.CollectionList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeatherVoteTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> emptyList = new ArrayList<>();
        if (args.length == 0) return Arrays.asList("sun", "thunder", "rain", "reset");
        if (args.length == 1) return filterArgsList(new CollectionList<>("sun", "thunder", "rain", "reset"), args[0]);
        return emptyList;
    }

    private CollectionList<String> filterArgsList(CollectionList<String> list, String s) {
        return list.filter(s2 -> s2.toLowerCase().startsWith(s.toLowerCase()));
    }
}
