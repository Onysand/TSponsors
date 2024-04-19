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


public class GradientSet implements SubCommand {
    private final TabAPI tabAPI = TSponsors.tabAPI;


    @Override
    public String getName() {
        return "gradientSet";
    }

    @Override
    public String getDescription() {
        return "Устанавливает градиент ника в табе";
    }

    @Override
    public String getSyntax() {
        return "/tabgradient set <player> <#HEX from> <#HEX to>";
    }

    @Override
    public void perform(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length <= 3) {
            commandSender.sendMessage(Component.text(getSyntax()).color(NamedTextColor.DARK_RED));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        TabPlayer tabPlayer = tabAPI.getPlayer(target.getUniqueId());
        if (tabPlayer == null) {
            commandSender.sendMessage(Component.text("Указанный игрок не найден").color(NamedTextColor.DARK_RED));
            return;
        }

        String gradientStart = args[2].startsWith("#") ? args[2] : "#" + args[2];
        String gradientEnd = args[3].startsWith("#") ? args[3] : "#" + args[3];

        if (gradientStart.length() != 7 || gradientEnd.length() != 7) {
            commandSender.sendMessage(Component.text("Цвета градиента должны быть указаны в формате HEX цвета #fff000").color(NamedTextColor.DARK_RED));
            return;
        }

        String currentName = "%essentials_nickname%";
        String currentPlaytimeIcon = "%rgb_#FCBA03%%playtimeicon%%rgb_#C7C8CC%";
        String newName = String.format(" %s<gradient:%s:%s>%s</gradient>", currentPlaytimeIcon, gradientStart, gradientEnd, currentName);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("tab player %s customtabname ' %s'", target.getName(), newName));

        commandSender.sendMessage(MiniMessage.miniMessage().deserialize(String.format("Новый ник игрока: <gradient:%s:%s>%s", gradientStart, gradientEnd, tabPlayer.getName())));
    }
}
