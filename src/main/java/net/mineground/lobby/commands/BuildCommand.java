package net.mineground.lobby.commands;

import net.mineground.lobby.Main;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    public static ArrayList<Player> canbuild = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("lobby.build") || p.hasPermission("lobby.*") || p.isOp()) {
                if (args.length == 0) {
                    if (canbuild.contains(p)) {
                        canbuild.remove(p);
                        p.sendMessage(Main.SPrefix() + Main.LPrefix() + "§7Du hast den Bau-Modus §cverlassen");
                                 p.setGameMode(GameMode.SURVIVAL);
                        p.setAllowFlight(false);
                    } else {
                        canbuild.add(p);
                        p.sendMessage(Main.SPrefix() + Main.LPrefix() + "§7Du hast den Bau-Modus §abetreten");
                                p.setGameMode(GameMode.CREATIVE);
                    }
                } else if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (p.hasPermission("lobby.build.other") || p.hasPermission("lobby.*") || p.isOp()) {
                        if (target != null) {
                            if (canbuild.contains(target)) {
                                canbuild.remove(target);
                                target.sendMessage(Main.SPrefix() + Main.LPrefix() + ("§7Du hast den Bau-Modus §cverlassen"));
                                        p.sendMessage(Main.SPrefix() + Main.LPrefix() + "§7"+ target.getDisplayName() + "§cbefindet sich nun nicht mehr im Bau - Modus!");
                                target.setGameMode(GameMode.SURVIVAL);
                                target.setAllowFlight(false);
                            } else {
                                canbuild.add(target);
                                target.sendMessage(Main.SPrefix() + Main.LPrefix() + "§7Du hast den Bau-Modus §abetreten");
                                        p.sendMessage(Main.SPrefix() + Main.LPrefix() + target.getDisplayName() + "§a befindet sich nun im Bau - Modus!");
                                target.setGameMode(GameMode.CREATIVE);
                            }
                        } else {
                            p.sendMessage(Main.SPrefix() + Main.LPrefix() + "§cSpieler §anicht gefunden!");
                        }
                    } else {
                        p.sendMessage(Main.SPrefix() + Main.ErrorM());
                    }
                } else {
                    p.sendMessage(Main.SPrefix() + Main.LPrefix() + "§cSyntax: /build <Spieler>");
                }
            } else {
                p.sendMessage(Main.SPrefix() + Main.ErrorM());
            }
        } return false;
    }
}