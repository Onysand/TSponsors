package org.onysand.mc.tsponsors.commands.bighead;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.onysand.mc.tsponsors.TSponsors;

import java.util.ArrayList;
import java.util.List;

public class BigHeadCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(Component.text("Только игрок может выполнять данную команду")
                    .color(NamedTextColor.DARK_RED));
            return false;
        }

        Player player = (Player) commandSender;
        ItemStack itemInOff = player.getInventory().getItemInOffHand();
        if (itemInOff.getType() != Material.AIR) {
            player.sendMessage(Component.text("Левая рука должна быть пустой"));
            return false;
        }

        player.getInventory().setItemInOffHand(getHead(Bukkit.getOfflinePlayer(player.getUniqueId())));

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }

    private ItemStack getHead(OfflinePlayer player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.displayName(Component.text("Гигамозг")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));
        ArrayList<Component> lore = new ArrayList<>();
        skull.lore(lore);
        skull.setOwningPlayer(player);
        skull.getPersistentDataContainer().set(TSponsors.bigHeadKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(skull);
        return item;
    }
}
