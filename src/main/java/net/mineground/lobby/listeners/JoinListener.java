package net.mineground.lobby.listeners;


import net.mineground.lobby.Main;
import net.mineground.lobby.Utils.Spawn;
import net.mineground.lobby.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class JoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("abcd", "abcd", "abcd");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6Willkommen");
        objective.getScore("").setScore(6);
        objective.getScore("§7Rang:").setScore(5);
        objective.getScore(player.hasPermission("§system.admin") ? "§4Admin" :
                (player.hasPermission("system.srdev") ? "§bSrDev" :
                        (player.hasPermission("system.srmod") ? "§cSrMod" :
                                (player.hasPermission("system.srbuild") ? "§2SrBuilder" :
                                        (player.hasPermission("system.dev") ? "§bDeveloper" :
                                                (player.hasPermission("system.cont") ? "§9Content" :
                                                        (player.hasPermission("system.mod") ? "§cModerator" :
                                                                (player.hasPermission("system.build") ? "§2Builder" :
                                                                        (player.hasPermission("system.sup") ? "§cSupporter" :
                                                                                (player.hasPermission("system.graf") ? "§5Graf" :
                                                                                        (player.hasPermission("system.herzog") ? "§3Herzog" :
                                                                                                (player.hasPermission("system.frst") ? "§6Fürst" :
                                                                                                        (player.hasPermission("system.freiherr") ? "§eFreiherr" :
                                                                                                                (player.hasPermission("system.ritter") ? "§aRitter" :
                                                                                                                        (player.hasPermission("system.p") ? "§dPremium" : "§7Spieler"))))))))))))))).setScore(4);
        objective.getScore("").setScore(3);
        objective.getScore("§7Server:").setScore(2);
        objective.getScore("§a" + player.getServer().getName()).setScore(1);

        player.setScoreboard(board);


    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String playerjoin = null;
        String joinmessage = null;
        if (Main.hasNewVersion() && (p.isOp() || Utils.hasPermission(p, "update-message")))
            p.sendMessage(Utils.color(Main.getConfiguration().getString("check-version.warning-message")));
        if (Main.getConfiguration().getBoolean("broadcast.player-join.enabled")) {
            e.setJoinMessage(null);
            if (!Main.getConfiguration().getBoolean("broadcast.player-join.hide"))
                playerjoin = "broadcast.player-join.message";
        }
        if (Main.getConfiguration().getBoolean("join-message.enabled"))
            joinmessage = "join-message.text";
        if (p.hasPlayedBefore()) {
            if (Main.getConfiguration().getBoolean("teleport-to-spawn-on.join"))
                Spawn.teleport(p, false, null);
        } else {
            if (Main.getConfiguration().getBoolean("teleport-to-spawn-on.first-join"))
                Spawn.teleport(p, false, null);
            if (Main.getConfiguration().getBoolean("broadcast.first-join.enabled"))
                playerjoin = "broadcast.first-join.message";
            if (Main.getConfiguration().getBoolean("first-join-message.enabled"))
                joinmessage = "first-join-message.text";
        }
        if (playerjoin != null)
            Bukkit.broadcastMessage(Utils.color(Main.getConfiguration().getString(playerjoin).replaceAll("%player%", p.getDisplayName())));
        if (joinmessage != null)
            for (String message : Main.getConfiguration().getStringList(joinmessage))
                p.sendMessage(Utils.color(message.replaceAll("%player%", p.getName())));
        int gm = Main.getConfiguration().getInt("options.set-gamemode-on-join.gamemode");
        if (Main.getConfiguration().getBoolean("options.set-gamemode-on-join.enabled"))
            if (gm == 0) {
                p.setGameMode(GameMode.SURVIVAL);
            } else if (gm == 1) {
                p.setGameMode(GameMode.CREATIVE);
            } else if (gm == 2) {
                p.setGameMode(GameMode.ADVENTURE);
            } else if (gm == 3) {
                p.setGameMode(GameMode.SPECTATOR);
            }
        if (Main.getConfiguration().getBoolean("options.set-fly-on-join.enabled") && gm != 3)
            p.setAllowFlight(Main.getConfiguration().getBoolean("options.set-fly-on-join.fly"));
        if (Main.getConfiguration().getBoolean("options.set-max-health-on-join"))
            p.setHealth(20.0D);
        if (Main.getConfiguration().getBoolean("options.set-max-food-level-on-join"))
            p.setFoodLevel(20);
//        Standard Join Massage deaktivieren
        e.setJoinMessage(null);

    }


}


