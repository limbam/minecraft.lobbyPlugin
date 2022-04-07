package net.mineground.lobby.listeners;

import net.mineground.lobby.Main;
import net.mineground.lobby.Utils.Spawn;
import net.mineground.lobby.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (Main.getConfiguration().getBoolean("options.set-fly-on-join.fly"))
            p.setAllowFlight(false);
        if (Main.getConfiguration().getBoolean("broadcast.player-quit.enabled")) {
            e.setQuitMessage(null);
            if (!Main.getConfiguration().getBoolean("broadcast.player-quit.hide"))
                Bukkit.broadcastMessage(Utils.color(Main.getConfiguration().getString("broadcast.player-quit.message").replaceAll("%player%", e.getPlayer().getDisplayName())));
        }
        BlockCombat.remove(p);
        Spawn.removeDelay(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }
}

