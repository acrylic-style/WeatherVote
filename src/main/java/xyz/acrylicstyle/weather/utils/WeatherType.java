package xyz.acrylicstyle.weather.utils;

import org.bukkit.ChatColor;

public enum WeatherType {
    RAIN(ChatColor.BLUE + "雨", true),
    THUNDER(ChatColor.YELLOW + "雷", true, true),
    CLEAR(ChatColor.GOLD + "晴れ", false);

    private String name;
    private boolean storm;
    private boolean thunder;

    WeatherType(String name, boolean storm) {
        this(name, storm, false);
    }

    WeatherType(String name, boolean storm, boolean thunder) {
        this.name = name;
        this.storm = storm;
        this.thunder = thunder;
    }

    public boolean isStorm() {
        return storm;
    }

    public boolean isThunder() {
        return thunder;
    }

    public String getName() {
        return name;
    }
}
