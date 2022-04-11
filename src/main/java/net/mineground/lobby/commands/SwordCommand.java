package net.mineground.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SwordCommand  implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player= (Player)sender;
            sender.sendMessage(ChatColor.BLUE + "Du hast nun " + ChatColor.DARK_RED + "DAS!" + ChatColor.BLUE + " Schwert");
            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta swordMeta = sword.getItemMeta();
            swordMeta.setDisplayName(ChatColor.GREEN + "DAS " + ChatColor.GOLD + "Schwert!");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add(ChatColor.LIGHT_PURPLE + "Das Verlorene Schwert der Göttin");
            lore.add("Bist Du bereit für die " + ChatColor.GOLD + "Unendliche"+ChatColor.DARK_PURPLE+" macht?");
            lore.add(" ");
            swordMeta.setLore(lore);
            swordMeta.setUnbreakable(true);
            swordMeta.addEnchant(Enchantment.KNOCKBACK, 3, true);
            sword.setItemMeta(swordMeta);
            player.getInventory().addItem(sword);
        }else{
            sender.sendMessage(ChatColor.RED+ "Du musst ein Spieler sein!");
        }
        return false;
    }
}
