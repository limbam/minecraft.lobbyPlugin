// package net.mineground.lobby.Utils;

// import org.bukkit.Bukkit;
// import org.bukkit.configuration.file.FileConfiguration;
// import org.bukkit.configuration.file.YamlConfiguration;
// import org.bukkit.entity.Player;
// import org.bukkit.event.EventHandler;
// import org.bukkit.event.Listener;
// import org.bukkit.event.player.PlayerMoveEvent;
// import org.bukkit.plugin.Plugin;
// import org.bukkit.plugin.java.JavaPlugin;

// public class DeathHeight extends JavaPlugin implements Listener {
//     private YamlConfiguration yamlConfiguration;

//     public void onEnable() {
//         Bukkit.getPluginManager().registerEvents(this, this);
//         loadConfig();
//     }

//     public void loadConfig() {
//         getConfig().options().copyDefaults(true);
//         saveConfig();
//     }

//     public YamlConfiguration getYamlConfiguration() {
//         YamlConfiguration yamlConfiguration = this.yamlConfiguration;
//         return yamlConfiguration;
//     }

//     int blocks = getConfig().getInt("deathHeight");

//     @EventHandler
//     public void ondeath(PlayerMoveEvent e) {
//         Player p = e.getPlayer();
//         if (p.getLocation().getBlockY() == this.blocks)
//             p.setHealth(0.0D);
//     }
// }