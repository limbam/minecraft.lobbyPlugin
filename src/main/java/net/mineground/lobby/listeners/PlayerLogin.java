package net.mineground.lobby.listeners;

import com.google.common.collect.Lists;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.mineground.lobby.Utils.IPWhitelist;
import net.mineground.lobby.config.ReflectUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.Plugin;


public class PlayerLogin implements Listener {
    public PlayerLogin() {}

    IPWhitelist plugin;
    ReflectUtils reflect = new ReflectUtils();
    Map<UUID, InetAddress> addresses = new HashMap<UUID, InetAddress>();

    public PlayerLogin(IPWhitelist plugin) {
    this.plugin =plugin;
}
    @EventHandler
    public void onPlayerChannelRegistered(PlayerRegisterChannelEvent event) {
        if (this.plugin.getConfig().getBoolean("setup", false) && event.getChannel().equals("BungeeCord")) {
            this.plugin.getConfig().set("whitelist", Lists.newArrayList((Object[])new String[] { ((InetAddress)this.addresses.get(event.getPlayer().getUniqueId())).getHostAddress() }));
            this.plugin.getConfig().set("setup", false);
            this.plugin.saveConfig();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        final InetAddress addr = event.getRealAddress();
        this.plugin.debug("Spieler " + event.getPlayer().getName() + " verbindet sich mit IP : " + addr);
        if (!this.plugin.allow(addr)) {
            if (this.plugin.getConfig().getBoolean("setup", false)) {
                addresses.put(event.getPlayer().getUniqueId(), addr);
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this.plugin, new Runnable() {
                    public void run() {
                        if (PlayerLogin.this.plugin.getConfig().getBoolean("setup", false)) {
                            event.getPlayer().kickPlayer("Server is in setup mode");
                        } else if (!PlayerLogin.this.plugin.allow(addr)) {
                            event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', PlayerLogin.this.plugin.getConfig().getString("playerKickMessage")));
                        }
                    }
                }, 20L);
            } else if (!this.plugin.allow(addr)) {
                event.setKickMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("playerKickMessage")));
                event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
            }
        }
    }
}
