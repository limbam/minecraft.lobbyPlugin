package net.mineground.lobby.Utils;


import com.google.common.collect.Lists;
import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.logging.Level;
import net.mineground.lobby.listeners.PlayerLogin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
//import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;

public class IPWhitelist extends JavaPlugin {
    private final List<String> bungeeips = Lists.newArrayList();

    private final List pendingList = null;

    public List getPendingList() {
        return this.pendingList;
    }

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PlayerLogin(this), this);
        reloadBukkitConfig();
        if (getConfig().getBoolean("setup", false) && (!this.bungeeips.isEmpty() || !getConfig().getStringList("whitelist").isEmpty())) {
            getConfig().set("setup", Boolean.valueOf(false));
            saveConfig();
        }
    }

    private void reloadBukkitConfig() {
        this.bungeeips.clear();
        File spigotyml = new File(getDataFolder().getParentFile().getParentFile(), "spigot.yml");
        File bukkityml = new File(getDataFolder().getParentFile().getParentFile(), "bukkit.yml");
        if (spigotyml.exists()) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(spigotyml);
            if (yamlConfiguration.getBoolean("settings.bungeecord"))
                this.bungeeips.addAll(yamlConfiguration.getStringList("settings.bungeecord-addresses"));
        } else if (bukkityml.exists()) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new File(getDataFolder().getParentFile().getParentFile(), "bukkit.yml"));
            this.bungeeips.addAll(yamlConfiguration.getStringList("settings.bungee-proxies"));
        }
    }

    public void onDisable() {
        this.bungeeips.clear();
    }

    public List<String> getBungeeIPs() {
        return this.bungeeips;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("ipwhitelist"))
            return false;
        if (!sender.hasPermission("ipwhitelist.setup")) {
            sender.sendMessage(ChatColor.RED + "Du hast nicht die Rechte um dies Auszufuehren");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(getTag() + ChatColor.AQUA + "Commands : ");
            sender.sendMessage(ChatColor.AQUA + "/ipwhitelist list [page] - Zeigt IPs auf der Liste");
            sender.sendMessage(ChatColor.AQUA + "/ipwhitelist addip <ip> - Fuegt IP zur Liste hinzu");
            sender.sendMessage(ChatColor.AQUA + "/ipwhitelist remip <ip> - Entfernt IP von der Liste");
            sender.sendMessage(ChatColor.AQUA + "/ipwhitelist reload - laedt die whitelist neu");
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            ChatPaginator.ChatPage page;
            sender.sendMessage(getTag() + ChatColor.AQUA + "Whitelisted IPs :");
            StringBuilder iplistbuff = new StringBuilder();
            for (String ip : this.bungeeips)
                iplistbuff.append(ChatColor.AQUA + ip + "n");
            for (String ip : getConfig().getStringList("whitelist"))
                iplistbuff.append(ChatColor.AQUA + ip + "n");
            if (iplistbuff.length() > 0)
                iplistbuff.deleteCharAt(iplistbuff.length() - 1);
            String iplist = iplistbuff.toString();
            if (args.length > 1) {
                page = ChatPaginator.paginate(iplist, Integer.parseInt(args[1]), 55, 8);
                sender.sendMessage(page.getLines());
            } else {
                page = ChatPaginator.paginate(iplist, 1, 55, 8);
                sender.sendMessage(page.getLines());
            }
            sender.sendMessage(ChatColor.AQUA + "Page " + page.getPageNumber() + "/" + page.getTotalPages() + ".");
            return true;
        }
        if (args[0].equalsIgnoreCase("addip")) {
            if (args.length < 2) {
                sender.sendMessage(getTag() + ChatColor.AQUA + "Command usage : ");
                sender.sendMessage(ChatColor.AQUA + "/ipwhitelist addip <ip>");
                return true;
            }
            if (!this.bungeeips.contains(args[1]) && !getConfig().getStringList("whitelist").contains(args[1])) {
                List<String> whitelist = getConfig().getStringList("whitelist");
                whitelist.add(args[1]);
                getConfig().set("whitelist", whitelist);
                getConfig().set("setup", Boolean.valueOf(false));
                saveConfig();
                sender.sendMessage(getTag() + ChatColor.AQUA + "Successfully whitelisted IP " + args[1] + "!");
                return true;
            }
            sender.sendMessage(getTag() + ChatColor.AQUA + "IP " + args[1] + " was already whitelisted!");
            return true;
        }
        if (args[0].equalsIgnoreCase("remip")) {
            if (args.length < 2) {
                sender.sendMessage(getTag() + ChatColor.AQUA + "Command usage : ");
                sender.sendMessage(ChatColor.AQUA + "/ipwhitelist remip <ip>");
                return true;
            }
            List<String> whitelist = getConfig().getStringList("whitelist");
            if (whitelist.remove(args[1])) {
                getConfig().set("whitelist", whitelist);
                saveConfig();
                sender.sendMessage(getTag() + ChatColor.AQUA + "Successfully unwhitelisted IP " + args[1] + "!");
                return true;
            }
            if (this.bungeeips.contains(args[1])) {
                sender.sendMessage(getTag() + ChatColor.AQUA + "IP " + args[1] + " is in your bukkit.yml or spigot.yml bungee-proxies. Remove it there!");
                return true;
            }
            sender.sendMessage(getTag() + ChatColor.AQUA + "IP " + args[1] + " was not whitelisted!");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            reloadBukkitConfig();
            sender.sendMessage(getTag() + ChatColor.AQUA + "Successfully reloaded config!");
            return true;
        }
        if (args[0].equalsIgnoreCase("debug")) {
            getConfig().set("debug", Boolean.valueOf(!getConfig().getBoolean("debug", false)));
            saveConfig();
            sender.sendMessage(getTag() + ChatColor.AQUA + "Debug mode : " + ChatColor.RED + getConfig().getBoolean("debug"));
            return true;
        }
        if (args[0].equalsIgnoreCase("setup")) {
            if (!getConfig().getBoolean("setup", false) && !(!this.bungeeips.isEmpty() && getConfig().getStringList("whitelist").isEmpty())) {
                sender.sendMessage(getTag() + ChatColor.RED + "Cannot enable setup mode, some IPs are already whitelisted");
                return true;
            }
            getConfig().set("setup", Boolean.valueOf(!getConfig().getBoolean("setup", false)));
            saveConfig();
            sender.sendMessage(getTag() + ChatColor.AQUA + "Setup mode : " + ChatColor.RED + getConfig().getBoolean("setup"));
            return true;
        }
        sender.sendMessage(getTag() + ChatColor.AQUA + "Commands : ");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist list [page] - List whitelisted IPs");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist addip <ip> - Add IP to whitelist");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist remip <ip> - Removes IP to whitelist");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist reload - Reload whitelist");
        sender.sendMessage(ChatColor.AQUA + "/ipwhtelist debug - Toggles debug state");
        sender.sendMessage(ChatColor.AQUA + "/ipwhitelist setup - Turn setup mode on");
        return true;
    }

    public String getTag() {
        return ChatColor.ITALIC.toString() + ChatColor.GREEN + "[" + ChatColor.AQUA + getName() + ChatColor.GREEN + "] " + ChatColor.RESET;
    }

    public boolean allow(String ip) {
        return (this.bungeeips.contains(ip) || getConfig().getStringList("whitelist").contains(ip));
    }

    public boolean allow(InetSocketAddress addr) {
        return allow(addr.getAddress().getHostAddress());
    }

    public boolean allow(InetAddress addr) {
        return allow(addr.getHostAddress());
    }

    public void whitelist(InetSocketAddress ip) {
        whitelist(ip.getAddress().getHostAddress());
    }

    public void whitelist(String ip) {
        getConfig().getStringList("whitelist").add(ip);
        saveConfig();
    }

    public void debug(String s) {
        if (getConfig().getBoolean("debug", false)) {
            getLogger().log(Level.INFO, s);
        }
    }
}


//import java.io.BufferedWriter;
////import net.mineground.lobby.config.Config;
////import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.command.ConsoleCommandSender;
////import org.bukkit.command.PluginCommand;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerLoginEvent;
//import org.bukkit.plugin.Plugin;
//import org.bukkit.plugin.PluginManager;
//import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.scheduler.BukkitRunnable;
////import org.bukkit.configuration.file.YamlConfiguration;
//
//public class IPWhitelist extends JavaPlugin implements Listener {
//    HashMap<Player, Boolean> canJoin = new HashMap<>();
//
//    public void onEnable() {
//        ConsoleCommandSender sender = Bukkit.getConsoleSender();
//        sender.sendMessage("§cdeveloped and ©opyrighted by §6Arengraf!§c");
////                File folder = new File(getDataFolder().getPath());
////        File config = new File(getDataFolder(), "config.yml");
//        String wname = Main.getConfiguration().getString("IPWhitelist");
//        if (wname == null || wname.equalsIgnoreCase(" "))
//            return;
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter(wname);
//            BufferedWriter bWriter = new BufferedWriter(writer);
//            bWriter.write("#Dev by Arengraf");
//            bWriter.newLine();
//            bWriter.write("kickMsg:");
//            bWriter.newLine();
//            bWriter.write("- &c Zutritt verweigert!");
//            bWriter.newLine();
//            bWriter.write("- &c Bitte nutze die richtige IP!");
//            bWriter.newLine();
//            bWriter.write("- &c Mineground.net");
//            bWriter.newLine();
//            bWriter.write("ip:");
//            bWriter.newLine();
//            bWriter.write("- 127.0.0.1");
//            bWriter.newLine();
//            bWriter.write("- 192.168.0.1");
//            bWriter.newLine();
//            bWriter.write("port:");
//            bWriter.newLine();
//            bWriter.write("- 25565");
//            bWriter.newLine();
//            bWriter.write("- 25577");
//            bWriter.close();
//            writer.close();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//        reloadConfig();
//        saveDefaultConfig();
//    } {
//    getConfig().options().header("Dev by Arengraf");
//    getConfig().options().copyHeader(true);
//    String[] list = { "&c Zutritt verweigert!", "&c Bitte nutze die richtige IP!", "&c Mineground.net" };
//    getConfig().addDefault("kickMsg", list);
//    List<String> ip = new ArrayList<>();
//    ip.add("127.0.0.1");
//    ip.add("192.168.0.1");
//    getConfig().addDefault("ip", ip);
//    List<String> port = new ArrayList<>();
//    port.add("25565");
//    port.add("25577");
//    getConfig().addDefault("port", port);
//    getConfig().options().copyDefaults(true);
//    saveConfig();
//    }
//    PluginManager pluginManager = Bukkit.getServer().getPluginManager();
//    public PluginManager getPluginManager() {
//        pluginManager.registerEvents(this, (Plugin) this);
//        return pluginManager;
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void join(PlayerLoginEvent e) {
//        List<String> ipProxy = getConfig().getStringList("ip");
//        List<String> port = getConfig().getStringList("port");
//        Player p = e.getPlayer();
//        this.canJoin.put(p, Boolean.valueOf(false));
//        String ip = e.getRealAddress().getHostAddress();
//        String pport = e.getHostname();
//        for (String ipP : ipProxy) {
//            if (ip.equals(ipP))
//                for (String vp : port) {
//                    if (pport.endsWith(":" + vp))
//                        this.canJoin.put(p, Boolean.valueOf(true));
//                }
//        }
//        if (!((Boolean)this.canJoin.get(p)).booleanValue()) {
//            List<String> kickMsg = getConfig().getStringList("kickMsg");
//            String msg = "";
//            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
//            for (String aKickMsg : kickMsg)
//                msg = String.valueOf(msg) + aKickMsg + "n";
//            msg = colorize(msg);
//            e.disallow(e.getResult(), msg);
//        }
//    }
//
//    private String colorize(String input) {
//        return ChatColor.translateAlternateColorCodes('§', input);
//    }
//
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (command.getName().equalsIgnoreCase("ipreload"))
//            if (sender instanceof Player) {
//                final Player p = (Player)sender;
//                if (p.hasPermission("ipw.reload")) {
//                    p.sendMessage("config...");
//                    Runnable runnable = (Runnable)(new BukkitRunnable() {
//                        public void run() {
//                            IPWhitelist.this.reloadConfig();
//                            IPWhitelist.this.saveDefaultConfig();
//                            p.sendMessage("complete !");
//                        }
//                    }).runTaskLater((Plugin)this, 10L);
//                }
//            } else {
//                getLogger().info("Reload config...");
//                Runnable runnable = (Runnable)(new BukkitRunnable() {
//                    public void run() {
//                        IPWhitelist.this.reloadConfig();
//                        IPWhitelist.this.saveDefaultConfig();
//                        IPWhitelist.this.getLogger().info("Reload complete !");
//                    }
//                }).runTaskLater((Plugin)this, 10L);
//            }
//        return super.onCommand(sender, command, label, args);
//    }
//}
//
