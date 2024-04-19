package org.onysand.mc.tsponsors.commands.counter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.onysand.mc.tsponsors.commands.SubCommand;
import org.onysand.mc.tsponsors.utils.Counters;
import org.onysand.mc.tsponsors.utils.Utils;

import java.util.UUID;

public class ListCounters implements SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Выводит кол-во всех полученных предметов спонсором";
    }

    @Override
    public String getSyntax() {
        return "/scount list <игрок>";
    }

    @Override
    public void perform(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length <= 1) {
            commandSender.sendMessage(Component.text(this.getSyntax()).color(NamedTextColor.DARK_RED));
            return;
        }

        if (Bukkit.getPlayerExact(args[1]) == null) {
            commandSender.sendMessage(Component.text("Указанный игрок не найден").color(NamedTextColor.DARK_RED));
            return;
        }

        UUID playerUID = Bukkit.getPlayerExact(args[1]).getUniqueId();

        TextComponent message = Component.text("=====================");
        message = message.append(Component.text("\nПолучено карт: " + Counters.gotMaps.getOrDefault(playerUID, 0)));
        message = message.append(Component.text("\nПолучено голов: " + Counters.gotHeads.getOrDefault(playerUID, 0)));
        message = message.append(Component.text("\nПолучено откатов: " + Counters.gotRollbacks.getOrDefault(playerUID, 0)));
        message = message.append(Component.text("\nПолучено пластинок: " + Counters.gotDiscs.getOrDefault(playerUID, 0)));
        message = message.append(Component.text("\n====================="));

        commandSender.sendMessage(message);
    }
}
