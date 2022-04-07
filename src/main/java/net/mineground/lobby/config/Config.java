package net.mineground.lobby.config;

import java.io.File;
import java.io.IOException;

import net.mineground.lobby.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
    private Plugin player;

    public Config(Plugin player) {
        this.player = player;
    }

    public void createConfig() {
        this.player.getConfig().options().copyDefaults(true);
        this.player.saveDefaultConfig();
    }

    public void checkVersion() {
        File file1 = new File(this.player.getDataFolder(), "config.yml");
        YamlConfiguration config1;
        config1 = YamlConfiguration.loadConfiguration(file1);
        if (this.player.getDataFolder().exists() && file1.exists() &&
                !Main.getConfigVersion().equalsIgnoreCase(config1.getString("config-version"))) {
            File file2 = new File(this.player.getDataFolder(), "oldconfig.yml");
            file1.renameTo(file2);
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file1.delete();
            this.player.getLogger().warning("Alte config geloescht!");
            this.player.getLogger().warning("Neue config erstellt!");
            this.player.getLogger().warning("Die config kann jetzt angepasst werden.");
        }
    }

    public void convertOldConfig() {
        File file = new File(this.player.getDataFolder(), "oldconfig.yml");
        if (this.player.getDataFolder().exists() && file.exists() && this.player.getConfig().getString("spawn.world") == null) {
            YamlConfiguration oldconfig = YamlConfiguration.loadConfiguration(file);
            if (oldconfig.getString("spawn.world") != null) {
                this.player.getConfig().set("spawn.world", oldconfig.getString("spawn.world"));
                this.player.getConfig().set("spawn.x", Double.valueOf(oldconfig.getDouble("spawn.x")));
                this.player.getConfig().set("spawn.y", Double.valueOf(oldconfig.getDouble("spawn.y")));
                this.player.getConfig().set("spawn.z", Double.valueOf(oldconfig.getDouble("spawn.z")));
                this.player.getConfig().set("spawn.yaw", Float.valueOf((float) oldconfig.getDouble("spawn.yaw")));
                this.player.getConfig().set("spawn.pitch", Float.valueOf((float) oldconfig.getDouble("spawn.pitch")));
                this.player.saveConfig();
            }
        }
    }

    public static void testConfig() {
        int seconds = Main.getConfiguration().getInt("teleport verzögerung in sekunden");
        if (seconds >= 60 || seconds <= 0)
            Main.getConfiguration().set("teleport-delay-in-seconds", Integer.valueOf(0));
        int gamemode = Main.getConfiguration().getInt("option.gamemode on join.gamemode");
        if (gamemode != 0 && gamemode != 1 && gamemode != 2 && gamemode != 3)
            Main.getConfiguration().set("option.gamemode on join.gamemode", Integer.valueOf(0));
        Main.getInstance().saveConfig();
    }
}
