package org.onysand.mc.tsponsors.commands.dynMap;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.onysand.mc.tsponsors.TSponsors;
import org.onysand.mc.tsponsors.utils.TimersUtil;

import java.util.List;

public class DynMapHideCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(Component.text("Только игрок может выполнять эту команду")
                    .color(NamedTextColor.DARK_RED));
            return false;
        }
        Player player = (Player) commandSender;
        TSponsors.dynmapCommonAPI.setPlayerVisiblity(player.getName(), false);
        long duration = (player.hasPermission("tsponsors.sponsorHide") ? 1440 : 10) * 6000; // time in minutes
        TimersUtil.dynMapTimers.put(player.getUniqueId(), System.currentTimeMillis() + duration);
        
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }
}
