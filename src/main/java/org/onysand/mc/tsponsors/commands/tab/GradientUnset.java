package org.onysand.mc.tsponsors.commands.tab;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.onysand.mc.tsponsors.TSponsors;
import org.onysand.mc.tsponsors.commands.SubCommand;

public class GradientUnset implements SubCommand {
    private final TabAPI tabAPI = TSponsors.tabAPI;

    @Override
    public String getName() {
        return "unset";
    }

    @Override
    public String getDescription() {
        return "Убирает градиент в табе";
    }

    @Override
    public String getSyntax() {
        return "/tabgradient unset <player>";
    }

    @Override
    public void perform(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length <= 1) {
            commandSender.sendMessage(Component.text(getSyntax()).color(NamedTextColor.DARK_RED));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        TabPlayer tabPlayer = tabAPI.getPlayer(target.getUniqueId());
        if (tabPlayer == null) {
            commandSender.sendMessage(Component.text("Указанный игрок не найден").color(NamedTextColor.DARK_RED));
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("tab player %s customtabname", target.getName()));

        commandSender.sendMessage(MiniMessage.miniMessage().deserialize("Новый ник игрока: " + target.getName()));
    }
}
