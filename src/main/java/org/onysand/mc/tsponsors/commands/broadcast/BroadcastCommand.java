package org.onysand.mc.tsponsors.commands.broadcast;

import com.earth2me.essentials.libs.kyori.adventure.audience.Audience;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.libs.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.onysand.mc.tsponsors.utils.TimersUtil;

import java.util.ArrayList;
import java.util.List;

public class BroadcastCommand implements TabExecutor {
    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Component.text("Только игрок может выполнять эту команду")
                    .color(NamedTextColor.DARK_RED));
            return false;
        }

        if (args.length == 0) {
            commandSender.sendMessage(Component.text("/tbc Текст")
                    .color(NamedTextColor.DARK_RED));
            return false;
        }

        long endTime = TimersUtil.broadcastTimers.getOrDefault(player.getUniqueId(), 0L);
        if (endTime > System.currentTimeMillis()) {
            long coolDown = (endTime - System.currentTimeMillis()) / 60000;
            player.sendMessage(mm.deserialize("<color:gray>Вы сможете использовать команду снова через</color> <color:green>" + coolDown + "</color> минут"));
            return false;
        }

        Bukkit.getServer().sendMessage(mm.deserialize(String.format(
                "<gold>[</gold><aqua><dark_aqua>%s</dark_aqua></aqua><gold>]</gold> <color:gray>%s",
                PlaceholderAPI.setPlaceholders(player, "%playeroriginalname%"),
                String.join(" ", args)
        )));
        TimersUtil.broadcastTimers.put(player.getUniqueId(), System.currentTimeMillis() + 1000L * 60 * 60); // 1 hour

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
