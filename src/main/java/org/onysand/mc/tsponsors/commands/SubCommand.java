package org.onysand.mc.tsponsors.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface SubCommand {
    public String getName();

    public String getDescription();

    public String getSyntax();

    public void perform(@NotNull CommandSender commandSender, @NotNull String[] args);
}
