package net.mineground.lobby.listeners;

import java.util.HashMap;
import net.mineground.lobby.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BlockCombat implements Listener {
    private static HashMap<Player, BukkitTask> pvp = new HashMap<>();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !Main.getConfiguration().getBoolean("disable-spawn-command-in-pvp.enabled"))
            return;
        Player p = (Player)e.getEntity();
        if (e.getDamager() instanceof Player) {
            pvp(p);
            pvp((Player)e.getDamager());
        } else if (e.getDamager() instanceof Arrow && ((Arrow)e.getDamager()).getShooter() instanceof Player) {
            pvp(p);
            pvp((Player)((Arrow)e.getDamager()).getShooter());
        }
    }

    public static boolean containsKey(Player p) {
        return pvp.containsKey(p);
    }

    public static void remove(Player p) {
        if (pvp.containsKey(p)) {
            if (((BukkitTask)pvp.get(p)).isSync())
                ((BukkitTask)pvp.get(p)).cancel();
            pvp.remove(p);
        }
    }

    private void pvp(final Player p) {
        if (pvp.containsKey(p) && (
                (BukkitTask)pvp.get(p)).isSync())
            ((BukkitTask)pvp.get(p)).cancel();
        pvp.put(p, (new BukkitRunnable() {
            public void run() {
                if (BlockCombat.pvp.containsKey(p))
                    BlockCombat.pvp.remove(p);
            }
        }).runTaskLater((Plugin)Main.getInstance(), 160L));
    }
}
