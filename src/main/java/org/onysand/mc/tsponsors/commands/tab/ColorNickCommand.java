package org.onysand.mc.tsponsors.commands.tab;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.onysand.mc.tsponsors.utils.TimersUtil;
import org.onysand.mc.tsponsors.utils.Utils;

import java.util.Collections;
import java.util.List;

public class ColorNickCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return Utils.returnMessage(commandSender, "Только игрок может использовать эту команду", NamedTextColor.DARK_RED);

        if (args.length < 1) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("tab player %s customtabname", player.getName()));
            return Utils.returnMessage(player,
                    "Градиент ника успешно убран!\n   Если вы хотите поменять цвет, используйте команду /colornick Цвет1 Цвет2 в HEX формате!",
                    NamedTextColor.GREEN);
        }

        if (args.length == 1) {
            commandSender.sendMessage(Component.text("Цвета градиента должны быть указаны в формате HEX цвета #fff000").color(NamedTextColor.DARK_RED)
                    .append(Component.text("\n   Если вы хотите убрать цвет, используйте команду /colornick без указания значений!").color(NamedTextColor.GOLD)));
            return false;
        }

        String gradientStart = args[0].startsWith("#") ? args[0] : "#" + args[0];
        String gradientEnd = args[1].startsWith("#") ? args[1] : "#" + args[1];

        if (gradientStart.length() != 7 || gradientEnd.length() != 7) {
            commandSender.sendMessage(Component.text("Цвета градиента должны быть указаны в формате HEX цвета #fff000").color(NamedTextColor.DARK_RED)
                    .append(Component.text("\n   Если вы хотите убрать цвет, используйте команду /colornick без указания значений!").color(NamedTextColor.GOLD)));
            return false;
        }

        String currentName = "%essentials_nickname%";
        String currentPlaytimeIcon = "%rgb_#FCBA03%%playtimeicon%%rgb_#C7C8CC%";
        String newName = String.format(" %s<gradient:%s:%s>%s</gradient>", currentPlaytimeIcon, gradientStart, gradientEnd, currentName);
        TimersUtil.rgbNameTimers.put(player.getUniqueId(), System.currentTimeMillis());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("tab player %s customtabname ' %s'", player.getName(), newName));
        return Utils.returnMessage(player, "Новый цвет ника успешно установлен", NamedTextColor.GREEN);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
