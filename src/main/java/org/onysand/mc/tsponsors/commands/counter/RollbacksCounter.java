package org.onysand.mc.tsponsors.commands.counter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.onysand.mc.tsponsors.commands.SubCommand;
import org.onysand.mc.tsponsors.utils.Counters;
import org.onysand.mc.tsponsors.utils.Utils;

import java.util.UUID;

public class HeadsCounter implements SubCommand {
    @Override
    public String getName() {
        return "gotMaps";
    }

    @Override
    public String getDescription() {
        return "Изменяет значение полученных спонсором карт";
    }

    @Override
    public String getSyntax() {
        return "/scount maps <игрок> <значение>";
    }

    @Override
    public void perform(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length <= 2) {
            commandSender.sendMessage(Component.text(this.getSyntax()).color(NamedTextColor.DARK_RED));
            return;
        }
        if (!Utils.isNumeric(args[2])) {
            commandSender.sendMessage(Component.text("Значение может содержать только числа").color(NamedTextColor.DARK_RED));
            return;
        }

        if (Bukkit.getPlayerExact(args[1]) == null) {
            commandSender.sendMessage(Component.text("Указанный игрок не найден").color(NamedTextColor.DARK_RED));
            return;
        }

        UUID playerUID = Bukkit.getPlayerExact(args[1]).getUniqueId();
        Integer now = Counters.gotMaps.get(playerUID);
        int value = Integer.parseInt(args[2]);
        now = now == null ? 0 : now;
        int assignValue = now + value;

        Counters.gotMaps.put(playerUID, assignValue);
        commandSender.sendMessage(Component.text("Новое значение полученных карт: " + assignValue).color(NamedTextColor.DARK_GREEN));
    }
}
