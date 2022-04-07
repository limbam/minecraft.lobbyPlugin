package net.mineground.lobby.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.mineground.lobby.Main;
import net.mineground.lobby.Utils.Utils;
import net.mineground.lobby.config.Config;
import net.mineground.lobby.config.ConfigUtil;
import org.bukkit.command.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class LobbyCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            if (Utils.hasPermission(sender, "help")) {
                sender.sendMessage("");
                sender.sendMessage("§b/spawn §7- Teleportiere zum SpawnPunkt.");
                sender.sendMessage("§b/setspawn §7- Setze SpawnPunkt.");
                sender.sendMessage("§b/" + cmd.getName() + " or /" + cmd.getName() + " help Commands list.");
                sender.sendMessage("§b/" + cmd.getName() + " info Plugin info.");
                sender.sendMessage("§b/" + cmd.getName() + " setdelay [seconds] Set spawn delay. 0 = no delay");
                sender.sendMessage("§b/" + cmd.getName() + " reload Reload config.");
            } else if (args[0].equalsIgnoreCase("setdelay")) {
                if (Utils.hasPermission(sender, "setdelay")) {
                    if (args.length == 1) {
                        sender.sendMessage("§7Aktuelle teleportverzögerung: §b" + Main.getConfiguration().getInt("teleportverzögerung in sekunden") + " sekunden§7.");
                    } else {
                        try {
                            Main.getConfiguration().set("teleportverzögerung in sekunden", Integer.valueOf(Integer.parseInt(args[1])));
                            Main.getInstance().saveConfig();
                            sender.sendMessage("Verzögerung auf:§b" + Integer.parseInt(args[1]) + " sekunden geändert§7.");
                        } catch (Exception e) {
                            sender.sendMessage("Hier funktionieren nur Zahlen.");
                        }
                    }
                } else {
                    sender.sendMessage(ConfigUtil.getNoPermission());
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (Utils.hasPermission(sender, "reload")) {
                    Main.getInstance().reloadConfig();
                    Config.testConfig();
                    if (sender instanceof org.bukkit.entity.Player)
                        Main.getInstance().getLogger().info("Config reloaded.");
                    sender.sendMessage(Utils.color(Main.getConfiguration().getString("messages.config-reloaded")));
                } else {
                    sender.sendMessage(ConfigUtil.getNoPermission());
                }
            } else if (Utils.hasPermission(sender, "help")) {
                sender.sendMessage("not found. For help use: /" + cmd.getName() + " help");
            } else {
                sender.sendMessage(ConfigUtil.getNoPermission());
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> results = new ArrayList<>();
            if (Utils.hasPermission(sender, "help") && "help".startsWith(args[0]))
                results.add("help");
            if (Utils.hasPermission(sender, "info") && "info".startsWith(args[0]))
                results.add("info");
            if (Utils.hasPermission(sender, "setdelay") && "setdelay".startsWith(args[0]))
                results.add("setdelay");
            if (Utils.hasPermission(sender, "reload") && "reload".startsWith(args[0]))
                results.add("reload");
                return results;
        }
        return Collections.emptyList();
    }
}