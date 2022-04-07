package net.mineground.lobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatClearCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player)  sender;
            if(player.hasPermission("world.cc")){
                if(args.length ==0) {
                    for(int i = 0; i <= 200; i++)
                        Bukkit.broadcastMessage((" "));
                    Bukkit.broadcastMessage("§aDer Chat wurde von §c" + player.getName() +  " §ageleert.");
                }else
                    player.sendMessage("§cBitte benutze §6/cc§c!");
            }else
                player.sendMessage("§cDazu hast du keine Rechte!");
        }

        return false;
    }
}
