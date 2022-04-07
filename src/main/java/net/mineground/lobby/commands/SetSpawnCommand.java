package net.mineground.lobby.commands;

import java.util.Collections;
import java.util.List;

import net.mineground.lobby.Main;
import net.mineground.lobby.Utils.Spawn;
import net.mineground.lobby.Utils.Utils;
import net.mineground.lobby.config.ConfigUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;


public class SetSpawnCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Utils.verifyIfIsAPlayer(sender))
            return true;
        Player p = (Player) sender;
        if (Utils.hasPermission(p, "setspawn")) {
            Location l = p.getLocation();
            Spawn.setLocation(l);
            p.getWorld().setSpawnLocation((int) l.getX(), (int) l.getY(), (int) l.getZ());
            Main.getInstance().getLogger().warning("Spawnpunkt erfolgreich gesetzt.");
            p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.spawn-successfully-set")));
            // p.sendMessage("&cSpawn Punkt wurde erfolgreich gesetzt!");
        } else {
            p.sendMessage(ConfigUtil.getNoPermission());
        }
        return true;
    }

    public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}