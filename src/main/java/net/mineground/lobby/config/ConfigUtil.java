package net.mineground.lobby.config;

import net.mineground.lobby.Main;
import net.mineground.lobby.Utils.Utils;

public class ConfigUtil {
    public static String getNoPermission() {
        return Utils.color(Main.getConfiguration().getString("Keine Berechtigung"));
    }

    public static String getPlayerNotFound() {
        return Utils.color(Main.getConfiguration().getString("Spieler nicht gefunden"));
    }

    public static String getConsoleUseCommand() {
        return Utils.color(Main.getConfiguration().getString("nutze commando"));
    }
}