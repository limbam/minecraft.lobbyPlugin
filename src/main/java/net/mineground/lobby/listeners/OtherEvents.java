package net.mineground.lobby.listeners;

import net.mineground.lobby.Main;
import net.mineground.lobby.Utils.Spawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
// import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class OtherEvents implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (Main.getConfiguration().getBoolean("teleport-to-spawn-on.respawn"))
            e.setRespawnLocation(Spawn.getLocation());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause().equals(EntityDamageEvent.DamageCause.VOID) && Main.getConfiguration().getBoolean("teleport-to-spawn-on.void-fall")) {
            Spawn.teleport((Player) e.getEntity(), false, null);
            e.setCancelled(true);
        }
        }
        // @EventHandler
    //     public void ondeath(PlayerMoveEvent e) {
    //         Player p = e.getPlayer();
    //         if (p.getLocation().getBlockY() == this.toString())
    //             p.setHealth(0.0D);
    // }
}

