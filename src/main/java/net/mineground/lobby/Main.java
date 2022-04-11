package net.mineground.lobby;

import net.mineground.lobby.Utils.Spawn;
import net.mineground.lobby.commands.*;
import net.mineground.lobby.config.Config;
import net.mineground.lobby.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
    private static Main instance;
    private static boolean new_version = false;
    private static String config_version = ("0.4");

    public void onEnable() {
        instance = this;
        Config c = new Config((Plugin) this);
        c.checkVersion();
        c.createConfig();
        c.convertOldConfig();
        Config.testConfig();
        registerCommands();
        registerListeners();
        Location lspawn = Spawn.getLocation();
        if (lspawn != null)
            lspawn.getWorld().setSpawnLocation((int) lspawn.getX(), (int) lspawn.getY(), (int) lspawn.getZ());

    }

    public void onDisable() {
        getLogger().info("Disabled!");
    }

    private void registerCommands() {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage("§cdeveloped for §6mineground.net§c");
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("cc").setExecutor(new ChatClearCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("build").setExecutor(new BuildCommand());
        getCommand("getsword").setExecutor(new SwordCommand());
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new OtherEvents(), this);
        pluginManager.registerEvents(new BlockCombat(), this);
        pluginManager.registerEvents(new PlayerQuit(), this);
        pluginManager.registerEvents(new BuildListener(), this);
    }

    public static String getConfigVersion() {
        return config_version;
    }

    public static boolean hasNewVersion() {
        return new_version;
    }


    public static FileConfiguration getConfiguration() {
        return getInstance().getConfig();
    }

    public static Main getInstance() {
        return instance;
    }

    public static String ErrorM() {
        return "§cDu hast keine Berechtigung für diesen Befehl!";
    }

    public static String SPrefix() {
        return "§8<<§6Mineground§8>>";
    }

    public static String LPrefix() {
        return "§eLobby";
    }
}
