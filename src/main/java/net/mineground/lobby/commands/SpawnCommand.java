package net.mineground.lobby.commands;


import java.util.Collections;
import java.util.List;
import net.mineground.lobby.Main;
import net.mineground.lobby.Utils.Spawn;
import net.mineground.lobby.Utils.Utils;
import net.mineground.lobby.config.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (Utils.verifyIfIsAPlayer(sender))
                return true;
            Player p = (Player)sender;
            if (Main.getConfiguration().getBoolean("Dazu brauchst du andere Rechte!")) {
                if (Utils.hasPermission(p, "spawn")) {
                    Spawn.spawn(p);
                } else {
                    sender.sendMessage(ConfigUtil.getNoPermission());
                }
            } else {
                Spawn.spawn(p);
            }
            return true;
        }
        if (Utils.hasPermission(sender, "teleportothers")) {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ConfigUtil.getPlayerNotFound());
                return true;
            }
            Spawn.teleport(target, true, sender);
        } else {
            sender.sendMessage(ConfigUtil.getNoPermission());
        }
        return true;
    }

    public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && Utils.hasPermission(sender, "teleportothers"))
            return null;
        return Collections.emptyList();
    }
}