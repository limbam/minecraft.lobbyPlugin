package net.mineground.lobby.Utils;

import java.util.HashMap;

import net.mineground.lobby.Main;
import net.mineground.lobby.listeners.BlockCombat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Spawn {
    public static HashMap<Player, Delay> delay = new HashMap<>();

    public static void setLocation(Location l) {
        Main.getConfiguration().set("spawn.world", l.getWorld().getName());
        Main.getConfiguration().set("spawn.x", Double.valueOf(l.getX()));
        Main.getConfiguration().set("spawn.y", Double.valueOf(l.getY()));
        Main.getConfiguration().set("spawn.z", Double.valueOf(l.getZ()));
        Main.getConfiguration().set("spawn.yaw", Float.valueOf(l.getYaw()));
        Main.getConfiguration().set("spawn.pitch", Float.valueOf(l.getPitch()));
        Main.getInstance().saveConfig();
    }

    public static Location getLocation() {
        String wname = Main.getConfiguration().getString("spawn.world");
        if (wname == null || wname.equalsIgnoreCase(""))
            return null;
        World w = Bukkit.getServer().getWorld(wname);
        double x = Main.getConfiguration().getDouble("spawn.x");
        double y = Main.getConfiguration().getDouble("spawn.y");
        double z = Main.getConfiguration().getDouble("spawn.z");
        float yaw = Main.getConfiguration().getInt("spawn.yaw");
        float pitch = Main.getConfiguration().getInt("spawn.pitch");
        return new Location(w, x, y, z, yaw, pitch);
    }

    public static void teleport(Player p, boolean message, CommandSender sender) {
        Location l = getLocation();
        if (l == null) {
            Main.getInstance().getLogger().warning("Spawnpunkt nicht gesetzt.");
            p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.spawn-not-set")));
        } else {
            if (!l.getChunk().isLoaded())
                l.getChunk().load();
            p.teleport(l);
            if (sender != null || (message && Main.getConfiguration().getBoolean("spawn-command.message-enabled"))) {
                p.sendMessage(Utils.color(Main.getConfiguration().getString("spawn-command.message")));
                if (sender != null &&
                        p.getName() != sender.getName())
                    sender.sendMessage(Utils.color(Main.getConfiguration().getString("messages.teleported-other-player")).replaceAll("%target%", p.getName()));
            }
        }
    }

    public static void spawn(final Player p) {
        if (BlockCombat.containsKey(p) && !Utils.hasPermission(p, "bypasscmdblock")) {
            p.sendMessage(Utils.color(Main.getConfiguration().getString("disable-spawn-command-in-pvp.warning-message")));
        } else if (Utils.hasPermission(p, "bypassdelay") || Main.getConfiguration().getInt("teleport-delay-in-seconds") <= 0) {
            teleport(p, true, null);
        } else {
            if (delay.containsKey(p) && (
                    (Delay)delay.get(p)).getTask().isSync())
                ((Delay)delay.get(p)).getTask().cancel();
            Location l = p.getLocation();
            delay.put(p, new Delay((new BukkitRunnable() {
                public void run() {
                    if (((Delay)Spawn.delay.get(p)).getI() >= Main.getConfiguration().getInt("teleport-delay-in-seconds")) {
                        ((Delay)Spawn.delay.get(p)).setI(0);
                        Spawn.teleport(p, true, null);
                        cancel();
                    } else {
                        ((Delay)Spawn.delay.get(p)).setI(((Delay)Spawn.delay.get(p)).getI() + 1);
                        if (((Delay)Spawn.delay.get(p)).getStartX() != (int)p.getLocation().getX() || (
                                (Delay)Spawn.delay.get(p)).getStartY() != (int)p.getLocation().getY() || (
                                (Delay)Spawn.delay.get(p)).getStartZ() != (int)p.getLocation().getZ()) {
                            ((Delay)Spawn.delay.get(p)).setI(0);
                            cancel();
                            p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.player-move")));
                        }
                    }
                }
            }).runTaskTimer((Plugin)Main.getInstance(), 20L, 20L), (int)l.getX(), (int)l.getY(), (int)l.getZ()));
            p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.teleport-delay")).replaceAll("%seconds%", (new StringBuilder(String.valueOf(Main.getConfiguration().getInt("teleport-delay-in-seconds")))).toString()));
        }
    }

    public static void removeDelay(Player p) {
        if (delay.containsKey(p)) {
            if (((Delay)delay.get(p)).getTask().isSync())
                ((Delay)delay.get(p)).getTask().cancel();
            delay.remove(p);
        }
    }
}
